package com.gta.zssx.fun.adjustCourse.view.page.v2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.widget.FrameLayout;

import com.gta.zssx.R;
import com.gta.zssx.fun.adjustCourse.model.nav.NavFragment;
import com.gta.zssx.fun.adjustCourse.model.nav.NavigationButton;
import com.gta.zssx.fun.adjustCourse.model.nav.OnTabReselectListener;
import com.gta.zssx.fun.adjustCourse.presenter.AdjustMainPresenter;
import com.gta.zssx.fun.adjustCourse.view.AdjustMainViewV2;
import com.gta.zssx.pub.base.BaseMvpActivity;

import butterknife.Bind;

/**
 * [Description]
 * <p>
 * [How to use]
 * <p>
 * [Tips]
 *
 * @author Created by Zhimin.Huang on 2017/3/6.
 * @since 1.0.0
 */
public class AdjustMvpMainActivityV2 extends BaseMvpActivity<AdjustMainViewV2, AdjustMainPresenter>
        implements AdjustMainViewV2, NavFragment.OnNavigationReselectListener {

    @Bind(R.id.adjust_main_container)
    FrameLayout mFrameLayout;


    public static void start(Context context) {
        final Intent lIntent = new Intent(context, AdjustMvpMainActivityV2.class);

        if (!(context instanceof Activity)) {
            lIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(lIntent);
    }



    @Override
    public int getLayoutId() {
        return R.layout.activity_adjust_main_v2;
    }

    @Override
    protected void initView() {

        mToolBarManager.setTitle(getString(R.string.main_tab_name_me))
                .showRightImage(true)
                .setRightImage(R.drawable.plus_sign)
                .clickRightImage(v -> CourseApplyActivityV2.start(mActivity, 0, null, null, 1));

        FragmentManager manager = getSupportFragmentManager();
        NavFragment lNavBar = ((NavFragment) manager.findFragmentById(R.id.fag_nav));
        lNavBar.setup(this, manager, R.id.adjust_main_container, this);
    }

    @Override
    protected void initData() {

    }

    @NonNull
    @Override
    public AdjustMainPresenter createPresenter() {
        return new AdjustMainPresenter();
    }

    @Override
    public void onReselect(NavigationButton navigationButton) {
        Fragment fragment = navigationButton.getFragment();
        if (fragment != null
                && fragment instanceof OnTabReselectListener) {
            OnTabReselectListener listener = (OnTabReselectListener) fragment;
            listener.onTabReselect();
        }
    }

    @Override
    public void onSelect(NavigationButton navigationButton) {
        switch (navigationButton.getNumber()) {
            default:
            case 1:
                mToolBarManager.setTitle(getString(R.string.main_tab_name_me))
                        .showRightImage(true)
                        .setRightImage(R.drawable.plus_sign)
                        .clickRightImage(v -> CourseApplyActivityV2.start(mActivity, 0, null, null, 1));
                break;
            case 2:
                mToolBarManager.setTitle(getString(R.string.main_tab_name_confirm))
                        .showRightImage(false);
                break;
            case 3:
                mToolBarManager.setTitle(getString(R.string.main_tab_name_review))
                        .showRightImage(false);
                break;
        }
    }
}
