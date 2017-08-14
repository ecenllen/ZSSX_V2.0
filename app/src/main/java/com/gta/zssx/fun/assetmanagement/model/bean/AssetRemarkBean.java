package com.gta.zssx.fun.assetmanagement.model.bean;

/**
 * Created by liang.lu on 2016/11/30 10:55.
 */

public class AssetRemarkBean {

    private int systemCount;
    private int currentCount;
    private int checkId;
    private int assetId;
    private String assetType;
    private int AssetCheckModuleType;

    public int getAssetCheckModuleType () {
        return AssetCheckModuleType;
    }

    public void setAssetCheckModuleType (int assetCheckModuleType) {
        AssetCheckModuleType = assetCheckModuleType;
    }

    public String getAssetType () {
        return assetType;
    }

    public void setAssetType (String assetType) {
        this.assetType = assetType;
    }

    public int getSystemCount () {
        return systemCount;
    }

    public void setSystemCount (int systemCount) {
        this.systemCount = systemCount;
    }

    public int getCurrentCount () {
        return currentCount;
    }

    public void setCurrentCount (int currentCount) {
        this.currentCount = currentCount;
    }

    public int getCheckId () {
        return checkId;
    }

    public void setCheckId (int checkId) {
        this.checkId = checkId;
    }

    public int getAssetId () {
        return assetId;
    }

    public void setAssetId (int assetId) {
        this.assetId = assetId;
    }
}
