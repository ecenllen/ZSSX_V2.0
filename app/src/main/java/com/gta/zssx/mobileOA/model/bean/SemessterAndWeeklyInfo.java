package com.gta.zssx.mobileOA.model.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lan.zheng on 2016/11/2.  用于学期计划和周程表列表
 */
public class SemessterAndWeeklyInfo implements Serializable {
    private String Time;
    private String Content;
    private String Place;
    private String ResponsibleDepartment;
    private String Host;    //学期计划，主持人
    private String Person;  //学期计划，参与人员

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

    public String getPlace() {
        return Place;
    }

    public void setPlace(String Place) {
        this.Place = Place;
    }

    public String getResponsibleDepartment() {
        return ResponsibleDepartment;
    }

    public void setResponsibleDepartment(String ResponsibleDepartment) {
        this.ResponsibleDepartment = ResponsibleDepartment;
    }

    public String getHost() {
        return Host;
    }

    public void setHost(String Host) {
        this.Host = Host;
    }

    public String getPerson() {
        return Person;
    }

    public void setPerson(String Person) {
        this.Person = Person;
    }
}
