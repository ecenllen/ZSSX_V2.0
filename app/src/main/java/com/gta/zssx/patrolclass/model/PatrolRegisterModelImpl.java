package com.gta.zssx.patrolclass.model;

import com.gta.zssx.patrolclass.model.entity.DockScoreEntity;
import com.gta.zssx.patrolclass.model.entity.PatrolRegisterBean;
import com.gta.zssx.patrolclass.model.entity.SubmitEntity;
import com.gta.zssx.patrolclass.model.entity.SubmitRegisterPatrol;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * [Description]
 * <p>
 * [How to use]
 * <p>
 * [Tips] Created by tengfei.lv on 2016/7/28 10:29.
 */

public class PatrolRegisterModelImpl implements BaseModel.PatrolRegisterModel {

    private DockScoreEntity dockScoreEntity1;
    private List<String> scoreTitle;
    private List<DockScoreEntity.DockScoreBean.OptionsBean> optionsBeenList;
    BaseDataBridge.PatrolRegisterBridge bridge;

    @Override
    public Subscription loadData (BaseDataBridge baseDataBridge,boolean notNull,String date) {
        bridge = (BaseDataBridge.PatrolRegisterBridge) baseDataBridge;

        return PlanPatrolResultManager.getRegisterPatrolDatas (bridge.getUid (), bridge.getDeptId (), bridge.getClassId (), bridge.getDate (), bridge.getSctionId ())
                .doOnNext (new Action1<DockScoreEntity> () {
                    @Override
                    public void call (DockScoreEntity dockScoreEntity) {
                        dockScoreEntity1 = dockScoreEntity;
                        bridge.getEntity (dockScoreEntity);
                        //对节次进行排序
                        if (dockScoreEntity != null) {
                            List<DockScoreEntity.SectionsListBean> sectionsList = dockScoreEntity.getSectionsList ();
                            Collections.sort (sectionsList, new Comparator<DockScoreEntity.SectionsListBean> () {
                                @Override
                                public int compare (DockScoreEntity.SectionsListBean lhs, DockScoreEntity.SectionsListBean rhs) {
                                    return lhs.getType () - rhs.getType ();
                                }
                            });
                            bridge.setSectionList (sectionsList);
                            bridge.setDockScoreEntity (dockScoreEntity);
                        } else {
                            return;
                        }
                    }
                }).flatMap (new Func1<DockScoreEntity, Observable<List<DockScoreEntity.DockScoreBean>>> () {
                    @Override
                    public Observable<List<DockScoreEntity.DockScoreBean>> call (DockScoreEntity dockScoreEntity) {
                        //
                        return Observable.just (dockScoreEntity.getDockScore ());
                    }
                }).doOnNext (new Action1<List<DockScoreEntity.DockScoreBean>> () {
                    @Override
                    public void call (List<DockScoreEntity.DockScoreBean> dockScoreBeen) {
                        //记录有多少个扣分指标（比如：教师分，学生分，卫生分等）
                        scoreTitle = new ArrayList<> ();
                        for (int i = 0; i < dockScoreBeen.size (); i++) {
                            scoreTitle.add (dockScoreBeen.get (i).getName ());
                        }
                        bridge.setScoreTitleList (scoreTitle);
                    }
                }).flatMap (new Func1<List<DockScoreEntity.DockScoreBean>, Observable<List<DockScoreEntity.DockScoreBean.OptionsBean>>> () {
                    @Override
                    public Observable<List<DockScoreEntity.DockScoreBean.OptionsBean>> call (List<DockScoreEntity.DockScoreBean> dockScoreBeen) {

                        //将内容转换成optionsbean的格式，在点击扣分详情时会使用到这些数据
                        List<DockScoreEntity.DockScoreBean.OptionsBean> optionsBeanList = new ArrayList<> ();
                        for (int i = 0; i < dockScoreBeen.size (); i++) {
                            DockScoreEntity.DockScoreBean.OptionsBean bean = new DockScoreEntity.DockScoreBean.OptionsBean ();
                            bean.setTitle (dockScoreBeen.get (i).getName ());
                            bean.setTitle (true);
                            optionsBeanList.add (bean);
                            for (int j = 0; j < dockScoreBeen.get (i).getOptions ().size (); j++) {
                                dockScoreBeen.get (i).getOptions ().get (j).setAllScore (dockScoreBeen.get (i).getAllScore ());
                            }
                            optionsBeanList.addAll (dockScoreBeen.get (i).getOptions ());
                        }

                        return Observable.just (optionsBeanList);
                    }
                }).subscribe (new Subscriber<List<DockScoreEntity.DockScoreBean.OptionsBean>> () {
                    @Override
                    public void onCompleted () {
                        bridge.onCompleted ();
                    }

                    @Override
                    public void onError (Throwable e) {
                        bridge.onError (e,date);
                    }

                    @Override
                    public void onNext (List<DockScoreEntity.DockScoreBean.OptionsBean> optionsBeen) {
                        optionsBeenList = optionsBeen;
                        if (!notNull){
                            bridge.onNext (optionsBeen);
                        }
                    }
                });
    }

