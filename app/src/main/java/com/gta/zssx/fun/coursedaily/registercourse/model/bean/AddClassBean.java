package com.gta.zssx.fun.coursedaily.registercourse.model.bean;

import java.io.Serializable;

/**
 * [Description]
 * <p/>
 * [How to use]
 * <p/>
 * [Tips]
 *
 * @author Created by Zhimin.Huang on 2016/6/21.
 * @since 1.0.0
 */
public class AddClassBean implements Serializable{


    /**
     * ClassID : 123
     * TeacherID : bdf3e816-bb5b-4539-a163-fc35ffc27032
     */

    private int ClassID;
    private String TeacherID;

    public int getClassID() {
        return ClassID;
    }

    public void setClassID(int ClassID) {
        this.ClassID = ClassID;
    }

    public String getTeacherID() {
        return TeacherID;
    }

    public void setTeacherID(String TeacherID) {
        this.TeacherID = TeacherID;
    }

}
