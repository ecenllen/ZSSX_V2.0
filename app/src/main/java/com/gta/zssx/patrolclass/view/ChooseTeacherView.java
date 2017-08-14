package com.gta.zssx.patrolclass.view;

import com.gta.utils.mvp.BaseView;
import com.gta.zssx.patrolclass.model.entity.ChooseTeacherEntity;

import java.util.List;

/**
 * Created by liang.lu on 2016/8/16 14:53.
 */
public interface ChooseTeacherView extends BaseView {
    void showResult(List<ChooseTeacherEntity> entities);
    String getDeptId();
}
