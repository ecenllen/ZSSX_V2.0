package com.gta.zssx.mobileOA.view.page;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.bigkoo.pickerview.TimePickerView;
import com.gta.utils.resource.Toast;
import com.gta.zssx.AppConfiguration;
import com.gta.zssx.R;
import com.gta.zssx.ZSSXApplication;
import com.gta.zssx.fun.personalcenter.model.bean.UserBean;
import com.gta.zssx.mobileOA.model.bean.Schedule;
import com.gta.zssx.mobileOA.presenter.SchedulePlanPresenter;
import com.gta.zssx.mobileOA.view.SchedulePlanView;
import com.gta.zssx.mobileOA.view.adapter.CalendarAdapter;
import com.gta.zssx.mobileOA.view.adapter.ScheduleAdapter;
import com.gta.zssx.mobileOA.view.base.BaseOAFragment;
import com.gta.zssx.pub.util.LogUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

/**
 * Created by lan.zheng on 2016/10/26.
 */
public class SchedulePlanFragment extends BaseOAFragment<SchedulePlanView,SchedulePlanPresenter> implements  SchedulePlanView,View.OnClickListener, View.OnTouchListener,GestureDetector.OnGestureListener, AdapterView.OnItemClickListener {
    private GestureDetector mGestureDetector = null;
    private CalendarAdapter calendarAdapter;
    private ScheduleAdapter scheduleAdapter;
//    private SchedulePlanAdapter scheduleAdapter;
    private ViewFlipper mFlipper;
    private GridView gridView = null;
    private TextView mMonthTextView;  //显示年月的
    private TextView mAddTextView;  //添加日程
//    private SwipeListView mListView;
    private SwipeMenuListView mListView;
    private List<Schedule> selectDateScheduleList;

    private int state = 0;// 用于记录你是跳转到下一月(1)还是上一月(-1)，本月为0。
    private static int jumpMonth = 0; // 每次滑动，增加或减去一个月,默认为0（即显示当前月）
    private static int jumpYear = 0; // 滑动跨越一年，则增加或者减去一年,默认为0(即当前年)

