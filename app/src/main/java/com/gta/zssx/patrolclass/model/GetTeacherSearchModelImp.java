package com.gta.zssx.patrolclass.model;

import com.gta.zssx.patrolclass.model.entity.ChooseTeacherSearchEntity;

import java.util.List;

import rx.Subscriber;
import rx.Subscription;

/**
 * Created by liang.lu on 2016/8/17 16:40.
 */
public class GetTeacherSearchModelImp implements BaseModel.GetTeacherSearchModel {
    BaseDataBridge.GetTeacherSearchBridge bridge;
    @Override
    public Subscription loadData (BaseDataBridge baseDataBridge) {
        bridge = (BaseDataBridge.GetTeacherSearchBridge) baseDataBridge;
        return PatrolClassManager.getTeacherSearchDatas (bridge.GetkeyWord ())
                .subscribe (new Subscriber<List<ChooseTeacherSearchEntity>> () {
                    @Override
                    public void onCompleted () {
                        bridge.onCompleted ();
                    }

                    @Override
                    public void onError (Throwable e) {
                        bridge.onError (e);
                    }

                    @Override
                    public void onNext (List<ChooseTeacherSearchEntity> entities) {
                        bridge.onNext (entities);
                    }
                });
    }
}
