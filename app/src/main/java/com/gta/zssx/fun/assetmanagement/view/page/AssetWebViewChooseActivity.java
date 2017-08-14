package com.gta.zssx.fun.assetmanagement.view.page;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;

import com.gta.utils.resource.SysRes;
import com.gta.utils.resource.Toast;
import com.gta.zssx.R;
import com.gta.zssx.ZSSXApplication;
import com.gta.zssx.fun.assetmanagement.presenter.AssetManagementPresenter;
import com.gta.zssx.fun.assetmanagement.view.AssetManagementView;
import com.gta.zssx.fun.assetmanagement.view.base.AssetManagementBaseActivity;
import com.gta.zssx.fun.assetmanagement.zxing.activity.CaptureActivity;
import com.gta.zssx.pub.util.LogUtil;
import com.gta.zssx.pub.util.LoginFailUtil;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

/**
 * Created by lan.zheng on 2016/8/9.
 * 选择资产，地点，人等界面
 */
public class AssetWebViewChooseActivity extends AssetManagementBaseActivity<AssetManagementView, AssetManagementPresenter> implements AssetManagementView {
    public static final String TAG = AssetWebViewChooseActivity.class.getSimpleName();
    private String jumpUrl;
    private WebView webView;
    private String errorHtml = "<html><body><h1>Page not find!</h1></body></html>";

    public static Intent start(Context context, String url) {
        Intent lIntent = new Intent(context, AssetWebViewChooseActivity.class);
        Bundle lBundle = new Bundle();
        lBundle.putString(TAG, url);
        lIntent.putExtras(lBundle);
        if (!(context instanceof Activity)) {
            lIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        return lIntent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asset_web_view_show_container);
        jumpUrl = getIntent().getExtras().getString(TAG);
        if (TextUtils.isEmpty(jumpUrl)) {
            finish();
        }
        LogUtil.Log(TAG, "shouldOverrideUrlLoading url = " + jumpUrl);
        /*FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.fragment_container, NewsCenterWebFragment.newInstance(hospitalUrl));
        transaction.commit();*/

//        loadHistoryUrls.add(jumpUrl);
        initWebView();
        getTicket();
    }

