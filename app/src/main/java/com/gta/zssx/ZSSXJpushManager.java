package com.gta.zssx;

import android.content.Context;

import com.blankj.utilcode.util.SPUtils;
import com.google.gson.Gson;
import com.gta.utils.thirdParty.jPush.JpushManager;
import com.gta.zssx.fun.adjustCourse.model.bean.AdjustPushBean;
import com.gta.zssx.fun.adjustCourse.model.bean.NoticeBean;
import com.gta.zssx.fun.adjustCourse.model.utils.NoticeManager;
import com.gta.zssx.fun.adjustCourse.view.base.AdjustCourse;
import com.gta.zssx.fun.adjustCourse.view.base.Constant;
import com.gta.zssx.fun.personalcenter.model.bean.UserBean;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

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
public class ZSSXJpushManager {

    private static UserBean sUserBean;
    private static WeakReference<Context> sContext;


    public static void registerJpushManager(final Context context) {

        sContext = new WeakReference<>(context);

        try {
            sUserBean = AppConfiguration.getInstance().getUserBean();
        } catch (Exception e) {
            e.printStackTrace();
        }

        /*一定要有清除监听事件的步骤，不然会导致重复代开很多页面*/
        JpushManager.getInstance().clearReceiveListener();
        unRegisterJpushManager(true);
        if (sUserBean != null) {
            //已登录则设置用户tags
            ArrayList<String> lTags = new ArrayList<>();
//            lTags.add(lUserLoginBean.getTagList());
//            String lString = "";
//            for (int i = 0; i < lTags.size(); i++) {
//                String s = lTags.get(i);
//                if (!TextUtils.isEmpty(s)) {
//                    lString = lString + s;
//                    if (i != lTags.size() - 1) {
//                        lString = lString + ",";
//                    }
//                }
//            }
//            String[] tags = new String[]{mUser.getTagsList()};
//            JpushManager.TagAlia tagAlias = new JpushManager.TagAlia(sUserBean.getUserName(), null);
            JpushManager.getInstance().setAlias(sUserBean.getUserName());
        } else {
//            JpushManager.TagAlia tagAlias = new JpushManager.TagAlia(null, null);
            JpushManager.getInstance().setAlias(null);
            JpushManager.getInstance().stopOrResumeJpush(false);
        }

        JpushManager.getInstance().addReceiveListener(new JpushManager.JpushMessageReceiveListener() {
            @Override
            public void OnReceiveJpushMessage(String actionType, int startWay, String extras, String alert) {
                try {
                    JSONObject json = new JSONObject(extras);
                    int ClassId = (int) json.getLong("sendType");
                    String mMsgId = json.getString("detailId");

                    /*classID：0.纯消息1. 调代课确认*/
                    /*跳转到报修详情页*/
                    switch (ClassId) {
                        case 1:
                            int transferType = json.getInt("transferType");
                            AdjustCourse.getInstance().goToDetail(Integer.valueOf(mMsgId), transferType);
                            break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        //用于自定义消息推送
        JpushManager.getInstance().addOtherActionListener(new JpushManager.JpushMessageReceiveListener() {
            @Override
            public void OnReceiveJpushMessage(String actionType, int startWay, String extras, String alert) {
                try {
                    if (extras != null) {
                        Gson lGson = new Gson();
                        AdjustPushBean lAdjustPushBean = lGson.fromJson(extras, AdjustPushBean.class);
                        SPUtils lSPUtils = new SPUtils( NOTICE_SP_NAME);
                        String lString = lSPUtils.getString(NOTICE_JSON);
                        NoticeBean lBeforeNoticeBean = lGson.fromJson(lString, NoticeBean.class);
                        if (lBeforeNoticeBean == null) {
                            lBeforeNoticeBean = new NoticeBean();
                        }
                        if (lAdjustPushBean.getTransferType() == 3) {
                            lBeforeNoticeBean.setWaitConfirmNumber(lAdjustPushBean.getWaitConfirmNumber());
                        } else if (lAdjustPushBean.getTransferType() == 4) {
                            lBeforeNoticeBean.setWaitAuditNumber(lAdjustPushBean.getWaitAuditNumber());
                            lBeforeNoticeBean.setIsAudit(Constant.HAS_AUDIT);
                            lBeforeNoticeBean.setIsHasRightAudit(Constant.HAS_AUDIT);
                        }
                        lSPUtils.put(NOTICE_JSON, lGson.toJson(lBeforeNoticeBean));
                        NoticeManager.getInstance().onNoticeChanged(lBeforeNoticeBean);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }

    public static void unRegisterJpushManager(boolean enable) {
        JpushManager.getInstance().stopOrResumeJpush(enable);
    }
}
