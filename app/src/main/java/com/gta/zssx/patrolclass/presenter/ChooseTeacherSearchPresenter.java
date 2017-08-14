package com.gta.zssx.patrolclass.presenter;

import com.gta.utils.mvp.BasePresenter;

import com.gta.zssx.patrolclass.model.BaseDataBridge;
import com.gta.zssx.patrolclass.model.BaseModel;
import com.gta.zssx.patrolclass.model.GetTeacherSearchModelImp;
import com.gta.zssx.patrolclass.model.entity.ChooseTeacherSearchEntity;
import com.gta.zssx.patrolclass.view.ChooseTeacherSearchView;

import java.util.List;

/**
 * Created by liang.lu on 2016/8/17 15:00.
 */
public class ChooseTeacherSearchPresenter extends BasePresenter<ChooseTeacherSearchView> implements BaseDataBridge.GetTeacherSearchBridge {



    BaseModel.GetTeacherSearchModel getTeacherSearchModel;

    public void getTeacherSearchDatas () {
        getTeacherSearchModel = new GetTeacherSearchModelImp();
        getView ().showLoadingDialog (false);
        mCompositeSubscription.add (getTeacherSearchModel.loadData (this));
    }

    @Override
    public void onNext (List<ChooseTeacherSearchEntity> entities) {
        if (entities == null){
            getView ().showError ("无匹配的搜索记录");
        }else {
            getView ().showResult (entities);
        }
    }

    @Override
    public String GetkeyWord () {
        return getView ().getKeyWord ();
    }

    @Override
    public void onError (Throwable e) {
        getView ().hideDialog ();
        getView ().showError (e.getMessage ());
    }

    @Override
    public void onCompleted () {
        getView ().hideDialog ();
    }
}
