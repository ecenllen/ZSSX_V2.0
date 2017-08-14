package com.gta.zssx.fun.coursedaily.registercourse.model.bean;

import java.io.Serializable;
import java.util.List;

/**
 * [Description]
 * <p/>
 * [How to use]
 * <p/>
 * [Tips]
 *
 * @author Created by Zhimin.Huang on 2016/6/16.
 * @since 1.0.0
 */
public class ClassChooseBean implements Serializable {

    /**
     * GradeId : 4
     * GradeCode : 2016
     * Year : 2016
     * GradeName : 2016级
     */

    private List<GradeBean> Grade;
    /**
     * Id : 78
     * ClassName : 14会计1
     * Grade : 2
     */

    private List<ClassBean> Class;

    public List<GradeBean> getGradeBean() {
        return Grade;
    }

    public void setGradeBean(List<GradeBean> Grade) {
        this.Grade = Grade;
    }

    public List<ClassBean> getClassBean() {
        return Class;
    }

    public void setClassBean(List<ClassBean> Class) {
        this.Class = Class;
    }

    public static class GradeBean {
        private String GradeId;
        private String GradeCode;
        private String Year;
        private String GradeName;

        public String getGradeId() {
            return GradeId;
        }

        public void setGradeId(String GradeId) {
            this.GradeId = GradeId;
        }

        public String getGradeCode() {
            return GradeCode;
        }

        public void setGradeCode(String GradeCode) {
            this.GradeCode = GradeCode;
        }

        public String getYear() {
            return Year;
        }

        public void setYear(String Year) {
            this.Year = Year;
        }

        public String getGradeName() {
            return GradeName;
        }

        public void setGradeName(String GradeName) {
            this.GradeName = GradeName;
        }
    }

    public static class ClassBean {
        private int Id;
        private String ClassName;
        private String Grade;

        public int getId() {
            return Id;
        }

        public void setId(int Id) {
            this.Id = Id;
        }

        public String getClassName() {
            return ClassName;
        }

        public void setClassName(String ClassName) {
            this.ClassName = ClassName;
        }

        public String getGrade() {
            return Grade;
        }

        public void setGrade(String Grade) {
            this.Grade = Grade;
        }
    }
}
