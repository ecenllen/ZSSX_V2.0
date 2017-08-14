package com.gta.zssx.fun.coursedaily.registerrecord.model.DTO;

import android.util.Log;

import com.gta.utils.resource.L;
import com.gta.zssx.fun.coursedaily.registercourse.model.bean.DetailItemShowBean;
import com.gta.zssx.fun.coursedaily.registercourse.model.bean.SectionBean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lan.zheng on 2016/6/22.
 * 用于一天内的已登记记录粗略内容
 */
public class RegisteredRecordFromSignatureDto implements Serializable {
    private int ClassID;
    private String ClassName;
    private String SignInfo;
    private String CourseName;  //课程名字 -旧 - 废弃
    private int SectionID; //节次ID - 旧 - 废弃
    private int LateCount;
    private int LeaveCount;
    private int TruantCount;
    private int HolidayCount;
    private String CourseTeacher;  //授课教师 - 旧 - 废弃
    private String TeacherId;
    private String SignDate;
    private int Score; //得分
    private String Remark; //备注
    private List<DetailItemShowBean.TeacherInfoBean> MultipleTeachersList; //授课教师 - 新
    private List<DetailItemShowBean.CourseInfoBean> MultipleCoursesList;  //课程名字 - 新
    private SectionBean Section;  //节次信息 - 新

    public String getTeacherId() {
        return TeacherId;
    }

    public void setTeacherId(String teacherId) {
        TeacherId = teacherId;
    }

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String remark) {
        Remark = remark;
    }

    public String getSignDate() {
        return SignDate;
    }

    public void setSignDate(String signDate) {
        SignDate = signDate;
    }

    public int getClassID() {
        return ClassID;
    }

    public void setClassID(int ClassID) {
        this.ClassID = ClassID;
    }

    public String getSignInfo() {
        return SignInfo;
    }

    public void setSignInfo(String SignInfo) {
        this.SignInfo = SignInfo;
    }

    public int getSectionID() {
        return SectionID;
    }

    public void setSectionID(int SectionID) {
        this.SectionID = SectionID;
    }

    public String getClassName() {
        return ClassName;
    }

    public void setClassName(String ClassName) {
        this.ClassName = ClassName;
    }

    public String getCourseName() {
        return CourseName;
    }

    public void setCourseName(String CourseName) {
        this.CourseName = CourseName;
    }

    public int getLateCount() {
        return LateCount;
    }

    public void setLateCount(int LateCount) {
        this.LateCount = LateCount;
    }

    public int getLeaveCount() {
        return LeaveCount;
    }

    public void setLeaveCount(int LeaveCount) {
        this.LeaveCount = LeaveCount;
    }

    public int getTruantCount() {
        return TruantCount;
    }

    public void setTruantCount(int TruantCount) {
        this.TruantCount = TruantCount;
    }

    public int getHolidayCount() {
        return HolidayCount;
    }

    public void setHolidayCount(int HolidayCount) {
        this.HolidayCount = HolidayCount;
    }

    public int getScore() {
        return Score;
    }

    public void setScore(int Score) {
        this.Score = Score;
    }

    public String getCourseTeacher(){return CourseTeacher;}

    public void setCourseTeacher(String CourseTeacher) {
        this.CourseTeacher = CourseTeacher;
    }

    public SectionBean getSection(){
        return Section;
    }

    public void setSection(SectionBean section){
        Section = section;
    }

    public List<DetailItemShowBean.TeacherInfoBean> getMultipleTeachersList(){
        return MultipleTeachersList;
    }

    public void setMultipleTeachersList(List<DetailItemShowBean.TeacherInfoBean> teachersList){
        MultipleTeachersList = teachersList;
    }

    public List<DetailItemShowBean.CourseInfoBean> getMultipleCoursesList(){
        return MultipleCoursesList;
    }

    public void setMultipleCoursesList(List<DetailItemShowBean.CourseInfoBean> coursesList){
        MultipleCoursesList = coursesList;
    }

    //判断两个对象是否一样
    @Override
    public boolean equals(Object o){
        if(((RegisteredRecordFromSignatureDto)o).getSection().getSectionId() == this.getSection().getSectionId()){
            Log.e("lenita","equals()");
            return true;
        }else{
            return false;
        }
    }

    @Override
    public int hashCode(){
        return 0;
    }
}
