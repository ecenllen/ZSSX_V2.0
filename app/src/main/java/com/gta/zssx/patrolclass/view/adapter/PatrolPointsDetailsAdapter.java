package com.gta.zssx.patrolclass.view.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

/**
 * Created by liang.lu on 2017/3/1 17:39.
 */

public class PatrolPointsDetailsAdapter extends FragmentStatePagerAdapter {
    private List<Fragment> list;
    private List<String> tabTitleList;

    public PatrolPointsDetailsAdapter (FragmentManager fm, List<Fragment> list,
                                       List<String> tabTitleList) {
        super (fm);
        this.list = list;
        this.tabTitleList = tabTitleList;
    }

    public void setTabTitleList (List<String> tabTitleList) {
        this.tabTitleList = tabTitleList;
    }

    public void setList (List<Fragment> list) {
        this.list = list;
    }

    public List<Fragment> getList () {
        return list;
    }

    @Override
    public Fragment getItem (int position) {
        return list.get (position);
    }

    @Override
    public int getCount () {
        return list.size ();
    }

    @Override
    public CharSequence getPageTitle (int position) {
        return tabTitleList.get (position);
    }


}
