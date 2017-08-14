package com.gta.zssx.pub;

import com.gta.zssx.fun.adjustCourse.model.bean.AdjustCourseBean;
import com.gta.zssx.fun.adjustCourse.model.bean.AdjustSearchClassBean;
import com.gta.zssx.fun.adjustCourse.model.bean.ApplyBean;
import com.gta.zssx.fun.adjustCourse.model.bean.ApplyConfirmBean;
import com.gta.zssx.fun.adjustCourse.model.bean.ApplyDetailBean;
import com.gta.zssx.fun.adjustCourse.model.bean.ApplyRecordBean;
import com.gta.zssx.fun.adjustCourse.model.bean.ApplySuccessBean;
import com.gta.zssx.fun.adjustCourse.model.bean.ConfirmApplyBean;
import com.gta.zssx.fun.adjustCourse.model.bean.CurrentSemesterBean;
import com.gta.zssx.fun.adjustCourse.model.bean.HasTimeScheduleBean;
import com.gta.zssx.fun.adjustCourse.model.bean.NoticeBean;
import com.gta.zssx.fun.adjustCourse.model.bean.ReplaceCourseBean;
import com.gta.zssx.fun.adjustCourse.model.bean.ScheduleBean;
import com.gta.zssx.fun.adjustCourse.model.bean.StuClassBean;
import com.gta.zssx.fun.adjustCourse.model.bean.TeacherBean;
import com.gta.zssx.fun.adjustCourse.model.bean.TeacherRecommendBean;
import com.gta.zssx.pub.http.HttpResult;

import java.util.List;

import javax.xml.parsers.SAXParser;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;


/**
 * [Description]
 * <p>
 * [How to use]
 * <p>
 * [Tips]
 *
 * @author Created by Zhimin.Huang on 2016/10/27.
 * @since 1.0.0
 */
public interface AdjustCourseInterfaceList {


    /**
     * 10.分页查询调代课申请记录
     *
     * @param page
     * @param id
     * @return
     */
    @POST("queryTransferApplyRecordData")
    Observable<HttpResult<ApplyRecordBean>> getApplyRecord(@Query("pageNo") int page, @Query("userId") String id);

    /**
     * 8.分页查询所有调代课申请
     *
     * @param page
     * @param userId
     * @return
     */
    @POST("queryTransferApplyData")
    Observable<HttpResult<ApplyBean>> getApplyList(@Query("pageNo") int page, @Query("userId") String userId, @Query("auditStatus") String status);

    /**
     * 9.分页查询调代课确认列表
     *
     * @param page
     * @param id
     * @return
     */
    @POST("queryTransferApplyAuditData")
    Observable<HttpResult<ApplyConfirmBean>> getApplyConfirm(@Query("pageNo") int page, @Query("userId") String id,
                                                             @Query("type") int type, @Query("auditStatus") String status);


    /**
     * 11.获取调代课申请详情
     *
     * @param applyId
     * @return
     */
    @POST("getTransferApplyDetail")
    Observable<HttpResult<ApplyDetailBean>> getApplyDetail(@Query("transferApplyId") int applyId, @Query("type") int type);

    /**
     * 13.获取调代课记录详情
     *
     * @param applyId
     * @return
     */
    @POST("getTransferRecordDetail")
    Observable<HttpResult<ApplyDetailBean>> getRecordDetail(@Query("transferRecordId") int applyId);

    /**
     * 12.获取调代课确认详情
     *
     * @param applyId
     * @return
     */
    @POST("getTransferAuditDetail")
    Observable<HttpResult<ApplyDetailBean>> getConfirmDetail(@Query("transferApplyId") int applyId);


    /**
     * 6.获取调代课同部门，同科组，同班级的教师
     *
     * @param date          申请课程的日期，日期格式：2016-10-19
     * @param teacherNumber 教师数量，多个教师上课时，教师数量>1
     * @param teacherId     申请教师id
     * @param classId       选中的行政班id
     * @param unit          选中的节次，多个节次用逗号分隔
     * @param type          调代课类型,调课：T,代课：D
     * @return
     */
    @POST("getSameClassAndDeptAndSubjectTeacher")
    Observable<HttpResult<TeacherRecommendBean>> getTeacherRecommend(@Query("day") String date,
                                                                     @Query("numberTeacher") int teacherNumber,
                                                                     @Query("applyTechBId") String teacherId,
                                                                     @Query("applyClassId") int classId,
                                                                     @Query("selectUnitApply") String unit,
                                                                     @Query("applyType") String type);

