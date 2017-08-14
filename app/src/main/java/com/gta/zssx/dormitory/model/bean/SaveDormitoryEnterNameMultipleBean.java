package com.gta.zssx.dormitory.model.bean;

import java.io.Serializable;

/**
 * Created by lan.zheng on 2017/7/13.
 * 保存信息 - 录入姓名多项
 */

public class SaveDormitoryEnterNameMultipleBean<T> implements Serializable{
    private String InputDate;
    private String UserId;
    private int ItemId;
    private int RecordId;
    private String BuildingOrDeptIds;
    private T DetailContent;

    public String getInputDate() {
        return InputDate;
    }

    public void setInputDate(String InputDate) {
        this.InputDate = InputDate;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String UserId) {
        this.UserId = UserId;
    }

    public int getRecordId() {
        return RecordId;
    }

    public void setRecordId(int RecordId) {
        this.RecordId = RecordId;
    }

    public int getItemId() {
        return ItemId;
    }

    public void setItemId(int ItemId) {
        this.ItemId = ItemId;
    }

    public String getBuildingOrDeptIds() {
        return BuildingOrDeptIds;
    }

    public void setBuildingOrDeptIds(String BuildingOrDeptIds) {
        this.BuildingOrDeptIds = BuildingOrDeptIds;
    }

    public void setDetailContent(T DetailContent) {
        this.DetailContent = DetailContent;
    }

    public T getDetailContent(){
        return DetailContent;
    }

//    static final int DORMITORY = 0;
//    static final int CLASS = 1;
}
