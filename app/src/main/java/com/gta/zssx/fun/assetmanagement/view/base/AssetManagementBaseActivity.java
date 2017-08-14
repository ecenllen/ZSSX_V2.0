package com.gta.zssx.fun.assetmanagement.view.base;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gta.utils.mvp.BaseMvpActivity;
import com.gta.utils.mvp.BaseView;
import com.gta.utils.resource.Toast;
import com.gta.zssx.AppConfiguration;
import com.gta.zssx.R;
import com.gta.zssx.fun.personalcenter.view.page.LoginActivity;
import com.gta.zssx.pub.exception.CustomException;
import com.gta.zssx.pub.manager.ToolBarManager;
import com.gta.zssx.pub.widget.loading.bar.LoadingBar;
import com.gta.zssx.pub.widget.loading.dialog.LoadingDialog;
import com.hannesdorfmann.mosby.mvp.MvpPresenter;

import java.lang.ref.WeakReference;
import java.net.ConnectException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import retrofit2.adapter.rxjava.HttpException;

/**
 * [Description]
 * <p> 资产管理BaseActivity
 * [How to use]
 * <p>
 * [Tips]
 *
 * @author Created by Weiye.Chen on 2017/7/20.
 * @since 2.0.0
 */
public abstract class AssetManagementBaseActivity<V extends BaseView, P extends MvpPresenter<V>> extends BaseMvpActivity<V, P> {

    private ProgressDialog dialog;
    protected Context mActivity;
    public Toolbar mToolbar;
    protected ToolBarManager mToolBarManager;

    //资产管理主页是Fragment，这个在base中直接初始化
    public WeakReference<AssetManagementBaseFragment> mMyBaseFragmentWeakReference;

    public void setMyBaseFragmentWeakReference(AssetManagementBaseFragment myBaseFragment) {
        mMyBaseFragmentWeakReference = new WeakReference<>(myBaseFragment);
    }


    public void initToolBar(Activity activity) {
        mToolbar = (Toolbar) activity.findViewById(R.id.toolbar);
        mToolbar.setNavigationOnClickListener(view -> onBackPressed());
        mToolBarManager = new ToolBarManager(mToolbar, this);
        mToolBarManager.init();
    }


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
        dialog = ProgressDialog.show(this, title, message, true);
        dialog.setCancelable(isCancelable);


//        mProgressDialog = TranProgressDialog.show(mActivity);
//        mProgressDialog.setCancelable(isCancelable);
    }

    @Override
    public void hideDialog() {

//        LoadingBar.cancel(mLoadingBaseView);
//        LoadingDialog.cancel();
        if (dialog != null) {
            dialog.dismiss();
        }
//        if (mProgressDialog != null) {
//            mProgressDialog.dismiss();
//        }
    }

    @Override
    public void showInfo(String message) {
        Toast.Short(this, message);
    }

    @Override
    public void showWarning(String warningMessage) {
        Toast.Short(this, warningMessage);
//        Toasteroid.show(getActivity(), warningMessage, Toasteroid.STYLES.WARNING);
    }

    @Override
    public void showError(String error) {
        Toast.Short(this, error);
//        Toasteroid.show(getActivity(), error, Toasteroid.STYLES.ERROR);
    }

    @Override
    public void showErrorLong(String error) {
        Toast.Long(this, error);
    }

    @Override
    public void showInfoLong(String message) {
        Toast.Long(this, message);
    }

    @Override
    public void showWarningLong(String error) {
        Toast.Long(this, error);
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
    public void showOnFailReloading(String error) {

    }

    @Override
    public void onErrorHandle(Throwable e) {
        if (e instanceof CustomException) {
            CustomException lErrorCodeException = (CustomException) e;
            showWarningLong(lErrorCodeException.getMessage());
        }

        if (e instanceof SocketTimeoutException || e instanceof SocketException) {
            showWarningLong(getString(R.string.requst_time_out));
            return;
        }

        if (e instanceof UnknownHostException) {
            showWarning(getString(R.string.server_address_error));

            //服务器地址不对退出登陆
//            sendBroadcast(new Intent(BaseActivity.ACTION_FINISH));
            AppConfiguration.getInstance().finishAllActivity();
            AppConfiguration.getInstance().setUserLoginBean(null).setFirstIn(null).saveAppConfiguration();
            LoginActivity.start(this);
            return;
        }

        if (e instanceof ConnectException) {
            showWarning(getString(R.string.server_address_error));
            return;
        }

        if (e instanceof HttpException) {
            showError(getString(R.string.server_address_error));
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = this;
        AppConfiguration.getInstance().addActivity(this);

    }

    public static final String ACTION_FINISH = "ACTION_FINISH";


    @Override
    protected void onDestroy() {
        super.onDestroy();

        mActivity = null;
        if(mToolBarManager != null)
            mToolBarManager.destroy();
    }

    @Override
    public void finish() {
        AppConfiguration.getInstance().removeActivity(this);
        super.finish();
    }

    public String getTicketFlag(String url, String ticket) {
        String ticketFlag = "";
        if (!TextUtils.isEmpty(url)) {
            if (!url.contains("ticket")) {
                ticketFlag = url.contains("?") ? "&ticket=" : "?ticket=";
                if (!url.contains("platForm"))
                    ticketFlag += ticket + "&platForm=MOBILE";
                else
                    ticketFlag += ticket;
            }

        }

        return ticketFlag;
    }

}
