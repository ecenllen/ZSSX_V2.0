package com.gta.zssx.fun.assetmanagement.presenter;

import android.os.Handler;

import com.gta.utils.mvp.BasePresenter;
import com.gta.zssx.ZSSXApplication;
import com.gta.zssx.fun.assetmanagement.model.AssetManagerInterface;
import com.gta.zssx.fun.assetmanagement.model.bean.AssetBean;
import com.gta.zssx.fun.assetmanagement.model.bean.UpLoadRemarksAssetBean;
import com.gta.zssx.fun.assetmanagement.view.AssetInventoryView;
import com.gta.zssx.fun.assetmanagement.zxing.activity.CaptureActivity;
import com.gta.zssx.fun.personalcenter.model.LoginDataManager;
import com.gta.zssx.fun.personalcenter.model.bean.UserBean;
import com.gta.zssx.pub.exception.AssetException;

import java.util.List;

import rx.Subscriber;

/**
 * Created by liang.lu on 2016/11/10 17:05.
 */

public class AssetInventoryPresenter extends BasePresenter<AssetInventoryView> {

    private UserBean mUserBean;

    public AssetInventoryPresenter() {
        mUserBean = ZSSXApplication.instance.getUser();
    }

    /**
     * @param Id assetId
     * @param checkId 盘点ID
     * @param tipType 加载提示类型-->1正在加载/2正在识别
     */
    public void GetAssetByAssetId(int Id, int checkId, int tipType) {
        if (!isViewAttached()) {
            return;
        }
        if(tipType == CaptureActivity.LOADING_TYPE) {
            showLoadingDialog();
        } else if(tipType == CaptureActivity.SCANING_TYPE) {
            showDialog("正在识别...");
        }
        mCompositeSubscription.add(AssetManagerInterface.getAssetData(Id, checkId)
                .subscribe(new Subscriber<List<AssetBean>>() {
                    @Override
                    public void onCompleted() {
                        if (getView() == null)
                            return;
                        hideDialog();
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (getView() == null)
                            return;
                        getView().hideProgressDialog();
                        hideDialog();
                        if (e instanceof AssetException) {
                            AssetException assetException = (AssetException) e;
                            int code = assetException.getCode();
                            if (code == AssetException.ASSET_NOT_IN_FROM_NUM) {
                                getView().changeMessage(AssetException.ASSET_NOT_IN_FROM_STRING);
                            }
                        } else {
                            getView().onErrorHandle(e);
                        }
                        resetPreview();
                    }

                    @Override
                    public void onNext(List<AssetBean> assetBeanList) {
                        if (getView() == null)
                            return;
                        AssetBean assetBean = assetBeanList.get(0);
                        getView().showResult(assetBean);
                        if (assetBean.getIsRepeatScan() == 2 && assetBean.getIsHasRemarks() == 2) {
                            resetPreview();
                        }
                    }
                }));

    }

    /**
     * 重置扫描框
     */
    private void resetPreview() {
        new Handler().postDelayed(() -> {
            if (getView() == null)
                return;
            getView().continuePreview();
        }, 2000);
    }

    /**
     * @param upLoadRemarksAssetBean
     * @param state 1 第一次扫描，2重复扫描，记录不添加备注，3重复扫描，记录并添加或者修改备注 
     * @param dismissDialog
     */
    public void upLoadAssetData(UpLoadRemarksAssetBean upLoadRemarksAssetBean, int state, boolean dismissDialog) {
        if (!isViewAttached()) {
            return;
        }
        if (state == 1) {
            getView().showLoadingDialog();
        } else if (state == 2) {
            getView().showDialog("正在记录...");
        } else if (state == 3) {
            getView().showDialog("正在保存...");
        }

        mCompositeSubscription.add(AssetManagerInterface.upLoadRemarksAsset(upLoadRemarksAssetBean)
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        if (getView() == null)
                            return;
                        getView().hideDialog();
                        getView().onErrorHandle(e);
                    }

                    @Override
                    public void onNext(String s) {
                        if (getView() == null)
                            return;
                        getView().hideDialog();
                        if (dismissDialog) {
                            getView().hideDismissDialog();
                        }
                    }
                }));

    }


    /**
     * 获取ticket 值
     */
    public void getTicket() {
        if (getView() == null)
            return;
        getView().showLoadingDialog();
//        String lMD5password = EncryptUtils.encryptMD5ToString(mUserBean.getMD5Password() + "{000000}");
//        String lPowerHostUrl = mUserBean.getPowerHostUrl() + "SignIn/";
        // 获取ticket 值
        mCompositeSubscription.add(LoginDataManager.getTicket(mUserBean.getUserName().toUpperCase(), mUserBean.getMD5Password(), mUserBean.getPowerHostUrl(), mUserBean.getTicket())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {
                        if (getView() == null)
                        return;
                        getView().hideDialog();
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (getView() == null)
                            return;
                        getView().hideDialog();
                        getView().onErrorHandle(e);
                        resetPreview();
                    }

                    @Override
                    public void onNext(String s) {
                        if (getView() == null)
                            return;
                        mUserBean.setTicket(s);
                        getView().onSuccessTicket(s);
                    }
                }));
    }
}
