package com.gta.zssx.fun.coursedaily.registercourse.view;

import com.gta.utils.mvp.BaseView;
import com.gta.zssx.fun.coursedaily.registercourse.model.bean.ClassDisplayBean;
import com.gta.zssx.fun.personalcenter.model.bean.UserBean;

import java.util.List;

/**
 * [Description]
 * <p>
 * [How to use]
 * <p>
 * [Tips]
 *
 * @author Created by Zhimin.Huang on 2016/7/4.
 * @since 1.0.0
 */
public interface CourseDailyView extends BaseView {

    void showResult(List<ClassDisplayBean> classDisplayBeen);

    void emptyUI(boolean isEmpty);


    void isTeacher(UserBean userBean);

    void deleteClassSuccess();

}
