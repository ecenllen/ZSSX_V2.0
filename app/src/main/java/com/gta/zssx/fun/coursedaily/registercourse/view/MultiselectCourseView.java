package com.gta.zssx.fun.coursedaily.registercourse.view;

import com.gta.utils.mvp.BaseView;
import com.gta.zssx.fun.coursedaily.registercourse.model.bean.DetailItemShowBean;

import java.util.List;

/**
 * Created by lan.zheng on 2017/3/9.
 */
public interface MultiselectCourseView extends BaseView {
    void showMyCourse(List<DetailItemShowBean.CourseInfoBean> courseInfoBeanList);

    void getMyCourseFailed();

    void showResultSearch(List<DetailItemShowBean.CourseInfoBean> courseInfoBeanList); //获取搜索课程成功

    void showToast(String s);  //添加"自定义课程"或 "搜索"失败都弹出提示

    void addCustomCourse(DetailItemShowBean.CourseInfoBean courseInfoBean);  //添加自定义课程
}
