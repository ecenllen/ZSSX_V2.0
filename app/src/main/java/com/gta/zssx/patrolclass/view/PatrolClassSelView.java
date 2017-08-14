package com.gta.zssx.patrolclass.view;

import com.gta.utils.mvp.BaseView;
import com.gta.zssx.patrolclass.model.entity.PatrolClassSelEntity;

import java.util.List;

/**
 * Created by liang.lu1 on 2016/7/13.
 */
public interface PatrolClassSelView extends BaseView {
    void showResult (List<PatrolClassSelEntity> models);

    void showEmpty ();

    int getXid ();

    String getPid ();

    void DeleteItemSuccess ();

    void submitSuccess ();

    void hideLoadMoreAndRefresh (List<PatrolClassSelEntity> mPatrolClassSelModels);

    void onLoadMoreError ();

    void onRefreshError ();

    void onLoadMoreEmpty ();
}
