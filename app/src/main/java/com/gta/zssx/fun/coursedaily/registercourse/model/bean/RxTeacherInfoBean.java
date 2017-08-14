package com.gta.zssx.fun.coursedaily.registercourse.model.bean;

import java.util.List;

/**
 * Created by lan.zheng on 2017/6/6.
 */

public class RxTeacherInfoBean {
    private int pageFlag;

    private DetailItemShowBean.TeacherInfoBean teacherInfoBean;

    public void setPageFlag(int pageFlag){
        this.pageFlag = pageFlag;
    }

    public int getPageFlag(){
        return pageFlag;
    }

    public void setTeacherInfoBean(DetailItemShowBean.TeacherInfoBean teacherInfoBean){
        this.teacherInfoBean = teacherInfoBean;
    }

    public DetailItemShowBean.TeacherInfoBean getTeacherInfoBean(){
        return teacherInfoBean;
    }

}
