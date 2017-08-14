package com.gta.zssx.pub.widget;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by lan.zheng on 2017/6/26.
 *
 */
public class FixedScrollRecyclerView extends RecyclerView {

    public FixedScrollRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FixedScrollRecyclerView(Context context) {
        super(context);
    }

    public FixedScrollRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(
                Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
