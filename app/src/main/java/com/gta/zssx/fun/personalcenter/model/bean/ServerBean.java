package com.gta.zssx.fun.personalcenter.model.bean;

import java.io.Serializable;

/**
 * [Description]
 * <p>
 * [How to use]
 * <p>
 * [Tips]
 *
 * @author Created by Zhimin.Huang on 2016/7/6.
 * @since 1.0.0
 */
public class ServerBean implements Serializable {

    private String ip;
    private String CourseDailyUrl;
    private String PatrolClassUrl;
    private String assetUrl;
    private String OAUrL;
    private String AdjustCourseUrl;
    private String ticket;
    private String DormitoryUrl;
    private String ClassroomFeedbackUrl;

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }

    public String getAdjustCourseUrl() {
        return AdjustCourseUrl;
    }

    public void setAdjustCourseUrl(String adjustCourseUrl) {
        AdjustCourseUrl = adjustCourseUrl;
    }

    public String getAssetUrl() {
        return assetUrl;
    }

    public void setAssetUrl(String assetUrl) {
        this.assetUrl = assetUrl;
    }

    public String getPatrolClassUrl() {
        return PatrolClassUrl;
    }

    public void setPatrolClassUrl(String patrolClassUrl) {
        PatrolClassUrl = patrolClassUrl;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getCourseDailyUrl() {
        return CourseDailyUrl;
    }

    public void setCourseDailyUrl(String courseDailyUrl) {
        this.CourseDailyUrl = courseDailyUrl;
    }

    public String getOAUrL() {
        return OAUrL;
    }

    public void setOAUrL(String OAUrL) {
        this.OAUrL = OAUrL;
    }

    public void setDormitoryUrl(String DormitoryUrl){
        this.DormitoryUrl = DormitoryUrl;
    }

    public String getDormitoryUrl(){
        return DormitoryUrl;
    }

    public String getClassroomFeedbackUrl () {
        return ClassroomFeedbackUrl;
    }

    public void setClassroomFeedbackUrl (String classroomFeedbackUrl) {
        ClassroomFeedbackUrl = classroomFeedbackUrl;
    }
}
