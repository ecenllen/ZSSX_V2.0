package com.gta.zssx.mobileOA.model.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lan.zheng on 2016/10/31.  事务提醒
 */
public class EventNoticeInfo implements Serializable {
    private String ServerTime;
    private List<EventRemindEntity> EventRemind;

    public String getServerTime() {
        return ServerTime;
    }

    public void setServerTime(String ServerTime) {
        this.ServerTime = ServerTime;
    }


    public List<EventRemindEntity> getEventRemind() {
        return EventRemind;
    }

    public void setEventRemind(List<EventRemindEntity> EventRemind) {
        this.EventRemind = EventRemind;
    }

    public static class EventRemindEntity {
        private String Type;//类型
        private String Subject;//内容
        private String EndTime;//时间
        private String Creator;//审批人
        private String Status;//状态
        private String CheckStatus;//审批结果

        public String getType() {
            return Type;
        }

        public void setType(String type) {
            Type = type;
        }

        public String getSubject() {
            return Subject;
        }

        public void setSubject(String subject) {
            Subject = subject;
        }

        public String getEndTime() {
            return EndTime;
        }

        public void setEndTime(String endTime) {
            EndTime = endTime;
        }

        public String getCreator() {
            return Creator;
        }

        public void setCreator(String creator) {
            Creator = creator;
        }

        public String getStatus() {
            return Status;
        }

        public void setStatus(String status) {
            Status = status;
        }

        public String getCheckStatus() {
            return CheckStatus;
        }

        public void setCheckStatus(String checkStatus) {
            CheckStatus = checkStatus;
        }
    }

}
