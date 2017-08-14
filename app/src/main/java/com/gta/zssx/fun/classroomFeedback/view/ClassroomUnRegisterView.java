package com.gta.zssx.fun.classroomFeedback.view;

import com.gta.utils.mvp.BaseView;
import com.gta.zssx.fun.classroomFeedback.model.bean.ClassroomFeedbackBean;

import java.util.List;

/**
 * [Description]
 * <p> 课堂教学反馈首页，未登记Fragment View层接口列表
 * [How to use]
 * <p>
 * [Tips]
 *
 * @author Created by liang.lu on 2017/06/20.
 * @since 2.0.0
 */

public interface ClassroomUnRegisterView extends BaseView {
    void showEmpty (String state,boolean isSuccess);

    void showResult (List<ClassroomFeedbackBean.RegisterDataListBean> list,String state);

    void showToast ();
}
