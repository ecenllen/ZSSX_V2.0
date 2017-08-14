package com.gta.zssx.fun.coursedaily.registerrecord.model.DTO;

import java.io.Serializable;

/**
 * Created by lan.zheng on 2016/6/29.
 * 用于已登记页面和签名确认页面
 */
public class SaveCacheDto implements Serializable {

    private String BeginDate;
    private String EndDate;
    private String SignDate;
    private boolean SaveRecord;
    private boolean SaveRecordFromSignature;
    private String ShowDate;  //用于我的班级--单班级的情况

    public String getBeginDate() {
        return BeginDate;
    }

    public void setBeginDate(String BeginDate) {
        this.BeginDate = BeginDate;
    }

    public String getEndDate() {
        return EndDate;
    }

    public void setEndDate(String EndDate) {
        this.EndDate = EndDate;
    }

    public String getSignDate() {
        return SignDate;
    }

    public void setSignDate(String SignDate) {
        this.SignDate = SignDate;
    }

    public boolean getSaveRecord() {
        return SaveRecord;
    }

    public void setSaveRecord(boolean SaveRecord) {
        this.SaveRecord = SaveRecord;
    }

    public boolean getSaveRecordFromSignature() {
        return SaveRecordFromSignature;
    }

    public void setSaveRecordFromSignature(boolean SaveRecordFromSignature) {
        this.SaveRecordFromSignature = SaveRecordFromSignature;
    }

    public String getShowDate() {
        return ShowDate;
    }

    public void setShowDate(String ShowDate) {
        this.ShowDate = ShowDate;
    }

}
