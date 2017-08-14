package com.gta.zssx.mobileOA.presenter;

import com.gta.utils.mvp.BasePresenter;
import com.gta.zssx.mobileOA.model.OAMainModel;
import com.gta.zssx.mobileOA.model.bean.DutyTimeListInfo;
import com.gta.zssx.mobileOA.view.DutyRegisterDetialInfoView;
import com.gta.zssx.pub.common.Constant;
import com.gta.zssx.pub.exception.CustomException;

import rx.Subscriber;
import rx.Subscription;

/**
 * Created by lan.zheng on 2016/11/16.
 */
public class DutyRegisterDetialInfoPresenter extends BasePresenter<DutyRegisterDetialInfoView>{
    public Subscription mSubscribe;
    /**
     * 获取时间段列表--值班登记
     */
    public void getRegisterDetailData(int DutyDetailId,String Date) {
    }
}
