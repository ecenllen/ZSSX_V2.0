package com.gta.zssx.mobileOA.model.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by xiaoxia.rang on 2016/11/16.
 * 待办/已办/我的事项 请求返回
 */

public class BacklogInfo implements Serializable {
    /**
     * 总页数
     */
    private int TotalCount;
    /**
     * 服务器时间
     */
    private String ServerTime;

    /**
     * 事项列表
     */
    private List<Backlog> Backlogs;

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

    public List<Backlog> getBacklogs() {
        return Backlogs;
    }

    public void setBacklogs(List<Backlog> backlogs) {
        Backlogs = backlogs;
    }
}
