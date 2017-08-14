package com.gta.zssx.fun.assetmanagement.view.page;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.gta.utils.helper.Helper_Drawable;
import com.gta.utils.helper.Helper_String;
import com.gta.utils.resource.SysRes;
import com.gta.utils.resource.Toast;
import com.gta.utils.widget.image_viewpager.BottomSelectorPopDialog;
import com.gta.zssx.R;
import com.gta.zssx.ZSSXApplication;
import com.gta.zssx.fun.adjustCourse.view.CenterItemDialog;
import com.gta.zssx.fun.adjustCourse.view.page.DetailSchedulePopupWindow;
import com.gta.zssx.fun.assetmanagement.model.bean.ApprovalDetailBean;
import com.gta.zssx.fun.assetmanagement.model.bean.AssetRemarkBean;
import com.gta.zssx.fun.assetmanagement.model.bean.BatchAddNotesBean;
import com.gta.zssx.fun.assetmanagement.model.bean.UpLoadRemarksAssetBean;
import com.gta.zssx.fun.assetmanagement.presenter.AssetWebViewShowPresenter;
import com.gta.zssx.fun.assetmanagement.view.AssetWebViewShowView;
import com.gta.zssx.fun.assetmanagement.view.base.AssetManagementBaseActivity;
import com.gta.zssx.fun.assetmanagement.zxing.activity.CaptureActivity;
import com.gta.zssx.fun.personalcenter.view.page.PersonCenterActivity;
import com.gta.zssx.patrolclass.popup.AddPageActivity;
import com.gta.zssx.pub.base.BaseMvpActivity;
import com.gta.zssx.pub.util.LogUtil;
import com.gta.zssx.pub.util.LoginFailUtil;
import com.gta.zssx.pub.util.TimeUtils;
import com.gta.zssx.pub.widget.loading.bar.LoadingBar;
import com.tbruyelle.rxpermissions.RxPermissions;
import com.tencent.smtt.export.external.interfaces.WebResourceRequest;
import com.tencent.smtt.export.external.interfaces.WebResourceResponse;
import com.tencent.smtt.sdk.ValueCallback;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

import java.util.ArrayList;
import java.util.List;

/**
 * [Description]
 * <p> 资产管理详情展示页
 * [How to use]
 * <p>
 * [Tips]
 *
 * @author Created by Weiye.Chen on 2017/7/20.
 * @since 2.0.0
 */
public class AssetWebViewShowActivity extends AssetManagementBaseActivity<AssetWebViewShowView, AssetWebViewShowPresenter> implements AssetWebViewShowView, View.OnClickListener {
    public static final String TAG = AssetWebViewShowActivity.class.getSimpleName();
    private static final String EXTRA_URL = "extra_url";
    private static final String EXTRA_ID = "extra_id";
    private static final String EXTRA_IS_RESULT = "is_result";
    /**
     * 加载页面类型
     */
    private static final String EXTRA_LOADING_TYPE = "extra_loading";
    /**
     * 资产主页
     */
    public static final int LOADING_ASSET_MAIN = 135;
    /**
     * 审批详情
     */
    public static final int LOADING_ASSET_APPROVAL = 138;
    /**
     * 选人选部门
     */
    public static final int LOADING_ASSET_SELECT = 137;
    /**
     * 普通,只有一个WebView
     */
    public static final int LOADING_ASSET_NORMAL = 136;
    
    private String jumpUrl;
    private String mBackUrl;
    private WebView webView;
    /**
     * 申请类型标题
     */
    private TextView mTitle;
    /**
     * 申请单号
     */
    private TextView mTitleNo;
    /**
     * 审批历史
     */
    private TextView mHistory;
    private Toolbar mToolbar;

    /**
     * 资产主界面
     */
    private TextView tab_home, tab_add, tab_my;

