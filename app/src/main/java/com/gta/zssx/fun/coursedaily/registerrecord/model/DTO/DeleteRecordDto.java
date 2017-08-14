package com.gta.zssx.fun.coursedaily.registerrecord.model.DTO;

import java.io.Serializable;

/**
 * Created by lan.zheng on 2016/6/25.
 */
public class DeleteRecordDto implements Serializable {

    /**
     * ClassID : 123
     * SignDate : 2016-06-21
     * SectionID : 1
     */

    private int ClassID;
    private String SignDate;
    private int SectionID;

    public int getClassID() {
        return ClassID;
    }

    public void setClassID(int ClassID) {
        this.ClassID = ClassID;
    }

    public String getSignDate() {
        return SignDate;
    }

    public void setSignDate(String SignDate) {
        this.SignDate = SignDate;
    }

    public int getSectionID() {
        return SectionID;
    }

    public void setSectionID(int SectionID) {
        this.SectionID = SectionID;
    }
}
