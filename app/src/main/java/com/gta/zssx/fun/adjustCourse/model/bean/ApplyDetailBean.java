package com.gta.zssx.fun.adjustCourse.model.bean;

import java.io.Serializable;

/**
 * [Description]
 * <p>
 * [How to use]
 * <p>
 * [Tips]
 *
 * @author Created by Zhimin.Huang on 2016/10/28.
 * @since 1.0.0
 */
public class ApplyDetailBean implements Serializable {


    /**
     * applyType : D
     * applyPolysyllabic : 1
     * adjustPolysyllabic : 1
     * applyTime : 1478534400000
     * adjustTime : 1478534400000
     * applyCause : 哈哈哈
     * applyId : 66
     * teacherType : false
     * auditStatus : N
     * adjustTeacherId : 4
     * applyClassName : 15会计3
     * adjustClassName : 15会计3
     * applyCourseName : 人文素养
     * adjustCourseName : null
     * applyTeacherName : 徐勇
     * adjustTeacherName : 冯让
     * adjustPhone : 15602333098
     * polysyllabic : null
     * adjustTeacherBid : 58fdd037-b534-4977-a139-3dbdf9908732
     * applyTeacherId : 13
     * applyTeacherBid : 65bfec1c-9e26-42ea-a9e0-8cd3924532c0
     */

//    申请类型，调课：T,代课：D,调时间：H
    private String applyType;
    private String applyPolysyllabic;
    private String adjustPolysyllabic;
    private long applyTime;
    private long adjustTime;
    //    调代课备注
    private String applyCause;
    private int applyId;
    private boolean teacherType;
    //    审核状态：N未结束，Y已结束
    private String auditStatus;
    private int adjustTeacherId;
    private String applyClassName;
    private String adjustClassName;
    private String applyCourseName;
    private String adjustCourseName;
    private String applyTeacherName;
    private String adjustTeacherName;
    private String adjustPhone;
    private String polysyllabic;
    private String adjustTeacherBid;
    private int applyTeacherId;
    private String applyTeacherBid;

    private int semester;
    //    申请id
    private int transferApplyId;
    private int weekDayAdjust;
    private String creatorId;
    private String confirmId;
    private String polysyllabicWordStrTow;
    private String polysyllabicWordStr;
    private String adjustTeacherIdName;
    private String weekNameSecond;
    private String adjustWeekNameSecond;
    private String adjustWeekName;
    //    审核人
    private String auditorName;
    //    审核时间
    private String auditTime;
    //    审核意见
    private String auditOpinion;
    //    确认人
    private String confirmName;
    //    确认时间
    private String confirmTime;
    //    确认意见
    private String confirmOpinion;
    //    确认状态：0为已确认，Null为未确认
    private String confirmState;

    private String weekName;
    //    类型：0为确认，1为审核，2为查看
    private int auditType;

    private int openCourseTypeTwo;
    //    是否需要审核：0为不需要，1为需要审核
    private int isAudit;
    //    审核状态：1为已审核，Null为未审核
    private String auditState;

    private long applyDate;
    private long adjustDate;

    public String getConfirmId() {
        return confirmId;
    }

    public void setConfirmId(String confirmId) {
        this.confirmId = confirmId;
    }

    public int getSemester() {
        return semester;
    }

    public void setSemester(int semester) {
        this.semester = semester;
    }

    public int getTransferApplyId() {
        return transferApplyId;
    }

    public void setTransferApplyId(int transferApplyId) {
        this.transferApplyId = transferApplyId;
    }

    public int getWeekDayAdjust() {
        return weekDayAdjust;
    }

