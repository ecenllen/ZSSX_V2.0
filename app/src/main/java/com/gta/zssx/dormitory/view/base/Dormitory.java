package com.gta.zssx.dormitory.view.base;

import android.content.Context;
import android.text.TextUtils;

import com.gta.utils.module.BaseModule;
import com.gta.zssx.AppConfiguration;
import com.gta.zssx.dormitory.view.page.DormitoryMainActivity;

/**
 * Created by lan.zheng on 2017/6/16.
 */

public class Dormitory implements BaseModule {

    private Context mContext;
    private String mServerAddress;

    public static Dormitory getInstance() {
        return singleton.S_DORMITORY;
    }

    private static class singleton {
        static final Dormitory S_DORMITORY = new Dormitory();
    }

    public void init(Context context, String serverAddress) {
        mContext = context;
        mServerAddress = serverAddress;
    }

    public String getmServerAddress() {
        if(mServerAddress == null)
            mServerAddress = AppConfiguration.getInstance ().getServerAddress ().getDormitoryUrl();
        return mServerAddress;
    }

    public void setmServerAddress(String serverAddress) {
        this.mServerAddress = serverAddress;
    }

    @Override
    public void destroy() {

    }

    @Override
    public void displayActivity() {
        //TODO 宿舍主页
        DormitoryMainActivity.start(mContext);
    }


    @Override
    public void displayInFragment(int fragmentContainerResID) {

    }
}
