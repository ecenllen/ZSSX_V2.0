package com.gta.zssx.dormitory.view;

import com.gta.utils.mvp.BaseView;
import com.gta.zssx.dormitory.model.bean.DormitoryEnterNameMultipleItemInfoBeanV2;

/**
 * Created by lan.zheng on 2017/6/29.
 * 录入姓名多项 - 班级维度
 */
public interface DormitoryEnterNameMultipleClassView  extends BaseView {
    void showToast(String msg);  //显示提示
    void getDataInfoFailed();//获取数据失败
    void showDataInfo(DormitoryEnterNameMultipleItemInfoBeanV2 dormitoryEnterNameMultipleItemInfoBeanV2);  //成功获取数据
    void saveAndGetRecordId(int trueRecordId);  //当记录Id为-1时，要拿到真正的Id存起来
    void saveDataFailHandle(String msg); //保存失败
}
