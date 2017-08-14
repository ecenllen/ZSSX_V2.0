package com.gta.zssx.patrolclass.view;

import com.gta.utils.mvp.BaseView;
import com.gta.zssx.patrolclass.model.entity.PatrolClassEntity;

import java.util.List;

/**
 * Created by liang.lu1 on 2016/7/8.
 */
public interface PatrolClassView extends BaseView {

    void showResult (List<PatrolClassEntity> lists);

    void showEmpty (String state, boolean isSuccess);

    void hideLoadMoreAndRefresh (List<PatrolClassEntity> mPatrolClassModels);

    void onLoadMoreError ();

    void onRefreshError ();

    void onLoadMoreEmpty ();
}