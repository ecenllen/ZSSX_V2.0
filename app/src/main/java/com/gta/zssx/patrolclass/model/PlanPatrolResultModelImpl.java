package com.gta.zssx.patrolclass.model;

import com.gta.zssx.patrolclass.model.entity.PlanPatrolResultEntity;

import java.util.List;

import rx.Subscriber;
import rx.Subscription;

/**
 * [Description]
 * <p/>
 * [How to use]
 * <p/>
 * [Tips] Created by tengfei.lv on 2016/7/28 9:45.
 */

public class PlanPatrolResultModelImpl implements BaseModel.PlanPatrolResultModel{

    @Override
    public Subscription loadData (BaseDataBridge baseDataBridge) {
        final BaseDataBridge.PlanPatrolResultBridge bridge = (BaseDataBridge.PlanPatrolResultBridge) baseDataBridge;
        return PlanPatrolResultManager.getPlanPatrolClassDatas (bridge.getUid ())
                    .subscribe (new Subscriber<List<PlanPatrolResultEntity>> () {
                        @Override
                        public void onCompleted () {
                            bridge.onCompleted ();
                        }

                        @Override
                        public void onError (Throwable e) {
                            bridge.onError (e);
                        }

                        @Override
                        public void onNext (List<PlanPatrolResultEntity> entityList) {
                            bridge.onNext (entityList);
                        }
                    });
    }

    @Override
    public Subscription deletePatrolClassItem (BaseDataBridge baseDataBridge) {
        BaseDataBridge.PlanPatrolResultBridge bridge = (BaseDataBridge.PlanPatrolResultBridge) baseDataBridge;
        return PatrolClassManager.deletePatrolClassItem (bridge.getUid (),bridge.getDeptId (),bridge.getClassId ())
                .subscribe (new Subscriber<String> () {
                    @Override
                    public void onCompleted () {
                        bridge.onCompleted ();
                    }

                    @Override
                    public void onError (Throwable e) {
                        bridge.onDeleteError (e);
                    }

                    @Override
                    public void onNext (String s) {
                        bridge.onDeleteNext (s);
                    }
                });
    }
}
