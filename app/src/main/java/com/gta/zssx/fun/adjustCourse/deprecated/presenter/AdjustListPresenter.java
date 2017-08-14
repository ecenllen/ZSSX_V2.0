package com.gta.zssx.fun.adjustCourse.deprecated.presenter;

import android.content.Context;

import com.gta.utils.mvp.BasePresenter;
import com.gta.zssx.fun.adjustCourse.deprecated.view.AdjustMainView;

/**
 * [Description]
 * <p>
 * [How to use]
 * <p>
 * [Tips]
 *
 * @author Created by Zhimin.Huang on 2016/10/20.
 * @since 1.0.0
 */
public class AdjustListPresenter extends BasePresenter<AdjustMainView> {

    private Context mContext;

    public AdjustListPresenter(Context context) {
        mContext = context;
    }
}
