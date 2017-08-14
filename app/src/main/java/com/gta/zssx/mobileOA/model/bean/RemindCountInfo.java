package com.gta.zssx.mobileOA.model.bean;

import java.io.Serializable;

/**
 * Created by xiaoxia.rang on 2016/11/21.
 */

public class RemindCountInfo implements Serializable {

    /**
     * 待办、已办数量
     */
    private int Tasks;

    /**
     * 公告数量
     */
    private int Notice;

    /**
     * 邮箱
     */
    private int Mail;

    /**
     * 会议
     */
    private int Meeting;

    /**
     * 当天日程数量
     */
    private int Schedule;

    /**
     * 事务提醒
     */
    private int events;

    /**
     *任务总数
     */
    private int missions;

    public int getTasks() {
        return Tasks;
    }

    public void setTasks(int tasks) {
        this.Tasks = tasks;
    }

    public int getNotice() {
        return Notice;
    }

    public void setNotice(int notice) {
        Notice = notice;
    }

    public int getMail() {
        return Mail;
    }

    public void setMail(int mail) {
        Mail = mail;
    }

    public int getMeeting() {
        return Meeting;
    }

    public void setMeeting(int meeting) {
        Meeting = meeting;
    }

    public int getSchedule() {
        return Schedule;
    }

    public void setSchedule(int schedule) {
        Schedule = schedule;
    }

    public int getEvents() {
        return events;
    }

    public void setEvents(int events) {
        this.events = events;
    }

    public int getMissions() {
        return missions;
    }

    public void setMissions(int missions) {
        this.missions = missions;
    }
}
