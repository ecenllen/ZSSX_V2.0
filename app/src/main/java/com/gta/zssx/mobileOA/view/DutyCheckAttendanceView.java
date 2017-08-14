package com.gta.zssx.mobileOA.view;

import com.gta.utils.mvp.BaseView;
import com.gta.zssx.mobileOA.model.bean.DutyRegisterOrCheckInfo;

/**
 * Created by lan.zheng on 2016/11/21.
 */
public interface DutyCheckAttendanceView  extends BaseView {
    void showAllEmptyPage();     //获取时间失败显示空页面
    void getCheckTimePeriodSuccess(DutyRegisterOrCheckInfo dutyRegisterOrCheckInfo, boolean isCanCheck);  //用于检查
}
