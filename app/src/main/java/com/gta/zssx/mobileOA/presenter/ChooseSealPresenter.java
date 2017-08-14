package com.gta.zssx.mobileOA.presenter;

import android.util.Log;

import com.gta.utils.mvp.BasePresenter;
import com.gta.zssx.ZSSXApplication;
import com.gta.zssx.mobileOA.model.BpmMainModel;
import com.gta.zssx.mobileOA.model.bean.SealInfo;
import com.gta.zssx.mobileOA.view.ChooseSealView;

import java.util.List;

import rx.Subscriber;
import rx.Subscription;

/**
 * Created by xiaoxia.rang on 2017/5/15.
 */

public class ChooseSealPresenter extends BasePresenter<ChooseSealView> {
    public Subscription mSubscribe;
    public String accout;

    public ChooseSealPresenter() {
        accout = ZSSXApplication.instance.getUser().getUserName();
    }

    public void getSealInfoList() {
        if (!isViewAttached()) {
            return;
        }
        getView().showLoadingDialog();
        mSubscribe = BpmMainModel.getSealInfoList(accout)
                .subscribe(new Subscriber<List<SealInfo>>() {
                    @Override
                    public void onCompleted() {
                        if (isViewAttached()) {

                        }

                    }

                    @Override
                    public void onError(Throwable throwable) {
                        if (isViewAttached()) {
                            Log.e("error", throwable.toString());
                            getView().hideDialog();
                            getView().showError("请求失败！");
                        }

                    }

                    @Override
                    public void onNext(List<SealInfo> sealInfos) {
                        if (isViewAttached()) {
                            getView().hideDialog();
                            if (sealInfos == null || sealInfos.isEmpty()) {
                                getView().showEmpty();
                            } else {
                                getView().showSealInfos(sealInfos);
                            }
                        }
                    }
                });
        mCompositeSubscription.add(mSubscribe);
    }
}
