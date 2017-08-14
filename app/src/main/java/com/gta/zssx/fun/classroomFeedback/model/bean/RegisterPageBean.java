package com.gta.zssx.fun.classroomFeedback.model.bean;

import android.content.Context;

import com.gta.zssx.pub.util.JsonHelper;
import com.gta.zssx.pub.util.LogUtil;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.List;

/**
 * [Description]
 * <p> 课堂教学反馈课表登记页面数据
 * [How to use]
 * <p>
 * [Tips]
 *
 * @author Created by liang.lu on 2017/6/28 10:27.
 * @since 2.0.0
 */
public class RegisterPageBean {


    /**
     * Auditor : 张三
     * AuditorDate : 2017-05-24 15:31
     * AuditorOpinion : 已通过!
     * ClassroomRegisterList : [{"Date":"2017-05-22","WeekDate":1,"SectionDataList":[{"Section":"1","CourseName":"数学","TeacherName":"张学友","Score":"90"},{"Section":"2","CourseName":"数学","TeacherName":"张学友","Score":"90"},{"Section":"3","CourseName":"","TeacherName":"","Score":""},{"Section":"4","CourseName":"","TeacherName":"","Score":""},{"Section":"5","CourseName":"数学","TeacherName":"张学友","Score":""},{"Section":"6","CourseName":"数学","TeacherName":"张学友","Score":""},{"Section":"7","CourseName":"","TeacherName":"","Score":""}]},{"Date":"2017-05-22","WeekDate":2,"SectionDataList":[{"Section":"1","CourseName":"数学","TeacherName":"张学友","Score":"90"},{"Section":"2","CourseName":"数学","TeacherName":"张学友","Score":"90"},{"Section":"3","CourseName":"数学","TeacherName":"张学友","Score":"90"},{"Section":"4","CourseName":"","TeacherName":"","Score":""},{"Section":"5","CourseName":"","TeacherName":"","Score":""},{"Section":"6","CourseName":"","TeacherName":"","Score":""},{"Section":"7","CourseName":"","TeacherName":"","Score":""}]},{"Date":"2017-05-22","WeekDate":3,"SectionDataList":[{"Section":"1","CourseName":"数学","TeacherName":"张学友","Score":"90"},{"Section":"2","CourseName":"","TeacherName":"","Score":""},{"Section":"3","CourseName":"数学","TeacherName":"张学友","Score":"90"},{"Section":"4","CourseName":"数学","TeacherName":"张学友","Score":"90"},{"Section":"5","CourseName":"","TeacherName":"","Score":""},{"Section":"6","CourseName":"数学","TeacherName":"张学友","Score":"90"},{"Section":"7","CourseName":"","TeacherName":"","Score":""}]},{"Date":"2017-05-22","WeekDate":4,"SectionDataList":[{"Section":"1","CourseName":"数学","TeacherName":"张学友","Score":"90"},{"Section":"2","CourseName":"数学","TeacherName":"张学友","Score":"90"},{"Section":"3","CourseName":"数学","TeacherName":"张学友","Score":"90"},{"Section":"4","CourseName":"","TeacherName":"","Score":""},{"Section":"5","CourseName":"","TeacherName":"","Score":""},{"Section":"6","CourseName":"","TeacherName":"","Score":""},{"Section":"7","CourseName":"","TeacherName":"","Score":""}]},{"Date":"2017-05-22","WeekDate":5,"SectionDataList":[{"Section":"1","CourseName":"数学","TeacherName":"张学友","Score":"90"},{"Section":"2","CourseName":"数学","TeacherName":"张学友","Score":"90"},{"Section":"3","CourseName":"数学","TeacherName":"张学友","Score":"90"},{"Section":"4","CourseName":"","TeacherName":"","Score":""},{"Section":"5","CourseName":"数学","TeacherName":"张学友","Score":"90"},{"Section":"6","CourseName":"","TeacherName":"","Score":""},{"Section":"7","CourseName":"数学","TeacherName":"张学友","Score":"90"}]}]
     */

