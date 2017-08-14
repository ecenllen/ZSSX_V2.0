package com.gta.zssx.fun.adjustCourse.view;

import com.gta.utils.mvp.BaseView;
import com.gta.zssx.fun.adjustCourse.model.bean.ApplySuccessBean;
import com.gta.zssx.fun.adjustCourse.model.bean.CurrentSemesterBean;

/**
 * [Description]
 * <p>
 * [How to use]
 * <p>
 * [Tips]
 *
 * @author Created by Zhimin.Huang on 2016/10/24.
 * @since 1.0.0
 */
public interface CourseApplyView extends BaseView {
    /**
     * 请求学期结果
     * @param semesterBean
     */
    void showSemester(CurrentSemesterBean semesterBean);

    /**
     * 代课新增成功
     *
     * @param applySuccessBean
     */
    void replaceCourseSuccess(ApplySuccessBean applySuccessBean);

    /**
     * 调课新增成功
     *
     * @param applySuccessBean
     */
    void AdjustCourseSuccess(ApplySuccessBean applySuccessBean);
}
