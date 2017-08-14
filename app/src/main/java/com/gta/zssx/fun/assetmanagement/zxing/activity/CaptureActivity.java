/*
 * Copyright (C) 2008 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.gta.zssx.fun.assetmanagement.zxing.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.zxing.Result;
import com.gta.utils.helper.Helper_String;
import com.gta.utils.resource.SysRes;
import com.gta.utils.resource.Toast;
import com.gta.utils.viewPagerIndicator.CirclePageIndicator;
import com.gta.zssx.AppConfiguration;
import com.gta.zssx.R;
import com.gta.zssx.ZSSXApplication;
import com.gta.zssx.fun.assetmanagement.model.bean.AssetBean;
import com.gta.zssx.fun.assetmanagement.model.bean.UpLoadRemarksAssetBean;
import com.gta.zssx.fun.assetmanagement.presenter.AssetInventoryPresenter;
import com.gta.zssx.fun.assetmanagement.view.AssetInventoryView;
import com.gta.zssx.fun.assetmanagement.view.page.AssetWebViewShowActivity;
import com.gta.zssx.fun.assetmanagement.zxing.camera.CameraManager;
import com.gta.zssx.fun.assetmanagement.zxing.decode.DecodeThread;
import com.gta.zssx.fun.assetmanagement.zxing.utils.BeepManager;
import com.gta.zssx.fun.assetmanagement.zxing.utils.CaptureActivityHandler;
import com.gta.zssx.fun.assetmanagement.zxing.utils.InactivityTimer;
import com.gta.zssx.fun.personalcenter.model.bean.UserBean;
import com.gta.zssx.pub.base.BaseActivity;
import com.gta.zssx.pub.manager.ToolBarManager;
import com.gta.zssx.pub.util.TimeUtils;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * This activity opens the camera and does the actual scanning on a background
 * thread. It draws a viewfinder to help the user place the barcode correctly,
 * shows feedback as the image processing is happening, and then overlays the
 * results when a scan is successful.
 *
 * @author dswitkin@google.com (Daniel Switkin)
 * @author Sean Owen
 */
public final class CaptureActivity extends BaseActivity<AssetInventoryView, AssetInventoryPresenter> implements SurfaceHolder.Callback, AssetInventoryView {

    private static final String TAG = CaptureActivity.class.getSimpleName();
    public static final String EXTRA_TYPE = "type";
    public static final String EXTRA_CHECKID = "checkId";
    /**
     * 加载框提示语
     */
    public static final int SCANING_TYPE = 2;// 正在识别...
    public static final int LOADING_TYPE = 1;// 正在加载...

    private CameraManager cameraManager;
    private CaptureActivityHandler handler;
    private InactivityTimer inactivityTimer;
    private BeepManager beepManager;

    private SurfaceView scanPreview = null;
    private RelativeLayout scanContainer;
    private RelativeLayout scanCropView;

    private Rect mCropRect = null;
    private boolean isHasSurface = false;
    private boolean isLight = false;
    public ViewPager mViewPager;
    public CirclePageIndicator mPageIndicator;
    public LinkedList<AssetBean> mViewList;
    public ViewPagerAdapter mViewPagerAdapter;
    public RelativeLayout mRelativeLayout;
    TextView tvCount, tv_title_assets;

    private Toolbar mToolbar;
    private ToolBarManager mToolBarManager;
    private AssetBean assetBean;
    private String type;//进入的标示，是首页进入还是盘点进入
    /**
     * 从盘点进入会传入 该ID, 首页进入不会
     */
    private int checkId;
    private UserBean mUserBean;
    private UpLoadRemarksAssetBean upLoadRemarksAssetBean;
    AlertDialog lDialog_scanning_again, lDialog_hold_message;
    private String mRemarks = null;
    private RelativeLayout rl_progress;
    private int scanAssetId;

    public Handler getHandler() {
        return handler;
    }

    public CameraManager getCameraManager() {
        return cameraManager;
    }

