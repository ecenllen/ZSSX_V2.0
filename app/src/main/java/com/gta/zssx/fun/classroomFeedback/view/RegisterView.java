package com.gta.zssx.fun.classroomFeedback.view;

import com.gta.utils.mvp.BaseView;
import com.gta.zssx.fun.classroomFeedback.model.bean.RegisterPageBean;
import com.gta.zssx.fun.classroomFeedback.model.bean.RegisterPageDateBean;

import java.util.List;

/**
 * [Description]
 * <p> 课堂教学反馈登记页面 View层接口列表
 * [How to use]
 * <p>
 * [Tips]
 *
 * @author Created by liang.lu on 2017/06/20.
 * @since 2.0.0
 */

public interface RegisterView extends BaseView {
    void showResult (RegisterPageBean registerPageBean);

    void createDateTextView (List<RegisterPageDateBean> dateBeanList);

    void showAllSectionData (List<RegisterPageBean.ClassroomRegisterListBean.SectionDataListBean> sectionDataListBeanList);

    void saveAuditContentSuccess (String s);

    void submitOneWeekDataSuccess ();

    void showEmpty ();
}
