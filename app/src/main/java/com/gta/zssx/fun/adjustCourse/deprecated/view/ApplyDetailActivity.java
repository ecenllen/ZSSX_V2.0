package com.gta.zssx.fun.adjustCourse.deprecated.view;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;


import com.blankj.utilcode.util.PhoneUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.gta.zssx.R;
import com.gta.zssx.fun.adjustCourse.model.bean.ApplyBean;
import com.gta.zssx.fun.adjustCourse.model.bean.ApplyDetailBean;
import com.gta.zssx.fun.adjustCourse.model.bean.CurrentSemesterBean;
import com.gta.zssx.fun.adjustCourse.model.bean.SearchArguments;
import com.gta.zssx.fun.adjustCourse.deprecated.presenter.ApplyDetailPresenter;
import com.gta.zssx.fun.adjustCourse.view.base.Constant;
import com.gta.zssx.fun.adjustCourse.view.page.ApplySuccessActivity;
import com.gta.zssx.fun.adjustCourse.view.page.CourseScheduleActivity;
import com.gta.zssx.pub.base.BaseActivity;
import com.gta.zssx.pub.manager.ToolBarManager;
import com.gta.zssx.pub.util.RxBus;
import com.gta.zssx.pub.widget.DisableEmojiEditText;
import com.tbruyelle.rxpermissions.RxPermissions;

import org.joda.time.DateTime;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

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
 * @author Created by Zhimin.Huang on 2016/10/28.
 * @since 1.0.0
 */