    public BottomSelectorPopDialog mBottomSelectorPopDialog;
    private AlertDialog lDialog_hold_message;
    private UpLoadRemarksAssetBean upLoadRemarksAssetBean;
    public Uri mCameraTempFileUri;
    private String errorHtml = "<html><body><h1>Page not find!</h1></body></html>";
    public static final int SELECT_ASSET = 1000; // 选资产
    public static final int SELECT_SITE = 1001;  //选地点
    public static final int SELECT_PERSON = 1002;   // 选人
    public static final int SELECT_REPAIR = 1003;   // 选人

    /**
     * checkType 盘点类型：待盘点2000,异常资产2001，正常资产2002
     */
    public int CHECK_TYPE;

    /**
     * 打开选人、选部门URL
     */
    private String mOpenSelectUrl;
    /**
     * 区分选人、选部门
     */
    private int mSelectType;

    private int mId;
    /**
     * 审批历史URL， 接口获取
     */
    private String mApprovalHistoryUrl;
    /** 需要关闭当前页面并且刷新，应用于处理审批，同意审批后刷新待审批列表*/
    private boolean isFinishAndRefresh;
    
    private static final int REQUEST_CODE_REFRESH = 122;

    /**
     * @param context
     * @param url     WebView 需要加载的完整URL
     * @param id      资产ID，盘点资产,审批流程才有值，其他不需要可传0
     */
    public static void start(Context context, String url, int id) {
        start(context, url, id, LOADING_ASSET_NORMAL);
    }

    /**
     * @param context     context
     * @param url         url
     * @param id          ID，盘点资产,审批流程才有值，其他不需要可传0
     * @param loadingType 加载页面类型， 资产主页、审批详情、选人选部门、普通
     */
    public static void start(Context context, String url, int id, int loadingType) {
        start(context, url, id, loadingType, false);
    }

