package com.gta.zssx.patrolclass.presenter;

import com.gta.utils.mvp.BasePresenter;
import com.gta.zssx.AppConfiguration;
import com.gta.zssx.fun.personalcenter.model.bean.UserBean;
import com.gta.zssx.patrolclass.model.BaseDataBridge;
import com.gta.zssx.patrolclass.model.BaseModel;
import com.gta.zssx.patrolclass.model.entity.PatrolClassEntity;
import com.gta.zssx.patrolclass.model.getSearchResultModelImp;
import com.gta.zssx.patrolclass.view.SearchResultView;
import com.gta.zssx.patrolclass.view.page.NotFinishedFragment;
import com.gta.zssx.pub.util.LogUtil;

import java.util.List;

/**
 * Created by liang.lu1 on 2016/7/19.
 * 搜索时间段巡课记录Presenter
 */
public class SearchResultPresenter extends BasePresenter<SearchResultView> implements BaseDataBridge.GetSearchResultBridge {

    String uId;
    private BaseModel.GetSearchResultModel getSearchResultModel;
    private List<PatrolClassEntity> mPatrolClassModels;

    public void getSearchResultData () {
        if (!isViewAttached ())
            return;
        UserBean lUserBean = null;
        try {
            lUserBean = AppConfiguration.getInstance ().getUserBean ();
            uId = lUserBean.getUserId ();
        } catch (Exception e) {
            e.printStackTrace ();
        }
        getSearchResultModel = new getSearchResultModelImp ();
        getView ().showLoadingDialog (false);
        mCompositeSubscription.add (getSearchResultModel.loadData (this));
    }

    @Override
    public void onNext (List<PatrolClassEntity> entities) {
        if (entities == null) {
            getView ().showEmpty ("", false);
        } else {
            mPatrolClassModels = entities;
            getView ().showResult (entities);
        }
    }

    @Override
    public String getUid () {
        return uId;
    }

    @Override
    public int getType () {
        return 0;
    }

    @Override
    public String getStartDate () {
        return getView ().getStartDate ();
    }

    @Override
    public String getEndDate () {
        return getView ().getEndDate ();
    }

    @Override
    public void onRefreshError (Throwable e) {
        getView ().onRefreshError ();
        getView ().onErrorHandle (e);
        page = 1;
        isRefresh = false;
    }

    @Override
    public void onRefreshCompleted (List<PatrolClassEntity> patrolClassEntities) {
        if (patrolClassEntities == null) {
            getView ().showEmpty (NotFinishedFragment.REFRESH, true);
            return;
        }
        mPatrolClassModels = patrolClassEntities;
        getView ().hideLoadMoreAndRefresh (patrolClassEntities);
        isRefresh = false;
    }

    @Override
    public void onLoadMoreError (Throwable e) {
        LogUtil.e ("haha" + e);
        if (e instanceof NullPointerException) {
            getView ().onLoadMoreEmpty ();
        } else {
            getView ().onLoadMoreError ();
        }
        page--;
        isLoadMore = false;
    }

    @Override
    public void onLoadMoreCompleted (List<PatrolClassEntity> patrolClassEntities) {
        mPatrolClassModels.addAll (patrolClassEntities);
        getView ().hideLoadMoreAndRefresh (mPatrolClassModels);
        isLoadMore = false;
    }

    @Override
    public void onError (Throwable e) {
        getView ().hideDialog ();
        //        getView ().showWarning (e.getMessage ());
        getView ().onErrorHandle (e);
        getView ().showEmpty ("", false);
    }

    @Override
    public void onCompleted () {
        getView ().hideDialog ();
    }

    private boolean isLoadMore;

    private int page = 1;

    /**
     * 加载更多
     * 需要改变页码
     */
    public void doLoadMore () {
        page++;
        isLoadMore = true;
        mCompositeSubscription.add (getSearchResultModel.onLoadMore (this, page));
    }

    private boolean isRefresh;

    /**
     * 刷新，将page置为1
     */
    public void doRefresh () {
        page = 1;
        isRefresh = true;
        mCompositeSubscription.add (getSearchResultModel.onRefresh (this));
    }
}
