package com.gta.zssx.fun.adjustCourse.model.bean;

/**
 * [Description]
 * <p>
 * [How to use]
 * <p>
 * [Tips]
 *
 * @author Created by Zhimin.Huang on 2017/4/7.
 * @since 1.0.0
 */
public class AdjustPushBean {
    private int waitAuditNumber;
    private int waitConfirmNumber;
    private int transferType;
    private int sendType;
    private int detailId;

    public int getWaitAuditNumber() {
        return waitAuditNumber;
    }

    public void setWaitAuditNumber(int waitAuditNumber) {
        this.waitAuditNumber = waitAuditNumber;
    }

    public int getWaitConfirmNumber() {
        return waitConfirmNumber;
    }

    public void setWaitConfirmNumber(int waitConfirmNumber) {
        this.waitConfirmNumber = waitConfirmNumber;
    }

    public int getTransferType() {
        return transferType;
    }

    public void setTransferType(int transferType) {
        this.transferType = transferType;
    }

    public int getSendType() {
        return sendType;
    }

    public void setSendType(int sendType) {
        this.sendType = sendType;
    }

    public int getDetailId() {
        return detailId;
    }

    public void setDetailId(int detailId) {
        this.detailId = detailId;
    }
}
