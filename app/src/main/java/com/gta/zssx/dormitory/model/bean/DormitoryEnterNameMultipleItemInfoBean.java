package com.gta.zssx.dormitory.model.bean;

import com.gta.utils.resource.L;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lan.zheng on 2017/6/29.
 * 录入姓名多项总bean  -  宿舍维度：有床位，没有备注
 */

public class DormitoryEnterNameMultipleItemInfoBean implements Serializable{
    private String DormitoryOrClassScore;  //宿舍分数
    private int DormitoryOrClassId; //保存的时候才使用
    private List<EnterNameMultipleItemBean> StudentList;  //学生列表
    private List<ClassInfoBean> ClassList;  //宿舍维度班级列表

    public String getDormitoryScore() {
        return DormitoryOrClassScore;
    }

    public void setDormitoryScore(String DormitoryOrClassScore) {
        this.DormitoryOrClassScore = DormitoryOrClassScore;
    }


    public int getDormitoryOrClassId() {
        return DormitoryOrClassId;
    }

    public void setDormitoryOrClassId(int DormitoryOrClassId) {
        this.DormitoryOrClassId = DormitoryOrClassId;
    }

    public List<EnterNameMultipleItemBean> getStudentList() {
        return StudentList;
    }

    public void setStudentList(List<EnterNameMultipleItemBean> StudentList) {
        this.StudentList = StudentList;
    }

    public List<ClassInfoBean> getClassList() {
        return ClassList;
    }

    public void setClassList(List<ClassInfoBean> ClassList) {
        this.ClassList = ClassList;
    }
}
