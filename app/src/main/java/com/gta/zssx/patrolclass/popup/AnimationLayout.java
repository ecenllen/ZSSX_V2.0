package com.gta.zssx.patrolclass.popup;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gta.zssx.R;

/**
 * [Description]
 * <p>
 * [How to use]
 * <p>
 * [Tips]
 *
 * @author Created by Zhimin.Huang on 2016/12/30.
 * @since 1.0.0
 */
public class AnimationLayout extends LinearLayout {

    public ImageView mImageView;
    public TextView mTextView;
    private String TAG = "AnimationLayout";
    private int mRawX;
    private int mRawY;
    private float mLastMotionX;
    private float mLastMotionY;

    public AnimationLayout(Context context) {
        this(context, null);
    }

    public AnimationLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AnimationLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        View lView = LayoutInflater.from(context).inflate(R.layout.view_animation_layout, this);
        mImageView = (ImageView) lView.findViewById(R.id.imageView);
        mTextView = (TextView) lView.findViewById(R.id.textView);

        initView(context);
    }

    private boolean isEnlarge;

    private void initView(Context context) {
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
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
                    shrink();
                    getParent().requestDisallowInterceptTouchEvent(false);
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

    private void shrink() {

        PropertyValuesHolder pvhX = PropertyValuesHolder.ofFloat("scaleY", 1.3f, 1.0f);
        PropertyValuesHolder pvhY = PropertyValuesHolder.ofFloat("scaleX", 1.3f, 1.0f);
        ObjectAnimator lObjectAnimator = ObjectAnimator.ofPropertyValuesHolder(this, pvhX, pvhY).setDuration(100);
        lObjectAnimator.setRepeatMode(ValueAnimator.REVERSE);
        lObjectAnimator.start();
    }

    private void enlarge() {
        PropertyValuesHolder pvhX = PropertyValuesHolder.ofFloat("scaleY", 1.0f, 1.3f);
        PropertyValuesHolder pvhY = PropertyValuesHolder.ofFloat("scaleX", 1.0f, 1.3f);
        ObjectAnimator lObjectAnimator = ObjectAnimator.ofPropertyValuesHolder(this, pvhX, pvhY).setDuration(100);
        lObjectAnimator.setRepeatMode(ValueAnimator.REVERSE);
        lObjectAnimator.start();
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

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int size = MeasureSpec.getSize(widthMeasureSpec);

    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean isIntercept = false;
        float evY = ev.getY();
        float evX = ev.getX();
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isIntercept = false;
                break;
            case MotionEvent.ACTION_MOVE:
                if (isNeedToIntercept(ev)) {
                    isIntercept = true;
                } else {
                    isIntercept = false;
                }
                break;
            case MotionEvent.ACTION_UP:
                isIntercept = false;
                break;
        }
        mLastMotionX = evX;
        mLastMotionY = evY;
        return isIntercept;
    }

    private boolean isNeedToIntercept(MotionEvent ev) {
        if (Math.abs(ev.getX() - mLastMotionX) > Math.abs(ev.getY() - mLastMotionY)) {
            return true;
        } else {
            return false;
        }
    }
}
