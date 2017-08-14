package com.gta.zssx.patrolclass.view;

import com.gta.utils.mvp.BaseView;
import com.gta.zssx.patrolclass.model.entity.DockScoreEntity;
import com.gta.zssx.patrolclass.model.entity.SubmitEntity;

import java.util.List;

/**
 * [Description]
 * <p>
 * [How to use]
 * <p>
 * [Tips] Created by tengfei.lv on 2016/7/19 14:01.
 */

public interface PatrolRegisterView extends BaseView {
    void showSuccessData (DockScoreEntity dockScoreEntity);

    void createScoreTitle (List<String> scoreTitle);

    void showSubmitNext (List<SubmitEntity> integer);

    String getDeptId ();

    int getClassId ();

    String getDate ();

    int getSectionId ();

    int getXid ();

    void showResult (List<DockScoreEntity.DockScoreBean.OptionsBean> optionsBeen);

    void requestData ();

    void clickSave ();

    void showDilog (boolean isShowSure, String content);

    void showTimeDilog(String content);
}
