package com.gta.zssx.fun.coursedaily.registercourse.view;

import com.gta.utils.mvp.BaseView;
import com.gta.zssx.fun.coursedaily.registercourse.model.bean.SectionStudentListBean;
import com.gta.zssx.fun.coursedaily.registercourse.model.bean.StudentListBean;
import com.gta.zssx.fun.coursedaily.registercourse.model.bean.StudentListNewBean;

import java.util.List;

/**
 * [Description]
 * <p/>
 * [How to use]
 * <p/>
 * [Tips]
 *
 * @author Created by Zhimin.Huang on 2016/6/21.
 * @since 1.0.0
 */
public interface SignView extends BaseView {

    void showResult(String s);
    //显示有节次已经被被登记了
    void showHaveBeenSignMassage(String s);
    //显示空UI
    void emptyUI();
    //学生列表-new
    void getStudentListInfo(List<SectionStudentListBean> sectionStudentListBeanList);
    //查看当提交的是不是有已登记
    void isHaveSectionHaveBeenSign(int unSignNum);
}
