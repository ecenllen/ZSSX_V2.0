package com.gta.zssx.fun.assetmanagement.view.page;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.widget.TextView;

import com.gta.utils.helper.Helper_Drawable;
import com.gta.utils.resource.SysRes;
import com.gta.zssx.R;
import com.gta.zssx.ZSSXApplication;
import com.gta.zssx.fun.assetmanagement.presenter.AssetManagementPresenter;
import com.gta.zssx.fun.assetmanagement.view.AssetManagementView;
import com.gta.zssx.fun.assetmanagement.view.base.AssetManagementBaseActivity;
import com.gta.zssx.fun.assetmanagement.zxing.activity.CaptureActivity;
import com.gta.zssx.fun.personalcenter.view.page.PersonCenterActivity;
import com.gta.zssx.patrolclass.popup.AddPageActivity;
import com.gta.zssx.pub.util.LogUtil;
import com.gta.zssx.pub.util.LoginFailUtil;
import com.gta.zssx.pub.util.TimeUtils;
import com.gta.zssx.pub.widget.loading.bar.LoadingBar;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;



/**
 * [Description]
 * <p> 资产管理首页
 * [How to use]
 * <p>
 * [Tips]
 *
 * @author Created by Weiye.Chen on 2017/7/20.
 * @since 2.0.0
 */
