package com.gta.zssx.mobileOA.view.page;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.widget.TextView;

import com.gta.zssx.R;
import com.gta.zssx.ZSSXApplication;
import com.gta.zssx.fun.personalcenter.model.bean.UserBean;
import com.gta.zssx.mobileOA.presenter.BacklogDetailPresenter;
import com.gta.zssx.mobileOA.view.BacklogDetailView;
import com.gta.zssx.pub.base.BaseActivity;
import com.gta.zssx.pub.common.Constant;
import com.gta.zssx.pub.util.LogUtil;
import com.gta.zssx.pub.util.LoginFailUtil;
import com.gta.zssx.pub.util.MIMETypeUtil;
import com.gta.zssx.pub.util.WebViewJavaScriptFunction;
import com.tencent.smtt.export.external.interfaces.ConsoleMessage;
import com.tencent.smtt.sdk.DownloadListener;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

import java.io.File;

import static com.gta.zssx.AppConfiguration.mContext;

/**
 * bpm界面展示
 */
public class BacklogDetailActivity extends BaseActivity<BacklogDetailView, BacklogDetailPresenter> implements BacklogDetailView {

    private static final String TAG = BacklogDetailActivity.class.getSimpleName();
    public static final int WEB_REQUEST_CODE = 1234;
    public static final int CHOOSE_SEAL_REQUEST_CODE = 2345;
    private Toolbar mToolbar;
    private TextView tvTitle;
    private TextView tvHistory;
    private WebView wvContent;

    private ProgressDialog dialog;

    private NotificationManager notificationManager;
    final NotificationCompat.Builder builder = new NotificationCompat.Builder(BacklogDetailActivity.this);

