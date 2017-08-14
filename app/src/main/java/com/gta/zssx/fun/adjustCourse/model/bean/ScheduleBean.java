package com.gta.zssx.fun.adjustCourse.model.bean;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * [Description]
 * <p>
 * [How to use]
 * <p>
 * [Tips]
 *
 * @author Created by Zhimin.Huang on 2016/10/31.
 * @since 1.0.0
 */
public class ScheduleBean implements Serializable {


    private Map<String, List<ScheduleBean.SectionBean>> courseListMap;

    public Map<String, List<ScheduleBean.SectionBean>> getCourseListMap() {
        return courseListMap;
    }

    public void setCourseListMap(Map<String, List<ScheduleBean.SectionBean>> courseListMap) {
        this.courseListMap = courseListMap;
    }

    /**
     * maxUnit : 8
     * isSaturdayHasCourse : false
     * weekUnit : []
     */


    private int maxUnit;
    private boolean isSaturdayHasCourse;
    private List<String> weekUnit;

    public int getMaxUnit() {
        return maxUnit;
    }

    public void setMaxUnit(int maxUnit) {
        this.maxUnit = maxUnit;
    }

    public boolean isIsSaturdayHasCourse() {
        return isSaturdayHasCourse;
    }

    public void setIsSaturdayHasCourse(boolean isSaturdayHasCourse) {
        this.isSaturdayHasCourse = isSaturdayHasCourse;
    }

    public List<String> getWeekUnit() {
        return weekUnit;
    }

    public void setWeekUnit(List<String> weekUnit) {
        this.weekUnit = weekUnit;
    }

    public static class SectionBean implements Serializable {


        /**
         * className : 15汽车营销
         * teachcourseCode : 110005
         * teachCourse : 体育
         * viewEnableFlag : 0
         * periodTypeId : 0
         * departName : 汽技部
         * teacherID : 1
         * roomParam : 3
         * listClass : []
         * buildingID : 0
         * isMultiClassFlag : 0
         * isMultiTeacherFlag : 0
         * giveCourseName :
         * buildingShortName : null
         * classroomName : null
         * openCourseType : 1
         * openCourseTypeName : 阶段一
         * classroomId : 0
         * schedulingIdStr : null
         * schedulingId : 275
         * teacherName : 李秋莲
         * classIdsStr :
         * teacherIdsStr : null
         * teachClassId : 49
         * transferType : null
         * isMultiFlag : 0
         * classId : 108
         * courseId : 92
         * teacherNo : T10047
         * listTeacher : []
         * adjustFag : null
         * courseName : 语文（基础模块）（下册）(理论)
         */

        private String className;
        private String teachcourseCode;
        private String teachCourse;
        private int viewEnableFlag;
        private int periodTypeId;
        private String departName;
        private String teacherID;
        private int roomParam;
        private int buildingID;
        private int isMultiClassFlag;
        private int isMultiTeacherFlag;
        private String giveCourseName;
        private String buildingShortName;
        private String classroomName;
        private int openCourseType;
        private String openCourseTypeName;
        private int classroomId;
        private String schedulingIdStr;
        private int schedulingId;
        private String teacherName;
        private String classIdsStr;
        private Object teacherIdsStr;
        private int teachClassId;
        private String transferType;
        private int isMultiFlag;
        private int classId;
        private int courseId;
        private String teacherNo;
        private Object adjustFag;
        private String courseName;
        private List<ClassBean> listClass;
        private List<TeacherBean> listTeacher;
        private String mDateString;
        private String key;
        private int numberTeacher;
        private int numberClass;
        private int numberCourse;

        public int getNumberTeacher() {
            return numberTeacher;
        }

        public void setNumberTeacher(int numberTeacher) {
            this.numberTeacher = numberTeacher;
        }

        public int getNumberClass() {
            return numberClass;
        }

        public void setNumberClass(int numberClass) {
            this.numberClass = numberClass;
        }

        public int getNumberCourse() {
            return numberCourse;
        }

