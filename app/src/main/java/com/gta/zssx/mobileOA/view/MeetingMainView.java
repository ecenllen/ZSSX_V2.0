package com.gta.zssx.mobileOA.view;

import com.gta.zssx.mobileOA.model.bean.Meeting;

import java.util.List;

/**
 * Created by xiaoxia.rang on 2016/10/26.
 */

public interface MeetingMainView extends BaseOAView {

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
    void refreshMeetingList(List<Meeting> meetingList);


    void appendMeetingList(List<Meeting> meetingList);

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
