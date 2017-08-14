package com.gta.zssx.patrolclass.model.entity;

import java.io.Serializable;
import java.util.List;

/**
 * [Description]
 * <p/>   上传巡课登记数据的bean
 * [How to use]
 * <p>
 * [Tips] Created by tengfei.lv on 2016/7/27 17:48.
 */

public class SubmitRegisterPatrol implements Serializable {


    /**
     * uId : afdasf
     * xId : 0
     * pId : 0
     * deptId : 0
     * json : {"date":"2016年6月1日","classId":1,"sectionId":2,"teacherId":"3","autoScore":"1","type":"1","getScoreList":[{"name":"教师分","getScore":80,"result":[{"id":1,"score":50},{"id":2,"score":50}]},{"name":"教师分","getScore":80,"result":[{"id":1,"score":50},{"id":2,"score":50}]}]}
     */

    private String uId;
    private int xId;
    private int pId;
    /**
     * date : 2016年6月1日
     * classId : 1
     * sectionId : 2
     * teacherId : 3
     * autoScore : 1
     * type : 1
     * getScoreList : [{"name":"教师分","getScore":80,"result":[{"id":1,"score":50},{"id":2,"score":50}]},{"name":"教师分","getScore":80,"result":[{"id":1,"score":50},{"id":2,"score":50}]}]
     */

    private JsonBean json;

    public String getUId () {
        return uId;
    }

    public void setUId (String uId) {
        this.uId = uId;
    }

    public int getXId () {
        return xId;
    }

    public void setXId (int xId) {
        this.xId = xId;
    }

    public int getPId () {
        return pId;
    }

    public void setPId (int pId) {
        this.pId = pId;
    }

    public JsonBean getJson () {
        return json;
    }

    public void setJson (JsonBean json) {
        this.json = json;
    }

    public static class JsonBean implements Serializable {
        private String date;
        private int classId;
        private int sectionId;
        private String teacherId;
        private String autoScore;
        private String type;
        private String deptId;
        /**
         * name : 教师分
         * getScore : 80
         * result : [{"id":1,"score":50},{"id":2,"score":50}]
         */

        private List<GetScoreListBean> getScoreList;

        public String getDeptId () {
            return deptId;
        }

        public void setDeptId (String deptId) {
            this.deptId = deptId;
        }

        public String getDate () {
            return date;
        }

        public void setDate (String date) {
            this.date = date;
        }

        public int getClassId () {
            return classId;
        }

        public void setClassId (int classId) {
            this.classId = classId;
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

        public String getAutoScore () {
            return autoScore;
        }

        public void setAutoScore (String autoScore) {
            this.autoScore = autoScore;
        }

        public String getType () {
            return type;
        }

        public void setType (String type) {
            this.type = type;
        }

        public List<GetScoreListBean> getGetScoreList () {
            return getScoreList;
        }

        public void setGetScoreList (List<GetScoreListBean> getScoreList) {
            this.getScoreList = getScoreList;
        }

        public static class GetScoreListBean implements Serializable {
            private String name;
            private int getScore;
            private int Id;
            /**
             * id : 1
             * score : 50
             */

            private List<ResultBean> result;

            public int getId () {
                return Id;
            }

            public void setId (int id) {
                Id = id;
            }

            public String getName () {
                return name;
            }

            public void setName (String name) {
                this.name = name;
            }

            public int getGetScore () {
                return getScore;
            }

            public void setGetScore (int getScore) {
                this.getScore = getScore;
            }

            public List<ResultBean> getResult () {
                return result;
            }

            public void setResult (List<ResultBean> result) {
                this.result = result;
            }

            public static class ResultBean implements Serializable {
                private int id;
                private int score;

                public int getId () {
                    return id;
                }

                public void setId (int id) {
                    this.id = id;
                }

                public int getScore () {
                    return score;
                }

                public void setScore (int score) {
                    this.score = score;
                }
            }
        }
    }
}
