package com.gta.zssx.mobileOA.view.page;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.webkit.JavascriptInterface;

import com.gta.zssx.R;
import com.gta.zssx.fun.assetmanagement.presenter.AssetManagementPresenter;
import com.gta.zssx.fun.assetmanagement.view.AssetManagementView;
import com.gta.zssx.pub.base.BaseActivity;
import com.gta.zssx.pub.util.LogUtil;
import com.gta.zssx.pub.util.LoginFailUtil;
import com.gta.zssx.pub.util.WebViewJavaScriptFunction;
import com.tencent.smtt.export.external.interfaces.ConsoleMessage;
import com.tencent.smtt.sdk.ValueCallback;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;


/**
 * bpm界面展示
 */
public class WebViewChooseActivity extends BaseActivity<AssetManagementView, AssetManagementPresenter> implements AssetManagementView {

    public static final String TAG = WebViewChooseActivity.class.getSimpleName();
    private WebView wvContent;
    private String mUrl;

    public static Intent start(Context context, String url) {
        Intent lIntent = new Intent(context, WebViewChooseActivity.class);
        Bundle lBundle = new Bundle();
        lBundle.putString(TAG, url);
        lIntent.putExtras(lBundle);
        if (!(context instanceof Activity)) {
            lIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        return lIntent;
    }

    @NonNull
    @Override
    public AssetManagementPresenter createPresenter() {
        return new AssetManagementPresenter();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asset_web_view_show_container);
        initBundle();
        inits();
    }

    private void initBundle() {
        mUrl = getIntent().getExtras().getString(TAG);
        if (TextUtils.isEmpty(mUrl)) {
            finish();
        }
        LogUtil.Log(TAG, mUrl);
    }

    private void inits() {
        wvContent = (WebView) findViewById(R.id.asset_management_web_view);

        WebSettings webSetting = wvContent.getSettings();
        webSetting.setAllowFileAccess(true);
        webSetting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        wvContent.setBackgroundColor(0);
        //设置支持手势缩放
        webSetting.setSupportZoom(true);
        webSetting.setBuiltInZoomControls(false);

        webSetting.setUseWideViewPort(true);
        webSetting.setLoadWithOverviewMode(true);


        webSetting.setSupportMultipleWindows(false);
        webSetting.setAppCacheEnabled(false);
        webSetting.setDatabaseEnabled(false);
        webSetting.setCacheMode(WebSettings.LOAD_NO_CACHE);
        webSetting.setDomStorageEnabled(true);

        //与JavaScript交互
        webSetting.setJavaScriptEnabled(true);
        //屏蔽掉长按事件 因为webview长按时将会调用系统的复制控件
        wvContent.setOnLongClickListener((view) -> {
            return true;
        });


        webSetting.setGeolocationEnabled(true);

        setOnInteractListener();

        wvContent.clearCache(true);

        wvContent.loadUrl(mUrl);
    }


    //事件处理
    @SuppressLint("JavascriptInterface")
    private void setOnInteractListener() {
        //  webview 监听事件
        wvContent.setWebViewClient(new WebViewClient() {

            //在点击请求的是链接是才会调用，重写此方法返回true表明点击网页里面的链接还是在当前的webview里跳转，不跳到浏览器那边。
            @Override
            public boolean shouldOverrideUrlLoading(WebView webView, String s) {
                LoginFailUtil.isLoginFailUrl(WebViewChooseActivity.this, s);
                webView.loadUrl(s);
                return true;
            }

            //在页面加载开始时调用。
            @Override
            public void onPageStarted(WebView webView, String s, Bitmap bitmap) {
                presenter.showLoadingDialog();
                super.onPageStarted(webView, s, bitmap);
            }

            //在页面加载结束时调用。
            @Override
            public void onPageFinished(WebView webView, String s) {
                presenter.hideDialog();
                super.onPageFinished(webView, s);
            }

            @Override
            public void onReceivedError(WebView webView, int i, String s, String s1) {
                presenter.hideDialog();
                super.onReceivedError(webView, i, s, s1);
            }


        });

        wvContent.setWebChromeClient(new WebChromeClient() {


            @Override
            public void onReceivedTitle(WebView webView, String s) {
                super.onReceivedTitle(webView, s);
            }

            //在Logcat中输出javascript的日志信息
            @Override
            public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
                LogUtil.Log("WebView", consoleMessage.message() + " js line: " + consoleMessage.lineNumber());
                return true;
            }

            @Override
            public void openFileChooser(ValueCallback<Uri> valueCallback, String s, String s1) {
                super.openFileChooser(valueCallback, s, s1);
            }

        });
        //web端调用native代码
        wvContent.addJavascriptInterface(new WebViewJavaScriptFunction() {

            @Override
            public void onJsFunctionCalled(String tag) {

            }

            @JavascriptInterface
            public void selectComplete(String data, String functionName) {
                setResultToBack(data, functionName);
            }

            @JavascriptInterface
            public void SelectorCallback(String data, String functionName) {
                setResultToBack(data, functionName);
            }

            @JavascriptInterface
            public void goBack() {
                back();
            }

        }, "android");

    }

    private void back() {
        runOnUiThread(() -> {
            if (wvContent.canGoBack()) {
                wvContent.goBack();
            } else {
                finish();
            }
        });
    }

    private void setResultToBack(String data, String functionName) {
        LogUtil.Log(TAG, "setResultToBack = " + data);
        Intent result = new Intent();
        result.putExtra("data", data);
        result.putExtra("functionName", functionName);
        setResult(RESULT_OK, result);
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && wvContent.canGoBack()) {
            back();
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        wvContent.clearCache(true);
        wvContent.clearFormData();
        wvContent.clearHistory();
        wvContent.destroy();
    }

    @Override
    public void onSuccessTicket(String ticket) {

    }
}
