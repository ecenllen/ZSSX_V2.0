package com.gta.zssx.mobileOA.presenter;

import android.util.Log;

import com.gta.utils.mvp.BasePresenter;
import com.gta.zssx.ZSSXApplication;
import com.gta.zssx.mobileOA.model.OAMainModel;
import com.gta.zssx.mobileOA.model.bean.ApplyFormInfo;
import com.gta.zssx.mobileOA.model.bean.MyApplyInfo;
import com.gta.zssx.mobileOA.view.ApplyView;
import com.gta.zssx.pub.common.Constant;

import rx.Subscriber;
import rx.Subscription;

/**
 * Created by xiaoxia.rang on 2016/11/14.
 * 发起申请presenter
 */

public class ApplyPresenter extends BasePresenter<ApplyView> {
    public Subscription mSubscribe;
    public int totalCount;
    public int pageIndex= 1;
    public String UserId;

    public ApplyPresenter() {
        UserId = ZSSXApplication.instance.getUser().getUserId();
    }

    /**
     * 获取申请表
     */
    public void getApplyFormList() {
        if (!isViewAttached()) {
            return;
        }
        getView().showLoadingDialog();
        mSubscribe = OAMainModel.getApplyList(UserId)
                .subscribe(new Subscriber<ApplyFormInfo>() {
                    @Override
                    public void onCompleted() {
                        if (isViewAttached()) {

                        }

                    }

                    @Override
                    public void onError(Throwable throwable) {
                        if (isViewAttached()) {
                            Log.e("error", throwable.toString());
                            getView().hideDialog();
                            getView().showError("请求失败！");
                        }

                    }

                    @Override
                    public void onNext(ApplyFormInfo applyFormInfo) {
                        if (isViewAttached()) {
                            getView().hideDialog();
                            if (applyFormInfo.getApplyFormList() == null || applyFormInfo.getApplyFormList().isEmpty()) {
                                getView().showEmpty();
                            } else {
                                getView().showFormList(applyFormInfo.getApplyFormList());
                            }
                        }
                    }
                });
        mCompositeSubscription.add(mSubscribe);
    }


    /**
     * 获取我的申请列表
     */
    public void getMyApplyList(int status) {

        if (!isViewAttached()) {
            return;
        }
        if (status == Constant.REFRESH) {
            pageIndex = 1;
        }else{
          if(Constant.LOAD_DATA_SIZE*pageIndex >= totalCount){
              getView().onLoadMoreEmpty();
              return;
          }else{
              pageIndex +=1;
          }
        }
        getView().showLoadingDialog();
        mSubscribe = OAMainModel.getMyApplyList(UserId, pageIndex, Constant.LOAD_DATA_SIZE)
                .subscribe(new Subscriber<MyApplyInfo>() {
                    @Override
                    public void onCompleted() {
                        if (isViewAttached()) {

                        }

                    }

                    @Override
                    public void onError(Throwable throwable) {
                        if (isViewAttached()) {
                            getView().hideDialog();
                            if (status == Constant.REFRESH) {
                                getView().onRefreshError();
                            } else {
                                getView().onLoadMoreError();
                            }
                        }

                    }

                    @Override
                    public void onNext(MyApplyInfo backlogInfo) {
                        if (isViewAttached()) {
                            getView().hideDialog();
                            totalCount = backlogInfo.getTotalCount();
                            if (backlogInfo.getBacklogs() == null||backlogInfo.getBacklogs().isEmpty()) {
                                if (status == Constant.REFRESH) {
                                    getView().showEmpty();
                                } else {
                                    getView().onLoadMoreEmpty();
                                }
                            } else {
                                if (status == Constant.REFRESH) {
                                    getView().setServerTime(backlogInfo.getServerTime());
                                    getView().showRefreshApplyList(backlogInfo.getBacklogs());
                                } else {
                                    getView().showLoadMoreApplyList(backlogInfo.getBacklogs());
                                }
                            }
                        }
                    }
                });
        mCompositeSubscription.add(mSubscribe);
    }

}
