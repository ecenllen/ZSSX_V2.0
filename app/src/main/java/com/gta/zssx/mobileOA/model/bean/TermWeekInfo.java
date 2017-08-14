package com.gta.zssx.mobileOA.model.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lan.zheng on 2016/11/19.
 */
public class TermWeekInfo implements Serializable {
    private int SemesterId;
    private String SemesterName;
    private int CurrentWeek;
    private List<WeekEntity> Semester;

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

    public int getCurrentWeek() {
        return CurrentWeek;
    }

    public void setCurrentWeek(int CurrentWeek) {
        this.CurrentWeek = CurrentWeek;
    }

    public List<WeekEntity> getSemester() {
        return Semester;
    }

    public void setSemester(List<WeekEntity> Semester) {
        this.Semester = Semester;
    }

    public static class WeekEntity{
        private int Week;
        private String WeekStartDate;
        private String WeekEndDate;

        public int getWeek() {
            return Week;
        }

        public void setWeek(int Week) {
            this.Week = Week;
        }

        public String getWeekStartDate() {
            return WeekStartDate;
        }

        public void setWeekStartDate(String WeekStartDate) {
            this.WeekStartDate = WeekStartDate;
        }

        public String getWeekEndDate() {
            return WeekEndDate;
        }

        public void setWeekEndDate(String WeekEndDate) {
            this.WeekEndDate = WeekEndDate;
        }
    }

}
