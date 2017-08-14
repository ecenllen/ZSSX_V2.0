package com.gta.zssx.fun.classroomFeedback.view;

import com.gta.utils.mvp.BaseView;
import com.gta.zssx.fun.coursedaily.registercourse.model.bean.DetailItemShowBean;

import java.util.List;

/**
 * [Description]
 * <p> 课堂教学反馈课程选择界面 View 层接口列表
 * [How to use]
 * <p>
 * [Tips]
 *
 * @author Created by liang.lu on 2017/06/20.
 * @since 2.0.0
 */

public interface CourseSearchView extends BaseView {
    void showMyCourseResult (List<DetailItemShowBean.CourseInfoBean> courseInfoBeanList);

    void showSearchResult (List<DetailItemShowBean.CourseInfoBean> courseInfoBeanList);

    void showToast (String string);

}
