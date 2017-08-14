package com.gta.zssx.patrolclass.model.entity;

/**
 * Created by liang.lu on 2016/8/23 14:05.
 */
public class PlanClassSearchEntity {
    private int Id;
    private String ClassName;
    private String deptId;

    public int getId () {
        return Id;
    }

    public void setId (int id) {
        Id = id;
    }

    public String getClassName () {
        return ClassName;
    }

    public void setClassName (String ClassName) {
        this.ClassName = ClassName;
    }

    public String getDeptId () {
        return deptId;
    }

    public void setDeptId (String deptId) {
        this.deptId = deptId;
    }
}