    /**
     * @param context     context
     * @param url         url
     * @param id          ID，盘点资产,审批流程才有值，其他不需要可传0
     * @param loadingType 加载页面类型， 资产主页、审批详情、选人选部门、普通
     * @param isFinishAndRefresh    需要关闭当前页面并且刷新上一页面，应用于处理审批，同意审批后刷新待审批列表
     */
    public static void start(Context context, String url, int id, int loadingType, boolean isFinishAndRefresh) {
        Intent lIntent = new Intent(context, AssetWebViewShowActivity.class);
        Bundle lBundle = new Bundle();
        lBundle.putString(EXTRA_URL, url);
        lBundle.putInt(EXTRA_ID, id);
        lBundle.putInt(EXTRA_LOADING_TYPE, loadingType);
        lBundle.putBoolean(EXTRA_IS_RESULT, isFinishAndRefresh);
        lIntent.putExtras(lBundle);
        if (!(context instanceof Activity)) {
            lIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        if(isFinishAndRefresh)
            ((Activity)context).startActivityForResult(lIntent, REQUEST_CODE_REFRESH);
        else
            context.startActivity(lIntent);
            
    }


    @NonNull
    @Override
    public AssetWebViewShowPresenter createPresenter() {
        return new AssetWebViewShowPresenter();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initAction();
        initWebView();
        getTicket();
    }

    private void initAction() {
        if (getIntent().getExtras() == null) {
            loadUrl(null);
            return;
        }
        jumpUrl = getIntent().getExtras().getString(EXTRA_URL);
        mId = getIntent().getExtras().getInt(EXTRA_ID);
        isFinishAndRefresh = getIntent().getExtras().getBoolean(EXTRA_IS_RESULT, false);
//        if (TextUtils.isEmpty(jumpUrl)) {
//            loadUrl(null);
//            return;
//        }

        switch (getIntent().getExtras().getInt(EXTRA_LOADING_TYPE)) {
            case LOADING_ASSET_MAIN: // 主界面, WebView + 底部 tab 栏
                setContentView(R.layout.activity_asset_management_container);
                initAssetManagerView();
                break;
            case LOADING_ASSET_APPROVAL://审批页面,标题栏自己定义 + WebView
                setContentView(R.layout.activity_asset_approval_detail);
                initApprovalView();
                break;
            case LOADING_ASSET_SELECT: // 选人选部门，暂时另一个新界面

                break;
            case LOADING_ASSET_NORMAL: // 普通界面，只有一个WebView
                setContentView(R.layout.activity_asset_web_view_show_container);
                break;
            default:

        }

    }

    /**
     * 初始化资产主界面
     */
    private void initAssetManagerView() {
        initToolBar(this);

        mToolBarManager
                .setTitle(getString(R.string.text_asset_management))
                .showRightImage(true)
                .setRightImage(Helper_Drawable.tintDrawable(mActivity.getResources().getDrawable(R.drawable.ic_asset_sweep_the_yard), ColorStateList.valueOf(Color.WHITE)))
                .clickRightImage(view -> {
//                    Intent lIntent = new Intent (mActivity, CaptureActivity.class);
//                    Bundle bundle = new Bundle ();
//                    bundle.putString ("type", "home_scan");
//                    lIntent.putExtras (bundle);
//                    startActivity (lIntent);

                    CaptureActivity.start(mActivity, "home_scan", 0);
                });

        tab_home = (TextView) findViewById(R.id.tab_home);
        tab_add = (TextView) findViewById(R.id.tab_add);
        tab_my = (TextView) findViewById(R.id.tab_my);
        setMainOnInteractListener();
    }

    /**
     * 初始化审批页并获取数据
     */
    private void initApprovalView() {
        mTitle = (TextView) findViewById(R.id.tv_title_approval);
        mTitleNo = (TextView) findViewById(R.id.tv_title_approval_NO);
        mHistory = (TextView) findViewById(R.id.tv_approval_history);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        setOnInteractListener();
        loadData();
    }

    private void setMainOnInteractListener() {
        tab_home.setOnClickListener(this);
        tab_add.setOnClickListener(this);
        tab_my.setOnClickListener(this);

    }

    private void setOnInteractListener() {
        mHistory.setOnClickListener(this);

        mToolbar.setNavigationOnClickListener(v -> finish());
    }


    private void initWebView() {
        webView = (WebView) findViewById(R.id.asset_management_web_view);
        //        tv_error = (TextView) findViewById (tv_error);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setDisplayZoomControls(false);
        webSettings.setSupportZoom(false);
        //屏蔽掉长按事件 因为webview长按时将会调用系统的复制控件
        webView.setOnLongClickListener((v) -> {
            return true;
        });
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


    private void loadData() {
        if (mId != 0)
            presenter.getApprovalDetailInfo(mId);
        else { // 单纯显示审批历史页面
            mTitle.setText(getString(R.string.approval_history));
            mTitleNo.setVisibility(View.GONE);
        }
    }

    private void loadUrl(String ticket) {
        if (!TextUtils.isEmpty(jumpUrl) && !jumpUrl.contains("http://")) {
            jumpUrl = ZSSXApplication.instance.getUser().getEamHostUrl() + jumpUrl + getTicketFlag(jumpUrl, ticket);
        }
        if (webView != null) {
            if (!TextUtils.isEmpty(jumpUrl)) {
                webView.loadUrl(jumpUrl);
            } else
                webView.loadDataWithBaseURL(null, errorHtml, "text/html", "utf-8", null);
        } else {
            webView = new WebView(this);
            setContentView(webView);
            webView.loadDataWithBaseURL(null, errorHtml, "text/html", "utf-8", null);
        }
    }

    @Override
    public void onSuccessTicket(String ticket) {
//        if(isSelectOperation) { // 选人、选部门，临时处理
//            String fullUrl = ZSSXApplication.instance.getUser().getEamHostUrl() + mOpenSelectUrl + getTicketFlag(mOpenSelectUrl, ticket);
//            LogUtil.Log(TAG, "fullUrl = " + fullUrl);
//            startActivityForResult(AssetWebViewChooseActivity.start(AssetWebViewShowActivity.this, fullUrl), mSelectType);
//        }

        loadUrl(ticket);
    }

    @Override
    public void GetApprovalDetailInfo(ApprovalDetailBean approvalDetailBean) {
        initData(approvalDetailBean);
    }

    /**
     * 初始化审批详情页数据-->标题栏
     *
     * @param approvalDetailBean 申请类型、单号
     */
    private void initData(ApprovalDetailBean approvalDetailBean) {
        if (approvalDetailBean == null)
            return;
        assert mTitle != null;
        mTitle.setText(approvalDetailBean.getOrderTypeCN());
        assert mTitleNo != null;
        mTitleNo.setText(approvalDetailBean.getOrderNo());
        mApprovalHistoryUrl = approvalDetailBean.getApprovalHistoryUrl();
    }

    @Override
    public void onErrorHandle(Throwable e) {
        super.onErrorHandle(e);
        loadUrl(""); // 空则加载失败页面
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

        @Override
        public void openFileChooser(ValueCallback<Uri> valueCallback, String s, String s1) {
            super.openFileChooser(valueCallback, s, s1);

        }

    };


    private WebViewClient webViewClient = new WebViewClient() {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            LogUtil.Log(TAG, "shouldOverrideUrlLoading url = " + url);
            if (!SysRes.isConnected(mActivity)) {
                Toast.Short(AssetWebViewShowActivity.this, "无法连接网络，请检查网络设置...");

            } else {
                if (LoginFailUtil.isLoginFailUrl(AssetWebViewShowActivity.this, url)) { // 是否登陆失效，是则注销当前跳转到登陆界面
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
            presenter.hideDialog();
            super.onReceivedError(webView, i, s, s1);
        }

        @Override
        public void onReceivedHttpError(WebView webView, WebResourceRequest webResourceRequest, WebResourceResponse webResourceResponse) {
            super.onReceivedHttpError(webView, webResourceRequest, webResourceResponse);
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        if(webView != null)
            webView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(webView != null)
            webView.onResume();
    }


    public int getmId() {
        return mId;
    }

    /**
     * 上传图片成功回调
     *
     * @param s
     */
    @Override
    public void showPicture(String s) {
        Toast.Short(this, getString(R.string.text_update_photo_success2));
        if(webView != null)
            webView.loadUrl("javascript:androidRefreshUploadAssetPicture('" + s + "')");
    }

    @Override
    public void upLoadRemark() {
        if (lDialog_hold_message != null && lDialog_hold_message.isShowing()) {
            lDialog_hold_message.dismiss();
        }
        if (CHECK_TYPE == 2000)// 更新  待盘点资产页
            webView.loadUrl("javascript:funcHide(" + upLoadRemarksAssetBean.getCheckId() + "," + upLoadRemarksAssetBean.getAssetId() + ")");
        else { // 更新 已盘点资产页面 （正常、异常）
            webView.loadUrl("javascript:RefreshRemark(" + upLoadRemarksAssetBean.getCheckId() + "," + CHECK_TYPE + ")");
        }
    }

    @Override
    public void uploadRemarks() {
        if (lDialog_hold_message.isShowing()) {
            lDialog_hold_message.dismiss();
        }
        //调用Js方法，更新页面
        webView.loadUrl("javascript:reloadPage()");
    }

    @Override
    public void onClick(View v) {
        if (TimeUtils.isFastDoubleClick()) {
            return;
        }
        switch (v.getId()) {
            case R.id.tab_home:
                finish();
                break;
            case R.id.tab_add:
                startActivity(new Intent(this, AddPageActivity.class));
                break;
            case R.id.tab_my:
                PersonCenterActivity.start(this);
                break;
            case R.id.tv_approval_history:
                openApprovalHistory();// 点击审批历史
                break;
            default:
                break;
        }
    }

    /**
     * 点击打开审批历史页面
     */
    private void openApprovalHistory() {
        if (!TextUtils.isEmpty(mApprovalHistoryUrl))
            AssetWebViewShowActivity.start(this, mApprovalHistoryUrl, 0);
    }

    class JavaScriptInterface {
        @JavascriptInterface
        public void backListener() {
            webView.post(() -> {
                LogUtil.Log(TAG, "NewsCenterWebFragment-->backListener(),webView.canGoBack() = " + webView.canGoBack());
                //                    FINISH();
                if (webView.canGoBack()) {
                    LogUtil.Log(TAG, "NewsCenterWebFragment-->webView.canGoBack()");
                    webView.goBack();
                } else {
                    LogUtil.Log(TAG, "NewsCenterWebFragment-->FINISH()");
                    finish();
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
         * 更新图片，上传图片
         *
         * @param assetsId 资产ID
         */
        @JavascriptInterface
        public void upLoadPictures(int assetsId) {
            LogUtil.Log(TAG, "assetsId = " + assetsId);
            mId = assetsId;
            setPictures();
        }

        /**
         * @param systemCount  systemCount
         * @param currentCount currentCount
         * @param checkId      checkId
         * @param assetId      assetId
         * @param checkType    盘点类型：待盘点2000,异常资产2001，正常资产2002
         */
        @JavascriptInterface
        public void showRemaksDialog(int systemCount, int currentCount, int checkId, int assetId, int checkType) {
            CHECK_TYPE = checkType;
            AssetRemarkBean remarkBean = new AssetRemarkBean();
            remarkBean.setSystemCount(systemCount);
            remarkBean.setCurrentCount(currentCount);
            remarkBean.setCheckId(checkId);
            remarkBean.setAssetId(assetId);
            remarkBean.setAssetType("single_remark");
            showRemarkDialog(remarkBean);
        }

        /**
         * @param checkId   checkId
         * @param assetType assetType
         */
        @JavascriptInterface
        public void showBatchAddRemarksDialog(int checkId, int assetType) {
            AssetRemarkBean remarkBean = new AssetRemarkBean();
            remarkBean.setCheckId(checkId);
            remarkBean.setAssetCheckModuleType(assetType);
            remarkBean.setAssetType("batch_add");
            showRemarkDialog(remarkBean);
            LogUtil.Log(TAG, presenter.getmUserBean().getMD5Password());
        }

        /**
         * 打开扫码界面
         */
        @JavascriptInterface
        public void showScan(int checkId) {
//            Intent lIntent = new Intent(mActivity, CaptureActivity.class);
//            Bundle bundle = new Bundle();
//            bundle.putString("type", "asset_scan");
//            lIntent.putExtras(bundle);
//            startActivity(lIntent);

            CaptureActivity.start(AssetWebViewShowActivity.this, "asset_scan", checkId);
        }

        @JavascriptInterface
        public void showPopuWindowMessage(String message) {
            showPopuWindow(message);
        }


        @JavascriptInterface
        public void useTelePhone(String phoneNum) {
            LogUtil.Log(TAG, "useTelePhone = " + phoneNum);
            startTelephone(phoneNum);
        }

        /**
         * 打开选择资产、地点、人方法
         *
         * @param url
         * @param selectType 打开Activity 请求码，用于返回该界面接收数据并刷新，onActivityForResult 使用,1001.1002,1003
         */
        @JavascriptInterface
        public void openAndroidWindow(String url, int selectType) {
//            mOpenSelectUrl = url;
            if (selectType > 0) {
                mSelectType = selectType;
                startActivityForResult(AssetWebViewChooseActivity.start(AssetWebViewShowActivity.this, url), mSelectType);
            } else {
                AssetWebViewShowActivity.start(AssetWebViewShowActivity.this, url, 0);
            }
//            presenter.getTicket();
//            String fullUrl = "http://" + AppConfiguration.getInstance().getServerAddress().getIp() + url + getTicketFlag(url);
//            LogUtil.Log(TAG, "fullUrl = " + fullUrl);
//            startActivityForResult(AssetWebViewChooseActivity.start(AssetWebViewShowActivity.this, fullUrl), selectType);
        }

        /**
         * 移动端打开bpm窗体.
         * bpmUrl:对应bpm地址,
         * approvedReturnUrl:审核后跳转的地址,
         * backButtonReturnUrl:点击返回跳转的地址;
         * <p>
         * 操作方法:
         * 1)bpm审批成功之后,发送消息给android,移动跳转到approvedReturnUrl指定地址;
         * 2)如果直接点击嵌套iframe资产审批页面的返回,资产系统这边处理返回到backButtonReturnUrl返回指定地址;
         */
        @JavascriptInterface
        public void androidOpenBpmWindow(String bpmUrl, String approvedReturnUrl, String backButtonReturnUrl) {
            LogUtil.Log(TAG, "bpmUrl = " + bpmUrl + ", approvedReturnUrl = " + approvedReturnUrl);
            mBackUrl = approvedReturnUrl;
//            AssetWebViewBPMActivity.start(mActivity, bpmUrl, approvedReturnUrl);
        }

        /**
         * 在BPM页面启动流程成功之后，跳到数据列表界面刷新数据
         */
        @JavascriptInterface
        public void androidCallBackListener() {
            LogUtil.Log(TAG, "androidCallBackListener");
            if(isFinishAndRefresh) { // 审批页是新开页，所以要关闭并通知上一个列表页刷新mBackUrl
                setResult(RESULT_OK);
                finish();
            } else {
                finish();
                AssetWebViewShowActivity.start(mActivity, mBackUrl, 0);
            }
//            loadUrl(null);
//            finish();
//            AssetWebViewShowActivity.start(mActivity, mBackUrl, 0);
        }

        /**
         * 资产分配成功后，刷新列表
         */
        @JavascriptInterface
        public void androidCallGoBack(String url) {
            toastSucc();
            finish();
            AssetWebViewShowActivity.start(mActivity, url, 0);
        }
        
        @JavascriptInterface
        public void openApprovalDetailWindow(String approvalUrl, int approvalId) {
            AssetWebViewShowActivity.start(AssetWebViewShowActivity.this, approvalUrl, approvalId, AssetWebViewShowActivity.LOADING_ASSET_APPROVAL, true);
        }

        @JavascriptInterface
        public void openAssetCard(String url) {
//            presenter.getTicket();
            AssetWebViewShowActivity.start(AssetWebViewShowActivity.this, url, 0);
        }


    }
    
    private void toastSucc() {
        runOnUiThread(() -> {
            showInfo("操作成功!");
        });
    }


    private void back() {
        runOnUiThread(() -> {
            if (webView.canGoBack()) {
                webView.goBack();
            } else {
                finish();
            }
        });

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (webView.canGoBack()) {
                back();
//                webView.goBack(); //goBack()表示返回WebView的上一页面
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }


    private void showTakePictureDialog() {
        if (mBottomSelectorPopDialog == null) {
            CharSequence[] actionTexts = new CharSequence[]{"从相机中选择", "拍照"};
            mBottomSelectorPopDialog = new BottomSelectorPopDialog(this, actionTexts, this.getResources().getColor(R.color.main_color));
            mBottomSelectorPopDialog.setListener(new BottomSelectorPopDialog.Listener() {
                @Override
                public void onBtnItemClick(int position) {
                    switch (position) {
                        case 0:
                            //从手机选择
                            startSelectPhotos();
                            break;
                        case 1:
                            //拍照
                            startActionCamera();
                            break;
                        default:
                            break;
                    }
                }

                @Override
                public void onCancelBtnClick() {
                    mBottomSelectorPopDialog.dismiss();
                }
            });
        }
        mBottomSelectorPopDialog.show();
    }

    private void setPictures() {
        RxPermissions.getInstance(this).request(Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(aBoolean -> {
                    if (aBoolean) {
                        showTakePictureDialog();
                    } else {
                        Toast.Short(AssetWebViewShowActivity.this, R.string.text_please_setting_authority);
                    }
                }, throwable -> {

                });
    }

    /**
     * 照片 请求码
     */
    public final static int CAMERA = 100;
    public final static int PICTURE = 101;
    public final static int UPLOADPICTURE = 102;

    /**
     * 从手机中选择
     */
    private void startSelectPhotos() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        //		打开相册
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "选择图片"), PICTURE);
    }

    /**
     * 拍照
     */
    private void startActionCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        mCameraTempFileUri = presenter.getCameraTempFile(this);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mCameraTempFileUri);
        startActivityForResult(intent, CAMERA);
    }

    public static Uri imgUri = null;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case CAMERA:
                imgUri = mCameraTempFileUri;
                presenter.doCropImage(imgUri, this, UPLOADPICTURE);
                break;
            case PICTURE:
                imgUri = data.getData();
                presenter.doCropImage(imgUri, this, UPLOADPICTURE);
                break;
            case UPLOADPICTURE:
                presenter.userPhotoUpdate(this);  //上传，不需要传Uri,在presenter已经得到
                break;
            case REQUEST_CODE_REFRESH:
                updateWebView();
//                jumpUrl = mBackUrl;
//                getTicket();
                break;

            default:
                break;
        }

        if (data != null)
            updateWebViewInfo(data.getIntExtra("selectType", -1), data.getStringExtra(AssetWebViewChooseActivity.TAG));

    }
    
    private void updateWebView() {
        jumpUrl = mBackUrl;
        getTicket();
    }

    private void updateWebViewInfo(int selectType, String msg) {
        switch (selectType) {
            case SELECT_ASSET:// 选择资产，回调JS传递数据
                webView.loadUrl("javascript:selectAssetCallback('" + msg + "')");
                break;
            case SELECT_SITE:// 选择地点，回调JS传递数据
                webView.loadUrl("javascript:SelectorSiteCallback('" + msg + "')");
                break;
            case SELECT_PERSON:// 选择人，回调JS传递数据
                webView.loadUrl("javascript:SelectorCallback('" + msg + "')");
                break;
            case SELECT_REPAIR:// 选择完成维修，回调JS传递数据
                webView.loadUrl("javascript:SelectRepairHandleRresultCallBack('" + msg + "')");
                break;
        }
    }

    private void showRemarkDialog(AssetRemarkBean remarkBean) {
        AlertDialog.Builder lBuilder = new AlertDialog.Builder(this);
        lBuilder.setCancelable(false);
        View lView = LayoutInflater.from(this).inflate(R.layout.dialog_scan_remarks, null);
        TextView lCancle = (TextView) lView.findViewById(R.id.cancel_remark_tv);
        TextView lSave = (TextView) lView.findViewById(R.id.save_remark_tv);
        TextView lCount = (TextView) lView.findViewById(R.id.text_count_tv);
        TextView tv_system_count = (TextView) lView.findViewById(R.id.tv_system_count);
        TextView tv_current_count = (TextView) lView.findViewById(R.id.tv_current_count);
        RadioGroup lRadioGroup = (RadioGroup) lView.findViewById(R.id.remark_radio_group);
        RadioGroup lRadioGroup2 = (RadioGroup) lView.findViewById(R.id.remark_radio_group2);
        EditText lEditText = (EditText) lView.findViewById(R.id.remark_et);
        LinearLayout ll_system_count = (LinearLayout) lView.findViewById(R.id.ll_system_count);
        LinearLayout ll_current_count = (LinearLayout) lView.findViewById(R.id.ll_current_count);
        TextView tv_prompt_message = (TextView) lView.findViewById(R.id.tv_prompt_message);
        if (remarkBean.getAssetType().equals("single_remark")) {
            ll_system_count.setVisibility(View.VISIBLE);
            ll_current_count.setVisibility(View.VISIBLE);
            tv_system_count.setText(remarkBean.getSystemCount() + "个");
            tv_current_count.setText(remarkBean.getCurrentCount() + "个");
        } else {
            tv_prompt_message.setVisibility(View.VISIBLE);
        }

        lBuilder.setView(lView);

        lDialog_hold_message = lBuilder.show();

        WindowManager.LayoutParams p = lDialog_hold_message.getWindow().getAttributes();
        p.width = (int) (this.getResources().getDisplayMetrics().widthPixels * 1.0);
        lDialog_hold_message.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        lDialog_hold_message.getWindow().setAttributes(p);

        lCancle.setOnClickListener(view -> lDialog_hold_message.dismiss());
        lSave.setOnClickListener(view -> {
            if (Helper_String.containsEmoji(lEditText.getText().toString().trim())) {
                showEmojiDialog();
                return;
            }

            if (remarkBean.getAssetType().equals("single_remark")) {
                upLoadRemarksAssetBean = new UpLoadRemarksAssetBean();
                upLoadRemarksAssetBean.setUserId(presenter.getmUserBean().getUserId());
                LogUtil.e(presenter.getmUserBean().getUserId());
                upLoadRemarksAssetBean.setAssetId(remarkBean.getAssetId());
                upLoadRemarksAssetBean.setIsRecord(2);
                upLoadRemarksAssetBean.setRemarks(lEditText.getText().toString());
                upLoadRemarksAssetBean.setCheckId(remarkBean.getCheckId());
                presenter.upLoadAssetData(upLoadRemarksAssetBean);

            } else {
                BatchAddNotesBean batchAddNotesBean = new BatchAddNotesBean();
                batchAddNotesBean.setAssetCheckModuleType(remarkBean.getAssetCheckModuleType());
                batchAddNotesBean.setCheckId(remarkBean.getCheckId());
                batchAddNotesBean.setUserId(presenter.getmUserBean().getUserId());
                batchAddNotesBean.setRemarks(lEditText.getText().toString());
                presenter.BatchAddNotes(batchAddNotesBean);
            }

        });
        lRadioGroup.setOnCheckedChangeListener((radioGroup, i) -> {
            switch (i) {
                case R.id.find_radio:
                    lRadioGroup2.clearCheck();
                    radioGroup.check(i);
                    lEditText.setText("");
                    lEditText.setText(getString(R.string.find_text));
                    break;
                case R.id.no_match_radio:
                    lRadioGroup2.clearCheck();
                    radioGroup.check(i);
                    lEditText.setText("");
                    lEditText.setText(getString(R.string.no_match_text));
                    break;
                case R.id.no_clear_radio:
                    lRadioGroup2.clearCheck();
                    radioGroup.check(i);
                    lEditText.setText("");
                    lEditText.setText(getString(R.string.no_clear_text));
                    break;
                default:
                    break;
            }
        });

        lRadioGroup2.setOnCheckedChangeListener((radioGroup, i) -> {
            switch (i) {
                case R.id.find_radio2:
                    lRadioGroup.clearCheck();
                    radioGroup.check(i);
                    lEditText.setText("");
                    lEditText.setText(getString(R.string.find_text2));
                    break;
                case R.id.no_match_radio2:
                    lRadioGroup.clearCheck();
                    radioGroup.check(i);
                    lEditText.setText("");
                    lEditText.setText(getString(R.string.no_match_text2));
                    break;
                default:
                    break;
            }
        });

        lEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                int lLength = editable.length();
                int count = 40 - lLength;
                lCount.setText("（剩余" + count + "字）");
            }
        });
    }

