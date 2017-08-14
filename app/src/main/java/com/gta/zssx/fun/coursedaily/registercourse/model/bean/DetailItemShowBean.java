package com.gta.zssx.fun.coursedaily.registercourse.model.bean;

import android.util.Log;

import java.io.Serializable;
import java.util.Set;

/**
 * Created by lan.zheng on 2017/2/27.
 * 用于本地保存所有课程、教师、节次信息
 */
public class DetailItemShowBean implements Serializable {

    //拿到教师、节次、课程的列表，可以多课程多节次多教师
//    public Set<SectionBean> mSectionInfoBean;
    private Set<CourseInfoBean> mCourseInfoBean;
    private Set<TeacherInfoBean> mTeacherInfoBean;

    public void setCourseInfoBean(Set<CourseInfoBean> courseInfoBean) {
        this.mCourseInfoBean = courseInfoBean;
    }

    public Set<CourseInfoBean> getCourseInfoBean() {
        return mCourseInfoBean;
    }

    public void setTeacherInfoBean(Set<DetailItemShowBean.TeacherInfoBean> teacherInfoBean) {
        this.mTeacherInfoBean = teacherInfoBean;
    }

    public Set<DetailItemShowBean.TeacherInfoBean> getTeacherInfoBean() {
        return mTeacherInfoBean;
    }

    /*public static class SectionInfoBean implements Serializable{
        private String mSectionName;  //节次名称

        //判断两个对象是否一样
        public boolean equals(Object o){
            if(((SectionInfoBean)o).getSectionName().equals(this.getSectionName())){
                return true;
            }else{
                return false;
            }
        }
        public int hashCode(){
            return 0;
        }

        public String getSectionName() {
            return mSectionName;
        }

        public void setSectionName(String mSectionName) {
            this.mSectionName = mSectionName;
        }
    }*/

    //课程的bean,用于上传
    public static class CourseInfoBean implements Serializable{
        private String CourseName;  //课程名称
        private String CourseId;    //课程Id
        private String CourseCode;  //课程码

        //判断两个对象是否一样
        @Override
        public boolean equals(Object o){
            return ((CourseInfoBean) o).getCourseId ().equals (this.getCourseId ());
        }

        @Override
        public int hashCode(){
            return 0;
        }

        public String getCourseName() {
            return CourseName;
        }

        public void setCourseName(String mCourseName) {
            this.CourseName = mCourseName;
        }

        public String getCourseId() {
            return CourseId;
        }

        public void setCourseId(String mCourseId) {
            this.CourseId = mCourseId;
        }

        public String getCourseCode() {
            return CourseCode;
        }

        public void setCourseCode(String mCourseCode) {
            this.CourseCode = mCourseCode;
        }
    }


    //教师的bean，用于上传
    public static class TeacherInfoBean implements Serializable{
        private String TeacherName;
        private String TeacherId;
        private String TeacherNo;
        private String TeacherGuid;

        //判断两个对象是否一样
        @Override
        public boolean equals(Object o){
            Log.e("lenita","equals()");
            if(((TeacherInfoBean)o).getTeacherId().equals(this.getTeacherId())){
                return true;
            }else{
                return false;
            }
        }

        @Override
        public int hashCode(){
            return 0;
        }

        public String getTeacherName() {
            return TeacherName;
        }

        public void setTeacherName(String mTeacherName) {
            this.TeacherName = mTeacherName;
        }

        public String getTeacherId() {
            return TeacherId;
        }

        public void setTeacherId(String mTeacherId) {
            this.TeacherId = mTeacherId;
        }

        public String getTeacherNo() {
            return TeacherNo;
        }

        public void setTeacherNo(String mTeacherNo) {
            this.TeacherNo = mTeacherNo;
        }

        public String getTeacherGuid() {
            return TeacherGuid;
        }

        public void setTeacherGuid(String TeacherGuid) {
            this.TeacherGuid = TeacherGuid;
        }
    }
}
