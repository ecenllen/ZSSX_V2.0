package com.gta.zssx.pub.util;

/**
 * Created by liang.lu on 2016/9/9 11:15.
 */
public class TimeUtils {
    private static long lastClickTime=0L;

    public static boolean isFastDoubleClick () {
        return isFastDoubleClick(500L);
    }

    public static boolean isFastDoubleClick (long lTime) {
        long time = System.currentTimeMillis ();
        long timeD = time - lastClickTime;
        if (0L < timeD && timeD < lTime) {
            return true;
        }
        lastClickTime = time;
        return false;
    }
}
