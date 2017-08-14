package com.gta.zssx.fun.coursedaily.registerrecord.view.page;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.bigkoo.pickerview.TimePickerView;
import com.blankj.utilcode.util.ToastUtils;
import com.gta.utils.resource.Toast;
import com.gta.zssx.AppConfiguration;
import com.gta.zssx.R;
import com.gta.zssx.fun.coursedaily.registerrecord.model.DTO.ClassInfoDto;
import com.gta.zssx.fun.coursedaily.registerrecord.model.DTO.RegisteredRecordDto;
import com.gta.zssx.fun.coursedaily.registerrecord.model.DTO.SaveCacheDto;
import com.gta.zssx.fun.coursedaily.registerrecord.model.RegisteredRecordManager;
import com.gta.zssx.fun.coursedaily.registerrecord.presenter.AlreadyRegisterRecordPresenter;
import com.gta.zssx.fun.coursedaily.registerrecord.view.AlreadyRegisteredRecordView;
import com.gta.zssx.fun.coursedaily.registerrecord.view.adapter.RegisterRecordListAdapter;
import com.gta.zssx.fun.personalcenter.model.bean.UserBean;
import com.gta.zssx.pub.base.BaseActivity;
import com.gta.zssx.pub.hfrecyclerview.HFRecyclerView;
import com.gta.zssx.pub.manager.ToolBarManager;
import com.gta.zssx.pub.util.LogUtil;
import com.gta.zssx.pub.util.RxBus;
import com.gta.zssx.pub.util.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import rx.functions.Action1;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by lan.zheng on 2016/7/27.
 */
