package com.gta.zssx.mobileOA.view;

import com.gta.utils.mvp.BaseView;
import com.gta.zssx.mobileOA.model.bean.Backlog;

import java.util.List;

/**
 * Created by xiaoxia.rang on 2016/10/27.
 */

public interface BacklogMainView extends BaseView {

    /**
     * 显示数据为空
     */
    void showEmpty();

    /**
     * 设置服务器时间
     * @param serverTime
     */
    void setServerTime(String serverTime);

    /**
     * 显示任务List
     */
    void refreshBacklogList(List<Backlog> backlogs);

    void appendBacklogList(List<Backlog> backlogs);

    /**
     * 加载更多失败
     */
    void onLoadMoreError ();

    /**
     * 刷新失败
     */
    void onRefreshError ();

    /**
     *
     */
    void onLoadMoreEmpty ();
}
