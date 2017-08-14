package com.gta.zssx.fun.adjustCourse.view.page.v2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.TimeUtils;
import com.gta.zssx.R;
import com.gta.zssx.fun.adjustCourse.model.bean.HasTimeScheduleBean;
import com.gta.zssx.fun.adjustCourse.model.bean.SearchArguments;
import com.gta.zssx.fun.adjustCourse.presenter.ClassSchedulePresenter;
import com.gta.zssx.fun.adjustCourse.view.ClassScheduleView;
import com.gta.zssx.fun.adjustCourse.view.adapter.HasTimeScheduleAdapter;
import com.gta.zssx.fun.adjustCourse.view.base.Constant;
import com.gta.zssx.fun.adjustCourse.view.page.CourseScheduleActivity;
import com.gta.zssx.fun.adjustCourse.view.page.DateChooseActivity;
import com.gta.zssx.pub.base.BaseMvpActivity;
import com.gta.zssx.pub.manager.ToolBarManager;
import com.gta.zssx.pub.util.DensityUtil;

import org.joda.time.DateTime;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import butterknife.Bind;
import butterknife.OnClick;

import static com.gta.zssx.fun.adjustCourse.view.page.CourseScheduleActivity.SEARCH_ARGUMENT;

/**
 * [Description]
 * <p>
 * [How to use]
 * <p>
 * [Tips]
 *
 * @author Created by Zhimin.Huang on 2017/3/13.
 * @since 1.0.0
 */
