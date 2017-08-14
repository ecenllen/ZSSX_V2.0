package com.gta.zssx.pub;

import com.gta.zssx.patrolclass.model.entity.AddClassEntity;
import com.gta.zssx.patrolclass.model.entity.ChooseTeacherEntity;
import com.gta.zssx.patrolclass.model.entity.ChooseTeacherSearchEntity;
import com.gta.zssx.patrolclass.model.entity.ClassChooseEntity;
import com.gta.zssx.patrolclass.model.entity.DockScoreEntity;
import com.gta.zssx.patrolclass.model.entity.PatrolClassEntity;
import com.gta.zssx.patrolclass.model.entity.PatrolClassSelEntity;
import com.gta.zssx.patrolclass.model.entity.PatrolRegisterBeanNew;
import com.gta.zssx.patrolclass.model.entity.PlanClassSearchEntity;
import com.gta.zssx.patrolclass.model.entity.PlanPatrolResultEntity;
import com.gta.zssx.patrolclass.model.entity.RegisterDataSubmitEntity;
import com.gta.zssx.patrolclass.model.entity.SeePointsListsEntity;
import com.gta.zssx.patrolclass.model.entity.SubmitEntity;
import com.gta.zssx.patrolclass.model.entity.SubmitRegisterPatrol;
import com.gta.zssx.pub.http.HttpResult;

import java.util.List;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by liang.lu on 2016/8/3 13:11.
 * 巡课数据接口
 */
public interface PatrolClassInterfaceList {

    /**
     * 课堂巡视列表(加分页),查询时间段巡课登记记录
     *
     * @param uId
     * @param type
     * @param page
     * @param pageNum
     * @return
     */
    @GET (PatrolClassInterfaceName.PATROL_CLASS_DATA)
    Observable<HttpResult<List<PatrolClassEntity>>> getPatrolClassData (@Query ("uId") String uId, @Query ("type") int type, @Query ("page") int page, @Query ("pageNum") int pageNum, @Query ("startDate") String startDate, @Query ("endDate") String endDate);

    /**
     * 课堂巡视详情列表
     *
     * @param uId
     * @param xId
     * @param page
     * @param pageNum
     * @return
     */
    @GET (PatrolClassInterfaceName.PATROL_CLASS_SEL_DATA)
    Observable<HttpResult<List<PatrolClassSelEntity>>> getPatrolClassSelData (@Query ("uId") String uId, @Query ("xId") int xId, @Query ("page") int page, @Query ("pageNum") int pageNum);

    /**
     * 查询每条巡课记录详情
     *
     * @param uId
     * @param pId
     * @return
     */
    @GET (PatrolClassInterfaceName.SEE_POINTS_LISTS_DATA)
    Observable<HttpResult<SeePointsListsEntity>> getSeePointsListsData (@Query ("uId") String uId, @Query ("pId") int pId);

    /**
     * 一键送审
     *
     * @return
     */
    @POST (PatrolClassInterfaceName.SUBMIT_APPROVAL)
    Observable<HttpResult<String>> submitApproval (@Query ("ids") String ids, @Query ("uId") String uId);

    /**
     * 删除巡课详情item
     */

    @POST (PatrolClassInterfaceName.DELETE_PATROL_ITEM)
    Observable<HttpResult<String>> deletePatrolItem (@Query ("uId") String uId, @Query ("xId") int xId, @Query ("pId") int pId);

    /**
     * 删除巡课班级
     */
    @POST (PatrolClassInterfaceName.DELETE_PATROL_CLASS_ITEM)
    Observable<HttpResult<String>> deletePatrolClassItem (@Query ("uId") String uId, @Query ("deptId") int deptId, @Query ("classId") int classId);

    /**
     * 获取班级列表（巡课使用）
     *
     * @return
     */
    @GET (PatrolClassInterfaceName.GET_CLASS_LIST)
    Observable<HttpResult<List<ClassChooseEntity>>> loadChooseClass ();

    /**
     * 上传添加的班级
     *
     * @param entities
     * @return
     */
    @POST (PatrolClassInterfaceName.UP_LOAD_PLAN_CLASS_LIST)
    Observable<HttpResult<String>> upLoadPlanClassList (@Body List<AddClassEntity> entities);


    /**
     * 获取选择之后的班级列表
     *
     * @return
     */
    @GET (PatrolClassInterfaceName.LOAD_PATROL_CHOOSE_CLASS)
    Observable<HttpResult<List<PlanPatrolResultEntity>>> loadPatrolChooseClass (@Query ("uId") String uId);

    /**
     * 获取巡课登记数据
     *
     * @return
     */
    @GET (PatrolClassInterfaceName.LOAD_REGISTER_PATROL_DATAS)
    Observable<HttpResult<DockScoreEntity>> loadRegisterPatrolDatas (@Query ("uId") String uId, @Query ("deptId") String deptId, @Query ("classId") int classId, @Query ("date") String date, @Query ("sectionId") int sectionId);

    /**
     * @param uId
     * @param deptId
     * @param classId
     * @param date
     * @param sectionId
     * @return 获取巡课登记数据（新）
     */
    @GET (PatrolClassInterfaceName.GET_PATROL_REGISTER_DATA)
    Observable<HttpResult<PatrolRegisterBeanNew>> getPatrolRegisterData (@Query ("uId") String uId, @Query ("deptId") String deptId, @Query ("classId") int classId, @Query ("date") String date, @Query ("sectionId") int sectionId);

    /**
     * 上传巡课登记数据
     *
     * @return
     */
    @POST (PatrolClassInterfaceName.UP_LOAD_REGISTER_PATROL)
    Observable<HttpResult<List<SubmitEntity>>> uploadRegisterPatrol (@Body SubmitRegisterPatrol submitRegisterPatrol);

    /**
     * 上传巡课登记数据(新)
     *
     * @param registerDataSubmitEntity
     * @return
     */
    @POST (PatrolClassInterfaceName.UP_LOAD_REGISTER_DATA)
    Observable<HttpResult<List<SubmitEntity>>> uploadRegisterData (@Body RegisterDataSubmitEntity registerDataSubmitEntity);

    /**
     * 选择教师
     */
    @GET (PatrolClassInterfaceName.GET_CHOOSE_TEACHER_DATAS)
    Observable<HttpResult<List<ChooseTeacherEntity>>> getChooseTeacherDatas (@Query ("deptId") String deptId);

    /**
     * 搜索教师
     *
     * @param keyWord
     * @return
     */
    @GET (PatrolClassInterfaceName.GET_TEACHER_SEARCH_DATAS)
    Observable<HttpResult<List<ChooseTeacherSearchEntity>>> getTeacherSearchDatas (@Query ("keyword") String keyWord);

    /**
     * 搜索班级
     */
    @GET (PatrolClassInterfaceName.QUERY_CLASS)
    Observable<HttpResult<List<PlanClassSearchEntity>>> queryClass (@Query ("keyword") String keyWord);

    /**
     * 检测数据接口
     */
    @GET (PatrolClassInterfaceName.CHECK_LOAD_CLASS_PATROL_STATE)
    Observable<HttpResult<String>> checkLoadClassPatrolState (@Query ("uId") String uId, @Query ("date") String date, @Query ("classId") int classId, @Query ("sectionId") int sectionId);
}
