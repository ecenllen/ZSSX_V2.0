package com.gta.zssx.fun.coursedaily.registerrecord.model.DTO;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lan.zheng on 2016/6/16.
 * 用于一个时间段内的已登记记录粗略内容
 */
public class RegisteredRecordDto implements Serializable {

    /**
     * totalSection: 总共的上课节次
     * Detail: 每个条目具体内容
     */
    private int Total;

    private List<recordEntry> Detail;

    public int getTotal() {
        return Total;
    }

    public void setTotal(int Total) {
        this.Total = Total;
    }

    public List<recordEntry> getDetail(){return Detail;}

    public void setDetail(List<recordEntry> mRecordEntries){this.Detail = mRecordEntries;}

    public static class recordEntry implements Serializable{

        /**
         * ClassID : 21
         * SignData : “2016-06-20”
         * Section : “第1、2节”
         * ClassName : “13会计1”
         */

        private int ClassId;
        private String SignDate;
        private String Section;
        private String ClassName;

        public int getClassID() {
            return ClassId;
        }

        public void setClassID(int ClassId) {
            this.ClassId = ClassId;
        }

        public String getSignDate() {
            return SignDate;
        }

        public void setSignDate(String SignDate) {
            this.SignDate = SignDate;
        }

        public String getSection() {
            return Section;
        }

        public void setSection(String Section) {
            this.Section = Section;
        }

        public String getClassName() {
            return ClassName;
        }

        public void setClassName(String ClassName) {
            this.ClassName = ClassName;
        }
    }
}
