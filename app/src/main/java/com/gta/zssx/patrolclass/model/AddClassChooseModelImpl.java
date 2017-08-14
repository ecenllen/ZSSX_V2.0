package com.gta.zssx.patrolclass.model;

import android.util.SparseIntArray;

import com.gta.zssx.patrolclass.model.entity.ClassChooseEntity;
import com.gta.zssx.pub.widget.easysidebar.EasySection;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * [Description]
 * <p/>
 * [How to use]
 * <p/>
 * [Tips] Created by tengfei.lv on 2016/7/29 9:41.
 */

public class AddClassChooseModelImpl implements BaseModel.AddClassChooseModel {

    BaseDataBridge.AddClassChooseBridge bridge;

    @Override
    public Subscription loadData (BaseDataBridge baseDataBridge) {

        bridge = (BaseDataBridge.AddClassChooseBridge) baseDataBridge;

        return PlanPatrolResultManager.loadChooseClass ()
                .doOnNext (new Action1<List<ClassChooseEntity>> () {//返回数据前做的一些操作
                    @Override
                    public void call (List<ClassChooseEntity> classChooseEntities) {

                        List<ClassChooseEntity> entities = new ArrayList<ClassChooseEntity> ();
                        for (int i = 0; i < classChooseEntities.size (); i++) {
                            ClassChooseEntity entity = classChooseEntities.get (i);
                            if (entity.getClassList ().size () > 0) {
                                entities.add (entity);
                            }
                        }
                        //获取右边栏的数据
                        List<EasySection> mEasySections = new ArrayList<> ();
                        SparseIntArray mIndexIntArray = new SparseIntArray ();
                        int size = 0;
                        for (int i = 0; i < entities.size (); i++) {
                            ClassChooseEntity entity = entities.get (i);

                            EasySection easySection = new EasySection (entity.getDeptCode ());
                            //                            easySection.setLetter ();
                            mEasySections.add (easySection);
                            mIndexIntArray.put (i, size);
                            size++;
                            size += entity.getClassList ().size ();

                        }
                        bridge.setEasySections (mEasySections);
                        bridge.setIndexIntArray (mIndexIntArray);

                    }
                }).flatMap (new Func1<List<ClassChooseEntity>, Observable<List<ClassChooseEntity.ClassListBean>>> () {
                    @Override
                    public Observable<List<ClassChooseEntity.ClassListBean>> call (List<ClassChooseEntity> classChooseEntities) {

                        List<ClassChooseEntity.ClassListBean> classListBeen = new ArrayList<> ();

                        for (int i = 0; i < classChooseEntities.size (); i++) {
                            ClassChooseEntity entity = classChooseEntities.get (i);
                            if (entity.getClassList ().size () > 0) {
                                ClassChooseEntity.ClassListBean bean = new ClassChooseEntity.ClassListBean ();
                                bean.setDeptName (entity.getDeptName ());
                                bean.setDeptCode (entity.getDeptCode ());
                                bean.setDeptId (entity.getDeptId ());
                                bean.setType (0);    // 0代表是系部
                                classListBeen.add (bean);

                                List<ClassChooseEntity.ClassListBean> classList = entity.getClassList ();
                                for (ClassChooseEntity.ClassListBean b : classList) {
                                    b.setDeptId (entity.getDeptId ());
                                    b.setType (1);
                                }
                                classListBeen.addAll (classList);
                            }

                        }

                        return Observable.just (classListBeen);
                    }
                }).subscribe (new Subscriber<List<ClassChooseEntity.ClassListBean>> () {//请求到的数据的回调
                    @Override
                    public void onCompleted () {
                        bridge.onCompleted ();
                    }

                    @Override
                    public void onError (Throwable e) {
                        bridge.onError (e);
                    }

                    @Override
                    public void onNext (List<ClassChooseEntity.ClassListBean> classListBeen) {
                        bridge.onNext (classListBeen);
                    }
                });
    }

    @Override
    public Subscription upLoadPlanClassList (BaseDataBridge baseDataBridge) {
        bridge = (BaseDataBridge.AddClassChooseBridge) baseDataBridge;
        return PlanPatrolResultManager.upLoadPlanClassList (bridge.getEntity ()).subscribe (new Subscriber<String> () {
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
