package com.gta.zssx.patrolclass.model.entity;

import java.io.Serializable;

/**
 * Created by liang.lu on 2016/8/8 13:30.
 */
public class DeleteItemEntity implements Serializable {
    private String uId;
    private int xId;
    private int pId;

    public String getuId () {
        return uId;
    }

    public void setuId (String uId) {
        this.uId = uId;
    }

    public int getxId () {
        return xId;
    }

    public void setxId (int xId) {
        this.xId = xId;
    }

    public int getpId () {
        return pId;
    }

    public void setpId (int pId) {
        this.pId = pId;
    }
}
