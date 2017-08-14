package com.gta.zssx;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;

import com.gta.utils.resource.L;
import com.gta.zssx.fun.personalcenter.model.bean.ServerBean;
import com.gta.zssx.fun.personalcenter.model.bean.UserBean;
import com.gta.zssx.pub.util.LogUtil;

import org.apache.commons.codec.binary.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.StreamCorruptedException;
import java.util.Iterator;
import java.util.Stack;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
public class AppConfiguration implements Serializable {

    private static AppConfiguration _instance;
    private static Stack<Activity> activityStack;
    public static Context mContext;
    private static final String SAVEAPPCONFIGURATION = "saveappconfiguration";
    private static final String APPCONFIGURATION = "appconfiguration";
    private UserBean mUserBean;
    public String mFIRST_in = "FIRST_IN";
    //    = "http://192.168.105.192:7072/TQA/API/MoblieAPI/"
    public static String SERVERURL;
    private static String sServerBean = "ip_port";
    private static ExecutorService EXECUTORS_INSTANCE;

    // 默认存放文件下载的路径
    public final static String DEFAULT_SAVE_FILE_PATH = Environment
            .getExternalStorageDirectory ()
            + File.separator
            + "ZSSX"
            + File.separator + "download" + File.separator;

    // 默认存放图片的路径
    public final static String DEFAULT_SAVE_IMAGE_PATH = Environment
            .getExternalStoragePublicDirectory (Environment.DIRECTORY_PICTURES)
            .getAbsolutePath () + File.separator + "智慧校园";

    private AppConfiguration () {
        super ();
    }

    public static AppConfiguration getInstance () {
        if (_instance == null) {
            _instance = SINGLETON.sAppConfiguration;
        }

        return _instance;
    }

    private static class SINGLETON {

        static AppConfiguration sAppConfiguration = new AppConfiguration ();
    }

    private static class getExecutor {
        static ExecutorService EXECUTORS_INSTANCE = Executors.newFixedThreadPool(6);
    }

    //    共用线程池
    public static void runOnThread(Runnable runnable) {
        if (EXECUTORS_INSTANCE == null) {
            EXECUTORS_INSTANCE = getExecutor.EXECUTORS_INSTANCE;
        }
        EXECUTORS_INSTANCE.execute(runnable);
    }


    /**
     * app运行初始，创建app配置
     */
    public static synchronized void init (Context context) {
        mContext = context;
        // 获取存储的配置对象
        _instance = getAppConfiguration ();
        if (_instance == null) {
            // 无存储,则需首次创建配置对象，并存储到sharereference
            _instance = SINGLETON.sAppConfiguration;
        }
        _instance.logScreenPX ();
    }

    /**
     * 保存AppConfiguration实例
     */
    public void saveAppConfiguration () {
        saveObject (_instance, SAVEAPPCONFIGURATION);
    }

    public static AppConfiguration getAppConfiguration () {
        return (AppConfiguration) readObject (SAVEAPPCONFIGURATION);
    }


    /////////功能:登录用户//////////////////////////////////////
    public UserBean getUserBean () throws Exception {
        if (mUserBean == null) {
            //return new User();
            throw new RuntimeException ("用户未登录");
        }
        return mUserBean;
    }

    public AppConfiguration setUserLoginBean (UserBean loginInfo) {
        mUserBean = loginInfo;
        return this;
    }

    //设置第一次登陆
    public AppConfiguration setFirstIn (UserBean userBean) {
        saveObject (userBean, mFIRST_in);
        return this;
    }

    public boolean isFistIn () {
        UserBean userBean = (UserBean) readObject (mFIRST_in);
        return userBean == null;
    }

    //用于记住服务器地址
    public AppConfiguration setServerAddress (ServerBean serverBean) {
        saveObject (serverBean, sServerBean);
        return this;
    }

    public ServerBean getServerAddress () {
        return (ServerBean) readObject (sServerBean);
    }

    public boolean hasServerAddress () {
        ServerBean ip = (ServerBean) readObject (sServerBean);
        return ip != null;
    }

    /**
     * 将对象编码
     *
     * @param object 编码对象
     * @param falg   flag
     */
    public static void saveObject (Object object, String falg) {
        SharedPreferences preferences = mContext.getSharedPreferences (APPCONFIGURATION,
                mContext.MODE_PRIVATE);
        // 创建字节输出流
        ByteArrayOutputStream baos = new ByteArrayOutputStream ();
        ObjectOutputStream oos = null;
        try {
            // 创建对象输出流，并封装字节流
            oos = new ObjectOutputStream (baos);
            // 将对象写入字节流
            oos.writeObject (object);
            // 将字节流编码成base64的字符窜
            String appConfiguration_Base64 = new String (Base64.encodeBase64 (baos
                    .toByteArray ()));
            SharedPreferences.Editor editor = preferences.edit ();
            editor.putString (falg, appConfiguration_Base64);

            editor.commit ();
        } catch (IOException e) {
            e.printStackTrace ();
        } finally {
            try {
                if (oos != null) {
                    oos.close ();
                }
                if (baos != null) {
                    baos.close ();
                }
            } catch (IOException e) {
                e.printStackTrace ();
            }
        }
        LogUtil.Log ("ok", "存储成功");
    }