@Deprecated
public class ApplyDetailActivity extends BaseActivity<ApplyDetailView, ApplyDetailPresenter>
        implements ApplyDetailView, PlatformActionListener {

    public static final String TYPE_T = "T";
    public static final String TYPE = "type";
    private static String APPLY_ID = "applyId";
    public static final int APPLY = 1;
    public static final int RECORD = 2;
    public static final int CONFIRM = 3;
    private int mApplyId;
    private TextView mApplyNameTv;
    private TextView mApplyDateTv;
    private TextView mApplyCourseTv;
    private TextView mAdjustNameTv;
    private TextView mAdjustDateTv;
    private TextView mAdjustCourseTv;
    private DisableEmojiEditText mRemark;

    private RadioButton mCallPhone;
    private RadioButton mSendMessage;
    private RadioButton mWeChat;
    private RadioButton mQQ;

    private LinearLayout mAdjustCourseLayout;
    private TextView mCourseType;
    private int mDetailType;
    private LinearLayout mApplyCourseLayout;
    private LinearLayout mShareLayout;
    private Button mConfirm;
    private ApplyDetailBean mApplyDetailBean;
    private CurrentSemesterBean mSemesterBean;
    private String mApplyDateStr;
    private TextView mNameAndPhone;
    private String mAdjustDateStr;
    private TextView mCountTv;
    private TextView mRemarkTv;
    private ImageView mAdjustImage;
    public LinearLayout mRemarkLayout;
    public String[] mApplyDays;
    public String[] mAdjustDays;

    @NonNull
    @Override
    public ApplyDetailPresenter createPresenter() {
        return new ApplyDetailPresenter();
    }

    public Toolbar mToolbar;
    public ToolBarManager mToolBarManager;

    public static void start(Context context, int applyId, int type) {
        final Intent lIntent = new Intent(context, ApplyDetailActivity.class);
        lIntent.putExtra(APPLY_ID, applyId);
        lIntent.putExtra(TYPE, type);
        if (!(context instanceof Activity)) {
            lIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(lIntent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_detail);
        initData();
        initView();
        loadData();
    }


    //用于页面间数据接收
    private void initData() {
        mApplyId = getIntent().getIntExtra(APPLY_ID, 0);
        mDetailType = getIntent().getIntExtra(TYPE, 0);
    }


    //初始化view
    private void initView() {
        findViews();
        initToolbar();
        setOnInteractListener();


    }

    private void loadData() {
        switch (mDetailType) {
            case APPLY:
                presenter.getApplyDetail(mApplyId, 2);
                break;
            default:
            case RECORD:
                presenter.getRecordDetail(mApplyId);
                break;
            case CONFIRM:
                presenter.getConfirmDetail(mApplyId);
                break;
        }

        presenter.getSemester();

    }


    private void initToolbar() {
        mToolbar.setVisibility(View.VISIBLE);
        mToolBarManager.init();
        mToolbar.setNavigationOnClickListener(v -> onBackPressed());

    }

    //事件处理
    private void setOnInteractListener() {
        mRemark.setMaxLength(100);
        //查看申请人在申请日期的课表
        mApplyCourseLayout.setOnClickListener(v -> {
            if (mSemesterBean == null)
                return;
            if (mApplyDetailBean == null)
                return;
            SearchArguments lSearchArguments = new SearchArguments();
            lSearchArguments.setFlag(CourseScheduleActivity.TEACHER_SCHEDULE);
            lSearchArguments.setSemesterId(mSemesterBean.getSemester());
            lSearchArguments.setTeacherUUId(mApplyDetailBean.getApplyTeacherBid());
            lSearchArguments.setFromSearch(false);
            lSearchArguments.setScheduleName(mApplyDetailBean.getApplyTeacherName());
            lSearchArguments.setDays(mApplyDays);
            lSearchArguments.setDate(mApplyDateStr);
            int lWeekIndex = TimeUtils.getWeekIndex(new DateTime(mApplyDateStr).toDate());
            lSearchArguments.setWeekIndex(lWeekIndex - 2);
            CourseScheduleActivity.start(mActivity, CourseScheduleActivity.LOOK, CourseApplyFragment.COURSE_N, lSearchArguments);
        });

        mShareLayout.setVisibility(mDetailType == RECORD ? View.GONE : View.VISIBLE);

        //查看被调代课人的课表
        mAdjustCourseLayout.setOnClickListener(v -> {
            if (mSemesterBean == null)
                return;
            if (mApplyDetailBean == null)
                return;
            SearchArguments lSearchArguments = new SearchArguments();
            lSearchArguments.setFlag(CourseScheduleActivity.TEACHER_SCHEDULE);
            lSearchArguments.setSemesterId(mSemesterBean.getSemester());
            lSearchArguments.setTeacherUUId(mApplyDetailBean.getAdjustTeacherBid());
            lSearchArguments.setFromSearch(false);
            lSearchArguments.setDays(mAdjustDays);
            lSearchArguments.setScheduleName(mApplyDetailBean.getAdjustTeacherName());
            lSearchArguments.setDate(mAdjustDateStr);
            int lWeekIndex = TimeUtils.getWeekIndex(new DateTime(mAdjustDateStr).toDate());
            lSearchArguments.setWeekIndex(lWeekIndex - 2);
            CourseScheduleActivity.start(mActivity, CourseScheduleActivity.LOOK, CourseApplyFragment.COURSE_N, lSearchArguments);
        });


        mCallPhone.setOnClickListener(v -> {
            if (mApplyDetailBean != null && !mApplyDetailBean.getAdjustPhone().trim().isEmpty()) {
                final String lAdjustPhone = mApplyDetailBean.getAdjustPhone().trim();
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
//                                    PhoneUtils.call(mActivity, mApplyDetailBean.getAdjustPhone());
                                    Uri uri = Uri.parse("tel:" + lAdjustPhone);

                                    Intent intent = new Intent(Intent.ACTION_DIAL, uri);

                                    startActivity(intent);
                                } else {
                                    showInfo("请先到设置开启电话权限");
                                }
                            }
                        });

            }
        });

        mSendMessage.setOnClickListener(v -> {
            String lAdjustPhone = mApplyDetailBean.getAdjustPhone().trim();
            if (mApplyDetailBean != null && !lAdjustPhone.isEmpty()) {

                PhoneUtils.sendSms(mApplyDetailBean.getAdjustPhone(), ApplySuccessActivity.SHARE_TEXT);

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

        mConfirm.setOnClickListener(v -> {
            if (mDetailType == APPLY && mApplyDetailBean != null) {
                if (mApplyDetailBean.getAuditStatus().equals("N")) {
                    presenter.showDeleteDialog(mApplyDetailBean.getApplyId(), mActivity);
                }
            } else if (mDetailType == CONFIRM && mApplyDetailBean != null) {
                if (mApplyDetailBean.getAuditStatus().equals("N")) {
                    presenter.showConfirmAndAuditDialog(mApplyDetailBean, mRemark.getText().toString(), mActivity,1);
                }
            }
        });

        //调代课记录详情不显示备注
        mRemarkLayout.setVisibility(mDetailType == RECORD ? View.GONE : View.VISIBLE);

        //详情页面不用显示剩余多少个字
//        TextWatcher lTextWatcher = new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                int lLength = s.toString().length();
//                mCountTv.setText("剩余" + (100 - lLength) + "字");
//            }
//        };
//
//        mRemark.addTextChangedListener(lTextWatcher);
    }

    private void shareQQ() {
        Platform.ShareParams sp = new Platform.ShareParams();
        sp.setTitle(ApplySuccessActivity.SHARE_TITLE);
        sp.setText(ApplySuccessActivity.SHARE_TEXT);
        Resources res = getResources();
        Bitmap bmp = BitmapFactory.decodeResource(res, R.drawable.app_icon);
        sp.setImageData(bmp);
        Platform qq = ShareSDK.getPlatform(QQ.NAME);
        qq.setPlatformActionListener(ApplyDetailActivity.this); // 设置分享事件回调
        // 执行分享
        qq.share(sp);
    }

    private void shareWeChat() {
        Platform.ShareParams sp = new Platform.ShareParams();
        sp.setShareType(Platform.SHARE_TEXT);
        sp.setTitle(ApplySuccessActivity.SHARE_TITLE);
        sp.setText(ApplySuccessActivity.SHARE_TEXT);
        Platform weChat = ShareSDK.getPlatform(Wechat.NAME);
        weChat.setPlatformActionListener(ApplyDetailActivity.this);
        weChat.share(sp);
    }

    //绑定控件
    private void findViews() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolBarManager = new ToolBarManager(mToolbar, this);

        mApplyNameTv = (TextView) findViewById(R.id.apply_teacher_name_tv);
        mApplyDateTv = (TextView) findViewById(R.id.apply_date_tv);
        mApplyCourseTv = (TextView) findViewById(R.id.apply_course_section_tv);
        mAdjustNameTv = (TextView) findViewById(R.id.adjust_teacher_name_tv);
        mAdjustDateTv = (TextView) findViewById(R.id.adjust_date_tv);
        mCourseType = (TextView) findViewById(R.id.course_type_tv);
        mAdjustCourseTv = (TextView) findViewById(R.id.adjust_course_section_tv);
        mAdjustCourseLayout = (LinearLayout) findViewById(R.id.adjust_course_schedule_layout);
        mApplyCourseLayout = (LinearLayout) findViewById(R.id.apply_course_layout);
        mRemarkLayout = (LinearLayout) findViewById(R.id.remark_layout);
        mShareLayout = (LinearLayout) findViewById(R.id.share_layout);
        mRemark = (DisableEmojiEditText) findViewById(R.id.remark_et);
        mRemarkTv = (TextView) findViewById(R.id.remark_tv);
        mCountTv = (TextView) findViewById(R.id.leave_count_tv);
        mAdjustImage = (ImageView) findViewById(R.id.adjust_image);

        mCallPhone = (RadioButton) findViewById(R.id.call_phone);
        mSendMessage = (RadioButton) findViewById(R.id.send_message);
        mWeChat = (RadioButton) findViewById(R.id.weChat);
        mQQ = (RadioButton) findViewById(R.id.qq);
        mConfirm = (Button) findViewById(R.id.confirm_btn);

        mNameAndPhone = (TextView) findViewById(R.id.notify_name_tv);

        mToolBarManager.setTitle("调课申请");

        mRemark.setVisibility(View.GONE);
        mRemarkTv.setVisibility(View.VISIBLE);
        mCountTv.setVisibility(View.GONE);

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mToolBarManager.destroy();
    }

    @Override
    public void showResult(ApplyDetailBean applyDetailBean) {

        mApplyDetailBean = applyDetailBean;



        String lApplyType = applyDetailBean.getApplyType();
        mAdjustCourseLayout.setVisibility(lApplyType.equals(TYPE_T) ? View.VISIBLE : View.GONE);
        mToolBarManager.setTitle(lApplyType.equals(TYPE_T) ? "调课申请" : "代课申请");
        mCourseType.setText(lApplyType.equals(TYPE_T) ? "调课教师" : "代课教师");

        mApplyNameTv.setText(applyDetailBean.getApplyTeacherName());

        long lApplyTime = applyDetailBean.getApplyTime();
        mApplyDateStr = TimeUtils.millis2String(lApplyTime, Constant.DATE_TYPE_01);
        Date lDate = TimeUtils.millis2Date(lApplyTime);
        String lWeek = TimeUtils.getWeek(lDate);
        //申请的日期信息（日期+星期）
        mApplyDateTv.setText(mApplyDateStr + " " + lWeek);

        mApplyDays = presenter.getWeekDay(new DateTime(mApplyDateStr).toDate());

        String lApplyCoordinate = applyDetailBean.getApplyPolysyllabic();
        String lSection = presenter.getSectionString(lApplyCoordinate, applyDetailBean.getApplyCourseName(), applyDetailBean.getApplyClassName());
        //申请的课程节次信息
        mApplyCourseTv.setText(lSection);


        //备注信息
        String lApplyCause = applyDetailBean.getApplyCause();
        if (lApplyCause != null && !lApplyCause.isEmpty() && !lApplyCause.equals("无")) {
            mRemarkTv.setText(lApplyCause);
        } else {
            mRemarkTv.setText("无备注");
            mRemarkTv.setTextColor(ContextCompat.getColor(mActivity, R.color.gray_999999));
        }

        //被调课教师
        mAdjustNameTv.setText(applyDetailBean.getAdjustTeacherName());

        //被调课节次
        String lAdjustCoordinate = applyDetailBean.getAdjustPolysyllabic();
        String lSection1 = presenter.getSectionString(lAdjustCoordinate, applyDetailBean.getAdjustCourseName(), applyDetailBean.getAdjustClassName());
        mAdjustCourseTv.setText(lSection1);

        //被调课日期信息
        long lAdjustTime = applyDetailBean.getAdjustTime();
        mAdjustDateStr = TimeUtils.millis2String(lAdjustTime, Constant.DATE_TYPE_01);
        Date lDate1 = TimeUtils.millis2Date(lAdjustTime);
        String lWeek1 = TimeUtils.getWeek(lDate1);
        mAdjustDateTv.setText(mAdjustDateStr + " " + lWeek1);

        mAdjustDays = presenter.getWeekDay(new DateTime(mAdjustDateStr).toDate());

        //当为调代课申请界面时并且还未确认的有底部按钮、有分享组件，已确认的无按钮、无分享组件
        if (mDetailType == APPLY) {
            if (applyDetailBean.getAuditStatus().equals("N")) {
                mConfirm.setVisibility(View.VISIBLE);
                mConfirm.setText("删除");
            } else {
                mConfirm.setVisibility(View.GONE);
                mShareLayout.setVisibility(View.GONE);
            }
        }

        mAdjustImage.setImageResource(mApplyDetailBean.getApplyType().equals(TYPE_T) ? R.drawable.exchange : R.drawable.character_exchange);

        //当为调代课确认列表的详情页面时，无分享组件，有底部按钮，分为已确认和确认，确认点击进行课程确认
        if (mDetailType == CONFIRM) {
            mConfirm.setVisibility(View.VISIBLE);
            if (applyDetailBean.getAuditStatus().equals("Y")) {
                mConfirm.setText("已确认");
                mConfirm.setTextColor(ContextCompat.getColor(mActivity, R.color.wirte_ffffff));
                mConfirm.setBackgroundResource(R.drawable.circleconer_button_gray);
                mConfirm.setEnabled(false);
            } else {
                mConfirm.setText("确认");
            }
            mShareLayout.setVisibility(View.GONE);
        }


    }

    @Override
    public void messageHasDelete(int code) {

    }

    @Override
    public void deleteSuccess(String s) {
        RxBus.getDefault().post(new ApplyBean.ListBean());
        finish();
    }

    @Override
    public void confirmSuccess(String s) {
//        ApplySuccessActivity.start(mActivity, null);
        mApplyDetailBean.setAuditStatus("Y");
        mConfirm.setText("已确认");
        mConfirm.setTextColor(ContextCompat.getColor(mActivity, R.color.wirte_ffffff));
        mConfirm.setBackgroundResource(R.drawable.circleconer_button_gray);
        mConfirm.setEnabled(false);
        //确认成功时发送事件通知列表刷新
        RxBus.getDefault().post(mApplyDetailBean);
        showInfo("确认成功");
    }

    @Override
    public void showSemester(CurrentSemesterBean semesterBean) {
        mSemesterBean = semesterBean;
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
        Message msg = new Message();
        msg.what = 3;
        msg.obj = throwable.getMessage();
        handler.sendMessage(msg);
    }

    @Override
    public void onCancel(Platform platform, int i) {
        handler.sendEmptyMessage(2);
    }


    Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    Toast.makeText(mActivity, "QQ分享成功", Toast.LENGTH_LONG).show();
                    break;
                case 2:
                    Toast.makeText(mActivity, "取消分享", Toast.LENGTH_LONG).show();
                    break;
                case 3:
                    Toast.makeText(mActivity, "分享失败" + msg.obj, Toast.LENGTH_LONG).show();
                    break;
                case 4:
                    Toast.makeText(mActivity, "微信分享成功", Toast.LENGTH_LONG).show();
                    break;
                default:
                    break;
            }
        }

    };
}
