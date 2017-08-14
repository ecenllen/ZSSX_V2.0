package com.gta.zssx.fun.coursedaily.registercourse.view.pageV2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.bigkoo.pickerview.TimePickerView;
import com.github.captain_miao.recyclerviewutils.EndlessRecyclerOnScrollListener;
import com.gta.zssx.AppConfiguration;
import com.gta.zssx.R;
import com.gta.zssx.fun.coursedaily.registercourse.presenter.MultiPageViewPresenter;
import com.gta.zssx.fun.coursedaily.registercourse.view.MultiPageView;
import com.gta.zssx.fun.coursedaily.registercourse.view.adapter.MultiPagerAdapter;
import com.gta.zssx.fun.coursedaily.registerrecord.model.DTO.RegisteredRecordDto;
import com.gta.zssx.fun.personalcenter.model.bean.UserBean;
import com.gta.zssx.pub.base.BaseActivity;
import com.gta.zssx.pub.manager.ToolBarManager;

import org.joda.time.DateTime;

import java.util.List;

/**
 * [Description]
 * <p>
 * [How to use]
 * <p>
 * [Tips]
 *
 * @author Created by Zhimin.Huang on 2016/7/19.
 * @since 1.0.0
 */
public class TestMultiPageActivity extends BaseActivity<MultiPageView, MultiPageViewPresenter> implements MultiPageView {


    private TextView mBeginDateTextView;
    private TextView mEndDateTextView;
    private TextView mSectionTextView;
    private TextView mNotResultTextView;
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    //当月时间段
    private String mBeginDate;
    private String mEndDate;


    private String mTeacherID;
    public TimePickerView mBeginPickView;
    public TimePickerView mEndPickView;
    public MultiPagerAdapter mAdapter;
    /**
     * 底部加载监听事件
     */
    private EndlessRecyclerOnScrollListener mEndlessRecyclerOnScrollListener;




    public Toolbar mToolbar;
    public ToolBarManager mToolBarManager;

    public static void start(Context context) {
        final Intent lIntent = new Intent(context, TestMultiPageActivity.class);
        if (!(context instanceof Activity)) {
            lIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(lIntent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multipage);
        initData();
        initView();
        loadData();
    }

    private void loadData() {
        presenter.getServerTime(mTeacherID);
    }

    //初始化view
    private void initView() {
        findViews();
        initToolbar();
        setOnInteractListener();


    }

    private void initToolbar() {
        mToolBarManager.init();
        mToolbar.setNavigationOnClickListener(v -> onBackPressed());

    }

    //事件处理
    private void setOnInteractListener() {

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mBeginDateTextView.setOnClickListener(view -> mBeginPickView.show());

        mBeginPickView.setOnTimeSelectListener(date -> {
            mBeginDate = presenter.getFormatDate(date);
            mBeginDateTextView.setText(mBeginDate);
            presenter.getRegisteredRecordData(mTeacherID, mBeginDate, mEndDate, 1);
        });

        mEndDateTextView.setOnClickListener(view -> mEndPickView.show());

        mEndPickView.setOnTimeSelectListener(date -> {
            mEndDate = presenter.getFormatDate(date);
            mEndDateTextView.setText(mEndDate);
            presenter.getRegisteredRecordData(mTeacherID, mBeginDate, mEndDate, 1);
        });

        mSwipeRefreshLayout.setOnRefreshListener(() -> presenter.getRegisteredRecordData(mTeacherID, mBeginDate, mEndDate, 1));

        //设置到达底部加载更多事件
        final LinearLayoutManager lLayoutManager = (LinearLayoutManager) mRecyclerView.getLayoutManager();
        mEndlessRecyclerOnScrollListener = new EndlessRecyclerOnScrollListener(lLayoutManager) {
            @Override
            public void onLoadMore(int i) {
                presenter.getRegisteredRecordData(mTeacherID, mBeginDate, mEndDate, i);
            }
        };
        mRecyclerView.addOnScrollListener(mEndlessRecyclerOnScrollListener);

    }

    //绑定控件
    private void findViews() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolBarManager = new ToolBarManager(mToolbar, this);

        mBeginDateTextView = (TextView) findViewById(R.id.tv_begin_date);
        mEndDateTextView = (TextView) findViewById(R.id.tv_end_date);
        mSectionTextView = (TextView) findViewById(R.id.tv_class_total_section);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        mNotResultTextView = (TextView) findViewById(R.id.tv_not_result_text);

    }

    //用于页面间数据接收
    private void initData() {
        presenter.attachView(this);
        try {
            UserBean lUserBean = AppConfiguration.getInstance().getUserBean();
            mTeacherID = lUserBean.getUserId();
        } catch (Exception e) {
            e.printStackTrace();
        }

        mBeginPickView = new TimePickerView(this, TimePickerView.Type.YEAR_MONTH_DAY);
        mEndPickView = new TimePickerView(this, TimePickerView.Type.YEAR_MONTH_DAY);

        MultiPagerAdapter.Listener lListener = recordEntry -> {

        };

        mAdapter = new MultiPagerAdapter(this, lListener);
        mAdapter.setFooterLoadingShowStringResource("加载中...");

        mBeginPickView.setCyclic(true);
        mEndPickView.setCyclic(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detachView(false);
        mToolBarManager.destroy();
    }

    @NonNull
    @Override
    public MultiPageViewPresenter createPresenter() {
        return new MultiPageViewPresenter();
    }

    @Override
    public void showResultList(RegisteredRecordDto registeredRecordDto) {

    }

    private void updateRecyclerView() {
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void setDate(String lNowString, String lBeginString, String lLastString) {
        mBeginDateTextView.setText(lBeginString);
        mEndDateTextView.setText(lLastString);
        mBeginPickView.setTime(new DateTime(lBeginString).toDate());
        mEndPickView.setTime(new DateTime(lLastString).toDate());
        mBeginDate = lBeginString;
        mEndDate = lLastString;
    }

    @Override
    public void LoadFirst(List<RegisteredRecordDto.recordEntry> recordEntryList) {

        mEndlessRecyclerOnScrollListener.restoreStatus();
        boolean lHasFooter = recordEntryList.size() >= 10;
        mAdapter.setHasMoreDataAndFooter(lHasFooter, lHasFooter);
        mAdapter.clear();
        mAdapter.appendToList(recordEntryList);
        updateRecyclerView();
        mSwipeRefreshLayout.post(() -> mSwipeRefreshLayout.setRefreshing(false));

    }

    @Override
    public void LoadMore(int page, List<RegisteredRecordDto.recordEntry> recordEntryList) {
        mAdapter.setHasMoreDataAndFooter(true, true);
        // 在列表后加上下一页的数据
        mAdapter.appendToList(recordEntryList);
        final int lItemCount = mAdapter.getItemCount();
        mAdapter.notifyItemRangeInserted(lItemCount, recordEntryList.size());
    }

    @Override
    public void LoadEnd(int page) {
        //刷新footer
        mAdapter.setHasMoreDataAndFooter(false, true);
        mEndlessRecyclerOnScrollListener.setLastPage(page);
        mEndlessRecyclerOnScrollListener.setLoadMoreEnable(false);
    }

    @Override
    public void showLoadingDialog() {
        mSwipeRefreshLayout.post(() -> mSwipeRefreshLayout.setRefreshing(true));
    }
}
