package com.gta.zssx.fun.adjustCourse.deprecated.view;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.blankj.utilcode.util.SPUtils;
import com.gta.utils.resource.L;
import com.gta.zssx.R;
import com.gta.zssx.fun.adjustCourse.model.bean.ApplySuccessBean;
import com.gta.zssx.fun.adjustCourse.deprecated.presenter.AdjustListPresenter;
import com.gta.zssx.fun.adjustCourse.view.base.AdjustCourse;
import com.gta.zssx.fun.coursedaily.registercourse.view.adapter.CommonPageAdapter;
import com.gta.zssx.pub.base.BaseActivity;
import com.gta.zssx.pub.manager.ToolBarManager;
import com.gta.zssx.pub.util.RxBus;
import com.gta.zssx.pub.widget.onDoubleClickListener;

import java.util.ArrayList;
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
@Deprecated
public class AdjustMainActivity extends BaseActivity<AdjustMainView, AdjustListPresenter> implements AdjustMainView {

    private static final String NOTIFY_CONFIRM_NEW_MESSAGE = "com.new.message";
    public static final String PAGE = "page";
    public TabLayout mTabLayout;
    public ViewPager mViewPager;
    public List<String> mTitleList;
    public LocalBroadcastManager mLocalBroadcastManager;
    private ImageView mRedPoint;
    public NewMessageReceiver mNewMessageReceiver;
    public SPUtils mSPUtils;
    private ConnectionChangeReceiver myReceiver;
    private RelativeLayout mNetworkLayout;
    public ArrayList<Fragment> mViewList;
    public boolean mDoRefresh;
    public int mDisplayPage;

    @NonNull
    @Override
    public AdjustListPresenter createPresenter() {
        return new AdjustListPresenter(mActivity);
    }

    public Toolbar mToolbar;
    public ToolBarManager mToolBarManager;

    public static void start(Context context, int displayPage) {
        final Intent lIntent = new Intent(context, AdjustMainActivity.class);
        lIntent.putExtra(PAGE, displayPage);
        if (!(context instanceof Activity)) {
            lIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(lIntent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adjust_main);
        initData();
        initView();
        loadData();
    }


    //用于页面间数据接收
    private void initData() {
        mTitleList = new ArrayList<>();
        mTitleList.add("调代课记录");
        mTitleList.add("调代课申请");
        mTitleList.add("调代课确认");
        mSPUtils = new SPUtils(AdjustCourse.RED_POINT_SP);
        mDisplayPage = getIntent().getIntExtra(PAGE, 0);
        registerReceiver();
    }


    //初始化view
    private void initView() {
        findViews();
        initToolbar();
        setOnInteractListener();
    }

    private void loadData() {
        mViewList = new ArrayList<>();

        mViewList.add(ApplyRecordFragment.newInstance(1));
        mViewList.add(ApplyCourseFragment.newInstance(2));
        mViewList.add(ApplyConfirmFragment.newInstance(3));

        for (int i = 0; i < mTitleList.size(); i++) {
            mTabLayout.addTab(mTabLayout.newTab().setText(mTitleList.get(i)));
        }
        CommonPageAdapter adapter = new CommonPageAdapter(getSupportFragmentManager(), mViewList, mTitleList);
        mViewPager.setAdapter(adapter);
        mTabLayout.setTabMode(TabLayout.MODE_FIXED);
        mTabLayout.setupWithViewPager(mViewPager);

    }


    private void initToolbar() {
        mToolBarManager.init();
        mToolbar.setNavigationOnClickListener(v -> onBackPressed());

        mToolBarManager.setTitle("调代课");

        mToolBarManager.showRightImage(true)
                .setRightImage(R.drawable.plus_sign)
                .clickRightImage(v -> CourseApplyActivity.start(mActivity, 0, null, null, 1));
//                .setRightText("查询")
//                .clickRightButton(v -> ScheduleSearchActivity.start(mActivity));

    }

    //事件处理
    private void setOnInteractListener() {

        //注册应用内广播
        mLocalBroadcastManager = LocalBroadcastManager.getInstance(getApplicationContext());
        mNewMessageReceiver = new NewMessageReceiver();
        IntentFilter lFilter = new IntentFilter();
        lFilter.addAction(NOTIFY_CONFIRM_NEW_MESSAGE);
        mLocalBroadcastManager.registerReceiver(mNewMessageReceiver, lFilter);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == mTitleList.size() - 1) {
                    mRedPoint.setVisibility(View.GONE);
                    mSPUtils.clear();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        mNetworkLayout.setOnClickListener(v -> {
            Intent intent = new Intent(Settings.ACTION_SETTINGS);
            startActivity(intent);
        });

        mToolbar.setOnTouchListener(new onDoubleClickListener(() -> {
            int lCurrentItem = mViewPager.getCurrentItem();
            if (0 == lCurrentItem) {
                ApplyRecordFragment lRecordFragment = (ApplyRecordFragment) mViewList.get(lCurrentItem);
                lRecordFragment.scrollToTop();
            } else if (1 == lCurrentItem) {
                ApplyCourseFragment lCourseFragment = (ApplyCourseFragment) mViewList.get(lCurrentItem);
                lCourseFragment.scrollToTop();
            } else {
                ApplyConfirmFragment lConfirmFragment = (ApplyConfirmFragment) mViewList.get(lCurrentItem);
                lConfirmFragment.scrollToTop();
            }

        }));

        presenter.mCompositeSubscription.add(RxBus.getDefault().toObserverable(ApplySuccessBean.class)
                .subscribe(applySuccessBean -> {
                    mViewPager.setCurrentItem(1);
                    ApplyCourseFragment lCourseFragment = (ApplyCourseFragment) mViewList.get(1);
                    lCourseFragment.loadData(1);
                }, throwable -> {

                }));

        presenter.mCompositeSubscription.add(RxBus.getDefault().toObserverable(Integer.class).subscribe(integer -> {
            if (integer == 2) {
                mRedPoint.setVisibility(View.GONE);
            }
        }, throwable -> {

        }));


    }

    @Override
    protected void onResume() {
        super.onResume();
        boolean lBoolean = mSPUtils.getBoolean(AdjustCourse.HAS_MESSAGE);
        mRedPoint.setVisibility(lBoolean ? View.VISIBLE : View.GONE);
    }

    private void setReceiverAction(String action, Intent intent) {
        if (action.equals(NOTIFY_CONFIRM_NEW_MESSAGE)) {
            mRedPoint.setVisibility(View.VISIBLE);
        } else {
            mRedPoint.setVisibility(View.VISIBLE);
        }
    }

    //绑定控件
    private void findViews() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolBarManager = new ToolBarManager(mToolbar, this);
        mTabLayout = (TabLayout) findViewById(R.id.adjust_list_tabLayout);
        mNetworkLayout = (RelativeLayout) findViewById(R.id.network_layout);
        mViewPager = (ViewPager) findViewById(R.id.adjust_list_viewpager);
        mRedPoint = (ImageView) findViewById(R.id.indicator_confirm_iv_tablayout);

    }

