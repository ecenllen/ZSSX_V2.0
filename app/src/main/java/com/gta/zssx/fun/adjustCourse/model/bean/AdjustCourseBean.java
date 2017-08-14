package com.gta.zssx.fun.adjustCourse.model.bean;

import java.io.Serializable;

/**
 * [Description]
 * <p>
 * [How to use]
 * <p>
 * [Tips]
 *
 * @author Created by Zhimin.Huang on 2016/11/8.
 * @since 1.0.0
 */
public class AdjustCourseBean implements Serializable {

    /**
     * semesterCurrent : 20161
     * applyTechId : f5353946-809e-43ec-a312-f4850ac6ed69
     * adjustTechId : 13
     * applyType : T
     * applyDate : 2016-10-25
     * adjustDate : 2016-10-25
     * applyPolysyllabicWordStr : 3,4
     * adjustPolysyllabicWordStr : 5,6
     * applyClassId : 113
     * applyCourseId : 109
     * adjustClassId : 113
     * adjustCourseId : 106
     * userId : f5353946-809e-43ec-a312-f4850ac6ed69
     * applyCause : test
     */

    private String semesterCurrent;
    private String applyTechId;
    private String adjustTechId;
    private String applyType;
    private String applyDate;
    private String adjustDate;
    private String applyPolysyllabicWordStr;
    private String adjustPolysyllabicWordStr;
    private String applyClassId;
    private String applyCourseId;
    private String adjustClassId;
    private String adjustCourseId;
    private String userId;
    private String applyCause;
    private int applyNumberTeacher;
    private int applyNumberClass;
    private int applyNumberCourse;
    private int applyOpenCourseType;

    private int adjustNumberTeacher;
    private int adjustNumberClass;
    private int adjustNumberCourse;
    private int adjustOpenCourseType;

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

    public int getAdjustNumberTeacher() {
        return adjustNumberTeacher;
    }

    public void setAdjustNumberTeacher(int adjustNumberTeacher) {
        this.adjustNumberTeacher = adjustNumberTeacher;
    }

    public int getAdjustNumberClass() {
        return adjustNumberClass;
    }

    public void setAdjustNumberClass(int adjustNumberClass) {
        this.adjustNumberClass = adjustNumberClass;
    }

    public int getAdjustNumberCourse() {
        return adjustNumberCourse;
    }

    public void setAdjustNumberCourse(int adjustNumberCourse) {
        this.adjustNumberCourse = adjustNumberCourse;
    }

    public int getAdjustOpenCourseType() {
        return adjustOpenCourseType;
    }

    public void setAdjustOpenCourseType(int adjustOpenCourseType) {
        this.adjustOpenCourseType = adjustOpenCourseType;
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

    public String getAdjustDate() {
        return adjustDate;
    }

    public void setAdjustDate(String adjustDate) {
        this.adjustDate = adjustDate;
    }

    public String getApplyPolysyllabicWordStr() {
        return applyPolysyllabicWordStr;
    }

    public void setApplyPolysyllabicWordStr(String applyPolysyllabicWordStr) {
        this.applyPolysyllabicWordStr = applyPolysyllabicWordStr;
    }

    public String getAdjustPolysyllabicWordStr() {
        return adjustPolysyllabicWordStr;
    }

    public void setAdjustPolysyllabicWordStr(String adjustPolysyllabicWordStr) {
        this.adjustPolysyllabicWordStr = adjustPolysyllabicWordStr;
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

    public String getAdjustClassId() {
        return adjustClassId;
    }

    public void setAdjustClassId(String adjustClassId) {
        this.adjustClassId = adjustClassId;
    }

    public String getAdjustCourseId() {
        return adjustCourseId;
    }

    public void setAdjustCourseId(String adjustCourseId) {
        this.adjustCourseId = adjustCourseId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getApplyCause() {
        return applyCause;
    }

    public void setApplyCause(String applyCause) {
        this.applyCause = applyCause;
    }


}
