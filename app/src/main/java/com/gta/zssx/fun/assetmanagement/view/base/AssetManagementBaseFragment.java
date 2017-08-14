package com.gta.zssx.fun.assetmanagement.view.base;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gta.utils.mvp.BaseMvpFragment;
import com.gta.utils.mvp.BasePresenter;
import com.gta.utils.mvp.BaseView;
import com.gta.utils.resource.Toast;
import com.gta.utils.resource.TranProgressDialog;
import com.gta.zssx.R;
import com.gta.zssx.fun.assetmanagement.view.page.AssetManagementActivity;
import com.gta.zssx.pub.exception.CustomException;
import com.gta.zssx.pub.manager.ToolBarManager;

import java.net.SocketTimeoutException;

/**
 * [Description]
 * <p> 资产管理BaseFragment
 * [How to use]
 * <p>
 * [Tips]
 *
 * @author Created by Weiye.Chen on 2017/7/20.
 * @since 2.0.0
 */
public abstract class AssetManagementBaseFragment <V extends BaseView,P extends BasePresenter<V>> extends BaseMvpFragment<V,P> {
    public static final String PAGE_TAG = AssetManagementBaseFragment.class.getSimpleName();

    protected AssetManagementBaseActivity mActivity;

    protected ToolBarManager mToolBarManager;
    public TranProgressDialog mProgressDialog;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof AssetManagementActivity) {
            mActivity = (AssetManagementBaseActivity) activity;

        } else {
            throw new RuntimeException("null activity");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return super.onCreateView(inflater, container, savedInstanceState);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mActivity.setMyBaseFragmentWeakReference(this);

    }

    public boolean onBackPress() {
        return false;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(mToolBarManager != null)
            mToolBarManager.renew();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private ProgressDialog dialog;

    @Override
    public void showLoadingDialog() {
        showDialog("", getResourceString(R.string.loading), true);
    }

    @Override
    public void showLoadingDialog(boolean isCancelable) {
        showDialog("", getResourceString(R.string.loading), isCancelable);
    }

    @Override
    public void showDialog(String message) {
        showDialog("", message, true);
    }

    @Override
    public void showDialog(String message, boolean isCancelable) {
        showDialog("", message, isCancelable);
    }

    @Override
    public void showDialog(String title, String message) {
        showDialog(title, message, false);
    }

    @Override
    public void showDialog(String title, String message, boolean isCancelable) {
        hideDialog();
        dialog = ProgressDialog.show(getActivity(), title, message, true);
        dialog.setCancelable(isCancelable);

//        mProgressDialog = TranProgressDialog.show(mActivity);
//        mProgressDialog.setCancelable(isCancelable);
    }

    @Override
    public void hideDialog() {
        if (dialog != null) {
            dialog.dismiss();
        }
//        if (mProgressDialog != null) {
//            mProgressDialog.dismiss();
//        }
    }

    @Override
    public void showInfo(String message) {
        Toast.Short(mActivity, message);
    }

    @Override
    public void showWarning(String warningMessage) {
        Toast.Short(mActivity, warningMessage);
//        Toasteroid.show(getActivity(), warningMessage, Toasteroid.STYLES.WARNING);
    }

    @Override
    public void showError(String error) {
        Toast.Short(mActivity, error);
//        Toasteroid.show(getActivity(), error, Toasteroid.STYLES.ERROR);
    }

    @Override
    public String getResourceString(int stringId) {
        return getString(stringId);
    }

    @Override
    public String getResourceString(int stringId, Object... formatArgs) {
        return getString(stringId, formatArgs);
    }

    @Override
    public void onErrorHandle(Throwable e) {
        if (e instanceof CustomException) {
            CustomException lErrorCodeException = (CustomException) e;
            showWarning(lErrorCodeException.getMessage());
        }

        if (e instanceof SocketTimeoutException) {
            showWarning("请求超时，请稍候重试");
        }
    }

    @Override
    public void showInfoLong(String message) {

    }

    @Override
    public void showWarningLong(String error) {

    }

    @Override
    public void showErrorLong(String error) {

    }

    @Override
    public void showOnFailReloading(String error) {

    }
}
