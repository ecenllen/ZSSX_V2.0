package com.gta.zssx.dormitory.view;

import com.gta.utils.mvp.BaseView;
import com.gta.zssx.dormitory.model.bean.DormitoryOrClassListBean;
import com.gta.zssx.dormitory.model.bean.OptionBean;

public interface OptionView extends BaseView {
    void showResult(OptionBean optionBean);
    void saveSuccess(boolean success, int recordId);
}