    private void initWebView() {
        webView = (WebView) findViewById(R.id.asset_management_web_view);
        //        tv_error = (TextView) findViewById (tv_error);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setDisplayZoomControls(false);
        webSettings.setSupportZoom(false);
        //设置 缓存模式
//        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        // 开启 DOM storage API 功能 
//        webSettings.setDomStorageEnabled(true);

        webSettings.setUseWideViewPort(true);//设置此属性，可任意比例缩放
        webSettings.setLoadWithOverviewMode(true);// 缩放至屏幕的大小 
        webView.clearCache(true);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY); //取消滚动条白边效果
        webView.setWebChromeClient(webChromeClient);
        webView.setWebViewClient(webViewClient);
        webView.addJavascriptInterface(new JavaScriptInterface(), "android");
//        loadUrl();
    }

    private void getTicket() {
        presenter.getTicket();
    }

    private void loadUrl(String ticket) {
        if(!TextUtils.isEmpty(jumpUrl)) {
            String fullUrl = ZSSXApplication.instance.getUser().getEamHostUrl() + jumpUrl + getTicketFlag(jumpUrl, ticket);
            webView.loadUrl(fullUrl);
        } else
            webView.loadUrl(errorHtml);
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
                Toast.Short(AssetWebViewChooseActivity.this, "无法连接网络，请检查网络设置...");

            } else {
                if(LoginFailUtil.isLoginFailUrl(AssetWebViewChooseActivity.this, url)) { // 是否登陆失效，是则注销当前跳转到登陆界面
                    return true;
                }
                view.loadUrl(url);
//                if (!loadHistoryUrls.contains(url))
//                    loadHistoryUrls.add(url);
                LogUtil.Log(TAG, "loadHistoryUrls add url = " + url);
            }
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            showLoadingDialog();
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            hideDialog();
            LogUtil.Log(TAG, "onPageFinished = " + url);
        }

        @Override
        public void onReceivedError(WebView webView, int i, String s, String s1) {
            super.onReceivedError(webView, i, s, s1);
        }
    };

    @Override
    public void onSuccessTicket(String ticket) {
        loadUrl(ticket);
    }

    final class JavaScriptInterface {
        @JavascriptInterface
        public void backListener() {
            webView.post(new Runnable() {
                @Override
                public void run() {
                    LogUtil.Log(TAG, "NewsCenterWebFragment-->backListener(),webView.canGoBack() = " + webView.canGoBack());
                    //                    FINISH();
                    if (webView.canGoBack()) {
                        LogUtil.Log(TAG, "NewsCenterWebFragment-->webView.canGoBack()");
                        webView.goBack();
                    } else {
                        LogUtil.Log(TAG, "NewsCenterWebFragment-->FINISH()");
                        finish();
                    }
                }
            });
        }

        @JavascriptInterface
        public void intoScan() {
            Intent lIntent = new Intent(mActivity, CaptureActivity.class);
            startActivity(lIntent);
        }

        @JavascriptInterface
        public void goBack() {
            LogUtil.Log(TAG, "goBack");
            back();
        }


        /**
         * 打开扫码界面
         */
        @JavascriptInterface
        public void showScan() {
            Intent lIntent = new Intent(mActivity, CaptureActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("type", "asset_scan");
            lIntent.putExtras(bundle);
            startActivity(lIntent);
        }

        /**
         * 选择资产确定回调
         *
         * @param msg 选中信息
         */
        @JavascriptInterface
        public void selectAssetCallback(String msg, int selectType) {
            LogUtil.Log(TAG, msg);
            setResultToBack(msg, selectType);
        }

        /**
         * 选择地点确定回调
         *
         * @param msg 选中信息
         */
        @JavascriptInterface
        public void SelectorSiteCallback(String msg, int selectType) {
            LogUtil.Log(TAG, msg);
            setResultToBack(msg, selectType);

        }

        /**
         * 选择人物确定回调
         *
         * @param msg 选中信息
         */
        @JavascriptInterface
        public void SelectorCallback(String msg, int selectType) {
            LogUtil.Log(TAG, msg);
            setResultToBack(msg, selectType);
        }

        /**
         * 选择完成维修确定回调
         *
         * @param msg 选中信息
         */
        @JavascriptInterface
        public void SelectRepairHandleRresultCallBack(String msg, int selectType) {
            LogUtil.Log(TAG, msg);
            setResultToBack(msg, selectType);
        }

    }

    private void setResultToBack(String msg, int selectType) {
        LogUtil.Log(TAG, "setResultToBack = " + msg);
        Intent result = new Intent();
        result.putExtra(TAG, msg);
        result.putExtra("selectType", selectType);
        setResult(RESULT_OK, result);
        finish();
    }


    private void back() {
        runOnUiThread(() -> {
                if (webView.canGoBack()) {
                    webView.goBack();
                } else {
                    finish();
                }

            //过滤是否为重定向后的链接    
//            if (loadHistoryUrls.size() > 1) {
//                loadHistoryUrls.remove(loadHistoryUrls.get(loadHistoryUrls.size() - 1));
//
//                //加载重定向之前的页  
//                webView.loadUrl(loadHistoryUrls.get(loadHistoryUrls.size() - 1));
//            } else {
//                finish();
//            }
        });

    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
            back();
//                webView.goBack(); //goBack()表示返回WebView的上一页面
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
//        CookieManager manager = CookieManager.getInstance();
//        manager.removeAllCookie();
        // Destroy the webView.
        if (webView != null) {
            webView.clearCache(true);
            webView.clearFormData();
            webView.clearHistory();
            final ViewGroup viewGroup = (ViewGroup) webView.getParent();
            if (viewGroup != null) {
                viewGroup.removeView(webView);
            }
            webView.removeAllViews();
            webView.destroy();
        }
        super.onDestroy();
    }

    @NonNull
    @Override
    public AssetManagementPresenter createPresenter() {
        return new AssetManagementPresenter();
    }

}
