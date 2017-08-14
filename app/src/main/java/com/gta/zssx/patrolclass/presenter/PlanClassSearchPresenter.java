package com.gta.zssx.patrolclass.presenter;

import com.gta.utils.mvp.BasePresenter;
import com.gta.zssx.AppConfiguration;
import com.gta.zssx.fun.personalcenter.model.bean.UserBean;
import com.gta.zssx.patrolclass.model.BaseDataBridge;
import com.gta.zssx.patrolclass.model.BaseModel;
import com.gta.zssx.patrolclass.model.QueryClassModelImp;
import com.gta.zssx.patrolclass.model.entity.AddClassEntity;
import com.gta.zssx.patrolclass.model.entity.PlanClassSearchEntity;
import com.gta.zssx.patrolclass.view.PlanClassSearchView;
import com.gta.zssx.pub.exception.PatrolClassException;

import java.util.List;

/**
 * Created by liang.lu on 2016/8/23 14:04.
 */
public class PlanClassSearchPresenter extends BasePresenter<PlanClassSearchView> implements BaseDataBridge.PlanClassSearchBridge {

    BaseModel.QueryClassModel queryClassModel;
    private String uId;


    public void getPlanClassSearchDatas () {
        if (!isViewAttached ())
            return;
        UserBean lUserBean = null;
        try {
            lUserBean = AppConfiguration.getInstance ().getUserBean ();
            uId = lUserBean.getUserId ();

        } catch (Exception e) {
            e.printStackTrace ();
        }

        queryClassModel = new QueryClassModelImp ();
        getView ().showLoadingDialog (false);
        mCompositeSubscription.add (queryClassModel.loadData (this));
    }

    @Override
    public void onNext (List<PlanClassSearchEntity> entities) {
        if (entities == null) {
            getView ().showError ("无匹配的搜索记录");
        } else {
            getView ().showResult (entities);
        }
    }

    @Override
    public String GetkeyWord () {
        return getView ().getKeyWord ();
    }

    @Override
    public void getOnAddClassNext (String s) {
        getView ().intoClassesPage ();
    }

    @Override
    public void onAddClassError (Throwable e) {
        getView ().hideDialog ();
        if (e instanceof PatrolClassException) {
            PatrolClassException exception = (PatrolClassException) e;
            int code = exception.getCode ();
            switch (code) {
                case PatrolClassException.HAS_CLASS_ADD:
                    getView ().showError (PatrolClassException.HAS_CLASS_ADD_STRING);
                    break;
                case PatrolClassException.HAS_CLASS_ADD_ALL:
                    getView ().showError (PatrolClassException.HAS_CLASS_ADD_ALL_STRING);
                    break;
                default:
                    break;
            }
        } else {
            getView ().onErrorHandle (e);
        }
    }

    @Override
    public List<AddClassEntity> getEntity () {
        return getView ().getEntity ();
    }

    @Override
    public void onError (Throwable e) {
        getView ().hideDialog ();
        getView ().onErrorHandle (e);
    }

    @Override
    public void onCompleted () {
        getView ().hideDialog ();
    }

    /**
     * 上传班级已选班级列表数据
     */
    public void uploadChooseClass () {
        getView ().showLoadingDialog (false);
        mCompositeSubscription.add (queryClassModel.upLoadPlanClassList (this));
    }

    public String getUid () {
        return uId;
    }
}
