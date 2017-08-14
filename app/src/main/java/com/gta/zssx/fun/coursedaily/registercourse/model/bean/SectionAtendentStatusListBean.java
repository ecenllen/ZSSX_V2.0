package com.gta.zssx.fun.coursedaily.registercourse.model.bean;

import java.util.List;

/**
 * Created by lan.zheng on 2017/3/27.
 * 全部节次考勤和学生列表状态 -- 提交登记时候需要转换
 */
public class SectionAtendentStatusListBean {
     private List<SectionBean> mSectionBeanList;   //用于本地存储所有登记的section数据
     private List<Boolean> mSameWithPreviousStatusList;   //记录

    public void setSectionBeanList(List<SectionBean>  mSectionBeanList){
        this.mSectionBeanList = mSectionBeanList;
    }

    public List<SectionBean>  getSectionBeanList(){
        return mSectionBeanList;
    }

    public void setSameWithPreviousStatusList(List<Boolean> mSameWithPreviousStatusList){
        this.mSameWithPreviousStatusList = mSameWithPreviousStatusList;
    }

    public List<Boolean> getSameWithPreviousStatusList(){
        return mSameWithPreviousStatusList;
    }
}
