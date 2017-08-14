package com.gta.zssx.mobileOA.view;

import com.gta.utils.mvp.BaseView;

/**
 * Created by lan.zheng on 2016/11/15.
 */
public interface DutyDateSelectView extends BaseView {

    /**
     * 显示数据为空
     */
    void showEmpty();

    /**
     * 加载更多失败
     */
    void onLoadMoreError();

    /**
     * 刷新失败
     */
    void onRefreshError();

    /**
     *
     */
    void onLoadMoreEmpty();
}
