package com.gta.zssx.main;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.gta.utils.helper.MutiHitHelper;
import com.gta.utils.resource.L;
import com.gta.utils.resource.SysRes;
import com.gta.zssx.AppConfiguration;
import com.gta.zssx.R;
import com.gta.zssx.ZSSXApplication;
import com.gta.zssx.ZSSXJpushManager;
import com.gta.zssx.dormitory.model.bean.DormitoryListArgumentBean;
import com.gta.zssx.dormitory.model.bean.DormitoryOrClassLevelList;
import com.gta.zssx.dormitory.model.bean.DormitoryOrClassListBean;
import com.gta.zssx.dormitory.model.bean.DormitoryOrClassListInfoBean;
import com.gta.zssx.dormitory.model.bean.ItemInfo;
import com.gta.zssx.dormitory.view.base.Dormitory;
import com.gta.zssx.fun.adjustCourse.model.bean.NoticeBean;
import com.gta.zssx.fun.adjustCourse.model.utils.NoticeManager;
import com.gta.zssx.fun.adjustCourse.view.base.AdjustCourse;
import com.gta.zssx.fun.adjustCourse.view.base.Constant;
import com.gta.zssx.fun.adjustCourse.view.page.ScheduleSearchActivity;
import com.gta.zssx.fun.assetmanagement.view.base.AssetManagement;
import com.gta.zssx.fun.assetmanagement.view.page.AssetWebViewShowActivity;
import com.gta.zssx.fun.classroomFeedback.view.base.ClassroomFeedback;
import com.gta.zssx.fun.coursedaily.registercourse.view.base.CourseDaily;
import com.gta.zssx.fun.personalcenter.model.bean.ServerBean;
import com.gta.zssx.fun.personalcenter.model.bean.UserBean;
import com.gta.zssx.fun.personalcenter.view.base.PersonalCenter;
import com.gta.zssx.mobileOA.OAMainActivity;
import com.gta.zssx.mobileOA.model.BaseManager;
import com.gta.zssx.patrolclass.popup.AddPageActivity;
import com.gta.zssx.patrolclass.view.base.PatrolClass;
import com.gta.zssx.pub.base.BaseActivity;
import com.gta.zssx.pub.util.TimeUtils;
import com.tbruyelle.rxpermissions.RxPermissions;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by liang.lu1 on 2016/7/6.
 * Home页
 */
