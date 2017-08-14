package com.gta.zssx.pub.service;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
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
public class GrayService extends Service {
    String TAG = "GrayService";
    private final static int GRAY_SERVICE_ID = 1001;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (Build.VERSION.SDK_INT < 18) {
            //初始化极光推送
//            JpushManager.getInstance().init(getApplicationContext(), HomePageActivity.class);
//            JpushManager.getInstance().setDebugMode(true);

            //初始化推送模块
//            ZSSXJpushManager.registerJpushManager(this);

            Log.v(TAG, "onStartCommand");
            startForeground(GRAY_SERVICE_ID, new Notification());//API < 18 ，此方法能有效隐藏Notification上的图标
        } else {
            Intent innerIntent = new Intent(this, GrayInnerService.class);
            startService(innerIntent);
            startForeground(GRAY_SERVICE_ID, new Notification());
        }
//        Notification lBuilder = new Notification.Builder(getApplicationContext())
//                .setSmallIcon(com.example.gtalutils.R.drawable.ec_evaluation_ic_add_to)
//                .setContentText("点我")
//                .setWhen(System.currentTimeMillis())
//                .setAutoCancel(true)
//                .build();
//        startForeground(GRAY_SERVICE_ID, lBuilder);
        return super.onStartCommand(intent, flags, startId);
    }
}
