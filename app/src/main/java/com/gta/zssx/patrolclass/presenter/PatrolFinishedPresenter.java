package com.gta.zssx.patrolclass.presenter;

import com.gta.utils.mvp.BasePresenter;
import com.gta.zssx.AppConfiguration;
import com.gta.zssx.fun.personalcenter.model.bean.UserBean;
import com.gta.zssx.patrolclass.model.BaseDataBridge;
import com.gta.zssx.patrolclass.model.BaseModel;
import com.gta.zssx.patrolclass.model.GetPatrolClassModelImp;
import com.gta.zssx.patrolclass.model.entity.PatrolClassEntity;
import com.gta.zssx.patrolclass.view.PatrolClassView;
import com.gta.zssx.patrolclass.view.page.NotFinishedFragment;
import com.gta.zssx.pub.util.LogUtil;

import java.util.List;

import rx.Subscription;

/**
 * Created by liang.lu1 on 2016/7/8.
 */
public class PatrolFinishedPresenter extends BasePresenter<PatrolClassView> implements BaseDataBridge.GetPatrolClassBridge {

    private List<PatrolClassEntity> mPatrolClassModels;
    public Subscription mSubscription;
    private BaseModel.GetPatrolClassModel getPatrolClassModel;
    private int page = 1;
    String uId;

    public void getFinishedData () {
        if (!isViewAttached ())
            return;
        UserBean lUserBean = null;
        try {
            lUserBean = AppConfiguration.getInstance ().getUserBean ();
            uId = lUserBean.getUserId ();
        } catch (Exception e) {
            e.printStackTrace ();
        }
        page = 1;
        getPatrolClassModel = new GetPatrolClassModelImp ();
        getView ().showLoadingDialog (false);
        mCompositeSubscription.add (getPatrolClassModel.loadData (this));
    }

    @Override
    public void onNext (List<PatrolClassEntity> patrolClassEntities) {
        if (patrolClassEntities == null) {
            getView ().showEmpty ("", false);
        } else {
            getView ().showResult (patrolClassEntities);
            mPatrolClassModels = patrolClassEntities;
        }
    }

    @Override
    public String getUid () {
        return uId;
    }

    @Override
    public int getType () {
        return 2;
    }

    @Override
    public void onRefreshError (Throwable e) {
        getView ().onRefreshError ();
        getView ().onErrorHandle (e);
        page = 1;
    }

    @Override
    public void onRefreshCompleted (List<PatrolClassEntity> patrolClassEntities) {
        if (patrolClassEntities == null) {
            getView ().showEmpty (NotFinishedFragment.REFRESH, true);
            return;
        }
        mPatrolClassModels = patrolClassEntities;
        getView ().hideLoadMoreAndRefresh (patrolClassEntities);
    }

    @Override
    public String getEndDate () {
        return "";
    }

    @Override
    public String getStartDate () {
        return "";
    }

    @Override
    public void onLoadMoreError (Throwable e) {
        LogUtil.e ("==" + e);
        if (e instanceof NullPointerException) {
            getView ().onLoadMoreEmpty ();
        } else {
            getView ().onLoadMoreError ();
        }
        page--;
    }

    @Override
    public void onLoadMoreCompleted (List<PatrolClassEntity> patrolClassEntities) {
        mPatrolClassModels.addAll (patrolClassEntities);
        getView ().hideLoadMoreAndRefresh (mPatrolClassModels);
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
     * 加载更多
     * 需要改变页码
     */
    public void doLoadMore () {
        page++;
        mCompositeSubscription.add (getPatrolClassModel.onLoadMore (this, page, "", ""));
    }

    /**
     * 刷新，将page置为1
     */
    public void doRefresh () {
        page = 1;
        mCompositeSubscription.add (getPatrolClassModel.onRefresh (this, "", ""));
    }
}