    @Override
    public Subscription checkLoadClassPatrolState (BaseDataBridge baseDataBridge, int res) {
        bridge = (BaseDataBridge.PatrolRegisterBridge) baseDataBridge;
        int sectionId;
        if (res == 1) {
            sectionId = 0;
        } else {
            sectionId = bridge.getSctionId ();
        }
        return PatrolClassManager.checkLoadClassPatrolState (bridge.getUid (), bridge.getDate (), bridge.getClassId (), sectionId)
                .subscribe (new Subscriber<String> () {
                    @Override
                    public void onCompleted () {
                        bridge.onCompleted ();
                    }

                    @Override
                    public void onError (Throwable e) {
                        bridge.onCheckLoadClassPatrolStateError (e);
                    }

                    @Override
                    public void onNext (String s) {
                        bridge.onCheckLoadClassPatrolStateNext (s, res);
                    }
                });
    }

    /*********
     * 将数据转换为提交json数据
     ************/
    @Override
    public void convertToSubmitData (PatrolRegisterBean lPatrolRegisterBean) {
        Observable<DockScoreEntity> observable = Observable.just (dockScoreEntity1);
        observable.flatMap (new Func1<DockScoreEntity, Observable<SubmitRegisterPatrol>> () {
            @Override
            public Observable<SubmitRegisterPatrol> call (DockScoreEntity dockScoreEntity) {
                SubmitRegisterPatrol submit = new SubmitRegisterPatrol ();
                SubmitRegisterPatrol.JsonBean jsonBean = new SubmitRegisterPatrol.JsonBean ();
                submit.setUId (bridge.getUid ());
                submit.setXId (lPatrolRegisterBean.getxId ());
                submit.setPId (lPatrolRegisterBean.getpId ());
                SimpleDateFormat dateFormat = new SimpleDateFormat ("yyyy年MM月dd日");
                Date date = null;
                try {
                    date = dateFormat.parse (dockScoreEntity.getCurrentdate ());
                } catch (ParseException e) {
                    e.printStackTrace ();
                }
                SimpleDateFormat sdf = new SimpleDateFormat ("yyyy-MM-dd");
                String format = sdf.format (date);
                String mDate= "";
                if (lPatrolRegisterBean.getDate ().equals ("")){
                    mDate = format;
                }else {
                    mDate = lPatrolRegisterBean.getDate ();
                }
                jsonBean.setDate (mDate);
                jsonBean.setDeptId (lPatrolRegisterBean.getDeptId ());
                int sectionId = 0;
                if (lPatrolRegisterBean.getSectionId ()==0){
                    sectionId = dockScoreEntity.getSectionId ();
                }else {
                    sectionId = lPatrolRegisterBean.getSectionId ();
                }
                jsonBean.setSectionId (sectionId);
                jsonBean.setTeacherId (lPatrolRegisterBean.getTeacherId ());
                jsonBean.setClassId (lPatrolRegisterBean.getClassId ());
                jsonBean.setAutoScore (lPatrolRegisterBean.getAutoScore ());
                jsonBean.setType (lPatrolRegisterBean.getIsType ());
                List<SubmitRegisterPatrol.JsonBean.GetScoreListBean> getScoreListBeen = new ArrayList<> ();
                if (lPatrolRegisterBean.getAutoScore ().equals ("1")) {   //如果是自动计算分数
                    if (lPatrolRegisterBean.getEntityMap () != null) {    //如果resultMap不为空
                        getScoreListBeen = lPatrolRegisterBean.getGetScoreListBeens ();
                    } else {    //如果为空，说明没有进去到扣分详情页面，那么每项扣分项都是100分
                        for (int i = 0; i < dockScoreEntity1.getDockScore ().size (); i++) {
                            SubmitRegisterPatrol.JsonBean.GetScoreListBean bean = new SubmitRegisterPatrol.JsonBean.GetScoreListBean ();
                            bean.setName (dockScoreEntity1.getDockScore ().get (i).getName ());
                            bean.setGetScore (Integer.parseInt (dockScoreEntity1.getDockScore ().get (i).getGetAllScore ()));
                            bean.setId (dockScoreEntity1.getDockScore ().get (i).getId ());
                            bean.setResult (null);
                            getScoreListBeen.add (bean);
                        }
                    }
                } else {
                    for (int i = 0; i < lPatrolRegisterBean.getScores ().length; i++) {
                        SubmitRegisterPatrol.JsonBean.GetScoreListBean bean = new SubmitRegisterPatrol.JsonBean.GetScoreListBean ();
                        bean.setName (scoreTitle.get (i));
                        bean.setId (dockScoreEntity1.getDockScore ().get (i).getId ());
                        bean.setGetScore (Integer.valueOf (lPatrolRegisterBean.getScores ()[i]));
                        bean.setResult (null);
                        getScoreListBeen.add (bean);
                    }
                }
                jsonBean.setGetScoreList (getScoreListBeen);
                submit.setJson (jsonBean);
                return Observable.just (submit);
            }
        }).flatMap (new Func1<SubmitRegisterPatrol, Observable<List<SubmitEntity>>> () {
            @Override
            public Observable<List<SubmitEntity>> call (SubmitRegisterPatrol submitRegisterPatrol) {
                return PlanPatrolResultManager.uploadRegisterPatrol (submitRegisterPatrol);
            }
        }).subscribe (new Subscriber<List<SubmitEntity>> () {
            @Override
            public void onCompleted () {
                bridge.onSubmitCompleted ();
            }

            @Override
            public void onError (Throwable e) {
                bridge.onSubmitError (e);
            }

            @Override
            public void onNext (List<SubmitEntity> integer) {
                bridge.onSubmitNext (integer);
            }
        });
    }

}