        public void setNumberCourse(int numberCourse) {
            this.numberCourse = numberCourse;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getDateString() {
            return mDateString;
        }

        public void setDateString(String dateString) {
            mDateString = dateString;
        }

        public String getClassName() {
            return className;
        }

        public void setClassName(String className) {
            this.className = className;
        }

        public String getTeachcourseCode() {
            return teachcourseCode;
        }

        public void setTeachcourseCode(String teachcourseCode) {
            this.teachcourseCode = teachcourseCode;
        }

        public String getTeachCourse() {
            return teachCourse;
        }

        public void setTeachCourse(String teachCourse) {
            this.teachCourse = teachCourse;
        }

        public int getViewEnableFlag() {
            return viewEnableFlag;
        }

        public void setViewEnableFlag(int viewEnableFlag) {
            this.viewEnableFlag = viewEnableFlag;
        }

        public int getPeriodTypeId() {
            return periodTypeId;
        }

        public void setPeriodTypeId(int periodTypeId) {
            this.periodTypeId = periodTypeId;
        }

        public String getDepartName() {
            return departName;
        }

        public void setDepartName(String departName) {
            this.departName = departName;
        }

        public String getTeacherID() {
            return teacherID;
        }

        public void setTeacherID(String teacherID) {
            this.teacherID = teacherID;
        }

        public int getRoomParam() {
            return roomParam;
        }

        public void setRoomParam(int roomParam) {
            this.roomParam = roomParam;
        }

        public int getBuildingID() {
            return buildingID;
        }

        public void setBuildingID(int buildingID) {
            this.buildingID = buildingID;
        }

        public int getIsMultiClassFlag() {
            return isMultiClassFlag;
        }

        public void setIsMultiClassFlag(int isMultiClassFlag) {
            this.isMultiClassFlag = isMultiClassFlag;
        }

        public int getIsMultiTeacherFlag() {
            return isMultiTeacherFlag;
        }

        public void setIsMultiTeacherFlag(int isMultiTeacherFlag) {
            this.isMultiTeacherFlag = isMultiTeacherFlag;
        }

        public String getGiveCourseName() {
            return giveCourseName;
        }

        public void setGiveCourseName(String giveCourseName) {
            this.giveCourseName = giveCourseName;
        }

        public Object getBuildingShortName() {
            return buildingShortName;
        }

        public void setBuildingShortName(String buildingShortName) {
            this.buildingShortName = buildingShortName;
        }

        public Object getClassroomName() {
            return classroomName;
        }

        public void setClassroomName(String classroomName) {
            this.classroomName = classroomName;
        }

        public int getOpenCourseType() {
            return openCourseType;
        }

        public void setOpenCourseType(int openCourseType) {
            this.openCourseType = openCourseType;
        }

        public String getOpenCourseTypeName() {
            return openCourseTypeName;
        }

        public void setOpenCourseTypeName(String openCourseTypeName) {
            this.openCourseTypeName = openCourseTypeName;
        }

        public int getClassroomId() {
            return classroomId;
        }

        public void setClassroomId(int classroomId) {
            this.classroomId = classroomId;
        }

        public Object getSchedulingIdStr() {
            return schedulingIdStr;
        }

        public void setSchedulingIdStr(String schedulingIdStr) {
            this.schedulingIdStr = schedulingIdStr;
        }

        public int getSchedulingId() {
            return schedulingId;
        }

        public void setSchedulingId(int schedulingId) {
            this.schedulingId = schedulingId;
        }

        public String getTeacherName() {
            return teacherName;
        }

        public void setTeacherName(String teacherName) {
            this.teacherName = teacherName;
        }

        public String getClassIdsStr() {
            return classIdsStr;
        }

        public void setClassIdsStr(String classIdsStr) {
            this.classIdsStr = classIdsStr;
        }

        public Object getTeacherIdsStr() {
            return teacherIdsStr;
        }

        public void setTeacherIdsStr(Object teacherIdsStr) {
            this.teacherIdsStr = teacherIdsStr;
        }

        public int getTeachClassId() {
            return teachClassId;
        }

        public void setTeachClassId(int teachClassId) {
            this.teachClassId = teachClassId;
        }

        public String getTransferType() {
            return transferType;
        }

        public void setTransferType(String transferType) {
            this.transferType = transferType;
        }

        public int getIsMultiFlag() {
            return isMultiFlag;
        }

        public void setIsMultiFlag(int isMultiFlag) {
            this.isMultiFlag = isMultiFlag;
        }

        public int getClassId() {
            return classId;
        }

        public void setClassId(int classId) {
            this.classId = classId;
        }

        public int getCourseId() {
            return courseId;
        }

        public void setCourseId(int courseId) {
            this.courseId = courseId;
        }

        public String getTeacherNo() {
            return teacherNo;
        }

        public void setTeacherNo(String teacherNo) {
            this.teacherNo = teacherNo;
        }

        public Object getAdjustFag() {
            return adjustFag;
        }

        public void setAdjustFag(Object adjustFag) {
            this.adjustFag = adjustFag;
        }

        public String getCourseName() {
            return courseName;
        }

        public void setCourseName(String courseName) {
            this.courseName = courseName;
        }

        public List<ClassBean> getListClass() {
            return listClass;
        }

        public void setListClass(List<ClassBean> listClass) {
            this.listClass = listClass;
        }

        public List<TeacherBean> getListTeacher() {
            return listTeacher;
        }

        public void setListTeacher(List<TeacherBean> listTeacher) {
            this.listTeacher = listTeacher;
        }

        public static class ClassBean implements Serializable {
            private String id;
            private int deptId;
            private int grade;
            private int stuCount;
            private int status;
            private String className;
            private String deptName;

            public String getId() {
                return id;
            }

            public int getDeptId() {
                return deptId;
            }

            public int getGrade() {
                return grade;
            }

            public int getStuCount() {
                return stuCount;
            }

            public int getStatus() {
                return status;
            }

            public String getClassName() {
                return className;
            }

            public String getDeptName() {
                return deptName;
            }
        }

        public static class TeacherBean implements Serializable{
            String teacherName;
            String teacherUUID;
            int teacherId;

        }
    }

}
