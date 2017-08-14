package com.gta.zssx.mobileOA.view;

import com.gta.utils.mvp.BaseView;
import com.gta.zssx.mobileOA.model.bean.DutyNoticeInfo;

import java.util.List;

/**
 * Created by lan.zheng on 2016/11/7.
 */
public interface DutyNoticeView extends BaseView {
    void showEmptyView();

    void showDutyListData(List<DutyNoticeInfo> list,int action);
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
