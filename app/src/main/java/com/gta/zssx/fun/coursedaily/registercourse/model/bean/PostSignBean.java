package com.gta.zssx.fun.coursedaily.registercourse.model.bean;

import java.io.Serializable;
import java.util.List;

/**
 * [Description]
 * <p>
 * [How to use]
 * <p>
 * [Tips]
 *
 * @author Created by Zhimin.Huang on 2016/6/21.
 * @since 1.0.0
 */
public class PostSignBean implements Serializable {


    /**
     * 原始班级ID
     */
    private int OriginalClassID;


    /**
     * 原始节次ID
     */
    private int OriginalSectionID;

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

    public int getType() {
        return Type;
    }

    public void setType(int type) {
        Type = type;
    }

    public String getOriginalSignDate() {
        return OriginalSignDate;
    }

    public void setOriginalSignDate(String originalSignDate) {
        OriginalSignDate = originalSignDate;
    }

    /**
     * 状态,0新增，1修改
     */
    private int Type;

    public static int ADD = 0;
    public static int MODIFY = 1;


    /**
     * 原始登记日期
     */
    private String OriginalSignDate;
    /**
     * ClassID : 123
     * SignDate : 2016-06-01
     * TeacherID : bdf3e816-bb5b-4539-a163-fc35ffc27032
     * Section : [{"SectionID":1,"CourseName":"课程名称","Score":97,"Remark":"备注"},{"SectionID":2,"CourseName":"课程名称","Score":97,"Remark":"备注"}]
     * Detail : [{"StudentID":"bdf3e816-bb5b-4539-a163-fc35ffc27032","StundentName":"蔡晓雯","StudentNO":"G44200020000107570X","LESSON1":1,"LESSON2":1,"LESSON3":2,"LESSON4":3,"LESSON5":4,"LESSON6":1,"LESSON7":2},{"StudentID":"bdf3e816-bb5b-4539-a163-fc35ffc27032","StundentName":"蔡晓雯","StudentNO":"G44200020000107570X","LESSON1":1,"LESSON2":1,"LESSON3":2,"LESSON4":3,"LESSON5":4,"LESSON6":1,"LESSON7":2}]
     */

    private int ClassID;
    private String SignDate;
    private String TeacherID;
    /**
     * SectionID : 1
     * CourseName : 课程名称
     * Score : 97
     * Remark : 备注
     */

    private List<SectionBean> Section;
    /**
     * StudentID : bdf3e816-bb5b-4539-a163-fc35ffc27032
     * StundentName : 蔡晓雯
     * StudentNO : G44200020000107570X
     * LESSON1 : 1
     * LESSON2 : 1
     * LESSON3 : 2
     * LESSON4 : 3
     * LESSON5 : 4
     * LESSON6 : 1
     * LESSON7 : 2
     */

    private List<StudentListBean> Detail;

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

    public List<SectionBean> getSection() {
        return Section;
    }

    public void setSection(List<SectionBean> Section) {
        this.Section = Section;
    }

    public List<StudentListBean> getDetail() {
        return Detail;
    }

    public void setDetail(List<StudentListBean> Detail) {
        this.Detail = Detail;
    }

    public static class SectionBean {
        private int SectionID;
        private String CourseName;
        private int Score;
        private String Remark="";

        public int getSectionID() {
            return SectionID;
        }

        public void setSectionID(int SectionID) {
            this.SectionID = SectionID;
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
    }


}
