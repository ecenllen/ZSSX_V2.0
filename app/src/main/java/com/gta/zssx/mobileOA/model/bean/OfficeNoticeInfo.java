package com.gta.zssx.mobileOA.model.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lan.zheng on 2016/11/4.
 */
public class OfficeNoticeInfo implements Serializable {
    private String ServerTime;
    private List<OfficeNoticeRemindEntity> Officials;

    public String getServerTime(){return ServerTime;}

    public void setServerTime(String ServerTime) {
        this.ServerTime = ServerTime;
    }


    public List<OfficeNoticeRemindEntity> getOfficeNoticeReminds() {
        return Officials;
    }

    public void setOfficeNoticeReminds(List<OfficeNoticeRemindEntity> Officials) {
        this.Officials = Officials;
    }

    public static class OfficeNoticeRemindEntity{
        private int Id;  //公文或公告ID
        private String RunId;//运行id
        private String Time;
        private String Content;
        private int Status;

        public int getId() {
            return Id;
        }

        public void setId(int Id) {
            this.Id = Id;
        }

        public String getRunId() {
            return RunId;
        }

        public void setRunId(String runId) {
            this.RunId = runId;
        }

        public String getTime() {
            return Time;
        }

        public void setTime(String Time) {
            this.Time = Time;
        }

        public String getContent() {
            return Content;
        }

        public void setContent(String Content) {
            this.Content = Content;
        }

        public int getStatus(){
            return Status;
        }

        public void setStatus(int Status){
            this.Status =Status;
        }
    }

}

