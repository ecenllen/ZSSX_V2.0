package com.gta.zssx.fun.adjustCourse.deprecated.presenter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;

import com.blankj.utilcode.util.TimeUtils;
import com.gta.utils.mvp.BasePresenter;
import com.gta.zssx.AppConfiguration;
import com.gta.zssx.R;
import com.gta.zssx.fun.adjustCourse.deprecated.view.ApplyDetailView;
import com.gta.zssx.fun.adjustCourse.model.AdjustDataManager;
import com.gta.zssx.fun.adjustCourse.model.bean.ApplyDetailBean;
import com.gta.zssx.fun.adjustCourse.model.bean.ConfirmApplyBean;
import com.gta.zssx.fun.adjustCourse.model.bean.CurrentSemesterBean;
import com.gta.zssx.fun.adjustCourse.view.base.Constant;
import com.gta.zssx.fun.adjustCourse.view.page.ApplySuccessActivity;
import com.gta.zssx.fun.personalcenter.model.bean.UserBean;
import com.gta.zssx.pub.exception.CustomException;

import org.joda.time.DateTime;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

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
public class ApplyDetailPresenter extends BasePresenter<ApplyDetailView> {

    private ApplyDetailBean mApplyDetailBean;
    public String mApplyDateStr;
    public String mAdjustDateStr;

    public ApplyDetailPresenter() {
    }

    //调代课申请详情
    public void getApplyDetail(int applyId, int type) {
        if (getView() == null)
            return;
        getView().showLoadingDialog();
        mCompositeSubscription.add(AdjustDataManager.getApplyDetail(applyId, type)
                .subscribe(new Subscriber<ApplyDetailBean>() {
                    @Override
                    public void onCompleted() {
                        getView().hideDialog();
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (getView() != null) {
                            getView().hideDialog();
                            if (e instanceof CustomException) {
                                CustomException lCustomException = (CustomException) e;
                                if (lCustomException.getCode() == 500) {
                                    getView().messageHasDelete(0);
                                } else {
                                    getView().onErrorHandle(e);
                                }
                            } else {
                                getView().onErrorHandle(e);
                            }
                        }
                    }

                    @Override
                    public void onNext(ApplyDetailBean applyDetailBean) {
                        mApplyDetailBean = applyDetailBean;
                        getView().showResult(applyDetailBean);
                    }
                }));
    }


    //记录详情
    public void getRecordDetail(int applyId) {
        if (getView() == null)
            return;

        getView().showLoadingDialog();
        mCompositeSubscription.add(AdjustDataManager.getRecordDetail(applyId)
                .subscribe(new Subscriber<ApplyDetailBean>() {
                    @Override
                    public void onCompleted() {
                        getView().hideDialog();
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (getView() != null) {
                            getView().hideDialog();
                            getView().onErrorHandle(e);
                        }

                    }

                    @Override
                    public void onNext(ApplyDetailBean applyDetailBean) {
                        getView().showResult(applyDetailBean);
                    }
                }));
    }

    //确认详情
    public void getConfirmDetail(int applyId) {
        if (getView() == null)
            return;

        getView().showLoadingDialog();
        mCompositeSubscription.add(AdjustDataManager.getConfirmDetail(applyId)
                .subscribe(new Subscriber<ApplyDetailBean>() {
                    @Override
                    public void onCompleted() {
                        getView().hideDialog();
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (getView() != null) {
                            getView().hideDialog();
                            getView().onErrorHandle(e);
                        }

                    }

                    @Override
                    public void onNext(ApplyDetailBean applyDetailBean) {
                        getView().showResult(applyDetailBean);
                    }
                }));
    }

