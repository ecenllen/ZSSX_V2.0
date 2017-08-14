package com.gta.zssx.mobileOA.view.page;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.gta.utils.resource.L;
import com.gta.zssx.R;
import com.gta.zssx.mobileOA.view.adapter.MyFragmentPagerAdapter;
import com.gta.zssx.mobileOA.view.base.BaseOAActivity;
import com.gta.zssx.pub.manager.ToolBarManager;
import com.gta.zssx.pub.util.LogUtil;

import java.util.ArrayList;

/**
 * Created by lan.zheng on 2016/11/15.
 */
public class DutyDateSelectActivity  extends BaseOAActivity{
    private ArrayList<Fragment> fragmentList;
    public Toolbar mToolbar;
    protected ToolBarManager mToolBarManager;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    public static final int DUTY_TYPE_REGISTER = 1;  //值日登记
    public static final String DUTY_TYPE = "duty_type";
    public static final String DUTY_DETAIL_ID = "duty_detail_id";
    private String[] tabs1;  //已值班，未值班
    private int mRegisterOrCheck;  //登记还是检查
    private int mDutyDetailId;

    public static void start(Context context, int registerOrCheck) {
        final Intent lIntent = new Intent(context, DutyDateSelectActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt(DUTY_TYPE,registerOrCheck);
        lIntent.putExtras(bundle);
        if (!(context instanceof Activity)) {
            lIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(lIntent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_duty_date_select);
        mRegisterOrCheck = getIntent().getExtras().getInt(DUTY_TYPE,1);
        mDutyDetailId = getIntent().getExtras().getInt(DUTY_DETAIL_ID,0);
        initView();
        initData();
    }

    private void initView(){
        mTabLayout = (TabLayout) findViewById(R.id.duty_date_select_tablayout);
        mViewPager = (ViewPager)findViewById(R.id.duty_date_select_viewpager);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mToolBarManager = new ToolBarManager(mToolbar, this);
        mToolBarManager.init();
        mToolBarManager.setTitle(R.string.text_duty_date_select);
        tabs1 = new String[]{getResources().getString(R.string.text_duty_done),getResources().getString(R.string.text_duty_undo)};
    }

    private void initData(){
        mTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        mTabLayout.setTabGravity(TabLayout.GRAVITY_CENTER);
        fragmentList = new ArrayList<>();
        //根据tab数生成Fragment
        for (int i = 0; i < tabs1.length; i++) {
            Bundle bundle = new Bundle();
            bundle.putInt(DUTY_DETAIL_ID,mDutyDetailId);
            bundle.putInt("position",i);
            bundle.putInt(DUTY_TYPE, mRegisterOrCheck);  //设置是登记还是检查
            DutyDateSelectFragment fragment = new DutyDateSelectFragment();
            fragment.setDateSelectListener(new DutyDateSelectFragment.DateSelectListener() {
                @Override
                public void DateSelect(Bundle bundle1) {
                    Intent intent = new Intent();
                    intent.putExtras(bundle1);
                    setResult(RESULT_OK ,intent);
                    finish();
                }
            });
            fragment.setArguments(bundle);
            fragmentList.add(fragment);
        }
//
        //给ViewPage设置Adapter
        mViewPager.setAdapter(new MyFragmentPagerAdapter(getSupportFragmentManager(), tabs1, fragmentList));
        //设置当前显示标签页为第一页
        mViewPager.setCurrentItem(0);
        mTabLayout.setupWithViewPager(mViewPager);
    }

}
