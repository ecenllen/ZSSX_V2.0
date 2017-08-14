package com.gta.zssx.patrolclass.view;

import com.gta.utils.mvp.BaseView;
import com.gta.zssx.patrolclass.model.entity.SeePointsListsEntity;

import java.util.List;

/**
 * Created by liang.lu1 on 2016/7/14.
 */
public interface SeePointsListsView extends BaseView {
    void showResult (SeePointsListsEntity model);

    void createScoreTitle (List<String> scoreTitle, SeePointsListsEntity model, List<String> allScore);

    String getPid();

    void showEmpty();
}
