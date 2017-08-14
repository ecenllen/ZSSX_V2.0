package com.gta.zssx.fun.adjustCourse.view.base;

import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.chad.library.adapter.base.listener.OnItemLongClickListener;
import com.github.captain_miao.recyclerviewutils.swipetoloadlayout.OnRefreshListener;
import com.github.captain_miao.recyclerviewutils.swipetoloadlayout.RefreshRecyclerView;
import com.gta.utils.mvp.BasePresenter;
import com.gta.utils.mvp.BaseView;
import com.gta.utils.resource.Toast;
import com.gta.zssx.R;
import com.gta.zssx.fun.adjustCourse.model.nav.OnTabReselectListener;
import com.gta.zssx.fun.adjustCourse.model.utils.MultiPageHelper;
import com.gta.zssx.pub.base.*;

import java.util.List;

import butterknife.Bind;


/**
 * [Description]
 * <p>
 * [How to use]
 * <p>
 * [Tips]
 *
 * @author Created by Zhimin.Huang on 2017/3/16.
 * @since 1.0.0
 */
public abstract class BaseRecyclerViewFragment<V extends BaseView, P extends BasePresenter<V>, T>
        extends BaseMvpFragment<V, P> implements OnRefreshListener,
        BaseQuickAdapter.RequestLoadMoreListener, MultiPageHelper.Listener<T>, OnTabReselectListener {

    @Bind(R.id.refresh_view)
    protected RefreshRecyclerView mRefreshRecyclerView;

    protected int mPage = 1;
    protected MultiPageHelper mMultiPageHelper;
    protected BaseQuickAdapter mBaseQuickAdapter;

    @Override
    public int getLayoutId() {
        return R.layout.refresh_recyclerview;
    }

    @Override
    protected void initView(View view) {
        mRefreshRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRefreshRecyclerView.setOnRefreshListener(this);
        mMultiPageHelper = new MultiPageHelper(this);
        mRefreshRecyclerView.getRecyclerView().addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                itemClick((T) adapter.getData().get(position), position);
            }
        });

        mRefreshRecyclerView.getRecyclerView().addOnItemTouchListener(new OnItemLongClickListener() {
            @Override
            public void onSimpleItemLongClick(BaseQuickAdapter adapter, View view, int position) {
                itemLongClick((T) adapter.getData().get(position), position);
            }
        });
    }

    @Override
    public void onRefresh() {
        mPage = 1;
        requestData();
    }

    @Override
    public void onLoadMoreRequested() {
        mPage++;
        requestData();
    }

    @Override
    public void onTabReselect() {
        mRefreshRecyclerView.autoRefresh();
    }


    @Override
    public void initAdapter(List<T> list) {
        mBaseQuickAdapter = getRecyclerAdapter();
        mRefreshRecyclerView.setNoDataVisibility(View.GONE);
        mBaseQuickAdapter.setNewData(list);
        mBaseQuickAdapter.setOnLoadMoreListener(this);
        mRefreshRecyclerView.setAdapter(mBaseQuickAdapter);
    }

    @Override
    public void showEmptyUI(boolean isShow) {
        mRefreshRecyclerView.setNoDataVisibility(isShow ? View.VISIBLE : View.GONE);
    }

    @Override
    public void getDataFail(boolean isFirstPage) {
        if (!isFirstPage) {
            mPage--;
        }
        Toast.Long(mActivity, "获取数据失败");
        showEmptyUI(true);
    }

    protected abstract BaseQuickAdapter getRecyclerAdapter();


    protected void itemClick(T t, int position) {

    }

    protected void itemLongClick(T t, int position) {

    }

    protected void requestData() {

    }

    protected void update(List<T> list) {
        if (mPage == 1) {
            mRefreshRecyclerView.setRefreshing(false);
        }
        mMultiPageHelper.update(list, mBaseQuickAdapter, mPage);
    }
}