    public void setWeekDayAdjust(int weekDayAdjust) {
        this.weekDayAdjust = weekDayAdjust;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    public String getPolysyllabicWordStrTow() {
        return polysyllabicWordStrTow;
    }

    public void setPolysyllabicWordStrTow(String polysyllabicWordStrTow) {
        this.polysyllabicWordStrTow = polysyllabicWordStrTow;
    }

    public String getPolysyllabicWordStr() {
        return polysyllabicWordStr;
    }

    public void setPolysyllabicWordStr(String polysyllabicWordStr) {
        this.polysyllabicWordStr = polysyllabicWordStr;
    }

    public String getAdjustTeacherIdName() {
        return adjustTeacherIdName;
    }

    public void setAdjustTeacherIdName(String adjustTeacherIdName) {
        this.adjustTeacherIdName = adjustTeacherIdName;
    }

    public String getWeekNameSecond() {
        return weekNameSecond;
    }

    public void setWeekNameSecond(String weekNameSecond) {
        this.weekNameSecond = weekNameSecond;
    }

    public String getAdjustWeekNameSecond() {
        return adjustWeekNameSecond;
    }

    public void setAdjustWeekNameSecond(String adjustWeekNameSecond) {
        this.adjustWeekNameSecond = adjustWeekNameSecond;
    }

    public String getAdjustWeekName() {
        return adjustWeekName;
    }

    public void setAdjustWeekName(String adjustWeekName) {
        this.adjustWeekName = adjustWeekName;
    }

    public String getAuditorName() {
        return auditorName;
    }

    public void setAuditorName(String auditorName) {
        this.auditorName = auditorName;
    }

    public String getAuditTime() {
        return auditTime;
    }

    public void setAuditTime(String auditTime) {
        this.auditTime = auditTime;
    }

    public String getAuditOpinion() {
        return auditOpinion;
    }

    public void setAuditOpinion(String auditOpinion) {
        this.auditOpinion = auditOpinion;
    }

    public String getConfirmName() {
        return confirmName;
    }

    public void setConfirmName(String confirmName) {
        this.confirmName = confirmName;
    }

    public String getConfirmTime() {
        return confirmTime;
    }

    public void setConfirmTime(String confirmTime) {
        this.confirmTime = confirmTime;
    }

    public String getConfirmOpinion() {
        return confirmOpinion;
    }

    public void setConfirmOpinion(String confirmOpinion) {
        this.confirmOpinion = confirmOpinion;
    }

    public String getConfirmState() {
        return confirmState;
    }

    public void setConfirmState(String confirmState) {
        this.confirmState = confirmState;
    }

    public String getWeekName() {
        return weekName;
    }

    public void setWeekName(String weekName) {
        this.weekName = weekName;
    }

    public int getAuditType() {
        return auditType;
    }

    public void setAuditType(int auditType) {
        this.auditType = auditType;
    }

    public int getOpenCourseTypeTwo() {
        return openCourseTypeTwo;
    }

    public void setOpenCourseTypeTwo(int openCourseTypeTwo) {
        this.openCourseTypeTwo = openCourseTypeTwo;
    }

    public int getIsAudit() {
        return isAudit;
    }

    public void setIsAudit(int isAudit) {
        this.isAudit = isAudit;
    }

    public String getAuditState() {
        return auditState;
    }

    public void setAuditState(String auditState) {
        this.auditState = auditState;
    }

    public long getApplyDate() {
        return applyDate;
    }

    public void setApplyDate(long applyDate) {
        this.applyDate = applyDate;
    }

    public long getAdjustDate() {
        return adjustDate;
    }

    public void setAdjustDate(long adjustDate) {
        this.adjustDate = adjustDate;
    }

    public String getApplyType() {
        return applyType;
    }

    public void setApplyType(String applyType) {
        this.applyType = applyType;
    }

    public String getApplyPolysyllabic() {
        return applyPolysyllabic;
    }

    public void setApplyPolysyllabic(String applyPolysyllabic) {
        this.applyPolysyllabic = applyPolysyllabic;
    }

    public String getAdjustPolysyllabic() {
        return adjustPolysyllabic;
    }

    public void setAdjustPolysyllabic(String adjustPolysyllabic) {
        this.adjustPolysyllabic = adjustPolysyllabic;
    }

    public long getApplyTime() {
        return applyTime;
    }

    public void setApplyTime(long applyTime) {
        this.applyTime = applyTime;
    }

    public long getAdjustTime() {
        return adjustTime;
    }

    public void setAdjustTime(long adjustTime) {
        this.adjustTime = adjustTime;
    }

    public String getApplyCause() {
        return applyCause;
    }

    public void setApplyCause(String applyCause) {
        this.applyCause = applyCause;
    }

    public int getApplyId() {
        return applyId;
    }

    public void setApplyId(int applyId) {
        this.applyId = applyId;
    }

    public boolean isTeacherType() {
        return teacherType;
    }

    public void setTeacherType(boolean teacherType) {
        this.teacherType = teacherType;
    }

    public String getAuditStatus() {
        return auditStatus;
    }

    public void setAuditStatus(String auditStatus) {
        this.auditStatus = auditStatus;
    }

    public int getAdjustTeacherId() {
        return adjustTeacherId;
    }

    public void setAdjustTeacherId(int adjustTeacherId) {
        this.adjustTeacherId = adjustTeacherId;
    }

    public String getApplyClassName() {
        return applyClassName;
    }

    public void setApplyClassName(String applyClassName) {
        this.applyClassName = applyClassName;
    }

    public String getAdjustClassName() {
        return adjustClassName;
    }

    public void setAdjustClassName(String adjustClassName) {
        this.adjustClassName = adjustClassName;
    }

    public String getApplyCourseName() {
        return applyCourseName;
    }

    public void setApplyCourseName(String applyCourseName) {
        this.applyCourseName = applyCourseName;
    }

    public String getAdjustCourseName() {
        return adjustCourseName;
    }

    public void setAdjustCourseName(String adjustCourseName) {
        this.adjustCourseName = adjustCourseName;
    }

    public String getApplyTeacherName() {
        return applyTeacherName;
    }

    public void setApplyTeacherName(String applyTeacherName) {
        this.applyTeacherName = applyTeacherName;
    }

    public String getAdjustTeacherName() {
        return adjustTeacherName;
    }

    public void setAdjustTeacherName(String adjustTeacherName) {
        this.adjustTeacherName = adjustTeacherName;
    }

    public String getAdjustPhone() {
        return adjustPhone;
    }

    public void setAdjustPhone(String adjustPhone) {
        this.adjustPhone = adjustPhone;
    }

    public String getPolysyllabic() {
        return polysyllabic;
    }

    public void setPolysyllabic(String polysyllabic) {
        this.polysyllabic = polysyllabic;
    }

    public String getAdjustTeacherBid() {
        return adjustTeacherBid;
    }

    public void setAdjustTeacherBid(String adjustTeacherBid) {
        this.adjustTeacherBid = adjustTeacherBid;
    }

    public int getApplyTeacherId() {
        return applyTeacherId;
    }

    public void setApplyTeacherId(int applyTeacherId) {
        this.applyTeacherId = applyTeacherId;
    }

    public String getApplyTeacherBid() {
        return applyTeacherBid;
    }

    public void setApplyTeacherBid(String applyTeacherBid) {
        this.applyTeacherBid = applyTeacherBid;
    }
}
