package com.gta.zssx.fun.adjustCourse.model.bean;

import java.util.List;
import java.util.Map;

/**
 * [Description]
 * <p>
 * [How to use]
 * <p>
 * [Tips]
 *
 * @author Created by Zhimin.Huang on 2016/11/28.
 * @since 1.0.0
 */
public class RxBusSectionBean {
    private Map<String, List<ScheduleBean.SectionBean>> mSelectedSchedule;

    public Map<String, List<ScheduleBean.SectionBean>> getSelectedSchedule() {
        return mSelectedSchedule;
    }

    public void setSelectedSchedule(Map<String, List<ScheduleBean.SectionBean>> selectedSchedule) {
        mSelectedSchedule = selectedSchedule;
    }
}
