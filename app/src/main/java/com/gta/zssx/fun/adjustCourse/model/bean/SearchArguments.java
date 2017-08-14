package com.gta.zssx.fun.adjustCourse.model.bean;

import java.io.Serializable;

/**
 * [Description]
 * <p>
 * [How to use]
 * <p>
 * [Tips]
 *
 * @author Created by Zhimin.Huang on 2016/11/4.
 * @since 1.0.0
 */
public class SearchArguments implements Serializable {

    //用于判断是教师课程表还是班级课程表
    private int flag;
    //学期id
    private String semesterId;
    //班级id
    private String classId;
    //教师id，加密后id
    private String teacherUUId;
    //日期
    private String date;
    //判断是不是从搜索进入的课程表，用于确定的时候返回判断
    private boolean isFromSearch;
    //用了fragment界面跳转
    private int page;
    //课表名称
    private String scheduleName;
    //申请人选择的课程数量
    private int courseCount;
    //用于课表查询跳转标记选中的节次
    private String key;
    //记录日期对应的列
    private int weekIndex;
    //用于记录一个星期对应的日期
    private String days[];

    //教室类型
    private int roomParams;
    //教师id
    private int roomId;
    //上课方式
    private int courseType;


    public int getRoomParams() {
        return roomParams;
    }

    public void setRoomParams(int roomParams) {
        this.roomParams = roomParams;
    }

    public int getRoomId() {
        return roomId;
    }

    public void setRoomId(int roomId) {
        this.roomId = roomId;
    }

    public int getCourseType() {
        return courseType;
    }

    public void setCourseType(int courseType) {
        this.courseType = courseType;
    }

    public String[] getDays() {
        return days;
    }

    public void setDays(String[] days) {
        this.days = days;
    }

    public int getWeekIndex() {
        return weekIndex;
    }

    public void setWeekIndex(int weekIndex) {
        this.weekIndex = weekIndex;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public int getCourseCount() {
        return courseCount;
    }

    public void setCourseCount(int courseCount) {
        this.courseCount = courseCount;
    }

    public String getScheduleName() {
        return scheduleName;
    }

    public void setScheduleName(String scheduleName) {
        this.scheduleName = scheduleName;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public boolean isFromSearch() {
        return isFromSearch;
    }

    public void setFromSearch(boolean fromSearch) {
        isFromSearch = fromSearch;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public String getSemesterId() {
        return semesterId;
    }

    public void setSemesterId(String semesterId) {
        this.semesterId = semesterId;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String getTeacherUUId() {
        return teacherUUId;
    }

    public void setTeacherUUId(String teacherUUId) {
        this.teacherUUId = teacherUUId;
    }
}
