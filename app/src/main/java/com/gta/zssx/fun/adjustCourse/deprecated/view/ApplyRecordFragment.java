package com.gta.zssx.fun.adjustCourse.deprecated.view;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.captain_miao.recyclerviewutils.EndlessRecyclerOnScrollListener;
import com.gta.zssx.R;
import com.gta.zssx.fun.adjustCourse.model.bean.ApplyRecordBean;
import com.gta.zssx.fun.adjustCourse.deprecated.presenter.ApplyRecordPresenter;
import com.gta.zssx.fun.adjustCourse.view.adapter.ApplyRecordAdapter;
import com.gta.zssx.pub.base.BaseFragment;

import java.util.List;


/**
 * [Description]
 * <p>
 * [How to use]
 * <p>
 * [Tips]
 *
 * @author Created by Zhimin.Huang on 2016/10/24.
 * @since 1.0.0
 */
@Deprecated
public class ApplyRecordFragment extends BaseFragment<ApplyRecordView,
        ApplyRecordPresenter> implements ApplyRecordView {

    private static String sType = "Type";
    private int mType;
    public TextView mNoRecord;
    public SwipeRefreshLayout mRefreshLayout;
    public RecyclerView mRecyclerView;


    /**
     * 底部加载监听事件
     */
    private EndlessRecyclerOnScrollListener mEndlessRecyclerOnScrollListener;
    private ApplyRecordAdapter mAdapter;
    private SwipeRefreshLayout mEmptyRefresh;

    @NonNull
    @Override
    public ApplyRecordPresenter createPresenter() {
        return new ApplyRecordPresenter();
    }

    public ApplyRecordFragment() {

    }

    public static ApplyRecordFragment newInstance(int type) {
        ApplyRecordFragment fragment = new ApplyRecordFragment();
        Bundle lArgs = new Bundle();
        lArgs.putInt(sType, type);
        fragment.setArguments(lArgs);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_apply_recourd_list, container, false);
        initView(view);
        setInteractListener();
        return view;
    }

    public void scrollToTop() {
        mRecyclerView.smoothScrollToPosition(0);
    }

    private void setInteractListener() {
        mRefreshLayout.setOnRefreshListener(() -> loadData(1));

        mEmptyRefresh.setOnRefreshListener(() -> loadData(1));

        //设置到达底部加载更多事件
        final LinearLayoutManager lLayoutManager = (LinearLayoutManager) mRecyclerView.getLayoutManager();

        mEndlessRecyclerOnScrollListener = new EndlessRecyclerOnScrollListener(lLayoutManager) {
            @Override
            public void onLoadMore(int i) {
                loadData(i);
            }
        };
        mRecyclerView.addOnScrollListener(mEndlessRecyclerOnScrollListener);

        ApplyRecordAdapter.Listener lListener = new ApplyRecordAdapter.Listener() {
            @Override
            public void itemLongClick(ApplyRecordBean.ListBean listBean) {

            }

            @Override
            public void itemClick(ApplyRecordBean.ListBean listBean) {
                ApplyDetailActivity.start(mActivity, listBean.getRecordId(), ApplyDetailActivity.RECORD);
            }

        };


        mAdapter = new ApplyRecordAdapter(mActivity, lListener);
        mAdapter.setFooterLoadingShowStringResource("加载中...");
    }

    private void initView(View view) {
        mNoRecord = (TextView) view.findViewById(R.id.no_record_tv);
        mRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        mEmptyRefresh = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_empty_view);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);


        mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mType = getArguments().getInt(sType);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.attachView(ApplyRecordFragment.this);
        initData();
        loadData(1);
    }

    public void loadData(int page) {

        if (page == 1) {
            if (mRefreshLayout != null) {
                mRefreshLayout.post(() -> mRefreshLayout.setRefreshing(true));
            }
            if (mEmptyRefresh != null) {
                mEmptyRefresh.post(() -> mEmptyRefresh.setRefreshing(true));
            }
        }
        presenter.getApplyRecord(page);
    }

    private void initData() {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.detachView(false);
    }


    @Override
    public void LoadFirst(ApplyRecordBean applyRecordBean) {
        List<ApplyRecordBean.ListBean> lList = applyRecordBean.getList();
        mEmptyRefresh.setVisibility(View.GONE);
        mEndlessRecyclerOnScrollListener.restoreStatus();

        mAdapter.loadFirst(lList, 20);
        updateRecyclerView();
        mRefreshLayout.post(() -> mRefreshLayout.setRefreshing(false));
        mEmptyRefresh.post(() -> mEmptyRefresh.setRefreshing(false));
    }

    private void updateRecyclerView() {
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void LoadMore(int page, ApplyRecordBean applyRecordBean) {
        List<ApplyRecordBean.ListBean> lList = applyRecordBean.getList();
        mAdapter.loadMore(lList);
    }

    @Override
    public void LoadEnd(int page) {
        //刷新footer
        mAdapter.loadEnd();
        mEndlessRecyclerOnScrollListener.setLastPage(page);
        mEndlessRecyclerOnScrollListener.setLoadMoreEnable(false);
    }

    @Override
    public void showEmpty(String message) {
        if (mAdapter != null) {
            mAdapter.clear();
        }
        mRefreshLayout.post(() -> mRefreshLayout.setRefreshing(false));
        mNoRecord.setText(message);
        mEmptyRefresh.setVisibility(View.VISIBLE);
        mEmptyRefresh.post(() -> mEmptyRefresh.setRefreshing(false));
    }

    @Override
    public void onRefreshError(int page) {
        if (page == 1) {
            mRefreshLayout.post(() -> mRefreshLayout.setRefreshing(false));
//            mAdapter.clear();
//            mAdapter.notifyDataSetChanged();
//            mAdapter.clearData();
        } else {
//            mAdapter.setHasMoreDataAndFooter(true, true);
//            mAdapter.setFooterNoMoreDataShowStringResource("加载失败...");
            mAdapter.setOnLoadMoreError();
        }
    }
}
