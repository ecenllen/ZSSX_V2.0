package com.gta.zssx.fun.adjustCourse.deprecated.view;

import com.gta.utils.mvp.BaseView;
import com.gta.zssx.fun.adjustCourse.model.bean.ApplyDetailBean;
import com.gta.zssx.fun.adjustCourse.model.bean.CurrentSemesterBean;

/**
 * [Description]
 * <p>
 * [How to use]
 * <p>
 * [Tips]
 *
 * @author Created by Zhimin.Huang on 2016/10/28.
 * @since 1.0.0
 */
public interface ApplyDetailView extends BaseView {
    void showResult(ApplyDetailBean applyDetailBean);

//    0代表查看时删除，1代表确认时删除，2代表审核时删除
    void messageHasDelete(int code);

    void deleteSuccess(String s);

    void confirmSuccess(String s);

    void showSemester(CurrentSemesterBean semesterBean);

    void confirmNotify(String weChat);
}
