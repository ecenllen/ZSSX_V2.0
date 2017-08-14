package com.gta.zssx.fun.adjustCourse.view.base;

import android.content.Context;
import android.text.TextUtils;

import com.blankj.utilcode.util.SPUtils;
import com.google.gson.Gson;
import com.gta.utils.module.BaseModule;
import com.gta.zssx.AppConfiguration;
import com.gta.zssx.fun.adjustCourse.model.bean.NoticeBean;
import com.gta.zssx.fun.adjustCourse.view.page.ScheduleSearchActivity;
import com.gta.zssx.fun.adjustCourse.view.page.v2.AdjustMvpMainActivityV2;
import com.gta.zssx.fun.adjustCourse.view.page.v2.CourseApplyActivityV2;
import com.gta.zssx.fun.adjustCourse.view.page.v2.DetailActivity;

import org.w3c.dom.Text;

import rx.Subscription;

import static com.gta.zssx.fun.adjustCourse.model.utils.NoticeManager.NOTICE_JSON;
import static com.gta.zssx.fun.adjustCourse.model.utils.NoticeManager.NOTICE_SP_NAME;

/**
 * [Description]
 * <p>
 * [How to use]
 * <p>
 * [Tips]
 *
 * @author Created by Zhimin.Huang on 2016/8/22.
 * @since 1.0.0
 */
public class AdjustCourse implements BaseModule {

    public static final String RED_POINT_SP = "red_point_sp";
    public static final String HAS_MESSAGE = "hasMessage";
    private Context mContext;
    private static String mServerAddress;
    private static String sCourseDialy = "AdjustCourse";
    private Subscription mSubscribe;
    public NoticeBean mNoticeBean;


    @Override
    public void destroy() {

    }

    public static AdjustCourse getInstance() {
        return singleton.S_COURSE_DAILY;
    }


    private static class singleton {
        static final AdjustCourse S_COURSE_DAILY = new AdjustCourse();
    }

    public void init(Context context, String serverAddress) {
        mContext = context;
        mServerAddress = serverAddress;
    }

    public String getmServerAddress() {
        if(TextUtils.isEmpty(mServerAddress))
            mServerAddress = AppConfiguration.getInstance ().getServerAddress ().getAdjustCourseUrl();
        return mServerAddress;
    }

    public void goToActivity(String className) {
        if (className.equals(CourseApplyActivityV2.class.getSimpleName())) {
            CourseApplyActivityV2.start(mContext, 0, null, null, 1);
        } else if (className.equals(ScheduleSearchActivity.class.getSimpleName())) {
            ScheduleSearchActivity.start(mContext);
        }
    }

    @Override
    public void displayActivity() {
//        AdjustMainActivity.start(mContext,0);
        AdjustMvpMainActivityV2.start(mContext);
    }

    //    获取通知实体
    private NoticeBean getNoticeBean() {
        if (mNoticeBean != null) {
            return mNoticeBean;
        } else {
            Gson lGson = new Gson();
            SPUtils lSPUtils = new SPUtils(NOTICE_SP_NAME);
            String lString = lSPUtils.getString(NOTICE_JSON);
            if (lString != null) {
                mNoticeBean = lGson.fromJson(lString, NoticeBean.class);
            }
            return mNoticeBean;
        }
    }

    public boolean hasAudit() {
        NoticeBean lNoticeBean = getNoticeBean();
        return lNoticeBean.getIsAudit() == Constant.HAS_AUDIT;
    }

    public boolean hasAuditRight() {
        NoticeBean lNoticeBean = getNoticeBean();
        return lNoticeBean.getIsAudit() == Constant.HAS_AUDIT && lNoticeBean.getIsHasRightAudit() == Constant.HAS_AUDIT;
    }

    public void goToDetail(int msg, int transferType) {
        DetailActivity.start(mContext, transferType, msg, 0);
    }

    public void getClassList() {


    }


    @Override
    public void displayInFragment(int fragmentContainerResID) {

    }


}
