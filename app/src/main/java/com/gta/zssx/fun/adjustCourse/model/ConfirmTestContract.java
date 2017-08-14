package com.gta.zssx.fun.adjustCourse.model;

import com.gta.utils.mvp.BasePresenter;
import com.gta.utils.mvp.BaseView;
import com.gta.zssx.fun.adjustCourse.model.bean.ApplyConfirmBean;

/**
 * [Description]
 * <p>
 * [How to use]
 * <p>
 * [Tips]
 *
 * @author Created by Zhimin.Huang on 2017/1/3.
 * @since 1.0.0
 */
public interface ConfirmTestContract {
    interface View extends BaseView {
        void LoadFirst(ApplyConfirmBean applyConfirmBean);

        void LoadMore(int page, ApplyConfirmBean applyConfirmBean);

        void LoadEnd(int page);

        void showEmpty(String message);

        void onRefreshError(int page);
    }

    abstract class Presenter extends BasePresenter<View> {
        public abstract void getApplyConfirm(int page);
    }

    interface Model {

    }

}
