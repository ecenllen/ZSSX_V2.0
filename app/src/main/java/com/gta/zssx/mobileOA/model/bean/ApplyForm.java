package com.gta.zssx.mobileOA.model.bean;

import java.io.Serializable;

/**
 * Created by xiaoxia.rang on 2016/11/14.
 * 发起申请->申请表
 */

public class ApplyForm implements Serializable {
    /**
     * 申请表id
     */
    private String Id;
    /**
     * 申请表名
     */
    private String Name;

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        this.Name = name;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        this.Id = id;
    }
}
