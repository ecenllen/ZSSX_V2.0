package com.gta.zssx.fun.assetmanagement.model.bean;

import java.io.Serializable;

/**
 * Created by liang.lu on 2016/11/17 14:34.
 */

public class UpLoadRemarksAssetBean implements Serializable {
    private String userId;//用户ID
    private int assetId;//资产ID
    private String remarks;//备注信息
    private int isRecord;//是否要记录  1是  2否
    private int checkId;

    public String getUserId () {
        return userId;
    }

    public void setUserId (String userId) {
        this.userId = userId;
    }

    public int getAssetId () {
        return assetId;
    }

    public void setAssetId (int assetId) {
        this.assetId = assetId;
    }

    public String getRemarks () {
        return remarks;
    }

    public void setRemarks (String remarks) {
        this.remarks = remarks;
    }

    public int getIsRecord () {
        return isRecord;
    }

    public void setIsRecord (int isRecord) {
        this.isRecord = isRecord;
    }

    public int getCheckId () {
        return checkId;
    }

    public void setCheckId (int checkId) {
        this.checkId = checkId;
    }
}
