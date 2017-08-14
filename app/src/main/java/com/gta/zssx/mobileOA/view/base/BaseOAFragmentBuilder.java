package com.gta.zssx.mobileOA.view.base;

import android.content.Context;
import android.support.v4.app.Fragment;

import com.gta.utils.fragment.FragmentBuilder;

/**
 * Created by lan.zheng on 2016/10/26.
 */
public abstract class BaseOAFragmentBuilder<T extends Fragment> extends FragmentBuilder<T> {

    public BaseOAFragmentBuilder(Context context) {
        super(context);
    }

    @Override
    protected int getContainerResID() {
        return BaseOAActivity.RESID;
    }


}