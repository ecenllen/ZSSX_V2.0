package com.gta.zssx.mobileOA.view;

import com.gta.utils.mvp.BaseView;
import com.gta.zssx.mobileOA.model.bean.DutyRegisterOrCheckInfo;
import com.gta.zssx.mobileOA.model.bean.DutyTimeListInfo;

/**
 * Created by lan.zheng on 2016/11/14.
 */
public interface DutyRegisterOrCheckView extends BaseView {
    void getRegisterTimePeriodSuccess(DutyTimeListInfo dutyTimeListInfo, boolean isFutureDuty);  //用于登记
    void showAllEmptyView();     //获取时间失败显示空页面
    void showListEmptyView();    //列表为空页面
}
