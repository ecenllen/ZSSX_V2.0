package com.github.captain_miao.recyclerviewutils.swipetoloadlayout;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.github.captain_miao.recyclerviewutils.R;


/**
 * [Description]
 * <p/>
 * [How to use]
 * <p/>
 * [Tips]
 *
 * @author Created by  on 2016-09-27 14:15
 * @since 1.0.0
 */
public class RefreshRecyclerView extends RelativeLayout {

    private SwipeToLoadLayout mSwipeToLoadLayout;
    private RecyclerView mRecyclerView;
    //    private LinearLayout mNoDataImg;
    private View mNoDataView;


    public RefreshRecyclerView(Context context) {
        this(context, null);
    }

    public RefreshRecyclerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RefreshRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.ptl_recycleview, this);
        mSwipeToLoadLayout = (SwipeToLoadLayout) view.findViewById(R.id.swipeRefreshLayout);
        mSwipeToLoadLayout.setSwipeStyle(SwipeToLoadLayout.STYLE.CLASSIC);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.swipe_target);
//        mNoDataImg = (LinearLayout) view.findViewById(R.id.no_data_img);
    }

    public void setOnRefreshListener(OnRefreshListener onRefreshListener) {
        mSwipeToLoadLayout.setOnRefreshListener(onRefreshListener);

    }

    public void setRefreshStyle(int style) {
        mSwipeToLoadLayout.setSwipeStyle(style);
    }

    public void setRefreshing(Boolean refreshing) {
        mSwipeToLoadLayout.setRefreshing(refreshing);
    }

    public void autoRefresh() {
        mSwipeToLoadLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeToLoadLayout.setRefreshing(true);
            }
        });
    }

    public RecyclerView getRecyclerView() {
        return mRecyclerView;
    }

    public void setNoDataVisibility(int visibility) {
        if (mNoDataView == null) {
            mNoDataView = ((ViewStub) findViewById(R.id.import_stub)).inflate();
        }
        mNoDataView.setVisibility(visibility);
//        mNoDataImg.setVisibility(visibility);
    }

    public void setLayoutManager(RecyclerView.LayoutManager layoutManager) {
        mRecyclerView.setLayoutManager(layoutManager);
    }

    public void setAdapter(RecyclerView.Adapter adapter) {
        mRecyclerView.setAdapter(adapter);
    }

}
