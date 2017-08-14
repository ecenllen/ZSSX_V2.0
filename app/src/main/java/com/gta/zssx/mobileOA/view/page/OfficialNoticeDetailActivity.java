package com.gta.zssx.mobileOA.view.page;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.gta.zssx.R;
import com.gta.zssx.mobileOA.presenter.OfficialNoticeDetailPresenter;
import com.gta.zssx.mobileOA.view.OfficialNoticeDetailView;
import com.gta.zssx.pub.base.BaseActivity;
import com.gta.zssx.pub.manager.ToolBarManager;

/**
 * Created by lan.zheng on 2016/11/9.  公文公告详情BPM
 */
@Deprecated
public class OfficialNoticeDetailActivity extends BaseActivity<OfficialNoticeDetailView,OfficialNoticeDetailPresenter>
        implements OfficialNoticeDetailView{
    public Toolbar mToolbar;
    protected ToolBarManager mToolBarManager;
    private WebView mWebView;
    private int titleType;
    private String officialId;
    private final static int SHOW_TITLE_PAGE_NOTICE = 0; //学校公告
    private final static String TYPE = "type";
    private final static String OFFICIAL_ID = "official_id";
    String url = "http://192.168.105.192:7071/bpm/platform/bpm/processRun/info.ht?link=1&runId=170000000020004&prePage=myFinishedTask&platForm=MOBILE";

    @NonNull
    @Override
    public OfficialNoticeDetailPresenter createPresenter() {
        return new OfficialNoticeDetailPresenter();
    }

    public static void start(Context context,int type,int id) {
        final Intent lIntent = new Intent(context, OfficialNoticeDetailActivity.class);
        Bundle lBundle = new Bundle();
        lBundle.putInt(TYPE,type);
        lBundle.putInt(OFFICIAL_ID,id);
        lIntent.putExtras(lBundle);
        if (!(context instanceof Activity)) {
            lIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(lIntent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_official_notice_detail);
        titleType = getIntent().getExtras().getInt(TYPE,SHOW_TITLE_PAGE_NOTICE);
        officialId = getIntent().getExtras().getString(OFFICIAL_ID,"");
        initView();
        initWebView();
        initData();
    }

    private void initView(){
        mWebView = (WebView)findViewById(R.id.web_view);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setNavigationOnClickListener(view -> onBackPressed());
        mToolBarManager = new ToolBarManager(mToolbar, this);
        mToolBarManager.init();
        if(titleType  == SHOW_TITLE_PAGE_NOTICE){
            mToolBarManager.setTitle(R.string.text_school_notice_detail);
        }else {
            mToolBarManager.setTitle(R.string.text_school_official_detail);
        }
    }

    private void initWebView(){
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setSupportZoom(true);
        mWebView.getSettings().setBuiltInZoomControls(true);
        mWebView.getSettings().setAllowFileAccess(true);
        mWebView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        mWebView.getSettings().setDefaultTextEncodingName("UTF-8") ;
        mWebView.getSettings().setBlockNetworkImage(false);

        mWebView.getSettings().setUseWideViewPort(true);
        mWebView.getSettings().setLoadWithOverviewMode(true);
        mWebView.getSettings().setSupportMultipleWindows(false);
        mWebView.getSettings().setAppCacheEnabled(true);
        mWebView.getSettings().setDatabaseEnabled(true);
        mWebView.getSettings().setDomStorageEnabled(true);
        //屏蔽掉长按事件 因为webview长按时将会调用系统的复制控件
        mWebView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                return true;
            }
        });
        mWebView.getSettings().setGeolocationEnabled(true);

        mWebView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY); //取消滚动条白边效果
        mWebView.setWebChromeClient(webChromeClient);
        mWebView.setWebViewClient(webViewClient);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            mWebView.getSettings().setMixedContentMode(mWebView.getSettings().MIXED_CONTENT_ALWAYS_ALLOW);
        }
        String runId = "170000000020004";  //测试字符串
//        String runId = String.valueOf(officialId);
        url =  "http://192.168.105.192:7071/bpm/platform/bpm/processRun/info.ht?link=1&runId="+runId+"&prePage=myFinishedTask&platForm=MOBILE";
        mWebView.loadUrl(url);
//        webView.loadDataWithBaseURL("http://avatar.csdn.net",getNewContent(IMAGE3),"text/html", "UTF-8", null);
    }

    private WebChromeClient webChromeClient = new WebChromeClient()
    {
        @Override
        public boolean onJsAlert(WebView webView, String s, String s1, JsResult jsResult) {


            AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
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
            return true;
        }
    };

    private WebViewClient webViewClient = new WebViewClient()
    {
        //在点击请求的是链接是才会调用，重写此方法返回true表明点击网页里面的链接还是在当前的webview里跳转，不跳到浏览器那边。
        @Override
        public boolean shouldOverrideUrlLoading(WebView webView, String s) {
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
            if(s.contains("runId")){
//                presenter.setNoticeRead();
            }
            super.onPageFinished(webView, s);
        }

        @Override
        public void onReceivedError(WebView webView, int i, String s, String s1) {
            super.onReceivedError(webView, i, s, s1);
        }
    };


    private void initData(){

    }
}
