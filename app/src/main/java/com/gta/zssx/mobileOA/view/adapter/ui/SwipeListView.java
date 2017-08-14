package com.gta.zssx.mobileOA.view.adapter.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.ListView;

/**
 * Created by lan.zheng on 2016/11/5.
 */
public class SwipeListView extends ListView implements GestureDetector.OnGestureListener{
    public SwipeListView(Context context) {
        super(context);
    }

    public SwipeListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SwipeListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    // 滑动距离及坐标,不让滑动事件覆盖点击事件
    private float xDistance, yDistance, xLast, yLast;
    private boolean isMove = true;
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
// TODO Auto-generated method stub
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                xDistance = yDistance = 0f;
                xLast = ev.getX();
                yLast = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                final float curX = ev.getX();
                final float curY = ev.getY();

                xDistance += Math.abs(curX - xLast);
                yDistance += Math.abs(curY - yLast);
                xLast = curX;
                yLast = curY;

                if(xDistance > 5){
                    isMove = true;
                }else {
                    isMove = false;  //让子类处理
                }
                break;
            case MotionEvent.ACTION_UP:
                if(isMove){  //true的父类处理
                    return super.onInterceptTouchEvent(ev);
                }else {
                    return false;
                }
        }

        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }
}
