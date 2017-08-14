package com.gta.zssx.patrolclass.view;

import com.gta.utils.mvp.BaseView;
import com.gta.zssx.patrolclass.model.entity.PlanPatrolResultEntity;

import java.util.List;

/**
 * [Description]
 * <p>
 * [How to use]
 * <p>
 * [Tips] Created by tengfei.lv on 2016/7/14 17:01.
 */

public interface PlanPatrolResultView extends BaseView {

    void showSuccessData (List<PlanPatrolResultEntity> entityList);

    void DeleteClassItemSuccess ();

    int getDeptId ();

    int getClassId ();

    void startAddClassPage ();
}
