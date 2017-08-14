package com.gta.zssx.patrolclass.model;

import com.gta.zssx.patrolclass.model.entity.SeePointsListsEntity;
import com.gta.zssx.pub.util.LogUtil;

import rx.Subscriber;
import rx.Subscription;

/**
 * Created by liang.lu on 2016/8/4 14:44.
 */
public class GetSeePointsListsModelImp implements BaseModel.GetSeePointsListsModel {

    BaseDataBridge.GetSeePointsListsBridge bridge;
    @Override
    public Subscription loadData (BaseDataBridge baseDataBridge) {
        bridge = (BaseDataBridge.GetSeePointsListsBridge) baseDataBridge;
        LogUtil.e ("haha    "+bridge.getUid ()+"----"+bridge.getPid ());
        return PatrolClassManager.getSeePointsListsData (bridge.getUid (),Integer.parseInt (bridge.getPid ()))
                .subscribe (new Subscriber<SeePointsListsEntity> () {
                    @Override
                    public void onCompleted () {
                        bridge.onCompleted ();
                    }

                    @Override
                    public void onError (Throwable e) {
                        bridge.onError (e);
                    }

                    @Override
                    public void onNext (SeePointsListsEntity seePointsListsEntity) {
                        bridge.onNext (seePointsListsEntity);
                    }
                });
    }
}
