package com.gta.zssx.patrolclass.model.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by liang.lu on 2017/3/10 17:18.
 */

public class DockScorePageBean implements Serializable {
    private List<PatrolRegisterBeanNew.TeacherListBean.DockScoreBean.OptionsBean> mOptionBeans;
    private String mTeacherName;
    private String autoScore;

    public String getAutoScore () {
        return autoScore;
    }

    public void setAutoScore (String autoScore) {
        this.autoScore = autoScore;
    }

    public List<PatrolRegisterBeanNew.TeacherListBean.DockScoreBean.OptionsBean> getmOptionBeans () {
        return mOptionBeans;
    }

    public void setmOptionBeans (List<PatrolRegisterBeanNew.TeacherListBean.DockScoreBean.OptionsBean> mOptionBeans) {
        this.mOptionBeans = mOptionBeans;
    }

    public String getmTeacherName () {
        return mTeacherName;
    }

    public void setmTeacherName (String mTeacherName) {
        this.mTeacherName = mTeacherName;
    }
}
