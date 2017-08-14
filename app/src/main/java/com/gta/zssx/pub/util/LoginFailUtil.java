package com.gta.zssx.pub.util;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import com.gta.zssx.AppConfiguration;
import com.gta.zssx.ZSSXApplication;
import com.gta.zssx.ZSSXJpushManager;
import com.gta.zssx.fun.personalcenter.view.page.LoginActivity;
import com.gta.zssx.pub.common.Constant;
import com.tencent.smtt.sdk.CookieManager;
import com.tencent.smtt.sdk.CookieSyncManager;

/**
 * Created by weiye.chen on 2017/1/17.
 */

public class LoginFailUtil {

    public static boolean isLoginFailUrl(Context context, String url) {
        boolean flag = false;
        if (!TextUtils.isEmpty(url)) {
            if (url.contains(Constant.LOGIN_FAIL_FLAG)) {
                logout(context, true);
                flag = true;
            }
        }
        return flag;
    }

    /**
     * 注销退出
     */
    public static void logout(Context context) {
        logout(context, false);
//        if (context == null)
//            return;
//        ZSSXApplication.instance.clearTodayTempSche();  //清除日程提醒
//        //这一句防止removeAllCookie在低版本手机导致退出登陆不了
//        CookieSyncManager.createInstance(context);
//        CookieManager manager = CookieManager.getInstance();
//        manager.removeAllCookie();
//        Toast.makeText(context, "登录超时，请重新登录！", Toast.LENGTH_LONG).show();
//        AppConfiguration.getInstance().finishAllActivity();
////        AppConfiguration.getInstance().setUserLoginBean(null).setFirstIn(null).saveAppConfiguration();
//        LoginActivity.start(context);
//        ZSSXJpushManager.unRegisterJpushManager(false);
//        ZSSXApplication.instance.userBean = null;
    }

    /**
     * @param context
     * @param isTimeout 是否登录超时,应用于资产等WebView 页面
     */
    public static void logout(Context context, boolean isTimeout) {
        if (context == null)
            return;
        ZSSXApplication.instance.clearTodayTempSche();  //清除日程提醒
        //这一句防止removeAllCookie在低版本手机导致退出登陆不了
        CookieSyncManager.createInstance(context);
        CookieManager manager = CookieManager.getInstance();
        manager.removeAllCookie();
        if (isTimeout) {
            Toast.makeText(context, "登录超时，请重新登录！", Toast.LENGTH_LONG).show();
        } else {
            // 用户主动点击退出登录，清空帐户信息，否则是登录超时，不清空。
            AppConfiguration.runOnThread(new Runnable() {
                @Override
                public void run() {
                    AppConfiguration.getInstance().setUserLoginBean(null).setFirstIn(null).saveAppConfiguration();
                }
            });
        }
        AppConfiguration.getInstance().finishAllActivity();
        LoginActivity.start(context);
        ZSSXJpushManager.unRegisterJpushManager(false);
        ZSSXApplication.instance.userBean = null;
    }

//    /**
//     * 注销退出
//     */
//    private void logout() {
//        ZSSXApplication.instance.clearTodayTempSche();  //清除日程提醒
//        //这一句防止removeAllCookie在低版本手机导致退出登陆不了
//        CookieSyncManager.createInstance(mActivity);
//        CookieManager manager = CookieManager.getInstance();
//        manager.removeAllCookie();
//        AppConfiguration.getInstance().finishAllActivity();
//        AppConfiguration.getInstance().setUserLoginBean(null).setFirstIn(null).saveAppConfiguration();
//        LoginActivity.start(PersonCenterActivity.this);
//        ZSSXJpushManager.unRegisterJpushManager(false);
//        ZSSXApplication.instance.userBean = null;
//    }
}
