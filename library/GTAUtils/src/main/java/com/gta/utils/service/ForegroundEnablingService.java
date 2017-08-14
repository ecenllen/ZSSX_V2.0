package com.gta.utils.service;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;

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
public class ForegroundEnablingService extends Service {

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (ForegroundService.instance == null)
            throw new RuntimeException(ForegroundService.class.getSimpleName() + " not running");

        if (Build.VERSION.SDK_INT < 18) {
            startForeground(NOTIFICATION_ID, new Notification());
        } else {
            //Set both services to foreground using the same notification id, resulting in just one notification
            startForeground(ForegroundService.instance);
            startForeground(this);

            //Cancel this service's notification, resulting in zero notifications
            stopForeground(true);

            //Stop this service so we don't waste RAM
            stopSelf();
        }
        return START_NOT_STICKY;
    }

    private static final int NOTIFICATION_ID = 10;

    private static void startForeground(Service service) {
        Notification notification = new Notification.Builder(service).getNotification();
        service.startForeground(NOTIFICATION_ID, notification);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}