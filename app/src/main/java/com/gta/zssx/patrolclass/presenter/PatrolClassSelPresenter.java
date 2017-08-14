package com.gta.zssx.patrolclass.presenter;

import com.gta.utils.mvp.BasePresenter;
import com.gta.zssx.AppConfiguration;
import com.gta.zssx.fun.personalcenter.model.bean.UserBean;
import com.gta.zssx.patrolclass.model.BaseDataBridge;
import com.gta.zssx.patrolclass.model.BaseModel;
import com.gta.zssx.patrolclass.model.GetPatrolClassSelModelImp;
import com.gta.zssx.patrolclass.model.entity.PatrolClassSelEntity;
import com.gta.zssx.patrolclass.view.PatrolClassSelView;
import com.gta.zssx.pub.util.LogUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by liang.lu1 on 2016/7/13.
 * 课堂巡视详情列表 和 提交送审
 */
public class PatrolClassSelPresenter extends BasePresenter<PatrolClassSelView> implements
        BaseDataBridge.GetPatrolClassSelBridge {

    public List<PatrolClassSelEntity> mPatrolClassSelModels;
    private BaseModel.GetPatrolClassSelModel getPatrolClassSelModel = new GetPatrolClassSelModelImp ();
    String Uid;

    public void getPatrolClassSelData () {
        if (!isViewAttached ())
            return;
        UserBean lUserBean = null;
        try {
            lUserBean = AppConfiguration.getInstance ().getUserBean ();
            Uid = lUserBean.getUserId ();
        } catch (Exception e) {
            e.printStackTrace ();
        }
        getView ().showLoadingDialog (false);

        mCompositeSubscription.add (getPatrolClassSelModel.loadData (this));

    }

    @Override
    public void onNext (List<PatrolClassSelEntity> patrolClassSelEntities) {
        if (patrolClassSelEntities != null) {
            mPatrolClassSelModels = patrolClassSelEntities;
            getView ().showResult (patrolClassSelEntities);
        } else {
            getView ().showEmpty ();
        }

    }

    @Override
    public String getUid () {
        return Uid;
    }

    @Override
    public int getXid () {
        return getView ().getXid ();
    }

    @Override
    public String getPid () {
        return getView ().getPid ();
    }


    @Override
    public void onError (Throwable e) {
        getView ().hideDialog ();
        getView ().onErrorHandle (e);
        if (e.getMessage () == null) {
            getView ().showEmpty ();
        }
    }

    @Override
    public void onCompleted () {
        getView ().hideDialog ();
    }

    /**
     * 一键送审
     */
    public void submitApproval () {
        getView ().showLoadingDialog (false);
        mCompositeSubscription.add (getPatrolClassSelModel.submitApproval (this));
    }

    /**
     * 删除巡课记录item
     */
    public void DeletePatrolItem () {
        getView ().showLoadingDialog (false);
        mCompositeSubscription.add (getPatrolClassSelModel.deletePatrolItem (this));
    }

    @Override
    public void onDeleteNext (String s) {
        getView ().DeleteItemSuccess ();
    }

    @Override
    public void onDeleteError (Throwable e) {
        getView ().hideDialog ();
        getView ().onErrorHandle (e);
    }

    @Override
    public void onRefreshError (Throwable e) {
        getView ().onRefreshError ();
        page = 1;
        isRefresh = false;
    }

    @Override
    public void onRefreshCompleted (List<PatrolClassSelEntity> patrolClassSelEntities) {
        if (patrolClassSelEntities == null) {
            getView ().showEmpty ();
            return;
        }
        mPatrolClassSelModels = patrolClassSelEntities;
        getView ().hideLoadMoreAndRefresh (patrolClassSelEntities);
        isRefresh = false;
    }

    @Override
    public void onLoadMoreError (Throwable e) {
        if (e instanceof NullPointerException) {
            getView ().onLoadMoreEmpty ();
        } else {
            getView ().onLoadMoreError ();
        }
        page--;
        isLoadMore = false;
    }

    @Override
    public void onLoadMoreCompleted (List<PatrolClassSelEntity> patrolClassSelEntities) {
        mPatrolClassSelModels.addAll (patrolClassSelEntities);
        getView ().hideLoadMoreAndRefresh (mPatrolClassSelModels);
        isLoadMore = false;
    }

    @Override
    public void onSubmitApprovalNext (String s) {
        getView ().submitSuccess ();
    }

    @Override
    public void onSubmitApprovalError (Throwable e) {
        LogUtil.e ("result:" + e);
        getView ().hideDialog ();
        //        getView ().showError ("提交失败！");
        getView ().onErrorHandle (e);
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
        mCompositeSubscription.add (getPatrolClassSelModel.onLoadMore (this, page));
    }

    private boolean isRefresh;

    /**
     * 刷新，将page置为1
     */
    public void doRefresh () {
        page = 1;
        isRefresh = true;
        mCompositeSubscription.add (getPatrolClassSelModel.onRefresh (this));
    }

    /**
     * 设置该页面的日期的方法
     *
     * @param date
     * @param Ttype
     * @return
     */
    public String setDateTv (String date, int Ttype) {
        SimpleDateFormat sdf;
        Date mDate = null;
        if (Ttype == 1) {
            sdf = new SimpleDateFormat ("yyyy-MM-dd");
            String dstr = date.substring (0, date.length () - 5);
            LogUtil.e ("woshi" + dstr);
            try {
                mDate = sdf.parse (dstr);
                String week = getDayOfWeek (mDate);
                return dstr + "  " + week;
            } catch (ParseException e) {
                e.printStackTrace ();
            }

        } else {
            sdf = new SimpleDateFormat ("yyyy年MM月dd日");
            try {
                mDate = sdf.parse (date);
                sdf = new SimpleDateFormat ("yyyy-MM-dd");
                String dstr = sdf.format (mDate);
                String week = getDayOfWeek (mDate);
                return dstr + "  " + week;
            } catch (ParseException e) {
                e.printStackTrace ();
            }
        }
        return null;
    }

    public String setDate (String date, int Ttype) {
        SimpleDateFormat sdf;
        Date mDate = null;
        if (Ttype == 1) {
            //            sdf = new SimpleDateFormat ("yyyy-MM-dd");
            String dstr = date.substring (0, date.length () - 5);
            LogUtil.e ("woshi" + dstr);

            return dstr;
            //            try {
            //                mDate = sdf.parse (dstr);
            //                String week = getDayOfWeek (mDate);
            //                return dstr + "  " + week;
            //            } catch (ParseException e) {
            //                e.printStackTrace ();
            //            }

        } else {
            sdf = new SimpleDateFormat ("yyyy年MM月dd日");
            try {
                mDate = sdf.parse (date);
                sdf = new SimpleDateFormat ("yyyy-MM-dd");
                String dstr = sdf.format (mDate);
                //                String week = getDayOfWeek (mDate);
                return dstr;
            } catch (ParseException e) {
                e.printStackTrace ();
            }
        }
        return null;
    }

    public String getDayOfWeek (Date date) {
        String[] weekDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        Calendar lCalendar = Calendar.getInstance ();
        lCalendar.setTime (date);
        int w = lCalendar.get (Calendar.DAY_OF_WEEK) - 1;
        if (w < 0) {
            w = 0;
        }
        return weekDays[w];
    }
}
