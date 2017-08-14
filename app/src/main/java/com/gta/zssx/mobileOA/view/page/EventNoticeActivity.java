package com.gta.zssx.mobileOA.view.page;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.gta.zssx.R;
import com.gta.zssx.mobileOA.model.bean.EventNoticeInfo;
import com.gta.zssx.mobileOA.presenter.EventNoticePresenter;
import com.gta.zssx.mobileOA.view.EventNoticeView;
import com.gta.zssx.mobileOA.view.adapter.EventNoticeAdapter;
import com.gta.zssx.pub.base.BaseActivity;
import com.gta.zssx.pub.hfrecyclerview.HFRecyclerView;
import com.gta.zssx.pub.manager.ToolBarManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by lan.zheng on 2016/10/31.  事务提醒一期
 */
public class EventNoticeActivity extends BaseActivity<EventNoticeView,EventNoticePresenter>
implements EventNoticeView,HFRecyclerView.HFRecyclerViewListener{

    private HFRecyclerView mRecyclerView;
    private TextView tv_emptyHintTextView;
    private EventNoticeAdapter mEventNoticeAdapter;
    public Toolbar mToolbar;
    protected ToolBarManager mToolBarManager;

    public static final int REFRESH = 0;
    public static final int MORE = 1;
    private int pageIndex = 1;  //第一页
    private SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);

    @NonNull
    @Override
    public EventNoticePresenter createPresenter() {
        return new EventNoticePresenter();
    }


    public static void start(Context context) {
        final Intent lIntent = new Intent(context, EventNoticeActivity.class);
        if (!(context instanceof Activity)) {
            lIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(lIntent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_notice);
        initView();
        initData();
    }

    private void initView(){
        tv_emptyHintTextView = (TextView)findViewById(R.id.tv_emptyHint);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setNavigationOnClickListener(view -> onBackPressed());
        mToolBarManager = new ToolBarManager(mToolbar, this);
        mToolBarManager.init();
        mToolBarManager.setTitle(getString(R.string.text_event_notice));
        mRecyclerView = (HFRecyclerView) findViewById(R.id.rv_event_notice);
        setOnInteractListener();
    }

    private void initData(){
        //初始化适配器
        EventNoticeInfo eventNoticeInfo = new EventNoticeInfo();
        Date d = new Date();
        eventNoticeInfo.setServerTime(s.format(d));
        List<EventNoticeInfo.EventRemindEntity> list = new ArrayList<>();
        eventNoticeInfo.setEventRemind(list);
//        EventNoticeInfo eventNoticeInfo1 = presenter.testData();  //测试数据
        mEventNoticeAdapter = new EventNoticeAdapter(this,eventNoticeInfo);
        mRecyclerView.setAdapter(mEventNoticeAdapter);
        tv_emptyHintTextView.setVisibility(View.VISIBLE);
        presenter.getEventsList(REFRESH, pageIndex);  //获取服务器数据
    }

    private void setOnInteractListener () {
        mRecyclerView.setCanLoadMore (true);
        mRecyclerView.setCanRefresh (true);
        mRecyclerView.setFooterViewText ("");
        mRecyclerView.setRecyclerViewListener (this);
    }


    @Override
    public void onRefresh() {
        pageIndex = 1;
        presenter.getEventsList(REFRESH,  pageIndex);  //获取服务器数据
    }

    @Override
    public void onLoadMore() {
        presenter.getEventsList(MORE, pageIndex);  //获取服务器数据
    }

    @Override
    public void showEmptyView() {
        mRecyclerView.stopLoadMore(true);
        mRecyclerView.stopRefresh(true);
        tv_emptyHintTextView.setVisibility(View.VISIBLE);
    }

    @Override
    public void showEventList(EventNoticeInfo eventNoticeInfo,int action) {
        tv_emptyHintTextView.setVisibility(View.GONE);
        if(action == REFRESH){
            mRecyclerView.stopRefresh(true);
            //重新设置数据集合
            mEventNoticeAdapter = new EventNoticeAdapter(this,eventNoticeInfo);
            mRecyclerView.setAdapter(mEventNoticeAdapter);
        }else {
            mRecyclerView.stopLoadMore(true);
            //加载更多就继续加数据
            mEventNoticeAdapter.setData(eventNoticeInfo);
        }
        pageIndex++;
    }

    @Override
    public void onLoadMoreError() {
        mRecyclerView.stopLoadMore(false);
    }

    @Override
    public void onRefreshError() {
        mRecyclerView.stopRefresh(false);
    }

    @Override
    public void onLoadMoreEmpty() {
        mRecyclerView.stopLoadMore(false);
        mRecyclerView.setFooterViewText ("无更多信息");
    }
}
