package com.gta.zssx.mobileOA.model.bean;

import java.io.Serializable;

/**
 * Created by lan.zheng on 2016/10/12.
 */
public class GetCalendarInfo implements Serializable {

    private String UserId; // 唯一标识
    private String beginTime;
    private String endTime;
    private int id;
    private String startTime;
    private int scheduleType;
    private String remind;
    private String scheduleContent;
    private String createBy;
    private int status;

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        this.UserId = userId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(String beginTime) {
        this.beginTime = beginTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public int getScheduleType(){
        return scheduleType;
    }

    public void setScheduleType(int scheduleType){
        this.scheduleType = scheduleType;
    }

    public String getRemind() {
        return remind;
    }

    public void setRemind(String remind) {
        this.remind = remind;
    }

    public String getScheduleContent() {
        return scheduleContent;
    }

    public void setScheduleContent(String scheduleContent) {
        this.scheduleContent = scheduleContent;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
