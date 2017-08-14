package com.gta.zssx.fun.adjustCourse.view.page;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.gta.zssx.R;
import com.gta.zssx.fun.adjustCourse.view.base.Constant;
import com.gta.zssx.pub.base.BaseAppCompatActivity;
import com.gta.zssx.pub.manager.ToolBarManager;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.format.TitleFormatter;

import org.joda.time.DateTime;

import java.util.Calendar;
import java.util.Date;

/**
 * [Description]
 * <p>
 * [How to use]
 * <p>
 * [Tips]
 *
 * @author Created by Zhimin.Huang on 2016/10/26.
 * @since 1.0.0
 */
public class DateChooseActivity extends BaseAppCompatActivity {

    public static final int SEARCH_SCHEDULE_DATE_RESULT_CODE = 101;
    public static final String DATE = "date";
    public Toolbar mToolbar;
    public ToolBarManager mToolBarManager;
    public MaterialCalendarView mCalendarView;
    public Date mDate = new Date();

    public static void start(Context context) {
        final Intent lIntent = new Intent(context, DateChooseActivity.class);
        if (!(context instanceof Activity)) {
            lIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(lIntent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_choose);
        initData();
        initView();
        loadData();
    }


    //用于页面间数据接收
    private void initData() {
        mDate = (Date) getIntent().getExtras().getSerializable(CourseScheduleActivity.DATETIME);
    }


    //初始化view
    private void initView() {
        findViews();
        initToolbar();
        setOnInteractListener();


    }

    private void loadData() {

    }


    private void initToolbar() {
        mToolBarManager.init();
        mToolbar.setNavigationOnClickListener(v -> onBackPressed());

        mToolBarManager.setTitle(getString(R.string.choose_date))
                .setRightText(getString(R.string.confirm))
                .clickRightButton(v -> {
                    Intent lIntent = getIntent();
                    Bundle lBundle = new Bundle();
                    lBundle.putSerializable(DATE, mDate);
                    lIntent.putExtras(lBundle);
                    setResult(SEARCH_SCHEDULE_DATE_RESULT_CODE, lIntent);
                    finish();
                });

    }

    //事件处理
    private void setOnInteractListener() {

        mCalendarView.setSelectedDate(mDate);
        mCalendarView.setTitleFormatter(new TitleFormatter() {
            @Override
            public CharSequence format(CalendarDay day) {
                DateTime lDateTime = new DateTime(day.getDate());
                return lDateTime.toString(Constant.DATE_TYPE_02);
            }
        });

        mCalendarView.state().edit()
                .setCalendarDisplayMode(CalendarMode.MONTHS)
                .setFirstDayOfWeek(Calendar.MONDAY)
                .commit();
        mCalendarView.setOnDateChangedListener((widget, date, selected) -> {
            mDate = date.getDate();
//            Toast.makeText(mActivity, mDate.getTime() + "", Toast.LENGTH_LONG).show();
        });

    }


    //绑定控件
    private void findViews() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolBarManager = new ToolBarManager(mToolbar, this);

        mCalendarView = (MaterialCalendarView) findViewById(R.id.calendarView);

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mToolBarManager.destroy();
    }
}
