package com.gta.zssx.pub.base;

import android.animation.ObjectAnimator;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import com.gta.utils.mvp.*;
import com.gta.zssx.R;
import com.gta.zssx.pub.base.interfaces.BaseOnNextListener;

import butterknife.Bind;
import butterknife.OnClick;

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
public abstract class NextBaseMvpActivity<V extends BaseView, P extends BasePresenter<V>> extends BaseMvpActivity<V, P> implements View.OnClickListener, BaseOnNextListener {
    @Bind(R.id.last)
    Button btnLast;
    @Bind(R.id.next)
    Button btnNext;
    @Bind(R.id.save)
    Button btnSave;
    private FrameLayout mContainer;

    @Override
    public int getLayoutId() {
        return R.layout.activity_base_next_mvp;
    }

    @Override
    protected void initView() {
        mContainer = (FrameLayout) findViewById(R.id.container);
        Fragment fragment = getFragment();
        if(fragment == null) return;
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().add(R.id.container, fragment, fragment.getTag()).commit();
    }

    public abstract Fragment getFragment();

    @OnClick({R.id.save, R.id.next, R.id.last})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.save:
                onSave();
                break;
            case R.id.next:
                onNext();
                nextAnimation();
                break;
            case R.id.last:
                onLast();
                lastAnimation();
                break;

        }

    }

    private void nextAnimation() {
        ObjectAnimator animator1 = ObjectAnimator.ofFloat(mContainer, "translationX", 0F, -1080F, 0F);
        animator1.setDuration(200);
        animator1.start();
    }

    private void lastAnimation() {
        ObjectAnimator animator1 = ObjectAnimator.ofFloat(mContainer, "translationX", 0F, 1080F, 0F);
        animator1.setDuration(200);
        animator1.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
