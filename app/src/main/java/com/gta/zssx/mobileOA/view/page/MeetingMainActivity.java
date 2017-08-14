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
import com.gta.zssx.mobileOA.model.bean.Meeting;
import com.gta.zssx.mobileOA.presenter.MeetingMainPresenter;
import com.gta.zssx.mobileOA.view.MeetingMainView;
import com.gta.zssx.mobileOA.view.adapter.MeetingAdapter;
import com.gta.zssx.pub.base.BaseActivity;
import com.gta.zssx.pub.common.Constant;
import com.gta.zssx.pub.hfrecyclerview.HFRecyclerView;
import com.gta.zssx.pub.manager.ToolBarManager;
import com.gta.zssx.pub.util.RxBus;

import java.util.ArrayList;
import java.util.List;

/**
 * 会议
 */
public class MeetingMainActivity extends BaseActivity<MeetingMainView, MeetingMainPresenter> implements MeetingMainView, HFRecyclerView.HFRecyclerViewListener {

    //    private ViewPager mPager;
//    private ArrayList<Fragment> fragmentList;
//    private TabLayout tlTabs;
    public Toolbar mToolbar;
    private HFRecyclerView hfRecyclerView;
    private TextView tvEmpty;
    int position;
    private MeetingAdapter adapter;
    private List<Meeting> meetings = new ArrayList<>();

    public ToolBarManager mToolBarManager;
//    private String[] tabs = new String[]{"未结束", "已结束"};


    public static void start(Context context) {
        final Intent lIntent = new Intent(context, MeetingMainActivity.class);
//        Bundle lBundle = new Bundle();
        if (!(context instanceof Activity)) {
            lIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(lIntent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oa_meeting_main);
        inits();
    }


    private void inits() {
        //初始化标题栏
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        hfRecyclerView = (HFRecyclerView) findViewById(R.id.recycler);
        setOnInteractListener();
        tvEmpty = (TextView) findViewById(R.id.tv_emptyHint);

        mToolBarManager = new ToolBarManager(mToolbar, this);
        mToolBarManager.init();
        mToolbar.setNavigationOnClickListener((v)-> onBackPressed());
        mToolBarManager.showBack(true)
                .setTitle(getResources().getString(R.string.oa_meeting))
                .showIvSearch(true)
                .clickIvSearch((v) -> {
                    Intent intent = new Intent(MeetingMainActivity.this, SearchActivity.class);
                    intent.putExtra(Constant.KEY_SEARCH_TYPE, Constant.SEARCH_TYPE_MEETING);
                    startActivity(intent);
                });
        adapter = new MeetingAdapter(MeetingMainActivity.this, meetings);
        hfRecyclerView.setAdapter(adapter);


        presenter.mCompositeSubscription.add(RxBus.getDefault().toObserverable(Integer.class)
                .subscribe(positon -> {
                    meetings.get(positon).setStatus("1");
                    adapter.notifyDataSetChanged();
                }, throwable -> {

                }));

        presenter.getMeetingList(Constant.REFRESH);
//        tlTabs = (TabLayout) findViewById(R.id.tl_tab);
//        tlTabs.setTabMode(TabLayout.MODE_SCROLLABLE);
//        tlTabs.setTabGravity(TabLayout.GRAVITY_CENTER);
//        mPager = (ViewPager) findViewById(R.id.vp_meeting);
//        fragmentList = new ArrayList<>();
//        for (int i = 0; i < tabs.length; i++) {
//            Bundle bundle = new Bundle();
//            bundle.putInt("position", i);
//            Fragment fragment = new MeetingMainFragment();
//            fragment.setArguments(bundle);
//            fragmentList.add(fragment);
//        }

//        //给ViewPage设置Adapter
//        mPager.setAdapter(new MyFragmentPagerAdapter(getSupportFragmentManager(), tabs, fragmentList));
//        //设置当前显示标签页为第一页
//        mPager.setCurrentItem(0);
//        tlTabs.setupWithViewPager(mPager);
    }

    private void setOnInteractListener() {
        hfRecyclerView.setCanLoadMore(true);
        hfRecyclerView.setCanRefresh(true);
        hfRecyclerView.setFooterViewText("");
        hfRecyclerView.setRecyclerViewListener(this);
    }

    @NonNull
    @Override
    public MeetingMainPresenter createPresenter() {
        return new MeetingMainPresenter();
    }

    @Override
    public void showEmpty() {
        hfRecyclerView.setVisibility(View.GONE);
        tvEmpty.setVisibility(View.VISIBLE);
    }

    @Override
    public void setServerTime(String serverTime) {
        adapter.setServerTime(serverTime);
    }

    @Override
    public void refreshMeetingList(List<Meeting> meetingList) {
        tvEmpty.setVisibility(View.GONE);
        hfRecyclerView.setVisibility(View.VISIBLE);
        hfRecyclerView.stopRefresh(true);
        adapter.refreshData(meetingList);
    }

    @Override
    public void appendMeetingList(List<Meeting> meetingList) {
        hfRecyclerView.stopLoadMore(true);
        adapter.appendData(meetingList);
    }

    @Override
    public void onLoadMoreError() {
        hfRecyclerView.stopLoadMore(false);
    }

    @Override
    public void onRefreshError() {
        hfRecyclerView.stopRefresh(false);
    }

    @Override
    public void onLoadMoreEmpty() {
        hfRecyclerView.stopLoadMore(false);
        hfRecyclerView.setFooterViewText("无更多信息");
    }

    @Override
    public void onRefresh() {
        presenter.getMeetingList(Constant.REFRESH);
    }

    @Override
    public void onLoadMore() {
        presenter.getMeetingList(Constant.LOAD_MORE);
    }
}
