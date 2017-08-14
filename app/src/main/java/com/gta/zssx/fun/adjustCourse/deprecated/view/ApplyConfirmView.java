package com.gta.zssx.fun.adjustCourse.deprecated.view;

import com.gta.utils.mvp.BaseView;
import com.gta.zssx.fun.adjustCourse.model.bean.ApplyConfirmBean;

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
public interface ApplyConfirmView extends BaseView {
    void LoadFirst(ApplyConfirmBean applyConfirmBean);

    void LoadMore(int page, ApplyConfirmBean applyConfirmBean);

    void LoadEnd(int page);

    void showEmpty(String message);

    void onRefreshError(int page);
}
