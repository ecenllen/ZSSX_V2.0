package com.gta.zssx.mobileOA.model.bean;
/**
 * 实体类-首页的更新数据，显示各项的最新待办或未读等的数字
 * @author bin.wang1
 *
 */

public class HomeInfo {
	
	private boolean Successed;
	private String ErrorMsg;
	
	private String Tasks;
	private String Notice;
	private String Mail;
	private String Meeting;
	private int Record;//用于会议	
	private String Schedule;   //日程 <=>ScheduleCount
	
	private String SchUpdateTime;
	
	private long TasksId;   //ID   越大越新
	private long NoticeId;
	private long MailId;
	private long MeetingId;
	private long RecordId;
	private long ScheduleId;
	public boolean isSuccessed() {
		return Successed;
	}
	public void setSuccessed(boolean successed) {
		Successed = successed;
	}
	public String getErrorMsg() {
		return ErrorMsg;
	}
	public void setErrorMsg(String errorMsg) {
		ErrorMsg = errorMsg;
	}
	public String getTasks() {
		return Tasks;
	}
	public void setTasks(String tasks) {
		Tasks = tasks;
	}
	public String getNotice() {
		return Notice;
	}
	public void setNotice(String notice) {
		Notice = notice;
	}
	public String getMail() {
		return Mail;
	}
	public void setMail(String mail) {
		Mail = mail;
	}
	public String getMeeting() {
		return Meeting;
	}
	public void setMeeting(String meeting) {
		Meeting = meeting;
	}
	public String getSchedule() {
		return Schedule;
	}
	public void setSchedule(String schedule) {
		Schedule = schedule;
	}
	public String getSchUpdateTime() {
		return SchUpdateTime;
	}
	public void setSchUpdateTime(String schUpdateTime) {
		SchUpdateTime = schUpdateTime;
	}
	public long getTasksId() {
		return TasksId;
	}
	public void setTasksId(long tasksId) {
		TasksId = tasksId;
	}
	public long getNoticeId() {
		return NoticeId;
	}
	public void setNoticeId(long noticeId) {
		NoticeId = noticeId;
	}
	public long getMailId() {
		return MailId;
	}
	public void setMailId(long mailId) {
		MailId = mailId;
	}
	public long getMeetingId() {
		return MeetingId;
	}
	public void setMeetingId(long meetingId) {
		MeetingId = meetingId;
	}
	public long getScheduleId() {
		return ScheduleId;
	}
	public void setScheduleId(long scheduleId) {
		ScheduleId = scheduleId;
	}
	public long getRecordId() {
		return RecordId;
	}
	public void setRecordId(long recordId) {
		RecordId = recordId;
	}
	public int getRecord() {
		return Record;
	}
	public void setRecord(int record) {
		Record = record;
	}

	
}
