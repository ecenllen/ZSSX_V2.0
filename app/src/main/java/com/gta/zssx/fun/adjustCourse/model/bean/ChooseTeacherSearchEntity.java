package com.gta.zssx.fun.adjustCourse.model.bean;

import java.io.Serializable;

/**
 * [Description]
 *
 * [How to use]
 *
 * [Tips]
 * @author
 *   Created by Zhimin.Huang on 2016/8/17 14:52.
 * @since
 *   1.0.0
 */
public class ChooseTeacherSearchEntity implements Serializable{

    /**
     * name : 张三
     * teacherId : 1
     * teacherCode : T2001
     */

    private String name;
    private String teacherId;
    private String teacherCode;
    private String teacherBId;

    public String getTeacherBId() {
        return teacherBId;
    }

    public void setTeacherBId(String teacherBId) {
        this.teacherBId = teacherBId;
    }

    public String getName () {
        return name;
    }

    public void setName (String name) {
        this.name = name;
    }

    public String getTeacherId () {
        return teacherId;
    }

    public void setTeacherId (String teacherId) {
        this.teacherId = teacherId;
    }

    public String getTeacherCode () {
        return teacherCode;
    }

    public void setTeacherCode (String teacherCode) {
        this.teacherCode = teacherCode;
    }
}
