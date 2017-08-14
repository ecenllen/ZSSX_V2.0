package com.gta.zssx.fun.classroomFeedback.model.bean;

import java.io.Serializable;
import java.util.List;

/**
 * [Description]
 * <p> 用于页面间数据传送的实体类
 * [How to use]
 * <p>
 * [Tips]
 *
 * @author Created by liang.lu on 2017/6/28 10:27.
 * @since 2.0.0
 */
public class TransferDataBean implements Serializable {
    /**
     * 状态 "-1": 未登记；"0":未送审；"1":待审核；"2":已通过；"3":未通过；
     */
    private String State;

    /**
     * 节次实体
     */
    private RegisterPageBean.ClassroomRegisterListBean.SectionDataListBean sectionBean;
    /**
     * 所有节次实体
     */
    private List<RegisterPageBean.ClassroomRegisterListBean.SectionDataListBean> allSectionBeanList;


    public List<RegisterPageBean.ClassroomRegisterListBean.SectionDataListBean> getAllSectionBeanList () {
        return allSectionBeanList;
    }

    public void setAllSectionBeanList (List<RegisterPageBean.ClassroomRegisterListBean.SectionDataListBean> allSectionBeanList) {
        this.allSectionBeanList = allSectionBeanList;
    }


    public RegisterPageBean.ClassroomRegisterListBean.SectionDataListBean getSectionBean () {
        return sectionBean;
    }

    public void setSectionBean (RegisterPageBean.ClassroomRegisterListBean.SectionDataListBean sectionBean) {
        this.sectionBean = sectionBean;
    }

    public String getState () {
        return State;
    }

    public void setState (String state) {
        State = state;
    }
}
