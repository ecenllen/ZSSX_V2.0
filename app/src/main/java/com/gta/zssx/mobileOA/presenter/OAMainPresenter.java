package com.gta.zssx.mobileOA.presenter;

import com.gta.utils.mvp.BasePresenter;
import com.gta.zssx.AppConfiguration;
import com.gta.zssx.fun.personalcenter.model.bean.UserBean;
import com.gta.zssx.mobileOA.model.OAMainModel;
import com.gta.zssx.mobileOA.model.bean.RemindCountInfo;
import com.gta.zssx.mobileOA.view.OAMainView;

import rx.Subscriber;
import rx.Subscription;

/**
 * Created by lan.zheng on 2016/10/11.
 */
public class OAMainPresenter extends BasePresenter<OAMainView> {

    public Subscription mSubscribe;

    public void getRemindCount(String userId, String userName) {

        if (!isViewAttached()) {
            return;
        }
        getView().showLoadingDialog();
        mSubscribe = OAMainModel.getRemindCount(userId,userName).subscribe(new Subscriber<RemindCountInfo>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (isViewAttached()) {
                    getView().hideDialog();
                }
            }

            @Override
            public void onNext(RemindCountInfo remindCountInfo) {
                if (isViewAttached()) {
                    getView().hideDialog();
                    int[] counts = new int[10];
                    counts[1] = remindCountInfo.getTasks();
                    counts[2] = remindCountInfo.getNotice();
                    counts[3] = remindCountInfo.getMeeting();
                    counts[5] = remindCountInfo.getEvents();
//                    counts[7] = remindCountInfo.getMail();
                    getView().showRemindCount(counts);

                }
            }
        });
        mCompositeSubscription.add(mSubscribe);

    }

    public UserBean getUserBean() {
        UserBean mUserBean = null;
        try {
            mUserBean = AppConfiguration.getInstance().getUserBean();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (mUserBean != null) {
            return mUserBean;
        } else {
            return null;
        }
    }

    public class UserInfo{
        String userId;
        String userName;

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }
    }

}
