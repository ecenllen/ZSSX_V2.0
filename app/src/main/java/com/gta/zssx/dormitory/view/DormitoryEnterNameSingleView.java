package com.gta.zssx.dormitory.view;

import com.gta.utils.mvp.BaseView;
import com.gta.zssx.dormitory.model.bean.DormitoryEnterNameSingleItemInfoBean;
import com.gta.zssx.dormitory.model.bean.OptionBean;

public interface DormitoryEnterNameSingleView extends BaseView {
    void showResult(DormitoryEnterNameSingleItemInfoBean optionBean);
    void saveSuccess(boolean success, int recordId);
}
