package com.gta.zssx.fun.personalcenter.view;

import com.gta.utils.mvp.BaseView;
import com.gta.zssx.fun.personalcenter.model.bean.UserBean;

/**
 * Created by lan.zheng on 2016/6/20.
 */
public interface UpdateDataView extends BaseView {
    void checkVersionUpdate(String versionCode);  //检查更新，需要传入当前版本号

    void userPhotoUpdate(UserBean userBean);  //用户头像更新

    void userPhotoUpdateFailed(); //头像更新失败

    void getUserInfoReturn(boolean isGetInfo); //更新用户信息显示
}
