package com.gta.zssx.dormitory.view;
import com.gta.utils.mvp.BaseView;
import com.gta.zssx.dormitory.model.bean.DormitoryOrClassSingleInfoBean;

import java.util.List;

/**
 * Created by lan.zheng on 2017/6/27.
 */

public interface DormitorySelectLevelView extends BaseView {
    void getDormitoryOrClassSuccess(List<DormitoryOrClassSingleInfoBean> domitoryOrClassInfoBeanList);

    void getDormitoryOrClassFailed();
}
