package com.gta.zssx.fun.adjustCourse.view;

import com.gta.utils.mvp.BaseView;
import com.gta.zssx.fun.adjustCourse.model.bean.ScheduleBean;

import org.joda.time.DateTime;

/**
 * [Description]
 * <p>
 * [How to use]
 * <p>
 * [Tips]
 *
 * @author Created by Zhimin.Huang on 2016/10/24.
 * @since 1.0.0
 */
public interface CourseScheduleView extends BaseView {
    void showResult(ScheduleBean scheduleBean);

    void showDateString(String date, DateTime dateTime,String day[]);
}
