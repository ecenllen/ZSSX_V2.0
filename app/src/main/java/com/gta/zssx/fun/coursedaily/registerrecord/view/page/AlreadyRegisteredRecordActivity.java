package com.gta.zssx.fun.coursedaily.registerrecord.view.page;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.gta.zssx.AppConfiguration;
import com.gta.zssx.R;
import com.gta.zssx.ZSSXApplication;
import com.gta.zssx.fun.coursedaily.registercourse.view.base.Constant;
import com.gta.zssx.fun.coursedaily.registercourse.view.pageV2.CourseDailyActivity;
import com.gta.zssx.fun.coursedaily.registerrecord.model.DTO.ClassInfoDto;
import com.gta.zssx.fun.coursedaily.registerrecord.model.DTO.MyClassCacheStatusDto;
import com.gta.zssx.fun.coursedaily.registerrecord.model.RegisteredRecordManager;
import com.gta.zssx.fun.coursedaily.registerrecord.presenter.AlreadyRegisteredRecordTeacherClassUpdatePresenter;
import com.gta.zssx.fun.coursedaily.registerrecord.view.AlreadyRegisteredRecordFragment;
import com.gta.zssx.fun.coursedaily.registerrecord.view.AlreadyRegisteredRecordTeacherClassUpdateView;
import com.gta.zssx.fun.coursedaily.registerrecord.view.base.RegisterRecordBaseFragment;
import com.gta.zssx.fun.personalcenter.model.bean.UserBean;
import com.gta.zssx.pub.base.BaseActivity;
import com.gta.zssx.pub.manager.ToolBarManager;
import com.gta.zssx.pub.util.LogUtil;
import com.gta.zssx.pub.util.RxBus;

import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import rx.functions.Action1;

/**
 * Created by lan.zheng on 2016/6/17.
 *
 */
