package com.gta.zssx.mobileOA.presenter;

import com.gta.utils.mvp.BasePresenter;
import com.gta.zssx.ZSSXApplication;
import com.gta.zssx.mobileOA.model.OAMainModel;
import com.gta.zssx.mobileOA.model.bean.DutyNoticeInfo;
import com.gta.zssx.mobileOA.model.bean.UserDutyListInfo;
import com.gta.zssx.mobileOA.view.DutyNoticeView;
import com.gta.zssx.mobileOA.view.page.DutyNoticeActivity;
import com.gta.zssx.pub.common.Constant;
import com.gta.zssx.pub.exception.CustomException;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;
import rx.Subscription;

/**
 * Created by lan.zheng on 2016/11/7.
 */
public class DutyNoticePresenter extends BasePresenter<DutyNoticeView> {

    public Subscription mSubscribe;
    private String UserId;

    public DutyNoticePresenter() {
        UserId = ZSSXApplication.instance.getUser().getUserId();
    }

    public void getUserDutyList(int action, int pageIndex){
        if (!isViewAttached()) {
            return;
        }
        getView().showLoadingDialog();
        mSubscribe = OAMainModel.getUserDutyList(UserId,pageIndex,Constant.LOAD_DATA_SIZE)
                .subscribe(new Subscriber<UserDutyListInfo>() {
                    @Override
                    public void onCompleted() {
                        if (isViewAttached()) {
                            getView().hideDialog();
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        if (!isViewAttached()) {
                            return;
                        }
                        getView().hideDialog();
                        if (throwable instanceof CustomException) {   //抛出自定义的异常
                            CustomException lE = (CustomException) throwable;
                            if (lE.getCode() == CustomException.NETWORK_UNAVAILABLE) {
                                getView().showError(throwable.getMessage());
                            } else {
                                getView().onErrorHandle(throwable);  //抛出默认异常
                            }
                        } else {
                            getView().onErrorHandle(throwable);  //抛出默认异常
                        }

                        if(action == DutyNoticeActivity.REFRESH){
                            getView().onRefreshError();
                        }else {
                            getView().onLoadMoreError();
                        }
                    }

                    @Override
                    public void onNext(UserDutyListInfo officeNoticeInfo) {
                        if (!isViewAttached()) {
                            return;
                        }
                        getView().hideDialog();
                        if(officeNoticeInfo.getDutyList().size() == 0){
                            if(action == DutyNoticeActivity.REFRESH){
                                getView().showEmptyView();
                            }else {
                                getView().onLoadMoreEmpty();
                            }
                        }else {
                            getView().showDutyListData(officeNoticeInfo.getDutyList(),action);
                        }
                    }
                });
        mCompositeSubscription.add(mSubscribe);
    }

    public List<DutyNoticeInfo> testData(){
        List<DutyNoticeInfo>  eventNoticeInfo = new ArrayList<>();
        DutyNoticeInfo.LeadersEntity leadersEntity = new DutyNoticeInfo.LeadersEntity();
        leadersEntity.setLeaderId("");
        leadersEntity.setLeaderName("张三");
        List<DutyNoticeInfo.LeadersEntity> list = new ArrayList<>();
        list.add(leadersEntity);
        list.add(leadersEntity);
        list.add(leadersEntity);
        list.add(leadersEntity);

        DutyNoticeInfo remind1 = new DutyNoticeInfo();
        DutyNoticeInfo remind2 = new DutyNoticeInfo();
        DutyNoticeInfo remind3 = new DutyNoticeInfo();

        remind1.setTime("2016-11-10");
        remind1.setDutyName("中秋值班安排表1");
        remind1.setArrangeType(2);  //手动调班
        remind1.setSwitchUserId(UserId);  //申请人
        remind1.setToSwitchUserId("test"); //被申请人
        remind1.setShiftStatus(1);  //未审批
        remind1.setDutyOrCheckDuty(1);
        remind1.setLeaders(list);

        remind2.setTime("2016-11-09");
        remind2.setDutyName("中秋值班安排表2");
        remind2.setArrangeType(2);  //手动调班
        remind2.setSwitchUserId("test");  //申请人
        remind2.setToSwitchUserId(UserId); //被申请人
        remind2.setShiftStatus(1);  //未审批
        remind2.setDutyOrCheckDuty(1);
        remind2.setLeaders(list);

        remind3.setTime("2016-11-01/2016-11-04");
        remind3.setDutyName("中秋值班安排表3");
        remind3.setArrangeType(1);  //值班安排
        remind3.setSwitchUserId("test");  //申请人
        remind3.setToSwitchUserId("test"); //被申请人
        remind3.setShiftStatus(2);  //已审批
        remind3.setDutyOrCheckDuty(2);  //值日检查
        remind3.setLeaders(list);

        eventNoticeInfo.add(remind1);  //调班

        eventNoticeInfo.add(remind2);  //值日登记
        eventNoticeInfo.add(remind2);
        eventNoticeInfo.add(remind2);
        eventNoticeInfo.add(remind3);  //值日检查
        eventNoticeInfo.add(remind3);
        eventNoticeInfo.add(remind3);
        return eventNoticeInfo;
    }

}
