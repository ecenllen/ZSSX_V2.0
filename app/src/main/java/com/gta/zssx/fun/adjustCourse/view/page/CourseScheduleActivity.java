package com.gta.zssx.fun.adjustCourse.view.page;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.TimeUtils;
import com.gta.zssx.AppConfiguration;
import com.gta.zssx.R;
import com.gta.zssx.fun.adjustCourse.model.bean.ScheduleBean;
import com.gta.zssx.fun.adjustCourse.model.bean.SearchArguments;
import com.gta.zssx.fun.adjustCourse.model.utils.SoftKeyboardStateHelper;
import com.gta.zssx.fun.adjustCourse.presenter.CourseSchedulePresenter;
import com.gta.zssx.fun.adjustCourse.view.CenterItemDialog;
import com.gta.zssx.fun.adjustCourse.view.CourseScheduleView;
import com.gta.zssx.fun.adjustCourse.view.adapter.ScheduleAdapter;
import com.gta.zssx.fun.adjustCourse.view.base.Constant;
import com.gta.zssx.fun.adjustCourse.view.page.v2.CourseApplyActivityV2;
import com.gta.zssx.fun.personalcenter.model.bean.UserBean;
import com.gta.zssx.pub.base.BaseActivity;
import com.gta.zssx.pub.manager.ToolBarManager;
import com.gta.zssx.pub.util.DensityUtil;
import com.gta.zssx.pub.util.LogUtil;

import org.joda.time.DateTime;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * [Description]
 * <p>
 * [How to use]
 * <p>
 * [Tips]
 *
 * @author Created by Zhimin.Huang on 2016/10/24.
 * @since 1.0.0
 */
public class CourseScheduleActivity extends BaseActivity<CourseScheduleView, CourseSchedulePresenter> implements CourseScheduleView {
    public static final int REQUEST_CODE = 100;
    public static final String SCHEDULE_TYPE = "type";
    public static final String COURSE_TYPE = "course_type";
    public static final String SCHEDULE = "schedule";
    public static final String DATETIME = "datetime";
    public static final String SEARCH_ARGUMENT = "searchArgument";
    public static final int CLASS_SCHEDULE = 1;
    public static final int TEACHER_SCHEDULE = 3;
    public static final String WEEK_INDEX = "weekIndex";
    public static final int APPLY_AND_ADJUST_RESULT_CODE = 103;
    public static String CLICKABLE = "clickable";
    public static String LOOK = "look";
    private RecyclerView mRecyclerView;
    private HorizontalScrollView mHorizontalScrollView;
    private Date mDate;
    private TextView mPlus;
    private TextView mReduce;
    private EditText mWeekEt;
    private Button mCourseDConfirm;
    private RelativeLayout mWeekLayout;
    private List<String> mStrings;
    private Map<String, List<ScheduleBean.SectionBean>> mListMap;
    private String mScheduleType;
    private String mCourseType;
    private Button mCourseTConfirm;
    public int mColumn;
    public DateTime mDateTime;
    public SearchArguments mSearchArguments;
    private int defaultWeek = 1;
    public UserBean mUserBean;
    private String[] mDay;
    private LinearLayout mCourseCountLayout;
    public TextView mMonth;


    @NonNull
    @Override
    public CourseSchedulePresenter createPresenter() {
        return new CourseSchedulePresenter();
    }

    private Toolbar mToolbar;
    private ToolBarManager mToolBarManager;

