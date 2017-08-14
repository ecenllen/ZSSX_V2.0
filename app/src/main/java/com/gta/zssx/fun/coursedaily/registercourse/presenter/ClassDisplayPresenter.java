package com.gta.zssx.fun.coursedaily.registercourse.presenter;

import com.gta.utils.mvp.BasePresenter;
import com.gta.zssx.AppConfiguration;
import com.gta.zssx.fun.coursedaily.registercourse.model.ClassDataManager;
import com.gta.zssx.fun.coursedaily.registercourse.model.bean.ClassDisplayBean;
import com.gta.zssx.fun.coursedaily.registercourse.view.ClassDisplayView;
import com.gta.zssx.fun.personalcenter.model.bean.UserBean;

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
 * @author Created by Zhimin.Huang on 2016/6/15.
 * @since 1.0.0
 */
public class ClassDisplayPresenter extends BasePresenter<ClassDisplayView> {


    public List<ClassDisplayBean> mClassDisplayBeans;
    public Subscription mSubscription;

    public void getClassList() {
        if (!isViewAttached())
            return;
        UserBean lUserBean = null;
        try {
            lUserBean = AppConfiguration.getInstance().getUserBean();
        } catch (Exception e) {
            e.printStackTrace();
        }

        List<ClassDisplayBean> lClassDisplay = ClassDataManager.getDataCache().getClassDisplay();
        if (lClassDisplay != null) {
            getView().showResult(lClassDisplay);
//            mClassDisplayBeans = lClassDisplay;
            return;
        }

        getView().showLoadingDialog();
        mSubscription = ClassDataManager.getClassList(lUserBean.getUserId())
                .subscribe(new Subscriber<List<ClassDisplayBean>>() {
                    @Override
                    public void onCompleted() {
                        getView().hideDialog();
                    }

                    @Override
                    public void onError(Throwable e) {
//                        if (e instanceof CustomException) {
//                            CustomException lErrorCodeException = (CustomException) e;
//                            getView().showWarning(lErrorCodeException.getMessage());
//                        }
//
//                        if (e instanceof SocketTimeoutException) {
//                            getView().showWarning("请求超时，请稍候重试");
//                        }
                        getView().onErrorHandle(e);
                        getView().hideDialog();
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

}
