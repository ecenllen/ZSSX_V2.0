package com.gta.zssx.patrolclass.model.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * [Description]
 * <p/>  添加按计划巡课班级和随机巡课班级 实体
 * [How to use]
 * <p/>
 * [Tips] Created by tengfei.lv on 2016/7/18 10:28.
 */

public class ClassChooseEntity implements Serializable {

    private int deptId;//系部ID
    private String deptName;//系部名称
    private String deptCode;//

    private List<ClassListBean> classList;

    public int getDeptId () {
        return deptId;
    }

    public void setDeptId (int deptId) {
        this.deptId = deptId;
    }

    public String getDeptName () {
        return deptName;
    }

    public void setDeptName (String deptName) {
        this.deptName = deptName;
    }

    public String getDeptCode () {
        return deptCode;
    }

    public void setDeptCode (String deptCode) {
        this.deptCode = deptCode;
    }

    public List<ClassListBean> getClassList () {
        return classList;
    }

    public void setClassList (List<ClassListBean> classList) {
        this.classList = classList;
    }

    public static class ClassListBean{
        private int id;
        private String className;
        private int deptId;
        private String deptName;
        private String deptCode;
        private String year;

        private boolean isCheck;


        private int type;   //0,系部，1，班级

        public boolean isCheck () {
            return isCheck;
        }

        public void setCheck (boolean check) {
            isCheck = check;
        }

        public int getType () {
            return type;
        }

        public void setType (int type) {
            this.type = type;
        }


        public int getId () {
            return id;
        }

        public void setId (int id) {
            id = id;
        }

        public String getClassName () {
            return className;
        }

        public void setClassName (String className) {
            className = className;
        }

        public int getDeptId () {
            return deptId;
        }

        public void setDeptId (int deptId) {
            this.deptId = deptId;
        }

        public String getDeptName () {
            return deptName;
        }

        public void setDeptName (String deptName) {
            this.deptName = deptName;
        }

        public String getDeptCode () {
            return deptCode;
        }

        public void setDeptCode (String deptCode) {
            this.deptCode = deptCode;
        }

        public String getYear () {
            return year;
        }

        public void setYear (String year) {
            year = year;
        }
    }


    public static List<ClassChooseEntity> getAddPlanClassDatas(){
        List<ClassChooseEntity> datas = new ArrayList<> ();
        for(int i = 0;i<5;i++){
            ClassChooseEntity entity = new ClassChooseEntity ();
            entity.setDeptName ("会计部");
            entity.setDeptCode ("会");
            List<ClassChooseEntity.ClassListBean> beanDatas = new ArrayList<> ();
            for(int j = 0;j<5;j++){
                ClassListBean bean = new ClassListBean ();
                bean.setClassName ("15会计"+i);
                beanDatas.add (bean);
            }
            entity.setClassList (beanDatas);

            datas.add (entity);
        }
        return datas;
    }
}
