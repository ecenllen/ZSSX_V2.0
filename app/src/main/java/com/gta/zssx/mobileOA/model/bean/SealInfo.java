package com.gta.zssx.mobileOA.model.bean;

import java.io.Serializable;

/**
 * Created by xiaoxia.rang on 2017/4/24.
 * 印章对象
 */

public class SealInfo implements Serializable {

    /**
     * 印章图片地址
     */
    private String sealPath;
    /**
     * 印章名
     */
    private String sealName;
    /**
     * 印章密码
     */
    private String sealPassword;
    /**
     * 印章id
     */
    private String sealId;

    public String getSealId() {
        return sealId;
    }

    public void setSealId(String sealId) {
        this.sealId = sealId;
    }

    public String getSealPath() {
        return sealPath;
    }

    public void setSealPath(String sealPath) {
        this.sealPath = sealPath;
    }

    public String getSealName() {
        return sealName;
    }

    public void setSealName(String sealName) {
        this.sealName = sealName;
    }

    public String getSealPassword() {
        return sealPassword;
    }

    public void setSealPassword(String sealPassword) {
        this.sealPassword = sealPassword;
    }
}
