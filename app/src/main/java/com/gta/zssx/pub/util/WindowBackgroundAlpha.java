package com.gta.zssx.pub.util;

import android.app.Activity;
import android.view.WindowManager;

/**
 * Created by weiye.chen on 2017/7/14.
 */

public class WindowBackgroundAlpha {

    public static void backgroundAlpha(Activity activity, float b) {
        WindowManager.LayoutParams layoutParams = activity.getWindow().getAttributes();
        layoutParams.alpha = b;
        activity.getWindow().setAttributes(layoutParams);
    }
}
