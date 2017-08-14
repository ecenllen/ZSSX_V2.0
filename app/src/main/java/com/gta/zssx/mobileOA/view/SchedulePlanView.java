package com.gta.zssx.mobileOA.view;

import com.gta.utils.mvp.BaseView;
import com.gta.zssx.mobileOA.model.bean.Schedule;

import java.util.List;

/**
 * Created by lan.zheng on 2016/10/17.
 */
public interface SchedulePlanView extends BaseView {
    //获取服务器时间
    void getServerDate(String date ,int year,int month, int day);

    //获取当月日程的安排列表,成功失败都用它
    void getScheduleList(List<Schedule> all);

    //删除日程
    void deleteLocalSchedules(int id); //对应一个presenter方法

    //更新日程状态
    void updateLocalSchedules(int id, int status);  //对应一个presenter方法

    //获取一条日程详细内容
    void getSingleSchedulesInfo(boolean isSuccess,int id, Schedule schedule);
}
