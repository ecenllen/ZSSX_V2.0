package com.gta.zssx.fun.classroomFeedback.view.base;

import android.content.Context;

import com.gta.utils.module.BaseModule;
import com.gta.zssx.fun.classroomFeedback.view.page.ClassroomFeedbackActivity;

/**
 * [Description]
 * <p> 课堂教学反馈模块入口类
 * [How to use]
 * <p>
 * [Tips]
 *
 * @author Created by liang.lu on 2017/06/20.
 * @since 2.0.0
 */

public class ClassroomFeedback implements BaseModule {

    private Context mContext;
    private String mServerAddress;

    public static ClassroomFeedback getInstance () {
        return singleton.S_DORMITORY;
    }

    private static class singleton {
        static final ClassroomFeedback S_DORMITORY = new ClassroomFeedback ();
    }

    public void init (Context context, String serverAddress) {
        mContext = context;
        mServerAddress = serverAddress;
    }

    public String getmServerAddress () {
        return mServerAddress;
    }

    public void setmServerAddress (String serverAddress) {
        this.mServerAddress = serverAddress;
    }

    @Override
    public void destroy () {

    }

    @Override
    public void displayActivity () {
        //TODO 课堂教学反馈
        ClassroomFeedbackActivity.start (mContext);
    }


    @Override
    public void displayInFragment (int fragmentContainerResID) {

    }
}
