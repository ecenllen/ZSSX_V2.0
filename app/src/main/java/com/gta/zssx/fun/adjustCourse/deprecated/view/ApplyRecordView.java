package com.gta.zssx.fun.adjustCourse.deprecated.view;

import com.gta.utils.mvp.BaseView;
import com.gta.zssx.fun.adjustCourse.model.bean.ApplyRecordBean;

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
public interface ApplyRecordView extends BaseView {


    void LoadFirst(ApplyRecordBean applyRecordBean);

    void LoadMore(int page, ApplyRecordBean applyRecordBean);

    void LoadEnd(int page);

    void showEmpty(String message);

    void onRefreshError(int page);
}
