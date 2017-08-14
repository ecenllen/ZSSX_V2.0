package com.gta.zssx.patrolclass.view;

import com.gta.utils.mvp.BaseView;
import com.gta.zssx.patrolclass.model.entity.ClassChooseEntity;

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
public interface AddClassChooseView extends BaseView {
    void showSuccessDatas (List<ClassChooseEntity.ClassListBean> addPlanClassDatas);

    //    int getType ();

    void intoClassesPage ();

    void showEmpty ();

}