    /**
     * 解码
     *
     * @return
     */
    public static Object readObject (String falg) {
        Object object = null;
        SharedPreferences preferences = mContext.getSharedPreferences (APPCONFIGURATION,
                mContext.MODE_PRIVATE);
        String productBase64 = preferences.getString (falg, "");

        //读取字节
        byte[] base64 = Base64.decodeBase64 (productBase64.getBytes ());

        //封装到字节流
        ByteArrayInputStream bais = new ByteArrayInputStream (base64);
        ObjectInputStream bis = null;
        try {
            //再次封装
            bis = new ObjectInputStream (bais);
            try {
                //读取对象
                object = bis.readObject ();
            } catch (ClassNotFoundException e) {
                e.printStackTrace ();
            }
        } catch (StreamCorruptedException e) {
            e.printStackTrace ();
        } catch (IOException e) {
            e.printStackTrace ();
        } finally {
            try {
                if (bais != null) {
                    bais.close ();
                }
                if (bis != null) {
                    bis.close ();
                }
            } catch (IOException i) {
                i.printStackTrace ();
            }
        }
        LogUtil.Log ("ok", "解析成功");
        return object;
    }

    /**
     * 获取手机屏幕信息：密度、宽、高
     *
     * @return
     */
    @TargetApi (Build.VERSION_CODES.FROYO)
    public AppConfiguration logScreenPX () {
        DisplayMetrics displayMetrics = mContext.getResources ().getDisplayMetrics ();
        float mDensity = displayMetrics.density;
        int widthPixels = displayMetrics.widthPixels;
        int hightPixels = displayMetrics.heightPixels;
        int uiMode = mContext.getResources ().getConfiguration ().uiMode;

        L.e (this, "本机参数:\n" + "【屏幕密度】（density）%1$s \n" + "【屏幕大小】（widthPixels）%2$s X （heightPixels）%3$s \n"
                + "【uiMode】:%4$s", mDensity, widthPixels, hightPixels, uiMode);
        return this;
    }

    public static boolean isFirstInstall () {
        try {
            if (getAppConfiguration () == null) {
                return true;
            }
        } catch (Exception e) {
            return true;
        }
        return false;
    }


    /**
     * 返回当前程序版本名
     */
    public String getAppVersionName (Context context) {
        String versionName = "";
        try {
            // ---get the package info---
            PackageManager pm = context.getPackageManager ();
            PackageInfo pi = pm.getPackageInfo (context.getPackageName (), 0);
            versionName = pi.versionName;
            if (versionName == null || versionName.length () <= 0) {
                return "";
            }
        } catch (Exception e) {
            Log.e ("VersionInfo", "Exception", e);
        }
        return versionName;
    }

    /**
     * 添加Activity到堆栈
     */
    public void addActivity (Activity activity) {
        if (activityStack == null) {
            activityStack = new Stack<> ();
        }
        activityStack.add (activity);
    }

    /**
     * 获取当前Activity（堆栈中最后一个压入的）
     */
    public Activity currentActivity () {
        return activityStack.lastElement ();
    }

    /**
     * 结束当前Activity（堆栈中最后一个压入的）
     */
    public void finishActivity () {
        Activity activity = activityStack.lastElement ();
        finishActivity (activity);
    }

    /**
     * 结束指定的Activity
     */
    public void finishActivity (Activity activity) {
        if (activity != null) {
            activityStack.remove (activity);
            activity.finish ();
            activity = null;
        }
    }

    /**
     * 移除指定的Activity
     */
    public void removeActivity (Activity activity) {
        if (activity != null) {
            activityStack.remove (activity);
            activity = null;
        }
    }

    /**
     * 结束指定类名的Activity
     */
    public void finishActivity (Class<?> cls) {

        Iterator<Activity> itr = activityStack.iterator ();

        while (itr.hasNext ()) {
            Activity activity = (Activity) itr.next ();
            if (activity.getClass ().equals (cls)) {
                itr.remove ();
                activity.finish ();
            }
        }
    }

    /**
     * 结束所有Activity
     */
    public void finishAllActivity () {
        for (int i = 0; i < activityStack.size (); i++) {
            if (null != activityStack.get (i)) {
                activityStack.get (i).finish ();
                i = -1;
            }
        }
        activityStack.clear ();
    }

    /**
     * 退出应用程序
     *
     * @param context      上下文
     * @param isBackground 是否开开启后台运行
     *                     <p>
     *                     在android2.2版本之后则不能再使用restartPackage()方法，而应该使用killBackgroundProcesses()方法
     */
    public void AppExit (Context context, Boolean isBackground) {
        try {
            finishAllActivity ();
            ActivityManager activityMgr = (ActivityManager) context
                    .getSystemService (Context.ACTIVITY_SERVICE);
            activityMgr.killBackgroundProcesses (context.getPackageName ());
        } catch (Exception e) {

        } finally {
            // 注意，如果您有后台程序运行，请不要支持此句子

        }
    }

    /**
     * 重启app,在activity里执行
     *
     * @param context 上下文
     */
    public void ReStartApp (Context context) {
        Activity activity = currentActivity ();
        activityStack.remove (activity);
        AppExit (context, true);
        Intent i = context.getPackageManager ()
                .getLaunchIntentForPackage (context.getPackageName ());
        i.addFlags (Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity (i);
        activity.finish ();
        activity = null;

    }

    /**
     * 获取指定Activity
     *
     * @return 指定Activity
     */
    public Activity getActivity (Class<?> cls) {
        Activity mActivity = null;
        for (Activity activity : activityStack) {
            if (activity.getClass ().equals (cls)) {
                mActivity = activity;
                break;
            }
        }
        return mActivity;
    }
}
