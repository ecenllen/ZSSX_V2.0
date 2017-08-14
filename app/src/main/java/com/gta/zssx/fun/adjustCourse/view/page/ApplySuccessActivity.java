package com.gta.zssx.fun.adjustCourse.view.page;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.PhoneUtils;
import com.gta.zssx.R;
import com.gta.zssx.fun.adjustCourse.model.bean.ApplySuccessBean;
import com.gta.zssx.fun.adjustCourse.presenter.ApplySuccessPresenter;
import com.gta.zssx.fun.adjustCourse.view.ApplySuccessView;
import com.gta.zssx.fun.adjustCourse.view.base.Constant;
import com.gta.zssx.fun.adjustCourse.view.page.v2.AdjustMvpMainActivityV2;
import com.gta.zssx.pub.base.BaseActivity;
import com.gta.zssx.pub.manager.ToolBarManager;
import com.gta.zssx.pub.util.RxBus;
import com.tbruyelle.rxpermissions.RxPermissions;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;
import rx.Subscriber;


/**
 * [Description]
 * <p>
 * [How to use]
 * <p>
 * [Tips]
 *
 * @author Created by Zhimin.Huang on 2016/10/24.
 * @since 1.0.0
 */
public class ApplySuccessActivity extends BaseActivity<ApplySuccessView, ApplySuccessPresenter>
        implements ApplySuccessView, PlatformActionListener {

    public static final String APPLY_SUCCESS_BEAN = "applySuccessBean";
    public static final String SHARE_TEXT = "我在调代课系统里向您发送了一个调代课申请，请您确认，谢谢！";
    public static final String SHARE_TITLE = "调代课申请";
    private RadioButton mCallPhone;
    private RadioButton mSendMessage;
    private RadioButton mWeChat;
    private RadioButton mQQ;
    private ApplySuccessBean mApplySuccessBean;
    private TextView mNameAndPhone;
    public LinearLayout mShare;

    @NonNull
    @Override
    public ApplySuccessPresenter createPresenter() {
        return new ApplySuccessPresenter(mActivity);
    }

    public Toolbar mToolbar;
    public ToolBarManager mToolBarManager;

    public static void start(Context context, ApplySuccessBean applySuccessBean) {
        final Intent lIntent = new Intent(context, ApplySuccessActivity.class);
        Bundle lBundle = new Bundle();
        lBundle.putSerializable(APPLY_SUCCESS_BEAN, applySuccessBean);
        lIntent.putExtras(lBundle);
        if (!(context instanceof Activity)) {
            lIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(lIntent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_success);
        initData();
        initView();
        loadData();
    }


    //用于页面间数据接收
    private void initData() {
        mApplySuccessBean = (ApplySuccessBean) getIntent().getExtras().getSerializable(APPLY_SUCCESS_BEAN);
    }


    //初始化view
    private void initView() {
        findViews();
        initToolbar();
        setOnInteractListener();


    }

    private void loadData() {

    }


    private void initToolbar() {
        mToolBarManager.init();
        mToolbar.setNavigationOnClickListener(v -> onBackPressed());

        mToolBarManager.setTitle(getString(R.string.substitute_and_adjust_apply))
                .showBack(false)
                .showLeftButton(true)
                .setLeftText(getString(R.string.turn_off))
                .clickLeftButton(v -> {
                    ScheduleSearchActivity.start(mActivity);
                    ScheduleSearchActivity.isFinish = true;
                    RxBus.getDefault().post(new Constant.DeleteSuccess());
                    AdjustMvpMainActivityV2.start(mActivity);
                    finish();
                });


    }

    //事件处理
    private void setOnInteractListener() {

        mCallPhone.setOnClickListener(v -> {
            if (mApplySuccessBean != null) {
                final String lPhone = mApplySuccessBean.getPhone() == null ? null : mApplySuccessBean.getPhone().trim();
                if (!lPhone.isEmpty()) {
                    RxPermissions.getInstance(mActivity).request(Manifest.permission.CALL_PHONE)
                            .subscribe(new Subscriber<Boolean>() {
                                @Override
                                public void onCompleted() {

                                }

                                @Override
                                public void onError(Throwable e) {

                                }

                                @Override
                                public void onNext(Boolean aBoolean) {
                                    if (aBoolean) {
//                                    PhoneUtils.call(mActivity, mApplySuccessBean.getPhone());
                                        Uri uri = Uri.parse("tel:" + lPhone);

                                        Intent intent = new Intent(Intent.ACTION_DIAL, uri);

                                        startActivity(intent);
                                    } else {
                                        showInfo(getString(R.string.get_phone_permission_first));
                                    }
                                }
                            });
                }
            }
        });

        mSendMessage.setOnClickListener(v -> {
            if (mApplySuccessBean != null) {
                final String lPhone = mApplySuccessBean.getPhone() == null ? null : mApplySuccessBean.getPhone().trim();
                if(!lPhone.isEmpty()) {
                    PhoneUtils.sendSms(mApplySuccessBean.getPhone(), SHARE_TEXT);
                }
            }
        });

        mWeChat.setOnClickListener(v -> {
            presenter.showPromoptDialog("weChat", mActivity);
//            shareWeChat();
        });

        mQQ.setOnClickListener(v -> {
            presenter.showPromoptDialog("qq", mActivity);
//            shareQQ();
        });


        if (mApplySuccessBean != null) {

            mShare.setVisibility(mApplySuccessBean.getType().equals(Constant.COURSE_S) ? View.GONE : View.VISIBLE);

            String lPhone = mApplySuccessBean.getPhone() == null ? null : mApplySuccessBean.getPhone().trim();
            if (lPhone != null && !lPhone.isEmpty() && mApplySuccessBean.getUserName() != null) {
                mNameAndPhone.setText(mApplySuccessBean.getUserName() + "，" + lPhone);
            } else {
                mNameAndPhone.setText(R.string.has_not_phone_number);
                Drawable lDrawable = ContextCompat.getDrawable(mActivity, R.drawable.mailbox);
                lDrawable.setBounds(0, 0, lDrawable.getMinimumWidth(), lDrawable.getMinimumHeight());
                mSendMessage.setCompoundDrawables(null, lDrawable, null, null);

                Drawable lDrawable1 = ContextCompat.getDrawable(mActivity, R.drawable.phone_gray);
                lDrawable1.setBounds(0, 0, lDrawable1.getMinimumWidth(), lDrawable1.getMinimumHeight());
                mCallPhone.setCompoundDrawables(null, lDrawable1, null, null);
            }
        }
    }

    private void shareQQ() {
        Platform.ShareParams sp = new Platform.ShareParams();
        sp.setTitle(SHARE_TITLE);
        sp.setText(SHARE_TEXT);

        Platform qq = ShareSDK.getPlatform(QQ.NAME);
        qq.setPlatformActionListener(ApplySuccessActivity.this); // 设置分享事件回调
        // 执行分享
        qq.share(sp);
    }

    private void shareWeChat() {
        Platform.ShareParams sp = new Platform.ShareParams();
        sp.setShareType(Platform.SHARE_TEXT);
        sp.setTitle(SHARE_TITLE);
        sp.setText(SHARE_TEXT);
        Platform weChat = ShareSDK.getPlatform(Wechat.NAME);
        weChat.setPlatformActionListener(ApplySuccessActivity.this);
        weChat.share(sp);
    }

    Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    Toast.makeText(mActivity, R.string.qq_share_success, Toast.LENGTH_LONG).show();
                    break;
                case 2:
                    Toast.makeText(mActivity, R.string.cancel_share, Toast.LENGTH_LONG).show();
                    break;
                case 3:
                    Toast.makeText(mActivity, getString(R.string.share_fail) + msg.obj, Toast.LENGTH_LONG).show();
                    break;
                case 4:
                    Toast.makeText(mActivity, R.string.wechat_share_success, Toast.LENGTH_LONG).show();
                    break;
                default:
                    break;
            }
        }

    };

    //绑定控件
    private void findViews() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mCallPhone = (RadioButton) findViewById(R.id.call_phone);
        mSendMessage = (RadioButton) findViewById(R.id.send_message);
        mWeChat = (RadioButton) findViewById(R.id.weChat);
        mQQ = (RadioButton) findViewById(R.id.qq);
        mNameAndPhone = (TextView) findViewById(R.id.notify_name_tv);
        mToolBarManager = new ToolBarManager(mToolbar, this);
        mShare = (LinearLayout) findViewById(R.id.share);

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mToolBarManager.destroy();
    }

    @Override
    public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
        if (platform.getName().equals(QQ.NAME)) {// 判断成功的平台是不是QQ
            handler.sendEmptyMessage(1);
        } else if (platform.getName().equals(Wechat.NAME)) {
            handler.sendEmptyMessage(4);

        }
    }

    @Override
    public void onError(Platform platform, int i, Throwable throwable) {
        throwable.printStackTrace();
        String expName = throwable.getClass().getSimpleName();
        //判断有没有安装客户端
        if ("WechatClientNotExistException".equals(expName) || "WechatTimelineNotSupportedException".equals(expName) || "WechatFavoriteNotSupportedException".equals(expName)) {
            showInfo("没有安装客端");
        } else {
            Message msg = new Message();
            msg.what = 3;
            msg.obj = throwable.getMessage();
            handler.sendMessage(msg);
        }
    }

    @Override
    public void onCancel(Platform platform, int i) {
        handler.sendEmptyMessage(2);
    }

    @Override
    public void confirmNotify(String weChat) {
        if (weChat.equals("qq")) {
            shareQQ();
        } else {
            shareWeChat();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        ScheduleSearchActivity.start(mActivity);
        ScheduleSearchActivity.isFinish = true;
        AdjustMvpMainActivityV2.start(mActivity);
        RxBus.getDefault().post(new Constant.DeleteSuccess());
        finish();
    }
}
