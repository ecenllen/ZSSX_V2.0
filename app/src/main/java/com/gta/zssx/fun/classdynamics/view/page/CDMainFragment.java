package com.gta.zssx.fun.classdynamics.view.page;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.gta.zssx.fun.adjustCourse.view.base.BaseRecyclerViewFragment;
import com.gta.zssx.fun.classdynamics.presenter.CDMainPresenter;
import com.gta.zssx.fun.classdynamics.view.CDMainView;
import com.gta.zssx.fun.classdynamics.view.adapter.CDMainAdapter;
import com.gta.zssx.fun.coursedaily.registercourse.model.bean.ClassDisplayBean;

/**
 * [Description]
 * <p>
 * [How to use]
 * <p>
 * [Tips]
 *
 * @author Created by Zhimin.Huang on 2017/3/20.
 * @since 1.0.0
 */
public class CDMainFragment extends BaseRecyclerViewFragment<CDMainView, CDMainPresenter, ClassDisplayBean> implements CDMainView {


    public static CDMainFragment newInstance() {
        Bundle args = new Bundle();
        CDMainFragment fragment = new CDMainFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected BaseQuickAdapter getRecyclerAdapter() {
        return new CDMainAdapter();
    }

    @NonNull
    @Override
    public CDMainPresenter createPresenter() {
        return new CDMainPresenter();
    }

    @Override
    protected void requestData() {
        super.requestData();

    }

    @Override
    protected void itemClick(ClassDisplayBean classDisplayBean, int position) {
        super.itemClick(classDisplayBean, position);
    }
}
