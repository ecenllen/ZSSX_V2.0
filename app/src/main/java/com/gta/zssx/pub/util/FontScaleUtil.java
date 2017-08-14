package com.gta.zssx.pub.util;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.util.DisplayMetrics;

/**
 * [Description]
 * <p>
 * [How to use]
 * <p>
 * [Tips]
 *
 * @author Created by Zhimin.Huang on 2016/10/8.
 * @since 1.0.0
 */
public class FontScaleUtil {

    public static void initFontScale(Context context) {
        Configuration configuration = context.getResources().getConfiguration();
        configuration.fontScale = (float) 1;
        //0.85 小, 1 标准大小, 1.15 大，1.3 超大 ，1.45 特大
        DisplayMetrics metrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(metrics);
        metrics.scaledDensity = configuration.fontScale * metrics.density;
        ((Activity) context).getBaseContext().getResources().updateConfiguration(configuration, metrics);
    }
}
