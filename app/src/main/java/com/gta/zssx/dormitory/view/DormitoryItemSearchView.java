package com.gta.zssx.dormitory.view;

import com.gta.utils.mvp.BaseView;
import com.gta.zssx.dormitory.model.bean.ItemLevelBean;

import java.util.List;

/**
 * Created by lan.zheng on 2017/6/29.
 */

public interface DormitoryItemSearchView extends BaseView{
    void showResult(List<ItemLevelBean> itemLevelBeanList);  //返回结果集合

    void showToast(String msg);  //无结果时弹出提示
}
