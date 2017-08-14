package com.gta.zssx.mobileOA.model.bean;

import java.io.Serializable;

/**
 * Created by xiaoxia.rang on 2016/10/26.
 */

public class Meeting implements Serializable {

    /**
     * 会议id
     */
    private int MeetingId;

    /**
     * 运行id
     */
    private String RunId;
    /**
     * 申请人
     */
    private String Creator;
    /**
     * 会议主题
     */
    private String Topic;
    /**
     * 会议开始时间
     */
    private String StartTime;
    /**
     * 会议结束时间
     */
    private String EndTime;
    /**
     * 会议室
     */
    private String MeetRoom;
    /**
     * 会议审批通过时间
     */
    private String CompleteTime;
    /**
     * 会议状态状：0：未读 1 ：已读
     */
    private String Status;

    public int getMeetingId() {
        return MeetingId;
    }

    public void setMeetingId(int meetingId) {
        MeetingId = meetingId;
    }

    public String getCreator() {
        return Creator;
    }

    public void setCreator(String creator) {
        this.Creator = creator;
    }

    public String getTopic() {
        return Topic;
    }

    public void setTopic(String topic) {
        Topic = topic;
    }

    public String getStartTime() {
        return StartTime;
    }

    public void setStartTime(String startTime) {
        StartTime = startTime;
    }

    public String getEndTime() {
        return EndTime;
    }

    public void setEndTime(String endTime) {
        EndTime = endTime;
    }

    public String getCompleteTime() {
        return CompleteTime;
    }

    public void setCompleteTime(String completeTime) {
        CompleteTime = completeTime;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getRunId() {
        return RunId;
    }

    public void setRunId(String runId) {
        RunId = runId;
    }

    public String getMeetRoom() {
        return MeetRoom;
    }

    public void setMeetRoom(String meetRoom) {
        MeetRoom = meetRoom;
    }
}
