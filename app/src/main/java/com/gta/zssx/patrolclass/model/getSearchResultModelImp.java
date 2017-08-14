package com.gta.zssx.patrolclass.model;

import com.gta.zssx.patrolclass.model.entity.PatrolClassEntity;

import java.util.List;

import rx.Subscriber;
import rx.Subscription;

/**
 * Created by liang.lu on 2016/8/4 16:13.
 */
public class getSearchResultModelImp implements BaseModel.GetSearchResultModel {

    BaseDataBridge.GetSearchResultBridge bridge;

    @Override
    public Subscription loadData (BaseDataBridge baseDataBridge) {

        bridge = (BaseDataBridge.GetSearchResultBridge) baseDataBridge;
        return PatrolClassManager.getSearchResultData (bridge.getUid (), bridge.getType (), 1, 20, bridge.getStartDate (), bridge.getEndDate ())
                .subscribe (new Subscriber<List<PatrolClassEntity>> () {
                    @Override
                    public void onCompleted () {
                        bridge.onCompleted ();
                    }

                    @Override
                    public void onError (Throwable e) {
                        bridge.onError (e);
                    }

                    @Override
                    public void onNext (List<PatrolClassEntity> patrolClassEntities) {
                        bridge.onNext (patrolClassEntities);
                    }
                });
    }

    @Override
    public Subscription onRefresh (BaseDataBridge baseDataBridge) {
        bridge = (BaseDataBridge.GetSearchResultBridge) baseDataBridge;
        return PatrolClassManager.getSearchResultData (bridge.getUid (),bridge.getType (),1,20,bridge.getStartDate (),bridge.getEndDate ())
                .subscribe (new Subscriber<List<PatrolClassEntity>> () {
                    @Override
                    public void onCompleted () {

                    }

                    @Override
                    public void onError (Throwable throwable) {
                        bridge.onRefreshError (throwable);
                    }

                    @Override
                    public void onNext (List<PatrolClassEntity> patrolClassEntities) {
                        bridge.onRefreshCompleted (patrolClassEntities);
                    }
                });
    }

    @Override
    public Subscription onLoadMore (BaseDataBridge baseDataBridge, int page) {
        bridge = (BaseDataBridge.GetSearchResultBridge) baseDataBridge;
        return  PatrolClassManager.getSearchResultData (bridge.getUid (),bridge.getType (),page,20,bridge.getStartDate (),bridge.getEndDate ())
                .subscribe (new Subscriber<List<PatrolClassEntity>> () {
                    @Override
                    public void onCompleted () {

                    }

                    @Override
                    public void onError (Throwable throwable) {
                        bridge.onLoadMoreError (throwable);
                    }

                    @Override
                    public void onNext (List<PatrolClassEntity> patrolClassEntities) {
                        bridge.onLoadMoreCompleted (patrolClassEntities);
                    }
                });
    }
}
