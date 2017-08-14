package com.gta.zssx.fun.coursedaily.registercourse.model.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lan.zheng on 2017/3/23.
 * 用于考勤状态改变的传递 -- 回调
 */
public class RxSectionBean implements Serializable{
    private SectionBean mSectionBean;   //用于回调
    private int mWitchString;  //更新的是分数还是备注

    public void setSectionBean(SectionBean mSectionBean){
        this.mSectionBean = mSectionBean;
    }

    public SectionBean getSectionBean(){
        return mSectionBean;
    }

    public void setWitchString(int mWitchString){
        this.mWitchString =mWitchString;
    }

    public int getWitchString(){
        return mWitchString;
    }

    //分数
    public static final int SCORE = 1;
    //备注
    public static final int REMARK = 2;
}