public class AlreadyRegisteredRecordActivity extends BaseActivity<AlreadyRegisteredRecordTeacherClassUpdateView, AlreadyRegisteredRecordTeacherClassUpdatePresenter>
        implements AlreadyRegisteredRecordTeacherClassUpdateView {

    public static final String PAGE_TAG = AlreadyRegisteredRecordActivity.class.getSimpleName();
    public final static int MY_CLASSES_INIT_CACHE = 0;  //初始化缓存
    public final static int MY_CLASSES_FIRST_GET_DATA_DONE = 1;   //得到缓存后--暂时不使用缓存
    //    public final static int MY_CLASSES_SHOW_CACHE = 2;   //显示缓存
    public static List<MyClassCacheStatusDto> mMyClassesStatus;
    public static boolean mMyClassesLoadCatch = false;

    private static String sClass_info_dot = "class_info_dot";
    private static String sClass_number_info = "my_class_number_info";
    private static String sClass_my_class = "sClass_my_class";
    public Toolbar mToolbar;
    public ToolBarManager mToolBarManager;
    public String mGo_To;
    public static final int RESID = R.id.container;
    public static int BACKSTACKENTRYCOUNT = 1;
    private ClassInfoDto mClassInfoDto;
    private List<UserBean.ClassList> mClassLists;
    public UserBean mUserBean;
    private String mTeacherId;
    private boolean mIsEnterMyClass;

    //此入口变为：AlreadyRegisteredRecordPeriodOfTimeActivity
    public static void start(Context context, String go_to) {
        final Intent lIntent = new Intent(context, AlreadyRegisteredRecordActivity.class)
                .putExtra(ZSSXApplication.GOTO, go_to);
        if (!(context instanceof Activity)) {
            lIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(lIntent);
    }

    //签名确认需要的数据 或 已登记跳转下一个页面需要的数据
    public static void start(Context context, String go_to, ClassInfoDto classInfoDto) {
        final Intent lIntent = new Intent(context, AlreadyRegisteredRecordActivity.class);
        Bundle lBundle = new Bundle();
        lBundle.putString(ZSSXApplication.GOTO, go_to);
        lBundle.putSerializable(sClass_info_dot, classInfoDto);
        lIntent.putExtras(lBundle);
        if (!(context instanceof Activity)) {
            lIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(lIntent);
    }

    //我的班级需要的数据
    public static void start(Context context, String go_to, List<UserBean.ClassList> classLists, boolean isEnterMyClass) {
        final Intent lIntent = new Intent(context, AlreadyRegisteredRecordActivity.class);
        Bundle lBundle = new Bundle();
        lBundle.putString(ZSSXApplication.GOTO, go_to);
        lBundle.putSerializable(sClass_number_info, (Serializable) classLists);
        lBundle.putBoolean(sClass_my_class, isEnterMyClass);
        lIntent.putExtras(lBundle);
        if (!(context instanceof Activity)) {
            lIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(lIntent);
    }

    public WeakReference<RegisterRecordBaseFragment> mMyBaseFragmentWeakReference;

    public void setMyBaseFragmentWeakReference(RegisterRecordBaseFragment myBaseFragment) {
        mMyBaseFragmentWeakReference = new WeakReference<>(myBaseFragment);
    }


    @NonNull
    @Override
    public AlreadyRegisteredRecordTeacherClassUpdatePresenter createPresenter() {
        return new AlreadyRegisteredRecordTeacherClassUpdatePresenter();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_already_registered_recond);
        initView();
        initIntent(getIntent());
        if (!mIsEnterMyClass) {  //非进入我的班级先更新老师是否是班主任
            try {
                mUserBean = AppConfiguration.getInstance().getUserBean();
            } catch (Exception e) {
                e.printStackTrace();
            }
            mTeacherId = mUserBean.getUserId();
            checkTeacherClassUpdate();//"已登记"和“签名确认”先去更新老师的是否是班主任和所带班级的信息,之后再跳转到对应的Fragment
        }
        goToFragment();
    }

    private void initIntent(Intent intent) {
        mClassLists = new ArrayList<>();
        mGo_To = intent.getStringExtra(ZSSXApplication.GOTO);
        mClassInfoDto = (ClassInfoDto) intent.getExtras().getSerializable(sClass_info_dot);
        mClassLists = (List<UserBean.ClassList>) intent.getExtras().getSerializable(sClass_number_info);
        mIsEnterMyClass = intent.getBooleanExtra(sClass_my_class, false);
        mTeacherId = "0";
    }


    /*@Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        initIntent(intent);
    }*/

    private void initView() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolBarManager = new ToolBarManager(mToolbar, this);
        mToolBarManager.init();
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        mToolBarManager
                .showRightButton(true)
                .setRightText(mActivity.getResources().getString(R.string.text_class_log_register))
                .clickRightButton(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //重新进入登记页面
                        LogUtil.Log("test11", "进入登记页面--清除当前所有相关的缓存");
                        if (RegisteredRecordManager.getSaveCacheDtoFromSignature() != null)
                            RegisteredRecordManager.setSaveCacheDtoFromSignature(null);
                        if (RegisteredRecordManager.getSaveCacheDto() != null)
                            RegisteredRecordManager.setSaveCacheDto(null);
                        CourseDailyActivity.start(mActivity, true);
                        finish();
                    }
                });

        /**
         * 签名确认后把除首页外的Activity的finish掉
         */
        presenter.mCompositeSubscription.add(RxBus.getDefault().toObserverable(Constant.finishBean.class)
                .subscribe(new Action1<Constant.finishBean>() {
                    @Override
                    public void call(Constant.finishBean finishBean) {
                        finish();
                    }
                }));
    }

    private void checkTeacherClassUpdate() {
        presenter.getTeacherClassUpdate(mTeacherId);
    }

    public ToolBarManager getToolBarManager() {
        return mToolBarManager;
    }

    @Override
    public void onBackPressed() {
        RegisterRecordBaseFragment lMyBaseFragment = mMyBaseFragmentWeakReference.get();

        if (lMyBaseFragment != null && lMyBaseFragment.onBackPress()) {
            return;
        }
        closeCurrentPage();
    }

    private void closeCurrentPage() {
        if (getSupportFragmentManager().getBackStackEntryCount() == BACKSTACKENTRYCOUNT) {
            finish();
        } else {
            super.onBackPressed();
        }
    }

    private void goToFragment() {
        if (mGo_To.equals(AlreadyRegisteredRecordFragment.PAGE_TAG)) {
            //从“已登记”进入--此入口变为：AlreadyRegisteredRecordPeriodOfTimeActivity
            new AlreadyRegisteredRecordFragment.Builder(this).display();
        } else if (mGo_To.equals(AlreadyRegisteredRecordFromSignatureFragment.PAGE_TAG)) {
            if (this.getSupportFragmentManager().findFragmentByTag(AlreadyRegisteredRecordFromSignatureFragment.PAGE_TAG) != null) {
//                LogUtil.Log("test11", "activity -- (AlreadyRegisteredRecordFromSignatureFragment.TAG) != null");
                return; //TODO 如果有原来的Fragment，不需要再次的生成
            }

            //从“签名确认” 或 “已登记”进入
            RegisteredRecordManager.setSaveCacheDtoFromSignature(null);  //确保将要跳转的下一页面缓存为空
            new AlreadyRegisteredRecordFromSignatureFragment.Builder(this, mClassInfoDto).display();
//            Bundle lBundle = new Bundle();
//            lBundle.putSerializable(CLASS_INFO_DTO, mClassInfoDto);
//            AlreadyRegisteredRecordFromSignatureFragment lAlreadyRegisteredRecordFromSignatureFragment = new AlreadyRegisteredRecordFromSignatureFragment();
//            lAlreadyRegisteredRecordFromSignatureFragment.setArguments(lBundle);
//            mClassInfoDto = null;  //传送后置空
        } else if (mGo_To.equals(MyClassRecordFragment.PAGE_TAG)) {
            if (this.getSupportFragmentManager().findFragmentByTag(MyClassRecordFragment.PAGE_TAG) != null) {
//                LogUtil.Log("test11", "activity -- (MyClassRecordFragment.TAG) != null");
                return; //TODO 如果有原来的Fragment，不需要再次的生成
            }

            //初始化只有一个班级的“我的班级”，传入ClassID,为了到下个页面去获取数据
            RegisteredRecordManager.destroyDataCache();
            RegisteredRecordManager.setSaveCacheDtoDate(null);
            new MyClassRecordFragment.Builder(this, mClassLists.get(0)).display();
        } else if (mGo_To.equals(MyClassRecordSelectTabFragment.PAGE_TAG)) {
            if (this.getSupportFragmentManager().findFragmentByTag(MyClassRecordSelectTabFragment.PAGE_TAG) != null) {
//                LogUtil.Log("test11", "activity -- (MyClassRecordSelectTabFragment.TAG) != null");
                getSupportFragmentManager().popBackStack();  //把旧的Fragment退出栈中，这里必须要重新生成Fragment
            }
            mMyClassesStatus = new ArrayList<>();  // 跳转多个班级的列表信息,初始值0
            for (int i = 0; i < mClassLists.size(); i++) {
                MyClassCacheStatusDto lMyClassCacheStatusDto = new MyClassCacheStatusDto();
                lMyClassCacheStatusDto.setClassID(mClassLists.get(i).getId());
                lMyClassCacheStatusDto.setStatus(MY_CLASSES_INIT_CACHE);
                mMyClassesStatus.add(lMyClassCacheStatusDto);
            }
            RegisteredRecordManager.destroyMyClassRecordDataCache();
            new MyClassRecordSelectTabFragment.Builder(this, mClassLists).display();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);   //在Activity跳转前做判断，如果Fragment根据tag能找到实例，就不用重新build,这里不做判断
    }

    @Override
    public void getTeacherClassInfoReturn(boolean isSuccess) {
//        LogUtil.Log("test11", "update teacher class info is success =" + isSuccess);
    }
}
