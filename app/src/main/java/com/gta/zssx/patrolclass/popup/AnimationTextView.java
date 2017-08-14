package com.gta.zssx.patrolclass.popup;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.TextView;

/**
 * [Description]
 * <p>
 * [How to use]
 * <p>
 * [Tips]
 *
 * @author Created by Zhimin.Huang on 2016/11/14.
 * @since 1.0.0
 */
public class AnimationTextView extends TextView {

    private String TAG = "AnimationTextView";
    private boolean isEnlarge;
    public int mRawX;
    public int mRawY;

    public AnimationTextView(Context context) {
        super(context);

    }

    public AnimationTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AnimationTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int lAction = event.getAction();

        switch (lAction) {
            case MotionEvent.ACTION_DOWN:
                mRawX = (int) event.getRawX();
                mRawY = (int) event.getRawY();
                getParent().requestDisallowInterceptTouchEvent(true);
                isEnlarge = true;
                enlarge();
                break;
            case MotionEvent.ACTION_UP:
                if (isEnlarge) {
                    getParent().requestDisallowInterceptTouchEvent(false);
                    shrink();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                int lRawX = (int) event.getRawX();
                int lRawY = (int) event.getRawY();
                int lScaledTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
                if (Math.max(Math.abs(mRawX - lRawX), Math.abs(mRawY - lRawY)) >= lScaledTouchSlop) {
                    if (!isInChangeImageZone(this, (int) event.getRawX(), (int) event.getRawY())) {
                        if (isEnlarge) {
                            shrink();
                            isEnlarge = false;
                            getParent().requestDisallowInterceptTouchEvent(false);
                        }
                    } else {
                        if (!isEnlarge) {
                            enlarge();
                            isEnlarge = true;
                        }
                    }
                }
                Log.i(TAG, "onTouchEvent: " + event.getX());
                Log.i(TAG, "onTouchEvent: " + event.getY());


                break;

            default:
        }

        return super.onTouchEvent(event);

    }

    private Rect mChangeImageBackgroundRect = null;

    private boolean isInChangeImageZone(View view, int x, int y) {
        if (null == mChangeImageBackgroundRect) {
            mChangeImageBackgroundRect = new Rect();
        }
        view.getDrawingRect(mChangeImageBackgroundRect);
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        mChangeImageBackgroundRect.left = location[0];
        mChangeImageBackgroundRect.top = location[1];
        mChangeImageBackgroundRect.right = mChangeImageBackgroundRect.left + view.getWidth();
        mChangeImageBackgroundRect.bottom = mChangeImageBackgroundRect.top + view.getHeight();
        return mChangeImageBackgroundRect.contains(x, y);
    }


    private void enlarge() {
//        ObjectAnimator lObjectAnimatorY = ObjectAnimator.ofFloat(this, "scaleY", 1.0f, 1.3f);
//        lObjectAnimatorY.setRepeatMode(ValueAnimator.REVERSE);
//        lObjectAnimatorY.setDuration(200);
//        lObjectAnimatorY.start();
//
//        ObjectAnimator lObjectAnimatorX = ObjectAnimator.ofFloat(this, "scaleX", 1.0f, 1.3f);
//        lObjectAnimatorX.setRepeatMode(ValueAnimator.REVERSE);
//        lObjectAnimatorX.setDuration(200);
//        lObjectAnimatorX.start();
        PropertyValuesHolder pvhX = PropertyValuesHolder.ofFloat("scaleY", 1.0f, 1.3f);
        PropertyValuesHolder pvhY = PropertyValuesHolder.ofFloat("scaleX", 1.0f, 1.3f);
        ObjectAnimator lObjectAnimator = ObjectAnimator.ofPropertyValuesHolder(this, pvhX, pvhY).setDuration(100);
        lObjectAnimator.setRepeatMode(ValueAnimator.REVERSE);
        lObjectAnimator.start();
    }

    private void shrink() {

        PropertyValuesHolder pvhX = PropertyValuesHolder.ofFloat("scaleY", 1.3f, 1.0f);
        PropertyValuesHolder pvhY = PropertyValuesHolder.ofFloat("scaleX", 1.3f, 1.0f);
        ObjectAnimator lObjectAnimator = ObjectAnimator.ofPropertyValuesHolder(this, pvhX, pvhY).setDuration(100);
        lObjectAnimator.setRepeatMode(ValueAnimator.REVERSE);
        lObjectAnimator.start();

//        ObjectAnimator lObjectAnimator1Y = ObjectAnimator.ofFloat(this, "scaleY", 1.3f, 1.0f);
//        lObjectAnimator1Y.setRepeatMode(ValueAnimator.REVERSE);
//        lObjectAnimator1Y.setDuration(200);
//        lObjectAnimator1Y.start();
//
//        ObjectAnimator lObjectAnimator1X = ObjectAnimator.ofFloat(this, "scaleX", 1.3f, 1.0f);
//        lObjectAnimator1X.setRepeatMode(ValueAnimator.REVERSE);
//        lObjectAnimator1X.setDuration(200);
//        lObjectAnimator1X.start();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
//        getParent().requestDisallowInterceptTouchEvent(true);
        return super.dispatchTouchEvent(ev);
    }
}
