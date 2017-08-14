package com.gta.zssx.fun.coursedaily.registercourse.view.base;

import android.content.Context;
import android.support.v4.app.Fragment;

import com.gta.utils.fragment.FragmentBuilder;

/**
 * [Description]
 * <p/>
 * [How to use]
 * <p/>
 * [Tips]
 *
 * @author Created by Zhimin.Huang on 2016/6/15.
 * @since 1.0.0
 */
public abstract class RegisterCourseFragmentBuilder<T extends Fragment> extends FragmentBuilder<T> {

    public RegisterCourseFragmentBuilder(Context context) {
        super(context);
    }

    @Override
    protected int getContainerResID() {
        return RegisterCourseActivity.RESID;
    }

}
