package com.gta.zssx.mobileOA.model.bean;

import com.gta.zssx.patrolclass.model.entity.ChooseTeacherEntity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by xiaoxia.rang on 2016/11/21.
 * 部门或者
 */

public class TeacherOrDeptEntity implements Serializable {

    private int orgId;
    private int parentId;
    private String name;
    private List<ChooseTeacherEntity.DeptListBean> deptList;

    public int getOrgId() {
        return orgId;
    }

    public void setOrgId(int orgId) {
        this.orgId = orgId;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ChooseTeacherEntity.DeptListBean> getDeptList() {
        return deptList;
    }

    public void setDeptList(List<ChooseTeacherEntity.DeptListBean> deptList) {
        this.deptList = deptList;
    }

    public class DeptListBean{
        private String deptId;
        private String deptName;
        private String teacherCode;
        private int type;

        public String getDeptId () {
            return deptId;
        }

        public void setDeptId (String deptId) {
            this.deptId = deptId;
        }

        public String getDeptName () {
            return deptName;
        }

        public void setDeptName (String deptName) {
            this.deptName = deptName;
        }

        public String getTeacherCode () {
            return teacherCode;
        }

        public void setTeacherCode (String teacherCode) {
            this.teacherCode = teacherCode;
        }

        public int getType () {
            return type;
        }

        public void setType (int type) {
            this.type = type;
        }
    }

}
