package com.gta.zssx.pub.widget.loading.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.view.LayoutInflater;
import android.view.View;

import com.gta.zssx.R;
import com.gta.zssx.pub.widget.loading.LoadingConfig;
import com.gta.zssx.pub.widget.loading.factory.DialogFactory;


/**
 * author  dengyuhan
 * created 2017/4/16 03:59
 */
public class LoadingDialog implements ILoadingDialog {
    private static LoadingDialog LOADINGDIALOG;

    private Dialog mDialog;
    private DialogFactory mFactory;

    public LoadingDialog(Context context, DialogFactory factory, View view) {
        this.mDialog = view == null ? factory.onCreateDialog(context) : factory.onCreateDialog(context, view);
        this.mFactory = factory;
        int animateStyleId = this.mFactory.getAnimateStyleId();
        if (animateStyleId > 0) {
            this.mDialog.getWindow().setWindowAnimations(animateStyleId);
        }
    }

    @Override
    public void show() {
        if (isValid() && !mDialog.isShowing()) {
            mDialog.show();
        }
    }

    public void cancelDialog() {
        if (isValid() && mDialog.isShowing()) {
            mDialog.cancel();
        }
    }


    @Override
    public Dialog create() {
        return mDialog;
    }

    @Override
    public ILoadingDialog setCancelable(boolean flag) {
        mDialog.setCancelable(flag);
        return this;
    }

    @Override
    public ILoadingDialog setMessage(CharSequence message) {
        mFactory.setMessage(mDialog, message);
        return this;
    }

    /**
     * 判断dialog 是否还存在或者挂载的Activity 是否已经销毁
     * @return true 可用,false 不可用
     */
    private boolean isValid() {
        if (mDialog != null) {
            Context context = mDialog.getContext();
            if (context instanceof ContextWrapper) {
                context = ((ContextWrapper) context).getBaseContext();
            }
            if (context instanceof Activity) {
                if (!((Activity) context).isFinishing()) {
                    return true;
                }
            }
        }
        return false;
    }

    public static LoadingDialog make(Context context) {
        return make(context, LoadingConfig.getDialogFactory(), null);
    }

    public static LoadingDialog make(Context context, View view) {
        return make(context, LoadingConfig.getDialogFactory(), view);
    }

    public static LoadingDialog make(Context context, DialogFactory factory, View view) {
        cancel();
        LOADINGDIALOG = new LoadingDialog(context, factory, view);
        return LOADINGDIALOG;
    }


    public static void cancel() {
        if (LOADINGDIALOG != null) {
            LOADINGDIALOG.cancelDialog();
            LOADINGDIALOG = null;
        }
    }


}
