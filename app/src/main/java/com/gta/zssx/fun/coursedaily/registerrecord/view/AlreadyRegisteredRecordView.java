package com.gta.zssx.fun.coursedaily.registerrecord.view;

import com.gta.utils.mvp.BaseView;
import com.gta.zssx.fun.coursedaily.registerrecord.model.DTO.RegisteredRecordDto;

/**
 * Created by lan.zheng on 2016/6/16.
 */
public interface AlreadyRegisteredRecordView extends BaseView {
    void showResultList(RegisteredRecordDto registeredRecordDto,boolean isRefresh);
    void notResult();  //无法获取服务器时间
    void setDate(String date);  //设置时间
    void getRegisterData(String beginDate,String endDate);  //获取已登记数据
    void getTeacherClassInfoReturn(boolean isSuccess);  //更新是否是老师

    /**
     * 加载更多的情况判断
     * @param isNotResult
     * @param msg
     */
    void setRefreshFalseandDealWithView(boolean isNotResult, String msg);  //msg,用于判断是无网络时候的什么情况：1.上拉无网，2.换日期无网，3，其他情况等

    /**
     * 刷新失败
     */
    void onRefreshError();

    //上拉下拉操作
    /*void LoadFirst(List<RegisteredRecordDto.recordEntry> recordEntryList);

    void LoadMore(int page, List<RegisteredRecordDto.recordEntry> recordEntryList);

    void LoadEnd(int page);*/
}
