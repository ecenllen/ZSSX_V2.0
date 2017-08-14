package com.gta.zssx.dormitory.view;

import com.gta.utils.mvp.BaseView;
import com.gta.zssx.dormitory.model.bean.DormitoryEnterNameMultipleItemInfoBean;

/**
 * Created by lan.zheng on 2017/6/30.
 * 录入姓名多项 - 宿舍维度
 */

public interface DormitoryEnterNameMultipleDormitoryView extends BaseView {
    void showToast(String msg);  //提示
    void getDataInfoFailed();//获取数据失败
    void showDataInfo(DormitoryEnterNameMultipleItemInfoBean dormitoryEnterNameMultipleItemInfoBean);  //成功获取数据
    void saveAndGetRecordId(int trueRecordId);  //当记录Id为-1时，要拿到真正的Id存起来
    void saveDataFailHandle(String msg); //保存失败
}
