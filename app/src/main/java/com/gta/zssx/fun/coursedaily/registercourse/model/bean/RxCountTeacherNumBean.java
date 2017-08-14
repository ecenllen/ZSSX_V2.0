package com.gta.zssx.fun.coursedaily.registercourse.model.bean;

import java.io.Serializable;
import java.util.Set;

/**
 * Created by lan.zheng on 2017/5/22.
 */
public class RxCountTeacherNumBean implements Serializable {


    private boolean isClickSureButton;
    private Set<DetailItemShowBean.TeacherInfoBean> teacherInfoBeanSet;  //老师列表不能重复
    private Set<DetailItemShowBean.TeacherInfoBean> teacherNewAddSet;    //新添加的老师数量

    public void setClickSureButton(boolean isClickSureButton){
        this.isClickSureButton =isClickSureButton;
    }

    public boolean getClickSureButton(){
        return isClickSureButton;
    }

    public void setTeacherInfoBeanSet(Set<DetailItemShowBean.TeacherInfoBean> teacherInfoBeanSet){
        this.teacherInfoBeanSet = teacherInfoBeanSet;
    }

    public Set<DetailItemShowBean.TeacherInfoBean> getTeacherInfoBeanSet(){
        return teacherInfoBeanSet;
    }

    public void setTeacherNewAddSet(Set<DetailItemShowBean.TeacherInfoBean> teacherNewAddSet){
        this.teacherNewAddSet = teacherNewAddSet;
    }

    public Set<DetailItemShowBean.TeacherInfoBean> getTeacherNewAddSet(){
        return teacherNewAddSet;
    }

    //总数
    public static final int TOTAL = 1;
    //新增数
    public static final int NEWADD = 2;
}