    /**
     * @param context      上下文
     * @param scheduleType 用于标记课程表能不能点击
     * @param courseType   用于区分是调课类型还是代课类型课表，只有scheduleType是CLICKABLE类型时才起作用
     * @param requestCode  请求码
     */
    public static void start4Result(Context context, String scheduleType, String courseType, int requestCode) {
        final Intent lIntent = new Intent(context, CourseScheduleActivity.class);
        lIntent.putExtra(SCHEDULE_TYPE, scheduleType);
        lIntent.putExtra(COURSE_TYPE, courseType);
        if (!(context instanceof Activity)) {
            lIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        ((Activity) context).startActivityForResult(lIntent, requestCode);
    }

    /**
     * @param context      上下文
     * @param scheduleType 用于标记课程表能不能点击
     * @param courseType   用于区分是调课类型还是代课类型课表，只有scheduleType是CLICKABLE类型时才起作用
     */
    public static void start(Context context, String scheduleType, String courseType) {
        final Intent lIntent = new Intent(context, CourseScheduleActivity.class);
        lIntent.putExtra(SCHEDULE_TYPE, scheduleType);
        lIntent.putExtra(COURSE_TYPE, courseType);
        if (!(context instanceof Activity)) {
            lIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(lIntent);
    }

    /**
     * @param context      上下文
     * @param scheduleType 用于标记课程表能不能点击
     * @param courseType   用于区分是调课类型还是代课类型课表，只有scheduleType是CLICKABLE类型时才起作用
     * @param arguments    搜索传过来的参数
     */
    public static void start(Context context, String scheduleType, String courseType, SearchArguments arguments) {
        final Intent lIntent = new Intent(context, CourseScheduleActivity.class);

        Bundle lBundle = new Bundle();
        lBundle.putSerializable(SEARCH_ARGUMENT, arguments);
        lIntent.putExtra(SCHEDULE_TYPE, scheduleType);
        lIntent.putExtra(COURSE_TYPE, courseType);

        lIntent.putExtras(lBundle);

        if (!(context instanceof Activity)) {
            lIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(lIntent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_schedule);
        initData();
        initView();
        loadData();
    }


    //用于页面间数据接收
    private void initData() {
        mDate = new Date();
        mStrings = new ArrayList<>();
        try {
            mUserBean = AppConfiguration.getInstance().getUserBean();
        } catch (Exception e) {
            e.printStackTrace();
        }

        //用于区分课表的课程是否可以被选中
        mScheduleType = getIntent().getStringExtra(SCHEDULE_TYPE);
        //用于区分调课、代课、调代课详情类型的课表
        mCourseType = getIntent().getStringExtra(COURSE_TYPE);
        //包括课表搜索的参数
        mSearchArguments = (SearchArguments) getIntent().getExtras().getSerializable(SEARCH_ARGUMENT);
    }


    //初始化view
    private void initView() {
        findViews();
        initToolbar();
        setOnInteractListener();
    }

    private void loadData() {
        presenter.getSchedule(mSearchArguments.getTeacherUUId(), mSearchArguments.getDate(), mSearchArguments.getSemesterId(), mSearchArguments.getFlag(), mSearchArguments.getClassId());
    }


    private void initToolbar() {
        mToolBarManager.init();
        mToolbar.setNavigationOnClickListener(v -> onBackPressed());

        String lScheduleName = mSearchArguments.getScheduleName();
        if (mScheduleType.equals(LOOK)) {
            if (mSearchArguments.isFromSearch()) {
                String lDate = mSearchArguments.getDate();
                Date lDate1 = new DateTime(lDate).toDate();
                presenter.getWeekDay(lDate == null ? mDate : lDate1);
                mDate = lDate1;
                mToolBarManager
                        .renew()
                        .showScheduleLayout(true)
                        .setScheduleTitle(lScheduleName)
                        .clickSchedule(v ->
                        {
                            Intent lIntent = new Intent(mActivity, DateChooseActivity.class);
                            Bundle lBundle = new Bundle();
                            lBundle.putSerializable(CourseScheduleActivity.DATETIME, mDate);
                            lIntent.putExtras(lBundle);
                            startActivityForResult(lIntent, REQUEST_CODE);
                        });
            } else {
                mToolBarManager.renew()
                        .setTitle(getString(R.string.schedule));
            }
        } else {
            mToolBarManager
                    .renew()
                    .showScheduleLayout(true)
                    .setScheduleTitle(lScheduleName)
                    .clickSchedule(v ->
                    {
                        Intent lIntent = new Intent(mActivity, DateChooseActivity.class);
                        Bundle lBundle = new Bundle();
                        lBundle.putSerializable(CourseScheduleActivity.DATETIME, mDate);
                        lIntent.putExtras(lBundle);
                        startActivityForResult(lIntent, REQUEST_CODE);
                    });

            String lDate = mSearchArguments.getDate();
            Date lDate1 = new DateTime(lDate).toDate();
            presenter.getWeekDay(lDate == null ? mDate : lDate1);
            mDate = lDate1;
        }

    }

    //事件处理
    private void setOnInteractListener() {

        mHorizontalScrollView.post(() -> mHorizontalScrollView.scrollTo(0, 10));


        if (mScheduleType.equals(LOOK)) {
            //查看状态
            mWeekLayout.setVisibility(View.GONE);
            mCourseTConfirm.setVisibility(View.GONE);
        } else {
            //代课课表
            if (mCourseType.equals(Constant.COURSE_D)) {
                mWeekLayout.setVisibility(View.VISIBLE);
                mCourseTConfirm.setVisibility(View.GONE);
            } else {
                //调课课表
                mWeekLayout.setVisibility(View.GONE);
                mCourseTConfirm.setVisibility(View.VISIBLE);
            }
        }

        mCourseDConfirm.setOnClickListener(v -> {
            if (mScheduleType.equals(CLICKABLE)) {
                if (mListMap == null || mListMap.size() == 0) {
                    showInfo(getString(R.string.write_course_first));
                    return;
                }
                if (mSearchArguments.isFromSearch()) {
                    CourseApplyActivityV2.start(mActivity, mSearchArguments.getPage(), mListMap, mDateTime.plusDays(mColumn), defaultWeek);
                } else {
                    Intent lIntent = getIntent();
                    Bundle lBundle = new Bundle();
                    lBundle.putSerializable(SCHEDULE, (Serializable) mListMap);
                    lBundle.putInt(WEEK_INDEX, defaultWeek);
                    DateTime lDateTime = mDateTime.plusDays(mColumn);
                    lBundle.putSerializable(DATETIME, lDateTime);
                    lIntent.putExtras(lBundle);
                    setResult(APPLY_AND_ADJUST_RESULT_CODE, lIntent);
                    finish();
                }

            }
        });

        mCourseTConfirm.setOnClickListener(v -> {
            if (mListMap == null || mListMap.size() == 0) {
                showInfo(getString(R.string.write_course_first));
                return;
            }
            if (mSearchArguments.getCourseCount() != 0 && mSearchArguments.getCourseCount() != mListMap.size()) {
                presenter.showCourseMismatchDialog(mSearchArguments.getCourseCount(), mListMap.size(), mActivity);
            } else {
                if (mSearchArguments.isFromSearch()) {
                    CourseApplyActivityV2.start(mActivity, mSearchArguments.getPage(), mListMap, mDateTime.plusDays(mColumn), defaultWeek);
                } else {
                    Intent lIntent = getIntent();
                    Bundle lBundle = new Bundle();
                    lBundle.putSerializable(SCHEDULE, (Serializable) mListMap);
                    lBundle.putInt(WEEK_INDEX, defaultWeek);
                    DateTime lDateTime = mDateTime.plusDays(mColumn);
                    lBundle.putSerializable(DATETIME, lDateTime);
                    lIntent.putExtras(lBundle);
                    setResult(APPLY_AND_ADJUST_RESULT_CODE, lIntent);
                    finish();
                }
            }

        });

        mPlus.setOnClickListener(v -> {
            if (defaultWeek < 25) {
                defaultWeek++;
                mWeekEt.setText(String.valueOf(defaultWeek));
            }
        });

        mReduce.setOnClickListener(v -> {
            if (defaultWeek > 1) {
                defaultWeek--;
                mWeekEt.setText(String.valueOf(defaultWeek));
            }
        });

        mWeekEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String lBasePath = s.toString();
                if (!lBasePath.equals("")) {
                    Integer lInteger = Integer.valueOf(lBasePath);
                    if (lInteger > 0 && lInteger < 26) {
                        defaultWeek = lInteger;
                    } else {
                        mWeekEt.setText(String.valueOf(defaultWeek));
                    }
                }
            }
        });


        final SoftKeyboardStateHelper softKeyboardStateHelper = new SoftKeyboardStateHelper(findViewById(R.id.course_root_layout));
        softKeyboardStateHelper.addSoftKeyboardStateListener(new SoftKeyboardStateHelper.SoftKeyboardStateListener() {
            @Override
            public void onSoftKeyboardOpened(int keyboardHeightInPx) {
                mWeekEt.setCursorVisible(true);
            }

            @Override
            public void onSoftKeyboardClosed() {
                mWeekEt.setCursorVisible(false);
            }
        });

    }

