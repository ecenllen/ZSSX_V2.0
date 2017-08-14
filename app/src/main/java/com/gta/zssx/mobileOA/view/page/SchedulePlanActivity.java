package com.gta.zssx.mobileOA.view.page;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
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
import com.gta.zssx.pub.base.BaseActivity;
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
 * Created by lan.zheng on 2016/10/17.
 */
@Deprecated
public class SchedulePlanActivity extends BaseActivity<SchedulePlanView,SchedulePlanPresenter>
        implements SchedulePlanView,View.OnClickListener,GestureDetector.OnGestureListener, AdapterView.OnItemClickListener, View.OnTouchListener {

    private GestureDetector mGestureDetector = null;
    private CalendarAdapter calendarAdapter;
    private ScheduleAdapter scheduleAdapter;
//    private SchedulePlanAdapter scheduleAdapter;
    private ViewFlipper mFlipper;
    private GridView gridView = null;
    private TextView mTodayTextView;
    private TextView mMonthTextView;
//    private ListView mListView;
    private SwipeMenuListView mListView;
    private List<Schedule> selectDateScheduleList;

    private int state = 0;// 用于记录你是跳转到下一月(1)还是上一月(-1)，本月为0。
    private static int jumpMonth = 0; // 每次滑动，增加或减去一个月,默认为0（即显示当前月）
    private static int jumpYear = 0; // 滑动跨越一年，则增加或者减去一年,默认为0(即当前年)

    private List<Schedule> list;//所有的日程集合
    public boolean isFilp = false;  //是否是滑动到别的月
    public boolean isFirstTimeGetServerTime  = true;
    public int currentPressedPositon = -1;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);

    private int year_c = 0;   // 当前年
    private int month_c = 0;  // 当前月
    private int day_c = 0;    // 当前日期
    private int positon_c = 0;  //当前位置
    String currentDate = "";  //当前时间
    private boolean isNeedTheMonthFirstDay = false;
    public UserBean mUserBean;
    private String TeacherID;

    @NonNull
    @Override
    public SchedulePlanPresenter createPresenter() {
        return new SchedulePlanPresenter();
    }

    public static void start(Context context) {
        final Intent lIntent = new Intent(context, SchedulePlanActivity.class);
//        Bundle lBundle = new Bundle();
        if (!(context instanceof Activity)) {
            lIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(lIntent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_plan);
        initView();
        initData();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    /**
     * 设置日历显示
     */
    public void startSetData(boolean needTheMonthFirstDay) {
        isNeedTheMonthFirstDay = needTheMonthFirstDay;
        String year = calendarAdapter.getShowYear();
        String month = calendarAdapter.getShowMonth();

        if (month.startsWith("0") && month.length() == 2) {
            month = month.substring(1, month.length());
        }
        setData(Integer.parseInt(year), Integer.parseInt(month));
    }

    /**
     *  去服务器获取该月分的内容
     * @param year
     * @param month
     */
    private void setData(int year, int month) {
        String temp = "-01";
        String beginDate = null;
        String endDate = null;

        if (month == 12) {
            beginDate = year + "-" + "12" + temp;
            endDate = (year + 1) + "-" + "01" + temp;
        } else {
            beginDate = year + "-" + (month < 10 ? ("0" + month) : month)+ temp;
            int nextMonth = month + 1;
            endDate = year + "-"+ (nextMonth < 10 ? ("0" + nextMonth) : nextMonth) + temp;
        }
        //TODO 假数据
        List<Schedule> all = presenter.testData();
        LogUtil.Log("lenita","all.size = "+all.size());
        list = increase(all);
        divideSchedules(list);
        //TODO 获取服务器ScheduleList
//        presenter.getScheduleListData(TeacherID,beginDate,endDate);
    }

    private void initView(){
        mTodayTextView =(TextView) findViewById(R.id.schedule_add_tv);
        mTodayTextView.setOnClickListener(this);
        mMonthTextView = (TextView)findViewById(R.id.schedule_month_tv);
        mMonthTextView.setOnClickListener(this);
        mFlipper = (ViewFlipper)findViewById(R.id.schedule_flipper);  //用于日历
        mListView = (SwipeMenuListView)findViewById(R.id.schedule_listview);   //用于日程显示

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
        try {
            mUserBean = AppConfiguration.getInstance().getUserBean();
            TeacherID = mUserBean.getUserId();
        } catch (Exception e) {
            e.printStackTrace();
        }
        mGestureDetector = new GestureDetector(this, this);
        scheduleAdapter = new ScheduleAdapter( this,listener, null);
        selectDateScheduleList = new ArrayList<>();
//        scheduleAdapter = new SchedulePlanAdapter(this,	listener, null);
        mListView.setAdapter(scheduleAdapter);
        initSwipeMenu();
        initListener();
        presenter.getServerTime();
    }

    ScheduleAdapter.ScheduleOperationListener listener = new ScheduleAdapter.ScheduleOperationListener() {

        @Override
        public void updateStatusSchedule(int id, int status) {
            //TODO 更新日程
            LogUtil.Log("lenita","updateStatusSchedule id = "+ id);
            presenter.changeScheduleStatus(id,status);
        }

        @Override
        public void editSchedule(Bundle bundle) {
            //TODO 编辑日程,跳转到NewScheduleActivity
            Intent intent=new Intent(mActivity,NewScheduleActivity.class);
            intent.putExtras(bundle);
            startActivity(intent);
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
        calendarAdapter = new CalendarAdapter(this, jumpMonth,jumpYear, year_c, month_c, day_c);
        calendarAdapter.setScheduleData(null, null);
        positon_c = day_c+calendarAdapter.getMonthStartPosition()-1;
        dayClick = day_c+calendarAdapter.getMonthStartPosition()-1;
        LogUtil.Log("lenita","first dayClick = "+dayClick);
        setCalender();  //设置日历
    }

    private void setCalender(){
        mFlipper.removeAllViews();
        jumpMonth = 0;
        jumpYear = 0;

        //第一次设置gridview
        gridView = (GridView) getLayoutInflater().inflate(R.layout.view_schedule_gridview, null);
        gridView.setOnItemClickListener(this);
        gridView.setOnTouchListener(this);
        gridView.setAdapter(calendarAdapter);

        mFlipper.addView(gridView, 0);
        //无论是否获得服务器时间,都显示出来时间
        LogUtil.Log("lenita","date = "+month_c+"-"+day_c);
        setTitleYearMonth();

        //设置初始数据,显示服务器的日期数据
        startSetData(false);
    }

    private void inflaterGridview() {
        gridView = (GridView) getLayoutInflater().inflate(
                R.layout.view_schedule_gridview, null);
        gridView.setOnItemClickListener(this);
        gridView.setOnTouchListener(this);
    }

    @Override
    public void getScheduleList(List<Schedule> all) {
//        list = increase(all);
//        divideSchedules(list);
    }

    @Override
    public void deleteLocalSchedules(int id) {

            Toast.Short(this,"删除成功");
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
            Toast.Short(this,"更新成功");
            for (int i = 0; i < list.size(); i++) {
                Schedule s = list.get(i);
                if (s.getId() == id) {
                    s.setStatus(status);
                }
            }
            divideSchedulesByDeleteOrUpdate(list);

    }

    @Override
    public void getSingleSchedulesInfo(boolean isSuccess, int id, Schedule schedule) {

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
        mListView.hideTouchView();  //TODO 先恢复收起状态，否则影响后面操作
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
                isFilp = false;
                dayClick = positon_c;  //今天所在的位置
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
                break;
            case R.id.schedule_month_tv:
                selectYearMonth();
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
            mFlipper.setInAnimation(AnimationUtils.loadAnimation(this,
                    R.anim.push_left_in));
            mFlipper.setOutAnimation(AnimationUtils.loadAnimation(this,
                    R.anim.push_left_out));
            mFlipper.showNext();
        } else if (state > 0) {
            mFlipper.setInAnimation(AnimationUtils.loadAnimation(this,
                    R.anim.push_right_in));
            mFlipper.setOutAnimation(AnimationUtils.loadAnimation(this,
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
        mFlipper.setInAnimation(AnimationUtils.loadAnimation(this,R.anim.push_right_in));
        mFlipper.setOutAnimation(AnimationUtils.loadAnimation(this,	R.anim.push_right_out));
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
        mFlipper.setInAnimation(AnimationUtils.loadAnimation(this,
                R.anim.push_left_in));
        mFlipper.setOutAnimation(AnimationUtils.loadAnimation(this,
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


    public void initSwipeMenu(){

        // step 1. create a MenuCreator
        SwipeMenuCreator creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(getApplicationContext());
                // set item background
                deleteItem.setBackground(getResources().getDrawable(R.color.red_color));
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
                        Log.d("lenita","delete item =" +item.getScheduleContent());
                        presenter.popupConfirmDialog(SchedulePlanActivity.this, "是否确认删除该日程？", new SchedulePlanPresenter.DeleteListener() {
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
        mListView.setOnSwipeListener(new SwipeMenuListView.OnSwipeListener() {

            @Override
            public void onSwipeStart(int position) {
                // swipe start
            }

            @Override
            public void onSwipeEnd(int position) {
                // swipe end
            }
        });

        // set MenuStateChangeListener
        mListView.setOnMenuStateChangeListener(new SwipeMenuListView.OnMenuStateChangeListener() {
            @Override
            public void onMenuOpen(int position) {
            }

            @Override
            public void onMenuClose(int position) {
            }
        });

        // other setting
//		listView.setCloseInterpolator(new BounceInterpolator());

        // test item long click
        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                           int position, long id) {
                android.widget.Toast.makeText(getApplicationContext(), position + " long click", android.widget.Toast.LENGTH_SHORT).show();
                return false;
            }
        });
    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }
}