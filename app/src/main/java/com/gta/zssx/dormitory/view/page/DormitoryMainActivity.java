package com.gta.zssx.dormitory.view.page;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.gta.zssx.R;
import com.gta.zssx.dormitory.model.bean.RxDormitoryRankingListUpdateBean;
import com.gta.zssx.fun.coursedaily.registercourse.view.adapter.CommonPageAdapter;
import java.util.ArrayList;
import com.gta.zssx.pub.base.BaseCommonActivity;

import com.gta.zssx.pub.common.Constant;
import com.gta.zssx.pub.util.RxBus;

import java.util.List;

import rx.Subscriber;
import rx.subscriptions.CompositeSubscription;

/**
 * [Description]
 * 宿舍模块首页Activity
 * [How to use]
 *
 * [Tips]
 *
 * Created by lan.zheng on 2017/7/24 13:14.
 */

public class DormitoryMainActivity extends BaseCommonActivity{
    public static final String PAGE_TAG = DormitoryMainActivity.class.getSimpleName();
    public static final String ITEM_FLAG = "item_flag";
    public TabLayout mTabLayout;
    public ViewPager mViewPager;
    private ArrayList<String> mTitleList;    //标题栏
    private ArrayList<Fragment> mViewList;   //Fragment集合
    public CompositeSubscription mCompositeSubscription;//监听

    public static void start(Context context) {
        final Intent lIntent = new Intent(context, DormitoryMainActivity.class);

        if (!(context instanceof Activity)) {
            lIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(lIntent);
    }


    @Override
    public int getLayoutId() {
        return R.layout.activity_dormitory_main;
    }

    @Override
    protected void initView() {
        mToolBarManager.setTitle(getString(R.string.string_dormitory_ranking_input)).setRightText(getString(R.string.string_dormitory_new_ranking))
                .clickRightButton(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //宿舍评分新增录入
                        DormitoryNewRankingActivity.start(DormitoryMainActivity.this);
                    }
                });
        mTabLayout = (TabLayout) findViewById(R.id.dormitory_item_tablayout);
        mViewPager = (ViewPager) findViewById(R.id.dormitory_item_viewpager);
        initPageView();
        addListener();
    }

    private void initPageView() {
        mTitleList = new ArrayList<>();
        mViewList = new ArrayList<>();

        mTitleList.add(getString(R.string.string_dormitory_not_submit));
        mTitleList.add(getString(R.string.string_dormitory_have_been_submit));

        mTabLayout.setTabMode(TabLayout.MODE_FIXED);
        for (int i = 0; i < mTitleList.size(); i++) {
            mTabLayout.addTab(mTabLayout.newTab().setText(mTitleList.get(i)));
            DormitoryRankingListFragment lDormitoryRankingListFragment  = new DormitoryRankingListFragment();
            Bundle bundle = new Bundle();
            bundle.putInt(ITEM_FLAG,i);
            lDormitoryRankingListFragment.setArguments(bundle);
            mViewList.add(lDormitoryRankingListFragment);
        }

        CommonPageAdapter adapter = new CommonPageAdapter(getSupportFragmentManager(), mViewList, mTitleList);
        mViewPager.setAdapter(adapter);
        mTabLayout.setTabMode(TabLayout.MODE_FIXED);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    private void addListener(){
        mCompositeSubscription = new CompositeSubscription();
        mCompositeSubscription.add(RxBus.getDefault().toObserverable(RxDormitoryRankingListUpdateBean.class)
                .subscribe(new Subscriber<RxDormitoryRankingListUpdateBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(RxDormitoryRankingListUpdateBean rxDormitoryRankingListUpdateBean) {
                        //TODO 在宿舍楼或专业部页面执行了送审操作，成功后返回要定位到已送审页面
                        if(rxDormitoryRankingListUpdateBean.getSpecialRefresh()){
                            mViewPager.setCurrentItem(mViewList.size()-1);
                        }
                    }
                }));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mCompositeSubscription.unsubscribe();
    }
}