public class AlreadyRegisteredRecordPeriodOfTimeActivity extends BaseActivity<AlreadyRegisteredRecordView, AlreadyRegisterRecordPresenter>
        implements AlreadyRegisteredRecordView, View.OnClickListener ,HFRecyclerView.HFRecyclerViewListener{
    public static final String PAGE_TAG = AlreadyRegisteredRecordPeriodOfTimeActivity.class.getSimpleName();
    public Toolbar mToolbar;
    public ToolBarManager mToolBarManager;
    private Calendar mCalendar;
    private TextView mBeginDateTextView;
    private TextView mEndDateTextView;
    private TextView mSectionTextView;
    private TextView mNotResultTextView;
//    private RecyclerView mRecyclerView;
    private HFRecyclerView mRecyclerView;
//    private SwipeRefreshLayoutBottom swipe_load;
//    public ABaseLinearLayoutManager mLayoutManager;
    private RegisterRecordListAdapter lAdapter;
    //分页标志
    private static int countpage = 1;
    //是否可以加载更多
    private boolean canLoadMore = false;
    public UserBean mUserBean;
    private String TeacherID;
    private SimpleDateFormat sdf;  //开始时间，结束时间

    //当月时间段
    private String beginDate;
    private String endDate;
    private String beginWeekDay;
    private String endWeekDay;
    private long beginTimeInMillis;
    private long endTimeInMillis;
    private final static int GET_BEGIN_WEEK = 1;
    private final static int GET_END_WEEK = 2;
    private CompositeSubscription allSubscription = new CompositeSubscription();

    //已登记需要的数据
    public static void start(Context context) {
        RegisteredRecordManager.setSaveCacheDto(null);  //确保缓存为空
        final Intent lIntent = new Intent(context, AlreadyRegisteredRecordPeriodOfTimeActivity.class);
        if (!(context instanceof Activity)) {
            lIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(lIntent);
//        context.startActivity(lIntent2);
    }

    @Override
    public void showResultList(RegisteredRecordDto registeredRecordDto,boolean isRefresh) {
        mRecyclerView.stopRefresh(true);
        mRecyclerView.stopLoadMore(true);
        if(isRefresh){ //TODO 如果是下拉刷新
            if(registeredRecordDto.getDetail().size() > 0){
                mNotResultTextView.setVisibility(View.GONE);  //没有结果的view
                mRecyclerView.setCanLoadMore(true);
                countpage++;//有结果的时候分页的count才自加
                lAdapter = null;  //并把lAdapter重置
                String section = registeredRecordDto.getTotal() + "";
                LogUtil.Log("lenita", "section = " + section + ",size() = " + registeredRecordDto.getDetail().size());
                //有数据就列出节次信息
                updateSectionInfo(section, registeredRecordDto.getDetail());
            }else {
                mRecyclerView.setCanLoadMore(false);
                mNotResultTextView.setVisibility(View.VISIBLE);  //没有结果的view
            }
            return;
        }

        canLoadMore = true; //有数据就可加载更多
        if (countpage <= 1) {
            countpage++;//有结果的时候分页的count才自加
            lAdapter = null;  //并把lAdapter重置
            String section = registeredRecordDto.getTotal() + "";
            LogUtil.Log("lenita", "section = " + section + ",size() = " + registeredRecordDto.getDetail().size());
            //有数据就列出节次信息
            updateSectionInfo(section, registeredRecordDto.getDetail());
        } else {
            lAdapter.loadMoreData(registeredRecordDto.getDetail());
            countpage++;
        }
    }

    @Override
    public void notResult() {
        //获取不到服务器时间
        ToastUtils.showShortToast("获取服务器时间失败");
        mBeginDateTextView.setText(beginDate);
        mEndDateTextView.setText(endDate);
        mNotResultTextView.setVisibility(View.VISIBLE);  //获取时间失败结果的view

    }

    private void updateSectionInfo(String section, final List<RegisteredRecordDto.recordEntry> recordEntryList) {
        mSectionTextView.setText(section);
        mNotResultTextView.setVisibility(View.GONE);
        RegisterRecordListAdapter.Listener lListener = new RegisterRecordListAdapter.Listener() {
            @Override
            public void itemClick(RegisteredRecordDto.recordEntry recordEntry) {
                //记下本页面的时间数据
                SaveCacheDto lSaveCacheDto = new SaveCacheDto();
                lSaveCacheDto.setBeginDate(beginDate);
                lSaveCacheDto.setEndDate(endDate);
                lSaveCacheDto.setSaveRecord(true);
                RegisteredRecordManager.setSaveCacheDto(lSaveCacheDto);
                //跳转到详细点的页面
                ClassInfoDto lClassInfoDto = new ClassInfoDto();
                lClassInfoDto.setSignDate(recordEntry.getSignDate());
                lClassInfoDto.setClassName(recordEntry.getClassName());
                lClassInfoDto.setClassID("" + recordEntry.getClassID());
                lClassInfoDto.setTeacherID(TeacherID);
                lClassInfoDto.setLogStatisticeInto(false); //不需要获取全部数据，只获取该老师登记的,只有从统计中进入才需要获取全部
                lClassInfoDto.setIsFromClassLogMainpage(true); //获取真实的数据传递入下一个Activity,是否是课堂日志主页进入，是为true
                Log.e("lenita","AlreadyR...Activity ClassID = "+recordEntry.getClassID()+",ClassName = "+recordEntry.getClassName());
                //改成跳转到Activity
                AlreadyRegisteredRecordActivity.start(mActivity, AlreadyRegisteredRecordFromSignatureFragment.PAGE_TAG, lClassInfoDto);
            }
        };
        lAdapter = new RegisterRecordListAdapter(mActivity, lListener, recordEntryList);
        mRecyclerView.setAdapter(lAdapter);
    }

    @Override
    public void setDate(String date) {
        SimpleDateFormat lSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        //获取服务器时间后，转换成当月时间段，显示在时间选择区间view中
        try {
            mCalendar.setTimeInMillis(lSimpleDateFormat.parse(date).getTime());  //重置时间
            getDate();
        } catch (ParseException e) {
            e.printStackTrace();

        }
        //测试更新view
        updateDateView();
    }

    @Override
    public void getRegisterData(String beginDate, String endDate) {
        //首次进入获取已登记节次-->登陆的老师ID
        presenter.getRegisteredRecordData(TeacherID,beginDate,endDate,1,false,false);
    }

    private void updateDateView() {
        mBeginDateTextView.setText(beginDate);
        mEndDateTextView.setText(endDate);
    }

    @Override
    public void setRefreshFalseandDealWithView(boolean isNotResult,String msg) {
//        swipe_load.setRefreshing(false);
        mRecyclerView.stopLoadMore(true);
        if (isNotResult) {//抛出异常的时候进入下面
            canLoadMore = false;  //先默认都不给加载更多，后面再根据情况改变该值
            if(msg.equals("change_date")){  //日期转换的时候，抛出异常的都显示“暂无结果”
                canLoadMore = true;
                mSectionTextView.setText("0");
                mNotResultTextView.setVisibility(View.VISIBLE);
                if (lAdapter != null) {
                    lAdapter.clearUpData();
                }
                return;
            }
            if(msg.equals("not_network")){
                //非日期转换，且没有网络的时候,保留页面显示
                canLoadMore = true;
                return;
            }
            //其他的情况
            if (countpage <= 1) {
                if(msg.equals(getResources().getString(R.string.text_not_more_result))){//暂无数据
                    ToastUtils.showShortToast(getResources().getString(R.string.text_not_result));
                }
                mSectionTextView.setText("0");
                mNotResultTextView.setVisibility(View.VISIBLE);
                if (lAdapter != null) {
                    lAdapter.clearUpData();
                }
            } else {
                if(msg.equals(getResources().getString(R.string.text_not_more_result))) {//无更多数据
                    ToastUtils.showShortToast( msg);
                    mRecyclerView.setFooterViewText ("无更多信息");
                }
                canLoadMore = true; //当页数是大于1的，不能清空这些数据，且允许加载更多
                mNotResultTextView.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onRefreshError() {
        mRecyclerView.stopRefresh(false);
    }

    @Override
    public void getTeacherClassInfoReturn(boolean isSuccess) {
        LogUtil.Log("lenita","update teacher class info is success ="+isSuccess);
    }

    private void getDate() {
        int maxDayofMonth = mCalendar.getActualMaximum(Calendar.DATE);
        beginDate = mCalendar.get(Calendar.YEAR) + "-"
                + ((mCalendar.get(Calendar.MONTH) + 1) > 9 ? (mCalendar.get(Calendar.MONTH) + 1) : "0" + (mCalendar.get(Calendar.MONTH) + 1)) + "-01";
        endDate = mCalendar.get(Calendar.YEAR) + "-"
                + ((mCalendar.get(Calendar.MONTH) + 1) > 9 ? (mCalendar.get(Calendar.MONTH) + 1) : "0" + (mCalendar.get(Calendar.MONTH) + 1)) + "-" + maxDayofMonth;
        try {
            beginTimeInMillis = sdf.parse(beginDate).getTime();
            endTimeInMillis = sdf.parse(endDate).getTime();
            Calendar lCalendar = Calendar.getInstance();
            //获取开始的星期几
            lCalendar.setTimeInMillis(beginTimeInMillis);
            int weekDay = lCalendar.get(Calendar.DAY_OF_WEEK);
            beginWeekDay = StringUtils.changeWeekDaytoString(weekDay);
            //获取结束的星期几
            lCalendar.setTimeInMillis(endTimeInMillis);
            weekDay = lCalendar.get(Calendar.DAY_OF_WEEK);
            endWeekDay = StringUtils.changeWeekDaytoString(weekDay);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }



    @NonNull
    @Override
    public AlreadyRegisterRecordPresenter createPresenter() {
        return new AlreadyRegisterRecordPresenter(this);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_already_registered_record_from_class_log_mainpage);
        initTeacherId();
        uiAction();
    }

    private void initTeacherId() {
        String format = "yyyy-MM-dd";
        sdf = new SimpleDateFormat(format, Locale.CHINA);
        try {
            mUserBean = AppConfiguration.getInstance().getUserBean();
            TeacherID = mUserBean.getUserId();
            //获得老师ID后去更新老师是否班主任
            presenter.getTeacherClassUpdate(TeacherID);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void uiAction() {
        mCalendar = Calendar.getInstance();
        countpage = 1;
        getDate();  //获取时间段
        //初始化View
        initView();
        firstRequestServer();
    }

    private void firstRequestServer() {
        //判断是否有cache--暂时不用判断,此处永远为null
        if (RegisteredRecordManager.getSaveCacheDto() != null && RegisteredRecordManager.getSaveCacheDto().getSaveRecord()) {
            beginDate = RegisteredRecordManager.getSaveCacheDto().getBeginDate();
            endDate = RegisteredRecordManager.getSaveCacheDto().getEndDate();
            getTimeMillisandWeek(GET_BEGIN_WEEK, beginDate);
            getTimeMillisandWeek(GET_END_WEEK, endDate);
            updateDateView();
            RegisteredRecordManager.setSaveCacheDto(null); //记录下值后，立刻置空
            presenter.getRegisteredRecordData(TeacherID, beginDate, endDate, countpage,false,false);
        } else {
            //请求网络时间，为了防止用户是错误的系统时间
            presenter.getServerTime(TeacherID);
            //广播监听
            presenter.mCompositeSubscription.add(RxBus.getDefault().toObserverable(AlreadyRegisterRecordPresenter.class).subscribe(new Action1<Object>() {
                @Override
                public void call(Object o) {
                    LogUtil.Log("lenita", "allSubscription.add -- outside");
                    if (RegisteredRecordManager.getSaveCacheDto() != null && RegisteredRecordManager.getSaveCacheDto().getSaveRecord()) {
                        LogUtil.Log("lenita", "allSubscription.add -- broadcast");
                        beginDate = RegisteredRecordManager.getSaveCacheDto().getBeginDate();
                        endDate = RegisteredRecordManager.getSaveCacheDto().getEndDate();
                        getTimeMillisandWeek(GET_BEGIN_WEEK, beginDate);
                        getTimeMillisandWeek(GET_END_WEEK, endDate);
                        updateDateView();
//                        RegisteredRecordManager.setSaveCacheDto(null); //若连续删除两条记录会有问题，不能置空
                        //TODO刷新数据
                        countpage = 1;
                        presenter.getRegisteredRecordData(TeacherID, beginDate, endDate, countpage,false,false);
                    }
                }
            }));
        }
    }

    private void initView() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolBarManager = new ToolBarManager(mToolbar, this);
        mToolBarManager.init()
                .showBack(true)
                .showLeftImage(false)
                .setTitle(getResources().getString(R.string.text_registered_record_title))
                .showRightButton(false)
                .setRightText("");
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mBeginDateTextView = (TextView) findViewById(R.id.tv_begin_date);
        mEndDateTextView = (TextView) findViewById(R.id.tv_end_date);
        mSectionTextView = (TextView) findViewById(R.id.tv_class_total_section);
        mRecyclerView = (HFRecyclerView) findViewById(R.id.prrv);
        setOnInteractListener();
//        swipe_load = (SwipeRefreshLayoutBottom) findViewById(R.id.swipe_load_frame);
        mNotResultTextView = (TextView) findViewById(R.id.tv_not_result_text);

        //滚动条管理器，用在有Refresh的操作上
//        mLayoutManager = new ABaseLinearLayoutManager(mActivity);
//        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
//        mRecyclerView.setLayoutManager(mLayoutManager);
       /* swipe_load.setOnLoadMoreListener(new SwipeRefreshLayoutBottom.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                swipe_load.setRefreshing(true);
                if (!canLoadMore) {
                    swipe_load.setRefreshing(false);
                    Toast.Short(mActivity, mActivity.getResources().getString(R.string.text_not_more_result));
                    return;
                }
                refreshListData(false);
            }
        });*/

        updateDateView();
        mNotResultTextView.setVisibility(View.GONE);
        mBeginDateTextView.setOnClickListener(this);
        mEndDateTextView.setOnClickListener(this);
    }

    private void setOnInteractListener () {
        mRecyclerView.setCanLoadMore (true);
        mRecyclerView.setCanRefresh (true);
        mRecyclerView.setFooterViewText ("");
        mRecyclerView.setRecyclerViewListener (this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_begin_date:
                selectBeginTime();
                break;
            case R.id.tv_end_date:
                selectEndTime();
                break;
            default:
                break;
        }
    }

    /**
     * 时间更改后重新去服务器获取已登记课堂日志数据
     */
    private void refreshListData(boolean changeDate) {
        // 接口的连通性 --> 传入真正的TeacherID,当是换日期的时候，没有网络时要另外做处理
        presenter.getRegisteredRecordData(TeacherID, beginDate, endDate, countpage,changeDate,false);
//        presenter.getRegisteredRecordData("7811107d-300b-4b72-84d2-715948f197ea",beginDate,endDate,countpage,mActivity);
    }

    public TimePickerView mBeginTimePickerView;

    private void selectBeginTime() {
        mBeginTimePickerView = new TimePickerView(mActivity, TimePickerView.Type.YEAR_MONTH_DAY);
        Date lDate = null;
        try {
            lDate = sdf.parse(beginDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        mBeginTimePickerView.setTime(lDate);
        mBeginTimePickerView.setCyclic(true);
        mBeginTimePickerView.setCancelable(true);
        mBeginTimePickerView.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date) {
                if (date.getTime() > endTimeInMillis) {
//                    Toast.makeText(getActivity(), "查询开始日期不能大于结束日期,请重新进行选择", Toast.LENGTH_SHORT).show();
                    Toast.Long(mActivity, mActivity.getResources().getString(R.string.search_date_error_begin));
                    return;
                }
                beginDate = sdf.format(date);
                getTimeMillisandWeek(GET_BEGIN_WEEK, beginDate);
                LogUtil.Log("lenita", "选择的时间是：" + beginDate + beginWeekDay);
                countpage = 1;  //刷新显示要重新置值
                updateDateView();
                refreshListData(true);
            }
        });
        mBeginTimePickerView.show();
    }

    public TimePickerView mEndTimePickerView;

    private void selectEndTime() {
        mEndTimePickerView = new TimePickerView(mActivity, TimePickerView.Type.YEAR_MONTH_DAY);
        Date lDate2 = null;
        try {
            lDate2 = sdf.parse(endDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        mEndTimePickerView.setTime(lDate2);
        mEndTimePickerView.setCyclic(true);
        mEndTimePickerView.setCancelable(true);
        mEndTimePickerView.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date) {
                if (date.getTime() < beginTimeInMillis) {
//                    Toast.makeText(getActivity(), "查询结束日期不能小于开始日期,请重新进行选择！", Toast.LENGTH_SHORT).show();
                    Toast.Long(mActivity, mActivity.getResources().getString(R.string.search_date_error_end));

                    return;
                }
                endDate = sdf.format(date);
                getTimeMillisandWeek(GET_END_WEEK, endDate);
                LogUtil.Log("lenita", "选择的时间是：" + endDate + endWeekDay);
                countpage = 1;  //刷新显示要重新置值
                updateDateView();
                refreshListData(true);
            }
        });
        mEndTimePickerView.show();
    }

    private void getTimeMillisandWeek(int flag, String date) {
        Calendar lCalendar = Calendar.getInstance();
        if (flag == GET_BEGIN_WEEK) {
            SimpleDateFormat lSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
            //获取星期几
            try {
                beginTimeInMillis = lSimpleDateFormat.parse(date).getTime();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            lCalendar.setTimeInMillis(beginTimeInMillis);
            int weekDay = lCalendar.get(Calendar.DAY_OF_WEEK);
            beginWeekDay = StringUtils.changeWeekDaytoString(weekDay);
        } else if (flag == GET_END_WEEK) {
            SimpleDateFormat lSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
            //获取星期几
            try {
                endTimeInMillis = lSimpleDateFormat.parse(date).getTime();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            lCalendar.setTimeInMillis(endTimeInMillis);
            int weekDay = lCalendar.get(Calendar.DAY_OF_WEEK);
            endWeekDay = StringUtils.changeWeekDaytoString(weekDay);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detachView(false);
        mToolBarManager.destroy();
        allSubscription.unsubscribe();
        RegisteredRecordManager.setSaveCacheDto(null);
    }

    @Override
    public void onRefresh() {
        countpage = 1;
        presenter.getRegisteredRecordData(TeacherID, beginDate, endDate, countpage,false,true);
    }

    @Override
    public void onLoadMore() {
        if (!canLoadMore) {
            mRecyclerView.stopLoadMore (true);
            Toast.Short(mActivity, mActivity.getResources().getString(R.string.text_not_more_result));
            return;
        }
        refreshListData(false);
    }

    public class UpdateListener { //空，不需要任何的数据,只为回调刷新数据操
    }
}
