package com.gta.zssx.fun.adjustCourse.deprecated.view;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blankj.utilcode.util.SPUtils;
import com.github.captain_miao.recyclerviewutils.EndlessRecyclerOnScrollListener;
import com.gta.zssx.R;
import com.gta.zssx.fun.adjustCourse.model.bean.ApplyConfirmBean;
import com.gta.zssx.fun.adjustCourse.model.bean.ApplyDetailBean;
import com.gta.zssx.fun.adjustCourse.model.utils.RefreshRecyclerView;
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
 * @author Created by Zhimin.Huang on 2017/1/3.
 * @since 1.0.0
 */
@Deprecated
public class ConfirmTestFragment extends BaseFragment<ApplyConfirmView, ApplyConfirmPresenter> implements ApplyConfirmView {

    public RefreshRecyclerView mRecyclerView;
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
        mRecyclerView.setNoDataVisibility(View.GONE);
        mRecyclerView.setRefreshing(false);
        mEndlessRecyclerOnScrollListener.restoreStatus();
        mAdapter.loadFirst(lList, 20);
        updateRecyclerView();
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
        mAdapter.loadEnd();
        mEndlessRecyclerOnScrollListener.setLastPage(page);
        mEndlessRecyclerOnScrollListener.setLoadMoreEnable(false);
    }

    @Override
    public void showEmpty(String message) {
        if (mAdapter != null) {
            mAdapter.clear();
        }
        mRecyclerView.setNoDataVisibility(View.VISIBLE);
        mRecyclerView.setNoDataRefreshing(false);
        mRecyclerView.setRefreshing(false);
    }

    @Override
    public void onRefreshError(int page) {
        if (page == 1) {
            mRecyclerView.setRefreshing(false);
        } else {
            mAdapter.setOnLoadMoreError();
        }
    }

    public ConfirmTestFragment() {

    }

    public static ConfirmTestFragment newInstance(int type) {
        ConfirmTestFragment fragment = new ConfirmTestFragment();
        Bundle lArgs = new Bundle();
        lArgs.putInt("", type);
        fragment.setArguments(lArgs);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_refresh, container, false);
        initView(view);
        setInteractListener();
        return view;
    }

    private void setInteractListener() {

        mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));

        mRecyclerView.setOnRefreshListener(() -> {
            loadData(1);
            SPUtils mSPUtils = new SPUtils(AdjustCourse.RED_POINT_SP);
            mSPUtils.clear();
            RxBus.getDefault().post(new Integer(2));
        });


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

    private void loadData(int page) {
        if (page == 1) {
            mRecyclerView.setRefreshing(true);
        }
        presenter.getApplyConfirm(page, 1, "Y");
    }

    private void initView(View view) {
        mRecyclerView = (RefreshRecyclerView) view.findViewById(R.id.refresh_view);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.attachView(ConfirmTestFragment.this);
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
