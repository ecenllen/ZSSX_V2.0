package com.gta.zssx.fun.coursedaily.registercourse.model.bean;

import java.io.Serializable;

/**
 * [Description]
 * <p>
 * [How to use]
 * <p>
 * [Tips]
 *
 * @author Created by Zhimin.Huang on 2016/6/18.
 * @since 1.0.0
 */
public class ClassDisplayBean implements Serializable{


    /**
     * ClassName : “13会计1”
     * DeptName : 财经系
     * TeacherName : “李小明”
     * StudentCount : 35
     */

    private int ClassId;
    private String ClassName;
    private String DeptName;
    private String TeacherName;
    private int StudentCount;

    public int getClassId() {
        return ClassId;
    }

    public void setClassId(int classId) {
        ClassId = classId;
    }

    public String getClassName() {
        return ClassName;
    }

    public void setClassName(String ClassName) {
        this.ClassName = ClassName;
    }

    public String getDeptName() {
        return DeptName;
    }

    public void setDeptName(String DeptName) {
        this.DeptName = DeptName;
    }

    public String getTeacherName() {
        return TeacherName;
    }

    public void setTeacherName(String TeacherName) {
        this.TeacherName = TeacherName;
    }

    public int getStudentCount() {
        return StudentCount;
    }

    public void setStudentCount(int StudentCount) {
        this.StudentCount = StudentCount;
    }
}
