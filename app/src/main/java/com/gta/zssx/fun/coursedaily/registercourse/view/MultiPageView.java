package com.gta.zssx.fun.coursedaily.registercourse.view;

import com.gta.utils.mvp.BaseView;
import com.gta.zssx.fun.coursedaily.registerrecord.model.DTO.RegisteredRecordDto;

import java.util.List;

/**
 * [Description]
 * <p>
 * [How to use]
 * <p>
 * [Tips]
 *
 * @author Created by Zhimin.Huang on 2016/7/20.
 * @since 1.0.0
 */
public interface MultiPageView extends BaseView {
    void showResultList(RegisteredRecordDto registeredRecordDto);

    void setDate(String lNowString, String lBeginString, String lLastString);

    void LoadFirst(List<RegisteredRecordDto.recordEntry> recordEntryList);

    void LoadMore(int page, List<RegisteredRecordDto.recordEntry> recordEntryList);

    void LoadEnd(int page);


}
