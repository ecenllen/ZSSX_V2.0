/**
 * =================================
 * 版权所属：卡荟（中国）有限责任公司
 *
 * @author Woode Wang
 * E-mail:wangwoode@qq.com
 * tel：18718575523
 * @version 创建时间：2015-3-17 下午4:42:12
 * ==================================
 */
package com.gta.utils.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.example.gtalutils.R;

/**
 * @param <T>
 * @description <b>【功能】</b><br>
 * <p/>
 * <p/>
 * <b>【使用方法】</b><br>
 * <p/>
 * <b>【注】</b><br>
 * <p/>
 * <p/>
 * @since 1.0.0
 */
public abstract class FragmentBuilder<T extends Fragment> {

    public static final String WARM_NEED_TO_IN_FRAGMENTACTIVITY = "啓動該頁的fragment所處的activity必須是FragmentActivity類型";

    private static final int ANIM_LEFT_IN_RIGHT_OUT = 0;
    private static final int ANIM_DOWN_IN_UP_OUT = 1;
    private static final int ANIM_NONE = 3;
    public static final String KEY_EX_PAGETAG = "KEY_EX_PAGETAG";
    public static final String KEY_CURRENT_PAGETAG = "KEY_CURRENT_PAGETAG";
    public static final String KEY_IFLOG = "KEY_IFLOG";
    public static final String KEY_ASPAGE = "KEY_ASPAGE";
    protected FragmentActivity containerActivity;
    protected String exPageTag;
    protected boolean ifLog = false;
    protected boolean asPage = true;
    private boolean reLoad;
    private boolean ifClearFragmentStack;
    private int animType = ANIM_LEFT_IN_RIGHT_OUT;
    private int containerResID;
    protected FragmentManager supportFragmentManager;

    private boolean backIfExistInstack;

    private Fragment targetFragment;

    private int requestCode;

    /**
     * 是否添加到栈，默认为true
     */
    private boolean addToBackStack = true;
    /**
     * 回退到该标志的fragment前面，默认为无
     */
    private String backBeforeFragmenTag = null;

    private Fragment mFragment;

    public FragmentBuilder(Context context) {
        super();
        setContext(context);
    }

    protected FragmentBuilder() {
    }

    public FragmentBuilder<T> setContext(Context context) {
        if (context instanceof FragmentActivity) {
            this.containerActivity = (FragmentActivity) context;
            supportFragmentManager = containerActivity.getSupportFragmentManager();
        } else {
            throw new ClassCastException(WARM_NEED_TO_IN_FRAGMENTACTIVITY);
        }
        return this;
    }

    public FragmentBuilder<T> addToBackStackOrNot(boolean addToBackStack) {
        this.addToBackStack = addToBackStack;
        return this;
    }

    public FragmentBuilder<T> setContainerResID(int containerResID) {
        this.containerResID = containerResID;
        return this;
    }

    public FragmentBuilder<T> setAnimType(int animType) {
        this.animType = animType;
        return this;
    }

    public FragmentBuilder<T> setAnimType_LeftInRightOut() {
        setAnimType(ANIM_LEFT_IN_RIGHT_OUT);
        return this;
    }

    public FragmentBuilder<T> setExPageTag(String exPageTag) {
        this.exPageTag = exPageTag;
        return this;
    }

    public FragmentBuilder<T> setDebugMode() {
        this.ifLog = true;
        return this;
    }

    public FragmentBuilder<T> setIfLog(boolean ifLog) {
        this.ifLog = ifLog;
        return this;
    }

    public FragmentBuilder<T> setAsPage(boolean asPage) {
        this.asPage = asPage;
        return this;
    }

    public FragmentBuilder<T> setCurrentFragment(Fragment fragment) {
        mFragment = fragment;
        return this;
    }

    /**
     * 添加此前置方法会：clear the fragment stack before display target fragment
     *
     * @return
     */
    public FragmentBuilder<T> clearFragmentStack() {
        ifClearFragmentStack = true;
        return this;
    }

    public FragmentBuilder<T> reload() {
        reLoad = true;
        return this;
    }

    /**
     * 回退到fragmentTag标志的fragment前面
     *
     * @param fragmentTag
     * @return
     */
    public FragmentBuilder<T> backBefore(String fragmentTag) {
        backBeforeFragmenTag = fragmentTag;
        return this;
    }

    public FragmentBuilder<T> backIfExistInstack() {
        backIfExistInstack = true;
        return this;
    }

    public FragmentBuilder<T> setTargetFragment(Fragment targetFragment, int requestCode) {
        this.targetFragment = targetFragment;
        this.requestCode = requestCode;
        return this;
    }

    public void initArgs(Bundle args) {
    }

