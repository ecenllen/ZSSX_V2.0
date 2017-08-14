package com.gta.zssx.fun.coursedaily.registerrecord.view;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bigkoo.pickerview.TimePickerView;
import com.gta.utils.resource.Toast;
import com.gta.zssx.AppConfiguration;
import com.gta.zssx.R;
import com.gta.zssx.fun.coursedaily.registerrecord.model.DTO.ClassInfoDto;
import com.gta.zssx.fun.coursedaily.registerrecord.model.DTO.RegisteredRecordDto;
import com.gta.zssx.fun.coursedaily.registerrecord.model.DTO.SaveCacheDto;
import com.gta.zssx.fun.coursedaily.registerrecord.model.RegisteredRecordManager;
import com.gta.zssx.fun.coursedaily.registerrecord.presenter.AlreadyRegisterRecordPresenter;
import com.gta.zssx.fun.coursedaily.registerrecord.view.adapter.RegisterRecordListAdapter;
import com.gta.zssx.fun.coursedaily.registerrecord.view.base.RegisterRecordBaseFragment;
import com.gta.zssx.fun.coursedaily.registerrecord.view.base.RegisterRecordFragmentBuilder;
import com.gta.zssx.fun.coursedaily.registerrecord.view.page.AlreadyRegisteredRecordFromSignatureFragment;
import com.gta.zssx.fun.personalcenter.model.bean.UserBean;
import com.gta.zssx.pub.util.LogUtil;
import com.gta.zssx.pub.util.StringUtils;
import com.gta.zssx.pub.widget.footer.ABaseLinearLayoutManager;
import com.gta.zssx.pub.widget.footer.SwipeRefreshLayoutBottom;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by lan.zheng on 2016/6/17.
 * 已登记 - 废弃
 */
