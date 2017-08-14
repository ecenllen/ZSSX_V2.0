package com.gta.zssx.fun.adjustCourse.view;

import com.gta.utils.mvp.BaseView;
import com.gta.zssx.fun.adjustCourse.model.bean.ApplyBean;
import com.gta.zssx.fun.adjustCourse.model.bean.ApplyConfirmBean;

/**
 * [Description]
 * <p>
 * [How to use]
 * <p>
 * [Tips]
 *
 * @author Created by Zhimin.Huang on 2017/3/20.
 * @since 1.0.0
 */
public interface ConfirmInnerView extends BaseView {
    void getResultData(ApplyConfirmBean applyConfirmBean);
}
