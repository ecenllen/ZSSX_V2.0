package com.gta.zssx.mobileOA.model.bean;

import java.io.Serializable;

/**
 * Created by xiaoxia.rang on 2016/10/27.
 * 待办、已办、我的申请
 * 注：已办事项返回为runId,其他事项返回为id
 */
public class Backlog implements Serializable {
//
//    "id": "150000000150012",
//            "subject": "资产转移(信息类)-系统管理员-2016-11-17 14:49:05",
//            "creator": "系统管理员",
//            "creatorAccount": "admin",
//            "actInstId": "100000000010677",
//            "processName": "资产转移(信息类)",
//            "createTime": "2016-11-17 17:24:24",
//            "avatarUrl": "/ecloud/face/100010001/20161118172917873.jpg",
//            "status": "6",
//            "position": ""

    /**
     * 待办事项id
     */
    private String id;

    /**
     * 已办事项id
     */
    private String runId;
    /**
     * 发起人
     */
    private String creator;

    /**
     * 头像
     */
    private String avatarUrl;

    /**
     * 部门id
     */
    private String position;

    /**
     * 申请时间
     */
    private String createTime;

    /**
     * 事项主题
     */
    private String subject;

    /**
     * 表单名称
     */
    private String processName;

    /**
     * 审批状态（1：审批中 2：已同意 3：已驳回）
     */
    private String status;

    /**
     * 流程id
     */
    private String actInstId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRunId() {
        return runId;
    }

    public void setRunId(String runId) {
        this.runId = runId;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getProcessName() {
        return processName;
    }

    public void setProcessName(String processName) {
        this.processName = processName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getActInstId() {
        return actInstId;
    }

    public void setActInstId(String actInstId) {
        this.actInstId = actInstId;
    }
}
