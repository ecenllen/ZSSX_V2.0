package com.gta.zssx.fun.coursedaily.registercourse.view.adapter;

import android.content.Context;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.gta.zssx.R;
import com.gta.zssx.fun.coursedaily.registercourse.model.bean.ClassDisplayBean;

import java.util.List;

/**
 * [Description]
 * <p>
 * [How to use]
 * <p>
 * [Tips]
 *
 * @author Created by Zhimin.Huang on 2016/8/4.
 * @since 1.0.0
 */
public class TestQuickAdapter extends BaseQuickAdapter<ClassDisplayBean, BaseViewHolder> {


    public TestQuickAdapter(List data) {
        super(data);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, ClassDisplayBean classDisplayBean) {

    }


}
