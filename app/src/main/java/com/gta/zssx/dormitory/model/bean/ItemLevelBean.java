package com.gta.zssx.dormitory.model.bean;

import java.io.Serializable;

/**
 * Created by lan.zheng on 2017/6/28.
 * 一个指标项层级下的层级或指标项
 */

public class ItemLevelBean implements Serializable{
    private String ItemName;
    private String ItemFullName;
    private int ItemId;
    private int ItemDimension;
    private int Type;

    public String getItemName() {
        return ItemName;
    }

    public void setItemName(String ItemName) {
        this.ItemName = ItemName;
    }

    public String getItemFullName() {
        return ItemFullName;
    }

    public void setItemFullName(String ItemFullName) {
        this.ItemFullName = ItemFullName;
    }

    public int getItemId() {
        return ItemId;
    }

    public void setItemId(int ItemId) {
        this.ItemId = ItemId;
    }

    public int getItemDimension() {
        return ItemDimension;
    }

    public void setItemDimension(int ItemDimension) {
        this.ItemDimension = ItemDimension;
    }

    public int getType() {
        return Type;
    }

    public void setType(int Type) {
        this.Type = Type;
    }

}
