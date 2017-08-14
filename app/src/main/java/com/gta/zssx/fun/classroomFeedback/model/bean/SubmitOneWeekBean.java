package com.gta.zssx.fun.classroomFeedback.model.bean;

import java.io.Serializable;

/**
 * [Description]
 * <p> 送审实体
 * [How to use]
 * <p>
 * [Tips]
 *
 * @author Created by liang.lu on 2017/6/28 10:27.
 * @since 2.0.0
 */

public class SubmitOneWeekBean implements Serializable {
    private int ClassId;
    private int WeekDate;

    public int getClassId () {
        return ClassId;
    }

    public void setClassId (int classId) {
        ClassId = classId;
    }

    public int getWeekDate () {
        return WeekDate;
    }

    public void setWeekDate (int weekDate) {
        WeekDate = weekDate;
    }
}
