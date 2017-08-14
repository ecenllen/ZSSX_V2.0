package com.gta.zssx.pub.base;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.blankj.utilcode.util.ToastUtils;
import com.gta.utils.mvp.BaseMvpFragment;
import com.gta.utils.mvp.BasePresenter;
import com.gta.utils.mvp.BaseView;
import com.gta.utils.resource.Toast;
import com.gta.zssx.R;
import com.gta.zssx.pub.exception.CustomException;
import com.gta.zssx.pub.util.FontScaleUtil;

import java.net.SocketTimeoutException;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by liang.lu1 on 2016/7/12.
 */
public abstract class BaseFragment<V extends BaseView, P extends BasePresenter<V>> extends BaseMvpFragment<V, P> {

    public static final String PAGE_TAG = BaseFragment.class.getSimpleName ();
    protected Context mActivity;
    //    protected View mLoadingBaseView;

    @Override
    public void onAttach (Activity activity) {
        super.onAttach (activity);
        //        if (activity instanceof PatrolClassActivity) {
        //            mActivity = (PatrolClassActivity) activity;
        ////            mToolBarManager = mActivity.getToolBarManager();
        //        } else {
        //            throw new RuntimeException("null activity");
        //        }
        mActivity = activity;
        //        mLoadingBaseView = activity.getWindow ().getDecorView ();
        FontScaleUtil.initFontScale (mActivity);
    }

    @Override
    public void onViewCreated (View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated (view, savedInstanceState);
        //        mActivity.setMyBaseFragmentWeakReference(this);

    }

    @Override
    public void onResume () {
        super.onResume ();

    }

    public boolean onBackPress () {
        return false;
    }

    @Override
    public void onDetach () {
        super.onDetach ();
        presenter.detachView (false);
    }

    @Override
    public void onDestroyView () {
        super.onDestroyView ();
        //        LoadingBar.cancel (mLoadingBaseView);
        //        mToolBarManager.renew();
    }

    @Override
    public void onDestroy () {
        super.onDestroy ();
        if (this.mCompositeSubscription != null) {
            this.mCompositeSubscription.unsubscribe ();
        }
        mActivity = null;
    }

    private CompositeSubscription mCompositeSubscription;

    protected void addSubscription (Subscription s) {
        if (this.mCompositeSubscription == null) {
            this.mCompositeSubscription = new CompositeSubscription ();
        }
        this.mCompositeSubscription.add (s);
    }

    private ProgressDialog dialog;

    @Override
    public void showLoadingDialog () {
        showDialog ("", getResourceString (R.string.loading), true);
    }

    @Override
    public void showLoadingDialog (boolean isCancelable) {
        showDialog ("", getResourceString (R.string.loading), isCancelable);
    }

    @Override
    public void showDialog (String message) {
        showDialog ("", message, true);
    }

    @Override
    public void showDialog (String message, boolean isCancelable) {
        showDialog ("", message, isCancelable);
    }

    @Override
    public void showDialog (String title, String message) {
        showDialog (title, message, false);
    }

    @Override
    public void showDialog (String title, String message, boolean isCancelable) {
        hideDialog ();
        dialog = ProgressDialog.show (getActivity (), title, message, true);
        dialog.setCancelable (isCancelable);

    }

    @Override
    public void hideDialog () {
        if (dialog != null) {
            dialog.dismiss ();
        }
    }

    @Override
    public void showInfo (String message) {
        Toast.Short(mActivity, message);
    }

    @Override
    public void showWarning (String warningMessage) {
        ToastUtils.showShortToast(warningMessage);
    }

    @Override
    public void showError (String error) {
        ToastUtils.showShortToast(error);
    }

    @Override
    public String getResourceString (int stringId) {
        return getString (stringId);
    }

    @Override
    public String getResourceString (int stringId, Object... formatArgs) {
        return getString (stringId, formatArgs);
    }

    @Override
    public void onErrorHandle (Throwable e) {
        if (e instanceof CustomException) {
            CustomException lErrorCodeException = (CustomException) e;
            showError (lErrorCodeException.getMessage ());

        }

        if (e instanceof SocketTimeoutException) {
            showError ("请求超时，请稍候重试");
        }

    }


    @Override
    public void showInfoLong (String message) {

    }

    @Override
    public void showWarningLong (String error) {

    }

    @Override
    public void showErrorLong (String error) {

    }

    @Override
    public void showOnFailReloading (String error) {

    }
}
