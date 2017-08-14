package com.gta.zssx.patrolclass.presenter;

import com.gta.utils.mvp.BasePresenter;
import com.gta.zssx.AppConfiguration;
import com.gta.zssx.fun.personalcenter.model.bean.UserBean;
import com.gta.zssx.patrolclass.model.PatrolClassManager;
import com.gta.zssx.patrolclass.model.PlanPatrolResultManager;
import com.gta.zssx.patrolclass.model.entity.PatrolRegisterBeanNew;
import com.gta.zssx.patrolclass.model.entity.RegisterDataSubmitEntity;
import com.gta.zssx.patrolclass.model.entity.RegisterInterfaceEntity;
import com.gta.zssx.patrolclass.model.entity.SubmitEntity;
import com.gta.zssx.patrolclass.view.PatrolRegisterViewNew;
import com.gta.zssx.patrolclass.view.page.DockScoreNewActivity;
import com.gta.zssx.pub.exception.PatrolClassException;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Func1;

/**
 * [Description]
 * <p>
 * [How to use]
 * <p>
 * [Tips] Created by liang.lu on 2016/7/19 14:02.
 */

public class PatrolRegisterPresenterNew extends BasePresenter<PatrolRegisterViewNew> {

    private Subscription mSubscription;
    private UserBean mUserBean;

    public PatrolRegisterPresenterNew () {
        super ();
        try {
            mUserBean = AppConfiguration.getInstance ().getUserBean ();
        } catch (Exception e) {
            e.printStackTrace ();
        }
    }

    /**
     * 请求登记数据
     */
    public void getPatrolRegisterData (RegisterInterfaceEntity entity) {

        /*
        getView ().showLoadingDialog ();
        mSubscription = PlanPatrolResultManager.getPatrolRegisterData (mUserBean.getUserId (),
                entity.getDeptId (), entity.getClassId (), entity.getLineDate (), entity.getSectionId ())
                .subscribe (new Subscriber<PatrolRegisterBeanNew> () {
                    @Override
                    public void onCompleted () {
                        getView ().hideDialog ();
                    }

                    @Override
                    public void onError (Throwable e) {
                        getView ().hideDialog ();
                        getView ().onErrorHandle (e);
                    }

                    @Override
                    public void onNext (PatrolRegisterBeanNew beanNew) {
                        getView ().setFragmentLists (beanNew);
                    }
                });
        mCompositeSubscription.add (mSubscription);*/
        PatrolRegisterBeanNew patrolRegisterBeanNew = PatrolRegisterBeanNew.getData (AppConfiguration.mContext);
        getView ().setFragmentLists (patrolRegisterBeanNew);
    }

    /**
     * 增加老师 ，请求数据
     */
    public void addTeacher () {
        PatrolRegisterBeanNew patrolRegisterBeanNew = PatrolRegisterBeanNew.getData (AppConfiguration.mContext);
        PatrolRegisterBeanNew.TeacherListBean teacherListBean = patrolRegisterBeanNew.getTeacherList ().get (0);
        teacherListBean.setTeacherName ("刘大脑袋");
        getView ().addTeacherData (teacherListBean);
    }

    /**
     * 日期验证接口
     *
     * @param entity 接口参数
     */
    public void VerificationDate (RegisterInterfaceEntity entity) {
        if (!isViewAttached ()) {
            return;
        }
        getView ().showLoadingDialog ();

        mSubscription = PatrolClassManager.checkLoadClassPatrolState (mUserBean.getUserId ()
                , entity.getLineDate (), entity.getClassId (), entity.getSectionId ())
                .subscribe (new Subscriber<String> () {
                    @Override
                    public void onCompleted () {
                        getView ().hideDialog ();
                    }

                    @Override
                    public void onError (Throwable e) {
                        if (getView () != null) {
                            getView ().hideDialog ();
                            if (e instanceof PatrolClassException) {
                                PatrolClassException pe = (PatrolClassException) e;
                                int code = pe.getCode ();
                                switch (code) {
                                    case PatrolClassException.DATA_IS_EMPTY:
                                        //当前巡视日期不属于任何一个学年学期 , 请重新选择
                                        getView ().showDateSelectErrorDialog (PatrolClassException.DATA_IS_EMPTY_STRING, true);
                                        break;
                                    case PatrolClassException.DATA_IS_NULL:
                                        //当前巡视日期所在的常年学期未启用对应的考核指标项 , 请通知系统管理员启用
                                        getView ().showDateSelectErrorDialog (PatrolClassException.DATA_IS_NULL_STRING, true);
                                        break;
                                    default:
                                        break;
                                }
                                return;
                            }
                            getView ().onErrorHandle (e);

                        }

                    }

                    @Override
                    public void onNext (String s) {
                        //验证日期通过，根据所选日期请求接口更新登记页面数据
                        getView ().refreshData (entity);
                    }
                });
        mCompositeSubscription.add (mSubscription);

    }

