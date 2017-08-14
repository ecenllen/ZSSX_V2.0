package com.gta.zssx.mobileOA.view;

import com.gta.utils.mvp.BaseView;
import com.gta.zssx.mobileOA.model.bean.SemessterAndWeeklyInfo;
import com.gta.zssx.mobileOA.model.bean.TermInfo;
import com.gta.zssx.mobileOA.model.bean.TermWeekInfo;

import java.util.List;

/**
 * Created by lan.zheng on 2016/11/2.
 */
public interface SemesterScheduleView extends BaseView {
    void getSemesterData(List<TermInfo> semesterArrayList);  //获取到学期列表

    void getWeekData(TermWeekInfo termWeekInfo);  //获取到学期的周列表

    void showAllEmptyPageWhenGetSemesterFailed();  //第一次进入获取不到数据为空

    void getWeeklyScheduleData(List<SemessterAndWeeklyInfo> semessterAndWeeklyInfoList);  //获取周程内容列表

    void showListEmptyPage(boolean isGetDetailFailed);   //周程列表获取不到数据为空

    void onRefreshError();  //刷新失败
}
