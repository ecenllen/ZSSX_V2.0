package com.gta.zssx.fun.adjustCourse.presenter;

import com.gta.utils.mvp.BasePresenter;
import com.gta.zssx.AppConfiguration;
import com.gta.zssx.fun.adjustCourse.model.AdjustDataManager;
import com.gta.zssx.fun.adjustCourse.model.bean.ApplyBean;
import com.gta.zssx.fun.adjustCourse.model.bean.ApplyConfirmBean;
import com.gta.zssx.fun.adjustCourse.view.ConfirmInnerView;
import com.gta.zssx.fun.personalcenter.model.bean.UserBean;
import com.gta.zssx.pub.exception.CustomException;

import rx.Subscriber;

/**
 * [Description]
 * <p>
 * [How to use]
 * <p>
 * [Tips]
 *
 * @author Created by Zhimin.Huang on 2017/3/20.
 * @since 1.0.0
 */
public class ConfirmInnerPresenter extends BasePresenter<ConfirmInnerView> {

    private UserBean mUserBean;

    public ConfirmInnerPresenter() {
        try {
            mUserBean = AppConfiguration.getInstance().getUserBean();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param page   页码
     * @param type   0未确认列表，1为审核列表
     * @param status N为未结束，Y为已结束
     */
    public void getApplyConfirm(int page, int type, String status) {
        if (getView() == null)
            return;
        mCompositeSubscription.add(AdjustDataManager.getConfirmBean(page, mUserBean.getUserId(), type, status)
                .subscribe(new Subscriber<ApplyConfirmBean>() {
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
                            getView().getResultData(new ApplyConfirmBean());
                        }

                    }

                    @Override
                    public void onNext(ApplyConfirmBean applyConfirmBean) {
                        getView().getResultData(applyConfirmBean);
                    }
                }));
    }
}
