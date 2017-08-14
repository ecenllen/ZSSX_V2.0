package com.gta.zssx.fun.assetmanagement.model.bean;

/**
 * Created by liang.lu on 2016/11/10 16:26.
 */

public class AssetBean {
    private int AssetId;//资产Id
    private String AssetCode;//资产编号
    private String AssetName;//资产名称
    private String Remarks;//备注信息
    private int IsRepeatScan;//是否重复扫描
    private int IsHasRemarks;//是否已有备注
    private int SystemNumber;//系统记录数量
    private int CurrentNumber;//目前已盘点数量
    private String AssetType; // 资产型号

    public String getAssetType() {
        return AssetType;
    }

    public void setAssetType(String assetType) {
        AssetType = assetType;
    }

    public int getSystemNumber () {
        return SystemNumber;
    }

    public void setSystemNumber (int systemNumber) {
        SystemNumber = systemNumber;
    }

    public int getCurrentNumber () {
        return CurrentNumber;
    }

    public void setCurrentNumber (int currentNumber) {
        CurrentNumber = currentNumber;
    }

    public String getRemarks () {
        return Remarks;
    }

    public void setRemarks (String remarks) {
        Remarks = remarks;
    }

    public int getIsRepeatScan () {
        return IsRepeatScan;
    }

    public void setIsRepeatScan (int isRepeatScan) {
        IsRepeatScan = isRepeatScan;
    }

    public int getIsHasRemarks () {
        return IsHasRemarks;
    }

    public void setIsHasRemarks (int isHasRemarks) {
        IsHasRemarks = isHasRemarks;
    }

    public int getAssetId () {
        return AssetId;
    }

    public void setAssetId (int assetId) {
        AssetId = assetId;
    }

    public String getAssetCode () {
        return AssetCode;
    }

    public void setAssetCode (String assetCode) {
        AssetCode = assetCode;
    }

    public String getAssetName () {
        return AssetName;
    }

    public void setAssetName (String assetName) {
        AssetName = assetName;
    }
}
