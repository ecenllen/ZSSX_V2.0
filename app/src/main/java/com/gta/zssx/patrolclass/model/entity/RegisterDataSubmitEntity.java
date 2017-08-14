package com.gta.zssx.patrolclass.model.entity;

import java.util.List;

/**
 * 登记提交的实体
 * Created by liang.lu on 2017/4/24 10:53.
 */

public class RegisterDataSubmitEntity {

    /**
     * Uid : 000
     * Xid : 0
     * Pid : 0
     * DeptId : 0
     * RegisterDataJson : {"Date":"2016年6月1日","ClassId":1,"SectionId":2,"Type":"1","TeacherList":[{"TeacherId":"3","AutoScore":"1","GetScoreList":[{"Name":"教师分","GetScore":80,"Result":[{"Id":1,"Score":50},{"Id":2,"Score":50}]},{"Name":"教师分","GetScore":80,"Result":[{"Id":1,"Score":50},{"Id":2,"Score":50}]}]},{"TeacherId":"3","AutoScore":"1","GetScoreList":[{"Name":"教师分","GetScore":80,"Result":[{"Id":1,"Score":50},{"Id":2,"Score":50}]},{"Name":"教师分","GetScore":80,"Result":[{"Id":1,"Score":50},{"Id":2,"Score":50}]}]}]}
     */

    private String Uid;
    private int Xid;
    private int Pid;
    private int DeptId;
    /**
     * Date : 2016年6月1日
     * ClassId : 1
     * SectionId : 2
     * Type : 1
     * TeacherList : [{"TeacherId":"3","AutoScore":"1","GetScoreList":[{"Name":"教师分","GetScore":80,"Result":[{"Id":1,"Score":50},{"Id":2,"Score":50}]},{"Name":"教师分","GetScore":80,"Result":[{"Id":1,"Score":50},{"Id":2,"Score":50}]}]},{"TeacherId":"3","AutoScore":"1","GetScoreList":[{"Name":"教师分","GetScore":80,"Result":[{"Id":1,"Score":50},{"Id":2,"Score":50}]},{"Name":"教师分","GetScore":80,"Result":[{"Id":1,"Score":50},{"Id":2,"Score":50}]}]}]
     */

    private RegisterDataJsonBean RegisterDataJson;

    public String getUid () {
        return Uid;
    }

    public void setUid (String Uid) {
        this.Uid = Uid;
    }

    public int getXid () {
        return Xid;
    }

    public void setXid (int Xid) {
        this.Xid = Xid;
    }

    public int getPid () {
        return Pid;
    }

    public void setPid (int Pid) {
        this.Pid = Pid;
    }

    public int getDeptId () {
        return DeptId;
    }

    public void setDeptId (int DeptId) {
        this.DeptId = DeptId;
    }

    public RegisterDataJsonBean getRegisterDataJson () {
        return RegisterDataJson;
    }

    public void setRegisterDataJson (RegisterDataJsonBean RegisterDataJson) {
        this.RegisterDataJson = RegisterDataJson;
    }

    public static class RegisterDataJsonBean {
        private String Date;
        private int ClassId;
        private int SectionId;
        private String Type;
        /**
         * TeacherId : 3
         * AutoScore : 1
         * GetScoreList : [{"Name":"教师分","GetScore":80,"Result":[{"Id":1,"Score":50},{"Id":2,"Score":50}]},{"Name":"教师分","GetScore":80,"Result":[{"Id":1,"Score":50},{"Id":2,"Score":50}]}]
         */

        private List<TeacherListBean> TeacherList;

        public String getDate () {
            return Date;
        }

        public void setDate (String Date) {
            this.Date = Date;
        }

        public int getClassId () {
            return ClassId;
        }

        public void setClassId (int ClassId) {
            this.ClassId = ClassId;
        }

        public int getSectionId () {
            return SectionId;
        }

        public void setSectionId (int SectionId) {
            this.SectionId = SectionId;
        }

        public String getType () {
            return Type;
        }

        public void setType (String Type) {
            this.Type = Type;
        }

        public List<TeacherListBean> getTeacherList () {
            return TeacherList;
        }

        public void setTeacherList (List<TeacherListBean> TeacherList) {
            this.TeacherList = TeacherList;
        }

        public static class TeacherListBean {
            private String TeacherId;
            private String AutoScore;
            /**
             * Name : 教师分
             * GetScore : 80
             * Result : [{"Id":1,"Score":50},{"Id":2,"Score":50}]
             */

            private List<GetScoreListBean> GetScoreList;

            public String getTeacherId () {
                return TeacherId;
            }

            public void setTeacherId (String TeacherId) {
                this.TeacherId = TeacherId;
            }

            public String getAutoScore () {
                return AutoScore;
            }

            public void setAutoScore (String AutoScore) {
                this.AutoScore = AutoScore;
            }

            public List<GetScoreListBean> getGetScoreList () {
                return GetScoreList;
            }

            public void setGetScoreList (List<GetScoreListBean> GetScoreList) {
                this.GetScoreList = GetScoreList;
            }

            public static class GetScoreListBean {
                private String Name;
                private int GetScore;
                /**
                 * Id : 1
                 * Score : 50
                 */

                private List<ResultBean> Result;

                public String getName () {
                    return Name;
                }

                public void setName (String Name) {
                    this.Name = Name;
                }

                public int getGetScore () {
                    return GetScore;
                }

                public void setGetScore (int GetScore) {
                    this.GetScore = GetScore;
                }

                public List<ResultBean> getResult () {
                    return Result;
                }

                public void setResult (List<ResultBean> Result) {
                    this.Result = Result;
                }

                public static class ResultBean {
                    private int Id;
                    private int Score;

                    public int getId () {
                        return Id;
                    }

                    public void setId (int Id) {
                        this.Id = Id;
                    }

                    public int getScore () {
                        return Score;
                    }

                    public void setScore (int Score) {
                        this.Score = Score;
                    }
                }
            }
        }
    }
}
