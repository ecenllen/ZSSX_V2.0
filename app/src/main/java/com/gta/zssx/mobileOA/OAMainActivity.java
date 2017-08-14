package com.gta.zssx.mobileOA;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gta.zssx.R;
import com.gta.zssx.fun.personalcenter.model.bean.UserBean;
import com.gta.zssx.fun.personalcenter.view.base.PersonalCenter;
import com.gta.zssx.mobileOA.presenter.OAMainPresenter;
import com.gta.zssx.mobileOA.view.OAMainView;
import com.gta.zssx.mobileOA.view.adapter.MobileOAItemListAdapter;
import com.gta.zssx.mobileOA.view.page.ApplyActivity;
import com.gta.zssx.mobileOA.view.page.BacklogMainActivity;
import com.gta.zssx.mobileOA.view.page.DutyNoticeActivity;
import com.gta.zssx.mobileOA.view.page.EventNoticeActivity;
import com.gta.zssx.mobileOA.view.page.MeetingMainActivity;
import com.gta.zssx.mobileOA.view.page.OfficialNoticeMainActivity;
import com.gta.zssx.mobileOA.view.page.ScheduleMainActivity;
import com.gta.zssx.mobileOA.view.page.TaskMainActivity;
import com.gta.zssx.patrolclass.popup.AddPageActivity;
import com.gta.zssx.pub.base.BaseActivity;
import com.gta.zssx.pub.manager.ToolBarManager;
import com.gta.zssx.pub.util.LogUtil;
import com.gta.zssx.pub.util.TimeUtils;

/**
 * Created by lan.zheng on 2016/10/11.
 */
public class OAMainActivity extends BaseActivity<OAMainView, OAMainPresenter> implements OAMainView, View.OnClickListener {
    public static final String PAGE_TAG = OAMainActivity.class.getSimpleName();
    public Toolbar mToolbar;
    protected ToolBarManager mToolBarManager;
    public RecyclerView mRecyclerView;
    private MobileOAItemListAdapter lMobileOAItemListAdapter;
    private String[] menu;
    private LinearLayout mButtonLayout;
    private TextView tab_home, tab_add, tab_my;
    private UserBean mUserBean;

    public static void start(Context context) {
        final Intent lIntent = new Intent(context, OAMainActivity.class);
//        Bundle lBundle = new Bundle();
        if (!(context instanceof Activity)) {
            lIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(lIntent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mobile_oa_main);
        initView();
        initData();
    }

    private void initView() {
        tab_home = (TextView) findViewById(R.id.tab_home);
        tab_add = (TextView) findViewById(R.id.tab_add);
        tab_my = (TextView) findViewById(R.id.tab_my);

        tab_home.setOnClickListener(this);
        tab_add.setOnClickListener(this);
        tab_my.setOnClickListener(this);

        mButtonLayout = (LinearLayout) findViewById(R.id.bottom_layout);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setNavigationOnClickListener(view -> onBackPressed());
        mToolBarManager = new ToolBarManager(mToolbar, this);
        mToolBarManager.init();
        mToolBarManager.setTitle(getString(R.string.text_mobile_oa));

        mRecyclerView = (RecyclerView) findViewById(R.id.list_item_oa_operation_show);
        GridLayoutManager lGridLayoutManager = new GridLayoutManager(this, 3);  //网格
        mRecyclerView.setLayoutManager(lGridLayoutManager);
    }

    private void initData() {
        mUserBean = presenter.getUserBean();
        menu = new String[]{getString(R.string.oa_applying_a_post), getString(R.string.oa_todo_object), getString(R.string.text_school_official), getString(R.string.oa_meeting),
                 getString(R.string.text_weekly_schedule), getString(R.string.text_event_notice)};
//        string = new String[]{getString(R.string.oa_applying_a_post), getString(R.string.oa_todo_object), getString(R.string.oa_official_notice), getString(R.string.oa_meeting),
//                getString(R.string.oa_mailbox), getString(R.string.oa_schedule_plan), getString(R.string.oa_contact), getString(R.string.text_event_notice), getString(R.string.oa_task), getString(R.string.oa_duty_notice)};
        WindowManager manager = this.getWindowManager();
        DisplayMetrics outMetrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(outMetrics);
        int width = outMetrics.widthPixels;
        int singleItemWidth = width / 3;
        float density = outMetrics.density;
        float allItemHeight = 460 * density;  //所有Item占的高度
        float allowHeight = mToolbar.getHeight() + mButtonLayout.getHeight() + 20 * density;  //别的地方占的高度
        if (outMetrics.heightPixels > allItemHeight + allowHeight) {
            LogUtil.Log("lenita", "height = " + outMetrics.heightPixels);
        }
        lMobileOAItemListAdapter = new MobileOAItemListAdapter(this, lListener, menu, singleItemWidth);
        mRecyclerView.setAdapter(lMobileOAItemListAdapter);
        //TODO 获取真实的信息
//        presenter.getAssetInfoData("", "", "");
    }

    MobileOAItemListAdapter.Listener lListener = new MobileOAItemListAdapter.Listener() {
        @Override
        public void itemClick(int position) {
            Log.d("test11", "position = " + position);
            switch (position) {

                case 0:  //发起申请
                    ApplyActivity.start(OAMainActivity.this);
                    break;
                case 1:  //待办/已办
                    BacklogMainActivity.start(OAMainActivity.this);
                    break;
                case 2:  //公文公告
//                    AttachmentListActivity.start(OAMainActivity.this);
                    OfficialNoticeMainActivity.start(OAMainActivity.this);
                    break;
                case 3:
                    MeetingMainActivity.start(OAMainActivity.this);
                    break;
                case 4: //日程
                    ScheduleMainActivity.start(OAMainActivity.this);
//                    ScheduleWeeklyActivity.start(OAMainActivity.this);
                    break;
                case 5: //事务提醒
                    EventNoticeActivity.start(OAMainActivity.this);
//                    DutyTableInfoActivity.start(OAMainActivity.this);
                    break;
                case 8: //任务安排
                    TaskMainActivity.start(OAMainActivity.this);
                    break;
                case 9:  //值班
                    DutyNoticeActivity.start(OAMainActivity.this);
                    break;
            }
        }
    };

    @NonNull
    @Override
    public OAMainPresenter createPresenter() {
        return new OAMainPresenter();
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.getRemindCount(mUserBean.getUserId(), mUserBean.getAccount());
    }

    @Override
    public void onClick(View v) {
        if (TimeUtils.isFastDoubleClick()) {
            return;
        }
        switch (v.getId()) {
            case R.id.tab_home:
                finish();
                break;
            case R.id.tab_add:
                setDrawableTop(tab_home, R.drawable.tab_home_nor);
                tab_home.setTextColor(getResources().getColor(R.color.gray_666666));
                startActivity(new Intent(this, AddPageActivity.class));
//                overridePendingTransition (R.anim.add_page_enter_anim,0);
                break;
            case R.id.tab_my:
                setDrawableTop(tab_home, R.drawable.tab_home_nor);
                tab_home.setTextColor(getResources().getColor(R.color.gray_666666));
                PersonalCenter.getInstance().displayActivity();
                break;
        }
    }

    @Override
    public void showRemindCount(int[] counts) {
        lMobileOAItemListAdapter.updateRemindCount(counts);
    }
}
