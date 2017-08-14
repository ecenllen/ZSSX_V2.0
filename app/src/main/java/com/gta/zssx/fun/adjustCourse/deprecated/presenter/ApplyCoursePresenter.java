package com.gta.zssx.fun.adjustCourse.deprecated.presenter;

import com.gta.utils.mvp.BasePresenter;
import com.gta.zssx.AppConfiguration;
import com.gta.zssx.fun.adjustCourse.model.AdjustDataManager;
import com.gta.zssx.fun.adjustCourse.model.bean.ApplyBean;
import com.gta.zssx.fun.adjustCourse.deprecated.view.ApplyCourseView;
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
public class ApplyCoursePresenter extends BasePresenter<ApplyCourseView> {
    private UserBean mUserBean;

    public ApplyCoursePresenter() {
        try {
            mUserBean= AppConfiguration.getInstance().getUserBean();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getApply(int page) {
        if (getView() == null)
            return;
        mCompositeSubscription.add(AdjustDataManager.getApplyBean(page, mUserBean.getUserId(),"N")
                .subscribe(new Subscriber<ApplyBean>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (getView() != null) {
                            CustomException lCustomException = (CustomException) e;
                            if (!lCustomException.getMessage().equals("java.lang.NullPointerException")) {
                                getView().onErrorHandle(e);
                            }
                            getView().onRefreshError(page);
                            getView().showEmpty("暂无记录");
                        }
                    }

                    @Override
                    public void onNext(ApplyBean applyBean) {
                        if (applyBean != null) {
                            if (applyBean.getList() != null && applyBean.getList().size() > 0) {
                                if (page == 1) {
                                    getView().LoadFirst(applyBean);
                                } else if (1 < page && page <= applyBean.getTotalPage()) {
                                    getView().LoadMore(page, applyBean);
                                } else {
                                    getView().LoadEnd(page);
                                }
                            } else {
                                if (page==1){
                                    getView().showEmpty("暂无记录");
                                }else {
                                    getView().LoadEnd(page);
                                }
                            }
                        }
                    }
                }));

    }
}
