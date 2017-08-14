package com.gta.zssx.patrolclass.model;

import com.gta.zssx.patrolclass.model.entity.AddClassEntity;
import com.gta.zssx.patrolclass.model.entity.ClassChooseEntity;
import com.gta.zssx.patrolclass.model.entity.DockScoreEntity;
import com.gta.zssx.patrolclass.model.entity.PatrolRegisterBeanNew;
import com.gta.zssx.patrolclass.model.entity.PlanPatrolResultEntity;
import com.gta.zssx.patrolclass.model.entity.RegisterDataSubmitEntity;
import com.gta.zssx.patrolclass.model.entity.SubmitEntity;
import com.gta.zssx.patrolclass.model.entity.SubmitRegisterPatrol;
import com.gta.zssx.patrolclass.view.base.PatrolClass;
import com.gta.zssx.pub.PatrolClassInterfaceList;
import com.gta.zssx.pub.http.HttpMethod;

import java.util.List;

import rx.Observable;

/**
 * [Description]
 * <p>
 * [How to use]
 * <p>
 * [Tips] Created by tengfei.lv on 2016/7/14 17:27.
 */

public class PlanPatrolResultManager {


    /**
     * 请求班级选择列表
     *
     * @param
     * @return
     */
    public static Observable<List<ClassChooseEntity>> loadChooseClass () {
        PatrolClassInterfaceList lInterfaceList = getPatrolClassInterfaceList ();
        return HttpMethod.getInstance ().call (lInterfaceList.loadChooseClass ());
    }

    private static PatrolClassInterfaceList getPatrolClassInterfaceList () {
        return HttpMethod
                .getInstance ()
                .retrofitClient (PatrolClass.getInstance ().getmServerAddress ())
                .create (PatrolClassInterfaceList.class);
    }

    /**
     * 上传已选择班级列表
     */
    public static Observable<String> upLoadPlanClassList (List<AddClassEntity> entities) {
        PatrolClassInterfaceList list = getPatrolClassInterfaceList ();
        return HttpMethod.getInstance ().call (list.upLoadPlanClassList (entities));
    }

    /**
     * 请求选择班级之后的结果列表
     *
     * @return
     */
    public static Observable<List<PlanPatrolResultEntity>> getPlanPatrolClassDatas (String uId) {
        PatrolClassInterfaceList interfaceList = getPatrolClassInterfaceList ();
        return HttpMethod.getInstance ().call (interfaceList.loadPatrolChooseClass (uId));
    }

    /**
     * 获取巡课登记数据
     *
     * @return
     */
    public static Observable<DockScoreEntity> getRegisterPatrolDatas (String uId, String deptId, int classId, String date, int sectionId) {
        PatrolClassInterfaceList interfaceList = getPatrolClassInterfaceList ();
        return HttpMethod.getInstance ().call (interfaceList.loadRegisterPatrolDatas (uId, deptId, classId, date, sectionId));
    }

    /**
     * @param uId
     * @param deptId
     * @param classId
     * @param date
     * @param sectionId
     * @return 获取巡课登记数据（新）
     */
    public static Observable<PatrolRegisterBeanNew> getPatrolRegisterData (String uId, String deptId, int classId, String date, int sectionId) {
        PatrolClassInterfaceList interfaceList = getPatrolClassInterfaceList ();
        return HttpMethod.getInstance ().call (interfaceList.getPatrolRegisterData (uId, deptId, classId, date, sectionId));
    }

    /**
     * 上传巡课登记数据
     *
     * @return 巡课id
     */
    public static Observable<List<SubmitEntity>> uploadRegisterPatrol (SubmitRegisterPatrol submitRegisterPatrol) {
        PatrolClassInterfaceList interfaceList = getPatrolClassInterfaceList ();
        return HttpMethod.getInstance ().call (interfaceList.uploadRegisterPatrol (submitRegisterPatrol));
    }

    /**
     * 上传巡课登记数据(新)
     *
     * @param registerDataSubmitEntity
     * @return
     */
    public static Observable<List<SubmitEntity>> uploadRegisterData (RegisterDataSubmitEntity registerDataSubmitEntity) {
        PatrolClassInterfaceList interfaceList = getPatrolClassInterfaceList ();
        return HttpMethod.getInstance ().call (interfaceList.uploadRegisterData (registerDataSubmitEntity));
    }


}
