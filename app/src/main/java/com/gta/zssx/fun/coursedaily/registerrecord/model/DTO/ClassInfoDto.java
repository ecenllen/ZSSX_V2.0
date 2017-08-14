package com.gta.zssx.fun.coursedaily.registerrecord.model.DTO;

import java.io.Serializable;

/**
 * Created by lan.zheng on 2016/6/28.
 * 用于封装去服务器获取的请求，已登记页面获取服务器和确认签名页获取服务器
 */
public class ClassInfoDto implements Serializable {
    private String ClassID;
    private String SignDate;
    private String TeacherID;
    private String ClassName;
    private boolean IsFromClassLogMainpage;
    private boolean IsLogStatisticeInto;
    private int SectionID;

    public boolean isLogStatisticeInto() {
        return IsLogStatisticeInto;
    }

    public void setLogStatisticeInto(boolean logStatisticeInto) {
        IsLogStatisticeInto = logStatisticeInto;
    }

    public boolean isFromClassLogMainpage() {
        return IsFromClassLogMainpage;
    }

    public void setFromClassLogMainpage(boolean fromClassLogMainpage) {
        IsFromClassLogMainpage = fromClassLogMainpage;
    }

    public String getClassID() {
        return ClassID;
    }

    public void setClassID(String ClassID) {
        this.ClassID = ClassID;
    }

    public String getSignDate() {
        return SignDate;
    }

    public void setSignDate(String SignDate) {
        this.SignDate = SignDate;
    }

    public String getTeacherID() {
        return TeacherID;
    }

    public void setTeacherID(String TeacherID) {
        this.TeacherID = TeacherID;
    }

    public String getClassName() {
        return ClassName;
    }

    public void setClassName(String ClassName) {
        this.ClassName = ClassName;
    }

    public boolean getIsFromClassLogMainpage() {
        return IsFromClassLogMainpage;
    }

    public void setIsFromClassLogMainpage(boolean IsFromClassLogMainpage) {
        this.IsFromClassLogMainpage = IsFromClassLogMainpage;
    }

    public int getSectionID() {
        return SectionID;
    }

    public void setSectionID(int SectionID) {
        this.SectionID = SectionID;
    }

}
