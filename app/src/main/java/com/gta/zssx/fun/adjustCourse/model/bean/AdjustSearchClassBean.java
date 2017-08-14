package com.gta.zssx.fun.adjustCourse.model.bean;

import java.io.Serializable;

/**
 * [Description]
 * <p>
 * [How to use]
 * <p>
 * [Tips]
 *
 * @author Created by Zhimin.Huang on 2016/11/10.
 * @since 1.0.0
 */
public class AdjustSearchClassBean implements Serializable {

    /**
     * id : 1
     * className : 13服5
     * status : 1
     * teacherName : 何绮环
     * grade :
     * deptName :
     * deptId :
     * majorId :
     * stuCount : 46
     * studentNum :
     * majorName :
     */

    private int id;
    private String className;
    private int status;
    private String teacherName;
    private String grade;
    private String deptName;
    private String deptId;
    private String majorId;
    private int stuCount;
    private String studentNum;
    private String majorName;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getDeptId() {
        return deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    public String getMajorId() {
        return majorId;
    }

    public void setMajorId(String majorId) {
        this.majorId = majorId;
    }

    public int getStuCount() {
        return stuCount;
    }

    public void setStuCount(int stuCount) {
        this.stuCount = stuCount;
    }

    public String getStudentNum() {
        return studentNum;
    }

    public void setStudentNum(String studentNum) {
        this.studentNum = studentNum;
    }

    public String getMajorName() {
        return majorName;
    }

    public void setMajorName(String majorName) {
        this.majorName = majorName;
    }
}
