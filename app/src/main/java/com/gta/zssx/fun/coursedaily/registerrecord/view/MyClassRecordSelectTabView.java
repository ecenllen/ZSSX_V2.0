package com.gta.zssx.fun.coursedaily.registerrecord.view;

import com.gta.utils.mvp.BaseView;

/**
 * Created by lan.zheng on 2016/7/1.
 */
public interface MyClassRecordSelectTabView extends BaseView {
    void changeCacheStatus();

    void setServerTime(String date, String week);

    void setServerTimeFailed(boolean isNotNetwork);
}
