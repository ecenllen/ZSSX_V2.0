package com.gta.zssx.patrolclass.model.entity;

import java.io.Serializable;

/**
 * [Description]
 * <p/>  按计划巡课结果 实体
 * [How to use]
 * <p>
 * [Tips] Created by tengfei.lv on 2016/7/14 17:20.
 */

public class PlanPatrolResultEntity implements Serializable {
    /**
     * classId : 1
     * deptId : 1
     * className : 11会计
     */

    private int classId;
    private int deptId;
    private String className;

    public int getClassId () {
        return classId;
    }

    public void setClassId (int classId) {
        this.classId = classId;
    }

    public int getDeptId () {
        return deptId;
    }

    public void setDeptId (int deptId) {
        this.deptId = deptId;
    }

    public String getClassName () {
        return className;
    }

    public void setClassName (String className) {
        this.className = className;
    }
}
