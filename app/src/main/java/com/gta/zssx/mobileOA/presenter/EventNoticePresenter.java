package com.gta.zssx.mobileOA.presenter;

import com.gta.utils.mvp.BasePresenter;
import com.gta.zssx.ZSSXApplication;
import com.gta.zssx.mobileOA.model.OAMainModel;
import com.gta.zssx.mobileOA.model.bean.EventNoticeInfo;
import com.gta.zssx.mobileOA.view.EventNoticeView;
import com.gta.zssx.mobileOA.view.page.EventNoticeActivity;
import com.gta.zssx.pub.common.Constant;
import com.gta.zssx.pub.exception.CustomException;

import rx.Subscriber;
import rx.Subscription;

/**
 * Created by lan.zheng on 2016/10/31.
 */
public class EventNoticePresenter extends BasePresenter<EventNoticeView> {
    public Subscription mSubscribe;
    private String UserId;

    public EventNoticePresenter() {
        UserId = ZSSXApplication.instance.getUser().getUserId();
    }

    public void getEventsList(int action ,int index) {
        if (!isViewAttached()) {
            return;
        }
        getView().showLoadingDialog();
        mSubscribe = OAMainModel.getEventNoticeList(UserId,index,Constant.LOAD_DATA_SIZE)
                .subscribe(new Subscriber<EventNoticeInfo>() {
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
                        if(action == EventNoticeActivity.REFRESH){
                            getView().onRefreshError();
//                            getView().showEmptyView();
                        }else{
                            getView().onLoadMoreError();
                        }
                    }

                    @Override
                    public void onNext(EventNoticeInfo eventNoticeInfo) {
                        if(!isViewAttached()) {
                            return;
                        }
                        if(eventNoticeInfo.getEventRemind().size() == 0){
                            if(action == EventNoticeActivity.REFRESH){
                                getView().showEmptyView(); //空页面
                            }else{
                                getView().onLoadMoreEmpty();  //无法加载更多
                            }
                        }else {
                            getView().showEventList(eventNoticeInfo,action);
                        }
                    }
                });
        mCompositeSubscription.add(mSubscribe);
    }

}
