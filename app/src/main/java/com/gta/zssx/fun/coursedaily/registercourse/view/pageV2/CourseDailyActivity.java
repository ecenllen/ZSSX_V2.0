package com.gta.zssx.fun.coursedaily.registercourse.view.pageV2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.gta.zssx.AppConfiguration;
import com.gta.zssx.R;
import com.gta.zssx.fun.coursedaily.registercourse.view.adapter.CommonPageAdapter;
import com.gta.zssx.fun.coursedaily.registercourse.view.page.CourseDailyFragment;
import com.gta.zssx.fun.coursedaily.registercourse.view.page.LogStatisticsFragment;
import com.gta.zssx.fun.coursedaily.registerrecord.view.page.AlreadyRegisteredRecordPeriodOfTimeActivity;
import com.gta.zssx.pub.base.BaseAppCompatActivity;
import com.gta.zssx.pub.common.Constant;
import com.gta.zssx.pub.manager.ToolBarManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * [Description]
 * <p>
 * [How to use]
 * <p>
 * [Tips]
 *
 * @author Created by Zhimin.Huang on 2016/7/4.
 * @since 1.0.0
 */
public class CourseDailyActivity extends BaseAppCompatActivity {

    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.tablayout)
    TabLayout mTabLayout;
    @Bind(R.id.viewPage_patrol)
    ViewPager mPager;

    public static final int FIRST_GET_IS_TEACHER = 1;
    public static final int NORMAL_GET_IS_TEACHER = 2;
    private ToolBarManager mToolBarManager;
    public static boolean isRefresh;
    public static boolean isRegister;

    private List<String> mTitleList;

    public static void start(Context context) {
        start(context, false);
    }

    public static void start(Context context, boolean register) {
        final Intent lIntent = new Intent(context, CourseDailyActivity.class);
        isRegister = register;
        if (!(context instanceof Activity)) {
            lIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(lIntent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_daily);
        ButterKnife.bind(this);

        initData();
        initToolbar();
        initviewPager();
    }

    private void initData() {
        mTitleList = new ArrayList<>();
        mTitleList.add("课堂登记");
        mTitleList.add("日志统计");
    }

    private void initToolbar() {
        mToolBarManager = new ToolBarManager(mToolbar, this);
        mToolBarManager.init();
        mToolbar.setNavigationOnClickListener(v -> onBackPressed());

        mToolBarManager.getRightButton().setEnabled(true);
        mToolBarManager.showBack(true)
                .setTitle(getString(R.string.course_daily))
                .setRightText(getString(R.string.has_signed))
                .clickRightButton(v -> {
                    //进入已登记页面
                    AlreadyRegisteredRecordPeriodOfTimeActivity.start(mActivity);
                });

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (isRegister) {
            mPager.setCurrentItem(0);
            isRegister = false;
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void initviewPager() {
        //角色权限获取
        boolean flag = false;
        try {
            flag = AppConfiguration.getInstance().getUserBean().getuType() == Constant.LEAD;
        } catch (Exception e) {
            e.printStackTrace();
        }
        ArrayList<Fragment> fragmentList = new ArrayList<>();
        //判断角色权限
        if (flag) {
            //是校领导的话,显示课堂登记,日志统计页面
            mTabLayout.setVisibility(View.VISIBLE);
            fragmentList.add(CourseDailyFragment.newInstance());
            fragmentList.add(LogStatisticsFragment.newInstance());
        } else {
            mTabLayout.setVisibility(View.GONE);
            fragmentList.add(CourseDailyFragment.newInstance());
        }

        CommonPageAdapter adapter = new CommonPageAdapter(getSupportFragmentManager(), fragmentList, mTitleList);
        //给ViewPage设置Adapter
        mPager.setAdapter(adapter);
        //设置当前显示标签页为第一页
        mPager.setCurrentItem(0);

        mTabLayout.setTabMode(TabLayout.MODE_FIXED);
        mTabLayout.setupWithViewPager(mPager);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mToolBarManager.destroy();
    }

}