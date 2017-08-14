package com.gta.zssx.fun.adjustCourse.deprecated.presenter;

import com.gta.utils.mvp.BasePresenter;
import com.gta.zssx.AppConfiguration;
import com.gta.zssx.fun.adjustCourse.model.AdjustDataManager;
import com.gta.zssx.fun.adjustCourse.model.bean.ApplyRecordBean;
import com.gta.zssx.fun.adjustCourse.deprecated.view.ApplyRecordView;
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
 * @author Created by Zhimin.Huang on 2016/10/24.
 * @since 1.0.0
 */
public class ApplyRecordPresenter extends BasePresenter<ApplyRecordView> {

    private UserBean mUserBean;


    public ApplyRecordPresenter() {
        try {
            mUserBean = AppConfiguration.getInstance().getUserBean();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getApplyRecord(int page) {
        if (getView() == null)
            return;
        mCompositeSubscription.add(AdjustDataManager.getRecordBean(page, mUserBean.getUserId())
                .subscribe(new Subscriber<ApplyRecordBean>() {
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
                    public void onNext(ApplyRecordBean applyRecordBean) {
                        if (applyRecordBean != null) {
                            if (applyRecordBean.getList() != null && applyRecordBean.getList().size() > 0) {
                                if (page == 1) {
                                    getView().LoadFirst(applyRecordBean);
                                } else if (1 < page && page <= applyRecordBean.getTotalPage()) {
                                    getView().LoadMore(page, applyRecordBean);
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
