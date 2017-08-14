package com.gta.zssx.fun.adjustCourse.model.bean;

import java.io.Serializable;

/**
 * [Description]
 * <p>
 * [How to use]
 * <p>
 * [Tips]
 *
 * @author Created by Zhimin.Huang on 2017/3/27.
 * @since 1.0.0
 */
public class HasTimeScheduleBean implements Serializable {

    /**
     * hasCourseTime : 1:4,1:3,1:2,1:1,2:3,2:4,3:1,3:2,2:6,5:3,2:5,5:1,6:2,5:4,6:1,5:2,2:1,4:2,4:1,2:2,3:5,5:6,3:6,5:5,3:4,3:3,4:4,4:3,4:6,4:5
     * maxUnit : 9
     * isSaturdayHasCourse : true
     */

    private String hasCourseTime;
    private int maxUnit;
    private boolean isSaturdayHasCourse;

    public String getHasCourseTime() {
        return hasCourseTime;
    }

    public void setHasCourseTime(String hasCourseTime) {
        this.hasCourseTime = hasCourseTime;
    }

    public int getMaxUnit() {
        return maxUnit;
    }

    public void setMaxUnit(int maxUnit) {
        this.maxUnit = maxUnit;
    }

    public boolean isIsSaturdayHasCourse() {
        return isSaturdayHasCourse;
    }

    public void setIsSaturdayHasCourse(boolean isSaturdayHasCourse) {
        this.isSaturdayHasCourse = isSaturdayHasCourse;
    }
}
