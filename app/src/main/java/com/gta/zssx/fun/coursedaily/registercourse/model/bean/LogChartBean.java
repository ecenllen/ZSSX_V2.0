package com.gta.zssx.fun.coursedaily.registercourse.model.bean;

import java.util.List;

/**
 * 课堂日志-图表
 * Created by xiao.peng on 2016/11/10.
 */
public class LogChartBean {

    /**
     * 今日迟到
     */
    private String todayLate;
    /**
     * 近5日迟到
     */
    private List<String> late;
    /**
     * 今日请假
     */
    private String todayVacate;
    /**
     * 近5日请假
     */
    private List<String> vacate;
    /**
     * 今日旷课
     */
    private String todayTruant;
    /**
     * 近5日旷课
     */
    private List<String> truant;
    /**
     * 今日公假
     */
    private String todaySabbaticals;
    /**
     * 近5日公假
     */
    private List<String> sabbaticals;

    public String getTodayLate() {
        return todayLate;
    }

    public void setTodayLate(String todayLate) {
        this.todayLate = todayLate;
    }

    public List<String> getLate() {
        return late;
    }

    public void setLate(List<String> late) {
        this.late = late;
    }

    public String getTodayVacate() {
        return todayVacate;
    }

    public void setTodayVacate(String todayVacate) {
        this.todayVacate = todayVacate;
    }

    public List<String> getVacate() {
        return vacate;
    }

    public void setVacate(List<String> vacate) {
        this.vacate = vacate;
    }

    public String getTodayTruant() {
        return todayTruant;
    }

    public void setTodayTruant(String todayTruant) {
        this.todayTruant = todayTruant;
    }

    public List<String> getTruant() {
        return truant;
    }

    public void setTruant(List<String> truant) {
        this.truant = truant;
    }

    public String getTodaySabbaticals() {
        return todaySabbaticals;
    }

    public void setTodaySabbaticals(String todaySabbaticals) {
        this.todaySabbaticals = todaySabbaticals;
    }

    public List<String> getSabbaticals() {
        return sabbaticals;
    }

    public void setSabbaticals(List<String> sabbaticals) {
        this.sabbaticals = sabbaticals;
    }
}
