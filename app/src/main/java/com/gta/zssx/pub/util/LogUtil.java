package com.gta.zssx.pub.util;

import android.util.Log;

import com.gta.zssx.BuildConfig;
import com.gta.zssx.ZSSXApplication;

/**
 * [Description]
 *
 * [How to use]
 *
 * [Tips]
 * @author
 *   Created by Zhimin.Huang on 2016/6/18.
 * @since
 *   1.0.0
 */
public class LogUtil {
    public static void Log(String TAG, String Message){
        if(BuildConfig.DEBUG){
            switch (ZSSXApplication.LOG_LEVEL){
                case "v":
                    Log.v(TAG,Message);
                    break;
                case "d":
                    Log.d(TAG,Message);
                    break;
                default:
                    break;
            }
        }

    }

    private static final String TAG = "TAG";

    public static void e(String msg){
        Log.e (TAG,msg);
    }
}
