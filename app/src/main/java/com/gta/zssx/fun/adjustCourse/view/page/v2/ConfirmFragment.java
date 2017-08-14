package com.gta.zssx.fun.adjustCourse.view.page.v2;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.gta.zssx.R;
import com.gta.zssx.fun.adjustCourse.model.nav.OnTabReselectListener;
import com.gta.zssx.fun.adjustCourse.view.base.BaseViewPagerFragment;
import com.gta.zssx.fun.adjustCourse.view.base.Constant;

/**
 * [Description]
 * <p>
 * [How to use]
 * <p>
 * [Tips]
 *
 * @author Created by Zhimin.Huang on 2017/3/6.
 * @since 1.0.0
 */
public class ConfirmFragment extends BaseViewPagerFragment implements OnTabReselectListener {



    @Override
    protected PagerInfo[] getPagers() {
        return new PagerInfo[]{
                new PagerInfo(getString(R.string.wait_confirm), ConfirmInnerFragment.class, getBundle(Constant.AUDIT_STATUS_N)),
                new PagerInfo(getString(R.string.has_confirm), ConfirmInnerFragment.class, getBundle(Constant.AUDIT_STATUS_Y))
        };
    }

    private Bundle getBundle(String catalog) {
        Bundle bundle = new Bundle();
        bundle.putString(MyApplyInnerFragment.STATUS, catalog);
        bundle.putInt(ConfirmInnerFragment.FRAGMENT_TYPE, ConfirmInnerFragment.CONFIRM);
        return bundle;
    }


    @Override
    public void onTabReselect() {
        if (mBaseViewPager != null) {
            BaseViewPagerAdapter pagerAdapter = (BaseViewPagerAdapter) mBaseViewPager.getAdapter();
            Fragment fragment = pagerAdapter.getCurFragment();
            if (fragment != null) {
                OnTabReselectListener listener = (OnTabReselectListener) fragment;
                listener.onTabReselect();
            }
        }
    }
}
