package com.gta.zssx.fun.adjustCourse.model.bean;

import java.io.Serializable;

/**
 * [Description]
 * <p>
 * [How to use]
 * <p>
 * [Tips]
 *
 * @author Created by Zhimin.Huang on 2016/11/2.
 * @since 1.0.0
 */
public class GetTeacherBean implements Serializable {
    private String date;
    private int numberTeacher;
    private String applyTechBId;
    private int applyTechId;
    private int applyClassId;
    private String selectUnitApply;
    //调课或者代课“D”or“T”
    private String applyType;
    //deptId
    private int deptId;

    public int getDeptId() {
        return deptId;
    }

    public void setDeptId(int deptId) {
        this.deptId = deptId;
    }

    public int getApplyTechId() {
        return applyTechId;
    }

    public void setApplyTechId(int applyTechId) {
        this.applyTechId = applyTechId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getNumberTeacher() {
        return numberTeacher;
    }

    public void setNumberTeacher(int numberTeacher) {
        this.numberTeacher = numberTeacher;
    }

    public String getApplyTechBId() {
        return applyTechBId;
    }

    public void setApplyTechBId(String applyTechBId) {
        this.applyTechBId = applyTechBId;
    }

    public int getApplyClassId() {
        return applyClassId;
    }

    public void setApplyClassId(int applyClassId) {
        this.applyClassId = applyClassId;
    }

    public String getSelectUnitApply() {
        return selectUnitApply;
    }

    public void setSelectUnitApply(String selectUnitApply) {
        this.selectUnitApply = selectUnitApply;
    }

    public String getApplyType() {
        return applyType;
    }

    public void setApplyType(String applyType) {
        this.applyType = applyType;
    }
}
