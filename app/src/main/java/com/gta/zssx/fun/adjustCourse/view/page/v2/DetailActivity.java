package com.gta.zssx.fun.adjustCourse.view.page.v2;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
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
import com.gta.zssx.fun.adjustCourse.deprecated.presenter.ApplyDetailPresenter;
import com.gta.zssx.fun.adjustCourse.deprecated.view.ApplyDetailView;
import com.gta.zssx.fun.adjustCourse.model.bean.ApplyDetailBean;
import com.gta.zssx.fun.adjustCourse.model.bean.CurrentSemesterBean;
import com.gta.zssx.fun.adjustCourse.model.bean.SearchArguments;
import com.gta.zssx.fun.adjustCourse.view.base.Constant;
import com.gta.zssx.fun.adjustCourse.view.page.ApplySuccessActivity;
import com.gta.zssx.fun.adjustCourse.view.page.CourseScheduleActivity;
import com.gta.zssx.pub.base.BaseMvpActivity;
import com.gta.zssx.pub.util.RxBus;
import com.gta.zssx.pub.widget.DisableEmojiEditText;
import com.tbruyelle.rxpermissions.RxPermissions;

import org.joda.time.DateTime;

import java.lang.ref.WeakReference;
import java.util.HashMap;

import butterknife.Bind;
import butterknife.OnClick;
import butterknife.OnTextChanged;
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
 * @author Created by Zhimin.Huang on 2017/3/28.
 * @since 1.0.0
 */
