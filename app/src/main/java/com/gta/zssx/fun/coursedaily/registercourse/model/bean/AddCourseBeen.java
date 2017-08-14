package com.gta.zssx.fun.coursedaily.registercourse.model.bean;

import java.io.Serializable;

/**
 * Created by lan.zheng on 2017/3/13.
 */
public class AddCourseBeen implements Serializable {


    /**
     * courseName : 123
     * creatorId : bdf3e816-bb5b-4539-a163-fc35ffc27032
     */

    private String courseName ;
    private String creatorId;

    public String getCourseName() {
            return courseName ;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCreatorId() {
        return creatorId ;
    }

    public void setCreatorId(String creatorId ) {
        this.creatorId  = creatorId ;
    }

}

