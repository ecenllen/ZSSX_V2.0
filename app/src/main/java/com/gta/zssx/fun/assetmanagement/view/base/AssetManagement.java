package com.gta.zssx.fun.assetmanagement.view.base;

import android.content.Context;
import android.text.TextUtils;

import com.gta.utils.module.BaseModule;
import com.gta.zssx.AppConfiguration;

/**
 * [Description]
 * <p>
 * [How to use]
 * <p>
 * [Tips]
 *
 * @author Created by Zhimin.Huang on 2016/8/24.
 * @since 1.0.0
 */
public class AssetManagement implements BaseModule {

    private Context mContext;
    private String mServerAddress;

    public static AssetManagement getInstance() {
        return singleton.S_COURSE_DAILY;
    }

    private static class singleton {
        static final AssetManagement S_COURSE_DAILY = new AssetManagement();
    }

    public void init(Context context, String serverAddress) {
        mContext = context;
        mServerAddress = serverAddress;
    }

    public String getmServerAddress() {
        if(TextUtils.isEmpty(mServerAddress))
            mServerAddress = AppConfiguration.getInstance ().getServerAddress ().getAssetUrl();
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
    }


    @Override
    public void displayInFragment(int fragmentContainerResID) {

    }


}
