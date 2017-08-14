package com.gta.zssx.fun.coursedaily.registercourse.model.bean;
import java.util.List;

/**
 * Created by lan.zheng on 2017/3/21.
 */
public class SubmitSignInfoBean {

        /**
         * 原始班级ID
         */
        private int OriginalClassID;
        /**
         * 原始节次ID
         */
        private int OriginalSectionID;
        /**
         * 原始登记日期
         */
        private String OriginalSignDate;
        /**
         * 状态,0新增，1修改
         */
        private int Type;

        public int getOriginalClassID() {
            return OriginalClassID;
        }

        public void setOriginalClassID(int originalClassID) {
            OriginalClassID = originalClassID;
        }

        public int getOriginalSectionID() {
            return OriginalSectionID;
        }

        public void setOriginalSectionID(int originalSectionID) {
            OriginalSectionID = originalSectionID;
        }

        public String getOriginalSignDate() {
            return OriginalSignDate;
        }

        public void setOriginalSignDate(String originalSignDate) {
            OriginalSignDate = originalSignDate;
        }

        public int getType() {
            return Type;
        }

        public void setType(int type) {
            Type = type;
        }

        public static int ADD = 0;
        public static int MODIFY = 1;

        /**
         * ClassID : 123
         */
        private int ClassID;   //登记班级Id
        /**
         * SignDate : 2016-06-01
         */
        private String SignDate;  //登记日期
        /**
         * TeacherID：test
         */
        private String TeacherID; //登记老师ID

        public int getClassID() {
            return ClassID;
        }
        public void setClassID(int ClassID) {
            this.ClassID = ClassID;
        }

        public String getSignDate() {
            return SignDate;
        }

        public void setSignDate(String SignData) {
            this.SignDate = SignData;
        }

        public String getTeacherID() {
            return TeacherID;
        }

        public void setTeacherID(String TeacherID) {
            this.TeacherID = TeacherID;
        }
        /**
         * 节次详细信息
         */
        private List<SectionBean> Section;


        public List<SectionBean> getSectionBean(){
            return Section;
        }

        public void setSectionBean(List<SectionBean> Section){
            this.Section = Section;
        }


        /**
         * 一个节次
         */
        public static class SectionBean {
            /**
             * TeacherID;  //录入登记老师Id
             * SectionID : 不是1，2，3，4，5
             * CourseName : 课程名称
             * Score : 97
             * Remark : 备注
             * MultipleCoursesList：课程列表
             * MultipleTeachersList：教师列表
             * StudentMarkDetails：该节次对应的所有学生状态列表
             */
            private String TeacherId;  //录入登记老师Id
            private int SectionID;  //
            private String CourseName;  //语文等多门课程
            private int Score;
            private String Remark="";
            private List<DetailItemShowBean.TeacherInfoBean> MultipleTeachersList;
            private List<DetailItemShowBean.CourseInfoBean> MultipleCoursesList;
            private List<StudentListNewBean> StudentMarkDetails;

            public int getSectionID() {
                return SectionID;
            }

            public void setSectionID(int SectionID) {
                this.SectionID = SectionID;
            }

            public String getTeacherId() {
                return TeacherId;
            }

            public void setTeacherId(String TeacherId) {
                this.TeacherId = TeacherId;
            }


            public String getCourseName() {
                return CourseName;
            }

            public void setCourseName(String CourseName) {
                this.CourseName = CourseName;
            }

            public int getScore() {
                return Score;
            }

            public void setScore(int Score) {
                this.Score = Score;
            }

            public String getRemark() {
                return Remark;
            }

            public void setRemark(String Remark) {
                this.Remark = Remark;
            }

            public List<DetailItemShowBean.TeacherInfoBean> getMultipleTeachersList(){
                return MultipleTeachersList;
            }

            public void setMultipleTeachersList(List<DetailItemShowBean.TeacherInfoBean> MultipleTeachersList){
                this.MultipleTeachersList = MultipleTeachersList;
            }

            public List<DetailItemShowBean.CourseInfoBean> getMultipleCoursesList(){
                return MultipleCoursesList;
            }

            public void setMultipleCoursesList(List<DetailItemShowBean.CourseInfoBean> MultipleCoursesList){
                this.MultipleCoursesList = MultipleCoursesList;
            }

            public List<StudentListNewBean> getStudentMarkDetails(){
                return StudentMarkDetails;
            }

            public void setStudentMarkDetails(List<StudentListNewBean> StudentMarkDetails){
                this.StudentMarkDetails = StudentMarkDetails;
            }

    /*    public static class StudentBean{
            private int MarkType;
            private String StudentID;

            public int getMarkType() {
                return MarkType;
            }

            public void setMarkType(int MarkType) {
                this.MarkType = MarkType;
            }

            public String getStudentID() {
                return StudentID;
            }

            public void setStudentID(String StudentID) {
                this.StudentID = StudentID;
            }
        }
            //新增
    public static final int ADD = 0;
    //修改
    public static final int MODIFY = 1;*/
        }
}
