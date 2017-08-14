package com.gta.zssx.mobileOA.model.bean;

import java.io.Serializable;

/**
 * Created by lan.zheng on 2016/11/25.
 */
public class DeleteSchuduleInfo implements Serializable{
    private int id;
    private int status;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
