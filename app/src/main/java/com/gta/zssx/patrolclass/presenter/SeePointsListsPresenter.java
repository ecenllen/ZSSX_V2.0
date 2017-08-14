package com.gta.zssx.patrolclass.presenter;

import com.gta.utils.mvp.BasePresenter;
import com.gta.zssx.AppConfiguration;
import com.gta.zssx.fun.personalcenter.model.bean.UserBean;
import com.gta.zssx.patrolclass.model.BaseDataBridge;
import com.gta.zssx.patrolclass.model.BaseModel;
import com.gta.zssx.patrolclass.model.GetSeePointsListsModelImp;
import com.gta.zssx.patrolclass.model.entity.SeePointsListsEntity;
import com.gta.zssx.patrolclass.view.SeePointsListsView;

import java.util.ArrayList;
import java.util.List;

import rx.Subscription;

/**
 * Created by liang.lu1 on 2016/7/14.
 * 查看每条巡课详情记录
 */
public class SeePointsListsPresenter extends BasePresenter<SeePointsListsView>implements BaseDataBridge.GetSeePointsListsBridge {
    public Subscription mSubscription;
    //记录扣分标题
    private List<String> scoreTitle;
    //记录每个指标扣得总分数
    private List<String> allScore;
    private BaseModel.GetSeePointsListsModel getSeePointsListsModel;
    String uId;

    public void getSeePointsListsData(){
        if (!isViewAttached ())
            return;
        UserBean lUserBean = null;
        try {
            lUserBean = AppConfiguration.getInstance ().getUserBean ();
            uId = lUserBean.getUserId ();
        } catch (Exception e) {
            e.printStackTrace ();
        }

        getView ().showLoadingDialog (false);
        getSeePointsListsModel = new GetSeePointsListsModelImp ();
        mCompositeSubscription.add (getSeePointsListsModel.loadData (this));

    }

    @Override
    public void onNext (SeePointsListsEntity seePointsListsEntities) {
        scoreTitle = new ArrayList<> ();
        allScore = new ArrayList<> ();
        for (int i = 0; i < seePointsListsEntities.getGetScoreList ().size (); i++) {
            scoreTitle.add (seePointsListsEntities.getGetScoreList ().get (i).getName ());
            allScore.add (seePointsListsEntities.getGetScoreList ().get (i).getScore ());
        }
        getView ().showResult (seePointsListsEntities);
        getView ().createScoreTitle (scoreTitle, seePointsListsEntities, allScore);
    }

    @Override
    public String getUid () {
        return uId;
    }

    @Override
    public String getPid () {
        return getView ().getPid ();
    }

    @Override
    public void onError (Throwable e) {
        getView ().hideDialog ();
        if (e.getMessage () == null||e instanceof NullPointerException){
            getView ().showEmpty ();
        }else {
            getView ().onErrorHandle (e);
        }
    }

    @Override
    public void onCompleted () {
        getView ().hideDialog ();
    }
}
