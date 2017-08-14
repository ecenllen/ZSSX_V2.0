package com.gta.zssx.fun.adjustCourse.model.nav;

import android.content.Context;
import android.graphics.RectF;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.gta.zssx.R;
import com.gta.zssx.fun.adjustCourse.model.bean.NoticeBean;
import com.gta.zssx.fun.adjustCourse.model.utils.NoticeManager;
import com.gta.zssx.fun.adjustCourse.view.base.Constant;
import com.gta.zssx.fun.adjustCourse.view.page.v2.ConfirmFragment;
import com.gta.zssx.fun.adjustCourse.view.page.v2.MyApplyFragment;
import com.gta.zssx.fun.adjustCourse.view.page.v2.ReviewFragment;
import com.gta.zssx.pub.base.BaseCommonFragment;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;



/**
 * [Description]
 * <p>
 * [How to use]
 * <p>
 * [Tips]
 *
 * @author Created by Zhimin.Huang on 2017/3/15.
 * @since 1.0.0
 */
public class NavFragment extends BaseCommonFragment implements View.OnClickListener, NoticeManager.NoticeNotify {

    @Bind(R.id.nav_my_apply)
    NavigationButton mNavMe;
    @Bind(R.id.nav_confirm_apply)
    NavigationButton mNavConfirm;
    @Bind(R.id.nav_review_apply)
    NavigationButton mNavReview;
    private NavigationButton mCurrentNavButton;
    private FragmentManager mFragmentManager;
    private int mContainerId;
    private OnNavigationReselectListener mOnNavigationReselectListener;
    private Context mContext;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_nav;
    }

    public NavFragment() {
    }

    @Override
    protected void initView(View view) {
        ShapeDrawable lineDrawable = new ShapeDrawable(new BorderShape(new RectF(0, 1, 0, 0)));
        lineDrawable.getPaint().setColor(getResources().getColor(R.color.divide_line));
        LayerDrawable layerDrawable = new LayerDrawable(new Drawable[]{
                new ColorDrawable(getResources().getColor(R.color.white)),
                lineDrawable
        });
        view.setBackgroundDrawable(layerDrawable);

        mNavMe.init(R.drawable.tab_icon_new, R.string.main_tab_name_me, MyApplyFragment.class, 1);
        mNavConfirm.init(R.drawable.tab_icon_confirm, R.string.main_tab_name_confirm, ConfirmFragment.class, 2);
        mNavReview.init(R.drawable.tab_icon_audit, R.string.main_tab_name_review, ReviewFragment.class, 3);

        NoticeManager.bindNotify(this);
    }

    @OnClick({R.id.nav_my_apply, R.id.nav_confirm_apply, R.id.nav_review_apply})
    @Override
    public void onClick(View v) {

        if (v instanceof NavigationButton) {
            NavigationButton nav = (NavigationButton) v;
            doSelect(nav);
        }
    }

    private void doSelect(NavigationButton newNavButton) {


        NavigationButton oldNavButton = null;
        if (mCurrentNavButton != null) {
            oldNavButton = mCurrentNavButton;
            if (oldNavButton == newNavButton) {
                onReselect(oldNavButton);
                return;
            }
            oldNavButton.setSelected(false);
        }
        newNavButton.setSelected(true);
        doTabChanged(oldNavButton, newNavButton);
        mOnNavigationReselectListener.onSelect(newNavButton);
        mCurrentNavButton = newNavButton;
    }

    public void setup(Context context, FragmentManager fragmentManager, int contentId, OnNavigationReselectListener listener) {
        mContext = context;
        mFragmentManager = fragmentManager;
        mContainerId = contentId;
        mOnNavigationReselectListener = listener;

        // do clear
        clearOldFragment();
        // do select first
        doSelect(mNavMe);
    }

    private void clearOldFragment() {
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        List<Fragment> fragments = mFragmentManager.getFragments();
        if (transaction == null || fragments == null || fragments.size() == 0)
            return;
        boolean doCommit = false;
        for (Fragment fragment : fragments) {
            if (fragment != this) {
                transaction.remove(fragment);
                doCommit = true;
            }
        }
        if (doCommit)
            transaction.commitNow();
    }


    private void doTabChanged(NavigationButton oldNavButton, NavigationButton newNavButton) {
        FragmentTransaction ft = mFragmentManager.beginTransaction();
        if (oldNavButton != null) {
            if (oldNavButton.getFragment() != null) {
                ft.detach(oldNavButton.getFragment());
            }
        }
        if (newNavButton != null) {
            if (newNavButton.getFragment() == null) {
                Fragment fragment = Fragment.instantiate(mContext,
                        newNavButton.getClx().getName(), null);
                ft.add(mContainerId, fragment, newNavButton.getTag());
                newNavButton.setFragment(fragment);
            } else {
                ft.attach(newNavButton.getFragment());
            }
        }
        ft.commit();
    }

    public void onReselect(NavigationButton navigationButton) {
        OnNavigationReselectListener listener = mOnNavigationReselectListener;
        if (listener != null) {
            listener.onReselect(navigationButton);
        }
    }

    @Override
    public void onNoticeArrived(NoticeBean bean) {
        if (bean.getIsAudit() == Constant.HAS_AUDIT) {
            if (bean.getIsHasRightAudit() == Constant.HAS_AUDIT) {
                mNavReview.setVisibility(View.VISIBLE);
            } else {
                mNavReview.setVisibility(View.GONE);
            }
        } else {
            mNavReview.setVisibility(View.GONE);
        }
        mNavConfirm.showRedDot(bean.getWaitConfirmNumber());
        mNavReview.showRedDot(bean.getWaitAuditNumber());
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        NoticeManager.unBindNotify(this);
    }

    public interface OnNavigationReselectListener {
        void onReselect(NavigationButton navigationButton);

        void onSelect(NavigationButton navigationButton);
    }
}
