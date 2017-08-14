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
import com.gta.zssx.fun.adjustCourse.model.bean.ApplyBean;
import com.gta.zssx.fun.adjustCourse.deprecated.presenter.ApplyCoursePresenter;
import com.gta.zssx.fun.adjustCourse.view.adapter.ApplyCourseAdapter;
import com.gta.zssx.pub.base.BaseFragment;
import com.gta.zssx.pub.util.RxBus;

import java.util.List;

import rx.Subscriber;


/**
 * [Description]
 * <p>
 * [How to use]
 * <p>
 * [Tips]
 *
 * @author Created by Zhimin.Huang on 2016/10/28.
 * @since 1.0.0
 */
@Deprecated
public class ApplyCourseFragment extends BaseFragment<ApplyCourseView, ApplyCoursePresenter> implements ApplyCourseView {


    public TextView mNoRecord;
    public SwipeRefreshLayout mRefreshLayout;
    public RecyclerView mRecyclerView;
    private SwipeRefreshLayout mEmptyRefresh;

    /**
     * 底部加载监听事件
     */
    private EndlessRecyclerOnScrollListener mEndlessRecyclerOnScrollListener;
    private ApplyCourseAdapter mAdapter;
    public int mPosition;

    @NonNull
    @Override
    public ApplyCoursePresenter createPresenter() {

        return new ApplyCoursePresenter();
    }


    @Override
    public void LoadFirst(ApplyBean applyBean) {
        List<ApplyBean.ListBean> lList = applyBean.getList();
        mEmptyRefresh.setVisibility(View.GONE);
        mEndlessRecyclerOnScrollListener.restoreStatus();

        mEmptyRefresh.post(() -> mEmptyRefresh.setRefreshing(false));
        mAdapter.loadFirst(lList, 20);
        updateRecyclerView();
        mRefreshLayout.post(() -> mRefreshLayout.setRefreshing(false));
    }

    private void updateRecyclerView() {
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void LoadMore(int page, ApplyBean applyBean) {
        List<ApplyBean.ListBean> lList = applyBean.getList();


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
            mAdapter.clearData();
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
        } else {
//            mAdapter.setHasMoreDataAndFooter(true, true);
//            mAdapter.setFooterNoMoreDataShowStringResource("加载失败...");
            mAdapter.setOnLoadMoreError();
        }
    }

    public ApplyCourseFragment() {

    }

    public static ApplyCourseFragment newInstance(int type) {
        ApplyCourseFragment fragment = new ApplyCourseFragment();
        Bundle lArgs = new Bundle();
        lArgs.putInt("", type);
        fragment.setArguments(lArgs);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_apply_course_list, container, false);
        initView(view);
        setInteractListener();
        return view;
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

        ApplyCourseAdapter.Listener lListener = new ApplyCourseAdapter.Listener() {
            @Override
            public void itemLongClick(ApplyBean.ListBean listBean) {

            }

            @Override
            public void itemClick(ApplyBean.ListBean listBean, int position) {
                mPosition = position;
                ApplyDetailActivity.start(mActivity, listBean.getTransferApplyId(), ApplyDetailActivity.APPLY);
            }

        };


        mAdapter = new ApplyCourseAdapter(mActivity, lListener);
        mAdapter.setFooterLoadingShowStringResource("加载中...");
    }

    private void initView(View view) {
        mNoRecord = (TextView) view.findViewById(R.id.no_record_tv);
        mRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        mEmptyRefresh = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_empty_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));


    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.attachView(ApplyCourseFragment.this);
        initData();
        loadData(1);

        //用于返回删除ApplyDetailActivity 362行
        presenter.mCompositeSubscription.add(RxBus.getDefault().toObserverable(ApplyBean.ListBean.class)
                .subscribe(new Subscriber<ApplyBean.ListBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(ApplyBean.ListBean listBean) {
//                        mAdapter.getList().remove(mPosition);
//                        mAdapter.notifyItemRemoved(mPosition);
                        loadData(1);
                    }
                }));
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
        presenter.getApply(page);
    }

    private void initData() {

    }

    public void scrollToTop() {
        mRecyclerView.smoothScrollToPosition(0);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.detachView(false);
    }
}
