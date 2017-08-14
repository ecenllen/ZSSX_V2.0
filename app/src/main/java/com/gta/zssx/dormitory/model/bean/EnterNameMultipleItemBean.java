package com.gta.zssx.dormitory.model.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lan.zheng on 2017/6/28.
 * 录入姓名多项详情  - 一个学生下的选项
 */

public class EnterNameMultipleItemBean implements Serializable{
    private int StudentId; //学生Id
    private String StudentName;  //学生学号
    private String StudentNo;  //学生学号
    private int ClassId;  //学生所在班级Id
    private String ClassName; //班级名称
    private String StudentScore;  //学生分数
    private String BedName;  //床位名称
    private String BedNo;  //床位Id
    private int DetailItemId;  //条目Id
    private boolean isGroupChecked;

    public boolean isGroupChecked() {
        return isGroupChecked;
    }

    public void setGroupChecked(boolean groupChecked) {
        isGroupChecked = groupChecked;
    }

    private List<DetailItemBean> DetailItemList;  //一个学生包含的指标项

    public int getDetailItemId(){
        return DetailItemId;
    }

    public void setDetailItemId(int DetailItemId){
        this.DetailItemId = DetailItemId;
    }

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

    public String getClassName(){
        return ClassName;
    }

    public void setClassName(String ClassName){
        this.ClassName = ClassName;
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

    public String getBedId() {
        return BedNo;
    }

    public void setBedId(String BedNo) {
        this.BedNo = BedNo;
    }

    public List<DetailItemBean> getIndexItemBeanList(){
        return DetailItemList;
    }

    public void setIndexItemBeanList(List<DetailItemBean> indexItemBeanList){
        this.DetailItemList = indexItemBeanList;
    }
}
