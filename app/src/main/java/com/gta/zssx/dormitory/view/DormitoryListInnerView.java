package com.gta.zssx.dormitory.view;

import com.gta.utils.mvp.BaseView;
import com.gta.zssx.dormitory.model.bean.DormitoryOrClassListBean;

public interface DormitoryListInnerView extends BaseView {
    void showResult(DormitoryOrClassListBean dormitoryListBean);
}
