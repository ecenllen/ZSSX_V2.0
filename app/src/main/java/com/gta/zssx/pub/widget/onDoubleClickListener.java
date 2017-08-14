package com.gta.zssx.pub.widget;

import android.view.MotionEvent;
import android.view.View;

/**
 * [Description]
 * <p>
 * [How to use]
 * <p>
 * [Tips]
 *
 * @author Created by Zhimin.Huang on 2016/9/9.
 * @since 1.0.0
 */
public class onDoubleClickListener implements View.OnTouchListener {

    int count = 0;
    int firClick = 0;
    int secClick = 0;

    private onDoubleClick mOnDoubleClick;

    public onDoubleClickListener(onDoubleClick onDoubleClick) {
        mOnDoubleClick = onDoubleClick;
    }

    public interface onDoubleClick {
        void onDouble();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (MotionEvent.ACTION_DOWN == event.getAction()) {
            count++;
            if (count == 1) {
                firClick = (int) System.currentTimeMillis();

            } else if (count == 2) {
                secClick = (int) System.currentTimeMillis();
                if (secClick - firClick < 1500) {
                    //双击事件
                    mOnDoubleClick.onDouble();
                }
                count = 0;
                firClick = 0;
                secClick = 0;
            }
        }
        return true;
    }

}