    //绑定控件
    private void findViews() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolBarManager = new ToolBarManager(mToolbar, this);

        mRecyclerView = (RecyclerView) findViewById(R.id.rec);
        mHorizontalScrollView = (HorizontalScrollView) findViewById(R.id.scrollview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.HORIZONTAL, false));

        mPlus = (TextView) findViewById(R.id.plus_btn);
        mReduce = (TextView) findViewById(R.id.reduce_btn);
        mWeekEt = (EditText) findViewById(R.id.week_index_et);
        mCourseDConfirm = (Button) findViewById(R.id.confirm_course_d_btn);
        mCourseTConfirm = (Button) findViewById(R.id.confirm_course_t_btn);
        mWeekLayout = (RelativeLayout) findViewById(R.id.choose_week_layout);
        mCourseCountLayout = (LinearLayout) findViewById(R.id.course_count_layout);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mToolBarManager.destroy();
    }


    @Override
    public void showResult(ScheduleBean scheduleBean) {

        int lMaxUnit = scheduleBean.getMaxUnit();
        mCourseCountLayout.removeAllViews();
        mCourseCountLayout.setOrientation(LinearLayout.VERTICAL);

        for (int i = 0; i <= lMaxUnit; i++) {
            if (i == 0) {
                mCourseCountLayout.addView(createMonthText());
            } else {
                mCourseCountLayout.addView(createTextView(i));
            }
        }


        ScheduleAdapter.Listener lListener = new ScheduleAdapter.Listener() {
            @Override
            public void itemClick(Map<String, List<ScheduleBean.SectionBean>> listMap, int column) {
                mListMap = listMap;
                mColumn = column;
                LogUtil.Log("ListMap", mListMap == null ? "null" : mListMap.size() + "");
            }

            @Override
            public void itemLongClick(List<ScheduleBean.SectionBean> beanList, int column, String key) {
                ScheduleBean.SectionBean lSectionBean = beanList.get(0);
                String lTransferType = lSectionBean.getTransferType();

                if (mScheduleType.equals(CLICKABLE)) {
                    mStrings.clear();
                    mStrings.add(getString(R.string.check_all_text));
                    showCenterItem(mStrings, beanList, column, key);
                } else {
                    if (mCourseType.equals(Constant.COURSE_N) || mSearchArguments.getFlag() == CourseScheduleActivity.CLASS_SCHEDULE) {
                        mStrings.clear();
                        mStrings.add(getString(R.string.check_all_text));
                        showCenterItem(mStrings, beanList, column, key);
                    } else {
                        mStrings.clear();
                        //教师课表时，只有看自己的看表时才能发起申请，否则查看只有全文
                        if (mSearchArguments.getFlag() == CourseScheduleActivity.TEACHER_SCHEDULE && mSearchArguments.getTeacherUUId().equals(mUserBean.getUserId())) {
                            //并且只有没有被调课的状态才能申请
                            if (lTransferType == null) {
                                mStrings.add(getString(R.string.apply_adjust_course));
                                mStrings.add(getString(R.string.apply_substitute_course));
                                mStrings.add(getString(R.string.check_all_text));
                            } else {
                                mStrings.add(getString(R.string.check_all_text));
                            }
                        } else {
                            mStrings.add(getString(R.string.check_all_text));
                        }

                        showCenterItem(mStrings, beanList, column, key);
                    }

                }

            }

            @Override
            public void notifySectionMap() {
                if (mListMap != null) {
                    mListMap.clear();
                }
            }
        };
        if (mDay != null) {
            mSearchArguments.setDays(mDay);
        }
        mRecyclerView.setAdapter(new ScheduleAdapter(mActivity, scheduleBean, lListener, mScheduleType, mSearchArguments, mCourseType));

        String key = mSearchArguments.getKey();
//        长按选择对应key的课程（多阶段的除外，先在adapter里面执行弹框）
        if (key != null) {
            List<ScheduleBean.SectionBean> sectionBeen = scheduleBean.getCourseListMap().get(key);
            ScheduleBean.SectionBean lSectionBean = sectionBeen.get(0);
            if (lSectionBean.getOpenCourseType() == 15 || lSectionBean.getOpenCourseType() == 0) {
                mListMap = new HashMap<>();
                mListMap.put(key, sectionBeen);
                mColumn = Integer.valueOf(key.substring(1, 2)) - 1;
            }
        }

    }

    private TextView createMonthText() {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 0, 0, DensityUtil.dip2px(mActivity, 1));
        mMonth = new TextView(mActivity);
        mMonth.setWidth(DensityUtil.dip2px(mActivity, 28));
        mMonth.setHeight(DensityUtil.dip2px(mActivity, 50));
        mMonth.setGravity(Gravity.CENTER);
        mMonth.setTextColor(ContextCompat.getColor(mActivity, R.color.gray_999999));
        mMonth.setBackgroundResource(R.color.wirte_ffffff);
        mMonth.setTextSize(13);
        mMonth.setPadding(0, DensityUtil.dip2px(mActivity, 7), 0, 0);
        if (mScheduleType.equals(LOOK)) {
            if (!mSearchArguments.isFromSearch()) {
                mDateTime = new DateTime(mSearchArguments.getDate());
            }
        }
        mMonth.setText(String.valueOf(mDateTime.getMonthOfYear() + "月"));
        mMonth.setLayoutParams(params);
        return mMonth;
    }

    private TextView createTextView(int i) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 0, 0, DensityUtil.dip2px(mActivity, 1));
        TextView txt = new TextView(mActivity);
        txt.setWidth(DensityUtil.dip2px(mActivity, 28));
        txt.setHeight(DensityUtil.dip2px(mActivity, 80));
        txt.setGravity(Gravity.CENTER);
        txt.setTextColor(ContextCompat.getColor(mActivity, R.color.gray_999999));
        txt.setText(String.valueOf(i));
        txt.setBackgroundResource(R.color.wirte_ffffff);
        txt.setTextSize(13);
        txt.setLayoutParams(params);
        return txt;
    }

    private void showCenterItem(List<String> list, List<ScheduleBean.SectionBean> beanList, int column, String key) {
        new CenterItemDialog(list, mActivity)
                .setOnItemClickListener(position -> {
                    if (list.size() == 1) {
                        new DetailSchedulePopupWindow(mActivity, "adjust")
                                .setmBeanList(beanList)
                                .onCreate();
                    } else {
                        beanList.get(0).setDateString(mDateTime.plusDays(column).toString(Constant.DATE_TYPE_01));
                        switch (position) {
                            //调课申请
                            case 0:
                                mSearchArguments.setPage(1);
                                mSearchArguments.setKey(key);
                                int weekIndex = Integer.valueOf(key.substring(1, 2)) - 1;
                                mSearchArguments.setWeekIndex(weekIndex);
                                CourseScheduleActivity.start(mActivity, CourseScheduleActivity.CLICKABLE, Constant.COURSE_T, mSearchArguments);
                                break;
                            //代课申请
                            case 1:
                                mSearchArguments.setKey(key);
                                mSearchArguments.setPage(2);
                                int weekIndex1 = Integer.valueOf(key.substring(1, 2)) - 1;
                                mSearchArguments.setWeekIndex(weekIndex1);
                                CourseScheduleActivity.start(mActivity, CourseScheduleActivity.CLICKABLE, Constant.COURSE_D, mSearchArguments);
                                break;
                            default:
                            case 2:
                                new DetailSchedulePopupWindow(mActivity, "adjust")
                                        .setmBeanList(beanList)
                                        .onCreate();
                                break;

                        }
                    }
                })
                .create();
    }

    @Override
    public void showDateString(String date, DateTime dateTime, String day[]) {
        mToolBarManager.setScheduleDate(date);
        mDateTime = dateTime;
        mDay = day;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == DateChooseActivity.SEARCH_SCHEDULE_DATE_RESULT_CODE) {
            mDate = (Date) data.getExtras().getSerializable(DateChooseActivity.DATE);
            mSearchArguments.setDate(new DateTime(mDate).toString(Constant.DATE_TYPE_01));
            mSearchArguments.setWeekIndex(TimeUtils.getWeekIndex(mDate) - 2);
            mSearchArguments.setKey(null);
            loadData();
            presenter.getWeekDay(mDate);
        }

    }


}
