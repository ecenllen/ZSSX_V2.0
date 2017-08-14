package com.gta.zssx.patrolclass.model;

import com.gta.zssx.patrolclass.model.entity.PatrolClassEntity;

import java.util.List;

import rx.Subscriber;
import rx.Subscription;

/**
 * Created by liang.lu on 2016/8/3 15:06.
 */
public class GetPatrolClassModelImp implements BaseModel.GetPatrolClassModel {

    BaseDataBridge.GetPatrolClassBridge bridge;

    @Override
    public Subscription loadData (BaseDataBridge baseDataBridge) {
        bridge = (BaseDataBridge.GetPatrolClassBridge) baseDataBridge;
        return PatrolClassManager.getPatrolClassData (bridge.getUid (),bridge.getType (),1,20,
                bridge.getStartDate(),bridge.getEndDate())

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

    public Subscription onRefresh(BaseDataBridge baseDataBridge,String startDate,String endDate){
        bridge = (BaseDataBridge.GetPatrolClassBridge) baseDataBridge;
        return PatrolClassManager.getPatrolClassData (bridge.getUid (),bridge.getType (),1,20,startDate,endDate)

                .subscribe (new Subscriber<List<PatrolClassEntity>> () {
                    @Override
                    public void onCompleted () {
                    }

                    @Override
                    public void onError (Throwable e) {
                        bridge.onRefreshError (e);
                    }

                    @Override
                    public void onNext (List<PatrolClassEntity> patrolClassEntities) {
                        bridge.onRefreshCompleted (patrolClassEntities);
                    }
                });
    }

    public Subscription onLoadMore(BaseDataBridge baseDataBridge,int page,String startDate,String endDate){
        bridge = (BaseDataBridge.GetPatrolClassBridge) baseDataBridge;
        return PatrolClassManager.getPatrolClassData (bridge.getUid (),bridge.getType (),page,20,startDate,endDate)

                .subscribe (new Subscriber<List<PatrolClassEntity>> () {
                    @Override
                    public void onCompleted () {
                    }

                    @Override
                    public void onError (Throwable e) {
                        bridge.onLoadMoreError (e);
                    }

                    @Override
                    public void onNext (List<PatrolClassEntity> patrolClassEntities) {
                        bridge.onLoadMoreCompleted (patrolClassEntities);
                    }
                });
    }

}
