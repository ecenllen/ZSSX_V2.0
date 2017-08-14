package com.gta.zssx.dormitory.model.bean;

import java.io.Serializable;

/**
 * Created by lan.zheng on 2017/6/29.
 * 录入姓名多项详细页面 - 一个指标项item的分数
 */

public class DetailItemBean implements Serializable{
    private String DetailItemName;
    private int DetailItemId;
    private String DetailItemScoreSet;
    private boolean DetailItemCheck;

    public int getDetailItemId(){
        return DetailItemId;
    }

    public void setDetailItemId(int DetailItemId){
        this.DetailItemId = DetailItemId;
    }

    public String getDetailItemScoreSet(){
        return DetailItemScoreSet;
    }

    public void setDetailItemScoreSet(String DetailItemScoreSet){
        this.DetailItemScoreSet = DetailItemScoreSet;
    }

    public String getDetailItemName(){
        return DetailItemName;
    }

    public void setDetailItemName(String DetailItemName){
        this.DetailItemName = DetailItemName;
    }

    public boolean getDetailItemCheck(){
        return DetailItemCheck;
    }

    public void setDetailItemCheck(boolean DetailItemCheck){
        this.DetailItemCheck = DetailItemCheck;
    }
}