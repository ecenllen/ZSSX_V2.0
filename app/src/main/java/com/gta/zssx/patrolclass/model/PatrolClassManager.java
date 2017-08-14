package com.gta.zssx.patrolclass.model;

import com.gta.zssx.patrolclass.model.entity.ChooseTeacherEntity;
import com.gta.zssx.patrolclass.model.entity.ChooseTeacherSearchEntity;
import com.gta.zssx.patrolclass.model.entity.PatrolClassEntity;
import com.gta.zssx.patrolclass.model.entity.PatrolClassSelEntity;
import com.gta.zssx.patrolclass.model.entity.PlanClassSearchEntity;
import com.gta.zssx.patrolclass.model.entity.SeePointsListsEntity;
import com.gta.zssx.patrolclass.view.base.PatrolClass;
import com.gta.zssx.pub.PatrolClassInterfaceList;
import com.gta.zssx.pub.http.HttpMethod;

import java.util.List;

import rx.Observable;

/**
 * Created by liang.lu1 on 2016/7/8.
 */
public class PatrolClassManager {
    /**
     * @return 课堂巡视列表(加分页)
     */
    public static Observable<List<PatrolClassEntity>> getPatrolClassData (String uId, int type, int page, int pageNum, String startDate, String endDate) {
        PatrolClassInterfaceList interfaceList = getPatrolClassInterfaceList ();
        return HttpMethod.getInstance ().call (interfaceList.getPatrolClassData (uId, type, page, pageNum, startDate, endDate));
    }

    private static PatrolClassInterfaceList getPatrolClassInterfaceList () {
        String lUrl = PatrolClass.getInstance ().getmServerAddress ();
        return HttpMethod
                .getInstance ()
                .retrofitClient (lUrl)
                .create (PatrolClassInterfaceList.class);
    }

    /**
     * 课堂巡视详情列表
     *
     * @param uId
     * @param xId
     * @param page
     * @param pageNum
     * @return
     */
    public static Observable<List<PatrolClassSelEntity>> getPatrolClassSelData (String uId, int xId, int page, int pageNum) {
        PatrolClassInterfaceList interfaceList = getPatrolClassInterfaceList ();
        return HttpMethod.getInstance ().call (interfaceList.getPatrolClassSelData (uId, xId, page, pageNum));
    }

    /**
     * 查询每条巡课记录明细
     *
     * @param uId
     * @param pId
     * @return
     */
    public static Observable<SeePointsListsEntity> getSeePointsListsData (String uId, int pId) {
        PatrolClassInterfaceList interfaceList = getPatrolClassInterfaceList ();
        return HttpMethod.getInstance ().call (interfaceList.getSeePointsListsData (uId, pId));
    }

    /**
     * 获取查询时间段巡课历史记录
     *
     * @param uId
     * @param type
     * @param page
     * @param pageNum
     * @param startDate
     * @param endDate
     * @return
     */
    public static Observable<List<PatrolClassEntity>> getSearchResultData (String uId, int type, int page, int pageNum, String startDate, String endDate) {
        PatrolClassInterfaceList interfaceList = getPatrolClassInterfaceList ();
        return HttpMethod.getInstance ().call (interfaceList.getPatrolClassData (uId, type, page, pageNum, startDate, endDate));
    }

    /**
     * 一键送审接口
     * @param
     * @return
     */
    public static Observable<String> submitApproval (String ids, String uId) {
        PatrolClassInterfaceList interfaceList = getPatrolClassInterfaceList ();
        return HttpMethod.getInstance ().call (interfaceList.submitApproval (ids, uId));
    }

    /**
     * 删除巡课item
     * @return
     */
    public static Observable<String> deletePatrolItem (String uId, int xId, int pId) {
        PatrolClassInterfaceList interfaceList = getPatrolClassInterfaceList ();
        return HttpMethod.getInstance ().call (interfaceList.deletePatrolItem (uId, xId, pId));
    }

    /**
     * 删除巡课班级
     *
     * @param uId
     * @param deptId
     * @param classId
     * @return
     */
    public static Observable<String> deletePatrolClassItem (String uId, int deptId, int classId) {
        PatrolClassInterfaceList interfaceList = getPatrolClassInterfaceList ();
        return HttpMethod.getInstance ().call (interfaceList.deletePatrolClassItem (uId, deptId, classId));
    }

    /**
     * 选择老师，获取组织架构所有老师
     *
     * @param deptId
     * @return
     */
    public static Observable<List<ChooseTeacherEntity>> getChooseTeacherDatas (String deptId) {
        PatrolClassInterfaceList interfaceList = getPatrolClassInterfaceList ();
        return HttpMethod.getInstance ().call (interfaceList.getChooseTeacherDatas (deptId));
    }

    /**
     * 搜索教师
     *
     * @param keyWord
     * @return
     */
    public static Observable<List<ChooseTeacherSearchEntity>> getTeacherSearchDatas (String keyWord) {
        PatrolClassInterfaceList interfaceList = getPatrolClassInterfaceList ();
        return HttpMethod.getInstance ().call (interfaceList.getTeacherSearchDatas (keyWord));
    }

    /**
     * 搜索班级
     *
     * @param keyWord
     * @return
     */
    public static Observable<List<PlanClassSearchEntity>> queryClass (String keyWord) {
        PatrolClassInterfaceList interfaceList = getPatrolClassInterfaceList ();
        return HttpMethod.getInstance ().call (interfaceList.queryClass (keyWord));
    }

    /**
     * 检测数据接口
     */
    public static Observable<String> checkLoadClassPatrolState (String uId, String date, int classId, int sectionId) {
        PatrolClassInterfaceList interfaceList = getPatrolClassInterfaceList ();
        return HttpMethod.getInstance ().call (interfaceList.checkLoadClassPatrolState (uId, date, classId, sectionId));
    }

}
