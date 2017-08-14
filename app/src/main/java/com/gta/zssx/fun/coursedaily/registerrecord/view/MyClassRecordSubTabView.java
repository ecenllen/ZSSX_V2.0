package com.gta.zssx.fun.coursedaily.registerrecord.view;

import com.gta.utils.mvp.BaseView;
import com.gta.zssx.fun.coursedaily.registerrecord.model.DTO.MyClassRecordDto;

/**
 * Created by lan.zheng on 2016/7/2.
 */
public interface MyClassRecordSubTabView extends BaseView {
    void showResult(boolean isNeedToast, MyClassRecordDto myClassRecordDto);

    void notResult(boolean isNeedToast, String className);

    void showApproveResult(boolean isApprove);

    void getApproveFailed(boolean showToast);

    void deleteResult();  //删除一条记录

    void deleteFailed();  //删除一条记录
}
