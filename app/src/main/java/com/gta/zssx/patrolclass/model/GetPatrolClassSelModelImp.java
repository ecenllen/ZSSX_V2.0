package com.gta.zssx.patrolclass.model;

import com.gta.zssx.patrolclass.model.entity.DeleteItemEntity;
import com.gta.zssx.patrolclass.model.entity.PatrolClassSelEntity;
import com.gta.zssx.pub.util.LogUtil;

import java.util.List;

import rx.Subscriber;
import rx.Subscription;
import rx.functions.Action1;

/**
 * Created by liang.lu on 2016/8/3 16:55.
 */
public class GetPatrolClassSelModelImp implements BaseModel.GetPatrolClassSelModel {


    BaseDataBridge.GetPatrolClassSelBridge bridge;


    /**
     * 一键送审  实体
     */
//    SubmitApprovalEntity submitApprovalEntity;
    String ids = "";
    /**
     * 删除item实体
     */
    DeleteItemEntity deleteItemEntity;

    int xId;
    String uId;

    @Override
    public Subscription loadData (BaseDataBridge baseDataBridge) {
        bridge = (BaseDataBridge.GetPatrolClassSelBridge) baseDataBridge;
        return PatrolClassManager.getPatrolClassSelData (bridge.getUid (), bridge.getXid (), 1, 20)
                .doOnNext (new Action1<List<PatrolClassSelEntity>> () {
                    @Override
                    public void call (List<PatrolClassSelEntity> patrolClassSelEntities) {
//                        submitApprovalEntity = new SubmitApprovalEntity ();
                        for (int i = 0; i < patrolClassSelEntities.size (); i++) {
                            PatrolClassSelEntity entity = patrolClassSelEntities.get (i);
                            ids += entity.getPId () + ",";
                        }
//                        submitApprovalEntity.setIds (ids.substring (0, ids.length () - 1));
//                        submitApprovalEntity.setUid (bridge.getUid ());
                        ids = ids.substring (0, ids.length () - 1);

                        uId = bridge.getUid ();
                        xId = bridge.getXid ();
                    }
                }).subscribe (new Subscriber<List<PatrolClassSelEntity>> () {
                    @Override
                    public void onCompleted () {
                        bridge.onCompleted ();
                    }

                    @Override
                    public void onError (Throwable e) {
                        bridge.onError (e);
                    }

                    @Override
                    public void onNext (List<PatrolClassSelEntity> patrolClassSelEntities) {
                        bridge.onNext (patrolClassSelEntities);
                    }
                });
    }


    @Override
    public Subscription submitApproval (BaseDataBridge baseDataBridge) {
        bridge = (BaseDataBridge.GetPatrolClassSelBridge) baseDataBridge;

        return PatrolClassManager.submitApproval (xId+"",uId)
                .subscribe (new Subscriber<String> () {
                    @Override
                    public void onCompleted () {
                        bridge.onCompleted ();
                    }

                    @Override
                    public void onError (Throwable e) {
                        bridge.onSubmitApprovalError (e);
                    }

                    @Override
                    public void onNext (String s) {
                        bridge.onSubmitApprovalNext (s);
                    }
                });
    }

    /**
     * 删除巡课详情item
     *
     * @param baseDataBridge
     * @return
     */
    @Override
    public Subscription deletePatrolItem (BaseDataBridge baseDataBridge) {
        bridge = (BaseDataBridge.GetPatrolClassSelBridge) baseDataBridge;
        LogUtil.e ("haha" + "uId==" + uId + "---" + "xId==" + xId + "---" + "pId==" + bridge.getPid ());
        return PatrolClassManager.deletePatrolItem (uId, xId, Integer.parseInt (bridge.getPid ()))

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

    @Override
    public Subscription onRefresh (BaseDataBridge baseDataBridge) {
        bridge = (BaseDataBridge.GetPatrolClassSelBridge) baseDataBridge;
        return PatrolClassManager.getPatrolClassSelData (bridge.getUid (),bridge.getXid (),1,20)
                .subscribe (new Subscriber<List<PatrolClassSelEntity>> () {
                    @Override
                    public void onCompleted () {

                    }

                    @Override
                    public void onError (Throwable throwable) {
                        bridge.onRefreshError (throwable);
                    }

                    @Override
                    public void onNext (List<PatrolClassSelEntity> entities) {
                        bridge.onRefreshCompleted (entities);
                    }
                });
    }

    @Override
    public Subscription onLoadMore (BaseDataBridge baseDataBridge, int page) {
        bridge = (BaseDataBridge.GetPatrolClassSelBridge) baseDataBridge;
        return PatrolClassManager.getPatrolClassSelData (bridge.getUid (),bridge.getXid (),page,20)
                .subscribe (new Subscriber<List<PatrolClassSelEntity>> () {
                    @Override
                    public void onCompleted () {

                    }

                    @Override
                    public void onError (Throwable throwable) {
                        bridge.onLoadMoreError (throwable);
                    }

                    @Override
                    public void onNext (List<PatrolClassSelEntity> entities) {
                        bridge.onLoadMoreCompleted (entities);
                    }
                });
    }
}
