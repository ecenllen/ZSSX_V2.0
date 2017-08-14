package com.gta.zssx.patrolclass.model.entity;

import java.io.Serializable;

/**
 * Created by liang.lu on 2016/8/17 14:52.
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
