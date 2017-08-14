package com.gta.zssx.mobileOA.model.bean;

import java.io.Serializable;

/**
 * Created by xiaoxia.rang on 2016/10/31.
 * 附件
 */

public class Attachment implements Serializable {
    /**
     * 附件名
     */
    private String name;

    /**
     * 大小
     */
    private String size;
    /**
     * 地址
     */
    private String url;

    /**
     * 类型
     */
    private int type;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
