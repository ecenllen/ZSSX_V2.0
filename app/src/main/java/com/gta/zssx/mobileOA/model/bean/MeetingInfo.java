package com.gta.zssx.mobileOA.model.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by xiaoxia.rang on 2016/11/16.
 * 会议列表返回
 */

public class MeetingInfo implements Serializable {
    /**
     * 页码
     */
    private int TotalCount;
    /**
     * 服务器时间
     */
    private String ServerTime;
    /**
     * 会议列表
     */
    private List<Meeting> Meetings;


    public int getTotalCount() {
        return TotalCount;
    }

    public void setTotalCount(int totalCount) {
        TotalCount = totalCount;
    }

    public String getServerTime() {
        return ServerTime;
    }

    public void setServerTime(String serverTime) {
        ServerTime = serverTime;
    }

    public List<Meeting> getMeetings() {
        return Meetings;
    }

    public void setMeetings(List<Meeting> meetings) {
        Meetings = meetings;
    }
}
