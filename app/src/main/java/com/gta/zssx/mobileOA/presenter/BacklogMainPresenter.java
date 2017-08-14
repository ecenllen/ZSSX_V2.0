package com.gta.zssx.mobileOA.presenter;

import android.util.Log;

import com.gta.utils.mvp.BasePresenter;
import com.gta.zssx.AppConfiguration;
import com.gta.zssx.fun.personalcenter.model.bean.UserBean;
import com.gta.zssx.mobileOA.model.OAMainModel;
import com.gta.zssx.mobileOA.model.bean.BacklogInfo;
import com.gta.zssx.mobileOA.view.BacklogMainView;
import com.gta.zssx.pub.common.Constant;

import rx.Subscriber;
import rx.Subscription;

/**
 * Created by xiaoxia.rang on 2016/10/27.
 * 待办、已办presenter
 */

public class BacklogMainPresenter extends BasePresenter<BacklogMainView> {
    public Subscription mSubscribe;
    public int pageIndex = 1;
    public int totalCount;

    public void getBacklogs(int opStatus, int type) {
        if (!isViewAttached()) {
            return;
        }
        if (opStatus == Constant.LOAD_MORE) {
            if(Constant.LOAD_DATA_SIZE*pageIndex >= totalCount){
                getView().onLoadMoreEmpty();
                return;
            }else{
                pageIndex +=1;
            }
        } else {
            pageIndex = 1;
        }
        getView().showLoadingDialog();

        mSubscribe = OAMainModel.getTaskList(getUserName(), Constant.LOAD_DATA_SIZE, pageIndex, type, null, null, null, null, null).subscribe(new Subscriber<BacklogInfo>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (isViewAttached()) {
                    getView().hideDialog();
                    Log.e("taskError", e.toString());
                    if(opStatus == Constant.LOAD_MORE){
                        getView().onLoadMoreError();
                    }else{
                        getView().onRefreshError();
                    }
                }
            }

            @Override
            public void onNext(BacklogInfo backlogInfo) {
                if (isViewAttached()) {
                    getView().hideDialog();
                    totalCount = backlogInfo.getTotalCount();
                    if (backlogInfo.getBacklogs().isEmpty()) {
                        if (opStatus == Constant.LOAD_MORE) {
                            getView().onLoadMoreEmpty();
                        } else {
                            getView().showEmpty();
                        }
                    } else {
                        if (opStatus == Constant.REFRESH) {
                            getView().setServerTime(backlogInfo.getServerTime());
                            getView().refreshBacklogList(backlogInfo.getBacklogs());
                        } else {
                            getView().appendBacklogList(backlogInfo.getBacklogs());
                        }
                    }
                }
            }
        });
        mCompositeSubscription.add(mSubscribe);
    }

    public String getUserName(){
        UserBean mUserBean = null;
        try {
            mUserBean = AppConfiguration.getInstance().getUserBean();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(mUserBean!= null){
            return mUserBean.getAccount();
        }else {
            return "";
        }
    }
}
