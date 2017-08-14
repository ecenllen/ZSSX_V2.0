package com.gta.utils.service;

import android.app.Service;
import android.content.Intent;
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
public class ForegroundService extends Service {

    static ForegroundService instance;

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;

        if (startService(new Intent(this, ForegroundEnablingService.class)) == null)
            throw new RuntimeException("Couldn't find " + ForegroundEnablingService.class.getSimpleName());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        instance = null;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}