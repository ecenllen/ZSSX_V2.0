package com.gta.zssx.fun.coursedaily.registerrecord.model.DTO;

import com.gta.zssx.fun.coursedaily.registercourse.model.bean.DetailItemShowBean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lan.zheng on 2016/6/16.
 * 用于保存详细的一条已登记记录内容
 */
public class DetailRegisteredRecordDto implements Serializable {

    /**
     * CourseName : “课程名称” - 多门
     * DeptName : “财经系”
     * TeacherName : “李老师”
     * CourseTeacher：“授课老师” - 多个
     * StudentCount : 38
     * AttendCount : 30
     * LateList : [{"StudentName":"\u201c张粒\u201d","StudentNo":"\u201cG44200020000107570X\u201d"},{"StudentName":"\u201c张粒2\u201d","StudentNo":"\u201cG44200020000107570X\u201d"}]
     * TruantList : [{"StudentName":"\u201c张粒\u201d","StudentNo":"\u201cG44200020000107570X\u201d"},{"StudentName":"\u201c张粒2\u201d","StudentNo":"\u201cG44200020000107570X\u201d"}]
     * LeaveList : [{"StudentName":"\u201c张粒\u201d","StudentNo":"\u201cG44200020000107570X\u201d"},{"StudentName":"\u201c张粒2\u201d","StudentNo":"\u201cG44200020000107570X\u201d"}]
     * HolidayList : [{"StudentName":"\u201c张粒\u201d","StudentNo":"\u201cG44200020000107570X\u201d"},{"StudentName":"\u201c张粒2\u201d","StudentNo":"\u201cG44200020000107570X\u201d"}]
     * Memo : 备注
     */

//    private String CourseName; //旧
    private String DeptName;
    private String TeacherName;
//    private String CourseTeacher; //旧
    private int StudentCount;
    private int AttendCount;
    private String Memo;
    private List<DetailItemShowBean.CourseInfoBean> CourseName;
    private List<DetailItemShowBean.TeacherInfoBean> CourseTeacher;

    /**
     * StudentName : “张粒”
     * StudentNo : “G44200020000107570X”
     */

    private List<LateListBean> LateList;
    /**
     * StudentName : “张粒”
     * StudentNo : “G44200020000107570X”
     */

    private List<TruantListBean> TruantList;
    /**
     * StudentName : “张粒”
     * StudentNo : “G44200020000107570X”
     */

    private List<LeaveListBean> LeaveList;
    /**
     * StudentName : “张粒”
     * StudentNo : “G44200020000107570X”
     */

    private List<HolidayListBean> HolidayList;

    public List<DetailItemShowBean.CourseInfoBean> getCourseName() {
        return CourseName;
    }

    public void setCourseName(List<DetailItemShowBean.CourseInfoBean> CourseName) {
        this.CourseName = CourseName;
    }

    public String getDeptName() {
        return DeptName;
    }

    public void setDeptName(String DeptName) {
        this.DeptName = DeptName;
    }

    public String getTeacherName() {
        return TeacherName;
    }

    public void setTeacherName(String TeacherName) {
        this.TeacherName = TeacherName;
    }

    public int getStudentCount() {
        return StudentCount;
    }

    public void setStudentCount(int StudentCount) {
        this.StudentCount = StudentCount;
    }

    public int getAttendCount() {
        return AttendCount;
    }

    public void setAttendCount(int AttendCount) {
        this.AttendCount = AttendCount;
    }

    public String getMemo() {
        return Memo;
    }

    public void setMemo(String Memo) {
        this.Memo = Memo;
    }

    public List<DetailItemShowBean.TeacherInfoBean> getCourseTeacher(){return CourseTeacher;}

    public void setCourseTeacher(List<DetailItemShowBean.TeacherInfoBean> CourseTeacher) {
        this.CourseTeacher = CourseTeacher;
    }


    public List<LateListBean> getLateList() {
        return LateList;
    }

    public void setLateList(List<LateListBean> LateList) {
        this.LateList = LateList;
    }

    public List<TruantListBean> getTruantList() {
        return TruantList;
    }

    public void setTruantList(List<TruantListBean> TruantList) {
        this.TruantList = TruantList;
    }

    public List<LeaveListBean> getLeaveList() {
        return LeaveList;
    }

    public void setLeaveList(List<LeaveListBean> LeaveList) {
        this.LeaveList = LeaveList;
    }

    public List<HolidayListBean> getHolidayList() {
        return HolidayList;
    }

    public void setHolidayList(List<HolidayListBean> HolidayList) {
        this.HolidayList = HolidayList;
    }

    public static class LateListBean {
        private String StudentName;
        private String StudentNo;

        public String getStudentName() {
            return StudentName;
        }

        public void setStudentName(String StudentName) {
            this.StudentName = StudentName;
        }

        public String getStudentNo() {
            return StudentNo;
        }

        public void setStudentNo(String StudentNo) {
            this.StudentNo = StudentNo;
        }
    }

    public static class TruantListBean {
        private String StudentName;
        private String StudentNo;

        public String getStudentName() {
            return StudentName;
        }

        public void setStudentName(String StudentName) {
            this.StudentName = StudentName;
        }

        public String getStudentNo() {
            return StudentNo;
        }

        public void setStudentNo(String StudentNo) {
            this.StudentNo = StudentNo;
        }
    }

    public static class LeaveListBean {
        private String StudentName;
        private String StudentNo;

        public String getStudentName() {
            return StudentName;
        }

        public void setStudentName(String StudentName) {
            this.StudentName = StudentName;
        }

        public String getStudentNo() {
            return StudentNo;
        }

        public void setStudentNo(String StudentNo) {
            this.StudentNo = StudentNo;
        }
    }

    public static class HolidayListBean {
        private String StudentName;
        private String StudentNo;

        public String getStudentName() {
            return StudentName;
        }

        public void setStudentName(String StudentName) {
            this.StudentName = StudentName;
        }

        public String getStudentNo() {
            return StudentNo;
        }

        public void setStudentNo(String StudentNo) {
            this.StudentNo = StudentNo;
        }
    }
}
