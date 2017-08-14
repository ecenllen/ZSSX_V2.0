package com.gta.zssx.fun.adjustCourse.view;

import com.gta.utils.mvp.BaseView;
import com.gta.zssx.fun.adjustCourse.model.bean.SearchClassBean;

import java.util.List;

/**
 * [Description]
 * <p/>
 * [How to use]
 * <p/>
 * [Tips]
 *
 * @author Created by Zhimin.Huang on 2016/6/17.
 * @since 1.0.0
 */
public interface AdjustClassSearchView extends BaseView {

    void showResult(List<SearchClassBean> searchClassBeans);

    void showEmpty();
}
