package com.gta.zssx.dormitory.view;

import com.gta.utils.mvp.BaseView;
import com.gta.zssx.dormitory.model.bean.ItemInfoBean;

import java.util.List;

/**
 * Created by lan.zheng on 2017/6/28.
 */

public interface DormitorySelectItemView extends BaseView {
    void showEmptyView();  //空页面

    void showToast();

    void showResult(ItemInfoBean itemInfoBean,boolean isNeedToRequestNextLevel);
}
