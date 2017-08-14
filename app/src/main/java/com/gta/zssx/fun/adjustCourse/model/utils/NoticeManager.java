package com.gta.zssx.fun.adjustCourse.model.utils;

import com.blankj.utilcode.util.SPUtils;
import com.google.gson.Gson;
import com.gta.zssx.AppConfiguration;
import com.gta.zssx.fun.adjustCourse.model.AdjustDataManager;
import com.gta.zssx.fun.adjustCourse.model.bean.NoticeBean;
import com.gta.zssx.fun.personalcenter.model.bean.UserBean;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;
import rx.Subscription;

/**
 * [Description]
 * <p>
 * [How to use]
 * <p>
 * [Tips]
 *
 * @author Created by Zhimin.Huang on 2017/4/6.
 * @since 1.0.0
 */
public final class NoticeManager {

    public static final String NOTICE_SP_NAME = "NoticeSpName";
    public static final String NOTICE_JSON = "notice_json";
    private static NoticeManager INSTANCE;

    private NoticeManager() {

    }


    public static synchronized NoticeManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = SINGLETON.sNoticeManager;
        }

        return INSTANCE;
    }

    private static class SINGLETON {

        static NoticeManager sNoticeManager = new NoticeManager();
    }


    private final List<NoticeNotify> mNotifies = new ArrayList<>();

    private NoticeBean mNotice;

    /**
     * 添加消息变化监听
     *
     * @param noticeNotify NoticeNotify
     */
    public static void bindNotify(NoticeNotify noticeNotify) {
        getInstance().mNotifies.add(noticeNotify);
        getInstance().check(noticeNotify);
    }

    /***
     * 取消消息变化监听
     *
     * @param noticeNotify NoticeNotify
     */
    public static void unBindNotify(NoticeNotify noticeNotify) {
        getInstance().mNotifies.remove(noticeNotify);
    }

    /**
     * 绑定消息变化接口时进行一次检查，直接通知一次最新状态
     *
     * @param noticeNotify NoticeNotify
     */
    private void check(NoticeNotify noticeNotify) {
        if (mNotice != null)
            noticeNotify.onNoticeArrived(mNotice);
    }

    /**
     * 当消息改变时通知
     *
     * @param bean NoticeBean
     */
    public void onNoticeChanged(NoticeBean bean) {
        mNotice = bean;
        //  Notify all
        for (NoticeNotify notify : mNotifies) {
            notify.onNoticeArrived(mNotice);
        }
    }

    public void getNotice() {
        UserBean lUserBean = null;
        try {
            lUserBean = AppConfiguration.getInstance().getUserBean();
        } catch (Exception e) {
            e.printStackTrace();
        }
        assert lUserBean != null;
        Subscription lSubscribe = AdjustDataManager.getNotice(lUserBean.getUserId())
                .subscribe(new Subscriber<NoticeBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(NoticeBean noticeBean) {
                        SPUtils lSPUtils = new SPUtils(NOTICE_SP_NAME);
                        Gson lGson = new Gson();
                        String lToJson = lGson.toJson(noticeBean);
                        lSPUtils.put(NOTICE_JSON, lToJson);
                        onNoticeChanged(noticeBean);
                    }
                });
    }

    /**
     * 消息变化时通知接口
     */
    public interface NoticeNotify {
        void onNoticeArrived(NoticeBean bean);
    }

}
