package com.gta.zssx.mobileOA.model.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by xiaoxia.rang on 2016/11/21.
 */

public class MyApplyInfo implements Serializable {
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
    private List<MyApplyBean> Backlogs;

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

    public List<MyApplyBean> getBacklogs() {
        return Backlogs;
    }

    public void setBacklogs(List<MyApplyBean> backlogs) {
        Backlogs = backlogs;
    }
}
