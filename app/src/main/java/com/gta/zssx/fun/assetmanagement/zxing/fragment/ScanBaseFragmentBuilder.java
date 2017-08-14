package com.gta.zssx.fun.assetmanagement.zxing.fragment;

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
public abstract class ScanBaseFragmentBuilder<T extends Fragment> extends FragmentBuilder<T> {

    public ScanBaseFragmentBuilder(Context context) {
        super(context);
    }

    @Override
    protected int getContainerResID() {
        return 0;
    }

}
