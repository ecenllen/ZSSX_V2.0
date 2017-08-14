package com.gta.zssx.fun.coursedaily.registercourse.view.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by lan.zheng on 2017/3/28.
 */
public class CommonFragmentStatePagerAdapter extends FragmentStatePagerAdapter {

    public static final String TAB_POSITION = "tab_position";
    private List<Fragment> mFragmentArrayList;
    public List<String> mTitleList;
    private FragmentManager fragmentManager;

    public CommonFragmentStatePagerAdapter(FragmentManager fm, List<Fragment> fragmentArrayList, List<String> titleList) {
        super(fm);
        mTitleList = titleList;
        mFragmentArrayList = fragmentArrayList;
        fragmentManager = fm;
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

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        return super.instantiateItem(container, position);
        /*Fragment fragment = mFragmentArrayList.get(position);
        //判断当前的fragment是否已经被添加进入Fragmentanager管理器中
        if (!fragment.isAdded()) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.add(fragment, fragment.getClass().getSimpleName());
            //不保存系统参数，自己控制加载的参数
            transaction.commitAllowingStateLoss();
            //手动调用,立刻加载Fragment片段
            fragmentManager.executePendingTransactions();
        }
        if (fragment.getView().getParent() == null) {
            //添加布局
            container.addView(fragment.getView());
        }
        return fragment.getView();*/
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((Fragment) object).getView();
    }
}