    @NonNull
    @Override
    public BacklogDetailPresenter createPresenter() {
        return new BacklogDetailPresenter();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oa_backlog_detail);
        inits();
    }

    private void inits() {
        //初始化标题栏
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        tvTitle = (TextView) findViewById(R.id.title);
        tvHistory = (TextView) findViewById(R.id.tv_history);
        wvContent = (WebView) findViewById(R.id.wv_content);
        mToolbar.setNavigationOnClickListener((view) -> finish());
        dialog = new ProgressDialog(this);
        notificationManager = (NotificationManager) getSystemService(Activity.NOTIFICATION_SERVICE);
        //审批历史
        tvHistory.setOnClickListener((view) -> {
            Intent intent = new Intent(BacklogDetailActivity.this, ApprovalHistoryActivity.class);
            intent.putExtra("runId", presenter.getRunId());
            startActivity(intent);
        });

        initWebView();
        setOnInteractListener();
        presenter.initData(BacklogDetailActivity.this);
    }

    public void initWebView() {
        wvContent.setBackgroundColor(0);
        wvContent.setAlpha(1);
        wvContent.setInitialScale(110);
        wvContent.setOnLongClickListener((view) -> {
            return true;
        });
        wvContent.clearCache(true);
        initWebSettings();
    }

    public void initWebSettings() {
        WebSettings webSetting = wvContent.getSettings();
        webSetting.setAllowFileAccess(true);
        webSetting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        //设置支持手势缩放
        webSetting.setSupportZoom(false);
        webSetting.setBuiltInZoomControls(false);
        webSetting.setUseWideViewPort(true);
        webSetting.setLoadWithOverviewMode(true);
        webSetting.setSupportMultipleWindows(false);
        webSetting.setAppCacheEnabled(false);
        webSetting.setDatabaseEnabled(false);
        webSetting.setCacheMode(WebSettings.LOAD_NO_CACHE);
        webSetting.setDomStorageEnabled(true);
        webSetting.setBlockNetworkImage(true);
        webSetting.setNeedInitialFocus(false);
        //与JavaScript交互
        webSetting.setJavaScriptEnabled(true);
        //屏蔽掉长按事件 因为webview长按时将会调用系统的复制控件
        webSetting.setGeolocationEnabled(true);
    }

    //事件处理
    @SuppressLint("JavascriptInterface")
    private void setOnInteractListener() {
        //  webview 监听事件
        wvContent.setWebViewClient(new WebViewClient() {

            //在点击请求的是链接是才会调用，重写此方法返回true表明点击网页里面的链接还是在当前的webview里跳转，不跳到浏览器那边。
            @Override
            public boolean shouldOverrideUrlLoading(WebView webView, String s) {
                LoginFailUtil.isLoginFailUrl(BacklogDetailActivity.this, s);
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
                super.onPageFinished(webView, s);
                presenter.hideDialog();
                if (s.contains("runId")) {
                    presenter.changeReadState();
                }
                webView.getSettings().setLoadsImagesAutomatically(true);
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
                return true;
            }


            @Override
            public void onProgressChanged(WebView webView, int i) {
                super.onProgressChanged(webView, i);
                webView.requestFocus();
            }


        });
        wvContent.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String s, String s1, String s2, String s3, long l) {
                presenter.downloadAttachment(s, null);
            }
        });
        //web端调用native代码
        wvContent.addJavascriptInterface(new WebViewJavaScriptFunction() {
            @Override
            public void onJsFunctionCalled(String tag) {

            }


            @JavascriptInterface
            public void androidCallBackListener() {
                setResult(BacklogMainActivity.BACKLOG_UPDATE_RESULT_CODE);
                finish();
            }

            /**
             * 打开选择会议室、人、车辆操作
             *
             * @param url
             */
            @JavascriptInterface
            public void openAndroidWindow(String url) {
                LogUtil.Log(TAG, "openAndroidWindow url = " + url);
                startActivityForResult(WebViewChooseActivity.start(BacklogDetailActivity.this, getAccountInfo(url, ZSSXApplication.instance.getUser().getTicket())), WEB_REQUEST_CODE);
            }

            @JavascriptInterface
            public void selectSeal() {
                Intent intent = new Intent(BacklogDetailActivity.this, ChooseSealActivity.class);
                startActivityForResult(intent, CHOOSE_SEAL_REQUEST_CODE);
            }

            @JavascriptInterface
            public void download(String path, String fileName) {
                UserBean userBean = ZSSXApplication.instance.getUser();
                String bpmHost = userBean.getBpmHostUrl().replace("/bpm", "");
                String pathUrl = bpmHost + path;
                presenter.downloadAttachment(pathUrl, fileName);
            }

        }, "android");


    }

    @Override
    public void setTitle(String title) {
        tvTitle.setText(title);
    }

    @Override
    public void setUrl(String url) {

        wvContent.loadUrl(url);
    }

    @Override
    public void showApprovalHistoryButton(boolean show) {
        if (show) {
            tvHistory.setVisibility(View.VISIBLE);
        } else {
            tvHistory.setVisibility(View.GONE);
        }

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == WEB_REQUEST_CODE) {
            if (data != null) {
                updateWebViewInfo(data.getStringExtra("functionName"), data.getStringExtra("data"));
            }
        } else if (requestCode == CHOOSE_SEAL_REQUEST_CODE) {
            if (data != null) {
                updateWebViewInfo("insertSeal", data.getStringExtra("data"));
            }
        }


    }

    private void updateWebViewInfo(String functionName, String msg) {
        wvContent.loadUrl("javascript:" + functionName + "('" + msg + "');");
    }

    @Override
    public void showDownloadNotification(int status, int progress, String fileName, File file) {
        builder.setSmallIcon(R.mipmap.applogo);
        builder.setContentTitle(fileName);
        builder.setTicker("开始下载...");
        if (status == Constant.DOWNLOAD_START) {
            notificationManager.notify(0, builder.build());
            builder.setProgress(100, 0, true);
        } else if (status == Constant.DOWNLOAD_PROGRESS) {
            builder.setProgress(100, progress, false);
        } else if (status == Constant.DOWNLOAD_SUCCESS) {
            builder.setProgress(0, 0, true);
            Intent intent = new Intent();
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            //设置intent的Action属性
            intent.setAction(Intent.ACTION_VIEW);
            //获取文件file的MIME类型
            String type = MIMETypeUtil.getMIMEType(file);
            //设置intent的data和Type属性。
            intent.setDataAndType(/*uri*/Uri.fromFile(file), type);
            PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0, intent,
                    0);

            //跳转
            builder.setContentIntent(pendingIntent);
            builder.setContentText("下载完成");
        } else {
            builder.setProgress(0, 0, true);
            builder.setContentText("下载失败");
        }
        notificationManager.notify(0, builder.build());
    }

    @Override
    protected void onPause() {
        super.onPause();
        wvContent.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        wvContent.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        wvContent.destroy();
    }

//    //写数据到SD中的文件
//    public void writeFileSdcardFile(String fileName,String write_str) throws IOException {
//        String filePath = Environment.getExternalStorageDirectory()+"/zssx";
//        File file = new File(filePath, fileName);
//        try{
//            FileOutputStream fout = new FileOutputStream(file);
//            byte [] bytes = write_str.getBytes();
//
//            fout.write(bytes);
//            fout.close();
//        }
//
//        catch(Exception e){
//            e.printStackTrace();
//        }
//    }
}
