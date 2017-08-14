package com.gta.zssx.fun.coursedaily.registercourse.view;

import com.gta.utils.mvp.BaseView;
import com.gta.zssx.fun.coursedaily.registercourse.model.bean.ClassChooseBean01;

import java.util.List;

/**
 * [Description]
 * <p>
 * [How to use]
 * <p>
 * [Tips]
 *
 * @author Created by Zhimin.Huang on 2016/6/15.
 * @since 1.0.0
 */
public interface ClassChooseView extends BaseView {

    void addSuccess();

    void showClass01(List<ClassChooseBean01.ClassListBean> classListBeen);

    void showEmpty();

}