    public void deleteApply(int applyId) {
        if (getView() == null)
            return;
        getView().showLoadingDialog();
        mCompositeSubscription.add(AdjustDataManager.deleteApply(applyId + "")
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {
                        getView().hideDialog();
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (getView() != null) {
                            getView().hideDialog();
                            getView().onErrorHandle(e);
                        }
                    }

                    @Override
                    public void onNext(String s) {
                        getView().deleteSuccess(s);
                    }
                }));
    }

    public void confirmApply(ApplyDetailBean applyDetailBean, String remark, int type) {
        if (getView() == null)
            return;
        ConfirmApplyBean lConfirmApplyBean = new ConfirmApplyBean();
        lConfirmApplyBean.setApplyId(applyDetailBean.getTransferApplyId());
        lConfirmApplyBean.setType(type);
        lConfirmApplyBean.setAuditOptions(remark);
        try {
            lConfirmApplyBean.setUserName(AppConfiguration.getInstance().getUserBean().getEchoName());
            lConfirmApplyBean.setUserId(AppConfiguration.getInstance().getUserBean().getUserId());
        } catch (Exception e) {
            e.printStackTrace();
        }
        getView().showLoadingDialog();
        mCompositeSubscription.add(AdjustDataManager.confirmApply(lConfirmApplyBean)
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {
                        getView().hideDialog();
                    }

                    @Override
                    public void onError(Throwable e) {

                        if (getView() != null) {
                            getView().hideDialog();
                            if (e instanceof CustomException) {
                                CustomException lCustomException = (CustomException) e;
                                if (lCustomException.getCode() == 500) {
                                    if (type == Constant.DETAIL_TYPE_CONFIRM) {
                                        getView().messageHasDelete(1);
                                    } else {
                                        getView().messageHasDelete(2);
                                    }
                                } else {
                                    getView().onErrorHandle(e);
                                }
                            }

                        }
                    }

                    @Override
                    public void onNext(String s) {
                        getView().confirmSuccess(s);
                    }
                }));
    }

    public void confirmAndAudit(String applyId, String option) {
        if (getView() == null)
            return;
        UserBean lUserBean = null;
        try {
            lUserBean = AppConfiguration.getInstance().getUserBean();
        } catch (Exception e) {
            e.printStackTrace();
        }
        assert lUserBean != null;
        mCompositeSubscription.add(AdjustDataManager.confirmAndAudit(applyId, lUserBean.getUserId(), lUserBean.getEchoName(), option)
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        if (getView() != null) {
                            getView().hideDialog();
                            getView().onErrorHandle(e);
                        }
                    }

                    @Override
                    public void onNext(String s) {
                        getView().confirmSuccess(s);
                    }
                }));

    }

    public void getSemester() {
        if (getView() == null)
            return;
//        getView().showLoadingDialog();
        mCompositeSubscription.add(AdjustDataManager.getSemester()
                .subscribe(new Subscriber<CurrentSemesterBean>() {
                    @Override
                    public void onCompleted() {
//                        getView().hideDialog();
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (getView() != null) {

//                            getView().hideDialog();
                            getView().onErrorHandle(e);
                        }

                    }

                    @Override
                    public void onNext(CurrentSemesterBean currentSemesterBean) {
                        getView().showSemester(currentSemesterBean);
                    }
                }));
    }

    public String getAdjustSection() {
        StringBuilder lBuilder = new StringBuilder();
        if (mApplyDetailBean.getApplyType().equals(Constant.APPLY_TYPE_T)) {
            return getSectionString(mApplyDetailBean.getPolysyllabicWordStrTow(), mApplyDetailBean.getAdjustCourseName(), mApplyDetailBean.getAdjustClassName());
        } else {
            return lBuilder.append("第").append(mApplyDetailBean.getPolysyllabicWordStrTow()).append("节  无课").toString();
        }
    }

    public String getSectionString(String adjustCoordinate, String adjustCourseName, String adjustClassName) {
        String[] lSplit = adjustCoordinate.split(",");
        StringBuilder lBuilder = new StringBuilder();
        for (int i = 0; i < lSplit.length; i++) {
            lBuilder.append("第").append(lSplit[i]).append("节 ").append(adjustCourseName).append(" (").append(adjustClassName).append(")");
            if (i != lSplit.length - 1) {
                lBuilder.append("\n");
            }
        }
        return lBuilder.toString();
    }

    public void showDeleteDialog(int applyId, Context context) {
        AlertDialog.Builder lBuilder = new AlertDialog.Builder(context);
        lBuilder
                .setMessage("确认删除？")
                .setPositiveButton("确定", (dialog, which) -> {
                    deleteApply(applyId);
                    dialog.dismiss();
                })
                .setNegativeButton("取消", null)
                .show();
    }

    /**
     * @param applyDetailBean bean
     * @param s               审核或确认意见
     * @param context         上下文
     * @param type            0为确认，1为审核
     */
    public void showConfirmAndAuditDialog(ApplyDetailBean applyDetailBean, String s, Context context, int type) {
        AlertDialog.Builder lBuilder = new AlertDialog.Builder(context);
        lBuilder
                .setMessage("确认同意调代课？")
                .setPositiveButton("确定", (dialog, which) -> {
                    confirmApply(applyDetailBean, s, type);
                    dialog.dismiss();
                })
                .setNegativeButton("取消", null)
                .show();
    }

    public void showPromoptDialog(String weChat, Context activity) {
        AlertDialog.Builder lBuilder = new AlertDialog.Builder(activity);
        lBuilder.setTitle("已为您复制了通知内容")
                .setMessage(ApplySuccessActivity.SHARE_TEXT)
                .setPositiveButton("通知", (dialog, which) -> {
                    getView().confirmNotify(weChat);
                    dialog.dismiss();
                })
                .setNegativeButton("取消", null)
                .show();
    }

    public Drawable getPhoneDrawable(Context activity) {
        Drawable lDrawable1 = ContextCompat.getDrawable(activity, R.drawable.phone_gray);
        lDrawable1.setBounds(0, 0, lDrawable1.getMinimumWidth(), lDrawable1.getMinimumHeight());
        return lDrawable1;
    }

    public Drawable getMailDrawable(Context activity) {
        Drawable lDrawable = ContextCompat.getDrawable(activity, R.drawable.mailbox);
        lDrawable.setBounds(0, 0, lDrawable.getMinimumWidth(), lDrawable.getMinimumHeight());
        return lDrawable;
    }


    public String[] getWeekDay(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        //判断要计算的日期是否是周日，如果是则减一天计算周六的，否则会出问题，计算到下一周去了
        int dayWeek = cal.get(Calendar.DAY_OF_WEEK);//获得当前日期是一个星期的第几天
        if (1 == dayWeek) {
            cal.add(Calendar.DAY_OF_MONTH, -1);
        }

        cal.setFirstDayOfWeek(Calendar.MONDAY);//设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一

        int day = cal.get(Calendar.DAY_OF_WEEK);//获得当前日期是一个星期的第几天
        cal.add(Calendar.DATE, cal.getFirstDayOfWeek() - day);//根据日历的规则，给当前日期减去星期几与一个星期第一天的差值
        Date lTime = cal.getTime();
        DateTime lDateTime = new DateTime(lTime);
        String weekDate[] = getweekDays(lDateTime);

        String lBasePath = lDateTime.toString("MM/dd");
//        System.out.println(cal.getFirstDayOfWeek() + "-" + day + "+6=" + (cal.getFirstDayOfWeek() - day + 6));

        cal.add(Calendar.DATE, 6);
        Date lTime1 = cal.getTime();
        DateTime lDateTime1 = new DateTime(lTime1);

        String lBasePath2 = lDateTime1.toString("MM/dd");

        return weekDate;

    }

    //得到一周对应的日期
    private String[] getweekDays(DateTime dateTime) {
        String days[] = new String[6];
        for (int i = 0; i < days.length; i++) {
            days[i] = dateTime.plusDays(i).toString("dd");
        }
        return days;
    }

    public String getApplyDate() {
        long lApplyTime = mApplyDetailBean.getApplyDate();
        mApplyDateStr = TimeUtils.millis2String(lApplyTime, Constant.DATE_TYPE_01);
        Date lDate = TimeUtils.millis2Date(lApplyTime);
        String lWeek = TimeUtils.getWeek(lDate);
        return mApplyDateStr + " " + lWeek;
    }


    public String getCourseType() {
        String lApplyType = mApplyDetailBean.getApplyType();
        return lApplyType.equals(Constant.APPLY_TYPE_D) ? "代课教师" :
                lApplyType.equals(Constant.APPLY_TYPE_T) ? "调课教师" : "调课班级";
    }


    public String getTitle() {

        return mApplyDetailBean.getApplyType().equals(Constant.APPLY_TYPE_D) ? "代课详情" : "调课详情";
    }

    public String getAdjustDate() {
        long lApplyTime = mApplyDetailBean.getAdjustDate();
        mAdjustDateStr = TimeUtils.millis2String(lApplyTime, Constant.DATE_TYPE_01);
        Date lDate = TimeUtils.millis2Date(lApplyTime);
        String lWeek = TimeUtils.getWeek(lDate);
        return mAdjustDateStr + " " + lWeek;
    }

    public String getAdjustName() {
        String lApplyType = mApplyDetailBean.getApplyType();
        return lApplyType.equals(Constant.APPLY_TYPE_H) ? mApplyDetailBean.getAdjustClassName() : mApplyDetailBean.getAdjustTeacherIdName();
    }
}