    public class NewMessageReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            L.d("CustomMessage", mActivity.getClass().getSimpleName(), "%1$s页收到广播：【Action = %2$s】", new Object[]{mActivity.getClass().getSimpleName(), action});
            setReceiverAction(action, intent);
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mToolBarManager.destroy();
        mLocalBroadcastManager.unregisterReceiver(mNewMessageReceiver);
        unregisterReceiver();
    }

    public static void notifyConfirmNewMessage(Context mContext) {
        final String lAction = NOTIFY_CONFIRM_NEW_MESSAGE;
        sendNewMessageBroadcast(mContext, lAction);
    }

    private static void sendNewMessageBroadcast(Context mContext, String action) {
        Context lApplicationContext = mContext.getApplicationContext();
        Intent intent = new Intent();
        intent.setAction(action);
        intent.putExtra("message", "Hello~");
        LocalBroadcastManager.getInstance(lApplicationContext).sendBroadcast(intent);
    }

    private class ConnectionChangeReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mobNetInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            NetworkInfo wifiNetInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

            if (mobNetInfo != null && mobNetInfo.isConnected() && !wifiNetInfo.isConnected()) {
                mNetworkLayout.setVisibility(View.VISIBLE);
                mDoRefresh = true;
                //改变背景或者 处理网络的全局变量
            } else {
                mNetworkLayout.setVisibility(View.GONE);
                int lCurrentItem = mViewPager.getCurrentItem();
                //防止第一次进来刷新空指针
                if (mDoRefresh) {
                    if (lCurrentItem == 0) {
                        ApplyRecordFragment lRecordFragment = (ApplyRecordFragment) mViewList.get(lCurrentItem);
                        lRecordFragment.loadData(1);
                    } else if (lCurrentItem == 1) {
                        ApplyCourseFragment lCourseFragment = (ApplyCourseFragment) mViewList.get(lCurrentItem);
                        lCourseFragment.loadData(1);
                    } else {
                        ApplyConfirmFragment lConfirmFragment = (ApplyConfirmFragment) mViewList.get(lCurrentItem);
                        lConfirmFragment.loadData(1);
                    }
                    mDoRefresh = false;
                }
                //改变背景或者 处理网络的全局变量
            }
        }
    }

    private void registerReceiver() {
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        myReceiver = new ConnectionChangeReceiver();
        this.registerReceiver(myReceiver, filter);
    }

    private void unregisterReceiver() {
        this.unregisterReceiver(myReceiver);
    }


}
