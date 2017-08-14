package com.gta.zssx.fun.coursedaily.registerrecord.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ExpandableListView;

/**
 * Created by lan.zheng on 2016/6/25.
 */
public class MyExpandListView extends ExpandableListView{
    public MyExpandListView(Context context) {
        super(context);
    }

    public MyExpandListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyExpandListView(Context context, AttributeSet attrs,
                                 int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    /**
     * 重写该方法，达到使ListView适应ScrollView的效果
     */
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }

    @Override
    public boolean expandGroup(int groupPos) {
        return super.expandGroup(groupPos);
    }

    @Override
    public boolean setSelectedChild(int groupPosition, int childPosition, boolean shouldExpandGroup) {
        return super.setSelectedChild(groupPosition, childPosition, shouldExpandGroup);
    }
}
