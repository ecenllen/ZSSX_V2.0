package com.gta.zssx.fun.adjustCourse.view.page;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;

import com.gta.zssx.R;
import com.gta.zssx.fun.coursedaily.registercourse.view.adapter.CommonPageAdapter;
import com.gta.zssx.pub.base.BaseAppCompatActivity;
import com.gta.zssx.pub.manager.ToolBarManager;

import java.util.ArrayList;
import java.util.List;


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
public class ScheduleSearchActivity extends BaseAppCompatActivity {

    public static boolean isFinish;
    private List<String> mTitleList;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;


    private Toolbar mToolbar;
    public ToolBarManager mToolBarManager;

    public static void start(Context context) {
        final Intent lIntent = new Intent(context, ScheduleSearchActivity.class);
        if (!(context instanceof Activity)) {
            lIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(lIntent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_search);
        initData();
        initView();
        loadData();
    }


    //用于页面间数据接收
    private void initData() {
        mTitleList = new ArrayList<>();
        mTitleList.add(getString(R.string.class_schedule));
        mTitleList.add(getString(R.string.teacher_schedule));
    }


    //初始化view
    private void initView() {
        findViews();
        initToolbar();
        setOnInteractListener();

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isFinish) {
            finish();
            isFinish = false;
        }
    }

    private void loadData() {
        ArrayList<Fragment> lViewList = new ArrayList<>();

        lViewList.add(ScheduleSearchFragment.newInstance(ScheduleSearchFragment.CLASS));
        lViewList.add(ScheduleSearchFragment.newInstance(ScheduleSearchFragment.TEACHER));
        for (int i = 0; i < mTitleList.size(); i++) {
            mTabLayout.addTab(mTabLayout.newTab().setText(mTitleList.get(i)));
        }
        CommonPageAdapter adapter = new CommonPageAdapter(getSupportFragmentManager(), lViewList, mTitleList);
        mViewPager.setAdapter(adapter);
        mTabLayout.setTabMode(TabLayout.MODE_FIXED);
        mTabLayout.setupWithViewPager(mViewPager);
    }


    private void initToolbar() {
        mToolBarManager.init();
        mToolbar.setNavigationOnClickListener(v -> onBackPressed());
        mToolBarManager.setTitle(getString(R.string.check_schedule));

    }

    //事件处理
    private void setOnInteractListener() {

    }

    //绑定控件
    private void findViews() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolBarManager = new ToolBarManager(mToolbar, this);

        mTabLayout = (TabLayout) findViewById(R.id.schedule_search_tabLayout);
        mViewPager = (ViewPager) findViewById(R.id.schedule_search_viewpager);

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mToolBarManager.destroy();
    }
}
