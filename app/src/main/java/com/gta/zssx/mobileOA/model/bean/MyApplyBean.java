package com.gta.zssx.mobileOA.model.bean;

import java.io.Serializable;

/**
 * Created by xiaoxia.rang on 2016/11/21.
 * 我的申请
 */

public class MyApplyBean  implements Serializable{
    /**
     * 待办事项id
     */
    private String Id;

    /**
     * 已办事项id
     */
    private String RunId;
//    /**
//     * 发起人
//     */
//    private String Creator;

    /**
     * 头像
     */
    private String AvatarUrl;

    /**
     * 部门id
     */
    private String Position;

    /**
     * 申请时间
     */
    private String CreateTime;

    /**
     * 事项主题
     */
    private String Subject;

    /**
     * 表单名称
     */
    private String ProcessName;

    /**
     * 审批状态（1：审批中 2：已同意 3：已驳回）
     */
    private String Status;

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getRunId() {
        return RunId;
    }

    public void setRunId(String runId) {
        RunId = runId;
    }
//
//    public String getCreator() {
//        return Creator;
//    }
//
//    public void setCreator(String creator) {
//        Creator = creator;
//    }

    public String getAvatarUrl() {
        return AvatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        AvatarUrl = avatarUrl;
    }

    public String getPosition() {
        return Position;
    }

    public void setPosition(String position) {
        Position = position;
    }

    public String getCreateTime() {
        return CreateTime;
    }

    public void setCreateTime(String createTime) {
        CreateTime = createTime;
    }

    public String getSubject() {
        return Subject;
    }

    public void setSubject(String subject) {
        Subject = subject;
    }

    public String getProcessName() {
        return ProcessName;
    }

    public void setProcessName(String processName) {
        ProcessName = processName;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }
}
