package com.gta.zssx.patrolclass.model;

import com.gta.zssx.patrolclass.model.entity.ChooseTeacherEntity;

import java.util.List;

import rx.Subscriber;
import rx.Subscription;

/**
 * Created by liang.lu on 2016/8/17 13:31.
 */
public class GetChooseTeacherModelImp implements BaseModel.GetChooseTeacherModel {
    BaseDataBridge.GetChooseTeacherBridge bridge;

    @Override
    public Subscription loadData (BaseDataBridge baseDataBridge) {
        bridge = (BaseDataBridge.GetChooseTeacherBridge) baseDataBridge;

        return PatrolClassManager.getChooseTeacherDatas (bridge.getDeptId ())
                .subscribe (new Subscriber<List<ChooseTeacherEntity>> () {
                    @Override
                    public void onCompleted () {
                        bridge.onCompleted ();
                    }

                    @Override
                    public void onError (Throwable e) {
                        bridge.onError (e);
                    }

                    @Override
                    public void onNext (List<ChooseTeacherEntity> entities) {
                        bridge.onNext (entities);
                    }
                });
    }
}
