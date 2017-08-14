package com.gta.zssx.fun.classroomFeedback.view.page;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.gta.zssx.R;
import com.gta.zssx.fun.coursedaily.registercourse.view.adapter.CommonPageAdapter;
import com.gta.zssx.pub.base.BaseCommonActivity;

import java.util.ArrayList;
import java.util.List;


/**
 * [Description]
 * <p> 课堂教学反馈首页，登记列表
 * [How to use]
 * <p>
 * [Tips]
 *
 * @author Created by liang.lu on 2017/06/20.
 * @since 2.0.0
 */
public class ClassroomFeedbackActivity extends BaseCommonActivity {

    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private List<String> mTitleList;

    public static void start (Context context) {
        final Intent lIntent = new Intent (context, ClassroomFeedbackActivity.class);
        if (!(context instanceof Activity)) {
            lIntent.setFlags (Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity (lIntent);
    }

    @Override
    public int getLayoutId () {
        return R.layout.activity_calssroom_feedback;
    }

    @Override
    protected void initView () {
        init ();
    }

    private void init () {
        findView ();
        initToolbar ();
        loadData ();
    }

    protected void initData () {
        mTitleList = new ArrayList<> ();
        mTitleList.add (getString (R.string.un_register));
        mTitleList.add (getString (R.string.is_register));
    }

    private void loadData () {
        ArrayList<Fragment> mFragmentList = new ArrayList<> ();
        Fragment classroomUnRegisterFragment = new ClassroomUnRegisterFragment ();
        Fragment classroomIsRegisterFragment = new ClassroomIsRegisterFragment ();
        mFragmentList.add (classroomUnRegisterFragment);
        mFragmentList.add (classroomIsRegisterFragment);
        for (int i = 0; i < mTitleList.size (); i++) {
            mTabLayout.addTab (mTabLayout.newTab ().setText (mTitleList.get (i)));
        }
        CommonPageAdapter adapter = new CommonPageAdapter (getSupportFragmentManager (), mFragmentList, mTitleList);
        mViewPager.setAdapter (adapter);
        mTabLayout.setTabMode (TabLayout.MODE_FIXED);
        mTabLayout.setupWithViewPager (mViewPager);
    }


    private void findView () {
        mTabLayout = (TabLayout) findViewById (R.id.classroom_feedback_tabLayout);
        mViewPager = (ViewPager) findViewById (R.id.classroom_feedback_viewpager);
    }


    private void initToolbar () {
        mToolBarManager.setTitle (getString (R.string.classroom_feedback));
    }

    @Override
    protected void onDestroy () {
        super.onDestroy ();
        mToolBarManager.destroy ();
    }
}
