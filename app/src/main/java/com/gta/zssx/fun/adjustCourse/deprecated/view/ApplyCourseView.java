package com.gta.zssx.fun.adjustCourse.deprecated.view;

import com.gta.utils.mvp.BaseView;
import com.gta.zssx.fun.adjustCourse.model.bean.ApplyBean;

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
public interface ApplyCourseView extends BaseView {
    void LoadFirst(ApplyBean applyBean);

    void LoadMore(int page, ApplyBean applyBean);

    void LoadEnd(int page);

    void showEmpty(String message);

    void onRefreshError(int page);
}