    /**
     * 2.删除调代课
     *
     * @param applyId
     * @return
     */
    @POST("deleteTransferApply")
    Observable<HttpResult<String>> deleteCourse(@Query("applyId") String applyId);


    /**
     * 4.获取班级列表接口
     *
     * @return
     */
    @GET("getStuClassList")
    Observable<HttpResult<List<StuClassBean>>> getAllClass();


    /**
     * 5.获取所有教师列表
     *
     * @return
     */
    @POST("getAllTeacher")
    Observable<HttpResult<List<TeacherBean>>> getAllTeacher(@Query("deptId") int deptId);

    /**
     * 5.获取所有教师列表
     *
     * @return
     */
    @POST("getAllTeacher")
    Observable<HttpResult<TeacherBean>> searchAllTeacher(@Query("nameOrCode") String keyWord);


    /**
     * @param day       申请课程的日期，日期格式：2016-10-19
     * @param name      教师code或者名称，为空时查询所有
     * @param teacherId 申请教师id
     * @param section   选中的节次，多个节次用逗号分隔
     * @return
     */
    @POST("getTeacherByNameOrCode")
    Observable<HttpResult<List<TeacherBean>>> getRecommendTeacher(@Query("day") String day, @Query("nameOrCode") String name,
                                                                  @Query("applyTechId") int teacherId, @Query("selectUnitApply") String section,
                                                                  @Query("applyType") String applyType, @Query("applyClassId") String classId,
                                                                  @Query("deptId") String deptId, @Query("numberTeacher") int numberTeacher);

    /**
     * @param teacherId
     * @param date
     * @param semesterId
     * @param flag
     * @return
     */
    @GET("getCourseSchedule")
    Observable<HttpResult<ScheduleBean>> getSchedule(@Query("teacherUUId") String teacherId,
                                                     @Query("date") String date,
                                                     @Query("semesterId") String semesterId,
                                                     @Query("flag") int flag,
                                                     @Query("classId") String classId);

    /**
     * 17.调代课确认
     *
     * @param confirmApplyBean
     * @return
     */
    @POST("auditTransfer")
    Observable<HttpResult<String>> confirmApply(@Body ConfirmApplyBean confirmApplyBean);

    /**
     * 17.调代课确认和审核
     *
     * @param
     * @return
     */
    @POST("auditTransfer")
    Observable<HttpResult<String>> confirmAndAudit(@Query("applyId") String applyId, @Query("userId") String userId, @Query("userName") String userName, @Query("auditOptions") String option);

    /**
     * 16.获取当前学期
     *
     * @return
     */
    @POST("getCurrentSemester")
    Observable<HttpResult<CurrentSemesterBean>> getSemester();

    /**
     * 新增代课
     *
     * @param replaceCourseBean
     * @return
     */
    @POST("addTransferOrReplaceCourse")
    Observable<HttpResult<ApplySuccessBean>> applyCourseReplace(@Body ReplaceCourseBean replaceCourseBean);

    /**
     * 新增调课
     *
     * @param replaceCourseBean
     * @return
     */
    @POST("addTransferOrReplaceCourse")
    Observable<HttpResult<ApplySuccessBean>> applyCourseAdjust(@Body AdjustCourseBean replaceCourseBean);

    /**
     * 7.获取班级列表接口
     *
     * @param keyWord
     * @return
     */
    @GET("queryClass")
    Observable<HttpResult<List<AdjustSearchClassBean>>> searchClass(@Query("className") String keyWord);


    /**
     * @param semesterId 学期id
     * @param teachId    教师id
     * @param adjustDate 调整后的日期
     * @param classId    班级id
     * @param roomParam  教室类型
     * @param roomId     教师id
     * @param courseType 开课方式
     * @return
     */
    @GET("getHasCourseTime")
    Observable<HttpResult<HasTimeScheduleBean>> getHasTimeSchedule(@Query("semester") String semesterId, @Query("teacherUUId") String teachId,
                                                                   @Query("adjustDate") String adjustDate, @Query("classIdsStr") String classId,
                                                                   @Query("roomParam") int roomParam, @Query("roomId") int roomId,
                                                                   @Query("openCourseType") int courseType);

    @POST("getWaitConfirmNumberAndWaitAuditNumber")
    Observable<HttpResult<NoticeBean>> getNotice(@Query("userId") String userId);
}
