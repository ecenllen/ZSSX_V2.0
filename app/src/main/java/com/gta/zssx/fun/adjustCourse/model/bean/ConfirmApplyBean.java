package com.gta.zssx.fun.adjustCourse.model.bean;

import java.io.Serializable;

/**
 * [Description]
 * <p>
 * [How to use]
 * <p>
 * [Tips]
 *
 * @author Created by Zhimin.Huang on 2016/11/4.
 * @since 1.0.0
 */
public class ConfirmApplyBean implements Serializable {

    /**
     * applyId : 23
     * userId : 65bfec1c-9e26-42ea-a9e0-8cd3924532c0
     * userName : 徐勇
     * auditOptions : 同意
     */

    private int applyId;
    private int type;
    private String userId;
    private String userName;
    private String auditOptions;

    public void setType(int type) {
        this.type = type;
    }

    public int getApplyId() {
        return applyId;
    }

    public void setApplyId(int applyId) {
        this.applyId = applyId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAuditOptions() {
        return auditOptions;
    }

    public void setAuditOptions(String auditOptions) {
        this.auditOptions = auditOptions;
    }
}
