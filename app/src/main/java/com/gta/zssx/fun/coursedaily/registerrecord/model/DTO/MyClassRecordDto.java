package com.gta.zssx.fun.coursedaily.registerrecord.model.DTO;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lan.zheng on 2016/6/30.
 */
public class MyClassRecordDto implements Serializable{
    private int ClassID;
    private String ClassName;
    private String Date;
    private String Week;
    private List<RegisteredRecordFromSignatureDto> Course;

    public int getClassID() {
        return ClassID;
    }

    public void setClassID(int ClassID) {
        this.ClassID = ClassID;
    }

    public String getClassName() {
        return ClassName;
    }

    public void setClassName(String ClassName) {
        this.ClassName = ClassName;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String Date) {
        this.Date = Date;
    }

    public String getWeek() {
        return Week;
    }

    public void setWeek(String Week) {
        this.Week = Week;
    }

    public List<RegisteredRecordFromSignatureDto> getCourse(){
        return Course;
    }

    public void setCourse(List<RegisteredRecordFromSignatureDto> Course){
        this.Course = Course;
    }

}
