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
 * @author Created by Zhimin.Huang on 2016/6/16.
 * @since 1.0.0
 */
public class SortClassBean implements Serializable {

    private String GradeId;
    private String GradeCode;
    private String Year;
    private String GradeName;
    private List<ClassByYear> mClassByYears;

    public String getGradeId() {
        return GradeId;
    }

    public void setGradeId(String gradeId) {
        GradeId = gradeId;
    }

    public String getGradeCode() {
        return GradeCode;
    }

    public void setGradeCode(String gradeCode) {
        GradeCode = gradeCode;
    }

    public String getYear() {
        return Year;
    }

    public void setYear(String year) {
        Year = year;
    }

    public String getGradeName() {
        return GradeName;
    }

    public void setGradeName(String gradeName) {
        GradeName = gradeName;
    }

    public List<ClassByYear> getClassByYears() {
        return mClassByYears;
    }

    public void setClassByYears(List<ClassByYear> classByYears) {
        mClassByYears = classByYears;
    }

    public static class ClassByYear {
        private int Id;
        private String ClassName;
        private String Grade;
        private String Year;

        public String getYear() {
            return Year;
        }

        public void setYear(String year) {
            Year = year;
        }

        public int getId() {
            return Id;
        }

        public void setId(int id) {
            Id = id;
        }

        public String getClassName() {
            return ClassName;
        }

        public void setClassName(String className) {
            ClassName = className;
        }

        public String getGrade() {
            return Grade;
        }

        public void setGrade(String grade) {
            Grade = grade;
        }
    }
}
