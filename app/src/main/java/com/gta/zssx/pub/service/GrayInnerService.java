package com.gta.zssx.pub.service;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.gta.utils.thirdParty.jPush.JpushManager;
import com.gta.zssx.ZSSXJpushManager;
import com.gta.zssx.main.HomePageActivity;

/**
 * [Description]
 * <p>
 * [How to use]
 * <p>
 * [Tips]
 *
 * @author Created by Zhimin.Huang on 2016/7/22.
 * @since 1.0.0
 */
public class GrayInnerService extends Service {
    String TAG = "GrayInnerService";
    private final static int GRAY_SERVICE_ID = 1001;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        startForeground(GRAY_SERVICE_ID, new Notification());
        stopForeground(true);
        stopSelf();
        //初始化极光推送
//        JpushManager.getInstance().init(getApplicationContext(), HomePageActivity.class);
//        JpushManager.getInstance().setDebugMode(true);
//
//        //初始化推送模块
//        ZSSXJpushManager.registerJpushManager(this);

        Log.v(TAG, "onStartCommand");

        return super.onStartCommand(intent, flags, startId);
    }
}
