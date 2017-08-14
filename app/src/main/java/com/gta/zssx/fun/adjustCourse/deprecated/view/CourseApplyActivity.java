package com.gta.zssx.fun.adjustCourse.deprecated.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;

import com.gta.zssx.R;
import com.gta.zssx.fun.adjustCourse.model.bean.ScheduleBean;
import com.gta.zssx.fun.adjustCourse.view.page.CourseScheduleActivity;
import com.gta.zssx.fun.coursedaily.registercourse.view.adapter.CommonPageAdapter;
import com.gta.zssx.pub.base.BaseAppCompatActivity;
import com.gta.zssx.pub.manager.ToolBarManager;

import org.joda.time.DateTime;

import java.io.Serializable;
import java.util.ArrayList;
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
@Deprecated
public class CourseApplyActivity extends BaseAppCompatActivity {
    public static final String PAGE_POSITION = "pagePosition";
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private List<String> mTitleList;
    private Map<String, List<ScheduleBean.SectionBean>> mListMap;


    public Toolbar mToolbar;
    public ToolBarManager mToolBarManager;
    public int mPage;
    public List<ScheduleBean.SectionBean> mSectionBeen;
    public DateTime mDateTime;
    public int mWeekIndex;

    public static void start(Context context) {
        Intent lIntent = new Intent(context, CourseApplyActivity.class);
        if (!(context instanceof Activity)) {
            lIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(lIntent);
    }

    public static void start(Context context, int pagePosition, Map<String,
            List<ScheduleBean.SectionBean>> listMap, DateTime dateTime,int weekIndex) {
        Intent lIntent = new Intent(context, CourseApplyActivity.class);
        lIntent.putExtra(PAGE_POSITION, pagePosition);
        lIntent.putExtra(CourseScheduleActivity.WEEK_INDEX,weekIndex);
        Bundle lBundle = new Bundle();
        lBundle.putSerializable(CourseScheduleActivity.SCHEDULE, (Serializable) listMap);
        lBundle.putSerializable(CourseScheduleActivity.DATETIME,dateTime);
        lIntent.putExtras(lBundle);
        if (!(context instanceof Activity)) {
            lIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(lIntent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_apply);
        initData();
        initView();
        loadData();
    }


    //用于页面间数据接收
    private void initData() {

        mPage = getIntent().getIntExtra(PAGE_POSITION, 0);
        mListMap = (Map<String, List<ScheduleBean.SectionBean>>) getIntent().getExtras().getSerializable(CourseScheduleActivity.SCHEDULE);
        mDateTime = (DateTime) getIntent().getExtras().getSerializable(CourseScheduleActivity.DATETIME);
        mWeekIndex = getIntent().getIntExtra(CourseScheduleActivity.WEEK_INDEX,1);
        mTitleList = new ArrayList<>();
        mTitleList.add("调课申请");
        mTitleList.add("代课申请");
    }


    //初始化view
    private void initView() {
        findViews();
        initToolbar();
        setOnInteractListener();


    }

    private void loadData() {
        ArrayList<Fragment> lViewList = new ArrayList<>();
        if (mPage==0){
            lViewList.add(CourseApplyFragment.newInstance(CourseApplyFragment.COURSE_T,mListMap,mDateTime,mWeekIndex));
            lViewList.add(CourseApplyFragment.newInstance(CourseApplyFragment.COURSE_D,null,null,mWeekIndex));
        }else {
            lViewList.add(CourseApplyFragment.newInstance(CourseApplyFragment.COURSE_T,null,null,mWeekIndex));
            lViewList.add(CourseApplyFragment.newInstance(CourseApplyFragment.COURSE_D,mListMap,mDateTime,mWeekIndex));
        }

//        lViewList.add(CourseApplyFragment.newInstance(CourseApplyFragment.COURSE_T,mListMap,mDateTime));
//        lViewList.add(CourseApplyFragment.newInstance(CourseApplyFragment.COURSE_D,mListMap,mDateTime));

        for (int i = 0; i < mTitleList.size(); i++) {
            mTabLayout.addTab(mTabLayout.newTab().setText(mTitleList.get(i)));
        }
        CommonPageAdapter adapter = new CommonPageAdapter(getSupportFragmentManager(), lViewList, mTitleList);
        mViewPager.setAdapter(adapter);
        mTabLayout.setTabMode(TabLayout.MODE_FIXED);
        mTabLayout.setupWithViewPager(mViewPager);
        mViewPager.setCurrentItem(mPage);
    }


    private void initToolbar() {
        mToolBarManager.init();
        mToolbar.setNavigationOnClickListener(v -> onBackPressed());
        mToolBarManager.setTitle("调代课申请");

    }

    //事件处理
    private void setOnInteractListener() {

    }

    //绑定控件
    private void findViews() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolBarManager = new ToolBarManager(mToolbar, this);
        mTabLayout = (TabLayout) findViewById(R.id.course_apply_tablayout);
        mViewPager = (ViewPager) findViewById(R.id.course_apply_viewpager);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mToolBarManager.destroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }
}
