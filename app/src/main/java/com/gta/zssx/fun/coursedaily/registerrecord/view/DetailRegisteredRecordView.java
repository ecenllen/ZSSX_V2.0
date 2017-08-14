package com.gta.zssx.fun.coursedaily.registerrecord.view;

import com.gta.utils.mvp.BaseView;
import com.gta.zssx.fun.coursedaily.registerrecord.model.DTO.DetailRegisteredRecordDto;

/**
 * Created by lan.zheng on 2016/6/22.
 */
public interface DetailRegisteredRecordView extends BaseView {
    void showResultList(DetailRegisteredRecordDto detailRegisteredRecordDto);
    void updateExpandableList();
}
