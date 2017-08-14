package com.gta.zssx.fun.assetmanagement.presenter;

import com.gta.utils.mvp.BasePresenter;
import com.gta.zssx.ZSSXApplication;
import com.gta.zssx.fun.assetmanagement.view.AssetManagementView;
import com.gta.zssx.fun.personalcenter.model.LoginDataManager;
import com.gta.zssx.fun.personalcenter.model.bean.UserBean;

import rx.Subscriber;
import rx.functions.Func1;

public class AssetManagementPresenter extends BasePresenter<AssetManagementView> {
    
    private UserBean mUserBean;

    public AssetManagementPresenter () {
        mUserBean = ZSSXApplication.instance.getUser();
    }

    public void getTicket() {
        if(getView() == null)
            return;
        getView().showLoadingDialog();
//        String lMD5password = EncryptUtils.encryptMD5ToString(mUserBean.getMD5Password() + "{000000}");
//        String lPowerHostUrl = mUserBean.getPowerHostUrl() + "SignIn/";
        // 获取ticket 值
        mCompositeSubscription.add(LoginDataManager.getTicket(mUserBean.getUserName().toUpperCase(), mUserBean.getMD5Password(), mUserBean.getPowerHostUrl(), mUserBean.getTicket())
                .flatMap(new Func1<String, rx.Observable<String>>() {
                    @Override
                    public rx.Observable<String> call(String ticketBean) {
                        return rx.Observable.just(ticketBean);
                    }
                }).subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {
                        if(getView() == null)
                            return;
                        getView().hideDialog();
                    }

                    @Override
                    public void onError(Throwable e) {
                        if(getView() == null)
                            return;
                        getView().hideDialog();
                        getView().onErrorHandle(e);
                    }

                    @Override
                    public void onNext(String s) {
                        if(getView() == null)
                            return;
                        mUserBean.setTicket(s);
                        getView().onSuccessTicket(s);
                    }
                }));
    }

}
