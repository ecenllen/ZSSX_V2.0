package com.gta.zssx.patrolclass.model;

import com.gta.zssx.patrolclass.model.entity.PatrolRegisterBean;

import rx.Subscription;

/**
 * [Description]
 * <p>
 * [How to use]
 * <p>
 * [Tips] Created by tengfei.lv on 2016/7/28 9:40.
 */

public interface BaseModel {

    /**
     * 加载数据
     *
     * @param baseDataBridge 根据具体的业务传递对应的类
     * @return
     */
    Subscription loadData (BaseDataBridge baseDataBridge);

    interface PlanPatrolResultModel extends BaseModel {
        Subscription deletePatrolClassItem (BaseDataBridge baseDataBridge);
    }

    /**
     * 巡课登记model
     */
    interface PatrolRegisterModel {
        /**
         * 提交的时候 数据转换
         */
        Subscription loadData (BaseDataBridge baseDataBridge, boolean notNull,String date);

        void convertToSubmitData (PatrolRegisterBean lPatrolRegisterBean);

        Subscription checkLoadClassPatrolState (BaseDataBridge baseDataBridge, int res);
    }

    /**
     * 选择班级页面model
     */
    interface AddClassChooseModel extends BaseModel {
        /**
         * 上传添加的班级列表
         */
        Subscription upLoadPlanClassList (BaseDataBridge baseDataBridge);
    }


    /**
     * 课堂巡视列表(加分页)
     */
    interface GetPatrolClassModel extends BaseModel {
        Subscription onRefresh (BaseDataBridge baseDataBridge, String startDate, String endDate);

        Subscription onLoadMore (BaseDataBridge baseDataBridge, int page, String startDate, String endDate);
    }

    /**
     * 课堂巡视详情列表
     */
    interface GetPatrolClassSelModel extends BaseModel {
        Subscription submitApproval (BaseDataBridge baseDataBridge);

        Subscription deletePatrolItem (BaseDataBridge baseDataBridge);

        Subscription onRefresh (BaseDataBridge baseDataBridge);

        Subscription onLoadMore (BaseDataBridge baseDataBridge, int page);
    }

    /**
     * 查询每条巡课记录详情
     */
    interface GetSeePointsListsModel extends BaseModel {
    }

    /**
     * 查询时间段的记录
     */
    interface GetSearchResultModel extends BaseModel {
        Subscription onRefresh (BaseDataBridge baseDataBridge);

        Subscription onLoadMore (BaseDataBridge baseDataBridge, int page);
    }

    /**
     * 选择教师，获取教师组织架构数据
     */
    interface GetChooseTeacherModel extends BaseModel {
    }

    /**
     * 搜索教师
     */
    interface GetTeacherSearchModel extends BaseModel {

    }

    /**
     * 搜索班级
     */
    interface QueryClassModel extends BaseModel {

        Subscription upLoadPlanClassList (BaseDataBridge baseDataBridge);
    }
}
