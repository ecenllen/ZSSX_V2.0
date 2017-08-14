package com.gta.zssx.fun.assetmanagement.model.bean;

/**
 * Created by liang.lu on 2016/11/30 13:54.
 */

public class BatchAddNotesBean {
    private int CheckId;
    private String Remarks;
    private int AssetCheckModuleType;
    private String UserId;

    public int getCheckId () {
        return CheckId;
    }

    public void setCheckId (int checkId) {
        CheckId = checkId;
    }

    public String getRemarks () {
        return Remarks;
    }

    public void setRemarks (String remarks) {
        Remarks = remarks;
    }

    public int getAssetCheckModuleType () {
        return AssetCheckModuleType;
    }

    public void setAssetCheckModuleType (int assetCheckModuleType) {
        AssetCheckModuleType = assetCheckModuleType;
    }

    public String getUserId () {
        return UserId;
    }

    public void setUserId (String userId) {
        UserId = userId;
    }
}
