package com.gta.zssx.patrolclass.model.entity;

import java.io.Serializable;

/**
 * Created by liang.lu on 2016/8/22 17:29.
 */
public class AddClassEntity implements Serializable {

    /**
     * ClassID : 1
     * TeacherID : 11
     */

    private int ClassID;
    private String TeacherID;

    public int getClassID () {
        return ClassID;
    }

    public void setClassID (int ClassID) {
        this.ClassID = ClassID;
    }

    public String getTeacherID () {
        return TeacherID;
    }

    public void setTeacherID (String TeacherID) {
        this.TeacherID = TeacherID;
    }
}
