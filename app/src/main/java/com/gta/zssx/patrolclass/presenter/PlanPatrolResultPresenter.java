package com.gta.zssx.patrolclass.presenter;

import com.gta.utils.mvp.BasePresenter;
import com.gta.zssx.AppConfiguration;
import com.gta.zssx.fun.personalcenter.model.bean.UserBean;
import com.gta.zssx.patrolclass.model.BaseDataBridge;
import com.gta.zssx.patrolclass.model.BaseModel;
import com.gta.zssx.patrolclass.model.PlanPatrolResultModelImpl;
import com.gta.zssx.patrolclass.model.entity.PlanPatrolResultEntity;
import com.gta.zssx.patrolclass.view.PlanPatrolResultView;
import com.gta.zssx.pub.util.LogUtil;

import java.util.List;

/**
 * [Description]
 * <p>
 * [How to use]
 * <p>
 * [Tips] Created by tengfei.lv on 2016/7/14 17:02.
 */

public class PlanPatrolResultPresenter extends BasePresenter<PlanPatrolResultView> implements
        BaseDataBridge.PlanPatrolResultBridge {

    String uId;

    private BaseModel.PlanPatrolResultModel planPatrolResultModel;

    public void loadPlanClassResultDatas () {
        if (!isViewAttached ())
            return;
        UserBean lUserBean = null;
        try {
            lUserBean = AppConfiguration.getInstance ().getUserBean ();
            uId = lUserBean.getUserId ();

        } catch (Exception e) {
            e.printStackTrace ();
        }
        planPatrolResultModel = new PlanPatrolResultModelImpl ();

        getView ().showLoadingDialog (false);

        mCompositeSubscription.add (planPatrolResultModel.loadData (this));

    }

    public void deletePatrolClassItem () {
        if(getView() == null) return;
        getView ().showLoadingDialog (false);
        mCompositeSubscription.add (planPatrolResultModel.deletePatrolClassItem (this));
    }

    @Override
    public void onNext (List<PlanPatrolResultEntity> entityList) {
        if(getView() == null) return;
        if (entityList == null || entityList.size () == 0) {
            getView ().startAddClassPage ();
            return;
        }
        getView ().showSuccessData (entityList);

    }

    @Override
    public String getUid () {
        return uId;
    }

    @Override
    public void onDeleteNext (String s) {
        if(getView() == null) return;
        getView ().DeleteClassItemSuccess ();
    }

    @Override
    public void onDeleteError (Throwable e) {
        if(getView() == null) return;
        getView ().hideDialog ();
        getView ().onErrorHandle (e);
        //        getView ().showError ("删除失败");
    }

    @Override
    public int getDeptId () {
        if(getView() == null) return 0;
        return getView ().getDeptId ();
    }

    @Override
    public int getClassId () {
        if(getView() == null) return 0;
        return getView ().getClassId ();
    }

    @Override
    public void onError (Throwable e) {
        if(getView() == null) return;

        LogUtil.e ("===" + e.getMessage ());
        if (e.getMessage () == null || e instanceof NullPointerException) {
            getView ().startAddClassPage ();
        }
        getView ().hideDialog ();
        getView ().onErrorHandle (e);
    }

    @Override
    public void onCompleted () {
        if(getView() == null) return;
        getView ().hideDialog ();
    }
}
