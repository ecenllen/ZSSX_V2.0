package com.gta.zssx.mobileOA.model.bean;

import java.io.Serializable;

/**
 * Created by xiaoxia.rang on 2016/11/7.
 * 任务安排
 */

public class TaskArrange implements Serializable {

    /**
     * 事项名
     */
    private String item;
    /**
     * 计划时间
     */
    private String planTime;
    /**
     * 人员
     */
    private String person;
    /**
     * 人员id
     */
    private String personId;
    /**
     * 部门
     */
    private String dept;
    /**
     * 部门id
     */
    private String deptId;

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getPlanTime() {
        return planTime;
    }

    public void setPlanTime(String planTime) {
        this.planTime = planTime;
    }

    public String getPerson() {
        return person;
    }

    public void setPerson(String person) {
        this.person = person;
    }

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public String getDept() {
        return dept;
    }

    public void setDept(String dept) {
        this.dept = dept;
    }

    public String getDeptId() {
        return deptId;
    }

    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }
}
