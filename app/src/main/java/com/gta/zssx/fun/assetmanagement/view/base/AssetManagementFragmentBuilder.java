package com.gta.zssx.fun.assetmanagement.view.base;

import android.content.Context;
import android.support.v4.app.Fragment;

import com.gta.utils.fragment.FragmentBuilder;
import com.gta.zssx.fun.coursedaily.registerrecord.view.page.AlreadyRegisteredRecordActivity;

/**
 * Created by lan.zheng on 2016/8/9.
 */
public abstract class AssetManagementFragmentBuilder <T extends Fragment> extends FragmentBuilder<T> {

    public AssetManagementFragmentBuilder(Context context) {
        super(context);
    }

    @Override
    protected int getContainerResID() {
        return AlreadyRegisteredRecordActivity.RESID;
    }

}

