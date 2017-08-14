package com.gta.zssx.dormitory.model.bean;

import java.io.Serializable;

/**
 * Created by lan.zheng on 2017/6/28.
 * 班级bean
 */

public class ClassInfoBean implements Serializable{
    private String ClassName;
    private int ClassId;
    private String ClassSingleScore;
    private String ClassSingleRemark;

    public String getClassName() {
        return ClassName;
    }

    public void setClassName(String ClassName) {
        this.ClassName = ClassName;
    }

    public int getClassId() {
        return ClassId;
    }

    public void setClassId(int ClassId) {
        this.ClassId = ClassId;
    }

    public String getClassSingleScore() {
        return ClassSingleScore;
    }

    public void setClassSingleScore(String ClassSingleScore) {
        this.ClassSingleScore = ClassSingleScore;
    }

    public String getClassSingleRemark() {
        return ClassSingleRemark;
    }

    public void setClassSingleRemark(String ClassSingleRemark) {
        this.ClassSingleRemark = ClassSingleRemark;
    }

}