@Deprecated
public class AlreadyRegisteredRecordFragment extends RegisterRecordBaseFragment<AlreadyRegisteredRecordView, AlreadyRegisterRecordPresenter>
        implements AlreadyRegisteredRecordView, View.OnClickListener {
    public static final String PAGE_TAG = AlreadyRegisteredRecordFragment.class.getSimpleName();
    private Calendar mCalendar;
    private TextView mBeginDateTextView;
    private TextView mEndDateTextView;
    private TextView mSectionTextView;
    private TextView mNotResultTextView;
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayoutBottom swipe_load;
    private RegisterRecordListAdapter lAdapter;
    //分页标志
    private static int countpage = 1;
    //是否可以加载更多
    private boolean canLoadMore = false;
    private String TeacherID;

    //当月时间段
    private String beginDate;
    private String endDate;
    private String beginWeekDay;
    private String endWeekDay;
    private long beginTimeInMillis;
    private long endTimeInMillis;
    private final static int GET_BEGIN_WEEK = 1;
    private final static int GET_END_WEEK = 2;

    @Override
    public void setRefreshFalseandDealWithView(boolean isNotResult,String msg) {
        swipe_load.setRefreshing(false);
        if (isNotResult) {
            //没有数据才进来
            canLoadMore = false;
            if (countpage <= 1) {
                LogUtil.Log("lenita", "mNotResultTextView.setVisibility(View.VISIBLE)");
                mSectionTextView.setText("0");
                mNotResultTextView.setVisibility(View.VISIBLE);
                if (lAdapter != null) {
                    lAdapter.clearUpData();
                }
            } else {
                LogUtil.Log("lenita", "mNotResultTextView.setVisibility(View.GONE)");
                mNotResultTextView.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onRefreshError() {

    }

    @Override
    public void getTeacherClassInfoReturn(boolean isSuccess) {

    }

    @Override
    public void showResultList(RegisteredRecordDto registeredRecordDto,boolean isRefresh) {
        LogUtil.Log("lenita", "showResultList countpage =  " + countpage);
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
        mNotResultTextView.setVisibility(View.VISIBLE);  //没有结果的view
    }

    //    public static boolean loading = false;
    private void updateSectionInfo(String section, final List<RegisteredRecordDto.recordEntry> recordEntryList) {
        mSectionTextView.setText(section);
        mNotResultTextView.setVisibility(View.GONE);
        RegisterRecordListAdapter.Listener lListener = new RegisterRecordListAdapter.Listener() {
            @Override
            public void itemClick(RegisteredRecordDto.recordEntry recordEntry) {
                //TODO 记下本页面的时间数据
                SaveCacheDto lSaveCacheDto = new SaveCacheDto();
                lSaveCacheDto.setBeginDate(beginDate);
                lSaveCacheDto.setEndDate(endDate);
                lSaveCacheDto.setSaveRecord(true);
                RegisteredRecordManager.setSaveCacheDto(lSaveCacheDto);
                //跳转到详细点的页面
                LogUtil.Log("lenita", "lSaveCacheDto.getTime = " + lSaveCacheDto.getBeginDate());
                ClassInfoDto lClassInfoDto = new ClassInfoDto();
                lClassInfoDto.setSignDate(recordEntry.getSignDate());
                lClassInfoDto.setClassName(recordEntry.getClassName());
                lClassInfoDto.setClassID("" + recordEntry.getClassID());
                lClassInfoDto.setTeacherID(TeacherID);
//                lClassInfoDto.setTeacherID("7811107d-300b-4b72-84d2-715948f197ea");
                //TODO 获取真实的数据传递入下一个fragment
                lClassInfoDto.setIsFromClassLogMainpage(true);
                new AlreadyRegisteredRecordFromSignatureFragment.Builder(mActivity, lClassInfoDto).display();
            }
        };
        lAdapter = new RegisterRecordListAdapter(mActivity, lListener, recordEntryList);
        mRecyclerView.setAdapter(lAdapter);
    }

    @Override
    public void setDate(String date) {
        //获取服务器时间后，转换成当月时间段，显示在时间选择区间view中
        LogUtil.Log("lenita", "date = " + date);
        SimpleDateFormat lSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
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

    }

    private void updateDateView() {
        mBeginDateTextView.setText(beginDate);
        mEndDateTextView.setText(endDate);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
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
    private void refreshListData() {
        //TODO 接口的连通性 --> 到时是传入真正的TeacherID
        presenter.getRegisteredRecordData(TeacherID, beginDate, endDate, countpage,false,false);
//        presenter.getRegisteredRecordData("7811107d-300b-4b72-84d2-715948f197ea",beginDate,endDate,countpage,mActivity);
    }

    private void selectBeginTime() {
        TimePickerView mBeginTimePickerView = new TimePickerView(mActivity, TimePickerView.Type.YEAR_MONTH_DAY);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date lDate = null;
        try {
            lDate = sdf.parse(beginDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        mBeginTimePickerView.setTime(lDate);
        mBeginTimePickerView.setCyclic(true);
        mBeginTimePickerView.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date) {
                if (date.getTime() > endTimeInMillis) {
//                    Toast.makeText(getActivity(), "查询开始日期不能大于结束日期,请重新进行选择", Toast.LENGTH_SHORT).show();
                    Toast.Long(mActivity, mActivity.getResources().getString(R.string.search_date_error_begin));
                    return;
                }
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                beginDate = sdf.format(date);
                getTimeMillisandWeek(GET_BEGIN_WEEK, beginDate);
                LogUtil.Log("lenita", "选择的时间是：" + beginDate + beginWeekDay);
                countpage = 1;  //刷新显示要重新置值
                updateDateView();
                refreshListData();
            }
        });
        mBeginTimePickerView.show();
    }

    private void selectEndTime() {
        TimePickerView mEndTimePickerView = new TimePickerView(mActivity, TimePickerView.Type.YEAR_MONTH_DAY);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date lDate2 = null;
        try {
            lDate2 = sdf.parse(endDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        mEndTimePickerView.setTime(lDate2);
        mEndTimePickerView.setCyclic(true);
        mEndTimePickerView.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date) {
                if (date.getTime() < beginTimeInMillis) {
//                    Toast.makeText(getActivity(), "查询结束日期不能小于开始日期,请重新进行选择！", Toast.LENGTH_SHORT).show();
                    Toast.Long(mActivity, mActivity.getResources().getString(R.string.search_date_error_end));

                    return;
                }
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                endDate = sdf.format(date);
                getTimeMillisandWeek(GET_END_WEEK, endDate);
                LogUtil.Log("lenita", "选择的时间是：" + endDate + endWeekDay);
                countpage = 1;  //刷新显示要重新置值
                updateDateView();
                refreshListData();
            }
        });
        mEndTimePickerView.show();
    }

    private void getTimeMillisandWeek(int flag, String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar lCalendar = Calendar.getInstance();
        if (flag == GET_BEGIN_WEEK) {
            //获取星期几
            try {
                beginTimeInMillis = sdf.parse(date).getTime();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            lCalendar.setTimeInMillis(beginTimeInMillis);
            int weekDay = lCalendar.get(Calendar.DAY_OF_WEEK);
            beginWeekDay = StringUtils.changeWeekDaytoString(weekDay);
        } else if (flag == GET_END_WEEK) {
            //获取星期几
            try {
                endTimeInMillis = sdf.parse(date).getTime();
            } catch (ParseException e) {
                e.printStackTrace();
            }
            lCalendar.setTimeInMillis(endTimeInMillis);
            int weekDay = lCalendar.get(Calendar.DAY_OF_WEEK);
            endWeekDay = StringUtils.changeWeekDaytoString(weekDay);
        }

    }

    public static class Builder extends RegisterRecordFragmentBuilder<AlreadyRegisteredRecordFragment> {


        public Builder(Context context) {
            super(context);
        }

        @Override
        public AlreadyRegisteredRecordFragment create() {
            return new AlreadyRegisteredRecordFragment();
        }

        @Override
        public String getPageTag() {
            return PAGE_TAG;
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_already_registered_record_from_class_log_mainpage, container, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            UserBean mUserBean = AppConfiguration.getInstance().getUserBean();
            TeacherID = mUserBean.getUserId();
            LogUtil.Log("lenita", "TeacherID = " + TeacherID);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.attachView(this);
        uiAction();
    }

    private void getDate() {
        int maxDayofMonth = mCalendar.getActualMaximum(Calendar.DATE);
        beginDate = mCalendar.get(Calendar.YEAR) + "-"
                + ((mCalendar.get(Calendar.MONTH) + 1) > 9 ? (mCalendar.get(Calendar.MONTH) + 1) : "0" + (mCalendar.get(Calendar.MONTH) + 1)) + "-01";
        endDate = mCalendar.get(Calendar.YEAR) + "-"
                + ((mCalendar.get(Calendar.MONTH) + 1) > 9 ? (mCalendar.get(Calendar.MONTH) + 1) : "0" + (mCalendar.get(Calendar.MONTH) + 1)) + "-" + maxDayofMonth;
        SimpleDateFormat lSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            beginTimeInMillis = lSimpleDateFormat.parse(beginDate).getTime();
            endTimeInMillis = lSimpleDateFormat.parse(endDate).getTime();
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

    private void uiAction() {
        mCalendar = Calendar.getInstance();
        countpage = 1;
        getDate();  //获取时间段
        //初始化View
        initView();
        firstRequestServer();
    }

    private void firstRequestServer() {
        //判断是否有cache
        if (RegisteredRecordManager.getSaveCacheDto() != null && RegisteredRecordManager.getSaveCacheDto().getSaveRecord()) {
            beginDate = RegisteredRecordManager.getSaveCacheDto().getBeginDate();
            endDate = RegisteredRecordManager.getSaveCacheDto().getEndDate();
            getTimeMillisandWeek(GET_BEGIN_WEEK, beginDate);
            getTimeMillisandWeek(GET_END_WEEK, endDate);
            updateDateView();
            RegisteredRecordManager.setSaveCacheDto(null); //记录下值后，立刻置空
            //TODO 获取真正的老师ID
            presenter.getRegisteredRecordData(TeacherID, beginDate, endDate, countpage,false,true);
//            presenter.getRegisteredRecordData("7811107d-300b-4b72-84d2-715948f197ea",beginDate,endDate,countpage,mActivity);
        } else {
            //请求网络时间，为了防止用户是错误的系统时间
            presenter.getServerTime(TeacherID);
        }
    }

    private void initView() {
        mBeginDateTextView = (TextView) mActivity.findViewById(R.id.tv_begin_date);
        mEndDateTextView = (TextView) mActivity.findViewById(R.id.tv_end_date);
        mSectionTextView = (TextView) mActivity.findViewById(R.id.tv_class_total_section);
        mRecyclerView = (RecyclerView) mActivity.findViewById(R.id.prrv);
        swipe_load = (SwipeRefreshLayoutBottom) mActivity.findViewById(R.id.swipe_load_frame);
        mNotResultTextView = (TextView) mActivity.findViewById(R.id.tv_not_result_text);

        //滚动条管理器，用在有Refresh的操作上
        ABaseLinearLayoutManager mLayoutManager = new ABaseLinearLayoutManager(mActivity);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);
        swipe_load.setOnLoadMoreListener(new SwipeRefreshLayoutBottom.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                swipe_load.setRefreshing(true);
                if (!canLoadMore) {
                    swipe_load.setRefreshing(false);
                    Toast.Short(mActivity, mActivity.getResources().getString(R.string.text_not_more_result));
                    return;
                }
                refreshListData();
            }
        });

        updateDateView();
        mToolBarManager
                .showBack(true)
                .showLeftImage(false)
                .setTitle(mActivity.getResources().getString(R.string.text_registered_record_title))
                .showRightButton(false)
                .setRightText("");
//        mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        mNotResultTextView.setVisibility(View.GONE);
        mBeginDateTextView.setOnClickListener(this);
        mEndDateTextView.setOnClickListener(this);
    }

    @NonNull
    @Override
    public AlreadyRegisterRecordPresenter createPresenter() {
        return new AlreadyRegisterRecordPresenter(getActivity());
    }

    @Override
    public void onDetach() {
        super.onDetach();
        presenter.detachView(false);
    }

    @Override
    public boolean onBackPress() {
        //把缓存清除
        if (RegisteredRecordManager.getSaveCacheDto() != null)
            RegisteredRecordManager.setSaveCacheDto(null);

        return super.onBackPress();
    }
}
