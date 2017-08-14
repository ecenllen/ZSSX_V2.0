package com.gta.zssx.mobileOA.view.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by xiaoxia.rang on 2016/10/19.
 */

public class MyFragmentPagerAdapter extends FragmentPagerAdapter {
    private String[] titles;
    private List<Fragment> fragments;

    public MyFragmentPagerAdapter(FragmentManager fm,String[] titles,List<Fragment> fragments) {
        super(fm);
        this.titles = titles;
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments == null? 0:fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }

   /* @Override
    public int getItemPosition(Object object) {
        return PagerAdapter.POSITION_NONE;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        return super.instantiateItem(container, position);
    }*/
}
