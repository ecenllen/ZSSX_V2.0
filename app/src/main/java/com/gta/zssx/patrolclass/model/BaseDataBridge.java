package com.gta.zssx.patrolclass.model;

import android.util.SparseIntArray;

import com.gta.zssx.patrolclass.model.entity.AddClassEntity;
import com.gta.zssx.patrolclass.model.entity.ChooseTeacherEntity;
import com.gta.zssx.patrolclass.model.entity.ChooseTeacherSearchEntity;
import com.gta.zssx.patrolclass.model.entity.ClassChooseEntity;
import com.gta.zssx.patrolclass.model.entity.DockScoreEntity;
import com.gta.zssx.patrolclass.model.entity.PatrolClassEntity;
import com.gta.zssx.patrolclass.model.entity.PatrolClassSelEntity;
import com.gta.zssx.patrolclass.model.entity.PlanClassSearchEntity;
import com.gta.zssx.patrolclass.model.entity.PlanPatrolResultEntity;
import com.gta.zssx.patrolclass.model.entity.SeePointsListsEntity;
import com.gta.zssx.patrolclass.model.entity.SubmitEntity;
import com.gta.zssx.pub.widget.easysidebar.EasySection;

import java.util.List;

/**
 * [Description]
 * <p>
 * [How to use]
 * <p>
 * [Tips] Created by tengfei.lv on 2016/7/28 9:48.
 */

public interface BaseDataBridge {
    /**
     * 网络访问出错时调用
     *
     * @param e
     */
    void onError (Throwable e);

    /**
     * 处理完成之后调用
     */
    void onCompleted ();

    /**
     * 选择班级完成之后的结果页面
     */
    interface PlanPatrolResultBridge extends BaseDataBridge {
        void onNext (List<PlanPatrolResultEntity> entityList);

        String getUid ();

        void onDeleteNext (String s);

        void onDeleteError (Throwable e);

        int getDeptId ();

        int getClassId ();
    }

    /**
     * 搜索班级
     */
    interface PlanClassSearchBridge extends BaseDataBridge {
        void onNext (List<PlanClassSearchEntity> entities);

        String GetkeyWord ();

        void getOnAddClassNext (String s);

        void onAddClassError (Throwable e);

        List<AddClassEntity> getEntity ();
    }

    /**
     * 巡课登记页面
     */
    interface PatrolRegisterBridge extends BaseDataBridge {

        void onNext (List<DockScoreEntity.DockScoreBean.OptionsBean> optionsBeen);

        void getEntity (DockScoreEntity dockScoreEntity);

        void setScoreTitleList (List<String> scoreTitle);

        void setDockScoreEntity (DockScoreEntity dockScoreEntity);

        void setSectionList (List<DockScoreEntity.SectionsListBean> sectionsList);

        void onSubmitCompleted ();

        void onSubmitError (Throwable e);

        void onSubmitNext (List<SubmitEntity> integer);

        String getUid ();

        String getDeptId ();

        int getClassId ();

        String getDate ();

        int getSctionId ();

        int getXid ();

        void onCheckLoadClassPatrolStateNext (String s, int res);

        void onCheckLoadClassPatrolStateError (Throwable throwable);

        void onError (Throwable e, String date);
    }

    /**
     * 选择班级页面
     */
    interface AddClassChooseBridge extends BaseDataBridge {

        void onNext (List<ClassChooseEntity.ClassListBean> classListBeen);

        void setEasySections (List<EasySection> mEasySections);

        void setIndexIntArray (SparseIntArray mIndexIntArray);

        //        String getUid ();

        //        int getType ();

        void getOnAddClassNext (String s);

        void onAddClassError (Throwable e);

        List<AddClassEntity> getEntity ();

    }

    /**
     * 课堂巡视列表(加分页)
     */
    interface GetPatrolClassBridge extends BaseDataBridge {
        void onNext (List<PatrolClassEntity> patrolClassEntities);

        String getUid ();

        int getType ();

        void onRefreshError (Throwable e);

        void onRefreshCompleted (List<PatrolClassEntity> patrolClassEntities);

        String getEndDate ();

        String getStartDate ();

        void onLoadMoreError (Throwable e);

        void onLoadMoreCompleted (List<PatrolClassEntity> patrolClassEntities);
    }

    /**
     * 课堂巡视详情列表
     */
    interface GetPatrolClassSelBridge extends BaseDataBridge {
        void onNext (List<PatrolClassSelEntity> patrolClassSelEntities);

        String getUid ();

        int getXid ();

        String getPid ();

        void onSubmitApprovalNext (String s);

        void onSubmitApprovalError (Throwable e);

        void onDeleteNext (String s);

        void onDeleteError (Throwable e);

        void onRefreshError (Throwable e);

        void onRefreshCompleted (List<PatrolClassSelEntity> patrolClassSelEntities);

        void onLoadMoreError (Throwable e);

        void onLoadMoreCompleted (List<PatrolClassSelEntity> patrolClassSelEntities);

    }

    /**
     * 查询每条巡课记录详情
     */
    interface GetSeePointsListsBridge extends BaseDataBridge {
        void onNext (SeePointsListsEntity seePointsListsEntities);

        String getUid ();

        String getPid ();
    }


    /**
     * 查询时间段巡课记录
     */
    interface GetSearchResultBridge extends BaseDataBridge {
        void onNext (List<PatrolClassEntity> entities);

        String getUid ();

        int getType ();

        String getStartDate ();

        String getEndDate ();

        void onRefreshError (Throwable e);

        void onRefreshCompleted (List<PatrolClassEntity> patrolClassEntities);

        void onLoadMoreError (Throwable e);

        void onLoadMoreCompleted (List<PatrolClassEntity> patrolClassEntities);
    }

    /**
     * 选择老师，获取所有老师列表
     */
    interface GetChooseTeacherBridge extends BaseDataBridge {
        void onNext (List<ChooseTeacherEntity> entities);

        String getDeptId ();
    }

    /**
     * 搜索老师
     */
    interface GetTeacherSearchBridge extends BaseDataBridge {
        void onNext (List<ChooseTeacherSearchEntity> entities);

        String GetkeyWord ();
    }
}
