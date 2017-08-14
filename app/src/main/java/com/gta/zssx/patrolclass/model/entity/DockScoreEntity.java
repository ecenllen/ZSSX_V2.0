package com.gta.zssx.patrolclass.model.entity;

import java.io.Serializable;
import java.util.List;

/**
 * [Description]
 * <p/>    扣分详情
 * [How to use]
 * <p>
 * [Tips] Created by tengfei.lv on 2016/7/20 11:04.
 */

public class DockScoreEntity implements Serializable {


    /**
     * currentdate : 2015/12/09 00:00:00
     * className : 13会计
     * serverTime : 17:25:25
     * teacherName : 张小四
     * dockScore : [{"name":"教学分","options":[{"type":1,"title":"这是题目","score":"8","maxScore":"","getScore":"8","itemId":5},{"type":1,"title":"这是题目","score":"8","maxScore":"","getScore":"8","itemId":5}]},{"name":"教学分","options":[{"type":1,"title":"这是题目","score":"8","maxScore":"","getScore":"8","itemId":5},{"type":1,"title":"这是题目","score":"8","maxScore":"","getScore":"8","itemId":5}]}]
     * sectionsList : [{"type":1,"beginTime":"9:45","endTime":"10:30","teacherId":9527,"teacherName":"张明","sectionName":"第一节"},{"type":1,"beginTime":"9:45","endTime":"10:30","teacherId":9527,"teacherName":"张明","sectionName":"第一节"}]
     */

    private String currentdate;
    private String className;
    private int classId;
    private String teacherName;
    private String teacherId;
    private String sectionName;
    private int sectionId;
    private String autoScore;
    private String type;
    private int pId;
    private String servicedate;

    public String getServicedate () {
        return servicedate;
    }

    public void setServicedate (String servicedate) {
        this.servicedate = servicedate;
    }

    public int getpId () {
        return pId;
    }

    public void setpId (int pId) {
        this.pId = pId;
    }

    public int getClassId () {
        return classId;
    }

    public void setClassId (int classId) {
        this.classId = classId;
    }

    public String getSectionName () {
        return sectionName;
    }

    public void setSectionName (String sectionName) {
        this.sectionName = sectionName;
    }

    public String getAutoScore () {
        return autoScore;
    }

    public void setAutoScore (String autoScore) {
        this.autoScore = autoScore;
    }

    public int getSectionId () {
        return sectionId;
    }

    public void setSectionId (int sectionId) {
        this.sectionId = sectionId;
    }

    public String getTeacherId () {
        return teacherId;
    }

    public void setTeacherId (String teacherId) {
        this.teacherId = teacherId;
    }

    public String getType () {
        return type;
    }

    public void setType (String type) {
        this.type = type;
    }

    /**
     * name : 教学分
     * options : [{"type":1,"title":"这是题目","score":"8","maxScore":"","getScore":"8","itemId":5},{"type":1,"title":"这是题目","score":"8","maxScore":"","getScore":"8","itemId":5}]
     */

    private List<DockScoreBean> dockScore;
    /**
     * type : 1
     * beginTime : 9:45
     * endTime : 10:30
     * teacherId : 9527
     * teacherName : 张明
     * sectionName : 第一节
     */

    private List<SectionsListBean> sectionsList;

    public String getCurrentdate () {
        return currentdate;
    }

    public void setCurrentdate (String currentdate) {
        this.currentdate = currentdate;
    }

    public String getClassName () {
        return className;
    }

    public void setClassName (String className) {
        this.className = className;
    }

    public String getTeacherName () {
        return teacherName;
    }

    public void setTeacherName (String teacherName) {
        this.teacherName = teacherName;
    }

    public List<DockScoreBean> getDockScore () {
        return dockScore;
    }

    public void setDockScore (List<DockScoreBean> dockScore) {
        this.dockScore = dockScore;
    }

    public List<SectionsListBean> getSectionsList () {
        return sectionsList;
    }

    public void setSectionsList (List<SectionsListBean> sectionsList) {
        this.sectionsList = sectionsList;
    }

    public static class DockScoreBean implements Serializable{
        private String name;
        private String allScore;
        private String getAllScore;
        private int Id;
        /**
         * type : 1
         * title : 这是题目
         * score : 8
         * maxScore :
         * getScore : 8
         * itemId : 5
         * Id : 135
         */

        private List<OptionsBean> options;

        public int getId () {
            return Id;
        }

        public void setId (int id) {
            Id = id;
        }

        public String getAllScore () {
            return allScore;
        }

        public void setAllScore (String allScore) {
            this.allScore = allScore;
        }

        public String getGetAllScore () {
            return getAllScore;
        }

        public void setGetAllScore (String getAllScore) {
            this.getAllScore = getAllScore;
        }

        public String getName () {
            return name;
        }

        public void setName (String name) {
            this.name = name;
        }

        public List<OptionsBean> getOptions () {
            return options;
        }

        public void setOptions (List<OptionsBean> options) {
            this.options = options;
        }

        public static class OptionsBean implements Serializable {
            private int type;
            private String title;
            private String score;
            private String maxScore;
            private String getScore;
            private int Id;
            private boolean isTitle;
            private boolean isCheck;
            private String changeScoer;
            private String allScore;

            public String getAllScore () {
                return allScore;
            }

            public void setAllScore (String allScore) {
                this.allScore = allScore;
            }

            public String getChangeScoer () {
                return changeScoer;
            }

            public void setChangeScoer (String changeScoer) {
                this.changeScoer = changeScoer;
            }

            public boolean isCheck () {
                return isCheck;
            }

            public void setCheck (boolean check) {
                isCheck = check;
            }

            public boolean isTitle () {
                return isTitle;
            }

            public void setTitle (boolean title) {
                isTitle = title;
            }

            public int getType () {
                return type;
            }

            public void setType (int type) {
                this.type = type;
            }

            public String getTitle () {
                return title;
            }

            public void setTitle (String title) {
                this.title = title;
            }

            public String getScore () {
                return score;
            }

            public void setScore (String score) {
                this.score = score;
            }

            public String getMaxScore () {
                return maxScore;
            }

            public void setMaxScore (String maxScore) {
                this.maxScore = maxScore;
            }

            public String getGetScore () {
                return getScore;
            }

            public void setGetScore (String getScore) {
                this.getScore = getScore;
            }

            public int getItemId () {
                return Id;
            }

            public void setItemId (int itemId) {
                this.Id = itemId;
            }
        }
    }

    public static class SectionsListBean implements Serializable{
        private int type;
        private String sectionName;
        private int sectionId;
        private boolean isCheck;

        public int getSectionId () {
            return sectionId;
        }

        public void setSectionId (int sectionId) {
            this.sectionId = sectionId;
        }

        public boolean isCheck () {
            return isCheck;
        }

        public void setCheck (boolean check) {
            isCheck = check;
        }

        public int getType () {
            return type;
        }

        public void setType (int type) {
            this.type = type;
        }

        public String getSectionName () {
            return sectionName;
        }

        public void setSectionName (String sectionName) {
            this.sectionName = sectionName;
        }
    }
}