    public void add2View(View containView) {
        add2View(containView.getId());
    }

    public void add2View(int viewId) {
        add2View(viewId, null);
    }

    public void add2View(View containView, FragmentManager fragmentManager) {
        add2View(containView.getId(), fragmentManager);
    }

    public void add2View(int viewId, FragmentManager fragmentManager) {
        Fragment fragment = create();

        if (fragmentManager == null) fragmentManager = supportFragmentManager;
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        /* 设置动画，默认没有动画 */
        switch (animType) {
            case ANIM_LEFT_IN_RIGHT_OUT:
                transaction.setCustomAnimations(R.anim.push_left_in, R.anim.push_left_out, R.anim.back_left_in, R.anim.back_right_out);
                break;
            case ANIM_DOWN_IN_UP_OUT:
                transaction.setCustomAnimations(R.anim.slide_in_from_bottom, R.anim
                        .slide_out_to_top);
                break;
            case ANIM_NONE:
            default:
                break;
        }
        /* 执行跳转 */
        transaction.replace(viewId, fragment, getPageTag());
        transaction.commit();
    }

    public void switchFragment(Fragment from, String fromTag, String toTag) {
        FragmentTransaction lTransaction = supportFragmentManager.beginTransaction();

        Fragment to = supportFragmentManager.findFragmentByTag(toTag);
        if (to == null) {
            to = createFragment();
        }
        /* 设置填充的container对象，默认为R.id.content_frame */
        if (containerResID == 0) containerResID = getContainerResID();
        if (mFragment != to) {
            mFragment = to;
            if (from != null && to != null) {
                if (!to.isAdded()) {
                    lTransaction.hide(from).add(containerResID, to, toTag).commit();
                } else {
                    lTransaction.hide(from).show(to).commit();
                }
            }
        }
    }


    public void display() {

		/* 回退到backBeforeFragmenTag标志的fragment前面 */
        if (backBeforeFragmenTag != null) {
            supportFragmentManager.popBackStack(backBeforeFragmenTag, 0);
        }
		/* 清空fragment堆栈，默认不执行,优先级第一 */
        if (ifClearFragmentStack) {
            supportFragmentManager.popBackStack(0, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        } else {
			/* 返回到栈中对应标志的fragment前一个位置以重现加载该fragment，默认不执行，优先级第二 */
            if (reLoad) {
                supportFragmentManager.popBackStack(getPageTag(), FragmentManager
                        .POP_BACK_STACK_INCLUSIVE);
            } else {
				/* 返回到栈中对应标志的fragment，如果栈中存在的话，否则依然创建，默认不执行，优先级第三 */
                if (backIfExistInstack & isExistInStack()) {
                    supportFragmentManager.popBackStack(getPageTag(), 0);
                    return;
                } else {

                }
            }
        }

        Fragment fragment = createFragment();

        if (targetFragment != null) fragment.setTargetFragment(targetFragment, requestCode);

        FragmentTransaction transaction = supportFragmentManager.beginTransaction();
		/* 设置动画，默认没有动画 */
        switch (animType) {
            case ANIM_LEFT_IN_RIGHT_OUT:
                transaction.setCustomAnimations(R.anim.animation_in, R.anim.animation_out, R.anim.back_left_in, R.anim.back_right_out);
                break;
            case ANIM_DOWN_IN_UP_OUT:
                transaction.setCustomAnimations(R.anim.slide_in_from_bottom, R.anim.slide_out_to_top);
                break;
            case ANIM_NONE:
            default:
                break;
        }
		/* 设置填充的container对象，默认为R.id.content_frame */
        if (containerResID == 0) containerResID = getContainerResID();
		/* 执行跳转 */
        transaction.replace(containerResID, fragment, getPageTag());
        if (addToBackStack) transaction.addToBackStack(getPageTag());
        transaction.commit();
    }

    @NonNull
    private Fragment createFragment() {
        Fragment fragment = create();
        Bundle args = fragment.getArguments();
        args = args == null ? new Bundle() : args;
        args.putString(KEY_EX_PAGETAG, exPageTag);
        args.putString(KEY_CURRENT_PAGETAG, getPageTag());
        args.putBoolean(KEY_IFLOG, ifLog);
        args.putBoolean(KEY_ASPAGE, asPage);
        fragment.setArguments(args);
        return fragment;
    }

    protected boolean isExistInStack() {
        Fragment findFragmentByTag = supportFragmentManager.findFragmentByTag(getPageTag());
        return findFragmentByTag != null;
    }

    public abstract T create();

    protected abstract int getContainerResID();

    public abstract String getPageTag();

    protected Context getContext() {
        return containerActivity;
    }

}
