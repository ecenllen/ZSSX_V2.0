package com.gta.zssx.dormitory.model.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 评分录入-不录入姓名-有选项设置
 */
public class OptionBean implements Serializable {
    /**
     * RecordId：1
     DomitoryScore：100
     Status:0
     Remark:备注
     ItemList：[
     {
     ItemId：
     ItemName：
     ItemScoreSet:分值
     ItemCheck：true
     },
     ],

     */
    private String ClassScore;
    private int DomitoryScore;
    private String DormitoryOrClassScore;  //宿舍分数
    private int DormitoryOrClassId;
    private String Remark;
    private ArrayList<OptionItemBean> ItemList;
    private ArrayList<ClassInfoBean> ClassList;

    public int getDormitoryOrClassId() {
        return DormitoryOrClassId;
    }

    public void setDormitoryOrClassId(int dormitoryOrClassId) {
        DormitoryOrClassId = dormitoryOrClassId;
    }

    public String getDormitoryOrClassScore() {
        return DormitoryOrClassScore;
    }

    public void setDormitoryOrClassScore(String dormitoryOrClassScore) {
        DormitoryOrClassScore = dormitoryOrClassScore;
    }

    public String getClassScore() {
        return ClassScore;
    }

    public void setClassScore(String classScore) {
        ClassScore = classScore;
    }

    public ArrayList<ClassInfoBean> getClassList() {
        return ClassList;
    }

    public void setClassList(ArrayList<ClassInfoBean> classList) {
        ClassList = classList;
    }

    public int getDomitoryScore() {
        return DomitoryScore;
    }

    public void setDomitoryScore(int domitoryScore) {
        DomitoryScore = domitoryScore;
    }

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String remark) {
        Remark = remark;
    }

    public ArrayList<OptionItemBean> getItemList() {
        return ItemList;
    }

    public void setItemList(ArrayList<OptionItemBean> itemList) {
        ItemList = itemList;
    }

}
