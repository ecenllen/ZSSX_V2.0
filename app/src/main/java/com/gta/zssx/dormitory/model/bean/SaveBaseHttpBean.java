package com.gta.zssx.dormitory.model.bean;

import java.io.Serializable;

/**
 * Created by weiye.chen on 2017/7/13.
 */

public class SaveBaseHttpBean implements Serializable {
    private int ItemId;
    private String InputDate;
    private int RecordId;
    private String UserId;
    private String BuildingOrDeptIds;

    public int getItemId() {
        return ItemId;
    }

    public void setItemId(int itemId) {
        ItemId = itemId;
    }

    public String getInputDate() {
        return InputDate;
    }

    public void setInputDate(String inputDate) {
        InputDate = inputDate;
    }

    public int getRecordId() {
        return RecordId;
    }

    public void setRecordId(int recordId) {
        RecordId = recordId;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getBuildingOrDeptIds() {
        return BuildingOrDeptIds;
    }

    public void setBuildingOrDeptIds(String buildingOrDeptIds) {
        BuildingOrDeptIds = buildingOrDeptIds;
    }
}
