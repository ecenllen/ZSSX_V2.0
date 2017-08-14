package com.gta.zssx.dormitory.model.bean;

import java.io.Serializable;

/**
 * Created by weiye.chen on 2017/6/28.
 */

public class OptionItemBean implements Serializable {
    /**
     * ItemId：
     ItemName：
     ItemScoreSet:分值
     ItemCheck：true
     */
    private int ItemId;
    private String ItemName;
    private float ItemScoreSet;
    private boolean ItemCheck;

    public int getItemId() {
        return ItemId;
    }

    public void setItemId(int itemId) {
        ItemId = itemId;
    }

    public String getItemName() {
        return ItemName;
    }

    public void setItemName(String itemName) {
        ItemName = itemName;
    }

    public float getItemScoreSet() {
        return ItemScoreSet;
    }

    public void setItemScoreSet(float itemScoreSet) {
        ItemScoreSet = itemScoreSet;
    }

    public boolean isItemCheck() {
        return ItemCheck;
    }

    public void setItemCheck(boolean itemCheck) {
        ItemCheck = itemCheck;
    }

//    @Override
//    protected Object clone(){
//        OptionItemBean optionItemBean = null;
//        try {
//            optionItemBean = (OptionItemBean) super.clone();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return optionItemBean;
//    }
}
