package com.gta.zssx.mobileOA.model.bean;

import java.io.Serializable;

/**
 * Created by lan.zheng on 2016/11/18.
 */
public class DutyTimeInfo implements Serializable{
    private int TimesId;
    private String Time;
    private int Status;

    public int getTimesId() {
        return TimesId;
    }

    public void setTimesId(int TimesId) {
        this.TimesId = TimesId;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String Time) {
        this.Time = Time;
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int Status) {
        this.Status = Status;
    }

}
