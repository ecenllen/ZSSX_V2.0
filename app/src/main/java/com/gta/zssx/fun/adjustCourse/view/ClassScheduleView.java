package com.gta.zssx.fun.adjustCourse.view;

import com.gta.utils.mvp.BaseView;
import com.gta.zssx.fun.adjustCourse.model.bean.HasTimeScheduleBean;

import org.joda.time.DateTime;

/**
 * [Description]
 * <p>
 * [How to use]
 * <p>
 * [Tips]
 *
 * @author Created by Zhimin.Huang on 2017/3/13.
 * @since 1.0.0
 */
public interface ClassScheduleView extends BaseView {
    void getSchedule(HasTimeScheduleBean timeScheduleBean);

    void showDateString(String date, DateTime dateTime, String[] weekDate);
}