    private String Auditor;
    private String AuditorDate;
    private String AuditorOpinion;
    private String Opinion;
    /**
     * Date : 2017-05-22
     * WeekDate : 1
     * SectionDataList : [{"Section":"1","CourseName":"数学","TeacherName":"张学友","Score":"90"},{"Section":"2","CourseName":"数学","TeacherName":"张学友","Score":"90"},{"Section":"3","CourseName":"","TeacherName":"","Score":""},{"Section":"4","CourseName":"","TeacherName":"","Score":""},{"Section":"5","CourseName":"数学","TeacherName":"张学友","Score":""},{"Section":"6","CourseName":"数学","TeacherName":"张学友","Score":""},{"Section":"7","CourseName":"","TeacherName":"","Score":""}]
     */

    private List<ClassroomRegisterListBean> ClassroomRegisterList;
    private int ClassTeachId;

    public int getClassTeachId () {
        return ClassTeachId;
    }

    public void setClassTeachId (int classTeachId) {
        ClassTeachId = classTeachId;
    }

    public static RegisterPageBean getRegisterPageBean (Context context) {
        try {
            InputStream inputStream = context.getAssets ().open ("register.json");

            InputStreamReader reader = new InputStreamReader (inputStream);

            BufferedReader bufferedReader = new BufferedReader (reader);

            String readerData;
            StringBuilder stringBuffer = new StringBuilder ();
            while ((readerData = bufferedReader.readLine ()) != null) {
                stringBuffer.append (readerData);
            }
            reader.close ();
            bufferedReader.close ();
            inputStream.close ();
            LogUtil.e ("解析成功：" + stringBuffer.toString ());
            JSONObject jsonObject = new JSONObject (stringBuffer.toString ());

            return JsonHelper.parseObject (jsonObject.toString (), RegisterPageBean.class);

        } catch (Exception e) {
            e.printStackTrace ();
        }
        return null;
    }

    public String getOpinion () {
        return Opinion;
    }

    public void setOpinion (String opinion) {
        Opinion = opinion;
    }

    public String getAuditor () {
        return Auditor;
    }

    public void setAuditor (String Auditor) {
        this.Auditor = Auditor;
    }

    public String getAuditorDate () {
        return AuditorDate;
    }

    public void setAuditorDate (String AuditorDate) {
        this.AuditorDate = AuditorDate;
    }

    public String getAuditorOpinion () {
        return AuditorOpinion;
    }

    public void setAuditorOpinion (String AuditorOpinion) {
        this.AuditorOpinion = AuditorOpinion;
    }

    public List<ClassroomRegisterListBean> getClassroomRegisterList () {
        return ClassroomRegisterList;
    }

    public void setClassroomRegisterList (List<ClassroomRegisterListBean> ClassroomRegisterList) {
        this.ClassroomRegisterList = ClassroomRegisterList;
    }

    public static class ClassroomRegisterListBean {
        private String Date;//2017-05-06
        private int WeekDate;// 1   某一周中的周几
        private boolean IsComplete;
        /**
         * Section : 1
         * CourseName : 数学
         * TeacherName : 张学友
         * Score : 90
         */

        private List<SectionDataListBean> SectionDataList;

        public boolean isComplete () {
            return IsComplete;
        }

        public void setComplete (boolean complete) {
            IsComplete = complete;
        }

        public String getDate () {
            return Date;
        }

        public void setDate (String Date) {
            this.Date = Date;
        }

        public int getWeekDate () {
            return WeekDate;
        }

        public void setWeekDate (int WeekDate) {
            this.WeekDate = WeekDate;
        }

        public List<SectionDataListBean> getSectionDataList () {
            return SectionDataList;
        }

        public void setSectionDataList (List<SectionDataListBean> SectionDataList) {
            this.SectionDataList = SectionDataList;
        }

