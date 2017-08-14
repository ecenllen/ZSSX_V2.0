package com.gta.zssx.fun.classdynamics.view.page;

import android.support.v4.app.FragmentTransaction;

import com.gta.zssx.R;
import com.gta.zssx.pub.base.BaseCommonActivity;

/**
 * [Description]
 * <p>
 * [How to use]
 * <p>
 * [Tips]
 *
 * @author Created by Zhimin.Huang on 2017/3/20.
 * @since 1.0.0
 */
public class CDMainCommonActivity extends BaseCommonActivity {

    @Override
    public int getLayoutId() {
        return R.layout.activity_common_container;
    }

    @Override
    protected void initView() {
        FragmentTransaction lFragmentTransaction = getSupportFragmentManager().beginTransaction();
        lFragmentTransaction.add(R.id.fragment_container, CDMainFragment.newInstance()).commit();
    }

}
