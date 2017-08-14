package com.gta.zssx.patrolclass.view;

import com.gta.utils.mvp.BaseView;
import com.gta.zssx.patrolclass.model.entity.PatrolRegisterBeanNew;
import com.gta.zssx.patrolclass.model.entity.RegisterInterfaceEntity;

/**
 * [Description]
 * <p>
 * [How to use]
 * <p>
 * [Tips] Created by tengfei.lv on 2016/7/19 14:01.
 */

public interface PatrolRegisterViewNew extends BaseView {
    void setFragmentLists (PatrolRegisterBeanNew patrolRegisterBeanNew);

    void addTeacherData (PatrolRegisterBeanNew.TeacherListBean mTeacherBean);

    void showDateSelectErrorDialog (String dataIsEmptyString, boolean b);

    void refreshData (RegisterInterfaceEntity entity);
}
