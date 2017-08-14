package com.gta.zssx.fun.coursedaily.registerrecord.view;

import com.gta.utils.mvp.BaseView;
import com.gta.zssx.fun.coursedaily.registerrecord.model.DTO.MyClassRecordDto;

import java.util.List;

/**
 * Created by lan.zheng on 2016/6/25.
 */
public interface MyClassRecordView extends BaseView {
    void showResult(List<MyClassRecordDto> recordFromSignatureDtos);

    void notResult();

    void setServerTime(String date, String week);

    void setServerTimeFailed(boolean isNotNetwork);

    void showApproveResult(boolean isApprove);

    void getApproveFailed(boolean showToast);

    void deleteResult();  //删除一条记录

    void deleteFailed();  //删除一条记录

}
