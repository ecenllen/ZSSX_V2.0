package com.gta.zssx.mobileOA.view.page;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.gta.zssx.R;
import com.gta.zssx.ZSSXApplication;
import com.gta.zssx.fun.personalcenter.model.bean.UserBean;
import com.tencent.smtt.export.external.interfaces.ConsoleMessage;
import com.tencent.smtt.export.external.interfaces.JsResult;
import com.tencent.smtt.sdk.ValueCallback;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;


/**
 * 审批历史
 */
public class ApprovalHistoryActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private WebView webView;
    private UserBean userBean;
    private ProgressDialog progressDialog;

    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oa_approval_history);
        url = ZSSXApplication.instance.getUser().getOaHostUrl() + "/MobileWorkFlow/Progress?runId=%s&platForm=MOBILE&ticket=%s";
        initView();
        initData();
    }

    void initView() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        webView = (WebView) findViewById(R.id.wv_content);
        mToolbar.setNavigationOnClickListener((view) -> finish());
        progressDialog = new ProgressDialog(this);

        WebSettings webSetting = webView.getSettings();
        webSetting.setAllowFileAccess(true);
        webSetting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);

        //设置支持手势缩放
        webSetting.setSupportZoom(true);
        webSetting.setBuiltInZoomControls(false);

        //网页自动缩放自屏幕大小
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
        webView.setOnLongClickListener((v) -> {
            return true;
        });


        webSetting.setGeolocationEnabled(true);

        setOnInteractListener();

        webView.clearCache(true);
    }

    void initData() {
        userBean = ZSSXApplication.instance.getUser();
        String runId = getIntent().getStringExtra("runId");
        webView.loadUrl(String.format(url, runId, userBean.getTicket()));
    }

    //事件处理
    @SuppressLint("JavascriptInterface")
    private void setOnInteractListener() {
        //  webview 监听事件
        webView.setWebViewClient(new WebViewClient() {
            //在点击请求的是链接是才会调用，重写此方法返回true表明点击网页里面的链接还是在当前的webview里跳转，不跳到浏览器那边。
            @Override
            public boolean shouldOverrideUrlLoading(WebView webView, String s) {
                webView.loadUrl(s);
                return true;
            }

            //在页面加载开始时调用。
            @Override
            public void onPageStarted(WebView webView, String s, Bitmap bitmap) {
                progressDialog.show();
                super.onPageStarted(webView, s, bitmap);
            }

            //在页面加载结束时调用。
            @Override
            public void onPageFinished(WebView webView, String s) {
                progressDialog.dismiss();
                super.onPageFinished(webView, s);
            }

            @Override
            public void onReceivedError(WebView webView, int i, String s, String s1) {
                super.onReceivedError(webView, i, s, s1);
            }
        });

        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onJsAlert(WebView webView, String s, String s1, JsResult jsResult) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ApprovalHistoryActivity.this);
                builder.setTitle("提示");
                builder.setMessage(s1);
                builder.setNegativeButton("确定", (dialog, which) -> {

                });
                AlertDialog lAlertDialog = builder.show();
                //否则弹框就弹出一次
                jsResult.confirm();

                return true;
            }

            @Override
            public void onReceivedTitle(WebView webView, String s) {
                super.onReceivedTitle(webView, s);
            }

            //在Logcat中输出javascript的日志信息
            @Override
            public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
                Log.d("WebView", consoleMessage.message() + " js line: " + consoleMessage.lineNumber());
                return true;
            }

            @Override
            public void openFileChooser(ValueCallback<Uri> valueCallback, String s, String s1) {
                super.openFileChooser(valueCallback, s, s1);
            }

        });

    }

    @Override
    protected void onDestroy() {
        if (webView != null) {
            // 如果先调用destroy()方法，则会命中if (isDestroyed()) return;这一行代码，需要先onDetachedFromWindow()，再
            // destory()
            ViewParent parent = webView.getParent();
            if (parent != null) {
                ((ViewGroup) parent).removeView(webView);
            }

            webView.stopLoading();
            // 退出时调用此方法，移除绑定的服务，否则某些特定系统会报错
            webView.getSettings().setJavaScriptEnabled(false);
            webView.clearHistory();
            webView.clearView();
            webView.removeAllViews();

            try {
                webView.destroy();
            } catch (Throwable ex) {

            }
        }
        super.onDestroy();
    }
}
