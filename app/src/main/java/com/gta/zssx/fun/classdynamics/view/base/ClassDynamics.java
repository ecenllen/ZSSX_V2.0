package com.gta.zssx.fun.classdynamics.view.base;

import android.content.Context;

import com.gta.utils.module.BaseModule;
import com.gta.zssx.fun.coursedaily.registercourse.view.pageV2.CourseDailyActivity;

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
public class ClassDynamics implements BaseModule {

    private Context mContext;
    private String mServerAddress;
    private static String sCourseDialy = "COURSEDIALY";
    private Subscription mSubscribe;


    @Override
    public void destroy() {

    }

    public static ClassDynamics getInstance() {
        return singleton.CLASS_DYNAMICS;
    }

    private static class singleton {
        static final ClassDynamics CLASS_DYNAMICS = new ClassDynamics();
    }

    public void init(Context context, String serverAddress) {
        mContext = context;
        mServerAddress = serverAddress;
    }

    public String getmServerAddress() {
        return mServerAddress;
    }

    @Override
    public void displayActivity() {
        CourseDailyActivity.start(mContext);
    }


    @Override
    public void displayInFragment(int fragmentContainerResID) {

    }

}