public class DetailActivity extends BaseMvpActivity<ApplyDetailView, ApplyDetailPresenter>
        implements ApplyDetailView, PlatformActionListener {

    public static final String DETAIL_TYPE = "type";
    public static final String DETAIL_ID = "id";
    public static final String FROM_PAGE = "from_page";
    public int mType;
    public int mDetailId;
    private ApplyDetailBean mApplyDetailBean;


    @Bind(R.id.apply_teacher_name_tv)
    TextView mApplyNameTv;
    @Bind(R.id.apply_date_tv)
    TextView mApplyDateTv;
    @Bind(R.id.apply_course_section_tv)
    TextView mApplyCourseTv;
    @Bind(R.id.adjust_teacher_name_tv)
    TextView mAdjustNameTv;
    @Bind(R.id.adjust_date_tv)
    TextView mAdjustDateTv;
    @Bind(R.id.leave_count_tv)
    TextView mCountTv;
    @Bind(R.id.adjust_course_section_tv)
    TextView mAdjustCourseTv;
    @Bind(R.id.adjust_image)
    ImageView mAdjustImage;
    @Bind(R.id.adjust_course_schedule_layout)
    LinearLayout mAdjustCourseLayout;
    @Bind(R.id.remark_layout)
    LinearLayout mRemarkLayout;
    @Bind(R.id.share_layout)
    LinearLayout mShareLayout;
    @Bind(R.id.remark_et)
    DisableEmojiEditText mRemark;
    @Bind(R.id.week_layout)
    LinearLayout mWeekLayout;
    @Bind(R.id.week_tv)
    TextView mWeekTv;
    @Bind(R.id.course_type_tv)
    TextView mCourseType;
    @Bind(R.id.remark_tv)
    TextView mRemarkTv;
    @Bind(R.id.notify_name_tv)
    TextView mNameAndPhone;
    @Bind(R.id.confirm_btn)
    Button mConfirm;

    @Bind(R.id.call_phone)
    RadioButton mCallPhone;
    @Bind(R.id.send_message)
    RadioButton mSendMessage;

    //    确认情况和审核情况最外层布局
    @Bind(R.id.confirm_layout)
    LinearLayout mConfirmLayout;
    @Bind(R.id.audit_layout)
    LinearLayout mAuditLayout;

    //    确认和审核详情显示局部
    @Bind(R.id.confirm_detail_layout)
    LinearLayout mConfirmDetailLayout;
    @Bind(R.id.audit_detail_layout)
    LinearLayout mAuditDetailLayout;

    //    填写确认和审核意见的布局
    @Bind(R.id.confirm_idea_layout)
    LinearLayout mConfirmIdeaLayout;
    @Bind(R.id.audit_idea_layout)
    LinearLayout mAuditIdeaLayout;

    //    填写确认信息
    @Bind(R.id.confirm_idea_edit)
    DisableEmojiEditText mConfirmEdit;
    //    填写确认信息
    @Bind(R.id.audit_idea_edit)
    DisableEmojiEditText mAuditEdit;

    @Bind(R.id.confirm_count_tv)
    TextView mConfirmCountTv;
    @Bind(R.id.audit_count_tv)
    TextView mAuditCountTv;


    @Bind(R.id.confirm_status_tv)
    TextView mConfirmStatusTv;
    @Bind(R.id.confirm_idea_tv)
    TextView mConfirmIdeaTv;
    @Bind(R.id.confirm_time_tv)
    TextView mConfirmTimeTv;
    @Bind(R.id.confirm_name_tv)
    TextView mConfirmNameTv;
    @Bind(R.id.audit_status_tv)
    TextView mAuditStatusTv;
    @Bind(R.id.audit_idea_tv)
    TextView mAuditIdeaTv;
    @Bind(R.id.audit_time_tv)
    TextView mAuditTimeTv;
    @Bind(R.id.audit_name_tv)
    TextView mAuditNameTv;
    public StringBuilder mStringBuilder;
    private String[] mApplyDays;
    private String[] mAdjustDays;
    private ShareHandler mShareHandler;


    /**
     * @param context  上下文
     * @param type     类型：0为确认，1为审核，2为查看
     * @param id       申请id
     * @param fromPage 从那个fragment跳入
     */
    public static void start(Context context, int type, int id, int fromPage) {
        Intent starter = new Intent(context, DetailActivity.class);
        starter.putExtra(DETAIL_TYPE, type);
        starter.putExtra(DETAIL_ID, id);
        starter.putExtra(FROM_PAGE, fromPage);
        if (!(context instanceof Activity)) {
            starter.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(starter);
    }

    @NonNull
    @Override
    public ApplyDetailPresenter createPresenter() {
        return new ApplyDetailPresenter();
    }


    @Override
    public int getLayoutId() {
        return R.layout.activity_apply_detail;
    }

    @Override
    protected void initView() {
        mShareLayout.setVisibility(View.GONE);

        mRemark.setVisibility(View.GONE);
        mRemarkTv.setVisibility(View.VISIBLE);
        mCountTv.setVisibility(View.GONE);

        mAuditEdit.setMaxLength(100);
        mConfirmEdit.setMaxLength(100);

        mShareHandler = new ShareHandler(mActivity);

    }

    @OnTextChanged(R.id.confirm_idea_edit)
    public void onConfirmTextChanged(CharSequence text) {
        int lLength = text.toString().length();
        if (mStringBuilder == null) {
            mStringBuilder = new StringBuilder();
        }
        mStringBuilder.delete(0, mStringBuilder.length());
        mStringBuilder.append("确认意见（").append(100 - lLength).append("字）");
        mConfirmCountTv.setText(mStringBuilder);
    }

    @OnTextChanged(R.id.audit_idea_edit)
    public void onAuditTextChanged(CharSequence text) {
        int lLength = text.toString().length();
        if (mStringBuilder == null) {
            mStringBuilder = new StringBuilder();
        }
        mStringBuilder.delete(0, mStringBuilder.length());
        mStringBuilder.append("审核意见（").append(100 - lLength).append("字）");
        mAuditCountTv.setText(mStringBuilder);
    }

    @Override
    protected void requestData() {
        presenter.getApplyDetail(mDetailId, mType);
    }

    @Override
    protected void initData() {
        super.initData();
        mType = getIntent().getIntExtra(DETAIL_TYPE, 2);
        mDetailId = getIntent().getIntExtra(DETAIL_ID, 0);
    }

    @OnClick({R.id.adjust_course_schedule_layout, R.id.apply_course_layout, R.id.call_phone,
            R.id.send_message, R.id.weChat, R.id.qq, R.id.confirm_btn})
    public void onClick(View view) {
        switch (view.getId()) {
//            被调代课人的课表
            case R.id.adjust_course_schedule_layout:
                if (mApplyDetailBean != null) {
                    if (mApplyDetailBean.getApplyType().equals(Constant.APPLY_TYPE_H)) {
                        goToApplySchedule();
                    } else {
                        if (mApplyDetailBean == null)
                            return;
                        goToAdjustSchedule();
                    }
                }

                break;
//            申请人的课表
            case R.id.apply_course_layout:
                if (mApplyDetailBean == null)
                    return;
                goToApplySchedule();
                break;

            case R.id.call_phone:
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
                                        Uri uri = Uri.parse("tel:" + lAdjustPhone);

                                        Intent intent = new Intent(Intent.ACTION_DIAL, uri);

                                        startActivity(intent);
                                    } else {
                                        showInfo(getString(R.string.get_phone_permission_first));
                                    }
                                }
                            });

                } else {
                    com.gta.utils.resource.Toast.Long(mActivity, "对方未填写联系电话");
                }
                break;
            case R.id.send_message:
                if (mApplyDetailBean != null && !mApplyDetailBean.getAdjustPhone().trim().isEmpty()) {

                    PhoneUtils.sendSms(mApplyDetailBean.getAdjustPhone(), ApplySuccessActivity.SHARE_TEXT);
                } else {
                    com.gta.utils.resource.Toast.Long(mActivity, "对方未填写联系电话");
                }
                break;
            case R.id.weChat:
                presenter.showPromoptDialog("weChat", mActivity);
                break;
            case R.id.qq:
                presenter.showPromoptDialog("qq", mActivity);
                break;
            case R.id.confirm_btn:
