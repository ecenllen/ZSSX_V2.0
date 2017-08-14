package com.gta.zssx.mobileOA.presenter;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.gta.utils.mvp.BasePresenter;
import com.gta.zssx.R;
import com.gta.zssx.fun.coursedaily.registerrecord.model.DTO.DeleteRecordDto;
import com.gta.zssx.mobileOA.model.OAMainModel;
import com.gta.zssx.mobileOA.model.bean.DutyTableInfo;
import com.gta.zssx.mobileOA.view.DutyTableInfoView;
import com.gta.zssx.pub.exception.CustomException;
import com.gta.zssx.pub.util.LogUtil;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;
import rx.Subscription;

/**
 * Created by lan.zheng on 2016/11/19.
 */
public class DutyTableInfoPresenter  extends BasePresenter<DutyTableInfoView> {

    public Subscription mSubscribe;
    public void getDutyTable(int tableId){
        if (!isViewAttached()) {
            return;
        }
        getView().showLoadingDialog();
        mSubscribe = OAMainModel.getDutyTable(tableId)
                .subscribe(new Subscriber<DutyTableInfo>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable throwable) {
                        if (isViewAttached()) {
                            getView().hideDialog();
                            if (throwable instanceof CustomException) {   //抛出自定义的异常
                                CustomException lE = (CustomException) throwable;
                                if (lE.getCode() == CustomException.NETWORK_UNAVAILABLE) {
                                    getView().showError(throwable.getMessage());
                                } else {
                                    getView().onErrorHandle(throwable);  //抛出默认异常
                                }
                            } else {
                                getView().onErrorHandle(throwable);  //抛出默认异常
                            }
                        }
                    }

                    @Override
                    public void onNext(DutyTableInfo dutyTableInfo) {

                    }
                });
        mCompositeSubscription.add(mSubscribe);
    }

    private void backgroundAlpha(float b, Activity mActivity) {
        WindowManager.LayoutParams layoutParams = mActivity.getWindow().getAttributes();
        layoutParams.alpha = b;
        mActivity.getWindow().setAttributes(layoutParams);
    }

    private Dialog mDialog;
    private TextView mContent;
    public void popupConfirmDialog(String content, Activity mActivity) {
        backgroundAlpha(0.8f,mActivity);
        if (mDialog == null) {
            View contentView = LayoutInflater.from(mActivity).inflate(R.layout.dialog_duty_table_remark, null);
            mContent = (TextView) contentView.findViewById(R.id.dialog_content_text);
            mContent.setText(content);
//            View btnOK = contentView.findViewById(R.id.dialog_btn_confirm);
//            View btnBack = contentView.findViewById(R.id.dialog_btn_cancel);
            mDialog = new Dialog(mActivity, R.style.myDialogTheme);  //使用自定义的样式
            mDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    backgroundAlpha(1f,mActivity);
                }
            });
            mDialog.setCanceledOnTouchOutside(true);  //点击外围消失
            mDialog.setContentView(contentView);
        }
        mDialog.show();
    }

}
