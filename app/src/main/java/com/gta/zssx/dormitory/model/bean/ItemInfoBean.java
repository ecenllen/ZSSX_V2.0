package com.gta.zssx.dormitory.model.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lan.zheng on 2017/6/28.
 * 指标项选择bean
 */

public class ItemInfoBean implements Serializable{
    private String Name;  //指标项或层级名称
    private int Id; //Id
    private List<ItemLevelBean> LevelList;  //层级内信息

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public int getId() {
        return Id;
    }

    public void setId(int Id) {
        this.Id = Id;
    }

    public List<ItemLevelBean> getLevelList() {
        return LevelList;
    }

    public void setLevelList(List<ItemLevelBean> LevelList) {
        this.LevelList = LevelList;
    }

}
