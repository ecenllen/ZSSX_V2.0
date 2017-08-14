package com.gta.zssx.mobileOA.model.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lan.zheng on 2016/11/14.  针对接口20
 */
@Deprecated
public class DutyRegisterOrCheckInfo implements Serializable {
    private int Status;   //已登记未登记
    private String ServiceDate;  //服务器
    private String Date;     //日期
//    private String ServiceDate;  //时间
//    private String ServiceDate;  //时间
    private List<Object> TimeList;  //这里用object,里面有TimeId和Time,适配器共用，勿乱改

    public int getStatus(){return Status;}

    public void setStatus(int Status) {
        this.Status = Status;
    }

    public String getServiceDate(){return ServiceDate;}

    public void setServiceDate(String ServiceDate) {
        this.ServiceDate = ServiceDate;
    }

    public String getDate(){return Date;}

    public void setDate(String Date) {
        this.Date = Date;
    }

    public List<Object> getTimeList(){return TimeList;}

    public void setTimeList(List<Object> TimeList) {
        this.TimeList = TimeList;
    }

    /*public List<TimeEntity> getTimeList(){return TimeList;}

    public void setTimeList(List<TimeEntity> TimeList) {
        this.TimeList = TimeList;
    }

    public static class TimeEntity{

    }*/
}
