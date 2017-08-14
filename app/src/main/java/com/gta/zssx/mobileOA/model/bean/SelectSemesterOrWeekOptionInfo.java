package com.gta.zssx.mobileOA.model.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lan.zheng on 2016/11/7.  用于学期选择和周期选择
 */
public class SelectSemesterOrWeekOptionInfo {
    private List<String> semesterList;
    private List<String> weekList;
    private String currentSemester;
    private String currentWeek;

    public List<String> getSemesterList() {
        return semesterList;
    }

    public void setSemesterList(List<String> semesterList) {
        this.semesterList = semesterList;
    }

    public List<String> getWeekList() {
        return weekList;
    }

    public void setWeekList(List<String> weekList) {
        this.weekList = weekList;
    }

    public String getCurrentSemester() {
        return currentSemester;
    }

    public void setCurrentSemester(String currentSemester) {
        this.currentSemester = currentSemester;
    }

    public String getCurrentWeek() {
        return currentWeek;
    }

    public void setCurrentWeek(String currentWeek) {
        this.currentWeek = currentWeek;
    }

    /*public static class Week{
        private String weekName;

        public String getWeekName() {
            return weekName;
        }

        public void setWeekName(String weekName) {
            this.weekName = weekName;
        }
    }*/


}
