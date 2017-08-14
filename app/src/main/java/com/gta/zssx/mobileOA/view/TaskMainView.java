package com.gta.zssx.mobileOA.view;


import com.gta.utils.mvp.BaseView;
import com.gta.zssx.mobileOA.model.bean.Tasks;

import java.util.List;

/**
 * Created by xiaoxia.rang on 2016/10/19.
 */

public interface TaskMainView extends BaseView {

    /**
     * 显示数据为空
     */
    void showEmpty();

    /**
     * 显示任务List
     */
    void showTasksList(int status,List<Tasks.TaskInfo> taskInfos);

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
