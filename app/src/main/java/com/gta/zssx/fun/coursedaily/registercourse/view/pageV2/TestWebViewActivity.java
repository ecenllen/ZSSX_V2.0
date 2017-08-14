package com.gta.zssx.fun.coursedaily.registercourse.view.pageV2;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JavascriptInterface;

import com.gta.utils.resource.Toast;
import com.gta.zssx.R;
import com.gta.zssx.fun.coursedaily.registercourse.presenter.TestWebViewPresenter;
import com.gta.zssx.fun.coursedaily.registercourse.view.TestWebView;
import com.gta.zssx.pub.base.BaseActivity;
import com.gta.zssx.pub.manager.ToolBarManager;
import com.gta.zssx.pub.util.WebViewJavaScriptFunction;
import com.tencent.smtt.export.external.interfaces.ConsoleMessage;
import com.tencent.smtt.export.external.interfaces.JsResult;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;


/**
 * [Description]
 * <p>
 * [How to use]
 * <p>
 * [Tips]
 *
 * @author Created by Zhimin.Huang on 2016/7/27.
 * @since 1.0.0
 */
public class TestWebViewActivity extends BaseActivity<TestWebView, TestWebViewPresenter> implements TestWebView {

    public WebView mWebView;
    public String mUrls = "http://x5.tencent.com/doc?id=1003";
    public String mUrlqq = "http://qq.com";
    public String mUrl = "http://192.168.105.192:7072/EAM/Mobile?ticket=6ada5a22-b76c-473b-a883-1595c1cd457f";


    @NonNull
    @Override
    public TestWebViewPresenter createPresenter () {
        return new TestWebViewPresenter ();
    }

    public Toolbar mToolbar;
    public ToolBarManager mToolBarManager;

    public static void start (Context context) {
        final Intent lIntent = new Intent (context, TestWebViewActivity.class);
        if (!(context instanceof Activity)) {
            lIntent.setFlags (Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity (lIntent);
    }


    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_test_webview);
        initData ();
        initView ();
        loadData ();
    }


    //用于页面间数据接收
    private void initData () {
        presenter.attachView (this);
    }


    //初始化view
    private void initView () {
        findViews ();
        initToolbar ();


        WebSettings webSetting = mWebView.getSettings ();
        webSetting.setAllowFileAccess (true);
        webSetting.setLayoutAlgorithm (WebSettings.LayoutAlgorithm.NARROW_COLUMNS);

        //设置支持手势缩放
        webSetting.setSupportZoom (true);
        webSetting.setBuiltInZoomControls (true);

        //网页自动缩放自屏幕大小
        webSetting.setUseWideViewPort (true);
        webSetting.setLoadWithOverviewMode (true);


        webSetting.setSupportMultipleWindows (false);
        webSetting.setAppCacheEnabled (true);
        webSetting.setDatabaseEnabled (true);
        webSetting.setDomStorageEnabled (true);

        //与JavaScript交互
        webSetting.setJavaScriptEnabled (true);
        //屏蔽掉长按事件 因为webview长按时将会调用系统的复制控件
        mWebView.setOnLongClickListener (new View.OnLongClickListener () {
            @Override
            public boolean onLongClick (View view) {
                return true;
            }
        });


        webSetting.setGeolocationEnabled (true);

        setOnInteractListener ();
    }

    private void loadData () {

    }


    private void initToolbar () {
        mToolBarManager.init ();
        mToolbar.setNavigationOnClickListener (v -> {
            if (mWebView.canGoBack ()) {
                mWebView.goBack ();
            } else {
                onBackPressed ();
            }
        });

        mToolBarManager.showRightButton (true)
                .setRightText ("提交")
                .clickRightButton (view -> {
                    mWebView.loadUrl ("javascript:returnMessage()");
                });

    }


    //事件处理
    @SuppressLint ("JavascriptInterface")
    private void setOnInteractListener () {
        //  webview 监听事件
        mWebView.setWebViewClient (new WebViewClient () {
            //在点击请求的是链接是才会调用，重写此方法返回true表明点击网页里面的链接还是在当前的webview里跳转，不跳到浏览器那边。
            @Override
            public boolean shouldOverrideUrlLoading (WebView webView, String s) {
                webView.loadUrl (s);
                return true;
            }

            //在页面加载开始时调用。
            @Override
            public void onPageStarted (WebView webView, String s, Bitmap bitmap) {

                presenter.showLoadingDialog ();
                super.onPageStarted (webView, s, bitmap);
            }

            //在页面加载结束时调用。
            @Override
            public void onPageFinished (WebView webView, String s) {
                presenter.hideDialog ();
                super.onPageFinished (webView, s);
            }

            @Override
            public void onReceivedError (WebView webView, int i, String s, String s1) {
                super.onReceivedError (webView, i, s, s1);
            }
        });

        mWebView.setWebChromeClient (new WebChromeClient () {
            @Override
            public boolean onJsAlert (WebView webView, String s, String s1, JsResult jsResult) {


                AlertDialog.Builder builder = new AlertDialog.Builder (mActivity);
                builder.setTitle ("提示");
                builder.setMessage (s1);
                builder.setNegativeButton ("确定", (dialog, which) -> {

                });
                AlertDialog lAlertDialog = builder.show ();


                //否则弹框就弹出一次
                jsResult.confirm ();

                return true;
            }

            @Override
            public void onReceivedTitle (WebView webView, String s) {
                super.onReceivedTitle (webView, s);
            }

            //在Logcat中输出javascript的日志信息
            @Override
            public boolean onConsoleMessage (ConsoleMessage consoleMessage) {
                Log.d("WebView", consoleMessage.message() + " js line: " + consoleMessage.lineNumber());
                return true;
            }
        });
        //web端调用native代码
        mWebView.addJavascriptInterface (new WebViewJavaScriptFunction () {
            @Override
            public void onJsFunctionCalled (String tag) {

            }

            @JavascriptInterface
            public void showToast (String message) {
                Toast.Short (mActivity, message);
            }

            @JavascriptInterface
            public String onReturnMsg () {
                return "111111111";
            }

        }, "Android");
    }

    //绑定控件
    private void findViews () {
        mToolbar = (Toolbar) findViewById (R.id.toolbar);
        mToolBarManager = new ToolBarManager (mToolbar, this);

        mWebView = (WebView) findViewById (R.id.test_webview);

        mWebView.loadUrl (mUrlqq);

    }


    @Override
    protected void onDestroy () {
        super.onDestroy ();
        presenter.detachView (false);
        mToolBarManager.destroy ();
    }

    @Override
    public boolean onKeyDown (int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && mWebView.canGoBack ()) {
            mWebView.goBack ();// 返回前一个页面
            return true;
        }
        finish ();
        return super.onKeyDown (keyCode, event);
    }


}
