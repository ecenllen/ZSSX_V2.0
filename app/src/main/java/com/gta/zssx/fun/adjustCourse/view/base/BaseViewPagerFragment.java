package com.gta.zssx.fun.adjustCourse.view.base;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.gta.zssx.R;
import com.gta.zssx.pub.base.BaseCommonFragment;

import butterknife.Bind;

/**
 * [Description]
 * <p>
 * [How to use]
 * <p>
 * [Tips]
 *
 * @author Created by Zhimin.Huang on 2017/3/14.
 * @since 1.0.0
 */
public abstract class BaseViewPagerFragment extends BaseCommonFragment {


    @Bind(R.id.tab_nav)
    protected TabLayout mTabNav;

    @Bind(R.id.view_pager_container)
    protected ViewPager mBaseViewPager;

    private BaseViewPagerAdapter adapter;

    public BaseViewPagerAdapter getAdapter() {
        return adapter;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_base_viewpager;
    }

    @Override
    protected void initView(View view) {
        adapter = new BaseViewPagerAdapter(getChildFragmentManager(), getPagers());
        mBaseViewPager.setAdapter(adapter);
        mTabNav.setTabMode(TabLayout.MODE_FIXED);
        mTabNav.setupWithViewPager(mBaseViewPager);
//        mBaseViewPager.setCurrentItem(0, true);
    }

    protected abstract PagerInfo[] getPagers();

    public static class PagerInfo {
        private String title;
        private Class<?> clx;
        private Bundle args;

        public PagerInfo(String title, Class<?> clx, Bundle args) {
            this.title = title;
            this.clx = clx;
            this.args = args;
        }
    }

    public class BaseViewPagerAdapter extends FragmentPagerAdapter {
        private PagerInfo[] mInfoList;
        private Fragment mCurFragment;

        public BaseViewPagerAdapter(FragmentManager fm, PagerInfo[] infoList) {
            super(fm);
            mInfoList = infoList;
        }

        @Override
        public void setPrimaryItem(ViewGroup container, int position, Object object) {
            super.setPrimaryItem(container, position, object);
            if (object instanceof Fragment) {
                mCurFragment = (Fragment) object;
            }
        }

        public Fragment getCurFragment() {
            return mCurFragment;
        }

        @Override
        public Fragment getItem(int position) {
            PagerInfo info = mInfoList[position];
            return Fragment.instantiate(getContext(), info.clx.getName(), info.args);
        }

        @Override
        public int getCount() {
            return mInfoList == null ? 0 : mInfoList.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mInfoList[position].title;
        }

        @Override
        public int getItemPosition(Object object) {
            return PagerAdapter.POSITION_NONE;
        }
    }

}
