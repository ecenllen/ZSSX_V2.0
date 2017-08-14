package com.gta.zssx.fun.coursedaily.registercourse.model.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lan.zheng on 2017/2/27.
 * 用于课堂日志登记页面从课表获取默认的老师和课程
 */
public class DefaultRegistInfoBean implements Serializable {
    private List<DetailItemShowBean.CourseInfoBean> MultipleCoursesList;
    private List<DetailItemShowBean.TeacherInfoBean> MultipleTeachersList;
    private String TeacherNo;  //拿到老师编号
    private boolean IsTeacher; //是否是老师

    public List<DetailItemShowBean.CourseInfoBean> getMultipleCoursesList(){
        return MultipleCoursesList;
    }

    public void setMultipleCoursesList(List<DetailItemShowBean.CourseInfoBean> MultipleCoursesList){
        this.MultipleCoursesList = MultipleCoursesList;
    }

    public List<DetailItemShowBean.TeacherInfoBean> getMultipleTeachersList(){
        return MultipleTeachersList;
    }

    public void setMultipleTeachersList(List<DetailItemShowBean.TeacherInfoBean> MultipleTeachersList){
        this.MultipleTeachersList = MultipleTeachersList;
    }

    public String getTeacherNo(){
        return TeacherNo;
    }

    public void setTeacherNo(String TeacherNo) {
        this.TeacherNo = TeacherNo;
    }

    public boolean getIsTeacher(){
        return IsTeacher;
    }

    public void serIsTeacher(boolean IsTeacher){
        this.IsTeacher = IsTeacher;
    }

}
