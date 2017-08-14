package com.gta.zssx.fun.adjustCourse.view;

import com.gta.utils.mvp.BaseView;
import com.gta.zssx.fun.adjustCourse.model.bean.StuClassBean;

import java.util.List;

/**
 * [Description]
 * <p>
 * [How to use]
 * <p>
 * [Tips]
 *
 * @author Created by Zhimin.Huang on 2016/11/3.
 * @since 1.0.0
 */
public interface ChooseClassView extends BaseView {
    void showResult(List<StuClassBean.ClassListBean> listBeen);
}
