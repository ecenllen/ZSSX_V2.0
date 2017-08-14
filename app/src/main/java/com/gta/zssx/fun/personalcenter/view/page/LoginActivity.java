package com.gta.zssx.fun.personalcenter.view.page;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.SPUtils;
import com.gta.utils.helper.Helper_MD5;
import com.gta.zssx.AppConfiguration;
import com.gta.zssx.R;
import com.gta.zssx.fun.coursedaily.registercourse.view.pageV2.ClassChooseActivity;
import com.gta.zssx.fun.personalcenter.model.bean.UserBean;
import com.gta.zssx.fun.personalcenter.presenter.LoginPresenter;
import com.gta.zssx.fun.personalcenter.view.LoginView;
import com.gta.zssx.main.HomePageActivity;
import com.gta.zssx.pub.base.BaseActivity;
import com.gta.zssx.pub.util.DES3Utils;
import com.gta.zssx.pub.widget.ClearEditText;

/**
 * [Description]
 * <p/>
 * [How to use]
 * <p/>
 * [Tips]
 *
 * @author Created by Zhimin.Huang on 2016/6/14.
 * @since 1.0.0
 */
public class LoginActivity extends BaseActivity<LoginView, LoginPresenter>
        implements LoginView, ViewTreeObserver.OnGlobalLayoutListener {

    public ClearEditText mAccountEt;
    public ClearEditText mPasswordEt;
    public ClearEditText mServerAddressEt;
    public TextView mServerSettingTv;
    public Button mLoginBtn;
    public boolean isServerVisiable;
    public RelativeLayout mServerAddressLayout;
    /**
     * 当没有保存过IP地址的时候，默认显示该IP地址
     */
//    private String DEFAULT_IP = "210.75.17.208:7072/TQA";
    /**
     * 中山沙溪学校地址
     */
    private String DEFAULT_IP = "113.106.4.202:7072";
    private ImageView mLogoImage;
    private int mLogoHeight;
    private int mLogoWidth;
    private LinearLayout mLoginLayout;
    /**
     * 郑州财经
     */
//    private String DEFAULT_IP = "218.28.167.66:7072";
    /** 开发地址*/
//    private String DEFAULT_IP = "192.168.105.192:7072";
    /** 资产测试地址*/
//    private String DEFAULT_IP = "192.168.105.253:7072";

    /**
     * 课堂日志和巡客地址
     */
//    private String DEFAULT_IP = "113.106.4.202:7072";
//    10.1.130.105:7072
    public static void start(Context context) {

        final Intent lIntent = new Intent(context, LoginActivity.class);
        if (!(context instanceof Activity)) {
            lIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(lIntent);
    }

    @NonNull
    @Override
    public LoginPresenter createPresenter() {
        return new LoginPresenter();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        initData();
    }


    private void initView() {

        findView();

//         /*调试阶段使用，以后要删除*/
//        mAccountEt.setText ("admin");
//        mPasswordEt.setText ("000000");
//         /*调试阶段使用，以后更改为false*/
        mLoginBtn.setEnabled(false);

        setOnInteractListener();

        boolean lHasServerAddress = AppConfiguration.getInstance().hasServerAddress();
        mServerAddressLayout.setVisibility(lHasServerAddress ? View.GONE : View.VISIBLE);
        if (lHasServerAddress) {
            mServerAddressEt.setText(AppConfiguration.getInstance().getServerAddress().getIp());
            isServerVisiable = false;
        } else {
            mServerAddressEt.setText(DEFAULT_IP);
            isServerVisiable = true;
        }
        mServerSettingTv.setText(!isServerVisiable ? getString(R.string.set_server_address) : getString(R.string.hide_server_address));
        SPUtils lSPUtils = new SPUtils( LoginPresenter.USERNAME_SP);
        String lString = lSPUtils.getString(LoginPresenter.KEY_SP);
        if (lString != null) {
            String lAccount = DES3Utils.decrypt3DESMode(lString, getString(R.string.PASSWORD_CRYPT_KEY));
            mAccountEt.setText(lAccount);
        }

    }

    private void setOnInteractListener() {

//        mLayBackBar.getViewTreeObserver().addOnGlobalLayoutListener(this);
        mLoginLayout.getViewTreeObserver().addOnGlobalLayoutListener(this);


        mLoginBtn.setOnClickListener(v -> {
            presenter.Login(LoginActivity.this,
                    getAccount(), getPassword(), getMD5Password(), getServerAddress());
        });

        mServerSettingTv.setOnClickListener(v -> {
            mServerAddressLayout.setVisibility(isServerVisiable ? View.GONE : View.VISIBLE);
            mServerSettingTv.setText(isServerVisiable ? getString(R.string.set_server_address) : getString(R.string.hide_server_address));
            isServerVisiable = !isServerVisiable;
        });


        TextWatcher lWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mLoginBtn.setEnabled(isEmpty());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
        mPasswordEt.addTextChangedListener(lWatcher);
        mAccountEt.addTextChangedListener(lWatcher);
        mServerAddressEt.addTextChangedListener(lWatcher);
    }

    private boolean isEmpty() {
        return !getAccount().isEmpty() && !getMD5Password().isEmpty() && !getServerAddress().isEmpty();
    }

    private void findView() {
        mAccountEt = (ClearEditText) findViewById(R.id.account_et);
        mPasswordEt = (ClearEditText) findViewById(R.id.password_et);
        mServerAddressEt = (ClearEditText) findViewById(R.id.server_address_et);
        mServerSettingTv = (TextView) findViewById(R.id.server_setting_tv);
        mLogoImage = (ImageView) findViewById(R.id.logo_iv);
        mLoginBtn = (Button) findViewById(R.id.login_btn);
        mServerAddressLayout = (RelativeLayout) findViewById(R.id.server_address_layout);
        mLoginLayout = (LinearLayout) findViewById(R.id.login_layout);
    }

    private void initData() {
        try {
            UserBean lUserBean = AppConfiguration.getInstance().getUserBean();
            if (lUserBean != null) {
                mAccountEt.setText(lUserBean.getAccount());
                mPasswordEt.setText(lUserBean.getPassword());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @NonNull
    private String getAccount() {
        return mAccountEt.getText().toString().trim();
    }

    @NonNull
    private String getMD5Password() {
        String lRule = mPasswordEt.getText().toString().trim() + "{000000}";
        String lEncryptToMD5 = Helper_MD5.getMD5(lRule);
        return lEncryptToMD5.replace("-", "").toLowerCase();
//        return mPasswordEt.getText().toString().trim();
    }

    @NonNull
    private String getPassword() {
        return mPasswordEt.getText().toString().trim();
    }

    //md5加密后返回
    @NonNull
    private String getServerAddress() {
        return mServerAddressEt.getText().toString().trim();
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void loginSuccess(UserBean userBean) {
        HomePageActivity.start(mActivity);
        finish();
    }

    @Override
    public void loginFail(int code, String errorMessage) {
        AlertDialog.Builder lBuilder = new AlertDialog.Builder(this);
        lBuilder.setMessage(errorMessage);
        lBuilder.setPositiveButton("确定", (dialog, which) -> dialog.dismiss()).show();
//        View lView = LayoutInflater.from(this).inflate(R.layout.dialog_login_fail, null);
//        TextView lSure = (TextView) lView.findViewById(R.id.sure_tv);
//        TextView lContent = (TextView) lView.findViewById(R.id.content_tv);
//        lContent.setText(errorMessage);
//        lBuilder.setView(lView);
//        final AlertDialog lDialog = lBuilder.show();

//        /**
//         * 把背景设置成透明，圆角
//         */
//        lDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

//        lSure.setOnClickListener(v -> lDialog.dismiss());


    }


    @Override
    public void noRegisterClass(int code, String errorMessage, UserBean userBean) {
        AppConfiguration.getInstance().setUserLoginBean(userBean).saveAppConfiguration();
        ClassChooseActivity.start(this);
        finish();
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.detachView(false);
        mLoginLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
    }

    @Override
    public void onGlobalLayout() {
        final ImageView ivLogo = this.mLogoImage;

        Rect KeypadRect = new Rect();

        mLoginLayout.getWindowVisibleDisplayFrame(KeypadRect);

//        int screenHeight = mLoginLayout.getRootView().getHeight();
//        DisplayMetrics dm = new DisplayMetrics();
//        getWindowManager().getDefaultDisplay().getMetrics(dm);
//        int screenHeight = dm.heightPixels;

        int screenHeight = getResources().getDisplayMetrics().heightPixels;

        int keypadHeight = screenHeight - KeypadRect.bottom;
        if(this.mLogoHeight <= 0 || this.mLogoWidth <= 0) {
            this.mLogoHeight = ivLogo.getHeight();
            this.mLogoWidth = ivLogo.getWidth();
        }

        if (keypadHeight > 0 && ivLogo.getTag() == null) {
//            final int height = ivLogo.getHeight();
//            final int width = ivLogo.getWidth();
//            this.mLogoHeight = height;
//            this.mLogoWidth = width;
            ivLogo.setTag(true);
            ValueAnimator valueAnimator = ValueAnimator.ofFloat(1, 0);
            valueAnimator.setDuration(400).setInterpolator(new DecelerateInterpolator());
            valueAnimator.addUpdateListener(animation -> {
                float animatedValue = (float) animation.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = ivLogo.getLayoutParams();
                layoutParams.height = (int) (mLogoHeight * animatedValue);
                layoutParams.width = (int) (mLogoWidth * animatedValue);
                ivLogo.requestLayout();
                ivLogo.setAlpha(animatedValue);
            });

            if (valueAnimator.isRunning()) {
                valueAnimator.cancel();
            }
            valueAnimator.start();
        } else if (keypadHeight == 0 && ivLogo.getTag() != null) {
//            final int height = mLogoHeight;
//            final int width = mLogoWidth;
            ivLogo.setTag(null);
            ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1);
            valueAnimator.setDuration(400).setInterpolator(new DecelerateInterpolator());
            valueAnimator.addUpdateListener(animation -> {
                float animatedValue = (float) animation.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = ivLogo.getLayoutParams();
                layoutParams.height = (int) (mLogoHeight * animatedValue);
                layoutParams.width = (int) (mLogoWidth * animatedValue);
                ivLogo.requestLayout();
                ivLogo.setAlpha(animatedValue);
            });

            if (valueAnimator.isRunning()) {
                valueAnimator.cancel();
            }
            valueAnimator.start();
        }

    }

}
