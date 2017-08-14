package com.gta.zssx.fun.classroomFeedback.view;

import com.gta.utils.mvp.BaseView;
import com.gta.zssx.fun.classroomFeedback.model.bean.RegisterDetailsBean;
import com.gta.zssx.fun.classroomFeedback.model.bean.RegisterPageBean;

/**
 * [Description]
 * <p> 课堂教学反馈登记详情页面 View 层接口列表
 * [How to use]
 * <p>
 * [Tips]
 *
 * @author Created by liang.lu on 2017/06/20.
 * @since 2.0.0
 */

public interface RegisterDetailsView extends BaseView {
    void showResult (RegisterDetailsBean detailsBean, RegisterPageBean.ClassroomRegisterListBean.SectionDataListBean sectionBean);

    void SaveCacheData (int state, boolean isSave);

    void showEmpty ();
}
