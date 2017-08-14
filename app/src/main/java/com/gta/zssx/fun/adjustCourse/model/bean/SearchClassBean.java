package com.gta.zssx.fun.adjustCourse.model.bean;

import java.io.Serializable;

/**
 * [Description]
 * <p/>
 * [How to use]
 * <p/>
 * [Tips]
 *
 * @author Created by Zhimin.Huang on 2016/6/17.
 * @since 1.0.0
 */
public class SearchClassBean implements Serializable {

    /**
     * Id : 1
     * ClassName : 13服5
     * Grade : 1
     */

    private int Id;
    private String ClassName;
    private int Grade;

    public int getId() {
        return Id;
    }

    public void setId(int Id) {
        this.Id = Id;
    }

    public String getClassName() {
        return ClassName;
    }

    public void setClassName(String ClassName) {
        this.ClassName = ClassName;
    }

    public int getGrade() {
        return Grade;
    }

    public void setGrade(int Grade) {
        this.Grade = Grade;
    }
}
