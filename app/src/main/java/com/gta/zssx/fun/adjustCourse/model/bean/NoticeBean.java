package com.gta.zssx.fun.adjustCourse.model.bean;

import java.io.Serializable;

/**
 * [Description]
 * <p>
 * [How to use]
 * <p>
 * [Tips]
 *
 * @author Created by Zhimin.Huang on 2017/4/6.
 * @since 1.0.0
 */
public class NoticeBean implements Serializable {


    
    /**
     * waitAuditNumber : 待审核数
     * isHasRightAudit : 是否有审核权限：0为没有审核权限，1为有审核权限
     * waitConfirmNumber : 待确认数
     */

    private int waitAuditNumber;
    private int waitConfirmNumber;
    private int isHasRightAudit;
    private int isAudit;

    public int getIsAudit() {
        return isAudit;
    }

    public void setIsAudit(int isAudit) {
        this.isAudit = isAudit;
    }

    public int getWaitAuditNumber() {
        return waitAuditNumber;
    }

    public void setWaitAuditNumber(int waitAuditNumber) {
        this.waitAuditNumber = waitAuditNumber;
    }

    public int getIsHasRightAudit() {
        return isHasRightAudit;
    }

    public void setIsHasRightAudit(int isHasRightAudit) {
        this.isHasRightAudit = isHasRightAudit;
    }

    public int getWaitConfirmNumber() {
        return waitConfirmNumber;
    }

    public void setWaitConfirmNumber(int waitConfirmNumber) {
        this.waitConfirmNumber = waitConfirmNumber;
    }
}
