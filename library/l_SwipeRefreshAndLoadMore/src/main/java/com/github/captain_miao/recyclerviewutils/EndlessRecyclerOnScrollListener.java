package com.github.captain_miao.recyclerviewutils;

/*
 * Copyright (C) 2015 Jorge Castillo Pérez
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * modify from https://github.com/JorgeCastilloPrz/Mirage
 */


import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

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
public abstract class EndlessRecyclerOnScrollListener extends RecyclerView.OnScrollListener {
    public static String TAG = EndlessRecyclerOnScrollListener.class.getSimpleName();

    private boolean loadMoreEnable = true;
    //list到达 最后一个item的时候 触发加载
    private int visibleThreshold = 1;
    // The minimum amount of items to have below your current scroll position before loading more.
    int visibleItemCount, totalItemCount;
    //默认第一页
    private int current_page = 1;

    private LinearLayoutManager mLinearLayoutManager;
    private int lastVisibleItemPosition;
    public int mLastPage;

    public EndlessRecyclerOnScrollListener(LinearLayoutManager linearLayoutManager) {
        this.mLinearLayoutManager = linearLayoutManager;
    }

    public void restoreStatus() {
        visibleThreshold = 1;
        current_page = 1;
        mLastPage = 0;
        loadMoreEnable = true;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        lastVisibleItemPosition = mLinearLayoutManager.findLastVisibleItemPosition();

    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
        visibleItemCount = recyclerView.getChildCount();
        RecyclerView.Adapter lAdapter = recyclerView.getAdapter();
        if (lAdapter != null) {
            totalItemCount = lAdapter.getItemCount();
        }
        if (loadMoreEnable && lAdapter != null && newState == RecyclerView.SCROLL_STATE_IDLE &&
                lastVisibleItemPosition + 1 == lAdapter.getItemCount()
                && totalItemCount > visibleItemCount) {

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (mLastPage == 0) {
                        current_page++;
                        onLoadMore(current_page);
                    }

                }
            }, 1000);
        }
    }

    public int getVisibleThreshold() {
        return visibleThreshold;
    }

    public void setVisibleThreshold(int visibleThreshold) {
        this.visibleThreshold = visibleThreshold;
    }


    public void setLastPage(int lastPage) {
        mLastPage = lastPage;
    }

    public int getCurrent_page() {
        return current_page;
    }

    public void setCurrent_page(int current_page) {
        this.current_page = current_page;
    }

    public void setLoadMoreEnable(boolean loadMoreEnable) {
        this.loadMoreEnable = loadMoreEnable;
    }

    public abstract void onLoadMore(int current_page);
}
