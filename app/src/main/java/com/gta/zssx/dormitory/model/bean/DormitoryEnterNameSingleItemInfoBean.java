package com.gta.zssx.dormitory.model.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lan.zheng on 2017/6/30.
 * 录入姓名单项总bean  -  宿舍维度：有床位
 */

public class DormitoryEnterNameSingleItemInfoBean implements Serializable{
    private String DormitoryOrClassScore;  //宿舍分数
    private int DormitoryOrClassId;
    private String Remark;
    private ArrayList<EnterNameSingleItemBean> StudentList;  //学生列表
    private ArrayList<ClassInfoBean> ClassList;  //宿舍维度班级列表

    public String getDormitoryOrClassScore() {
        return DormitoryOrClassScore;
    }

    public void setDormitoryOrClassScore(String dormitoryOrClassScore) {
        DormitoryOrClassScore = dormitoryOrClassScore;
    }

    public int getDormitoryOrClassId() {
        return DormitoryOrClassId;
    }

    public void setDormitoryOrClassId(int dormitoryOrClassId) {
        DormitoryOrClassId = dormitoryOrClassId;
    }

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String remark) {
        Remark = remark;
    }

    public String getDormitoryScore() {
        return DormitoryOrClassScore;
    }

    public void setDormitoryScore(String DormitoryScore) {
        this.DormitoryOrClassScore = DormitoryScore;
    }


    public List<EnterNameSingleItemBean> getStudentList() {
        return StudentList;
    }

    public void setStudentList(ArrayList<EnterNameSingleItemBean> StudentList) {
        this.StudentList = StudentList;
    }

    public ArrayList<ClassInfoBean> getClassList() {
        return ClassList;
    }

    public void setClassList(ArrayList<ClassInfoBean> ClassList) {
        this.ClassList = ClassList;
    }
}
