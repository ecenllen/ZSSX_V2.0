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

import com.gta.utils.resource.Toast;
import com.gta.zssx.R;
import com.gta.zssx.mobileOA.view.adapter.MyFragmentPagerAdapter;
import com.gta.zssx.mobileOA.view.base.BaseOAActivity;
import com.gta.zssx.patrolclass.view.page.HistoryTimeActivity;
import com.gta.zssx.pub.common.Constant;
import com.gta.zssx.pub.manager.ToolBarManager;

import java.util.ArrayList;

public class TaskMainActivity extends BaseOAActivity {

    private ViewPager mPager;
    private ArrayList<Fragment> fragmentList;
    private TabLayout tlTabs;
    public Toolbar mToolbar;
    public ToolBarManager mToolBarManager;
    private String[] tabs = new String[]{"未结束", "已结束"};


    public static void start(Context context) {
        final Intent lIntent = new Intent(context, TaskMainActivity.class);
//        Bundle lBundle = new Bundle();
        if (!(context instanceof Activity)) {
            lIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(lIntent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oa_task);
        inits();
    }


    private void inits() {
        //初始化标题栏
        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        mToolBarManager = new ToolBarManager(mToolbar, this);
        mToolBarManager.init();
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        mToolBarManager.showBack(true)
                .setTitle(getResources().getString(R.string.oa_task))
                .showRightIcTime(true)
                .showIvSearch(true)
                .clickTime(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(TaskMainActivity.this, HistoryTimeActivity.class);
                        intent.putExtra("isFromOA", true);
                        startActivityForResult(intent, 100);
                    }
                })
                .clickIvSearch(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(TaskMainActivity.this, SearchActivity.class);
                        intent.putExtra(Constant.KEY_SEARCH_TYPE, Constant.SEARCH_TYPE_TASK);
                        startActivity(intent);
                    }
                });

        tlTabs = (TabLayout) findViewById(R.id.tl_tab);
        tlTabs.setTabMode(TabLayout.MODE_SCROLLABLE);
        tlTabs.setTabGravity(TabLayout.GRAVITY_CENTER);
        mPager = (ViewPager) findViewById(R.id.vp_task);
        fragmentList = new ArrayList<>();
        for (int i = 0; i < tabs.length; i++) {
            Bundle bundle = new Bundle();
            bundle.putInt("position", i);
            Fragment fragment = new TaskMainFragment();
            fragment.setArguments(bundle);
            fragmentList.add(fragment);
        }

        //给ViewPage设置Adapter
        mPager.setAdapter(new MyFragmentPagerAdapter(getSupportFragmentManager(), tabs, fragmentList));
        //设置当前显示标签页为第一页
        mPager.setCurrentItem(0);
        tlTabs.setupWithViewPager(mPager);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK) {
            String startDate = data.getStringExtra("startDate");
            String endDate = data.getStringExtra("endDate");
            Toast.Short(TaskMainActivity.this, "StartDate : " + startDate + " /nEndDate :" + endDate);
        }
    }
}
