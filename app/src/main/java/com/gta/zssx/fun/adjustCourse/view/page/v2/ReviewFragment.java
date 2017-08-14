package com.gta.zssx.fun.adjustCourse.view.page.v2;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.gta.zssx.R;
import com.gta.zssx.fun.adjustCourse.model.nav.OnTabReselectListener;
import com.gta.zssx.fun.adjustCourse.view.base.BaseViewPagerFragment;
import com.gta.zssx.fun.adjustCourse.view.base.Constant;

import butterknife.Bind;

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
public class ReviewFragment extends BaseViewPagerFragment implements OnTabReselectListener {

    @Bind(R.id.tab_nav)
    TabLayout mTabLayout;
    @Bind(R.id.view_pager_container)
    ViewPager mViewPager;


    @Override
    public int getLayoutId() {
        return R.layout.fragment_base_viewpager;
    }


    @Override
    protected PagerInfo[] getPagers() {
        return new PagerInfo[]{
                new PagerInfo(getString(R.string.wait_audit), ConfirmInnerFragment.class, getBundle(Constant.AUDIT_STATUS_N)),
                new PagerInfo(getString(R.string.has_audit), ConfirmInnerFragment.class, getBundle(Constant.AUDIT_STATUS_Y))
        };
    }

    private Bundle getBundle(String catalog) {
        Bundle bundle = new Bundle();
        bundle.putString(MyApplyInnerFragment.STATUS, catalog);
        bundle.putInt(ConfirmInnerFragment.FRAGMENT_TYPE, ConfirmInnerFragment.AUDIT);
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
