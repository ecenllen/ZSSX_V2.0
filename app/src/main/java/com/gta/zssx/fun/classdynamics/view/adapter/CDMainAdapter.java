package com.gta.zssx.fun.classdynamics.view.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.gta.zssx.R;
import com.gta.zssx.fun.classdynamics.model.bean.ClassDynamicsBean;

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
public class CDMainAdapter extends BaseQuickAdapter<ClassDynamicsBean, BaseViewHolder> {


    public CDMainAdapter() {
        super(R.layout.item_class_dynamics_main);
    }

    @Override
    protected void convert(BaseViewHolder helper, ClassDynamicsBean item) {

    }
}
