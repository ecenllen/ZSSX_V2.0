package com.gta.zssx.fun.classroomFeedback.model.bean;

import java.io.Serializable;
import java.util.List;

/**
 * [Description]
 * <p> 登记详情页提交保存评分实体
 * [How to use]
 * <p>
 * [Tips]
 *
 * @author Created by liang.lu on 2017/6/28 10:27.
 * @since 2.0.0
 */
public class SubmitSaveScoreBean implements Serializable {

    /**
     * ClassID : 1111
     * Json : {"WeekDate":5,"Date":"2017-05-06","Section":8,"CourseName":"数学","TeacherId":"44444","Score":"90","SaveScoreOptionsList":[{"DeductItemID":888,"SaveOptionsList":[{"OptionId":5},{"OptionId":5}]},{"DeductItemID":888,"SaveOptionsList":[{"OptionId":5},{"OptionId":5}]},{"DeductItemID":888,"SaveOptionsList":[{"OptionId":5},{"OptionId":5}]}]}
     */

    private int ClassID;
    private int ClassTeachId;
    /**
     * WeekDate : 5
     * Date : 2017-05-06
     * Section : 8
     * CourseName : 数学
     * TeacherId : 44444
     * Score : 90
     * SaveScoreOptionsList : [{"DeductItemID":888,"SaveOptionsList":[{"OptionId":5},{"OptionId":5}]},{"DeductItemID":888,"SaveOptionsList":[{"OptionId":5},{"OptionId":5}]},{"DeductItemID":888,"SaveOptionsList":[{"OptionId":5},{"OptionId":5}]}]
     */

    private JsonBean Json;

    public int getClassTeachId () {
        return ClassTeachId;
    }

    public void setClassTeachId (int classTeachId) {
        ClassTeachId = classTeachId;
    }

    public int getClassID () {
        return ClassID;
    }

    public void setClassID (int ClassID) {
        this.ClassID = ClassID;
    }

    public JsonBean getJson () {
        return Json;
    }

    public void setJson (JsonBean Json) {
        this.Json = Json;
    }

    public static class JsonBean implements Serializable {
        private int WeekDate;
        private String Date;
        private int Section;
        //        private List<CourseListBean> CourseList;
        private String TeacherId;
        private int Score;
        private String CourseId;
        private String CourseName;

        public String getCourseName () {
            return CourseName;
        }

        public void setCourseName (String courseName) {
            CourseName = courseName;
        }

        public String getCourseId () {
            return CourseId;
        }

        public void setCourseId (String courseId) {
            CourseId = courseId;
        }

        /**
         * DeductItemID : 888
         * SaveOptionsList : [{"OptionId":5},{"OptionId":5}]
         */

        private List<SaveScoreOptionsListBean> SaveScoreOptionsList;

        //        public List<CourseListBean> getCourseList () {
        //            return CourseList;
        //        }
        //
        //        public void setCourseList (List<CourseListBean> courseList) {
        //            CourseList = courseList;
        //        }

        public int getWeekDate () {
            return WeekDate;
        }

        public void setWeekDate (int WeekDate) {
            this.WeekDate = WeekDate;
        }

        public String getDate () {
            return Date;
        }

        public void setDate (String Date) {
            this.Date = Date;
        }

        public int getSection () {
            return Section;
        }

        public void setSection (int Section) {
            this.Section = Section;
        }

        public String getTeacherId () {
            return TeacherId;
        }

        public void setTeacherId (String TeacherId) {
            this.TeacherId = TeacherId;
        }

        public int getScore () {
            return Score;
        }

        public void setScore (int Score) {
            this.Score = Score;
        }

        public List<SaveScoreOptionsListBean> getSaveScoreOptionsList () {
            return SaveScoreOptionsList;
        }

        public void setSaveScoreOptionsList (List<SaveScoreOptionsListBean> SaveScoreOptionsList) {
            this.SaveScoreOptionsList = SaveScoreOptionsList;
        }

        public static class SaveScoreOptionsListBean implements Serializable {
            private int DeductItemID;
            /**
             * OptionId : 5
             */

            private List<SaveOptionsListBean> SaveOptionsList;

            public int getDeductItemID () {
                return DeductItemID;
            }

            public void setDeductItemID (int DeductItemID) {
                this.DeductItemID = DeductItemID;
            }

            public List<SaveOptionsListBean> getSaveOptionsList () {
                return SaveOptionsList;
            }

            public void setSaveOptionsList (List<SaveOptionsListBean> SaveOptionsList) {
                this.SaveOptionsList = SaveOptionsList;
            }

            public static class SaveOptionsListBean implements Serializable {
                private int OptionId;

                public int getOptionId () {
                    return OptionId;
                }

                public void setOptionId (int OptionId) {
                    this.OptionId = OptionId;
                }
            }
        }

        //        public static class CourseListBean {
        //            private String CourseId;
        //            private String CourseName;
        //
        //            public String getCourseName () {
        //                return CourseName;
        //            }
        //
        //            public void setCourseName (String courseName) {
        //                CourseName = courseName;
        //            }
        //
        //            public String getCourseId () {
        //                return CourseId;
        //            }
        //
        //            public void setCourseId (String courseId) {
        //                CourseId = courseId;
        //            }
        //        }
    }
}
