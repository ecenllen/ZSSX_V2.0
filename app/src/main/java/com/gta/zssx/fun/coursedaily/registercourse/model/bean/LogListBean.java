package com.gta.zssx.fun.coursedaily.registercourse.model.bean;


/**
 * 日志统计-列表
 * Created by xiao.peng on 2016/11/10.
 */
public class LogListBean {

    /**
     * 专业部
     */
    private String mName;
    /**
     * 专业部门Id
     */
    private String mId;
    /**
     * 班级名称
     */
    private String cName;
    /**
     * 班级Id
     */
    private String cId;
    /**
     * 迟到
     */
    private String late;
    /**
     * 请假
     */
    private String vacate;
    /**
     * 旷课
     */
    private String truant;
    /**
     * 公假
     */
    private String sabbaticals;

    /**
     * 是否是最大值
     */
    private boolean maxLate;
    private boolean maxVacate;
    private boolean maxTruant;
    private boolean maxSabbaticals;

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getSabbaticals() {
        return sabbaticals;
    }

    public void setSabbaticals(String sabbaticals) {
        this.sabbaticals = sabbaticals;
    }

    public String getTruant() {
        return truant;
    }

    public void setTruant(String truant) {
        this.truant = truant;
    }

    public String getVacate() {
        return vacate;
    }

    public void setVacate(String vacate) {
        this.vacate = vacate;
    }

    public String getLate() {
        return late;
    }

    public void setLate(String late) {
        this.late = late;
    }

    public String getcName() {
        return cName;
    }

    public void setcName(String cName) {
        this.cName = cName;
    }

    public String getmId() {
        return mId;
    }

    public void setmId(String mId) {
        this.mId = mId;
    }

    public String getcId() {
        return cId;
    }

    public void setcId(String cId) {
        this.cId = cId;
    }

    public boolean isMaxLate() {
        return maxLate;
    }

    public void setMaxLate(boolean maxLate) {
        this.maxLate = maxLate;
    }

    public boolean isMaxVacate() {
        return maxVacate;
    }

    public void setMaxVacate(boolean maxVacate) {
        this.maxVacate = maxVacate;
    }

    public boolean isMaxTruant() {
        return maxTruant;
    }

    public void setMaxTruant(boolean maxTruant) {
        this.maxTruant = maxTruant;
    }

    public boolean isMaxSabbaticals() {
        return maxSabbaticals;
    }

    public void setMaxSabbaticals(boolean maxSabbaticals) {
        this.maxSabbaticals = maxSabbaticals;
    }
}
