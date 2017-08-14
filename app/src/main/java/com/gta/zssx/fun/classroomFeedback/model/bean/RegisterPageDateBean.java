package com.gta.zssx.fun.classroomFeedback.model.bean;

/**
 * [Description]
 * <p> 登记页面日期实体
 * [How to use]
 * <p>
 * [Tips]
 *
 * @author Created by liang.lu on 2017/6/28 10:27.
 * @since 2.0.0
 */

public class RegisterPageDateBean {
    /**
     * 日期
     */
    private String Date;
    /**
     * 用于判断当天时候登记完成
     */
    private boolean IsComplete;

    public String getDate () {
        return Date;
    }

    public void setDate (String date) {
        Date = date;
    }

    public boolean isComplete () {
        return IsComplete;
    }

    public void setComplete (boolean complete) {
        IsComplete = complete;
    }
}
