package com.gta.zssx.mobileOA.view;

import com.gta.utils.mvp.BaseView;
import com.gta.zssx.mobileOA.model.bean.Meeting;
import com.gta.zssx.mobileOA.model.bean.OfficeNoticeInfo;

import java.util.List;

/**
 * Created by lan.zheng on 2016/11/3.
 */
public interface OfficailRemindView extends BaseView {
    /**
     * 显示数据为空
     */
    void showEmptyView();

    /**
     * 显示任务List
     */
    void showRemindList(OfficeNoticeInfo officeNoticeInfo,int action);

    /**
     * 加载更多失败
     */
    void onLoadMoreError();

    /**
     * 刷新失败
     */
    void onRefreshError();

    /**
     * 无更多加载信息
     */
    void onLoadMoreEmpty();

    void setReadSuccessful(int id,int position);
}
