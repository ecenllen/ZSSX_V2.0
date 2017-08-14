package com.gta.zssx.fun.coursedaily.registercourse.view.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.gta.utils.helper.MutiHitHelper;
import com.gta.utils.resource.Toast;
import com.gta.zssx.R;
import com.gta.zssx.ZSSXApplication;
import com.gta.zssx.fun.coursedaily.registercourse.view.page.ClassChooseFragment;
import com.gta.zssx.fun.coursedaily.registercourse.view.page.ClassDisplayFragment;
import com.gta.zssx.fun.coursedaily.registercourse.view.page.RegisterDetailFragment;
import com.gta.zssx.fun.coursedaily.registerrecord.model.DTO.RegisteredRecordFromSignatureDto;
import com.gta.zssx.pub.base.BaseAppCompatActivity;
import com.gta.zssx.pub.manager.ToolBarManager;

import java.lang.ref.WeakReference;

/**
 * [Description]
 * <p/>
 * [How to use]
 * <p/>
 * [Tips]
 *
 * @author Created by Zhimin.Huang on 2016/6/15.
 * @since 1.0.0
 */
public class RegisterCourseActivity extends BaseAppCompatActivity {

    public static String sModifyBean = "modifyBean";
    public static String sIsModify = "isModify";
    public Toolbar mToolbar;
    public ToolBarManager mToolBarManager;
    public String mGo_To;
    public static final int RESID = R.id.container;
    private boolean isModify;
    private RegisteredRecordFromSignatureDto mSignatureDto;
    public static int BACKSTACKENTRYCOUNT = 1;

    public static void start(Context context, String go_to) {
        final Intent lIntent = new Intent(context, RegisterCourseActivity.class)
                .putExtra(ZSSXApplication.GOTO, go_to);
        if (!(context instanceof Activity)) {
            lIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(lIntent);
    }

    public static void start(Context context, String go_to, RegisteredRecordFromSignatureDto registeredRecordFromSignatureDto, boolean isModify) {
        final Intent lIntent = new Intent(context, RegisterCourseActivity.class);
        Bundle lBundle = new Bundle();
        lBundle.putString(ZSSXApplication.GOTO, go_to);
        lBundle.putSerializable(sModifyBean, registeredRecordFromSignatureDto);
        lBundle.putBoolean(sIsModify, isModify);
        lIntent.putExtras(lBundle);
        if (!(context instanceof Activity)) {
            lIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(lIntent);
    }

    public WeakReference<RegisterCourseBaseFragment> mMyBaseFragmentWeakReference;

    public void setMyBaseFragmentWeakReference(RegisterCourseBaseFragment myBaseFragment) {
        mMyBaseFragmentWeakReference = new WeakReference<>(myBaseFragment);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        initView();
        updateView();
        goToFragment();
    }

    private void goToFragment() {
        if (mGo_To.equals(ClassChooseFragment.PAGE_TAG)) {
            new ClassChooseFragment.Builder(this).display();
        } else if (mGo_To.equals(ClassDisplayFragment.PAGE_TAG)) {
            new ClassDisplayFragment.Builder(this).display();
        } else if (mGo_To.equals(RegisterDetailFragment.PAGE_TAG)) {
            new RegisterDetailFragment.Builder(this, mSignatureDto, isModify).display();
        }
    }

    private void updateView() {

    }

    private void initView() {
        isModify = getIntent().getExtras().getBoolean(sIsModify);
        mSignatureDto = (RegisteredRecordFromSignatureDto) getIntent().getExtras().getSerializable(sModifyBean);
        mGo_To = getIntent().getStringExtra(ZSSXApplication.GOTO);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolBarManager = new ToolBarManager(mToolbar, this);
        mToolBarManager.init();
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    public ToolBarManager getToolBarManager() {
        return mToolBarManager;
    }

    @Override
    public void onBackPressed() {
        RegisterCourseBaseFragment lMyBaseFragment = mMyBaseFragmentWeakReference.get();
        if (lMyBaseFragment != null && lMyBaseFragment.onBackPress()) {
            return;
        }
        closeCurrentPage();

    }

    private MutiHitHelper mMutiHitHelper;

    public Class<? extends Fragment> getCurrentLocationFragmentClass() {
        return getSupportFragmentManager().findFragmentById(RESID).getClass();
    }

    private void closeCurrentPage() {
//        if (getSupportFragmentManager().getBackStackEntryCount() == BACKSTACKENTRYCOUNT) {
//            FINISH();
//        } else {
//            super.onBackPressed();
//        }
        Class<? extends Fragment> CurrentFragmentLocation = getCurrentLocationFragmentClass();
        if (getSupportFragmentManager().getBackStackEntryCount() == 1
                | CurrentFragmentLocation.equals(ClassDisplayFragment.class)
                ) {
            if (mMutiHitHelper == null) {
                MutiHitHelper.OnMutiHitListener lMOnMutiHitListener = new MutiHitHelper.OnMutiHitListener() {
                    @Override
                    public void onMaxHit() {
                        finish();
                    }

                    @Override
                    public void onHitDoing(int i) {
                        Toast.Short(RegisterCourseActivity.this, R.string.util_toast_TipsClickAgain);
                    }
                };
                mMutiHitHelper = new MutiHitHelper(lMOnMutiHitListener);
                mMutiHitHelper.setMax_hit(1);
                mMutiHitHelper.setTimePerHit(500);
            }
            mMutiHitHelper.Hit();
        } else {
            super.onBackPressed();
        }
    }


}
