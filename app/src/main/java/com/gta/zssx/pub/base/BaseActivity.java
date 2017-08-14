package com.gta.zssx.pub.base;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.IdRes;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.gta.utils.mvp.BaseMvpActivity;
import com.gta.utils.mvp.BaseView;
import com.gta.utils.resource.Toast;
import com.gta.zssx.AppConfiguration;
import com.gta.zssx.BuildConfig;
import com.gta.zssx.R;
import com.gta.zssx.fun.personalcenter.view.page.LoginActivity;
import com.gta.zssx.pub.exception.CustomException;
import com.gta.zssx.pub.util.FontScaleUtil;
import com.gta.zssx.pub.widget.loading.bar.LoadingBar;
import com.gta.zssx.pub.widget.loading.dialog.LoadingDialog;
import com.hannesdorfmann.mosby.mvp.MvpPresenter;

import java.net.ConnectException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import retrofit2.adapter.rxjava.HttpException;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * [Description]
 * <p>
 * [How to use]
 * <p>
 * [Tips]
 *
 * @author Created by Zhimin.Huang on 2016/6/14.
 * @since 1.0.0
 */
public abstract class BaseActivity<V extends BaseView, P extends MvpPresenter<V>> extends BaseMvpActivity<V, P> {

    private ProgressDialog dialog;
    protected Context mActivity;

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
        dialog = ProgressDialog.show (this, title, message, true);
        dialog.setCancelable (isCancelable);

        //        mProgressDialog = TranProgressDialog.show(mActivity);
        //        mProgressDialog.setCancelable(isCancelable);
    }

    @Override
    public void hideDialog () {
        if (dialog != null) {
            dialog.dismiss ();
            dialog = null;
        }
        //        if (mProgressDialog != null) {
        //            mProgressDialog.dismiss();
        //        }
    }

    @Override
    public void showInfo (String message) {
        Toast.Short (this, message);
    }

    @Override
    public void showWarning (String warningMessage) {
        Toast.Short (this, warningMessage);
        //        Toasteroid.show(getActivity(), warningMessage, Toasteroid.STYLES.WARNING);
    }

    @Override
    public void showError (String error) {
        Toast.Short (this, error);
        //        Toasteroid.show(getActivity(), error, Toasteroid.STYLES.ERROR);
    }

    @Override
    public void showErrorLong (String error) {
        Toast.Long (this, error);
    }

    @Override
    public void showInfoLong (String message) {
        Toast.Long (this, message);
    }

    @Override
    public void showWarningLong (String error) {
        Toast.Long (this, error);
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
    public void showOnFailReloading(String error) {

    }

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);

        mActivity = this;
        AppConfiguration.getInstance ().addActivity (this);
        FontScaleUtil.initFontScale (mActivity);
        //        registerReceiver(finishActivitiesReceiver, new IntentFilter(ACTION_FINISH));
    }

    @Override
    public void onErrorHandle (Throwable e) {
        if (e instanceof CustomException) {
            CustomException lErrorCodeException = (CustomException) e;
            showErrorLong (lErrorCodeException.getMessage ());
        }

        if (e instanceof SocketTimeoutException || e instanceof SocketException) {
            showErrorLong (getString (R.string.requst_time_out));
            return;
        }

        if (e instanceof UnknownHostException) {
            showError (getString (R.string.server_address_error));

            //服务器地址不对退出登陆
            //            sendBroadcast(new Intent(BaseActivity.ACTION_FINISH));
            AppConfiguration.getInstance ().setUserLoginBean (null).setFirstIn (null).saveAppConfiguration ();
            LoginActivity.start (this);
            return;
        }

        if (e instanceof ConnectException) {
            showError (getString (R.string.server_address_error));
            return;
        }

        if (e instanceof HttpException) {
            showError (getString (R.string.server_address_error));
        }

        if (e instanceof NullPointerException) {
            showError (getString (R.string.string_class_null));
        }

    }


    @Override
    protected void onResume () {
        super.onResume ();
        if (BuildConfig.DEBUG) {
            Log.e ("CurrentPageName", "\n" + this.getClass ().getSimpleName ());
        }

    }

    @Override
    protected void onDestroy () {
        super.onDestroy ();
        if (this.mCompositeSubscription != null) {
            this.mCompositeSubscription.unsubscribe ();
        }
        hideDialog();
        mActivity = null;
    }

    @Override
    public void finish () {
        super.finish ();
        AppConfiguration.getInstance ().removeActivity (this);
        presenter.detachView (false);
    }

    protected void showSoftInputOrNot (EditText mobilEditText, boolean show) {
        mobilEditText.requestFocus ();
        InputMethodManager imm = (InputMethodManager) mobilEditText.getContext ().getApplicationContext ().getSystemService (
                Context.INPUT_METHOD_SERVICE);
        if (show) {
            imm.showSoftInput (mobilEditText, 0);
        } else {
            imm.hideSoftInputFromWindow (mobilEditText.getWindowToken (), 0);
        }
    }

    protected <VH extends View> VH getViewById (@IdRes int resId) {
        return (VH) this.findViewById (resId);
    }


    protected void showSnackbar (String s) {
        Snackbar.make (this.getWindow ().getDecorView (), s, Snackbar.LENGTH_SHORT).show ();
    }

    protected String getAccountInfo (String url, String ticket) {
        String lAccountInfo = "";
        if (!TextUtils.isEmpty (url)) {
            lAccountInfo = url + (url.contains ("?") ? "&ticket=" : "?ticket=") + ticket + "&platForm=MOBILE";
        }

        return lAccountInfo;
    }

    /**
     * 设置textView顶部图片
     *
     * @param v           要设置的TextView
     * @param id_drawable 还要设置的图片
     */
    protected void setDrawableTop (TextView v, int id_drawable) {
        Drawable topDrawable = getResources ().getDrawable (id_drawable);
        topDrawable.setBounds (0, 0, topDrawable.getMinimumWidth (), topDrawable.getMinimumHeight ());
        //void android.widget.TextView.setCompoundDrawables(Drawable left, Drawable top, Drawable right, Drawable bottom)
        v.setCompoundDrawables (null, topDrawable, null, null);
    }

    private CompositeSubscription mCompositeSubscription;


    protected CompositeSubscription getCompositeSubscription () {
        if (this.mCompositeSubscription == null) {
            this.mCompositeSubscription = new CompositeSubscription ();
        }

        return this.mCompositeSubscription;
    }


    protected void addSubscription (Subscription s) {
        if (this.mCompositeSubscription == null) {
            this.mCompositeSubscription = new CompositeSubscription ();
        }

        this.mCompositeSubscription.add (s);
    }

    //解决多次快速点击出现多个Activity的情况，这里设置400s内算快速点击,注意设置太大会引起滑动卡顿
    @Override
    public boolean dispatchTouchEvent (MotionEvent ev) {
        if (ev.getAction () == MotionEvent.ACTION_DOWN) {
            if (isFastDoubleClick ()) {
                //                Toast.Short(this,"快速点击");
                return true;
            }
        }
        return super.dispatchTouchEvent (ev);
    }

    long lastClickTime = 0;

    public boolean isFastDoubleClick () {
        long time = System.currentTimeMillis ();
        long timeD = time - lastClickTime;
        lastClickTime = time;
        return timeD <= 400;
    }
}
