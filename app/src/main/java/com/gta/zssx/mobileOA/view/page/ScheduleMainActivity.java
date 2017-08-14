package com.gta.zssx.mobileOA.view.page;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gta.zssx.R;
import com.gta.zssx.mobileOA.view.adapter.ui.titlePopupWindow;
import com.gta.zssx.mobileOA.view.base.BaseOAActivity;

/**
 * Created by lan.zheng on 2016/10/31.  日程计划：个人日程（二期）、学期计划（二期）、周程表(一期)
 */
public class ScheduleMainActivity extends BaseOAActivity implements View.OnClickListener{

    private TextView titleTextView;
    private ImageView searchImageView;
    private TextView todayTextView;
    private TextView selectYearTextView;
    private String[] titles;
    private Toolbar toolbar;
    private titlePopupWindow mTitlePopupWindow;

    public static void start(Context context) {
        final Intent lIntent = new Intent(context, ScheduleMainActivity.class);
//        Bundle lBundle = new Bundle();
        if (!(context instanceof Activity)) {
            lIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(lIntent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_main_page);
        initView();
        initData();
    }

    private void initView(){
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        setSelectTitleToolbar(toolbar);
        titles = new String[]{getString(R.string.text_person_schedule),getString(R.string.text_term_schedule),getString(R.string.text_weekly_schedule)};
        titleTextView = (TextView) findViewById(R.id.tv_select_title);
        titleTextView.setCompoundDrawables(null,null,null,null);
        titleTextView.setText(titles[2]);
        searchImageView = (ImageView)findViewById(R.id.iv_search);
        searchImageView.setVisibility(View.GONE);
        todayTextView = (TextView) findViewById(R.id.iv_right_three);
        todayTextView.setVisibility(View.GONE);
        selectYearTextView = (TextView)findViewById(R.id.iv_right_two);
        selectYearTextView.setVisibility(View.GONE);
//        titleTextView.setOnClickListener(this);

    }

    private void initData(){
        initFragmentManager();
        goToFragment(2);  //默认“个人日程”
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_select_title:
                showTitleItems();
                break;
        }
    }

    private void showTitleItems(){
        String title = titleTextView.getText().toString();
        mTitlePopupWindow = new titlePopupWindow(this,titles,title, new titlePopupWindow.Listener() {
            @Override
            public void onPopupWindowDismissListener() {
                //无操作
            }

            @Override
            public void onItemClickListener(int position) {
                if(title.equals(titles[position]))
                    return;
                titleTextView.setText(titles[position]);
                goToFragment(position);
            }
        });
        mTitlePopupWindow.showAsDropDown(titleTextView);
    }

    private void initFragmentManager() {
        fragmentManager = getSupportFragmentManager();
    }

    private void highFragment(FragmentTransaction fragmentTransaction) {
        if (schedulePlanFragment != null) {
            fragmentTransaction.hide(schedulePlanFragment);
        }
        if (semesterScheduleFragment != null) {
            fragmentTransaction.hide(semesterScheduleFragment);
        }
        if (weeklyScheduleFragment != null) {
            fragmentTransaction.hide(weeklyScheduleFragment);
        }
    }


    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private SchedulePlanFragment schedulePlanFragment;  //个人日程
    private SemesterScheduleFragment semesterScheduleFragment;  //学期计划
    private WeeklyScheduleFragment weeklyScheduleFragment;  //周程表
    private void goToFragment(int p){
        fragmentTransaction = fragmentManager.beginTransaction();
        highFragment(fragmentTransaction);
        switch (p){
            case 0:
                todayTextView.setVisibility(View.VISIBLE);
                selectYearTextView.setVisibility(View.VISIBLE);
                if(schedulePlanFragment == null){
                    schedulePlanFragment = new SchedulePlanFragment();
                    fragmentTransaction.add(R.id.container, schedulePlanFragment);
                }else {
                    fragmentTransaction.show(schedulePlanFragment);
                    schedulePlanFragment.setListener();  //重置监听
                }
                break;
            case 1:
                todayTextView.setVisibility(View.GONE);
                selectYearTextView.setVisibility(View.GONE);
//                searchImageView.setVisibility(View.VISIBLE);
                if(semesterScheduleFragment == null){
                    semesterScheduleFragment = new SemesterScheduleFragment();
                    fragmentTransaction.add(R.id.container, semesterScheduleFragment);
                }else {
                    fragmentTransaction.show(semesterScheduleFragment);
                    //刷新列表
                    semesterScheduleFragment.onRefresh();
                }
                break;
            case 2:
                todayTextView.setVisibility(View.GONE);
                selectYearTextView.setVisibility(View.GONE);
//                searchImageView.setVisibility(View.VISIBLE);
                if(weeklyScheduleFragment == null){
                    weeklyScheduleFragment = new WeeklyScheduleFragment();
                    fragmentTransaction.add(R.id.container, weeklyScheduleFragment);
                }else {
                    fragmentTransaction.show(weeklyScheduleFragment);
                    //刷新列表
                    semesterScheduleFragment.onRefresh();
                }
                break;
        }
        fragmentTransaction.commitAllowingStateLoss();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (schedulePlanFragment != null) {
            fragmentTransaction.remove(schedulePlanFragment);
            schedulePlanFragment = null;
        }
        if (semesterScheduleFragment != null) {
            fragmentTransaction.remove(semesterScheduleFragment);
            semesterScheduleFragment = null;
        }
        if (weeklyScheduleFragment != null) {
            fragmentTransaction.remove(weeklyScheduleFragment);
            weeklyScheduleFragment = null;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
    }


}
