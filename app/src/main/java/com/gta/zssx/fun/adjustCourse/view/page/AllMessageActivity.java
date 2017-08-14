package com.gta.zssx.fun.adjustCourse.view.page;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.gta.zssx.R;
import com.gta.zssx.fun.adjustCourse.model.bean.ScheduleBean;
import com.gta.zssx.fun.adjustCourse.view.adapter.AllMessageAdapter;
import com.gta.zssx.pub.base.BaseAppCompatActivity;
import com.gta.zssx.pub.manager.ToolBarManager;

import java.io.Serializable;
import java.util.List;

/**
 * [Description]
 * <p>
 * [How to use]
 * <p>
 * [Tips]
 *
 * @author Created by Zhimin.Huang on 2016/11/9.
 * @since 1.0.0
 */
public class AllMessageActivity extends BaseAppCompatActivity {
    public static final String SECTIONBEAN = "sectionbean";
    private Toolbar mToolbar;
    private ToolBarManager mToolBarManager;
    private RecyclerView mRecyclerView;
    private List<ScheduleBean.SectionBean> mSectionBeen;

    public static void start(Context context, List<ScheduleBean.SectionBean> beanList) {
        final Intent lIntent = new Intent(context, AllMessageActivity.class);
        Bundle lBundle = new Bundle();
        lBundle.putSerializable(SECTIONBEAN, (Serializable) beanList);
        lIntent.putExtras(lBundle);
        if (!(context instanceof Activity)) {
            lIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(lIntent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_message);
        initData();
        initView();
        loadData();
    }


    //用于页面间数据接收
    private void initData() {
        mSectionBeen = (List<ScheduleBean.SectionBean>) getIntent().getExtras().getSerializable(SECTIONBEAN);
    }


    //初始化view
    private void initView() {
        findViews();
        initToolbar();
        setOnInteractListener();
    }

    private void loadData() {

    }


    private void initToolbar() {
        mToolBarManager.init();
        mToolbar.setNavigationOnClickListener(v -> onBackPressed());
        mToolBarManager.setTitle("全文");

    }

    //事件处理
    private void setOnInteractListener() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        mRecyclerView.setAdapter(new AllMessageAdapter(mActivity, mSectionBeen));
    }

    //绑定控件
    private void findViews() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolBarManager = new ToolBarManager(mToolbar, this);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mToolBarManager.destroy();
    }
}
