package com.gta.zssx.fun.coursedaily.registercourse.presenter;

import android.content.Context;

import com.gta.utils.mvp.BasePresenter;
import com.gta.zssx.AppConfiguration;
import com.gta.zssx.fun.coursedaily.registercourse.model.ClassDataManager;
import com.gta.zssx.fun.coursedaily.registercourse.model.bean.ClassDisplayBean;
import com.gta.zssx.fun.coursedaily.registercourse.view.CourseDailyView;
import com.gta.zssx.fun.coursedaily.registercourse.view.pageV2.CourseDailyActivity;
import com.gta.zssx.fun.personalcenter.model.bean.UserBean;
import com.gta.zssx.pub.exception.CustomException;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;
import rx.Subscription;

/**
 * [Description]
 * <p>
 * [How to use]
 * <p>
 * [Tips]
 *
 * @author Created by Zhimin.Huang on 2016/7/4.
 * @since 1.0.0
 */
public class CourseDailyPresenter extends BasePresenter<CourseDailyView> {
    public List<ClassDisplayBean> mClassDisplayBeans;
    public Subscription mSubscription;
    public Subscription mSubscribeList;

    public void getClassList() {
        if (getView() == null)
            return;
        UserBean lUserBean = null;
        try {
            lUserBean = AppConfiguration.getInstance().getUserBean();
        } catch (Exception e) {
            e.printStackTrace();
        }


        getView().showLoadingDialog();

        mSubscription = ClassDataManager.getClassList(lUserBean.getUserId())
                .subscribe(new Subscriber<List<ClassDisplayBean>>() {
                    @Override
                    public void onCompleted() {
//                        getView().hideDialog();
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (getView() != null) {
                            getView().onErrorHandle(e);
                            getView().emptyUI(mClassDisplayBeans == null);
                        }
                    }

                    @Override
                    public void onNext(List<ClassDisplayBean> classDisplayBeen) {
                        mClassDisplayBeans = classDisplayBeen;
                        getView().showResult(classDisplayBeen);
                    }
                });

        mCompositeSubscription.add(mSubscription);
    }

    public List<ClassDisplayBean> getClassDisplayBeans() {
        return mClassDisplayBeans;
    }


    public void getTeacherClassUpdate(final UserBean lUserBean, int flag) {
        if (!isViewAttached()) {
            return;
        }

        mSubscribeList = ClassDataManager.getTeacherClassUpdate(lUserBean.getUserId())
                .subscribe(new Subscriber<UserBean>() {

                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (getView() != null) {
                            if(flag == CourseDailyActivity.FIRST_GET_IS_TEACHER){
                                return;  //第一次进入都不执行下面操作
                            }
                            if (e instanceof CustomException) {
                                CustomException lCustomException = (CustomException) e;
                                if (lCustomException.getCode() == CustomException.NETWORK_UNAVAILABLE) {
                                    getView().isTeacher(lUserBean);
                                } else {
                                    getView().onErrorHandle(e);
                                }
                            } else {
                                getView().onErrorHandle(e);
                            }


                        }

                    }

                    @Override
                    public void onNext(UserBean userBean) {
                        if (userBean == null) {
                            return;
                        }
                        if (getView() == null)
                            return;
                        if(flag != CourseDailyActivity.FIRST_GET_IS_TEACHER){
                            getView().isTeacher(userBean);  //跳转我的班级判断
                        }
                        boolean IsTeacherName = userBean.isTeacherName();
                        int TotalCount = userBean.getTotalCount();
                        List<UserBean.ClassList> lClassList = new ArrayList<>();
                        if (userBean.getClassName() != null) {
                            lClassList = userBean.getClassName();
                        }
                        lUserBean.setTeacherName(IsTeacherName);
                        lUserBean.setTotalCount(TotalCount);
                        lUserBean.setClassName(lClassList);
                        AppConfiguration.getInstance().setUserLoginBean(lUserBean).saveAppConfiguration();
                    }
                });

        mCompositeSubscription.add(mSubscribeList);
    }


    //TODO 删除添加班级
    public void deleteTeacherClass(String ClassId, String TeacherId, final Context context) {
        if (!isViewAttached()) {
            return;
        }

        mSubscribeList = ClassDataManager.deleteTeacherClass(ClassId, TeacherId)
                .subscribe(new Subscriber<String>() {

                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (getView() != null) {
                           getView().onErrorHandle(e);
                        }

                    }

                    @Override
                    public void onNext(String s) {
                        if (getView() == null)
                            return;
                        getView().deleteClassSuccess();
                    }
                });

        mCompositeSubscription.add(mSubscribeList);
    }
}
