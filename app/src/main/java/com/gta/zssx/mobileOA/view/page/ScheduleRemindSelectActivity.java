package com.gta.zssx.mobileOA.view.page;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.gta.zssx.R;
import com.gta.zssx.mobileOA.view.adapter.ScheduleRemindselectAdapter;
import com.gta.zssx.pub.base.BaseAppCompatActivity;
import com.gta.zssx.pub.manager.ToolBarManager;
import com.gta.zssx.pub.util.LogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lan.zheng on 2016/10/26.
 */
public class ScheduleRemindSelectActivity extends BaseAppCompatActivity{
    private RecyclerView mRecyclerView;
    private ScheduleRemindselectAdapter mScheduleRemindselectAdapter;
    private int positionSelect;
    public Toolbar mToolbar;
    protected ToolBarManager mToolBarManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fschedule_remind_select);
        initView();
        initData();
    }

    private void initView(){
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_schedule_remind_select);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setNavigationOnClickListener(view -> onBackPressed());
        mToolBarManager = new ToolBarManager(mToolbar, this);
        mToolBarManager.init();
        mToolBarManager.setTitle(getString(R.string.text_ring_cycle))
                .setRightText(R.string.finish).clickRightButton(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("positionSelect",positionSelect);
                setResult(RESULT_OK,intent);
                finish();
            }
        });
    }

    private void initData(){
        positionSelect = getIntent().getIntExtra("selectPosition",5);
        String[] strings = getIntent().getStringArrayExtra("list");
        List<String> list = new ArrayList<>();
        for(int i = 0;i<strings.length;i++){
            list.add(strings[i]);
        }
        LogUtil.Log("lenita","position = "+positionSelect+",list = "+list.size());
        mScheduleRemindselectAdapter = new ScheduleRemindselectAdapter(this, new ScheduleRemindselectAdapter.Listener() {
            @Override
            public void itemClick(int position) {
                positionSelect = position;
            }
        },list,positionSelect);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mScheduleRemindselectAdapter);
    }
}
