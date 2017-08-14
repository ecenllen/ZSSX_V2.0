package com.gta.zssx.patrolclass.view;

import com.gta.utils.mvp.BaseView;
import com.gta.zssx.patrolclass.model.entity.AddClassEntity;
import com.gta.zssx.patrolclass.model.entity.PlanClassSearchEntity;

import java.util.List;

/**
 * Created by liang.lu on 2016/8/23 14:03.
 */
public interface PlanClassSearchView extends BaseView {
    void showResult (List<PlanClassSearchEntity> entities);

    String getKeyWord ();

    List<AddClassEntity> getEntity ();

    void intoClassesPage ();

}
