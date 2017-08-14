package com.gta.zssx.mobileOA.presenter;

import com.gta.utils.mvp.BasePresenter;
import com.gta.zssx.ZSSXApplication;
import com.gta.zssx.mobileOA.model.OAMainModel;
import com.gta.zssx.mobileOA.model.bean.DutyDateInfo;
import com.gta.zssx.mobileOA.view.DutyDateSelectView;
import com.gta.zssx.pub.exception.CustomException;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;
import rx.Subscription;

/**
 * Created by lan.zheng on 2016/11/15.
 */
public class DutyDateSelectPresenter extends BasePresenter<DutyDateSelectView> {


    public String UserId;
    public Subscription mSubscribe;
//    public boolean mIsFutureDate = false;  //是否是未来时间


    public DutyDateSelectPresenter() {
        UserId = ZSSXApplication.instance.getUser().getUserId();
    }

    /**
     * 获取值班日期列表
     */
    public void getRegisterDateListData(int DutyDetailId,int Status) {
        if (!isViewAttached()) {
            return;
        }
        getView().showLoadingDialog();
        mSubscribe = OAMainModel.getDutyRegisterDate(UserId,DutyDetailId,Status)
                .subscribe(new Subscriber<List<DutyDateInfo>>() {

                    @Override
                    public void onCompleted() {
                        if(isViewAttached()) {
                            getView().hideDialog();
                        }

                    }

                    @Override
                    public void onError(Throwable throwable) {
                        if(!isViewAttached()) {
                            return;
                        }
                        getView().hideDialog();
                        if (throwable instanceof CustomException) {   //抛出自定义的异常
                            CustomException lE = (CustomException) throwable;
                            if (lE.getCode() == CustomException.NETWORK_UNAVAILABLE) {
                                getView().showError(throwable.getMessage());
                            }else {
                                getView().onErrorHandle(throwable);  //抛出默认异常
                            }
                        }else {
                            getView().onErrorHandle(throwable);  //抛出默认异常
                        }
                        //TODO 获取失败
                    }

                    @Override
                    public void onNext(List<DutyDateInfo> dutyDateInfoList) {
                        if(!isViewAttached()) {
                            return;
                        }
                        getView().hideDialog();

                    }
                });
        mCompositeSubscription.add(mSubscribe);
    }

    public ArrayList<Object> testList(){
        ArrayList<Object> list = new ArrayList<>();
        DutyDateInfo dutyDateInfo1 = new DutyDateInfo();
        DutyDateInfo dutyDateInfo2 = new DutyDateInfo();
        dutyDateInfo1.setDate("2016-10-10 00:00:00");
        dutyDateInfo1.setStatus(1);  //已提交
        dutyDateInfo2.setDate("2016-11-11 00:00:00");
        dutyDateInfo2.setStatus(0);  //未提交
        list.add(dutyDateInfo1);
        list.add(dutyDateInfo1);
        list.add(dutyDateInfo1);
        list.add(dutyDateInfo1);
        list.add(dutyDateInfo1);
        list.add(dutyDateInfo2);
        list.add(dutyDateInfo2);
        list.add(dutyDateInfo2);
        list.add(dutyDateInfo2);
        return list;
    }

    public ArrayList<Object> testList1(){
        ArrayList<Object> list = new ArrayList<>();
        DutyDateInfo dutyDateInfo1 = new DutyDateInfo();
        DutyDateInfo dutyDateInfo2 = new DutyDateInfo();
        dutyDateInfo1.setDate("2016-11-12 00:00:00");
        dutyDateInfo1.setStatus(1);  //未检查
        dutyDateInfo2.setDate("2016-11-13 00:00:00");
        dutyDateInfo2.setStatus(2);  //已检查
        list.add(dutyDateInfo1);
        list.add(dutyDateInfo1);
        list.add(dutyDateInfo1);
        list.add(dutyDateInfo2);
        list.add(dutyDateInfo2);
        list.add(dutyDateInfo2);
        list.add(dutyDateInfo2);
        list.add(dutyDateInfo2);
        list.add(dutyDateInfo2);
        return list;
    }

    public ArrayList<Object> testListRegister(){
        //未值班都是不可登记
        ArrayList<Object> list = new ArrayList<>();
        DutyDateInfo dutyDateInfo1 = new DutyDateInfo();
        DutyDateInfo dutyDateInfo2 = new DutyDateInfo();
        dutyDateInfo1.setDate("2016-11-18 00:00:00");
        dutyDateInfo1.setStatus(2);
        dutyDateInfo2.setDate("2016-11-18 00:00:00");
        dutyDateInfo2.setStatus(2);
        list.add(dutyDateInfo1);
        list.add(dutyDateInfo1);
        list.add(dutyDateInfo1);
        list.add(dutyDateInfo2);
        list.add(dutyDateInfo2);
        list.add(dutyDateInfo2);
        list.add(dutyDateInfo2);
        list.add(dutyDateInfo2);
        list.add(dutyDateInfo2);
        return list;
    }

    public ArrayList<Object> testListCheck(){
        //未值班都是不可检查
        ArrayList<Object> list = new ArrayList<>();
        DutyDateInfo dutyDateInfo1 = new DutyDateInfo();
        DutyDateInfo dutyDateInfo2 = new DutyDateInfo();
        dutyDateInfo1.setDate("2016-11-19 00:00:00");
        dutyDateInfo1.setStatus(1);
        dutyDateInfo2.setDate("2016-11-19 00:00:00");
        dutyDateInfo2.setStatus(1);
        list.add(dutyDateInfo1);
        list.add(dutyDateInfo1);
        list.add(dutyDateInfo1);
        list.add(dutyDateInfo2);
        list.add(dutyDateInfo2);
        list.add(dutyDateInfo2);
        list.add(dutyDateInfo2);
        list.add(dutyDateInfo2);
        list.add(dutyDateInfo2);
        return list;
    }
}
