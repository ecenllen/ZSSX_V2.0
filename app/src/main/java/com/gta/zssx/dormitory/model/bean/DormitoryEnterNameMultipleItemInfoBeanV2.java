package com.gta.zssx.dormitory.model.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lan.zheng on 2017/6/29.
 * 录入姓名多项总bean  -  班级维度:有备注和学号
 */

public class DormitoryEnterNameMultipleItemInfoBeanV2 implements Serializable {
    private String  DormitoryOrClassScore;   //班级分数
    private int DormitoryOrClassId; //保存的时候才使用
    private String Remark; //班级备注
    private List<EnterNameMultipleItemBean> StudentList;  //学生列表

    public String getClassScore() {
        return DormitoryOrClassScore;
    }

    public void setClassScore(String DormitoryOrClassScore) {
        this.DormitoryOrClassScore = DormitoryOrClassScore;
    }

    public int getDormitoryOrClassId() {
        return DormitoryOrClassId;
    }

    public void setDormitoryOrClassId(int DormitoryOrClassId) {
        this.DormitoryOrClassId = DormitoryOrClassId;
    }

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String Remark) {
        this.Remark = Remark;
    }

    public List<EnterNameMultipleItemBean> getStudentList() {
        return StudentList;
    }

    public void setStudentList(List<EnterNameMultipleItemBean> StudentList) {
        this.StudentList = StudentList;
    }
}