//                删除操作
                if (mType == Constant.DETAIL_TYPE_CHECK) {
                    if (!("0").equals(mApplyDetailBean.getConfirmState()) && !("1").equals(mApplyDetailBean.getAuditState())) {
                        presenter.showDeleteDialog(mApplyDetailBean.getTransferApplyId(), mActivity);
                    }
                }
//                确认操作
                if (mType == Constant.DETAIL_TYPE_CONFIRM) {
                    presenter.showConfirmAndAuditDialog(mApplyDetailBean, mConfirmEdit.getText().toString(), mActivity, Constant.DETAIL_TYPE_CONFIRM);
                }
//                审核操作
                if (mType == Constant.DETAIL_TYPE_AUDIT) {
                    presenter.showConfirmAndAuditDialog(mApplyDetailBean, mAuditEdit.getText().toString(), mActivity, Constant.DETAIL_TYPE_AUDIT);
                }
                break;
            default:

        }
    }

    private void goToAdjustSchedule() {
        SearchArguments lSearchArguments = new SearchArguments();
        lSearchArguments.setFlag(CourseScheduleActivity.TEACHER_SCHEDULE);
        lSearchArguments.setSemesterId(mApplyDetailBean.getSemester() + "");
        lSearchArguments.setTeacherUUId(mApplyDetailBean.getAdjustTeacherBid());
        lSearchArguments.setFromSearch(false);
        lSearchArguments.setDays(mAdjustDays);
        lSearchArguments.setScheduleName(mApplyDetailBean.getAdjustTeacherName());
        lSearchArguments.setDate(presenter.mAdjustDateStr);
        int lWeekIndex = TimeUtils.getWeekIndex(new DateTime(presenter.mAdjustDateStr).toDate());
        lSearchArguments.setWeekIndex(lWeekIndex - 2);
        CourseScheduleActivity.start(mActivity, CourseScheduleActivity.LOOK, Constant.COURSE_N, lSearchArguments);
    }

    private void goToApplySchedule() {
        SearchArguments lSearchArguments = new SearchArguments();
        lSearchArguments.setFlag(CourseScheduleActivity.TEACHER_SCHEDULE);
        lSearchArguments.setSemesterId(mApplyDetailBean.getSemester() + "");
        lSearchArguments.setTeacherUUId(mApplyDetailBean.getApplyTeacherBid());
        lSearchArguments.setFromSearch(false);
        lSearchArguments.setScheduleName(mApplyDetailBean.getApplyTeacherName());
        lSearchArguments.setDays(mApplyDays);
        lSearchArguments.setDate(presenter.mApplyDateStr);
        int lWeekIndex = TimeUtils.getWeekIndex(new DateTime(presenter.mApplyDateStr).toDate());
        lSearchArguments.setWeekIndex(lWeekIndex - 2);
        CourseScheduleActivity.start(mActivity, CourseScheduleActivity.LOOK, Constant.COURSE_N, lSearchArguments);
    }

    @Override
    public void showResult(ApplyDetailBean applyDetailBean) {
        if (applyDetailBean == null) {
            showInfo("请求出错");
            return;
        }
        mApplyDetailBean = applyDetailBean;

        mToolBarManager.setTitle(presenter.getTitle());

        mApplyNameTv.setText(applyDetailBean.getApplyTeacherName());
        //申请日期
        mApplyDateTv.setText(presenter.getApplyDate());
        //申请的课程节次班级信息
        mApplyCourseTv.setText(presenter.getSectionString(applyDetailBean.getPolysyllabicWordStr(), applyDetailBean.getApplyCourseName(), applyDetailBean.getApplyClassName()));
        //申请类型
        mCourseType.setText(presenter.getCourseType());
        //被调课教师
        mAdjustNameTv.setText(presenter.getAdjustName());
        //代课时不显示调课信息
        mAdjustCourseLayout.setVisibility(applyDetailBean.getApplyType().equals(Constant.APPLY_TYPE_D) ? View.GONE : View.VISIBLE);
        //备注信息
        String lApplyCause = applyDetailBean.getApplyCause();
        if (lApplyCause != null && !lApplyCause.isEmpty() && !lApplyCause.equals("无")) {
            mRemarkTv.setText(lApplyCause);
        } else {
            mRemarkTv.setText(R.string.no_remarks);
            mRemarkTv.setTextColor(ContextCompat.getColor(mActivity, R.color.gray_999999));
        }
        //被调课节次
        mAdjustCourseTv.setText(presenter.getAdjustSection());

        mAdjustDateTv.setText(presenter.getAdjustDate());

        mApplyDays = presenter.getWeekDay(new DateTime(presenter.mApplyDateStr).toDate());

        mAdjustDays = presenter.getWeekDay(new DateTime(presenter.mAdjustDateStr).toDate());


        mAdjustImage.setImageResource(mApplyDetailBean.getApplyType().equals(Constant.APPLY_TYPE_D) ? R.drawable.character_exchange : R.drawable.exchange);


        if (mType == Constant.DETAIL_TYPE_CHECK) {
            //已审核
            if (mApplyDetailBean.getIsAudit() == Constant.HAS_AUDIT) {
                if (("1").equals(applyDetailBean.getAuditState())) {
                    showAuditDetail();
                }
            }

            //已确定
            if (("0").equals(applyDetailBean.getConfirmState())) {
                showConfirmDetail();
            }


            //未结束
            if (!("0").equals(applyDetailBean.getConfirmState()) && !("1").equals(applyDetailBean.getAuditState())) {
                showShareLayout();
                mConfirm.setVisibility(View.VISIBLE);
                mConfirm.setText(R.string.delete);
            } else {
                mConfirm.setVisibility(View.GONE);
                mShareLayout.setVisibility(View.GONE);
            }

        }

        if (mType == Constant.DETAIL_TYPE_CONFIRM) {
            mConfirmLayout.setVisibility(View.VISIBLE);
            mConfirmIdeaLayout.setVisibility(View.VISIBLE);
            mConfirmDetailLayout.setVisibility(View.GONE);
            mConfirm.setVisibility(View.VISIBLE);
            mConfirm.setText(R.string.confirm);

            //未审核
            if (mApplyDetailBean.getIsAudit() == Constant.HAS_AUDIT) {
                if (("1").equals(applyDetailBean.getAuditState())) {
                    showAuditDetail();
                }
            }
        }

        if (mType == Constant.DETAIL_TYPE_AUDIT) {
            mAuditLayout.setVisibility(View.VISIBLE);
            mAuditIdeaLayout.setVisibility(View.VISIBLE);
            mAuditDetailLayout.setVisibility(View.GONE);
            mConfirm.setVisibility(View.VISIBLE);
            mConfirm.setText(R.string.audit);
            //已确认
            if (("0").equals(applyDetailBean.getConfirmState())) {
                showConfirmDetail();
            }

        }

    }

    @Override
    public void messageHasDelete(int code) {
        switch (code) {
            default:
            case 0:
                com.gta.utils.resource.Toast.Long(mActivity, getString(R.string.apply_be_deleted));
                RxBus.getDefault().post(new Constant.DeleteSuccess());
                break;
            case 1:
                com.gta.utils.resource.Toast.Long(mActivity, getString(R.string.confirm_fail_be_deleted));
                RxBus.getDefault().post(new Constant.ConfirmSuccess());
                break;
            case 2:
                com.gta.utils.resource.Toast.Long(mActivity, getString(R.string.audit_fail_be_deleted));
                RxBus.getDefault().post(new Constant.AuditSuccess());
                break;
        }
    }

    private void showShareLayout() {
        mShareLayout.setVisibility(View.VISIBLE);
        if (mApplyDetailBean != null) {
            String lAdjustPhone = mApplyDetailBean.getAdjustPhone();
            if (null != lAdjustPhone && !lAdjustPhone.isEmpty()) {
                mNameAndPhone.setText(mApplyDetailBean.getAdjustTeacherIdName() + " " + lAdjustPhone);
            } else {
                mNameAndPhone.setText(getString(R.string.has_not_phone_number));

                mSendMessage.setCompoundDrawables(null, presenter.getMailDrawable(mActivity), null, null);

                mCallPhone.setCompoundDrawables(null, presenter.getPhoneDrawable(mActivity), null, null);
            }
        }
    }

    private void showConfirmDetail() {
        mConfirmLayout.setVisibility(View.VISIBLE);
        mConfirmDetailLayout.setVisibility(View.VISIBLE);
        mConfirmIdeaLayout.setVisibility(View.GONE);

        mConfirmNameTv.setText(mApplyDetailBean.getConfirmName());
        mConfirmIdeaTv.setText(mApplyDetailBean.getConfirmOpinion() == null ? "无" : mApplyDetailBean.getConfirmOpinion());
        String lConfirmTime = mApplyDetailBean.getConfirmTime();
        String lConfirmDate = TimeUtils.millis2String(Long.parseLong(lConfirmTime), Constant.DATE_TYPE_03);
        mConfirmTimeTv.setText(lConfirmDate);
        mConfirmStatusTv.setText(getString(R.string.has_confirm));
    }

    private void showAuditDetail() {
        mAuditLayout.setVisibility(View.VISIBLE);
        mAuditIdeaLayout.setVisibility(View.GONE);
        mAuditDetailLayout.setVisibility(View.VISIBLE);

        mAuditNameTv.setText(mApplyDetailBean.getAuditorName());
        mAuditIdeaTv.setText(mApplyDetailBean.getAuditOpinion() == null ? "无" : mApplyDetailBean.getAuditOpinion());
        String lAuditTime = mApplyDetailBean.getAuditTime();
        String lAuditDate = TimeUtils.millis2String(Long.parseLong(lAuditTime), Constant.DATE_TYPE_03);
        mAuditTimeTv.setText(lAuditDate);
        mAuditStatusTv.setText(getString(R.string.has_audit));
    }

    @Override
    public void deleteSuccess(String s) {
        showInfo(getString(R.string.delete_success));
        RxBus.getDefault().post(new Constant.DeleteSuccess());
        finish();
    }

    @Override
    public void confirmSuccess(String s) {

        showInfo(mType == Constant.DETAIL_TYPE_CONFIRM ? getString(R.string.confirm_success) : getString(R.string.audit_success));
        if (mType == Constant.DETAIL_TYPE_CONFIRM) {
            RxBus.getDefault().post(new Constant.ConfirmSuccess());
        } else {
            RxBus.getDefault().post(new Constant.AuditSuccess());
        }
        finish();
    }

    @Override
    public void showSemester(CurrentSemesterBean semesterBean) {

    }

    @Override
    public void confirmNotify(String weChat) {
        if (weChat.equals("qq")) {
            shareQQ();
        } else {
            shareWeChat();
        }
    }

    private void shareQQ() {
        Platform.ShareParams sp = new Platform.ShareParams();
        sp.setTitle(ApplySuccessActivity.SHARE_TITLE);
        sp.setText(ApplySuccessActivity.SHARE_TEXT);
        Resources res = getResources();
        Bitmap bmp = BitmapFactory.decodeResource(res, R.drawable.app_icon);
        sp.setImageData(bmp);
        Platform qq = ShareSDK.getPlatform(QQ.NAME);
        qq.setPlatformActionListener(DetailActivity.this); // 设置分享事件回调
        // 执行分享
        qq.share(sp);
    }

    private void shareWeChat() {
        Platform.ShareParams sp = new Platform.ShareParams();
        sp.setShareType(Platform.SHARE_TEXT);
        sp.setTitle(ApplySuccessActivity.SHARE_TITLE);
        sp.setText(ApplySuccessActivity.SHARE_TEXT);
        Platform weChat = ShareSDK.getPlatform(Wechat.NAME);
        weChat.setPlatformActionListener(DetailActivity.this);
        weChat.share(sp);
    }

    @Override
    public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
        if (platform.getName().equals(QQ.NAME)) {// 判断成功的平台是不是QQ
            mShareHandler.sendEmptyMessage(1);
        } else if (platform.getName().equals(Wechat.NAME)) {
            mShareHandler.sendEmptyMessage(4);
        }
    }

    @Override
    public void onError(Platform platform, int i, Throwable throwable) {
        throwable.printStackTrace();
        Message msg = new Message();
        msg.what = 3;
        msg.obj = throwable.getMessage();
        mShareHandler.sendMessage(msg);
    }

    @Override
    public void onCancel(Platform platform, int i) {
        mShareHandler.sendEmptyMessage(2);
    }

    private static class ShareHandler extends Handler {

        private WeakReference<Context> mWeakReference;

        ShareHandler(Context context) {
            mWeakReference = new WeakReference<>(context);
        }

        @Override
        public void handleMessage(Message msg) {
            Context context = mWeakReference.get();
            if (context == null)
                return;
            switch (msg.what) {
                case 1:
                    Toast.makeText(context, context.getString(R.string.qq_share_success), Toast.LENGTH_LONG).show();
                    break;
                case 2:
                    Toast.makeText(context, context.getString(R.string.cancel_share), Toast.LENGTH_LONG).show();
                    break;
                case 3:
                    Toast.makeText(context, context.getString(R.string.share_fail) + msg.obj, Toast.LENGTH_LONG).show();
                    break;
                case 4:
                    Toast.makeText(context, context.getString(R.string.wechat_share_success), Toast.LENGTH_LONG).show();
                    break;
                default:
                    break;
            }
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mShareHandler.removeCallbacksAndMessages(null);
    }
}
