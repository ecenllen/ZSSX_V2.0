package com.gta.zssx.patrolclass.presenter;

import com.gta.utils.mvp.BasePresenter;
import com.gta.zssx.patrolclass.model.BaseDataBridge;
import com.gta.zssx.patrolclass.model.BaseModel;
import com.gta.zssx.patrolclass.model.GetChooseTeacherModelImp;
import com.gta.zssx.patrolclass.model.entity.ChooseTeacherEntity;
import com.gta.zssx.patrolclass.view.ChooseTeacherView;

import java.util.List;

/**
 * Created by liang.lu on 2016/8/16 14:54.
 */
public class ChooseTeacherPresenter extends BasePresenter<ChooseTeacherView>implements BaseDataBridge.GetChooseTeacherBridge {

    BaseModel.GetChooseTeacherModel getChooseTeacherModel;

    public void getChooseTeacherDatas(){
        getView ().showLoadingDialog (false);
        getChooseTeacherModel = new GetChooseTeacherModelImp ();
        mCompositeSubscription.add (getChooseTeacherModel.loadData (this));

    }
    @Override
    public void onNext (List<ChooseTeacherEntity> entities) {
        getView ().showResult (entities);
    }

    @Override
    public String getDeptId () {
        return getView ().getDeptId ();
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
