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

import com.blankj.utilcode.util.SPUtils;
import com.github.captain_miao.recyclerviewutils.EndlessRecyclerOnScrollListener;
import com.gta.zssx.R;
import com.gta.zssx.fun.adjustCourse.model.bean.ApplyConfirmBean;
import com.gta.zssx.fun.adjustCourse.model.bean.ApplyDetailBean;
import com.gta.zssx.fun.adjustCourse.deprecated.presenter.ApplyConfirmPresenter;
import com.gta.zssx.fun.adjustCourse.view.adapter.ApplyConfirmAdapter;
import com.gta.zssx.fun.adjustCourse.view.base.AdjustCourse;
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
public class ApplyConfirmFragment extends BaseFragment<ApplyConfirmView, ApplyConfirmPresenter> implements ApplyConfirmView {


    public TextView mNoRecord;
    public SwipeRefreshLayout mRefreshLayout;
    private SwipeRefreshLayout mEmptyRefresh;
    public RecyclerView mRecyclerView;

    /**
     * 底部加载监听事件
     */
    private EndlessRecyclerOnScrollListener mEndlessRecyclerOnScrollListener;
    private ApplyConfirmAdapter mAdapter;

    @NonNull
    @Override
    public ApplyConfirmPresenter createPresenter() {
        return new ApplyConfirmPresenter();
    }


    @Override
    public void LoadFirst(ApplyConfirmBean applyConfirmBean) {
        List<ApplyConfirmBean.ListBean> lList = applyConfirmBean.getList();
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
    public void LoadMore(int page, ApplyConfirmBean applyConfirmBean) {
        List<ApplyConfirmBean.ListBean> lList = applyConfirmBean.getList();

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
        } else {
//            mAdapter.setHasMoreDataAndFooter(true, true);
//            mAdapter.setFooterNoMoreDataShowStringResource("加载失败...");
            mAdapter.setOnLoadMoreError();
        }
    }

    public ApplyConfirmFragment() {

    }

    public static ApplyConfirmFragment newInstance(int type) {
        ApplyConfirmFragment fragment = new ApplyConfirmFragment();
        Bundle lArgs = new Bundle();
        lArgs.putInt("", type);
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

    private void setInteractListener() {
        mRefreshLayout.setOnRefreshListener(() -> {
            loadData(1);
            SPUtils mSPUtils = new SPUtils( AdjustCourse.RED_POINT_SP);
            mSPUtils.clear();
            RxBus.getDefault().post(new Integer(2));
        });

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

        ApplyConfirmAdapter.Listener lListener = new ApplyConfirmAdapter.Listener() {
            @Override
            public void itemLongClick(ApplyConfirmBean.ListBean listBean) {

            }

            @Override
            public void itemClick(ApplyConfirmBean.ListBean listBean) {
                ApplyDetailActivity.start(mActivity, listBean.getTransferApplyId(), ApplyDetailActivity.CONFIRM);
            }

        };


        mAdapter = new ApplyConfirmAdapter(mActivity, lListener);
        mAdapter.setFooterLoadingShowStringResource("加载中...");
    }

    public void loadData(int page) {
        if (page == 1) {
            if (mRefreshLayout != null) {
                mRefreshLayout.post(() -> mRefreshLayout.setRefreshing(true));
            }
        }
        presenter.getApplyConfirm(page,1,"N");
    }

    private void initView(View view) {
        mNoRecord = (TextView) view.findViewById(R.id.no_record_tv);
        mRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        mEmptyRefresh = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_empty_view);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
    }

    public void scrollToTop() {
        mRecyclerView.smoothScrollToPosition(0);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.attachView(ApplyConfirmFragment.this);
        initData();
        loadData(1);
    }

    private void initData() {
        //用于调代课确认返回刷新ApplyDetailActivity 370行
        presenter.mCompositeSubscription.add(RxBus.getDefault().toObserverable(ApplyDetailBean.class)
                .subscribe(new Subscriber<ApplyDetailBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(ApplyDetailBean applyDetailBean) {
                        loadData(1);
                    }
                }));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.detachView(false);
    }
}
