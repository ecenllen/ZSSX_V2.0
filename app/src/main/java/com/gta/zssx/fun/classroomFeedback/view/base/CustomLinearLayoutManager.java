package com.gta.zssx.fun.classroomFeedback.view.base;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;

/**
 * [Description]
 * <p>
 * [How to use]
 * <p>
 * [Tips]
 *
 * @author Created by liang.lu on 2017/7/26 11:02.
 * @since 2.0.0
 */


public class CustomLinearLayoutManager extends LinearLayoutManager {

    private boolean isScrollEnabled = true;

    public CustomLinearLayoutManager (Context context) {
        super (context);
    }

    public void setScrollEnabled (boolean flag) {
        this.isScrollEnabled = flag;
    }

    @Override
    public boolean canScrollVertically () {
        return isScrollEnabled && super.canScrollVertically ();
    }
}
