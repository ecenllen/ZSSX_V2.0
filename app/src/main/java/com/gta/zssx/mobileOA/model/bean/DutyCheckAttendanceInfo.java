package com.gta.zssx.mobileOA.model.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lan.zheng on 2016/11/18. 登记考勤bean
 */
public class DutyCheckAttendanceInfo implements Serializable {
    private int Status;   //已检查、未检查
    private List<CheckAttendanceEntity> CheckList;

    public int getStatus(){return Status;}

    public void setStatus(int Status) {
        this.Status = Status;
    }

    public List<CheckAttendanceEntity> getCheckList(){return CheckList;}

    public void setCheckList(List<CheckAttendanceEntity> CheckList) {
        this.CheckList = CheckList;
    }

    public static class CheckAttendanceEntity{
        private int TimesId;
        private int Attendance;
        private int Tour;
        private int TourGrade;
        private int TotalGrade;

        public int getTimesId() {
            return TimesId;
        }

        public void setTimesId(int TimesId) {
            this.TimesId = TimesId;
        }

        public int getAttendance() {
            return Attendance;
        }

        public void setAttendance(int Attendance) {
            this.Attendance = Attendance;
        }

        public int getTour() {
            return Tour;
        }

        public void setTour(int Tour) {
            this.Tour = Tour;
        }

        public int getTourGrade() {
            return TourGrade;
        }

        public void setTourGradee(int TourGrade) {
            this.TourGrade = TourGrade;
        }

        public int getTotalGrade() {
            return TotalGrade;
        }

        public void setTotalGrade(int TotalGrade) {
            this.TotalGrade = TotalGrade;
        }

    }

}
