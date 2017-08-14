package com.gta.zssx.fun.coursedaily.registercourse.view;

import com.gta.utils.mvp.BaseView;
import com.gta.zssx.fun.coursedaily.registercourse.model.bean.DefaultRegistInfoBean;
import com.gta.zssx.fun.coursedaily.registercourse.model.bean.DetailItemShowBean;
import com.gta.zssx.fun.coursedaily.registercourse.model.bean.SectionBean;
import com.gta.zssx.fun.coursedaily.registercourse.presenter.RegisterDetailPresenter;

import java.util.List;

/**
 * [Description]
 * <p/>
 * [How to use]
 * <p/>
 * [Tips]
 *
 * @author Created by Zhimin.Huang on 2016/6/18.
 * @since 1.0.0
 */
public interface DetailView extends BaseView {
    void showResult(RegisterDetailPresenter.Combined combined);

    void showLastestSection(SectionBean sectionBean);

    void showSectionDefaultTeacherAndCourse(DefaultRegistInfoBean defaultRegistInfoBean,DetailItemShowBean.TeacherInfoBean teacherInfoBean);

    //判断是否有被别的地方抢先登记的节次,isCheckAllSection表示是否是检查整一天的节次登记情况
    void showSelectSectionNotSignCount(int notSignNum,boolean isCheckAllSection);

}
