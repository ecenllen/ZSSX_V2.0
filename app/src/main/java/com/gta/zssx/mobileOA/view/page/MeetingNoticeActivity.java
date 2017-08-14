package com.gta.zssx.mobileOA.view.page;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.gta.zssx.R;
import com.gta.zssx.mobileOA.presenter.MeetingNoticePresenter;
import com.gta.zssx.mobileOA.view.MeetingNoticeView;
import com.gta.zssx.pub.base.BaseActivity;
import com.gta.zssx.pub.manager.ToolBarManager;

public class MeetingNoticeActivity extends BaseActivity<MeetingNoticeView, MeetingNoticePresenter> {

    private Toolbar mToolbar;
    private ToolBarManager mToolBarManager;

    @NonNull
    @Override
    public MeetingNoticePresenter createPresenter() {
        return new MeetingNoticePresenter();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oa_meeting_notice);
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
                .setTitle("会议通知").setRightImage(R.drawable.tab_add_sel_bg).showRightImage(true).clickRightImage(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MeetingNoticeActivity.this,MeetingSummaryActivity.class);
                startActivity(intent);
            }
        });


    }
}
