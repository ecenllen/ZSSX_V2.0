package com.gta.zssx.fun.personalcenter.view;

import android.content.Context;

import com.gta.utils.mvp.BaseView;
import com.gta.zssx.fun.personalcenter.model.bean.UserBean;

/**
 * [Description]
 * <p/>
 * [How to use]
 * <p/>
 * [Tips]
 *
 * @author Created by Zhimin.Huang on 2016/6/14.
 * @since 1.0.0
 */
public interface LoginView extends BaseView {
    Context getContext();
    void loginSuccess(UserBean userBean);

    void loginFail(int code, String errorMessage);

    void noRegisterClass(int code, String errorMessage,UserBean userBean);
}
