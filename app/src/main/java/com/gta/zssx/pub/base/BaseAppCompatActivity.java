package com.gta.zssx.pub.base;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.gta.zssx.AppConfiguration;
import com.gta.zssx.pub.util.FontScaleUtil;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * [Description]
 * <p>
 * [How to use]
 * <p>
 * [Tips]
 *
 * @author Created by Zhimin.Huang on 2016/6/27.
 * @since 1.0.0
 */
public class BaseAppCompatActivity extends AppCompatActivity {
    protected Context mActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppConfiguration.getInstance().addActivity(BaseAppCompatActivity.this);
        mActivity = BaseAppCompatActivity.this;
        FontScaleUtil.initFontScale(mActivity);
    }

    /**
     * 设置textView顶部图片
     * @param v           要设置的TextView
     * @param id_drawable 还要设置的图片
     */
    public void setDrawableTop(TextView v, int id_drawable) {
        Drawable topDrawable = getResources().getDrawable(id_drawable);
        topDrawable.setBounds(0, 0, topDrawable.getMinimumWidth(), topDrawable.getMinimumHeight());
        //void android.widget.TextView.setCompoundDrawables(Drawable left, Drawable top, Drawable right, Drawable bottom)
        v.setCompoundDrawables(null, topDrawable, null, null);
    }

    @Override
    public void finish() {
        AppConfiguration.getInstance().removeActivity(this);
        super.finish();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private CompositeSubscription mCompositeSubscription;

    protected CompositeSubscription getCompositeSubscription() {
        if (this.mCompositeSubscription == null) {
            this.mCompositeSubscription = new CompositeSubscription();
        }

        return this.mCompositeSubscription;
    }

    protected void addSubscription(Subscription s) {
        if (this.mCompositeSubscription == null) {
            this.mCompositeSubscription = new CompositeSubscription();
        }

        this.mCompositeSubscription.add(s);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (this.mCompositeSubscription != null) {
            this.mCompositeSubscription.unsubscribe();
        }
    }

    protected void showSoftInputOrNot(EditText mobilEditText, boolean show) {
        mobilEditText.requestFocus();
        InputMethodManager imm = (InputMethodManager) mobilEditText.getContext().getApplicationContext().getSystemService(
                Context.INPUT_METHOD_SERVICE);
        if (show) {
            imm.showSoftInput(mobilEditText, 0);
        } else {
            imm.hideSoftInputFromWindow(mobilEditText.getWindowToken(), 0);
        }
    }

    protected <VH extends View> VH getViewById(@IdRes int resId) {
        return (VH) this.findViewById(resId);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            if (isFastDoubleClick()) {
//                Toast.Short(this,"快速点击");
                return true;
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    long lastClickTime = 0;
    public boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        lastClickTime = time;
        return timeD <= 400;
    }
}
