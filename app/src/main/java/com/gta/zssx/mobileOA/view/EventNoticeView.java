package com.gta.zssx.mobileOA.view;

import com.gta.utils.mvp.BaseView;
import com.gta.zssx.mobileOA.model.bean.EventNoticeInfo;

/**
 * Created by lan.zheng on 2016/10/31.
 */
public interface EventNoticeView extends BaseView {
    /**
     * 显示数据为空
     */
    void showEmptyView();

    /**
     * 显示任务List
     */
    void showEventList(EventNoticeInfo eventNoticeInfo,int action);

    /**
     * 加载更多失败
     */
    void onLoadMoreError();

    /**
     * 刷新失败
     */
    void onRefreshError();

    /**
     * 加载更多为空
     */
    void onLoadMoreEmpty();
}