public class AssetManagementActivity extends AssetManagementBaseActivity<AssetManagementView, AssetManagementPresenter> implements AssetManagementView,
        View.OnClickListener {

    private static final String TAG = AssetManagementActivity.class.getSimpleName();
    private String errorHtml = "<html><body><h1>Page not find!</h1></body></html>";
    
    public RecyclerView mRecyclerView;
    private TextView tab_home, tab_add, tab_my;
    private WebView mWebView;
    private View mRootView;

    public static void start(Context context) {
        Intent lIntent = new Intent(context, AssetManagementActivity.class);
        if (!(context instanceof Activity)) {
            lIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(lIntent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asset_management_container);
        initView();
        getTicketData();
    }

    @Override
    public void showLoadingDialog() {
        LoadingBar.make(mRootView);
    }

    @Override
    public void showDialog(String title, String message, boolean isCancelable) {
        LoadingBar.make(mRootView);
    }

    @Override
    public void hideDialog() {
        LoadingBar.cancel(mRootView);
    }

    private void initView() {
        initToolBar(this);

        mToolBarManager
                .setTitle(getString(R.string.text_asset_management))
                .showRightImage(true)
                .setRightImage(Helper_Drawable.tintDrawable(mActivity.getResources().getDrawable(R.drawable.ic_asset_sweep_the_yard), ColorStateList.valueOf(Color.WHITE)))
                .clickRightImage(view -> {
                    CaptureActivity.start(mActivity, "home_scan", 0);
                });

        tab_home = (TextView) findViewById(R.id.tab_home);
        tab_add = (TextView) findViewById(R.id.tab_add);
        tab_my = (TextView) findViewById(R.id.tab_my);
        mRootView = findViewById(R.id.base_activity_rl);

        initWebView();
        setOnInteractListener();
    }

    private void initWebView() {
        mWebView = (WebView) findViewById(R.id.wv_content);
        //        tv_error = (TextView) findViewById (tv_error);
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setDisplayZoomControls(false);
        webSettings.setSupportZoom(false);
        //设置 缓存模式, 返回的时候不需要刷新页面
//        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        // 开启 DOM storage API 功能 
//        webSettings.setDomStorageEnabled(true);

        webSettings.setUseWideViewPort(true);//设置此属性，可任意比例缩放
        webSettings.setLoadWithOverviewMode(true);// 缩放至屏幕的大小 
        mWebView.clearCache(true);
        mWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY); //取消滚动条白边效果
        mWebView.setWebChromeClient(webChromeClient);
        mWebView.setWebViewClient(webViewClient);
        mWebView.addJavascriptInterface(new JavaScriptInterface(), "android");
//        mWebView.loadUrl(ZSSXApplication.instance.getUser().getEamHostUrl() + getString(R.string.url_asset_management) + getTicketFlag(getString(R.string.url_asset_management), mTicket));

    }


    private void setOnInteractListener() {
        tab_home.setOnClickListener(this);
        tab_add.setOnClickListener(this);
        tab_my.setOnClickListener(this);

    }

    private void getTicketData() {
        //        presenter.getAssetInfoData ("", "", "");
        presenter.getTicket();
    }

    private WebChromeClient webChromeClient = new WebChromeClient() {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
        }


    };


    private WebViewClient webViewClient = new WebViewClient() {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            LogUtil.Log(TAG, "shouldOverrideUrlLoading url = " + url);
            if (!SysRes.isConnected(mActivity)) {
                showInfo("无法连接网络，请检查网络设置...");
            } else {
                if (LoginFailUtil.isLoginFailUrl(AssetManagementActivity.this, url)) { // 是否登陆失效，是则注销当前跳转到登陆界面
                    return true;
                }
                view.loadUrl(url);
            }
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            presenter.showLoadingDialog();
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);

            presenter.hideDialog();
            LogUtil.Log(TAG, "onPageFinished = " + url);
        }

        @Override
        public void onReceivedError(WebView webView, int i, String s, String s1) {
            super.onReceivedError(webView, i, s, s1);
        }
    };

    final class JavaScriptInterface {

        @JavascriptInterface
        public void intoScan() {
            if (!TimeUtils.isFastDoubleClick(1000)) {
                Intent lIntent = new Intent(mActivity, CaptureActivity.class);
                startActivity(lIntent);
            }
        }

        /**
         * 打开选择界面
         *
         * @param url url
         * @param selectType 选人，选部门
         */
        @JavascriptInterface
        public void openAndroidWindow(String url, int selectType) {

            AssetWebViewShowActivity.start(AssetManagementActivity.this, url, 0);
//            presenter.getTicket();

//            String fullUrl = "http://" + AppConfiguration.getInstance().getServerAddress().getIp() + url + getTicketFlag(url);
//            LogUtil.Log(TAG, "fullUrl = " + fullUrl);
//            AssetWebViewShowActivity.start(AssetManagementActivity.this, fullUrl, 0);
        }
        
    }

    private void loadUrl(String ticket) {
        String fullUrl = ZSSXApplication.instance.getUser().getEamHostUrl() + getString(R.string.url_asset_management) + getTicketFlag(getString(R.string.url_asset_management), ticket);
        if (!TextUtils.isEmpty(fullUrl)) {
            mWebView.loadUrl(fullUrl);
        } else
            mWebView.loadUrl(errorHtml);
    }

    @Override
    public void onSuccessTicket(String ticket) {
//        String fullUrl = ZSSXApplication.instance.getUser().getEamHostUrl() + mUrl + getTicketFlag(mUrl, mTicket);
//        AssetWebViewShowActivity.start(AssetManagementActivity.this, fullUrl, 0);
        loadUrl(ticket);
    }

    @Override
    public void onErrorHandle(Throwable e) {
        super.onErrorHandle(e);
        loadUrl(null);
    }

    @NonNull
    @Override
    public AssetManagementPresenter createPresenter() {
        return new AssetManagementPresenter();
    }

//    public String binary(byte[] bytes, int radix) {
//        return new BigInteger(1, bytes).toString(radix);// 这里的1代表正数  
//    }

    @Override
    public void onBackPressed() {
        finish();
    }


    @Override
    public void onClick(View view) {
        if (TimeUtils.isFastDoubleClick()) {
            return;
        }
        switch (view.getId()) {
            case R.id.tab_home:
                finish();
                break;
            case R.id.tab_add:
                startActivity(new Intent(this, AddPageActivity.class));
                break;
            case R.id.tab_my:
                PersonCenterActivity.start(this);
                break;
            default:
                break;
        }
    }


    @Override
    protected void onDestroy() {
        // Destroy the webView.
        if (mWebView != null) {
            mWebView.clearCache(true);
            mWebView.clearFormData();
            mWebView.clearHistory();
            ViewGroup viewGroup = (ViewGroup) mWebView.getParent();
            if (viewGroup != null) {
                viewGroup.removeView(mWebView);
            }
            mWebView.removeAllViews();
            mWebView.destroy();
        }
        presenter.detachView(false);
        super.onDestroy();
    }
}
