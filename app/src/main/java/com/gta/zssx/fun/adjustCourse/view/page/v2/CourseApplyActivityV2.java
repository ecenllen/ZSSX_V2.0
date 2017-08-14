package com.gta.zssx.fun.adjustCourse.view.page.v2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import com.gta.zssx.R;
import com.gta.zssx.fun.adjustCourse.model.bean.ScheduleBean;
import com.gta.zssx.fun.adjustCourse.view.page.CourseScheduleActivity;
import com.gta.zssx.pub.base.BaseCommonActivity;

import org.joda.time.DateTime;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import static com.gta.zssx.fun.adjustCourse.deprecated.view.CourseApplyActivity.PAGE_POSITION;

/**
 * [Description]
 * <p>
 * [How to use]
 * <p>
 * [Tips]
 *
 * @author Created by Zhimin.Huang on 2017/3/21.
 * @since 1.0.0
 */
public class CourseApplyActivityV2 extends BaseCommonActivity {


    //页面
    private int mPage;
    //选择的节次
    private Map<String, List<ScheduleBean.SectionBean>> mListMap;
    private DateTime mDateTime;
    //周次
    private int mWeekIndex;

    public static void start(Context context) {
        Intent lIntent = new Intent(context, CourseApplyActivityV2.class);
        if (!(context instanceof Activity)) {
            lIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(lIntent);
    }

    public static void start(Context context, int pagePosition, Map<String,
            List<ScheduleBean.SectionBean>> listMap, DateTime dateTime, int weekIndex) {
        Intent lIntent = new Intent(context, CourseApplyActivityV2.class);
        lIntent.putExtra(PAGE_POSITION, pagePosition);
        lIntent.putExtra(CourseScheduleActivity.WEEK_INDEX, weekIndex);
        Bundle lBundle = new Bundle();
        lBundle.putSerializable(CourseScheduleActivity.SCHEDULE, (Serializable) listMap);
        lBundle.putSerializable(CourseScheduleActivity.DATETIME, dateTime);
        lIntent.putExtras(lBundle);
        if (!(context instanceof Activity)) {
            lIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(lIntent);
    }

    @Override
    protected void initData() {
        super.initData();

        mPage = getIntent().getIntExtra(PAGE_POSITION, 0);
        mListMap = (Map<String, List<ScheduleBean.SectionBean>>) getIntent().getExtras().getSerializable(CourseScheduleActivity.SCHEDULE);
        mDateTime = (DateTime) getIntent().getExtras().getSerializable(CourseScheduleActivity.DATETIME);
        mWeekIndex = getIntent().getIntExtra(CourseScheduleActivity.WEEK_INDEX, 1);
    }



    @Override
    public int getLayoutId() {
        return R.layout.activity_common_container;
    }

    @Override
    protected void initView() {
        mToolBarManager.setTitle(getString(R.string.course_apply_title));
        Bundle lBundle = new Bundle();
        lBundle.putInt(PAGE_POSITION, mPage);
        lBundle.putSerializable(CourseScheduleActivity.WEEK_INDEX, mWeekIndex);
        lBundle.putSerializable(CourseScheduleActivity.SCHEDULE, (Serializable) mListMap);
        lBundle.putSerializable(CourseScheduleActivity.DATETIME, mDateTime);
        FragmentManager lSupportFragmentManager = getSupportFragmentManager();
        lSupportFragmentManager.beginTransaction().add(R.id.fragment_container, CourseApplyPageFragment.newInstance(lBundle)).commit();
    }
}
