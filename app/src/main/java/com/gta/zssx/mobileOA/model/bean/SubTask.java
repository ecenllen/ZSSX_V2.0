package com.gta.zssx.mobileOA.model.bean;

import java.io.Serializable;

/**
 * Created by xiaoxia.rang on 2016/11/5.
 * 子任务
 */

public class SubTask implements Serializable {

    /**
     * 任务id
     */
    private String taskId;
    /**
     * 任务详情
     */
    private String topic;
    /**
     * 任务状态
     */
    private int status;
    /**
     * 负责人
     */
    private String person;
    /**
     * 时间
     */
    private String time;

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getPerson() {
        return person;
    }

    public void setPerson(String person) {
        this.person = person;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
