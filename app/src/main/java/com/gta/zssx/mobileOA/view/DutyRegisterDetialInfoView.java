package com.gta.zssx.mobileOA.view;

import com.gta.utils.mvp.BaseView;
import com.gta.zssx.mobileOA.model.bean.DutyRegisterDetailInfo;

/**
 * Created by lan.zheng on 2016/11/16.
 */
public interface DutyRegisterDetialInfoView extends BaseView {
    void getRegisterDetailInfo(DutyRegisterDetailInfo registerDetailInfo);  //获取登记内容成功

    void showEmptyView();  //获取失败显示空白
}
