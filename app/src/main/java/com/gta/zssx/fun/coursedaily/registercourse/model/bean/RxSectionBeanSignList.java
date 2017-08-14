package com.gta.zssx.fun.coursedaily.registercourse.model.bean;

import java.io.Serializable;
import java.util.Set;

/**
 * Created by lan.zheng on 2017/4/7.
 * 用于当发现要登记的节次已经被登记后，默认从节次中删除
 */
public class RxSectionBeanSignList implements Serializable{
    private Set<SectionBean> sectionBeanList;
    public void setSectionBeanList(Set<SectionBean> sectionBeanList){
        this.sectionBeanList = sectionBeanList;
    }

    public Set<SectionBean> getSectionBeanList(){
        return sectionBeanList;
    }
}