    public static void start(Context context, String type, int checkId) {
        Intent lIntent = new Intent(context, CaptureActivity.class);
        lIntent.putExtra(EXTRA_TYPE, type);
        lIntent.putExtra(EXTRA_CHECKID, checkId);
        if (!(context instanceof Activity)) {
            lIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(lIntent);
    }

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_capture);

        type = getIntent().getStringExtra(EXTRA_TYPE);
        checkId = getIntent().getIntExtra(EXTRA_CHECKID, 0);
        scanPreview = (SurfaceView) findViewById(R.id.capture_preview);
        scanContainer = (RelativeLayout) findViewById(R.id.capture_container);
        scanCropView = (RelativeLayout) findViewById(R.id.capture_crop_view);
        ImageView scanLine = (ImageView) findViewById(R.id.capture_scan_line);

        inactivityTimer = new InactivityTimer(this);
        beepManager = new BeepManager(this);

        TranslateAnimation animation = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0.0f, Animation
                .RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT,
                0.9f);
        animation.setDuration(4000);
        animation.setRepeatCount(-1);
        animation.setRepeatMode(Animation.RESTART);
        if (scanLine != null) {
            scanLine.startAnimation(animation);
        }

        findView();
        setOnInteractListener();
        loadData();
    }

    private void loadData() {
        upLoadRemarksAssetBean = new UpLoadRemarksAssetBean();
        try {
            mUserBean = AppConfiguration.getInstance().getUserBean();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setOnInteractListener() {
        mViewList = new LinkedList<>();

        mToolBarManager.init();
        mToolbar.setNavigationOnClickListener(v -> onBackPressed());

        mToolBarManager.setTitle(getString(R.string.asset_title))
                .showRightImage(true)
                .setRightImage(R.drawable.guan_the_flash)
                .showRightButton(false)
                .clickRightImage(view -> {
                    if (!isLight) {
                        turnLightOn(cameraManager.getCamera());
                        isLight = true;
                    } else {
                        turnLightOff(cameraManager.getCamera());
                        isLight = false;
                    }
                });
    }

    private void findView() {

        mPageIndicator = (CirclePageIndicator) findViewById(R.id.indicator);
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mRelativeLayout = (RelativeLayout) findViewById(R.id.framelayout);
        tv_title_assets = (TextView) findViewById(R.id.tv_title_assets);
        if (tv_title_assets != null) {
            tv_title_assets.setVisibility(View.VISIBLE);
        }
        rl_progress = (RelativeLayout) findViewById(R.id.rl_progress);

        mToolbar = (Toolbar) findViewById(R.id.capture_toolbar);
        mToolbar.setBackgroundColor(this.getResources().getColor(R.color.gray_4c4c4c));
        mToolBarManager = new ToolBarManager(mToolbar, this);
        tvCount = (TextView) findViewById(R.id.scan_result_tv);

    }

    @Override
    protected void onResume() {
        super.onResume();

        // CameraManager must be initialized here, not in onCreate(). This is
        // necessary because we don't
        // want to open the camera driver and measure the screen size if we're
        // going to show the help on
        // first launch. That led to bugs where the scanning rectangle was the
        // wrong size and partially
        // off screen.
        cameraManager = new CameraManager(getApplication());

        handler = null;

        if (isHasSurface) {
            // The activity was paused but not stopped, so the surface still
            // exists. Therefore
            // surfaceCreated() won't be called, so init the camera here.
            initCamera(scanPreview.getHolder());
        } else {
            // Install the callback and wait for surfaceCreated() to init the
            // camera.
            scanPreview.getHolder().addCallback(this);
        }

        inactivityTimer.onResume();
    }

    @NonNull
    @Override
    public AssetInventoryPresenter createPresenter() {
        return new AssetInventoryPresenter();
    }

    @Override
    protected void onPause() {
        if (handler != null) {
            handler.quitSynchronously();
            handler = null;
        }
        inactivityTimer.onPause();
        beepManager.close();
        cameraManager.closeDriver();
        if (!isHasSurface) {
            scanPreview.getHolder().removeCallback(this);
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        inactivityTimer.shutdown();
        super.onDestroy();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (holder == null) {
            Log.e(TAG, "*** WARNING *** surfaceCreated() gave us a null surface!");
        }
        if (!isHasSurface) {
            isHasSurface = true;
            initCamera(holder);
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        isHasSurface = false;
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    /**
     * A valid barcode has been found, so give an indication of success and show
     * the results.
     *
     * @param rawResult The contents of the barcode.
     * @param bundle    The extras
     */
    public void handleDecode(Result rawResult, Bundle bundle) {
        inactivityTimer.onActivity();
        beepManager.playBeepSoundAndVibrate();

        if (!SysRes.isConnected(mActivity)) {
            changeMessage(getString(R.string.no_network));
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    changeMessage(getString(R.string.scanning_asset_code));
                    continuePreview();
                }
            }, 2000);
            return;
        }

        Intent resultIntent = new Intent();
        bundle.putInt("width", mCropRect.width());
        bundle.putInt("height", mCropRect.height());
        bundle.putString("result", rawResult.getText());
        resultIntent.putExtras(bundle);
        this.setResult(RESULT_OK, resultIntent);
        scanAssetId = 0;
        if (rawResult.getText().contains("assetId=")) {
            Pattern p = Pattern.compile("\\bassetId=(\\d+)");
            Matcher m = p.matcher(rawResult.getText());
            while (m.find()) {
                scanAssetId = Integer.parseInt(m.group(1));
            }
            changeMessage(getString(R.string.scanning_asset_code));
        } else {
            changeMessage(getString(R.string.scanning_true_code));
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    changeMessage(getString(R.string.scanning_asset_code));
                    continuePreview();
                }
            }, 2000);
            return;
        }
        if ("home_scan".equals(type)) {
            presenter.getTicket();
//            AssetWebViewShowActivity.start(this, ZSSXApplication.instance.getUser().getEamHostUrl() + getTicketFlag(getString(R.string.asset_card_details) + scanAssetId, ""), scanAssetId);
        } else {
            presenter.GetAssetByAssetId(scanAssetId, checkId, SCANING_TYPE);
        }
        //        showRepeatDialog();

    }

    /**
     * 重复扫描时，Dialog
     *
     * @param assetBean 滑动卡片
     */
    private void showRepeatDialog(AssetBean assetBean) {
        if (lDialog_scanning_again != null && lDialog_scanning_again.isShowing())
            lDialog_scanning_again.dismiss();
        AlertDialog.Builder lBuilder = new AlertDialog.Builder(this);
        lBuilder.setCancelable(false);//按返回键不退出
        View lView = LayoutInflater.from(this).inflate(R.layout.dialog_scan_repeat, null);
        TextView noRecord = (TextView) lView.findViewById(R.id.no_record_tv);
        TextView record = (TextView) lView.findViewById(R.id.record_no_flag_tv);
        TextView flag = (TextView) lView.findViewById(R.id.record_and_flag_tv);
        TextView checkCount = (TextView) lView.findViewById(R.id.tv_check_count);
        lBuilder.setView(lView);

        int lCount = assetBean.getCurrentNumber();
        checkCount.setText(lCount > 0 ? lCount + "个" : "1个");

        lDialog_scanning_again = lBuilder.show();

        WindowManager.LayoutParams p = lDialog_scanning_again.getWindow().getAttributes();
        p.width = (int) (this.getResources().getDisplayMetrics().widthPixels * 0.75);
        lDialog_scanning_again.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        lDialog_scanning_again.getWindow().setAttributes(p);
        lDialog_scanning_again.setOnDismissListener(dialogInterface -> continuePreview());

        //重复扫描，不做记录
        noRecord.setOnClickListener(view -> {
            lDialog_scanning_again.dismiss();
        });

        //记录，不添加备注
        record.setOnClickListener(view -> {
            upLoadRemarksAssetBean.setUserId(mUserBean.getUserId());
            upLoadRemarksAssetBean.setAssetId(assetBean.getAssetId());
            upLoadRemarksAssetBean.setIsRecord(1);
            upLoadRemarksAssetBean.setRemarks(assetBean.getRemarks());
            upLoadRemarksAssetBean.setCheckId(checkId);
            presenter.upLoadAssetData(upLoadRemarksAssetBean, 2, true);

            // 添加下面滑动卡片, 在网络请求成功之后添加
//            addView(assetBean);
        });

        //记录，并添加备注
        flag.setOnClickListener(view -> {
            showRemarkDialog(assetBean, 1, true);
        });
    }

    /**
     * 资产被扫描时已有备注，弹出的Dialog
     *
     * @param assetBean 滑动卡片
     */
    private void showIsRecordDialog(AssetBean assetBean) {
        AlertDialog.Builder lBuilder = new AlertDialog.Builder(this);
        lBuilder.setCancelable(false);//按返回键不退出
        View lView = LayoutInflater.from(this).inflate(R.layout.dialog_scan_repeat_remarks, null);
        TextView tv_message = (TextView) lView.findViewById(R.id.tv_message);
        TextView tv_no_record = (TextView) lView.findViewById(R.id.no_record_tv);
        TextView tv_record = (TextView) lView.findViewById(R.id.record_no_flag_tv);
        tv_message.setText(getString(R.string.crrent_record) + assetBean.getRemarks());
        lBuilder.setView(lView);
        AlertDialog lDialog = lBuilder.show();
        WindowManager.LayoutParams p = lDialog.getWindow().getAttributes();
        p.width = (int) (this.getResources().getDisplayMetrics().widthPixels * 0.85);
        lDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        lDialog.getWindow().setAttributes(p);
        //        lDialog.setOnDismissListener (dialogInterface -> continuePreview ());

        lDialog.setCanceledOnTouchOutside(false);

        tv_no_record.setOnClickListener(view -> {
            //不需要修改备注
            lDialog.dismiss();
            // 添加下面滑动卡片
            addView(assetBean);
            continuePreview();
        });

        tv_record.setOnClickListener(view -> {
            //修改备注
            showRemarkDialog(assetBean, 2, true);
            lDialog.dismiss();
        });

    }

    /**
     * 保存备注dialog
     *
     * @param assetBean     一个卡片实体
     * @param statu         是否要上传盘点记录  1 是  2 否 (只修改备注)
     * @param isAddCardView 是否添加下面滑动卡片(不记录,只修改备注则不添加)
     */
    private void showRemarkDialog(AssetBean assetBean, int statu, boolean isAddCardView) {
        this.mIsAddCardView = isAddCardView;

        if (lDialog_hold_message != null && lDialog_hold_message.isShowing())
            lDialog_hold_message.dismiss();
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

        lBuilder.setView(lView);

        lDialog_hold_message = lBuilder.show();

        WindowManager.LayoutParams p = lDialog_hold_message.getWindow().getAttributes();
        p.width = (int) (this.getResources().getDisplayMetrics().widthPixels * 1.0);
        lDialog_hold_message.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        lDialog_hold_message.getWindow().setAttributes(p);

        lDialog_hold_message.setOnDismissListener(dialog -> {
            mIsAddCardView = true;
            continuePreview();
        });

        lCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lDialog_hold_message.dismiss();
            }
        });
        lSave.setOnClickListener(view -> {
            if (Helper_String.containsEmoji(lEditText.getText().toString().trim())) { // 输入内容含有表情
                showEmojiDialog();
                return;
            }

            upLoadRemarksAssetBean.setUserId(mUserBean.getUserId());
            upLoadRemarksAssetBean.setAssetId(assetBean.getAssetId());
            upLoadRemarksAssetBean.setIsRecord(statu);
            mRemarks = lEditText.getText().toString();
            upLoadRemarksAssetBean.setRemarks(mRemarks);
            upLoadRemarksAssetBean.setCheckId(checkId);
            presenter.upLoadAssetData(upLoadRemarksAssetBean, 3, true);
            //            lDialog_hold_message.dismiss ();

//            if(statu == 1) { // 修改备注不添加
//                // 添加下面滑动卡片
//                addView(assetBean);
//            }

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

        tv_system_count.setText(assetBean.getSystemNumber() + "个");
        tv_current_count.setText(assetBean.getCurrentNumber() + "个");
        lEditText.setText(assetBean.getRemarks());
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


    /**
     * 扫码完成，下面滑动栏添加一个卡片
     *
     * @param assetBean
     */
    private void addView(AssetBean assetBean) {
        mViewList.addFirst(assetBean);
        if (mViewList.size() > 5) {
            mViewList.removeLast();
        }
        if (mViewPagerAdapter == null) {
            mRelativeLayout.setVisibility(View.VISIBLE);
            mViewPagerAdapter = new ViewPagerAdapter(mViewList);
            mViewPager.setAdapter(mViewPagerAdapter);
        } else {
            mViewPagerAdapter.notifyDataSetChanged();
        }
        if (mViewPagerAdapter.getCount() > 1) {
            mPageIndicator.setViewPager(mViewPager);
            mPageIndicator.setCurrentItem(0);
            mPageIndicator.setVisibility(View.VISIBLE);
        } else {
            mPageIndicator.setVisibility(View.INVISIBLE);
        }

        tvCount.setVisibility(View.VISIBLE);
        tvCount.setText("最近" + mViewList.size() + "个盘点结果：");

    }

    public void continuePreview() {
        SurfaceView surfaceView = (SurfaceView) findViewById(R.id.capture_preview);
        SurfaceHolder surfaceHolder = surfaceView.getHolder();
        initCamera(surfaceHolder);
        if (handler != null) {
            handler.restartPreviewAndDecode();
        }
    }

    /**
     * 是否添加下面滑动卡片,(暂时这么处理,不太恰当)
     */
    boolean mIsAddCardView = true;

    @Override
    public void hideDismissDialog() {

        if (mIsAddCardView) {
            // 添加下面滑动卡片, 一定要比下面代码先执行
            addView(assetBean);
        }
        mIsAddCardView = true;// 恢复默认值,修改范围小点

        if (!TextUtils.isEmpty(mRemarks)) { // 刷新卡片备注信息，找到对应的TextView
            TextView lTextView = (TextView) mViewPager.findViewWithTag("tv_message" + mViewPager.getCurrentItem());
            if (lTextView != null)
                lTextView.setText(getString(R.string.flag_change_remarks) + mRemarks);
            AssetBean lAssetBean = mViewList.get(mViewPager.getCurrentItem());
            lAssetBean.setRemarks(mRemarks);
        }
        mRemarks = null;

        if (lDialog_scanning_again != null && lDialog_scanning_again.isShowing()) {
            lDialog_scanning_again.dismiss();
        }

        if (lDialog_hold_message != null && lDialog_hold_message.isShowing()) {
            lDialog_hold_message.dismiss();
        }
    }

    private void initCamera(SurfaceHolder surfaceHolder) {
        if (surfaceHolder == null) {
            throw new IllegalStateException("No SurfaceHolder provided");
        }
        if (cameraManager.isOpen()) {
            Log.w(TAG, "initCamera() while already open -- late SurfaceView callback?");
            return;
        }
        try {
            cameraManager.openDriver(surfaceHolder);
            // Creating the handler starts the preview, which can also throw a
            // RuntimeException.
            if (handler == null) {
                handler = new CaptureActivityHandler(this, cameraManager, DecodeThread.ALL_MODE);
            }

            initCrop();
        } catch (IOException ioe) {
            Log.w(TAG, ioe);
            displayFrameworkBugMessageAndExit();
        } catch (RuntimeException e) {
            // Barcode Scanner has seen crashes in the wild of this variety:
            // java.?lang.?RuntimeException: Fail to connect to camera service
            Log.w(TAG, "Unexpected error initializing camera", e);
            displayFrameworkBugMessageAndExit();
        }
    }

    private void displayFrameworkBugMessageAndExit() {
        // camera error
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.app_name));
        builder.setMessage("Camera error");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }

        });
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {

            @Override
            public void onCancel(DialogInterface dialog) {
                finish();
            }
        });
        builder.show();
    }

    public void restartPreviewAfterDelay(long delayMS) {
        if (handler != null) {
            handler.sendEmptyMessageDelayed(R.id.restart_preview, delayMS);
        }
    }

    public Rect getCropRect() {
        return mCropRect;
    }

    /**
     * 初始化截取的矩形区域
     */
    private void initCrop() {
        int cameraWidth = cameraManager.getCameraResolution().y;
        int cameraHeight = cameraManager.getCameraResolution().x;

        /** 获取布局中扫描框的位置信息 */
        int[] location = new int[2];
        scanCropView.getLocationInWindow(location);

        int cropLeft = location[0];
        int cropTop = location[1] - getStatusBarHeight();

        int cropWidth = scanCropView.getWidth();
        int cropHeight = scanCropView.getHeight();

        /** 获取布局容器的宽高 */
        int containerWidth = scanContainer.getWidth();
        int containerHeight = scanContainer.getHeight();

        /** 计算最终截取的矩形的左上角顶点x坐标 */
        int x = cropLeft * cameraWidth / containerWidth;
        /** 计算最终截取的矩形的左上角顶点y坐标 */
        int y = cropTop * cameraHeight / containerHeight;

        /** 计算最终截取的矩形的宽度 */
        int width = cropWidth * cameraWidth / containerWidth;
        /** 计算最终截取的矩形的高度 */
        int height = cropHeight * cameraHeight / containerHeight;

        /** 生成最终的截取的矩形 */
        mCropRect = new Rect(x, y, width + x, height + y);
    }

    private int getStatusBarHeight() {
        try {
            Class<?> c = Class.forName("com.android.internal.R$dimen");
            Object obj = c.newInstance();
            Field field = c.getField("status_bar_height");
            int x = Integer.parseInt(field.get(obj).toString());
            return getResources().getDimensionPixelSize(x);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }


    /**
     * 通过设置Camera关闭闪光灯
     *
     * @param mCamera
     */
    public void turnLightOff(Camera mCamera) {
        if (mCamera == null) {
            return;
        }
        Camera.Parameters parameters = mCamera.getParameters();
        if (parameters == null) {
            return;
        }
        List<String> flashModes = parameters.getSupportedFlashModes();
        String flashMode = parameters.getFlashMode();
        // Check if camera flash exists
        if (flashModes == null) {
            return;
        }
        if (!Camera.Parameters.FLASH_MODE_OFF.equals(flashMode)) {
            // Turn off the flash
            if (flashModes.contains(Camera.Parameters.FLASH_MODE_OFF)) {
                parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                mCamera.setParameters(parameters);
            } else {
                Log.e(TAG, "FLASH_MODE_OFF not supported");
            }
        }
        mToolBarManager.setRightImage(R.drawable.guan_the_flash);
    }

    /**
     * 通过设置Camera打开闪光灯
     *
     * @param mCamera
     */
    public void turnLightOn(Camera mCamera) {
        if (mCamera == null) {
            return;
        }
        Camera.Parameters parameters = mCamera.getParameters();
        if (parameters == null) {
            Toast.Short(CaptureActivity.this, getString(R.string.cant_open_flashlight));
            return;
        }
        List<String> flashModes = parameters.getSupportedFlashModes();
        // Check if camera flash exists
        if (flashModes == null) {
            // Use the screen as a flashlight (next best thing)
            Toast.Short(CaptureActivity.this, getString(R.string.cant_open_flashlight));
            return;
        }
        String flashMode = parameters.getFlashMode();
        if (!Camera.Parameters.FLASH_MODE_TORCH.equals(flashMode)) {
            // Turn on the flash
            if (flashModes.contains(Camera.Parameters.FLASH_MODE_TORCH)) {
                parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                mCamera.setParameters(parameters);
            } else {
            }
        }
        mToolBarManager.setRightImage(R.drawable.flash_icon);
    }


    @Override
    public void showResult(AssetBean assetBean) {
        if (assetBean == null)
            return;
        this.assetBean = assetBean;

        if (!mIsAddCardView) { // 不添加卡片情况-->获取最新备注信息
            showRemarkDialog(assetBean, 2, false);
            return;
        }

        if (assetBean.getIsRepeatScan() == 1) {//重复扫描 1 是  2 否
            showRepeatDialog(assetBean);
        } else {
//            // 添加下面滑动卡片
//            addView(assetBean); 
            if (assetBean.getIsRepeatScan() == 2 && assetBean.getIsHasRemarks() == 1) {
                showIsRecordDialog(assetBean);
            } else {
                // 添加下面滑动卡片
                addView(assetBean);
            }
            upLoadRemarksAssetBean.setAssetId(assetBean.getAssetId());
            upLoadRemarksAssetBean.setIsRecord(1); // 是否要记录，1是 2 否
            upLoadRemarksAssetBean.setRemarks(assetBean.getRemarks());
            upLoadRemarksAssetBean.setUserId(mUserBean.getUserId());
            upLoadRemarksAssetBean.setCheckId(checkId);
            presenter.upLoadAssetData(upLoadRemarksAssetBean, 1, false);
        }

    }

    public class ViewPagerAdapter extends PagerAdapter {
        private List<AssetBean> list;

        public ViewPagerAdapter(List<AssetBean> list) {
            this.list = list;
        }

        @Override
        public int getCount() {
            return list == null ? 0 : list.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View lView = LayoutInflater.from(CaptureActivity.this).inflate(R.layout.item_scan, null);
            TextView lTextView = (TextView) lView.findViewById(R.id.scan_name_tv);
            TextView tv_model = (TextView) lView.findViewById(R.id.tv_model);
            TextView tv_count = (TextView) lView.findViewById(R.id.scan_count_tv);
            TextView look_item = (TextView) lView.findViewById(R.id.tv_look_item);
            TextView tv_message = (TextView) lView.findViewById(R.id.tv_message_show);
            String name;
            if (list.get(position).getAssetName().length() > 13) {
                name = list.get(position).getAssetName().substring(0, 13);
                name = name + "...";
                lTextView.setText(name);
            } else {
                lTextView.setText(list.get(position).getAssetName());
            }
            tv_model.setText(list.get(position).getAssetType());
            tv_count.setText(list.get(position).getCurrentNumber() <= 0 ? 1 + "" : list.get(position).getCurrentNumber() + 1 + "");// 第一次扫返回为0，所以N+1
            look_item.setOnClickListener(view -> {
                if (TimeUtils.isFastDoubleClick()) {
                    return;
                }
                presenter.getTicket();
//                String fullUrl = ZSSXApplication.instance.getUser().getEamHostUrl() + getString(R.string.asset_card_details) + assetBean.getAssetId() + getTicketFlag()
            });

            tv_message.setText(TextUtils.isEmpty(list.get(position).getRemarks()) ? getString(R.string.flag_add_remarks) : getString(R.string.flag_change_remarks) + list.get(position).getRemarks());
            tv_message.setOnClickListener(view -> {
                mIsAddCardView = false;
                presenter.GetAssetByAssetId(list.get(position).getAssetId(), checkId, LOADING_TYPE);
//                showRemarkDialog(list.get(position), 2, false);
            });
            String key = "tv_message" + position;
            tv_message.setTag(key);
            container.addView(lView);
            return lView;
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

    }

    public void showProgressDialog() {
        tv_title_assets.setVisibility(View.GONE);
        rl_progress.setVisibility(View.VISIBLE);
    }

    public void hideProgressDialog() {
        tv_title_assets.setVisibility(View.VISIBLE);
        rl_progress.setVisibility(View.GONE);
    }

    @Override
    public void changeMessage(String message) {
        tv_title_assets.setText(message);
    }

    @Override
    public void onSuccessTicket(String ticket) {
        String fullUrl = ZSSXApplication.instance.getUser().getEamHostUrl() + getAccountInfo(getString(R.string.asset_card_details)
                + scanAssetId, ticket);
        AssetWebViewShowActivity.start(CaptureActivity.this, fullUrl, scanAssetId);
    }

}