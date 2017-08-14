package com.gta.zssx.fun.adjustCourse.model.bean;

import java.io.Serializable;

/**
 * [Description]
 * <p>
 * [How to use]
 * <p>
 * [Tips]
 *
 * @author Created by Zhimin.Huang on 2016/11/7.
 * @since 1.0.0
 */
public class ReplaceCourseBean implements Serializable {

    //代课提交信息

    /**
     * semesterCurrent : 20161
     * applyTechId : f5353946-809e-43ec-a312-f4850ac6ed69
     * adjustTechId : 13
     * applyType : D
     * applyDate : 2016-10-25
     * applyPolysyllabicWordStr : 3,4
     * applyClassId : 113
     * applyCourseId : 109
     * userId : f5353946-809e-43ec-a312-f4850ac6ed69
     * serialWeekNum : 2
     * applyCause : test
     */

    //当前学期
    private String semesterCurrent;
    //申请教师id
    private String applyTechId;
    //被代课教师id
    private String adjustTechId;
    //申请类型D
    private String applyType;
    //申请日期
    private String applyDate;
    //代课日期
    private String adjustDate;
    //申请课程，用,号分隔
    private String applyPolysyllabicWordStr;
    //申请班级id
    private String applyClassId;
    //申请课程id
    private String applyCourseId;
    //用户id
    private String userId;
    //代课星期数量
    private int serialWeekNum;
    //备注
    private String applyCause;

    private int applyNumberTeacher;
    private int applyNumberClass;
    private int applyNumberCourse;
    private int applyOpenCourseType;

    public int getApplyNumberTeacher() {
        return applyNumberTeacher;
    }

    public void setApplyNumberTeacher(int applyNumberTeacher) {
        this.applyNumberTeacher = applyNumberTeacher;
    }

    public int getApplyNumberClass() {
        return applyNumberClass;
    }

    public void setApplyNumberClass(int applyNumberClass) {
        this.applyNumberClass = applyNumberClass;
    }

    public int getApplyNumberCourse() {
        return applyNumberCourse;
    }

    public void setApplyNumberCourse(int applyNumberCourse) {
        this.applyNumberCourse = applyNumberCourse;
    }

    public int getApplyOpenCourseType() {
        return applyOpenCourseType;
    }

    public void setApplyOpenCourseType(int applyOpenCourseType) {
        this.applyOpenCourseType = applyOpenCourseType;
    }

    public String getAdjustDate() {
        return adjustDate;
    }

    public void setAdjustDate(String adjustDate) {
        this.adjustDate = adjustDate;
    }

    public String getSemesterCurrent() {
        return semesterCurrent;
    }

    public void setSemesterCurrent(String semesterCurrent) {
        this.semesterCurrent = semesterCurrent;
    }

    public String getApplyTechId() {
        return applyTechId;
    }

    public void setApplyTechId(String applyTechId) {
        this.applyTechId = applyTechId;
    }

    public String getAdjustTechId() {
        return adjustTechId;
    }

    public void setAdjustTechId(String adjustTechId) {
        this.adjustTechId = adjustTechId;
    }

    public String getApplyType() {
        return applyType;
    }

    public void setApplyType(String applyType) {
        this.applyType = applyType;
    }

    public String getApplyDate() {
        return applyDate;
    }

    public void setApplyDate(String applyDate) {
        this.applyDate = applyDate;
    }

    public String getApplyPolysyllabicWordStr() {
        return applyPolysyllabicWordStr;
    }

    public void setApplyPolysyllabicWordStr(String applyPolysyllabicWordStr) {
        this.applyPolysyllabicWordStr = applyPolysyllabicWordStr;
    }

    public String getApplyClassId() {
        return applyClassId;
    }

    public void setApplyClassId(String applyClassId) {
        this.applyClassId = applyClassId;
    }

    public String getApplyCourseId() {
        return applyCourseId;
    }

    public void setApplyCourseId(String applyCourseId) {
        this.applyCourseId = applyCourseId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getSerialWeekNum() {
        return serialWeekNum;
    }

    public void setSerialWeekNum(int serialWeekNum) {
        this.serialWeekNum = serialWeekNum;
    }

    public String getApplyCause() {
        return applyCause;
    }

    public void setApplyCause(String applyCause) {
        this.applyCause = applyCause;
    }
}
