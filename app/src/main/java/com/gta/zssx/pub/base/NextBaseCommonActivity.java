package com.gta.zssx.pub.base;

import android.animation.ObjectAnimator;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.gta.zssx.R;
import com.gta.zssx.pub.base.interfaces.BaseOnNextListener;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * [Description]
 * <p> 宿舍列表页/班级列表页，封装的基类，DormitoryOrClassIndexPointActivity继承
 * [How to use]
 * <p>
 * [Tips]
 *
 * @author Created by Weiye.Chen on 2017/07/24.
 * @since 2.0.0
 */
public abstract class NextBaseCommonActivity extends BaseCommonActivity implements View.OnClickListener, BaseOnNextListener {
    @Bind(R.id.tv_previous_section)
    public TextView btnLast;
    @Bind(R.id.tv_next_section)
    public TextView btnNext;
    @Bind(R.id.tv_save)
    public TextView btnSave;
    private FrameLayout mContainer;

    @Override
    public int getLayoutId() {
        return R.layout.activity_base_next_common;
    }

    @Override
    protected void initView() {
        mContainer = (FrameLayout) findViewById(R.id.container);
        Fragment fragment = getFragment();
        if(fragment == null){
            throw new NullPointerException("getFragment can not be null!");
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().add(R.id.container, fragment, fragment.getTag()).commit();
    }

    public abstract Fragment getFragment();

    @OnClick({R.id.tv_save, R.id.tv_next_section, R.id.tv_previous_section})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_save:
                onSave();
                break;
            case R.id.tv_next_section:
                onNext();
//                nextAnimation();
                break;
            case R.id.tv_previous_section:
                onLast();
//                lastAnimation();
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
