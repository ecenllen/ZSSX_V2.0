package com.gta.zssx.mobileOA.model.bean;

import java.io.Serializable;

/**
 * Created by lan.zheng on 2016/11/19.
 */
public class TermInfo implements Serializable{
    private int SemesterId;
    private String SemesterName;
    private String SemesterStartDate;
    private String SemesterEndDate;
    private boolean IsCurrentSemester;


    public int getSemesterId() {
        return SemesterId;
    }

    public void setSemesterId(int SemesterId) {
        this.SemesterId = SemesterId;
    }

    public String getSemesterName() {
        return SemesterName;
    }

    public void setSemesterName(String SemesterName) {
        this.SemesterName = SemesterName;
    }

    public String getSemesterStartDate() {
        return SemesterStartDate;
    }

    public void setSemesterStartDate(String SemesterStartDate) {
        this.SemesterStartDate = SemesterStartDate;
    }

    public String getSemesterEndDate() {
        return SemesterEndDate;
    }

    public void setSemesterEndDate(String SemesterEndDate) {
        this.SemesterEndDate = SemesterEndDate;
    }

    public boolean getIsCurrentSemester() {
        return IsCurrentSemester;
    }

    public void setIsCurrentSemester(boolean IsCurrentSemester) {
        this.IsCurrentSemester = IsCurrentSemester;
    }
}
