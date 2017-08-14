package com.gta.zssx.fun.adjustCourse.view.page.v2;

import android.os.Bundle;
import android.view.View;

import com.gta.zssx.R;
import com.gta.zssx.fun.adjustCourse.model.bean.ScheduleBean;
import com.gta.zssx.fun.adjustCourse.view.base.BaseViewPagerFragment;
import com.gta.zssx.fun.adjustCourse.view.base.Constant;
import com.gta.zssx.fun.adjustCourse.view.page.CourseScheduleActivity;

import org.joda.time.DateTime;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import static com.gta.zssx.fun.adjustCourse.deprecated.view.CourseApplyActivity.PAGE_POSITION;
import static com.gta.zssx.fun.adjustCourse.deprecated.view.CourseApplyFragment.sType;

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
public class CourseApplyPageFragment extends BaseViewPagerFragment {

    private Map<String, List<ScheduleBean.SectionBean>> mAppListMap;
    private DateTime mAppleDateTime;
    private int mWeekNum;
    private int mPage;

    public static CourseApplyPageFragment newInstance() {
        Bundle args = new Bundle();
        CourseApplyPageFragment fragment = new CourseApplyPageFragment();
        fragment.setArguments(args);
        return fragment;
    }


    public static CourseApplyPageFragment newInstance(Bundle bundle) {
        CourseApplyPageFragment fragment = new CourseApplyPageFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void initData() {
        super.initData();
        mPage = getArguments().getInt(PAGE_POSITION, 0);
        mAppListMap = (Map<String, List<ScheduleBean.SectionBean>>) getArguments().getSerializable(CourseScheduleActivity.SCHEDULE);
        mAppleDateTime = (DateTime) getArguments().getSerializable(CourseScheduleActivity.DATETIME);
        mWeekNum = getArguments().getInt(CourseScheduleActivity.WEEK_INDEX, 1);
    }

    @Override
    protected void initView(View view) {
        super.initView(view);
        mBaseViewPager.setCurrentItem(mPage, true);
    }

    @Override
    protected PagerInfo[] getPagers() {
//        if (mPage == 1) {
//            return new PagerInfo[]{
//                    new PagerInfo(getString(R.string.adjust_time), CourseApplyDetailFragment.class, getBundle(1)),
//                    new PagerInfo(getString(R.string.adjust_teacher), CourseApplyDetailFragment.class, getBundle(2)),
//                    new PagerInfo(getString(R.string.repleace_course), CourseApplyDetailFragment.class, getBundle(3))
//            };
//        } else if (mPage == 2) {
//            return new PagerInfo[]{
//                    new PagerInfo(getString(R.string.adjust_time), CourseApplyDetailFragment.class, getBundle(1)),
//                    new PagerInfo(getString(R.string.adjust_teacher), CourseApplyDetailFragment.class, getBundle(2)),
//                    new PagerInfo(getString(R.string.repleace_course), CourseApplyDetailFragment.class, getBundle(3))
//            };
//        } else {
//            return new PagerInfo[]{
//                    new PagerInfo(getString(R.string.adjust_time), CourseApplyDetailFragment.class, getBundle(1)),
//                    new PagerInfo(getString(R.string.adjust_teacher), CourseApplyDetailFragment.class, getBundle(2)),
//                    new PagerInfo(getString(R.string.repleace_course), CourseApplyDetailFragment.class, getBundle(3))
//            };
//        }

        return new PagerInfo[]{
                new PagerInfo(getString(R.string.adjust_time), CourseApplyDetailFragment.class, getBundle(1)),
                new PagerInfo(getString(R.string.adjust_teacher), CourseApplyDetailFragment.class, getBundle(2)),
                new PagerInfo(getString(R.string.repleace_course), CourseApplyDetailFragment.class, getBundle(3))
        };
    }

    private Bundle getBundle(int i) {
        if (mPage == 1) {
            switch (i) {
                default:
                case 1:
                    Bundle lBundle1 = new Bundle();
                    lBundle1.putSerializable(CourseScheduleActivity.SCHEDULE, null);
                    lBundle1.putSerializable(CourseScheduleActivity.DATETIME, null);
                    lBundle1.putString(sType, Constant.COURSE_S);
                    lBundle1.putInt(CourseScheduleActivity.WEEK_INDEX, mWeekNum);
                    return lBundle1;
                case 2:

                    Bundle lBundle = new Bundle();
                    lBundle.putSerializable(CourseScheduleActivity.SCHEDULE, (Serializable) mAppListMap);
                    lBundle.putSerializable(CourseScheduleActivity.DATETIME, mAppleDateTime);
                    lBundle.putString(sType, Constant.COURSE_T);
                    lBundle.putInt(CourseScheduleActivity.WEEK_INDEX, mWeekNum);
                    return lBundle;
                case 3:
                    Bundle lBundle2 = new Bundle();
                    lBundle2.putSerializable(CourseScheduleActivity.SCHEDULE, null);
                    lBundle2.putSerializable(CourseScheduleActivity.DATETIME, null);
                    lBundle2.putString(sType, Constant.COURSE_D);
                    lBundle2.putInt(CourseScheduleActivity.WEEK_INDEX, mWeekNum);
                    return lBundle2;
            }
        } else if (mPage == 2) {
            switch (i) {
                default:
                case 1:
                    Bundle lBundle1 = new Bundle();
                    lBundle1.putSerializable(CourseScheduleActivity.SCHEDULE, null);
                    lBundle1.putSerializable(CourseScheduleActivity.DATETIME, null);
                    lBundle1.putString(sType, Constant.COURSE_S);
                    lBundle1.putInt(CourseScheduleActivity.WEEK_INDEX, mWeekNum);
                    return lBundle1;
                case 2:
                    Bundle lBundle = new Bundle();
                    lBundle.putSerializable(CourseScheduleActivity.SCHEDULE, null);
                    lBundle.putSerializable(CourseScheduleActivity.DATETIME, null);
                    lBundle.putString(sType, Constant.COURSE_T);
                    lBundle.putInt(CourseScheduleActivity.WEEK_INDEX, mWeekNum);
                    return lBundle;
                case 3:
                    Bundle lBundle2 = new Bundle();
                    lBundle2.putSerializable(CourseScheduleActivity.SCHEDULE, (Serializable) mAppListMap);
                    lBundle2.putSerializable(CourseScheduleActivity.DATETIME, mAppleDateTime);
                    lBundle2.putString(sType, Constant.COURSE_D);
                    lBundle2.putInt(CourseScheduleActivity.WEEK_INDEX, mWeekNum);
                    return lBundle2;
            }
        } else {
            switch (i) {
                default:
                case 1:
                    Bundle lBundle1 = new Bundle();
                    lBundle1.putSerializable(CourseScheduleActivity.SCHEDULE, null);
                    lBundle1.putSerializable(CourseScheduleActivity.DATETIME, null);
                    lBundle1.putString(sType, Constant.COURSE_S);
                    lBundle1.putInt(CourseScheduleActivity.WEEK_INDEX, mWeekNum);
                    return lBundle1;
                case 2:
                    Bundle lBundle = new Bundle();
                    lBundle.putSerializable(CourseScheduleActivity.SCHEDULE, null);
                    lBundle.putSerializable(CourseScheduleActivity.DATETIME, null);
                    lBundle.putString(sType, Constant.COURSE_T);
                    lBundle.putInt(CourseScheduleActivity.WEEK_INDEX, mWeekNum);
                    return lBundle;
                case 3:
                    Bundle lBundle2 = new Bundle();
                    lBundle2.putSerializable(CourseScheduleActivity.SCHEDULE, null);
                    lBundle2.putSerializable(CourseScheduleActivity.DATETIME, null);
                    lBundle2.putString(sType, Constant.COURSE_D);
                    lBundle2.putInt(CourseScheduleActivity.WEEK_INDEX, mWeekNum);
                    return lBundle2;
            }
        }
    }
}
