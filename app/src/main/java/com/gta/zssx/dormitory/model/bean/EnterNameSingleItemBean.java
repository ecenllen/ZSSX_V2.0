package com.gta.zssx.dormitory.model.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lan.zheng on 2017/6/28.
 * 录入姓名单项详情 - 一个学生下的选项
 */

public class EnterNameSingleItemBean implements Serializable{
    /**
     "StudentScore":"",
     "StudentId":2326,
     "StudentName":"郭苇权",
     "StudentNo":null,
     "ClassId":112,
     "BedName":"1床",
     "BedNo":null,
     "StudentCheck":false,
     "DetailItemList":null
     */
    private int StudentId; //学生Id
    private int ClassId;  //学生所在班级Id
    private String StudentName;  //学生学号
    private String StudentNo;  //学生学号
    private String StudentScore;  //学生分数
    private String BedName;  //床位名称
    private int BedId;  //床位Id
    private boolean StudentCheck; //是否勾选

    public int getStudentId(){
        return StudentId;
    }

    public void setStudentId(int StudentId){
        this.StudentId = StudentId;
    }

    public int getClassId(){
        return ClassId;
    }

    public void setClassId(int ClassId){
        this.ClassId = ClassId;
    }

    public String getStudentName() {
        return StudentName;
    }

    public void setStudentName(String StudentName) {
        this.StudentName = StudentName;
    }

    public String getStudentNo() {
        return StudentNo;
    }

    public void setStudentNo(String StudentNo) {
        this.StudentNo = StudentNo;
    }

    public String getStudentScore() {
        return StudentScore;
    }

    public void setStudentScore(String StudentScore) {
        this.StudentScore = StudentScore;
    }

    public String getBedName() {
        return BedName;
    }

    public void setBedName(String BedName) {
        this.BedName = BedName;
    }

    public int getBedId() {
        return BedId;
    }

    public void setBedId(int BedId) {
        this.BedId = BedId;
    }


    public boolean getStudentCheck() {
        return StudentCheck;
    }

    public void setStudentCheck(boolean StudentCheck) {
        this.StudentCheck = StudentCheck;
    }
}
