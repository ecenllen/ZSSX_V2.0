package com.gta.zssx.fun.coursedaily.registercourse.view.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import java.util.List;

/**
 * [Description]
 * <p>
 * [How to use]
 * <p>
 * [Tips]
 *
 * @author Created by Zhimin.Huang on 2016/10/20.
 * @since 1.0.0
 */
public class CommonPageAdapter extends FragmentPagerAdapter {
    public static final String TAB_POSITION = "tab_position";
    private List<Fragment> mFragmentArrayList;
    public List<String> mTitleList;

    public CommonPageAdapter(FragmentManager fm, List<Fragment> fragmentArrayList, List<String> titleList) {
        super(fm);
        mTitleList = titleList;
        mFragmentArrayList = fragmentArrayList;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment List = mFragmentArrayList.get(position);
        Bundle lArguments = List.getArguments();
//            Bundle args = new Bundle();
        if(lArguments != null ){
            lArguments.putInt(TAB_POSITION, position);
            List.setArguments(lArguments);
        }else {
            Log.e("CommonPageAdapter","lArguments is null");
            try {
                throw new Exception();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return List;
    }

    @Override
    public int getCount() {
        return mFragmentArrayList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitleList.get(position);
    }
}
