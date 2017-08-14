package com.gta.zssx.mobileOA.view.page;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.gta.zssx.R;
import com.gta.zssx.mobileOA.presenter.MeetingSummaryPresenter;
import com.gta.zssx.mobileOA.view.MeetingSummaryView;
import com.gta.zssx.pub.base.BaseActivity;
import com.gta.zssx.pub.manager.ToolBarManager;

public class MeetingSummaryActivity extends BaseActivity<MeetingSummaryView, MeetingSummaryPresenter> {

    private Toolbar mToolbar;
    private ToolBarManager mToolBarManager;

    @NonNull
    @Override
    public MeetingSummaryPresenter createPresenter() {
        return new MeetingSummaryPresenter();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oa_meeting_summary);
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
                .setTitle("会议纪要");


    }
}
