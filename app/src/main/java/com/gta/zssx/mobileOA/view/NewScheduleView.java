package com.gta.zssx.mobileOA.view;

import com.gta.utils.mvp.BaseView;
import com.gta.zssx.mobileOA.model.bean.Schedule;

/**
 * Created by lan.zheng on 2016/10/20.
 */
public interface NewScheduleView extends BaseView {
    void getServerTime(String date,int year ,int month,int day,int hour,int min);
    //删除日程
    void deleteLocalSchedulesSuccess();
    //编辑或新建
    void saveScheduleDataSuccess();
}