public class HomePageActivity extends BaseActivity<HomePageView, HomePagePresenter>
        implements View.OnClickListener, HomePageView, NoticeManager.NoticeNotify, HomePageAdapter.HomePageItemListener {
    private TextView tab_home, tab_add, tab_my;
    private RelativeLayout mClass_log;
    private RelativeLayout mClass_patrol;
    private RelativeLayout mClass_asset;
    private RelativeLayout mClass_oa;
    private RelativeLayout mClass_course;
    private TextView mAdjustCourseRedPoint;
    private LocalBroadcastManager mLocalBroadcastManager;
    private NewMessageReceiver mNewMessageReceiver;
    private RelativeLayout mClass_schedule;
    private RelativeLayout mClass_dormitory;
    private RelativeLayout mClass_feedback;
    private RecyclerView mRecyclerView;
    private List<UserBean.MenuItemListBean> mMenuItemListBeen;

    private HomePageAdapter mAdapter;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_home_page_v3);

        ServerBean lServerAddress = AppConfiguration.getInstance ().getServerAddress ();
        if (lServerAddress != null) {
            //            PersonalCenter.getInstance ().init (getApplicationContext (), lServerAddress.getCourseDailyUrl ());
            CourseDaily.getInstance ().init (getApplicationContext (), lServerAddress.getCourseDailyUrl ());
            PatrolClass.getInstance ().init (getApplicationContext (), lServerAddress.getPatrolClassUrl ());
            AssetManagement.getInstance ().init (getApplicationContext (), lServerAddress.getAssetUrl ());
            AdjustCourse.getInstance ().init (getApplicationContext (), lServerAddress.getAdjustCourseUrl ());
            Dormitory.getInstance ().init (getApplicationContext (), lServerAddress.getDormitoryUrl ());
            // 课堂教学反馈
            ClassroomFeedback.getInstance ().init (getApplicationContext (), lServerAddress.getClassroomFeedbackUrl ());
            BaseManager.address = lServerAddress.getOAUrL ();
        }

        //初始化推送模块
        ZSSXJpushManager.registerJpushManager (getApplicationContext ());

        NoticeManager.getInstance ().getNotice ();
        initView ();
        setOnInteractListener ();
        intiData ();
        loadData ();
        NoticeManager.bindNotify (this);
        setPageData ();

    }

    private void intiData () {
        mMenuItemListBeen = presenter.getPageList ();
        ZSSXApplication.instance.getUser ().setSmallIconsList (presenter.getSmallIconsList (mMenuItemListBeen));
    }

    private void setPageData () {
        mAdapter = new HomePageAdapter (this, this);
        mAdapter.setMenuItemListBeen (mMenuItemListBeen);
        mRecyclerView.setAdapter (mAdapter);
    }

    @TargetApi (Build.VERSION_CODES.JELLY_BEAN)
    private void loadData () {
        RxPermissions.getInstance (this).request (Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe (aBoolean -> {
                    if (aBoolean) {
                        presenter.checkUpdate (this);
                    } else {
                        com.gta.utils.resource.Toast.Long (mActivity, "请到设置开启应用权限");
                    }
                }, throwable -> {

                });
    }

    private void setOnInteractListener () {

        tab_home.setOnClickListener (this);
        tab_add.setOnClickListener (this);
        tab_my.setOnClickListener (this);

        mClass_log.setOnClickListener (this);
        mClass_patrol.setOnClickListener (this);
        mClass_asset.setOnClickListener (this);
        mClass_oa.setOnClickListener (this);
        mClass_course.setOnClickListener (this);
        mClass_schedule.setOnClickListener (this);
        mClass_dormitory.setOnClickListener (this);
        mClass_feedback.setOnClickListener (this);

        //注册应用内广播
        mLocalBroadcastManager = LocalBroadcastManager.getInstance (getApplicationContext ());
        mNewMessageReceiver = new NewMessageReceiver ();
        IntentFilter lFilter = new IntentFilter ();
        lFilter.addAction (NOTIFY_CONFIRM_NEW_MESSAGE);
        mLocalBroadcastManager.registerReceiver (mNewMessageReceiver, lFilter);
        mRecyclerView.setLayoutManager (new GridLayoutManager (this, 2));
        mRecyclerView.setHasFixedSize (true);

    }

    @Override
    public void onNoticeArrived (NoticeBean bean) {
        //        if (bean.getIsAudit () == Constant.HAS_AUDIT) {
        //            if (bean.getIsHasRightAudit () == Constant.HAS_AUDIT) {
        //                showRedDot (bean.getWaitAuditNumber () + bean.getWaitConfirmNumber ());
        //            } else {
        //                showRedDot (bean.getWaitConfirmNumber ());
        //            }
        //        } else {
        //            showRedDot (bean.getWaitConfirmNumber ());
        //        }

        UserBean.MenuItemListBean menuItemBean = null;
        for (int i = 0; i < mMenuItemListBeen.size (); i++) {
            if (mMenuItemListBeen.get (i).getPowerCode ().equals ("0106")) {
                menuItemBean = mMenuItemListBeen.get (i);
                break;
            }
        }
        if (menuItemBean != null) {
            if (bean.getIsAudit () == Constant.HAS_AUDIT) {
                if (bean.getIsHasRightAudit () == Constant.HAS_AUDIT) {
                    //                    showRedDot (bean.getWaitAuditNumber () + bean.getWaitConfirmNumber ());
                    menuItemBean.setPushCount (String.valueOf (bean.getWaitAuditNumber () + bean.getWaitConfirmNumber ()));
                } else {
                    //                    showRedDot (bean.getWaitConfirmNumber ());
                    menuItemBean.setPushCount (String.valueOf (bean.getWaitConfirmNumber ()));
                }
            } else {
                //                showRedDot (bean.getWaitConfirmNumber ());
                menuItemBean.setPushCount (String.valueOf (bean.getWaitConfirmNumber ()));
            }
            if (mAdapter != null) {
                mAdapter.notifyDataSetChanged ();
            }
        }

    }

    private void showRedDot (int waitConfirmNumber) {
        mAdjustCourseRedPoint.setVisibility (waitConfirmNumber > 0 ? View.VISIBLE : View.GONE);
        mAdjustCourseRedPoint.setText (String.valueOf (waitConfirmNumber));
    }

    @Override
    public void clickHomePageItem (int position) {
        UserBean.MenuItemListBean bean = mMenuItemListBeen.get (position);
        String powerCode = bean.getPowerCode ();
        switch (powerCode) {
            case "B00":
                //跳转到课堂日志页面
                if (!SysRes.isConnected (mActivity)) {
                    ToastUtils.showShortToast(getString (R.string.net_work_error));
                } else {
                    CourseDaily.getInstance ().displayWithCheck ();
                }
                break;
            case "E00200":
                //跳转到巡课页面
                if (!SysRes.isConnected (mActivity)) {
                    ToastUtils.showShortToast(getString (R.string.net_work_error));
                } else {
                    PatrolClass.getInstance ().displayActivity ();
                }
                break;
            case "200":
                //跳转到OA页面
                OAMainActivity.start (this);
                break;
            case "0106":
                //跳转到调代课页面
                AdjustCourse.getInstance ().displayActivity ();
                break;
            case "ZC":
                //跳转到资产页面
                if (!SysRes.isConnected (mActivity)) {
                    ToastUtils.showShortToast(getString (R.string.net_work_error));
                } else {
                    AssetWebViewShowActivity.start (this, getString (R.string.url_asset_management), 0, AssetWebViewShowActivity.LOADING_ASSET_MAIN);
                }
                break;
            case "0107":
                //跳转到课表查询页面
                AdjustCourse.getInstance ().goToActivity (ScheduleSearchActivity.class.getSimpleName ());
                break;
            case "406402":
                //跳转到宿舍页面
                if (!SysRes.isConnected (mActivity)) {
                    ToastUtils.showShortToast(getString (R.string.net_work_error));
                } else {
                    Dormitory.getInstance ().displayActivity ();
                }
                break;
            case "E00300":
                //课堂教学反馈入口
                if (!SysRes.isConnected (mActivity)) {
                    ToastUtils.showShortToast(getString (R.string.net_work_error));
                } else {
                    ClassroomFeedback.getInstance ().displayActivity ();
                }
                break;
            default:
                break;
        }

    }


    public class NewMessageReceiver extends BroadcastReceiver {

        @Override
        public void onReceive (Context context, Intent intent) {
            String action = intent.getAction ();
            L.d ("CustomMessage", mActivity.getClass ().getSimpleName (), "%1$s页收到广播：【Action = %2$s】", new Object[]{mActivity.getClass ().getSimpleName (), action});
            setReceiverAction (action, intent);
        }
    }

    private void setReceiverAction (String action, Intent intent) {
        if (action.equals (NOTIFY_CONFIRM_NEW_MESSAGE)) {
            mAdjustCourseRedPoint.setVisibility (View.VISIBLE);
        } else {
            mAdjustCourseRedPoint.setVisibility (View.VISIBLE);
        }
    }

    //    public static boolean isJustClassLog = false;  //只显示课堂日志的标识，勿删
    private void initView () {

        //        mSPUtils = new SPUtils(mActivity, AdjustCourse.RED_POINT_SP);

        tab_home = (TextView) findViewById (R.id.tab_home);
        tab_add = (TextView) findViewById (R.id.tab_add);
        tab_my = (TextView) findViewById (R.id.tab_my);


        mClass_log = (RelativeLayout) findViewById (R.id.ll_class_log);
        mClass_patrol = (RelativeLayout) findViewById (R.id.ll_class_patrol);
        mClass_asset = (RelativeLayout) findViewById (R.id.ll_asset);
        mClass_oa = (RelativeLayout) findViewById (R.id.ll_oa);
        mClass_course = (RelativeLayout) findViewById (R.id.ll_adjust_course);
        mClass_schedule = (RelativeLayout) findViewById (R.id.ll_schedule_search);
        mClass_dormitory = (RelativeLayout) findViewById (R.id.ll_dormitory);
        mClass_feedback = (RelativeLayout) findViewById (R.id.ll_classroom_feedback);
        mAdjustCourseRedPoint = (TextView) findViewById (R.id.indicator_adjust_course_home);

        mRecyclerView = (RecyclerView) findViewById (R.id.recycler);
    }

    public static void start (Context context) {
        final Intent lIntent = new Intent (context, HomePageActivity.class);
        if (!(context instanceof Activity)) {
            lIntent.setFlags (Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity (lIntent);
    }


    @Override
    public void onClick (View v) {
        if (TimeUtils.isFastDoubleClick ()) {
            return;
        }
        switch (v.getId ()) {
            case R.id.tab_home:
                break;
            case R.id.tab_add:
                setDrawableTop (tab_home, R.drawable.tab_home_nor);
                tab_home.setTextColor (getResources ().getColor (R.color.gray_666666));
                Intent mIntent = new Intent (this, AddPageActivity.class);
                startActivity (mIntent);
                overridePendingTransition (R.anim.add_page_enter_anim, 0);
                break;
            case R.id.tab_my:
                setDrawableTop (tab_home, R.drawable.tab_home_nor);
                tab_home.setTextColor (getResources ().getColor (R.color.gray_666666));
                PersonalCenter.getInstance ().displayActivity ();
                break;
            case R.id.ll_class_log:
                //跳转到课堂日志页面
                if (!SysRes.isConnected (mActivity)) {
                    ToastUtils.showShortToast(getString (R.string.net_work_error));
                } else {
                    CourseDaily.getInstance ().displayWithCheck ();
                }
                break;
            case R.id.ll_class_patrol:
                //跳转到巡客
                if (!SysRes.isConnected (mActivity)) {
                    ToastUtils.showShortToast(getString (R.string.net_work_error));
                } else {
                    PatrolClass.getInstance ().displayActivity ();
                    //                    PatrolRegisterActivityNew.start(this);
                }
                break;
            case R.id.ll_asset:
                //跳转到资产
                if (!SysRes.isConnected (mActivity)) {
                    ToastUtils.showShortToast(getString (R.string.net_work_error));
                } else {
                    AssetWebViewShowActivity.start (this, getString (R.string.url_asset_management), 0, AssetWebViewShowActivity.LOADING_ASSET_MAIN);
                    //                    presenter.getTicket();
                    //                    AssetManagement.getInstance().displayAsActivity();
                    //                    Toast.makeText(HomePageActivity.this, "该功能尚未实现！", Toast.LENGTH_SHORT).show();
                    //                    TestWebViewActivity.start (this);
                }
                break;
            case R.id.ll_oa:
                //                startActivity(new Intent(this, AssetManagementActivity.class));
                //TODO 移动OA的入口
                OAMainActivity.start (this);
                break;
            case R.id.ll_adjust_course:
                AdjustCourse.getInstance ().displayActivity ();
                break;
            case R.id.ll_schedule_search:
                AdjustCourse.getInstance ().goToActivity (ScheduleSearchActivity.class.getSimpleName ());
                break;
            case R.id.ll_dormitory:
                //TODO 宿舍入口
                if (!SysRes.isConnected (mActivity)) {
                    ToastUtils.showShortToast(getString (R.string.net_work_error));
                } else {
                    Dormitory.getInstance ().displayActivity ();
                }
                break;
            case R.id.ll_classroom_feedback:
                //课堂教学反馈入口
                if (!SysRes.isConnected (mActivity)) {
                    ToastUtils.showShortToast(getString (R.string.net_work_error));
                } else {
                    ClassroomFeedback.getInstance ().displayActivity ();
                }
                break;

            default:

        }
    }

    @Override
    protected void onDestroy () {
        super.onDestroy ();
        mLocalBroadcastManager.unregisterReceiver (mNewMessageReceiver);
        NoticeManager.unBindNotify (this);
    }

    @Override
    protected void onResume () {
        super.onResume ();
        setDrawableTop (tab_home, R.drawable.tab_home_sel);
        tab_home.setTextColor (getResources ().getColor (R.color.main_color));

    }

    @NonNull
    @Override
    public HomePagePresenter createPresenter () {
        return new HomePagePresenter ();
    }


    private MutiHitHelper mMutiHitHelper;

    @Override
    public void onBackPressed () {
        //        super.onBackPressed();
        if (mMutiHitHelper == null) {
            MutiHitHelper.OnMutiHitListener lMOnMutiHitListener = new MutiHitHelper.OnMutiHitListener () {
                @Override
                public void onMaxHit () {
                    finish ();
                }

                @Override
                public void onHitDoing (int i) {
                    com.gta.utils.resource.Toast.Short (mActivity, R.string.util_toast_TipsClickAgain);
                }
            };
            mMutiHitHelper = new MutiHitHelper (lMOnMutiHitListener);
            mMutiHitHelper.setMax_hit (1);
            mMutiHitHelper.setTimePerHit (1000);
        }
        mMutiHitHelper.Hit ();
    }

    private static final String NOTIFY_CONFIRM_NEW_MESSAGE = "com.new.message";
}
