package com.gta.zssx.mobileOA.model.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by xiaoxia.rang on 2016/11/19.
 */

public class UserDutyListInfo implements Serializable {

    List<DutyNoticeInfo>  DutyList;

    public List<DutyNoticeInfo> getDutyList() {
        return DutyList;
    }

    public void setDutyList(List<DutyNoticeInfo> dutyList) {
        DutyList = dutyList;
    }
}
