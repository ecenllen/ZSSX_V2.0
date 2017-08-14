package com.gta.zssx.fun.coursedaily.registerrecord.presenter;

import com.gta.utils.mvp.BasePresenter;
import com.gta.zssx.AppConfiguration;
import com.gta.zssx.fun.coursedaily.registercourse.model.ClassDataManager;
import com.gta.zssx.fun.coursedaily.registerrecord.view.AlreadyRegisteredRecordTeacherClassUpdateView;
import com.gta.zssx.fun.personalcenter.model.bean.UserBean;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;
import rx.Subscription;

/**
 * Created by lan.zheng on 2016/7/14.
 */
public class AlreadyRegisteredRecordTeacherClassUpdatePresenter extends BasePresenter<AlreadyRegisteredRecordTeacherClassUpdateView> {

    private UserBean mUserBean;
    public Subscription mSubscribeList;
    public void getTeacherClassUpdate(String TeacherID){
        if (!isViewAttached()) {
            return;
        }

        mSubscribeList = ClassDataManager.getTeacherClassUpdate(TeacherID)
                .subscribe(new Subscriber<UserBean>() {

                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        if(isViewAttached()){
//                            getView().onErrorHandle(e);
                            getView().getTeacherClassInfoReturn(false);
                        }

                    }

                    @Override
                    public void onNext(UserBean userBean) {
                        if(userBean == null){
                            return;
                        }
                        try {
                            mUserBean = AppConfiguration.getInstance().getUserBean();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        boolean IsTeacherName = userBean.isTeacherName();
                        int TotalCount = userBean.getTotalCount();
                        List<UserBean.ClassList> lClassList = new ArrayList<>();
                        if(userBean.getClassName() != null){
                            lClassList = userBean.getClassName();
                        }
                        mUserBean.setTeacherName(IsTeacherName);
                        mUserBean.setTotalCount(TotalCount);
                        mUserBean.setClassName(lClassList);
                        AppConfiguration.getInstance().setUserLoginBean(mUserBean).saveAppConfiguration();
                        if(isViewAttached()){
                            getView().getTeacherClassInfoReturn(true);
                        }

                    }
                });

        mCompositeSubscription.add(mSubscribeList);
    }
}
