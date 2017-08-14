package com.gta.zssx.fun.coursedaily.registerrecord.view;

import com.gta.utils.mvp.BaseView;
import com.gta.zssx.fun.coursedaily.registercourse.model.bean.ApproveBean;
import com.gta.zssx.fun.coursedaily.registerrecord.model.DTO.RegisteredRecordFromSignatureDto;

import java.util.List;

/**
 * Created by lan.zheng on 2016/6/22.
 */
public interface AlreadyRegisteredRecordFromSignatureView extends BaseView {
    void showResultList(List<RegisteredRecordFromSignatureDto> registeredRecordFromSignatureDtos);
    void notResult();
    void showApproveResult(boolean isApprove);
    void getApproveFailed(boolean showToast);
    void deleteResult();
    void deleteFailed();
}