    /**
     * 提交登记数据
     *
     * @param mPatrolRegisterBean
     */
    public void submitRegisterData (PatrolRegisterBeanNew mPatrolRegisterBean) {
        if (!isViewAttached ()) {
            return;
        }
        getView ().showDialog ("正在提交中...");
        mSubscription = Observable.just (mPatrolRegisterBean)
                .flatMap (new Func1<PatrolRegisterBeanNew, Observable<RegisterDataSubmitEntity>> () {
                    @Override
                    public Observable<RegisterDataSubmitEntity> call (PatrolRegisterBeanNew beanNew) {
                        RegisterDataSubmitEntity mEntity = getSubmitEntity (beanNew);
                        return Observable.just (mEntity);
                    }
                }).flatMap (new Func1<RegisterDataSubmitEntity, Observable<List<SubmitEntity>>> () {
                    @Override
                    public Observable<List<SubmitEntity>> call (RegisterDataSubmitEntity registerDataSubmitEntity) {
                        return PlanPatrolResultManager.uploadRegisterData (registerDataSubmitEntity);
                    }
                }).subscribe (new Subscriber<List<SubmitEntity>> () {
                    @Override
                    public void onCompleted () {
                        getView ().hideDialog ();
                    }

                    @Override
                    public void onError (Throwable e) {
                        getView ().hideDialog ();
                        getView ().onErrorHandle (e);
                    }

                    @Override
                    public void onNext (List<SubmitEntity> submitEntities) {

                    }
                });

        mCompositeSubscription.add (mSubscription);
    }

    /**
     * 生成登记提交接口参数的实体
     *
     * @param beanNew
     * @return
     */
    private RegisterDataSubmitEntity getSubmitEntity (PatrolRegisterBeanNew beanNew) {
        RegisterDataSubmitEntity mEntity = new RegisterDataSubmitEntity ();
        mEntity.setUid (mUserBean.getUserId ());
        mEntity.setDeptId (beanNew.getDeptId ());
        mEntity.setXid (beanNew.getXId ());
        mEntity.setPid (beanNew.getPId ());

        RegisterDataSubmitEntity.RegisterDataJsonBean registerDataJsonBean = new RegisterDataSubmitEntity.RegisterDataJsonBean ();
        registerDataJsonBean.setType ((String) beanNew.getType ());
        registerDataJsonBean.setClassId (beanNew.getClassId ());
        registerDataJsonBean.setSectionId (beanNew.getSectionId ());
        registerDataJsonBean.setDate (beanNew.getCurrentdate ());

        List<RegisterDataSubmitEntity.RegisterDataJsonBean.TeacherListBean> teacherListBeen = new ArrayList<> ();
        for (int i = 0, ss = beanNew.getTeacherList ().size (); i < ss; i++) {
            PatrolRegisterBeanNew.TeacherListBean teacherBean = beanNew.getTeacherList ().get (i);
            RegisterDataSubmitEntity.RegisterDataJsonBean.TeacherListBean registerTeacherBean
                    = new RegisterDataSubmitEntity.RegisterDataJsonBean.TeacherListBean ();
            registerTeacherBean.setAutoScore (teacherBean.getAutoScore ());
            registerTeacherBean.setTeacherId (teacherBean.getTeacherId ());
            List<RegisterDataSubmitEntity.RegisterDataJsonBean.TeacherListBean.GetScoreListBean>
                    getScoreListBean = new ArrayList<> ();

            for (int j = 0, size = teacherBean.getDockScore ().size (); j < size; j++) {
                RegisterDataSubmitEntity.RegisterDataJsonBean.TeacherListBean.GetScoreListBean
                        getScoreBean = new RegisterDataSubmitEntity.RegisterDataJsonBean.TeacherListBean.GetScoreListBean ();
                getScoreBean.setName (teacherBean.getDockScore ().get (i).getName ());
                List<RegisterDataSubmitEntity.RegisterDataJsonBean.TeacherListBean.GetScoreListBean.ResultBean>
                        resultBeanList = new ArrayList<> ();
                if (teacherBean.getAutoScore ().equals (DockScoreNewActivity.AUTO_SCORE)) {
                    //自动计算,遍历options，把勾选的添加到提交bean中
                    getScoreBean.setGetScore (Integer.parseInt (teacherBean.getDockScore ().get (i).getGetAllScore ()));
                    for (int h = 0, length = teacherBean.getDockScore ().get (i).getOptions ().size (); h < length; h++) {
                        RegisterDataSubmitEntity.RegisterDataJsonBean.TeacherListBean.GetScoreListBean.ResultBean resultBean = new RegisterDataSubmitEntity.RegisterDataJsonBean.TeacherListBean.GetScoreListBean.ResultBean ();
                        if (teacherBean.getDockScore ().get (i).getOptions ().get (h).isIsCheck () || teacherBean.getDockScore ().get (i).getOptions ().get (h).getGetScore ().length () > 0) {
                            resultBean.setId (teacherBean.getDockScore ().get (i).getOptions ().get (h).getId ());
                            resultBean.setScore (teacherBean.getDockScore ().get (i).getOptions ().get (h).getInputScore ().length () < 0 ? Integer.parseInt (teacherBean.getDockScore ().get (i).getOptions ().get (h).getGetScore ()) : Integer.parseInt (teacherBean.getDockScore ().get (i).getOptions ().get (h).getInputScore ()));
                            resultBeanList.add (resultBean);
                        }
                    }
                    getScoreBean.setResult (resultBeanList);
                } else {
                    //非自动计算
                    getScoreBean.setGetScore (teacherBean.getDockScore ().get (i).getUnAutoScore ().length () > 0 ? Integer.parseInt (teacherBean.getDockScore ().get (i).getUnAutoScore ()) : Integer.parseInt (teacherBean.getDockScore ().get (i).getGetAllScore ()));
                    getScoreBean.setResult (null);
                }
                getScoreListBean.add (getScoreBean);
            }
            registerTeacherBean.setGetScoreList (getScoreListBean);
            teacherListBeen.add (registerTeacherBean);
        }
        registerDataJsonBean.setTeacherList (teacherListBeen);
        mEntity.setRegisterDataJson (registerDataJsonBean);
        return mEntity;
    }
}
