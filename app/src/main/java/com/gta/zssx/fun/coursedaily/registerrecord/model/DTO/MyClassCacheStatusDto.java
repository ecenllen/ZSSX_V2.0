package com.gta.zssx.fun.coursedaily.registerrecord.model.DTO;

import java.io.Serializable;

/**
 * Created by lan.zheng on 2016/7/4.
 * 用于“我的班级”多页面的情况
 */
public class MyClassCacheStatusDto implements Serializable {
    private int ClassID;
    private int Status;

    public int getClassID() {
        return ClassID;
    }

    public void setClassID(int ClassID) {
        this.ClassID = ClassID;
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int Status) {
        this.Status = Status;
    }


}
