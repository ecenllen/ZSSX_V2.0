package com.gta.zssx.fun.adjustCourse.model.utils;


import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.List;


/**
 * [Description]
 * <p>
 * [How to use]
 * <p>
 * [Tips]
 *
 * @author Created by Zhimin.Huang on 2017/3/10.
 * @since 1.0.0
 */
public class MultiPageHelper {


    private Listener mListener;

    public MultiPageHelper(Listener listener) {
        mListener = listener;
    }

    public interface Listener<T> {

        void initAdapter(List<T> list);

        void showEmptyUI(boolean isShow);

        void getDataFail(boolean isFirstPage);
    }

    public void update(List list, BaseQuickAdapter mAdapter, int currentPage) {
        if (currentPage == 1) {
            if (list == null) {
                mListener.getDataFail(true);
            } else {
                if (mAdapter == null) {
                    mListener.initAdapter(list);
                    mListener.showEmptyUI(list.size() == 0);
                } else {
                    mAdapter.setNewData(list);
                    mListener.showEmptyUI(list.size() == 0);
                }
            }
        } else {
            if (list == null) {
                mAdapter.loadMoreFail();
                mListener.getDataFail(false);
            } else if (list.size() == 0) {
                mAdapter.loadMoreEnd();
            } else {
                mAdapter.addData(list);
                mAdapter.loadMoreComplete();
            }
        }
    }
}
