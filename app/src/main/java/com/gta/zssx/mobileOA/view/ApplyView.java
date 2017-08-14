package com.gta.zssx.mobileOA.view;

import com.gta.utils.mvp.BaseView;
import com.gta.zssx.mobileOA.model.bean.ApplyForm;
import com.gta.zssx.mobileOA.model.bean.MyApplyBean;

import java.util.List;

/**
 * Created by xiaoxia.rang on 2016/11/14.
 * 我的申请
 */

public interface ApplyView extends BaseView {

    void showFormList(List<ApplyForm> forms);

    void setServerTime(String serverTime);

    void showRefreshApplyList(List<MyApplyBean> backlogs);


    void showLoadMoreApplyList(List<MyApplyBean> backlogs);

    /**
     * 显示数据为空
     */
    void showEmpty();

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
