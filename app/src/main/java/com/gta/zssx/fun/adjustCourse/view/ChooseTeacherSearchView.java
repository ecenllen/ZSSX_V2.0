package com.gta.zssx.fun.adjustCourse.view;

import com.gta.utils.mvp.BaseView;
import com.gta.zssx.fun.adjustCourse.model.bean.ChooseTeacherSearchEntity;

import java.util.List;

/**
 * Created by liang.lu on 2016/8/17 14:46.
 */
public interface ChooseTeacherSearchView extends BaseView {
    void showResult(List<ChooseTeacherSearchEntity> entities);

    void showEmpty();
}
