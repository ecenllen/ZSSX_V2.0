package com.gta.zssx.fun.assetmanagement.model.bean;

/**
 * Created by weiye.chen on 2017/4/28.
 */

public class ApprovalDetailBean {
    /** 申请单号*/
    private String OrderNo;
    /** 申请单类型：转移/报废/..*/
    private String OrderTypeCN;
    /** 审批历史URL*/
    private String ApprovalHistoryUrl;

    public String getApprovalHistoryUrl() {
        return ApprovalHistoryUrl;
    }

    public void setApprovalHistoryUrl(String approvalHistoryUrl) {
        ApprovalHistoryUrl = approvalHistoryUrl;
    }

    public String getOrderNo() {
        return OrderNo;
    }

    public void setOrderNo(String orderNo) {
        OrderNo = orderNo;
    }

    public String getOrderTypeCN() {
        return OrderTypeCN;
    }

    public void setOrderTypeCN(String orderTypeCN) {
        OrderTypeCN = orderTypeCN;
    }
}
