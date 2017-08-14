package com.gta.zssx.dormitory.view.page;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.view.View;
import android.view.ViewGroup;

import com.gta.zssx.R;
import com.gta.zssx.dormitory.model.bean.DormitoryListArgumentBean;
import com.gta.zssx.dormitory.model.bean.DormitoryOrClassSingleInfoBean;
import com.gta.zssx.fun.adjustCourse.view.base.BaseViewPagerFragment;
import com.gta.zssx.pub.util.BeanCloneUtil;
import com.gta.zssx.pub.util.DensityUtil;

import java.util.ArrayList;

/**
 * [Description]
 * <p> 填充宿舍列表/班级列表Activity， 主要只是实现ViewPager 来显示每一栋宿舍楼界面
 * [How to use]
 * <p>
 * [Tips]
 * @author Created by Weiye.Chen on 2017/7/20.
 * @since 2.0.0
 */
public class DormitoryListFragment extends BaseViewPagerFragment {

    private PagerInfo[] mPageInfo;
    /*当前宿舍楼下标*/
    public static final String DormitoryBuildingPosition = "position";

    @Override
    public int getLayoutId() {
        return R.layout.fragment_base_viewpager;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    protected void initView(View view) {
        super.initView(view);
        if(getAdapter().getCount() > 2)
            mTabNav.setTabMode(TabLayout.MODE_SCROLLABLE);
        ViewGroup.LayoutParams layoutParams = mTabNav.getLayoutParams();
        if(layoutParams != null)
            layoutParams.height = DensityUtil.dip2px(getActivity(), 40);
    }

    @Override
    protected void initBundle(Bundle bundle) {
        super.initBundle(bundle);
//        ArrayList<DormitoryOrClassSingleInfoBean> list = (ArrayList<DormitoryOrClassSingleInfoBean>) bundle.getSerializable(DormitoryListActivity.LIST_ID);
        DormitoryListArgumentBean dormitoryListArgumentBean = bundle.getParcelable(DormitoryListActivity.KEY_DORMITORY_LIST_ARGUMENT);
        if (dormitoryListArgumentBean != null) {
            ArrayList<DormitoryOrClassSingleInfoBean> list = dormitoryListArgumentBean.getDormitoryIdList();
            if (list != null) {
                mPageInfo = new PagerInfo[list.size()];
                for (int i = 0; i < list.size(); i++) {
                    DormitoryListArgumentBean bean = BeanCloneUtil.cloneTo(dormitoryListArgumentBean);
                    Bundle bundle2 = new Bundle();
                    bundle2.putParcelable(DormitoryListActivity.KEY_DORMITORY_LIST_ARGUMENT, bean);
                    bundle2.putInt(DormitoryBuildingPosition, i);
                    mPageInfo[i] = new PagerInfo(list.get(i).getDormitoryOrClassName(), DormitoryListInnerFragment.class, bundle2);
                }
            }
        }
    }

    @Override
    protected PagerInfo[] getPagers() {
        return mPageInfo;
    }

    public void refreshData() {
        if (mBaseViewPager != null) {
            BaseViewPagerAdapter adapter = (BaseViewPagerAdapter) mBaseViewPager.getAdapter();
            DormitoryListInnerFragment curFragment = (DormitoryListInnerFragment) adapter.getCurFragment();
            adapter.getItemPosition(curFragment);
        }
    }

//    @Override
//    public void onTabReselect() {
        /*if (mBaseViewPager != null) {
            BaseViewPagerAdapter pagerAdapter = (BaseViewPagerAdapter) mBaseViewPager.getAdapter();
            Fragment fragment = pagerAdapter.getCurFragment();
            if (fragment != null) {
                OnTabReselectListener listener = (OnTabReselectListener) fragment;
                listener.onTabReselect();
            }
        }*/
//    }
}
