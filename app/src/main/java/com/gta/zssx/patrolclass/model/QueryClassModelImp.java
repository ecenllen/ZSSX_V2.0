package com.gta.zssx.patrolclass.model;

import com.gta.zssx.patrolclass.model.entity.PlanClassSearchEntity;

import java.util.List;

import rx.Subscriber;
import rx.Subscription;

/**
 * Created by liang.lu on 2016/8/23 14:40.
 */
public class QueryClassModelImp implements BaseModel.QueryClassModel {
    @Override
    public Subscription loadData (BaseDataBridge baseDataBridge) {
        BaseDataBridge.PlanClassSearchBridge bridge = (BaseDataBridge.PlanClassSearchBridge) baseDataBridge;
        return PatrolClassManager.queryClass (bridge.GetkeyWord ())
                .subscribe (new Subscriber<List<PlanClassSearchEntity>> () {
                    @Override
                    public void onCompleted () {
                        bridge.onCompleted ();
                    }

                    @Override
                    public void onError (Throwable e) {
                        bridge.onError (e);
                    }

                    @Override
                    public void onNext (List<PlanClassSearchEntity> entities) {
                        bridge.onNext (entities);
                    }
                });
    }

    @Override
    public Subscription upLoadPlanClassList (BaseDataBridge baseDataBridge) {
        BaseDataBridge.PlanClassSearchBridge bridge = (BaseDataBridge.PlanClassSearchBridge) baseDataBridge;
        return PlanPatrolResultManager.upLoadPlanClassList (bridge.getEntity ())
                .subscribe (new Subscriber<String> () {
                    @Override
                    public void onCompleted () {
                        bridge.onCompleted ();
                    }

                    @Override
                    public void onError (Throwable e) {
                        bridge.onAddClassError (e);
                    }

                    @Override
                    public void onNext (String s) {
                        bridge.getOnAddClassNext (s);
                    }
                });
    }
}
