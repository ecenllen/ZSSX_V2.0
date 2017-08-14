package com.gta.zssx.patrolclass.popup;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.AnticipateOvershootInterpolator;

/**
 * [Description]
 * <p>
 * [How to use]
 * <p>
 * [Tips]
 *
 * @author Created by Zhimin.Huang on 2016/11/24.
 * @since 1.0.0
 */
public class CustomViewPager extends ViewPager {
    private String TAG = "CustomViewPager";
    private boolean isFirst;

    public CustomViewPager(Context context) {
        super(context);
        isFirst = true;
    }

    public CustomViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        isFirst = true;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (isFirst) {
            ObjectAnimator lObjectAnimatorY = ObjectAnimator.ofFloat(this, "translationY", canvas.getHeight(), 0);
            lObjectAnimatorY.setRepeatMode(ValueAnimator.REVERSE);
            lObjectAnimatorY.setInterpolator(new AnticipateOvershootInterpolator(0.6f));
            lObjectAnimatorY.setDuration(700);
            lObjectAnimatorY.start();
            isFirst = false;
        }

    }

}