    /**
     * 请勿输入表情等特殊字符！
     */
    private void showEmojiDialog() {
        AlertDialog.Builder lBuilder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_not_emoji, null);
        Button confirm = (Button) view.findViewById(R.id.btn_dialog_confirm);
        lBuilder.setView(view);
        AlertDialog dialog = lBuilder.show();
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
        confirm.setOnClickListener(v -> dialog.dismiss());
    }

    public void startTelephone(String phoneNum) {
        Intent intent = null;
        if (!TextUtils.isEmpty(phoneNum)) {
            intent = new Intent(Intent.ACTION_DIAL,
                    Uri.parse("tel:" + phoneNum));
        } else {
            intent = new Intent(Intent.ACTION_CALL_BUTTON);
        }
        mActivity.startActivity(intent);
    }

    public void showPopuWindow(String message) {
        List<String> list = new ArrayList<>();
        list.add("查看全文");
        new CenterItemDialog(list, mActivity)
                .setOnItemClickListener(position -> {
                    if (list.size() == 1) {
                        new DetailSchedulePopupWindow(mActivity, "asset")
                                .setMessage(message)
                                .onCreate();
                    }
                })
                .create();

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
        presenter.detachView(false);
        if (mToolBarManager != null)
            mToolBarManager.destroy();
    }

}
