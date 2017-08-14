package com.gta.zssx.fun.adjustCourse.model.utils;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gta.zssx.R;


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
public class RefreshRecyclerView extends RelativeLayout {

    private TextView mNoRecord;
    private SwipeRefreshLayout mRefreshLayout;
    private SwipeRefreshLayout mEmptyRefresh;
    private RecyclerView mRecyclerView;
    public SwipeRefreshLayout.OnRefreshListener mOnRefreshListener;

    public RefreshRecyclerView(Context context) {
        this(context, null);
    }

    public RefreshRecyclerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RefreshRecyclerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        View lInflate = LayoutInflater.from(context).inflate(R.layout.fragment_apply_recourd_list, this);
        mNoRecord = (TextView) lInflate.findViewById(R.id.no_record_tv);
        mRefreshLayout = (SwipeRefreshLayout) lInflate.findViewById(R.id.swipe_refresh_layout);
        mEmptyRefresh = (SwipeRefreshLayout) lInflate.findViewById(R.id.swipe_refresh_empty_view);
        mRecyclerView = (RecyclerView) lInflate.findViewById(R.id.recyclerView);
    }

    public void setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener onRefreshListener) {
        mOnRefreshListener = onRefreshListener;
        mRefreshLayout.setOnRefreshListener(mOnRefreshListener);
        mEmptyRefresh.setOnRefreshListener(mOnRefreshListener);
    }

    public void setNoDataVisibility(int visibility) {
        mEmptyRefresh.setVisibility(visibility);
    }

    public RecyclerView getRecyclerView() {
        return mRecyclerView;
    }

    public void setAdapter(RecyclerView.Adapter adapter) {
        mRecyclerView.setAdapter(adapter);
    }

    public void setLayoutManager(RecyclerView.LayoutManager layoutManager) {
        mRecyclerView.setLayoutManager(layoutManager);
    }

    public void setRefreshing(boolean refreshing) {
        mRefreshLayout.post(() -> mRefreshLayout.setRefreshing(refreshing));
    }

    public void setNoDataRefreshing(boolean refreshing) {
        mEmptyRefresh.post(() -> mEmptyRefresh.setRefreshing(refreshing));
    }

    public RecyclerView.LayoutManager getLayoutManager() {
        return mRecyclerView.getLayoutManager();
    }

    public void addOnScrollListener(RecyclerView.OnScrollListener onScrollListener) {
        mRecyclerView.addOnScrollListener(onScrollListener);
    }
}
