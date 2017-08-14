package com.gta.zssx.mobileOA.model.bean;

import java.io.Serializable;

/**
 * Created by lan.zheng on 2016/11/14.
 */
public class DutyDateInfo implements Serializable {
    private String Date; //时间
    private int Status;  //1:未登记，未检查,2：已提交、未提交

    public String getDate() {
        return Date;
    }

    public void setDate(String Date) {
        this.Date = Date;
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int Status) {
        this.Status = Status;
    }
}
