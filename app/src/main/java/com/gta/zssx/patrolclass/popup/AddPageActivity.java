package com.gta.zssx.patrolclass.popup;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RelativeLayout;

import com.gta.utils.resource.SysRes;
import com.gta.zssx.R;
import com.gta.zssx.ZSSXApplication;
import com.gta.zssx.fun.adjustCourse.view.base.AdjustCourse;
import com.gta.zssx.fun.adjustCourse.view.page.v2.CourseApplyActivityV2;
import com.gta.zssx.fun.assetmanagement.view.page.AssetWebViewShowActivity;
import com.gta.zssx.fun.coursedaily.registercourse.view.base.CourseDaily;
import com.gta.zssx.fun.personalcenter.model.bean.UserBean;
import com.gta.zssx.patrolclass.view.page.AddClassChooseActivity;
import com.gta.zssx.patrolclass.view.page.PlanProtalResultActivity;
import com.gta.zssx.pub.base.BaseAppCompatActivity;
import com.gta.zssx.pub.fragment.AddPageFragmentOne;
import com.gta.zssx.pub.fragment.AddPageFragmentTwo;

import java.util.List;


/**
 * Created by liang.lu on 2016/11/14 15:35.
 */

public class AddPageActivity extends BaseAppCompatActivity implements View.OnClickListener
        , AddPageFragmentOne.FinishedListener, AddPageFragmentTwo.FinishedListener, AddPageAdapter.SmallIconsClickListener {

//    private AddPageFragmentOne fragmentOne;
//    private AddPageFragmentTwo fragmentTwo;
    //    private ProgressDialog mProgressDialog;
    private List<UserBean.MenuItemListBean> mSmallIconsListBeen;
    private MyRecyclerView mRecyclerView;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.home_add_page);
        //        getWindow().setWindowAnimations(R.style.mystyle);
        initData ();
        initView ();
        setOnClickListener ();
        loadData ();
    }

    private void loadData () {
        AddPageAdapter mAdapter = new AddPageAdapter (this, mSmallIconsListBeen, this);
        mRecyclerView.setAdapter (mAdapter);
    }

    private void setOnClickListener () {
        //        fragmentOne.setListener (this);
        //        fragmentTwo.setListener (this);
        mRecyclerView.setLayoutManager (new GridLayoutManager (this, 3));
        mRecyclerView.setHasFixedSize (true);
    }

    private void initData () {
        mSmallIconsListBeen = ZSSXApplication.instance.getUser ().getSmallIconsList ();
    }

    private void initView () {
        RelativeLayout lRl_exit = (RelativeLayout) findViewById (R.id.rl_exit);
        //        CustomViewPager mViewPage = (CustomViewPager) findViewById (R.id.viewpager);
        //        CirclePageIndicator mPageIndicator = (CirclePageIndicator) findViewById (R.id.indicator);
        mRecyclerView = (MyRecyclerView) findViewById (R.id.recycler);

        //        List<Fragment> fragmentList = new ArrayList<> ();
        //        fragmentOne = AddPageFragmentOne.getInstance ();
        //        fragmentTwo = AddPageFragmentTwo.getInstance ();
        //        fragmentList.add (fragmentOne);
        /**
         * todo 课堂日志暂时隐藏
         */
        //        fragmentList.add (fragmentTwo);
        //        MyFragmentPagerAdapter pagerAdapter = new MyFragmentPagerAdapter (getSupportFragmentManager (), fragmentList);
        //        if (mViewPage != null) {
        //            mViewPage.setAdapter (pagerAdapter);
        //            mViewPage.setCurrentItem (0);
        //        }
        //        if (mPageIndicator != null) {
        //            if (pagerAdapter.getCount () > 1) {
        //                mPageIndicator.setViewPager (mViewPage);
        //                mPageIndicator.setVisibility (View.VISIBLE);
        //            } else {
        //                mPageIndicator.setVisibility (View.INVISIBLE);
        //            }
        //        }
        if (lRl_exit != null) {
            lRl_exit.setOnClickListener (this);
        }
    }

    @Override
    public void onClick (View v) {
        finish ();
        overridePendingTransition (0, R.anim.add_page_out_anim);
    }

    @Override
    public void Finished () {
        finish ();
        overridePendingTransition (0, R.anim.add_page_out_anim);
    }

    @Override
    public boolean onKeyDown (int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish ();
            overridePendingTransition (0, R.anim.add_page_out_anim);
        }
        return super.onKeyDown (keyCode, event);
    }

    @Override
    public void clickSmallIconItem (int position) {
        finish ();
        overridePendingTransition (0, R.anim.add_page_out_anim);
        UserBean.MenuItemListBean bean = mSmallIconsListBeen.get (position);
        String powerName = bean.getPowerName ();
        switch (powerName) {
            case "日志登记":
                if (!SysRes.isConnected (this)) {
                    com.gta.utils.resource.Toast.Short (this, getString (R.string.net_work_error));
                } else {
                    CourseDaily.getInstance ().displayWithCheck ();
                }
                break;
            case "随机巡课":
                if (!SysRes.isConnected (this)) {
                    com.gta.utils.resource.Toast.Short (this, getString (R.string.net_work_error));
                } else {
                    AddClassChooseActivity.start (this, "random", "1", "");
                }
                break;
            case "按计划巡课":
                if (!SysRes.isConnected (this)) {
                    com.gta.utils.resource.Toast.Short (this, getString (R.string.net_work_error));
                } else {
                    PlanProtalResultActivity.start (this, "1", "");
                }
                break;
            case "调代课申请":
                if (!SysRes.isConnected (this)) {
                    com.gta.utils.resource.Toast.Short (this, getString (R.string.net_work_error));
                    return;
                }
                AdjustCourse.getInstance ().goToActivity (CourseApplyActivityV2.class.getSimpleName ());
                break;
            case "资产分配":
                if (!SysRes.isConnected (this)) {
                    com.gta.utils.resource.Toast.Short (this, getString (R.string.net_work_error));
                    return;
                }
                AssetWebViewShowActivity.start (this, getString (R.string.url_distribute_asset), 0, AssetWebViewShowActivity.LOADING_ASSET_NORMAL);
                break;
            case "资产转移":
                if (!SysRes.isConnected (this)) {
                    com.gta.utils.resource.Toast.Short (this, getString (R.string.net_work_error));
                    return;
                }
                AssetWebViewShowActivity.start (this, getString (R.string.url_shift_asset), 0, AssetWebViewShowActivity.LOADING_ASSET_NORMAL);
                break;
            case "资产维修":
                AssetWebViewShowActivity.start (this, getString (R.string.url_repair_asset), 0, AssetWebViewShowActivity.LOADING_ASSET_NORMAL);
                break;
            case "资产报废":
                AssetWebViewShowActivity.start (this, getString (R.string.url_scrap_asset), 0, AssetWebViewShowActivity.LOADING_ASSET_NORMAL);
                break;
            default:
                break;
        }
    }
}

