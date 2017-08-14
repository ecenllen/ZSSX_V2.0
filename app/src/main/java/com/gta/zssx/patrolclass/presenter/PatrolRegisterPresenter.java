package com.gta.zssx.patrolclass.presenter;

import com.gta.utils.mvp.BasePresenter;
import com.gta.zssx.AppConfiguration;
import com.gta.zssx.fun.personalcenter.model.bean.UserBean;
import com.gta.zssx.patrolclass.model.BaseDataBridge;
import com.gta.zssx.patrolclass.model.BaseModel;
import com.gta.zssx.patrolclass.model.PatrolRegisterModelImpl;
import com.gta.zssx.patrolclass.model.entity.DockScoreEntity;
import com.gta.zssx.patrolclass.model.entity.PatrolRegisterBean;
import com.gta.zssx.patrolclass.model.entity.SubmitEntity;
import com.gta.zssx.patrolclass.view.PatrolRegisterView;
import com.gta.zssx.pub.exception.PatrolClassException;
import com.gta.zssx.pub.util.LogUtil;

import java.util.List;

/**
 * [Description]
 * <p>
 * [How to use]
 * <p>
 * [Tips] Created by tengfei.lv on 2016/7/19 14:02.
 */

public class PatrolRegisterPresenter extends BasePresenter<PatrolRegisterView> implements
        BaseDataBridge.PatrolRegisterBridge {

    private List<DockScoreEntity.SectionsListBean> sectionsList;

    String uId;

    public DockScoreEntity getDockScoreEntity () {
        return mDockScoreEntity;
    }

    private DockScoreEntity mDockScoreEntity;

    @Override
    public void onNext (List<DockScoreEntity.DockScoreBean.OptionsBean> optionsBeen) {
        optionsBeenList = optionsBeen;
        if (getView () != null) {
            getView ().showResult (optionsBeen);
        }
    }

    @Override
    public void getEntity (DockScoreEntity dockScoreEntity) {
        mDockScoreEntity = dockScoreEntity;
    }

    @Override
    public void setScoreTitleList (List<String> scoreTitle) {
        getView ().createScoreTitle (scoreTitle);
    }

    @Override
    public void setDockScoreEntity (DockScoreEntity dockScoreEntity) {
        if (dockScoreEntity == null) {
            getView ().showError ("暂无数据");
        } else {
            getView ().showSuccessData (dockScoreEntity);
        }
    }

    @Override
    public void setSectionList (List<DockScoreEntity.SectionsListBean> sectionsList) {
        this.sectionsList = sectionsList;
    }

    @Override
    public void onError (Throwable e) {


    }

    @Override
    public void onCompleted () {
        if(getView() == null)
            return;
        getView ().hideDialog ();
    }

    @Override
    public void onSubmitCompleted () {
        if(getView() == null)
            return;
        getView ().hideDialog ();
    }

    @Override
    public void onSubmitError (Throwable e) {
        if(getView() == null)
            return;
        getView ().hideDialog ();
        if (e instanceof NullPointerException){
            getView ().showTimeDilog (PatrolClassException.IS_SUBMIT_NOW_STRING);
        }else {

            getView ().onErrorHandle (e);
        }
        LogUtil.e ("==" + e.getMessage ());
    }

    @Override
    public void onSubmitNext (List<SubmitEntity> integer) {
        if(getView() == null)
            return;
        getView ().showSubmitNext (integer);
    }

    @Override
    public String getUid () {
        return uId;
    }

    @Override
    public String getDeptId () {
        return getView ().getDeptId ();
    }

    @Override
    public int getClassId () {
        return getView ().getClassId ();
    }

    @Override
    public String getDate () {
        return getView ().getDate ();
    }

    @Override
    public int getSctionId () {
        return getView ().getSectionId ();
    }

    @Override
    public int getXid () {
        return getView ().getXid ();
    }

    @Override
    public void onCheckLoadClassPatrolStateNext (String s, int res) {
        if(getView() == null)
            return;
        if (res == 1) {
            getView ().requestData ();
        } else {
            getView ().clickSave ();
        }
    }

    @Override
    public void onCheckLoadClassPatrolStateError (Throwable throwable) {
        if(getView() == null)
            return;
        getView ().hideDialog ();
        if (throwable instanceof PatrolClassException) {
            PatrolClassException e = (PatrolClassException) throwable;
            int code = e.getCode ();
            switch (code) {
                case PatrolClassException.DATA_IS_EMPTY:
                    getView ().showTimeDilog ( PatrolClassException.DATA_IS_EMPTY_STRING);
                    break;
                case PatrolClassException.DATA_IS_NULL:
                    getView ().showTimeDilog (PatrolClassException.DATA_IS_NULL_STRING);
                    break;
                case PatrolClassException.HAS_PATROL_CLASS:
                    getView ().showDilog (true, PatrolClassException.HAS_PATROL_CLASS_STTING);
                    break;
                default:
                    break;
            }
        } else {
            getView ().onErrorHandle (throwable);
        }
    }

    @Override
    public void onError (Throwable e, String date) {
        if(getView() == null)
            return;
        getView ().hideDialog ();
        if (e instanceof PatrolClassException) {
            PatrolClassException exception = (PatrolClassException) e;
            if (exception.getCode () == 105) {
                getView ().showTimeDilog (date+"巡课记录已提交审核，请选择其它日期");
            }
        } else if (e instanceof NullPointerException) {
            e.printStackTrace();
            //            getView ().showError ("暂无数据");
        } else {
            getView ().onErrorHandle (e);
        }
    }

    private static class SingletonHolder {
        private static final PatrolRegisterPresenter INSTANCE = new PatrolRegisterPresenter ();
    }

    public PatrolRegisterPresenter () {
    }

    public static PatrolRegisterPresenter getInstance () {
        return SingletonHolder.INSTANCE;
    }

    private DockScoreEntity dockScoreEntity;
    //记录扣分详情列表
    private List<DockScoreEntity.DockScoreBean.OptionsBean> optionsBeenList;

    public List<DockScoreEntity.SectionsListBean> getSectionsBeanList () {
        return sectionsList;
    }

    public List<DockScoreEntity.DockScoreBean.OptionsBean> getOptionsBeen () {
        return optionsBeenList;
    }


    private BaseModel.PatrolRegisterModel patrolRegisterModel;

    //加载数据
    public void loadData (boolean notNull,String date) {
        if (!isViewAttached ())
            return;
        UserBean lUserBean = null;
        try {
            lUserBean = AppConfiguration.getInstance ().getUserBean ();
            uId = lUserBean.getUserId ();

        } catch (Exception e) {
            e.printStackTrace ();
        }
        
        patrolRegisterModel = new PatrolRegisterModelImpl ();
		getView ().showLoadingDialog (false);
        mCompositeSubscription.add (patrolRegisterModel.loadData (this,notNull,date));
    }

    //巡课登记页面，点击完成之后的操作
    public void finishRegisterPatrol (PatrolRegisterBean lPatrolRegisterBean) {
        if (!isViewAttached ()) {
            return;
        }
        getView ().showDialog ("正在提交中...");
        patrolRegisterModel.convertToSubmitData (lPatrolRegisterBean);
    }

    private String sectionName;
    private String teacherName;
    private int sectionId;

    public int getSectionId2 () {
        return sectionId;
    }

    public String getSectionName () {
        return sectionName;
    }

    public String getTeacherName () {
        return teacherName;
    }

    /**
     * 节次选择完成之后，进行处理 如果没有选择，提示用户进行选择 如果已经选择，就把选择之后的节次名称和教师显示在页面上
     */
    public boolean finishSectionChoose (List<DockScoreEntity.SectionsListBean> sectionsList) {
        //遍历section集合是否有选中的
        for (int i = 0; i < sectionsList.size (); i++) {
            if (sectionsList.get (i).getType () == 0) {
                if (sectionsList.get (i).isCheck ()) {
                    sectionName = sectionsList.get (i).getSectionName ();
                    sectionId = sectionsList.get (i).getSectionId ();
                    //                    teacherName = sectionsList.get (i).getTeacherName ();
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 验证数据
     *
     * @param res 是日期验证还是登记完成验证  1  日期验证   2  登记完成验证
     */
    public void checkLoadClassPatrolState (int res) {
        if (!isViewAttached ()) {
            return;
        }

        if (res == 1) {
            getView ().showLoadingDialog (false);
        } else {
            getView ().showDialog ("正在提交中...");
        }

        mCompositeSubscription.add (patrolRegisterModel.checkLoadClassPatrolState (this, res));
    }
}
