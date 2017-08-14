package com.gta.zssx;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.StrictMode;
import android.support.multidex.MultiDex;

import com.blankj.utilcode.util.Utils;
import com.gta.utils.resource.L;
import com.gta.utils.thirdParty.jPush.JpushManager;
import com.gta.zssx.fun.personalcenter.model.bean.UserBean;
import com.gta.zssx.main.HomePageActivity;
import com.gta.zssx.mobileOA.model.bean.Schedule;
import com.gta.zssx.pub.common.Config;
import com.gta.zssx.pub.http.HttpMethod;
import com.gta.zssx.pub.manager.DownloadManager;
import com.gta.zssx.pub.service.GrayService;
import com.tencent.bugly.crashreport.CrashReport;

import java.util.ArrayList;
import java.util.List;

import cn.sharesdk.framework.ShareSDK;

/**
 * [Description]
 * <p>
 * [How to use]
 * <p>
 * [Tips]
 *
 * @author Created by Zhimin.Huang on 2016/6/13.
 * @since 1.0.0
 */
public class ZSSXApplication extends Application {

    public static final String GOTO = "go_to";
    public static final boolean IS_ENABLE_LOG = true;  //通过改变这个值来看是否开启log打印
    public static final String LOG_LEVEL = "d";  //v:verbose,d:debug,w:warn,e:error
    private List<Schedule> todayScheTemp;  //用于日程提醒的列表
    public static ZSSXApplication instance;
    public UserBean userBean;

    @Override
    public void onCreate () {
        super.onCreate ();
        instance = this;

        // 下面注释掉的初始化工作放到StartActivity 里执行
        Context mContext = getApplicationContext ();

        /*if (LeakCanary.isInAnalyzerProcess (this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install (this);*/

        init (mContext);

        userBean = getUser ();
//        ServerBean lServerAddress = AppConfiguration.getInstance ().getServerAddress ();
//        if (lServerAddress != null) {
//            PersonalCenter.getInstance ().init (this, lServerAddress.getCourseDailyUrl ());
//            CourseDaily.getInstance ().init (this, lServerAddress.getCourseDailyUrl ());
//            PatrolClass.getInstance ().init (this, lServerAddress.getPatrolClassUrl ());
//            AssetManagement.getInstance ().init (this, lServerAddress.getAssetUrl ());
//            AdjustCourse.getInstance ().init (this, lServerAddress.getAdjustCourseUrl ());
//        }
        // 记录未处理的异常情况，日程提醒
        this.todayScheTemp = new ArrayList<Schedule> ();
        //        CrashReport.testJavaCrash();

        //初始化下载downloadManager
//        DownloadManager.getInstance ().init (getContext ());

        //        因为buildsdk是>=24,所以调用Uri.fromFile时保错
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder ();
        StrictMode.setVmPolicy (builder.build ());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            builder.detectFileUriExposure ();
        }
    }

    private void init (Context mContext) {
    /*
   * 初始化AppConfiguration设置
   */
        try {
            AppConfiguration.init (mContext);
        } catch (Exception e) {
            e.printStackTrace ();
        }
        L.writeLogs (BuildConfig.DEBUG);

        MultiDex.install (mContext);
        //toast 工具类初始化
        Utils.init (this);
        //分享初始化
        ShareSDK.initSDK (this);

        //bugly初始化
        CrashReport.initCrashReport (getApplicationContext (), getString (R.string.Buyly_AppId), false);

        //初始化网络请求
        HttpMethod.init (getContext (), AppConfiguration.SERVERURL);

        //初始化极光推送
        JpushManager.getInstance ().init (this, HomePageActivity.class);
        JpushManager.getInstance ().setDebugMode (false);

        //初始化推送模块
        ZSSXJpushManager.registerJpushManager (this);

        Intent intent = new Intent (this, GrayService.class);
        startService (intent);
    }


    public Context getContext () {
        return getApplicationContext ();
    }

    public List<Schedule> getTodayScheTemp () {
        return todayScheTemp;
    }

    public void setTodayScheTemp (List<Schedule> scheTemp) {

        if (null != todayScheTemp) {
            for (Schedule ts : todayScheTemp) {

                if (ts.isHasNoti ()) {
                    int id = ts.getId ();
                    for (Schedule s : scheTemp) {
                        if (id == s.getId ()) {
                            s.setHasNoti (true);
                        }
                    }
                }
            }
            this.todayScheTemp = scheTemp;
        }
    }

    public void updateTodaySche (int id, boolean isNotify) {

        for (Schedule s : todayScheTemp) {
            if (id == s.getId ()) {
                s.setHasNoti (isNotify);
            }
        }
    }

    /**
     * 用户退出登录时，清空缓存集合
     */
    public void clearTodayTempSche () {
        if (null != todayScheTemp) {
            todayScheTemp.clear ();
        }
    }

    public String getProperty (String key) {
        return Config.getGTAConfig (this).get (key);
    }

    public UserBean getUser () {
        if (userBean == null) {
            try {
                userBean = AppConfiguration.getInstance ().getUserBean ();
            } catch (Exception e) {
                e.printStackTrace ();
            }
        }
        return userBean;
    }

}
