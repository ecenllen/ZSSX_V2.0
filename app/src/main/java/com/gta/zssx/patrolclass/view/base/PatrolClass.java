package com.gta.zssx.patrolclass.view.base;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

import com.gta.utils.module.BaseModule;
import com.gta.zssx.AppConfiguration;
import com.gta.zssx.patrolclass.view.page.PatrolClassActivity;

import org.apache.commons.codec.binary.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;

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
public class PatrolClass implements BaseModule {

    private  Context mContext;
    private  String mServerAddress;
    private  static String sCourseDialy = "COURSEDIALY";

    public static PatrolClass getInstance() {
        return singleton.S_COURSE_DAILY;
    }

    private static class singleton {
        static final PatrolClass S_COURSE_DAILY = new PatrolClass();
    }

    public void init(Context context, String serverAddress) {
        mContext = context;
        mServerAddress = serverAddress;
    }

    public  String getmServerAddress() {
        if(TextUtils.isEmpty(mServerAddress))
            mServerAddress = AppConfiguration.getInstance ().getServerAddress ().getPatrolClassUrl();
        return mServerAddress;
    }

    @Override
    public void destroy() {

    }

    @Override
    public void displayActivity() {
        PatrolClassActivity.start(mContext);
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
    public  void saveObject(Object object, String falg) {
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
    public  Object readObject(String falg) {
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