        public static class SectionDataListBean implements Serializable {
            private int Section;//节次 "1"
            private String TeacherName;
            private String TeacherID;
            private int Score;
            private boolean SectionComplete;
            private String SectionText; //  节次   第一节
            private String Date;  // 日期  2017-06-09
            private String DateText;  //日期     周一（5月22日）
            private int WeekDate; //周次   15
            private String WeekDateText;//周次   十三周
            private boolean IsEmpty;//是否为空
            private int CurrentPosition; //当前节次在总集合所处的位置
            private List<RegisterDetailsBean.ScoreOptionsListBean.OptionsBean> OptionsBeanList;
            private List<RegisterDetailsBean.ScoreOptionsListBean.OptionsBean> OptionsList;
            private int ClassTeachId;
            private int CourseID;

            public int getCourseID () {
                return CourseID;
            }

            public void setCourseID (int courseID) {
                CourseID = courseID;
            }

            public int getClassTeachId () {
                return ClassTeachId;
            }

            public void setClassTeachId (int classTeachId) {
                ClassTeachId = classTeachId;
            }

            public List<RegisterDetailsBean.ScoreOptionsListBean.OptionsBean> getOptionsList () {
                return OptionsList;
            }

            public void setOptionsList (List<RegisterDetailsBean.ScoreOptionsListBean.OptionsBean> optionsList) {
                OptionsList = optionsList;
            }

            //            private List<CourseListBean> CourseList;
            private String CourseName;
            private String CourseId;

            public String getCourseId () {
                return CourseId;
            }

            public void setCourseId (String courseId) {
                CourseId = courseId;
            }

            public String getCourseName () {
                return CourseName;
            }

            public void setCourseName (String courseName) {
                CourseName = courseName;
            }
            //            public List<CourseListBean> getCourseList () {
            //                return CourseList;
            //            }
            //
            //            public void setCourseList (List<CourseListBean> courseList) {
            //                CourseList = courseList;
            //            }

            public List<RegisterDetailsBean.ScoreOptionsListBean.OptionsBean> getOptionsBeanList () {
                return OptionsBeanList;
            }

            public void setOptionsBeanList (List<RegisterDetailsBean.ScoreOptionsListBean.OptionsBean> optionsBeanList) {
                this.OptionsBeanList = optionsBeanList;
            }

            public String getSectionText () {
                return SectionText;
            }

            public void setSectionText (String sectionText) {
                SectionText = sectionText;
            }

            public String getDate () {
                return Date;
            }

            public void setDate (String date) {
                Date = date;
            }

            public String getDateText () {
                return DateText;
            }

            public void setDateText (String dateText) {
                DateText = dateText;
            }

            public int getWeekDate () {
                return WeekDate;
            }

            public void setWeekDate (int weekDate) {
                WeekDate = weekDate;
            }

            public String getWeekDateText () {
                return WeekDateText;
            }

            public void setWeekDateText (String weekDateText) {
                WeekDateText = weekDateText;
            }

            public boolean isEmpty () {
                return IsEmpty;
            }

            public void setEmpty (boolean empty) {
                IsEmpty = empty;
            }

            public int getCurrentPosition () {
                return CurrentPosition;
            }

            public void setCurrentPosition (int currentPosition) {
                CurrentPosition = currentPosition;
            }

            public boolean isSectionComplete () {
                return SectionComplete;
            }

            public void setSectionComplete (boolean sectionComplete) {
                SectionComplete = sectionComplete;
            }

            public String getTeacherID () {
                return TeacherID;
            }

            public void setTeacherID (String teacherID) {
                TeacherID = teacherID;
            }

            public int getSection () {
                return Section;
            }

            public void setSection (int Section) {
                this.Section = Section;
            }

            public String getTeacherName () {
                return TeacherName;
            }

            public void setTeacherName (String TeacherName) {
                this.TeacherName = TeacherName;
            }

            public int getScore () {
                return Score;
            }

            public void setScore (int Score) {
                this.Score = Score;
            }

            public static class CourseListBean implements Serializable {
                private String CourseId;
                private String CourseName;

                public String getCourseId () {
                    return CourseId;
                }

                public void setCourseId (String courseId) {
                    CourseId = courseId;
                }

                public String getCourseName () {
                    return CourseName;
                }

                public void setCourseName (String courseName) {
                    CourseName = courseName;
                }
            }
        }
    }
}
