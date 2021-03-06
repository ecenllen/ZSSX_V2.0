package com.gta.zssx.fun.adjustCourse.deprecated.presenter;

import com.gta.utils.mvp.BasePresenter;
import com.gta.zssx.AppConfiguration;
import com.gta.zssx.fun.adjustCourse.model.AdjustDataManager;
import com.gta.zssx.fun.adjustCourse.model.bean.ApplyConfirmBean;
import com.gta.zssx.fun.adjustCourse.deprecated.view.ApplyConfirmView;
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
 * @author Created by Zhimin.Huang on 2016/10/28.
 * @since 1.0.0
 */
public class ApplyConfirmPresenter extends BasePresenter<ApplyConfirmView> {
    private UserBean mUserBean;

    public ApplyConfirmPresenter() {
        try {
            mUserBean = AppConfiguration.getInstance().getUserBean();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getApplyConfirm(int page,int type,String status) {
        if (getView() == null)
            return;
        mCompositeSubscription.add(AdjustDataManager.getConfirmBean(page, mUserBean.getUserId(),type,status)
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
                            getView().onRefreshError(page);
                            getView().showEmpty("暂无记录");
                        }
                    }

                    @Override
                    public void onNext(ApplyConfirmBean applyConfirmBean) {
                        if (applyConfirmBean != null) {
                            if (applyConfirmBean.getList() != null && applyConfirmBean.getList().size() > 0) {
                                if (page == 1) {
                                    getView().LoadFirst(applyConfirmBean);
                                } else if (1 < page && page <= applyConfirmBean.getTotalPage()) {
                                    getView().LoadMore(page, applyConfirmBean);
                                } else {
                                    getView().LoadEnd(page);
                                }
                            } else {
                                if (page == 1) {
                                    getView().showEmpty("暂无记录");
                                } else {
                                    getView().LoadEnd(page);
                                }
                            }
                        }
                    }
                }));
    }
}