public class ClassScheduleActivity extends BaseMvpActivity<ClassScheduleView, ClassSchedulePresenter>
        implements ClassScheduleView {


    public static final int REQUEST_CODE = 0x0001;
    public static final int RESULT_CODE = 0x0002;
    public static final String SELECT_COURSE = "SelectCourse";
    public static final String SELECT_DAY = "selectDay";
    private RecyclerView mRecyclerView;
    private HorizontalScrollView mHorizontalScrollView;
    private SearchArguments mSearchArguments;
    private Date mDate;
    private DateTime mDateTime;
    private String[] mDay;
    public TextView mMonth;


    @Bind(R.id.choose_week_layout)
    RelativeLayout mWeekLayout;
    @Bind(R.id.course_count_layout)
    LinearLayout mCourseCountLayout;
    @Bind(R.id.confirm_course_t_btn)
    Button mCourseTConfirm;
    public List<String> mSelectKeys;

    public static void start4Result(Context context, SearchArguments searchArguments, int requestCode) {
        final Intent lIntent = new Intent(context, ClassScheduleActivity.class);
        Bundle lBundle = new Bundle();
        lBundle.putSerializable(SEARCH_ARGUMENT, searchArguments);
        lIntent.putExtras(lBundle);
        ((Activity) context).startActivityForResult(lIntent, requestCode);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_course_schedule;
    }

    @Override
    protected void initView() {
        Toolbar lToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolBarManager = new ToolBarManager(lToolbar, this);

        mRecyclerView = (RecyclerView) findViewById(R.id.rec);
        mHorizontalScrollView = (HorizontalScrollView) findViewById(R.id.scrollview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.HORIZONTAL, false));

        mToolBarManager.init();
        lToolbar.setNavigationOnClickListener(v -> onBackPressed());
        mHorizontalScrollView.post(() -> mHorizontalScrollView.scrollTo(0, 10));

        String lDate = mSearchArguments.getDate();
        mDate = new DateTime(lDate).toDate();
        presenter.getWeekDay(mDate);
        mToolBarManager
                .showScheduleLayout(true)
                .setScheduleTitle("请选择调课节次")
                .clickSchedule(v ->
                {
                    Intent lIntent = new Intent(mActivity, DateChooseActivity.class);
                    Bundle lBundle = new Bundle();
                    lBundle.putSerializable(CourseScheduleActivity.DATETIME, mDate);
                    lIntent.putExtras(lBundle);
                    startActivityForResult(lIntent, REQUEST_CODE);
                });

        mWeekLayout.setVisibility(View.GONE);
        mCourseTConfirm.setVisibility(View.VISIBLE);
    }


    @Override
    protected void initData() {
        //包括课表搜索的参数
        mSearchArguments = (SearchArguments) getIntent().getExtras().getSerializable(SEARCH_ARGUMENT);

    }

    @Override
    protected void requestData() {
        super.requestData();
        presenter.getSchedule(mSearchArguments.getSemesterId(), mSearchArguments.getTeacherUUId(), mSearchArguments.getDate(),
                mSearchArguments.getClassId(), mSearchArguments.getRoomParams(), mSearchArguments.getRoomId(),
                mSearchArguments.getCourseType());
    }

    @NonNull
    @Override
    public ClassSchedulePresenter createPresenter() {
        return new ClassSchedulePresenter();
    }

    @Override
    protected boolean isNeedToolbar() {
        return false;
    }

    @OnClick({R.id.confirm_course_t_btn})
    public void onClick(View view) {
        if (mSelectKeys != null) {
            if (mSelectKeys.size() != mSearchArguments.getCourseCount()) {
                presenter.showCourseMismatchDialog(mSearchArguments.getCourseCount(), mSelectKeys.size(), mActivity);
            } else {
                Intent lIntent = getIntent();
                Bundle lBundle = new Bundle();
                lBundle.putSerializable(SELECT_COURSE, (Serializable) mSelectKeys);
                String lS = mSelectKeys.get(0);
                String[] lSplit = lS.split(Pattern.quote(":"));
                DateTime lDateTime = mDateTime.plusDays(Integer.parseInt(lSplit[0]) - 1);
                lBundle.putSerializable(SELECT_DAY, lDateTime);
                lIntent.putExtras(lBundle);
                setResult(RESULT_CODE, lIntent);
                finish();
            }
        }
    }

    @Override
    public void getSchedule(HasTimeScheduleBean timeScheduleBean) {
        int lMaxUnit = timeScheduleBean.getMaxUnit();
        mCourseCountLayout.removeAllViews();
        mCourseCountLayout.setOrientation(LinearLayout.VERTICAL);

        for (int i = 0; i <= lMaxUnit; i++) {
            if (i == 0) {
                mCourseCountLayout.addView(createMonthText());
            } else {
                mCourseCountLayout.addView(createTextView(i));
            }
        }
        HasTimeScheduleAdapter.Listener lListener = new HasTimeScheduleAdapter.Listener() {
            @Override
            public void itemClick(List<String> mKeys) {
                mSelectKeys = mKeys;
            }

            @Override
            public void notifySectionMap() {
                if (mSelectKeys != null) {
                    mSelectKeys.clear();
                }
            }
        };
        if (mDay != null) {
            mSearchArguments.setDays(mDay);
        }
        HasTimeScheduleAdapter lAdapter = new HasTimeScheduleAdapter(mActivity, lListener, timeScheduleBean, mSearchArguments);
        mRecyclerView.setAdapter(lAdapter);
    }

    private TextView createTextView(int i) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
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
        mMonth.setText(String.valueOf(mDateTime.getMonthOfYear() + "月"));
        mMonth.setLayoutParams(params);
        return mMonth;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == DateChooseActivity.SEARCH_SCHEDULE_DATE_RESULT_CODE) {
            mDate = (Date) data.getExtras().getSerializable(DateChooseActivity.DATE);
            mSearchArguments.setDate(new DateTime(mDate).toString(Constant.DATE_TYPE_01));
            mSearchArguments.setWeekIndex(TimeUtils.getWeekIndex(mDate) - 2);
            mSearchArguments.setKey(null);
            requestData();
            presenter.getWeekDay(mDate);
        }
    }

    @Override
    public void showDateString(String date, DateTime dateTime, String[] weekDate) {
        mToolBarManager.setScheduleDate(date);
        mDateTime = dateTime;
        mDay = weekDate;

    }
}
