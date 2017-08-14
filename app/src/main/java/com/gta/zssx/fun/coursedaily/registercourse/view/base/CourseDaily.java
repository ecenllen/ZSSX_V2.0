package com.gta.zssx.fun.coursedaily.registercourse.view.base;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

import com.gta.utils.module.BaseModule;
import com.gta.zssx.AppConfiguration;
import com.gta.zssx.fun.coursedaily.registercourse.model.ClassDataManager;
import com.gta.zssx.fun.coursedaily.registercourse.view.pageV2.ClassChooseActivity;
import com.gta.zssx.fun.coursedaily.registercourse.view.pageV2.CourseDailyActivity;
import com.gta.zssx.fun.personalcenter.model.bean.UserBean;
import com.gta.zssx.pub.exception.CustomException;

import org.apache.commons.codec.binary.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;

import rx.Subscription;

/**
 * [Description]
 * <p>
 * [How to use]
 * <p>
 * [Tips]
 *
 * @author Created by Zhimin.Huang on 2016/8/22.
 * @since 1.0.0
 */
public class CourseDaily implements BaseModule {

    private Context mContext;
    private String mServerAddress;
    private static String sCourseDialy = "COURSEDIALY";
    private Subscription mSubscribe;


    @Override
    public void destroy() {

    }

    public static CourseDaily getInstance() {
        return singleton.S_COURSE_DAILY;
    }

    private static class singleton {
        static final CourseDaily S_COURSE_DAILY = new CourseDaily();
    }

    public void init(Context context, String serverAddress) {
        mContext = context;
        mServerAddress = serverAddress;
    }

    public String getmServerAddress() {
        if(TextUtils.isEmpty(mServerAddress))
            mServerAddress = AppConfiguration.getInstance ().getServerAddress ().getCourseDailyUrl();
        return mServerAddress;
    }

    @Override
    public void displayActivity() {
        CourseDailyActivity.start(mContext);
    }

    public void displayWithCheck() {
        getClassList();
    }

    public void getClassList() {

        if (!AppConfiguration.getInstance().isFistIn()) {
            displayActivity();
            return;
        }

        UserBean lUserBean = null;
        try {
            lUserBean = AppConfiguration.getInstance().getUserBean();
        } catch (Exception e) {
            e.printStackTrace();
        }
        final UserBean finalLUserBean = lUserBean;
        mSubscribe = ClassDataManager.getClassList(lUserBean.getUserId())
                .subscribe(classDisplayBeen -> {
                    if (classDisplayBeen != null) {
                        AppConfiguration.getInstance().setFirstIn(finalLUserBean);
                        displayActivity();
                    } else {
                        ClassChooseActivity.start(mContext);
                    }
                    mSubscribe.unsubscribe();
                }, throwable -> {
                    if (throwable instanceof CustomException) {
                        CustomException lErrorCodeException = (CustomException) throwable;
                        if (lErrorCodeException.getMessage().equals("无匹配的班级信息！")  || lErrorCodeException.getCode() == 1) {
                            ClassChooseActivity.start(mContext);  //code 为1 也进入班级选择页面
                        }
                    }
                    mSubscribe.unsubscribe();
                });
    }


    @Override
    public void displayInFragment(int fragmentContainerResID) {

    }


    /**
     * 将对象编码
     *
     * @param object 编码对象
     * @param falg   flag
     */
    public void saveObject(Object object, String falg) {
        SharedPreferences preferences = mContext.getSharedPreferences(sCourseDialy,
                mContext.MODE_PRIVATE);
        // 创建字节输出流
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            // 创建对象输出流，并封装字节流
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            // 将对象写入字节流
            oos.writeObject(object);
            // 将字节流编码成base64的字符窜
            String appConfiguration_Base64 = new String(Base64.encodeBase64(baos
                    .toByteArray()));
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(falg, appConfiguration_Base64);

            editor.commit();
        } catch (IOException e) {
        }
        Log.i("ok", "存储成功");
    }

    /**
     * 解码
     *
     * @return
     */
    public Object readObject(String falg) {
        Object object = null;
        SharedPreferences preferences = mContext.getSharedPreferences(sCourseDialy,
                mContext.MODE_PRIVATE);
        String productBase64 = preferences.getString(falg, "");

        //读取字节
        byte[] base64 = Base64.decodeBase64(productBase64.getBytes());

        //封装到字节流
        ByteArrayInputStream bais = new ByteArrayInputStream(base64);
        try {
            //再次封装
            ObjectInputStream bis = new ObjectInputStream(bais);
            try {
                //读取对象
                object = bis.readObject();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        } catch (StreamCorruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.i("ok", "解析成功");
        return object;
    }
}
