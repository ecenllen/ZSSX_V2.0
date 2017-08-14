package com.gta.zssx.fun.coursedaily.registercourse.model.bean;

import java.io.Serializable;
import java.util.List;

/**
 * [Description]
 * <p>
 * [How to use]
 * <p>
 * [Tips]
 *
 * @author Created by Zhimin.Huang on 2016/6/28.
 * @since 1.0.0
 */
public class ClassChooseBean01 implements Serializable {

    /**
     * DeptId : 5
     * DeptCode : 服
     * DeptName : 服装部
     * classList : [{"Id":124,"ClassName":"服装设计1班16级"}]
     */

    private int DeptId;
    private String DeptName;
    private String DeptCode;
    /**
     * Id : 124
     * ClassName : 服装设计1班16级
     */

    private List<ClassListBean> classList;

    public int getDeptId() {
        return DeptId;
    }

    public void setDeptId(int DeptId) {
        this.DeptId = DeptId;
    }

    public String getDeptCode() {
        return DeptCode;
    }

    public void setDeptCode(String DeptCode) {
        this.DeptCode = DeptCode;
    }

    public String getDeptName() {
        return DeptName;
    }

    public void setDeptName(String DeptName) {
        this.DeptName = DeptName;
    }

    public List<ClassListBean> getClassList() {
        return classList;
    }

    public void setClassList(List<ClassListBean> classList) {
        this.classList = classList;
    }

    public static class ClassListBean {
        private int Id;
        private String ClassName;
        private int DeptId;
        private String DeptName;
        private String DeptCode;
        private boolean isCheck;
        private String Year;

        //根据后台返回顺序分配的值增长id；
        private int sortId;

        public int getSortId() {
            return sortId;
        }

        public void setSortId(int sortId) {
            this.sortId = sortId;
        }

        public String getYear() {
            return Year;
        }

        public void setYear(String year) {
            Year = year;
        }

        //记录头部位置
        private int headerPosition;

        //记录某部的成员数量
        private int size;

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }

        public int getHeaderPosition() {
            return headerPosition;
        }

        public void setHeaderPosition(int headerPosition) {
            this.headerPosition = headerPosition;
        }

        public boolean isCheck() {
            return isCheck;
        }

        public void setCheck(boolean check) {
            isCheck = check;
        }

        public String getDeptCode() {
            return DeptCode;
        }

        public void setDeptCode(String deptCode) {
            DeptCode = deptCode;
        }

        public int getDeptId() {
            return DeptId;
        }

        public void setDeptId(int deptId) {
            DeptId = deptId;
        }

        public String getDeptName() {
            return DeptName;
        }

        public void setDeptName(String deptName) {
            DeptName = deptName;
        }

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
    }
}
