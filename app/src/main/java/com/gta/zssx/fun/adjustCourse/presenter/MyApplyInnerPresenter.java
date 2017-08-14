package com.gta.zssx.fun.adjustCourse.presenter;

import com.gta.utils.mvp.BasePresenter;
import com.gta.zssx.AppConfiguration;
import com.gta.zssx.fun.adjustCourse.model.AdjustDataManager;
import com.gta.zssx.fun.adjustCourse.model.bean.ApplyBean;
import com.gta.zssx.fun.adjustCourse.view.MyApplyInnerView;
import com.gta.zssx.fun.personalcenter.model.bean.UserBean;
import com.gta.zssx.pub.exception.CustomException;
import com.gta.zssx.pub.util.LogUtil;

import rx.Subscriber;

/**
 * [Description]
 * <p>
 * [How to use]
 * <p>
 * [Tips]
 *
 * @author Created by Zhimin.Huang on 2017/3/6.
 * @since 1.0.0
 */
public class MyApplyInnerPresenter extends BasePresenter<MyApplyInnerView> {

    private UserBean mUserBean;

    public MyApplyInnerPresenter() {
        try {
            mUserBean = AppConfiguration.getInstance().getUserBean();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param page   页码
     * @param status N未结束，Y已结束
     */
    public void getApply(int page, String status) {
        if (getView() == null)
            return;
        mCompositeSubscription.add(AdjustDataManager.getApplyBean(page, mUserBean.getUserId(), status)
                .subscribe(new Subscriber<ApplyBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        if (getView() != null) {
                            if (e instanceof CustomException) {
                                CustomException lCustomException = (CustomException) e;
                                if (!lCustomException.getMessage().equals("java.lang.NullPointerException")) {
                                    getView().onErrorHandle(e);
                                }
                            }
                            getView().getResultData(new ApplyBean());
                        }

                    }

                    @Override
                    public void onNext(ApplyBean applyBean) {
                        getView().getResultData(applyBean);
                    }
                }));

    }
}
