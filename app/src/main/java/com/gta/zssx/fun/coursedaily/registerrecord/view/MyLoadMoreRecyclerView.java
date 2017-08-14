package com.gta.zssx.fun.coursedaily.registerrecord.view;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.gta.zssx.R;

/**
 * Created by lan.zheng on 2016/6/27.
 */
public class MyLoadMoreRecyclerView extends LinearLayout {

    private Context mContext;
    static final int VERTICAL = LinearLayoutManager.VERTICAL;
    private RecyclerView recyclerView;
    /**
     * 刷新布局控件
     */
    private SwipeRefreshLayout swipeRfl;
    private LinearLayoutManager layoutManager;
    /*
    * 刷新布局的监听
    */
    private SwipeRefreshLayout.OnRefreshListener mRefreshListener;
    /**
     * 内容控件滚动监听
     */
    private RecyclerView.OnScrollListener mScrollListener;
    /**
     * 内容适配器
     */
    private RecyclerView.Adapter mAdapter;
    /**
     * 监听
     */
    private LoadMoreListener mRefreshLoadMoreListner;
    /**
     * 是否可以加载更多
     */
    public static boolean hasMore = true;
    /**
     * 是否正在刷新
     */
    private boolean isRefresh = false;
    /**
     * 是否正在加载更多
     */
    private boolean isLoadMore = false;

    public MyLoadMoreRecyclerView(Context context) {
        super(context);

    }

    public MyLoadMoreRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }


    /**
     * 初始化-添加滚动监听
     * <p>
     * 回调加载更多方法，前提是
     * <pre>
     *    1、有监听并且支持加载更多：null != mListener && mIsFooterEnable
     *    2、目前没有在加载，正在上拉（dy>0），当前最后一条可见的view是否是当前数据列表的最好一条--及加载更多
     * </pre>
     */
    private void init() {
        LayoutInflater.from(mContext).inflate(
                R.layout.pull_to_refresh_recyclerview, this, true);
        swipeRfl = (SwipeRefreshLayout) findViewById(R.id.all_swipe);
        recyclerView = (RecyclerView) findViewById(R.id.list_item_class_show_from_class_log_mainpage);
        /**
         * 监听上拉至底部滚动监听
         */
        mScrollListener = new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                //最后显示的项
                int lastVisibleItem = layoutManager
                        .findLastVisibleItemPosition();
                int totalItemCount = layoutManager.getItemCount();
                // lastVisibleItem >= totalItemCount - 4 表示剩下4个item自动加载
                // dy>0 表示向下滑动
                /*  if (hasMore && (lastVisibleItem >= totalItemCount - 1)
                        && dy > 0 && !isLoadMore) {
                    isLoadMore = true;
                    loadMore();
                }*/
                /**
                 * 无论水平还是垂直
                 */
                if (hasMore && (lastVisibleItem >= totalItemCount - 1)
                        && !isLoadMore) {
                    isLoadMore = true;
                    loadMore();
                }

            }
        };
        /**
         * 下拉至顶部刷新监听
         */
        /*mRefreshListener = new OnRefreshListener() {

            @Override
            public void onRefresh() {
                if (!isRefresh) {
                    isRefresh = true;
                    refresh();
                }
            }
        };*/
//        swipeRfl.setOnRefreshListener(mRefreshListener);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setOnScrollListener(mScrollListener);


    }

    public void setOrientation(int orientation) {
//        if (orientation != 0 && orientation != 1) {
            layoutManager.setOrientation(VERTICAL);
//        } else {
//            layoutManager.setOrientation(HORIZONTAL);
//        }
    }

    public int getOrientation() {
//        return layoutManager.getOrientation();
        return LinearLayout.VERTICAL;
    }

    public void setPullLoadMoreEnable(boolean enable) {
        this.hasMore = enable;
    }

    public boolean getPullLoadMoreEnable() {
        return hasMore;
    }

    public void setPullRefreshEnable(boolean enable) {
        swipeRfl.setEnabled(enable);
    }

    public boolean getPullRefreshEnable() {
        return swipeRfl.isEnabled();
    }

    public void setLoadMoreListener() {
        recyclerView.setOnScrollListener(mScrollListener);
    }

    public void loadMore() {
        if (mRefreshLoadMoreListner != null && hasMore) {
            mRefreshLoadMoreListner.onLoadMore();

        }
    }

    public void setLoadMoreCompleted(){
        isLoadMore = false;
    }

    public void stopRefresh() {
        isRefresh = false;
        swipeRfl.setRefreshing(false);
    }

    public void setRefreshLoadMoreListener(LoadMoreListener listener) {
        mRefreshLoadMoreListner = listener;
    }

    /*public void refresh() {
        if (mRefreshLoadMoreListner != null) {
            mRefreshLoadMoreListner.onRefresh();
        }
    }*/

    public void setAdapter(RecyclerView.Adapter adapter) {
        if (adapter != null)
            recyclerView.setAdapter(adapter);
    }

    /**
     * 加载更多监听
     */
    public interface LoadMoreListener {

        void onLoadMore();
    }
}