    private List<Schedule> list;//所有的日程集合
    public boolean isFilp = false;  //是否是滑动到别的月（滑动用于判断要显示第一天数据）
    public boolean isFirstTimeGetServerTime  = true;
    public int currentPressedPositon = -1;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);

    private int year_c = 0;   // 当前年
    private int month_c = 0;  // 当前月
    private int day_c = 0;    // 当前日期
    private int position_c = 0;  //当前位置i
    String currentDate = "";  //当前时间
    private boolean isNeedTheMonthFirstDay = false;  //是否需要显示每月第一天
    private boolean isGetDataByOnResume = false;     //是否开启Resume监听，此开关避免初始化日程报错

    @NonNull
    @Override
    public SchedulePlanPresenter createPresenter() {
        return new SchedulePlanPresenter();
    }

    private View view;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_schedule_plan, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.attachView(this);
        initView();
        initData();
    }

    @Override
    public void onResume() {
        super.onResume();
        if(mActivity.getOpenOnResume()){
            startSetDataOnResume();
        }
    }

    /**
     * 设置日历显示--onResume()
     */
    public void startSetDataOnResume() {
        LogUtil.Log("lenita1","onResume isGetDataByOnResume = "+isGetDataByOnResume);
        isGetDataByOnResume = true;
        String year = calendarAdapter.getShowYear();
        String month = calendarAdapter.getShowMonth();

        if (month.startsWith("0") && month.length() == 2) {
            month = month.substring(1, month.length());
        }
        setData(Integer.parseInt(year), Integer.parseInt(month));
    }

    /**
     * 设置日历显示--正常启动
     */
    public void startSetData(boolean needTheMonthFirstDay) {
        isNeedTheMonthFirstDay = needTheMonthFirstDay;
        String year = calendarAdapter.getShowYear();
        String month = calendarAdapter.getShowMonth();

        if (month.startsWith("0") && month.length() == 2) {
            month = month.substring(1, month.length());
        }
        mActivity.setOpenOnResume(true); //开启onResume()的重载
        setData(Integer.parseInt(year), Integer.parseInt(month));
    }

    /**
     *  去服务器获取该月分的内容
     * @param year
     * @param month
     */
    private void setData(int year, int month) {
        String beginDate = null;
        String endDate = null;
        //根据年月获得开始时间和结束时间
        List<String> dateList = presenter.getDateData(year,month);
        beginDate = dateList.get(0);
        endDate = dateList.get(1);

        //TODO ===========假数据=========
        /*List<Schedule> all = presenter.testData();
        list = increase(all);
        if(isGetDataByOnResume){
            isGetDataByOnResume = false;
            divideSchedulesByOnResume(list);
        }else {
            divideSchedules(list);
        }*/
        //======================================
        LogUtil.Log("lenita1","beginDate = "+beginDate+",endDate =  "+endDate);
        //TODO 获取服务器ScheduleList网络请求
        presenter.getScheduleListData(beginDate,endDate);
    }

    private void initView(){
        mMonthTextView = (TextView)view.findViewById(R.id.schedule_month_tv);
        mAddTextView =(TextView) view.findViewById(R.id.schedule_add_tv);
        mAddTextView.setOnClickListener(this);
        mFlipper = (ViewFlipper)view.findViewById(R.id.schedule_flipper);  //用于日历
        mListView = (SwipeMenuListView) view.findViewById(R.id.schedule_listview);   //用于日程显示
        setListener();  //设置标题栏的监听 “今天”和“年月”
        // 当期日期"yyyy-MM-dd",先获取到手机时间
        currentDate = sdf.format(new Date());
        String[] array = currentDate.split("-");
        String year = array[0];
        String month = array[1];
        String day = array[2];
        year_c = Integer.parseInt(year);
        month_c = presenter.noZero(month);
        day_c = presenter.noZero(day);
    }


    private void initData(){
        mGestureDetector = new GestureDetector(getActivity(), this);
        scheduleAdapter = new ScheduleAdapter( getActivity(),listener, null);
        selectDateScheduleList = new ArrayList<>();
        mListView.setAdapter(scheduleAdapter);
        initSwipeMenu();
        initListener();
        presenter.getServerTime();
    }

    ScheduleAdapter.ScheduleOperationListener listener = new ScheduleAdapter.ScheduleOperationListener() {

        @Override
        public void updateStatusSchedule(int id, int status) {
            //TODO 更新日程，完毕后数据会刷新此时“未完成”和“已完成”数据会转换
            LogUtil.Log("lenita","updateStatusSchedule id = "+ id);
            presenter.changeScheduleStatus(id,status);
        }

        @Override
        public void editSchedule(Bundle bundle) {
            int scheduleId  = bundle.getInt("ScheduleId",-2);
            Log.d("lenita","编辑日程,已完成的日程也可以编辑，只是不能变为未完成状态了，所以还是要通过id来拿数据"+scheduleId);
            if(scheduleId > 0){
                //先去服务器拿到数据
                presenter.getSingleScheduleById(scheduleId);
            }else {
                //如果是传schedule实体，进入编辑日程--NewScheduleActivity
                Intent intent = new Intent(mActivity,NewScheduleActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }

        }
    };

    @Override
    public void getServerDate(String date, int year, int month, int day) {
        if(!TextUtils.isEmpty(date)){
            year_c = year;
            month_c = month;
            day_c = day;
            currentDate = date;
        }
        //设置日历适配
        calendarAdapter = new CalendarAdapter(mActivity,this, jumpMonth,jumpYear, year_c, month_c, day_c);
        calendarAdapter.setScheduleData(null, null);
        position_c = day_c+calendarAdapter.getMonthStartPosition()-1;
        dayClick = day_c+calendarAdapter.getMonthStartPosition()-1;
        LogUtil.Log("lenita","first dayClick = "+dayClick);
        setCalender();  //设置日历
    }

    private void setCalender(){
        mFlipper.removeAllViews();
        jumpMonth = 0;
        jumpYear = 0;

        //第一次设置gridview
        gridView = (GridView) getActivity().getLayoutInflater().inflate(R.layout.view_schedule_gridview, null);
        gridView.setOnItemClickListener(SchedulePlanFragment.this);
        gridView.setOnTouchListener(SchedulePlanFragment.this);
        gridView.setAdapter(calendarAdapter);

        mFlipper.addView(gridView, 0);
        //无论是否获得服务器时间,都显示出来时间
        LogUtil.Log("lenita","date = "+month_c+"-"+day_c);
        setTitleYearMonth();

        //设置初始数据,显示服务器的日期数据
        startSetData(false);
    }

    private void inflaterGridview() {
        gridView = (GridView) getActivity().getLayoutInflater().inflate(
                R.layout.view_schedule_gridview, null);
        gridView.setOnItemClickListener(this);
        gridView.setOnTouchListener(this);
    }

    @Override
    public void getScheduleList(List<Schedule> all) {
        //正常的情况去显示列表
        list = increase(all);
        if(isGetDataByOnResume){
            //Fragment OnResume方法启动时，获取是当前的位置的数据
            isGetDataByOnResume = false;
            divideSchedulesByOnResume(list);
        }else {
            //正常的情况
              divideSchedules(list);
        }
    }

    @Override
    public void deleteLocalSchedules(int id) {
        Toast.Short(getActivity(),"删除日程成功");
        Iterator<Schedule> it = list.iterator();
        while(it.hasNext()){
            Schedule e = it.next();
            if(e.getId() == id){
                it.remove();
            }
        }
        divideSchedulesByDeleteOrUpdate(list);
    }

    @Override
    public void updateLocalSchedules(int id, int status) {
        Toast.Short(getActivity(),"更新日程状态成功");
        for (int i = 0; i < list.size(); i++) {
            Schedule s = list.get(i);
            if (s.getId() == id) {
                s.setStatus(status);
            }
        }
        divideSchedulesByDeleteOrUpdate(list);
    }

    @Override
    public void getSingleSchedulesInfo(boolean isSuccess, int scheduleId, Schedule schedule) {
        Bundle bundle = new Bundle();
        if(isSuccess){
            bundle.putSerializable("Schedule", schedule);
            Intent intent = new Intent(mActivity,NewScheduleActivity.class);
            intent.putExtras(bundle);
            startActivity(intent);
        }else {
            //获取单条记录失败，能进入编辑页面，但是让保存按钮置灰和删除按钮置隐藏
            bundle.putSerializable("ScheduleId", scheduleId);  //id不为负数，进入这里证明获取服务器已经失败
            Intent intent = new Intent(mActivity,NewScheduleActivity.class);
            intent.putExtras(bundle);
            startActivity(intent);
        }

    }

    private void divideSchedulesByDeleteOrUpdate(List<Schedule> all){
        ArrayList<Schedule> finished= presenter.divideFinished(all);
        ArrayList<Schedule> unfinished = presenter.divideUnfinished(all);

        // 划分完毕后显示日历和日程信息
        SetCalendarData(finished, unfinished);
        //设置日程变化
        setScheduleData(calendarAdapter.getDateByClickItem(currentPressedPositon));
    }

    public List<Schedule> increase(List<Schedule> schedules) {
        List<Schedule> all = new ArrayList<Schedule>();

        Iterator<Schedule> it = schedules.iterator();
        while(it.hasNext()){
            Schedule e = it.next();

            if(e.getRemind() == 5 || e.getRemind() == 1){
                //再插入一个小集合
                List<Schedule> small = presenter.clone(e,calendarAdapter.getShowYear()+"-"+calendarAdapter.getShowMonth()+"-"+"01",year_c,month_c);
                all.addAll(small);
                it.remove();
            }
        }

        all.addAll(schedules);
        return all;
    }

    /**
     * 根据status，将服务器返回的List总集合，分成未完成和已完成的两个集合
     */
    protected void divideSchedules(List<Schedule> all) {
        ArrayList<Schedule> finished= presenter.divideFinished(all);
        ArrayList<Schedule> unfinished = presenter.divideUnfinished(all);

        // 划分完毕后显示日历和日程信息
        SetCalendarData(finished, unfinished);
        int testDay = 0;
        if(isFirstTimeGetServerTime){
            testDay = day_c;
            setScheduleData(day_c);
            isFirstTimeGetServerTime = false;
        }else {
//            setScheduleData(calendarAdapter.getDateByClickItem(currentPressedPositon));
            if(isNeedTheMonthFirstDay){
                //滑动月份/年月选择,需要第一天数据
                int firstDay = calendarAdapter.getMonthFirstDay();
                testDay = calendarAdapter.getDateByClickItem(firstDay);
                setScheduleData(calendarAdapter.getDateByClickItem(firstDay));
            }else {
                //当进入的是点击“今天”，不需要获得第一天数据
                testDay = day_c;
                setScheduleData(day_c);
            }
        }
        LogUtil.Log("lenita","testDay = "+testDay);
    }

    /**
     * 用于重载数据回来的时候
     * @param all
     */
    protected void divideSchedulesByOnResume(List<Schedule> all) {
        ArrayList<Schedule> finished= presenter.divideFinished(all);
        ArrayList<Schedule> unfinished = presenter.divideUnfinished(all);

        // 划分完毕后显示日历和日程信息
        SetCalendarData(finished, unfinished);
        setScheduleData(calendarAdapter.getDateByClickItem(currentPressedPositon));
    }

    /**
     * 首次进入和滑动月份都开始于此函数
     * @param finished
     * @param unfinished
     */
    private void SetCalendarData(List<Schedule> finished, List<Schedule> unfinished) {
        ArrayList<Integer> startFinished = new ArrayList<Integer>(); //已完成
        ArrayList<Integer> startUnfinished = new ArrayList<Integer>(); //未完成

        // 对于已完成的日程
        for (Schedule schedule : finished) {
            int start = presenter.parseDate(schedule.getStartTime());
            startFinished.add(start);
        }
        // 对于未完成的日程
        for (Schedule s : unfinished) {
            int startTime = presenter.parseDate(s.getStartTime());
            startUnfinished.add(startTime);
        }

        calendarAdapter.setScheduleData(startFinished, startUnfinished);
        calendarAdapter.notifyDataSetChanged();
    }

    /**
     *
     * 根据当前的日期，从集合中获取相关日程数据
     * date是你要显示的该天的日程。
     */
    private void setScheduleData(int date) {
        // date 如2015-01-15 由于本次的总的日程中只有本月的，所以只需匹配day
        int currentDay = date;
        ArrayList<Schedule> currentSchedules = new ArrayList<Schedule>();
        /**
         * 注意：只以开始时间为比较的标准
         */
        if (list == null ) {
            return;
        }
        for (Schedule schedule : list) {
            int start = presenter.parseDate(schedule.getStartTime());
            if (currentDay == start) {
                currentSchedules.add(schedule);
            }
        }
        mListView.hideTouchView();  //先恢复收起状态，否则影响后面操作
        scheduleAdapter.setData(currentSchedules);
        scheduleAdapter.notifyDataSetChanged();
        if (day_c == date) {
            //保存本月今日的所有日程，用于日程提醒，写入Application中
            ZSSXApplication.instance.setTodayScheTemp(currentSchedules);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.schedule_add_tv:
                LogUtil.Log("lenita","添加新的日程");
                Bundle bundle = new Bundle();
                bundle.putInt("ScheduleId", -2); //Id为负数时新建日程
                Intent intent = new Intent(mActivity,NewScheduleActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    /**
     * 当前月
     */
    private void enterCurrentMonth() {
        isNeedTheMonthFirstDay = false;  //跳转显示第一天数据
        jumpMonth = 0;
        jumpYear = 0;

        inflaterGridview();
        mFlipper.removeAllViews();

        calendarAdapter.setData(jumpMonth, jumpYear, year_c, month_c);
        this.currentPressedPositon =-1;
        gridView.setAdapter(calendarAdapter);
        setTitleYearMonth();
        mFlipper.addView(gridView, 0);

        if (state < 0) {
            mFlipper.setInAnimation(AnimationUtils.loadAnimation(getActivity(),
                    R.anim.push_left_in));
            mFlipper.setOutAnimation(AnimationUtils.loadAnimation(getActivity(),
                    R.anim.push_left_out));
            mFlipper.showNext();
        } else if (state > 0) {
            mFlipper.setInAnimation(AnimationUtils.loadAnimation(getActivity(),
                    R.anim.push_right_in));
            mFlipper.setOutAnimation(AnimationUtils.loadAnimation(getActivity(),
                    R.anim.push_right_out));
            mFlipper.showPrevious();
        }

        state = 0;
        setData(year_c, month_c);
    }

    /**
     * 移动到上一个月
     */
    private void enterPrevMonth(int gvFlag) {

        state--;
        inflaterGridview();// 添加一个gridView
        jumpMonth--; // 上一个月
        calendarAdapter.setData(jumpMonth, jumpYear, year_c, month_c);
        isFilp = true;
        calendarAdapter.setPressedPosition(currentPressedPositon);
        gridView.setAdapter(calendarAdapter);
        setTitleYearMonth();
        mFlipper.addView(gridView, gvFlag);
        mFlipper.setInAnimation(AnimationUtils.loadAnimation(getActivity(),R.anim.push_right_in));
        mFlipper.setOutAnimation(AnimationUtils.loadAnimation(getActivity(),	R.anim.push_right_out));
        mFlipper.showPrevious();
        mFlipper.removeViewAt(0);
        dayClick = calendarAdapter.getMonthStartPosition();
        LogUtil.Log("lenita","enterPrevMonth dayClick = "+dayClick);
        startSetData(true);  //需要第一天数据
    }

    /**
     * 移动到下一个月
     */
    private void enterNextMonth(int gvFlag) {
        state++;

        inflaterGridview();
        jumpMonth++; // 下一个月

        calendarAdapter.setData(jumpMonth, jumpYear, year_c, month_c);
        isFilp = true;
        calendarAdapter.setPressedPosition(currentPressedPositon);
        gridView.setAdapter(calendarAdapter);

        setTitleYearMonth();
        mFlipper.addView(gridView, gvFlag);
        // 设置 过场动画
        mFlipper.setInAnimation(AnimationUtils.loadAnimation(getActivity(),
                R.anim.push_left_in));
        mFlipper.setOutAnimation(AnimationUtils.loadAnimation(getActivity(),
                R.anim.push_left_out));
        mFlipper.showNext();
        mFlipper.removeViewAt(0);
        dayClick =calendarAdapter.getMonthStartPosition();
        LogUtil.Log("lenita","enterNextMonth dayClick = "+dayClick);
        startSetData(true);//需要第一天数据
    }

    public TimePickerView mTimePickerView;
    int nowShowYear = 0;  //初始默认，选择后的年,用于第二次选择的判断，如果和下次选择一样，返回
    int nowShowMonth = 0; //初始默认，选择后的月,用于第二次选择的判断，如果和下次选择一样，返回
    /**
     * 年月选择器
     */
    private void selectYearMonth() {
        mTimePickerView = new TimePickerView(mActivity, TimePickerView.Type.YEAR_MONTH);
        SimpleDateFormat simpleDateFormat =  new SimpleDateFormat("yyyy年MM月");
        String yearMonth = mMonthTextView.getText().toString();
        Date lDate = null;
        try {
            lDate = simpleDateFormat.parse(yearMonth);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar calendar  = Calendar.getInstance();
        calendar.setTime(lDate);
        nowShowYear = calendar.get(Calendar.YEAR);
        nowShowMonth = presenter.noZero(String.valueOf(calendar.get(Calendar.MONTH)+1));
        mTimePickerView.setTime(lDate);
        mTimePickerView.setCyclic(true);
        mTimePickerView.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date) {
                calendar.setTime(date);
                int selectYear = calendar.get(Calendar.YEAR);
                int selectMonth = presenter.noZero(String.valueOf(calendar.get(Calendar.MONTH)+1));
                LogUtil.Log("lenita","nowShow = "+nowShowYear+"-"+nowShowMonth+", select = "+selectYear+"-"+selectMonth);
                if(selectYear == nowShowYear && selectMonth == nowShowMonth){
                    return;  //表明选择年月没变，不需要改变界面
                }
                //跳转到对应的月份的操作,相当于滑动，默认定位第一天
                mMonthTextView.setText(selectYear+"年"+selectMonth+"月");
                if(selectYear == year_c && selectMonth == month_c){
                    state = 0; //如果正好要滑动的是到本年本月，state值变回原来的0
                }
                isFilp = true;
                enterSelectedMonth(selectYear,selectMonth);
            }
        });
        mTimePickerView.show();
    }

    protected void enterSelectedMonth(int targetYear, int targetMonth) {
        isNeedTheMonthFirstDay = true;
        if (year_c == targetYear) {
            jumpMonth = targetMonth - month_c;
        }else if(targetYear < year_c) {
            jumpMonth = -((12-targetMonth)+(year_c- 1-targetYear)*12+month_c);
        }else if (year_c < targetYear) {
            jumpMonth = (12-month_c)+(targetYear-1-year_c)*12+targetMonth;
        }

        //只操作 jumpMonth
        inflaterGridview();
        mFlipper.removeAllViews();
        calendarAdapter.setData(jumpMonth, jumpYear, year_c, month_c);
        calendarAdapter.setPressedPosition(currentPressedPositon);
        gridView.setAdapter(calendarAdapter);
        setTitleYearMonth();  //设置首部时间日期
        mFlipper.addView(gridView, 0);

        if (targetYear < year_c) {
            //负数
            state = state-((12-targetMonth) + (year_c-targetYear-1)*12 + month_c);
        }else if (year_c == targetYear) {
            state = state+(targetMonth -month_c);
        }else if (year_c < targetYear) {
            //正数
            state = state+(12-month_c) + (targetYear-year_c-1)*12 + targetMonth;
        }
        dayClick =calendarAdapter.getMonthStartPosition();
        LogUtil.Log("lenita","enterSelectedMonth dayClick = "+dayClick);
        setData(targetYear, targetMonth);
    }

    private void setTitleYearMonth(){
        StringBuffer textDate = new StringBuffer();
        textDate.append(calendarAdapter.getShowYear()).append("年")
                .append(calendarAdapter.getShowMonth()).append("月")
                .append("\t");
        mMonthTextView.setText(textDate);
    }

    //    int lastClickPosition = 0;
    int dayClick = 0;  //用于判断是不是点击同一个位置
    /**
     * 点击某一日期
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        isFilp = false;
        calendarAdapter.setPressedPosition(position);
        calendarAdapter.notifyDataSetChanged();

        int monthStart=calendarAdapter.getMonthStartPosition();
        int monthEnd=calendarAdapter.getMonthEndPosition();
        LogUtil.Log("lenita","原来标识位置dayClick = "+ dayClick);
        if(dayClick == position){
            LogUtil.Log("lenita","点击的还是本条目"+ position);
            return;  //无法确定是不是同条目，有可能换了一日期，但是位置还是一样
        }
        dayClick = position;
        if (position < monthEnd && position >= monthStart) {
            // 点击任何一个item，得到这个item的日期,非该月第一天的数据
            int nowClick = calendarAdapter.getDateByClickItem(position);  //获得点击位置
            setScheduleData(nowClick);
//            scheduleAdapter.notifyDataSetChanged();
        }else{
            mListView.hideTouchView();  //TODO 先恢复收起状态，否则影响后面操作
            scheduleAdapter.setData(null);
            scheduleAdapter.notifyDataSetChanged();
        }
    }

    //==================
    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {}

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {}
    //========================
    /**
     * 滑动时切换月份
     * @param e1
     * @param e2
     * @param velocityX
     * @param velocityY
     * @return
     */
    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        int gvFlag = 0; // 每次添加gridview到viewflipper中时给的标记
        if (e1.getX() - e2.getX() > 30) {
            gvFlag++;
            enterNextMonth(gvFlag);
            return true;
        } else if (e1.getX() - e2.getX() < -30) {
            gvFlag++;
            enterPrevMonth(gvFlag);
            return true;
        }
        return false;
    }

    private float dowmX, dowmY;
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                dowmX = event.getX();
                dowmY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                if (Math.abs(dowmX - event.getX()) < Math.abs(dowmY - event.getY())) {
                    return true;
                }
                if (Math.abs(dowmY - event.getY()) > 13) {
                    return true;
                }
                break;
            default:
                break;
        }
        return mGestureDetector.onTouchEvent(event);
    }

    //设置“今天”和“年月”的监听
    public void setListener(){
        mActivity.setTodayTextViewListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isFilp = false;   //是否是前后滑动，滑动默认选中1号，这个为判断标识
                dayClick = position_c;  //今天所在的位置
                if (state == 0) {
                    //颜色变为今天,非显示该月第一天数据
                    isNeedTheMonthFirstDay = false;
                    calendarAdapter.setPressedPosition(calendarAdapter.getTodayPosition());
                    calendarAdapter.notifyDataSetChanged();
                    setScheduleData(day_c);
                    scheduleAdapter.notifyDataSetChanged();
                    return;
                }
                enterCurrentMonth();
            }
        });
        mActivity.setSelectYearTextViewListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectYearMonth();
            }
        });
    }

    public void initSwipeMenu(){
        // step 1. create a MenuCreator
        SwipeMenuCreator creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(getActivity());
                // set item background
                deleteItem.setBackground(getResources().getDrawable(R.color.delete_red));
                // set item width
                deleteItem.setWidth(dp2px(70));//这里设定宽度为80，可以改
                // set item title
                deleteItem.setTitle("删除");
                // set item title fontsize
                deleteItem.setTitleSize(16);
                //set icon
                deleteItem.setIcon(R.drawable.schedule_ic_del);
                // set item title font color
                deleteItem.setTitleColor(Color.WHITE);
                // add to menu
                menu.addMenuItem(deleteItem);
            }
        };
        // set creator
        mListView.setMenuCreator(creator);
    }

    private void initListener(){
        // step 2. listener item click event
        mListView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                selectDateScheduleList.clear();
                selectDateScheduleList.addAll(scheduleAdapter.getSchedule());
                Schedule item = selectDateScheduleList.get(position);
                switch (index) {
                    case 0:
                        // delete
                        presenter.popupConfirmDialog(getActivity(), "是否确认删除该日程？", new SchedulePlanPresenter.DeleteListener() {
                            @Override
                            public void sureDelete() {
                                LogUtil.Log("lenita","delete id ="+item.getId());
                                presenter.deleteSchedule(item.getId());
                            }
                        });
                        break;
                }
                return false;
            }
        });
        // set SwipeListener
//        mListView.setOnSwipeListener();
        // set MenuStateChangeListener
//        mListView.setOnMenuStateChangeListener();
        // other setting
//		  listView.setCloseInterpolator(new BounceInterpolator());
//        mListView.setOnItemLongClickListener();
    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }
}
