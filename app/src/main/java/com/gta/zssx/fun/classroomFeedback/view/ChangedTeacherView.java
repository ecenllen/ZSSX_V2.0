package com.gta.zssx.fun.classroomFeedback.view;

import com.gta.utils.mvp.BaseView;
import com.gta.zssx.patrolclass.model.entity.ChooseTeacherEntity;

import java.util.List;

/**
 * [Description]
 * <p>教师选择页面 View层接口列表
 * [How to use]
 * <p>
 * [Tips]
 *
 * @author Created by liang.lu on 2017/6/28 10:27.
 * @since 2.0.0
 */
public interface ChangedTeacherView extends BaseView {
    void showResult (List<ChooseTeacherEntity> beanList);

    void showEmpty ();
}
