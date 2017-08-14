package com.gta.zssx.pub;

import com.gta.zssx.fun.coursedaily.registercourse.model.bean.AddClassBean;
import com.gta.zssx.fun.coursedaily.registercourse.model.bean.AddCourseBeen;
import com.gta.zssx.fun.coursedaily.registercourse.model.bean.ApproveBean;
import com.gta.zssx.fun.coursedaily.registercourse.model.bean.ClassChooseBean;
import com.gta.zssx.fun.coursedaily.registercourse.model.bean.ClassChooseBean01;
import com.gta.zssx.fun.coursedaily.registercourse.model.bean.ClassDisplayBean;
import com.gta.zssx.fun.coursedaily.registercourse.model.bean.DefaultRegistInfoBean;
import com.gta.zssx.fun.coursedaily.registercourse.model.bean.DetailItemShowBean;
import com.gta.zssx.fun.coursedaily.registercourse.model.bean.LogChartBean;
import com.gta.zssx.fun.coursedaily.registercourse.model.bean.LogListBean;
import com.gta.zssx.fun.coursedaily.registercourse.model.bean.PostSignBean;
import com.gta.zssx.fun.coursedaily.registercourse.model.bean.SearchClassBean;
import com.gta.zssx.fun.coursedaily.registercourse.model.bean.SectionBean;
import com.gta.zssx.fun.coursedaily.registercourse.model.bean.SectionStudentListBean;
import com.gta.zssx.fun.coursedaily.registercourse.model.bean.StudentListBean;
import com.gta.zssx.fun.coursedaily.registercourse.model.bean.StudentListNewBean;
import com.gta.zssx.fun.coursedaily.registercourse.model.bean.SubmitSignInfoBean;
import com.gta.zssx.fun.coursedaily.registerrecord.model.DTO.DeleteRecordDto;
import com.gta.zssx.fun.coursedaily.registerrecord.model.DTO.DetailRegisteredRecordDto;
import com.gta.zssx.fun.coursedaily.registerrecord.model.DTO.MyClassRecordDto;
import com.gta.zssx.fun.coursedaily.registerrecord.model.DTO.RegisteredRecordDto;
import com.gta.zssx.fun.coursedaily.registerrecord.model.DTO.RegisteredRecordFromSignatureDto;
import com.gta.zssx.fun.coursedaily.registerrecord.model.DTO.ServerTimeDto;
import com.gta.zssx.fun.personalcenter.model.bean.UpdateBean;
import com.gta.zssx.fun.personalcenter.model.bean.UserBean;
import com.gta.zssx.pub.http.HttpResult;

import java.util.List;
import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PartMap;
import retrofit2.http.Query;
import rx.Observable;

/**
 * [Description]
 * <p>
 * [How to use]
 * <p>
 * [Tips]
 *
 * @author Created by Zhimin.Huang on 2016/6/16.
 * @since 1.0.0
 */
public interface InterfaceList {

    /**
     * 登陆接口
     *
     * @param loginName
     * @param password
     * @return
     */
    @GET(InterfaceName.LOGIN)
    Observable<HttpResult<UserBean>> userLogin(@Query("loginName") String loginName,
                                               @Query("passWord") String password);


    @POST("MobilePlatIntegrationLogin")
    Observable<HttpResult<String>> getTicket(@Query("username") String loginName,
                                                 @Query("password") String password,@Query("ticket") String ticket);

    /**
     * 获取班级列表
     *
     * @return
     */
    @GET(InterfaceName.GET_CLASS_LIST)
    Observable<HttpResult<ClassChooseBean>> loadClass();


    /**
     * 获取班级列表
     *
     * @return
     */
    @GET(InterfaceName.GET_CLASS_LIST)
    Observable<HttpResult<List<ClassChooseBean01>>> loadClass01();

    /**
     * 查询班级
     *
     * @param className
     * @return
     */
    @GET(InterfaceName.QUERY_CLASS)
    Observable<HttpResult<List<SearchClassBean>>> search(@Query("ClassName") String className);


    /**
     * 获得添加班级的列表
     *
     * @param teacherId
     * @return
     */
    @GET(InterfaceName.GET_CLASS_BY_TEACHERID)
    Observable<HttpResult<List<ClassDisplayBean>>> getClassList(@Query("TeacherID") String teacherId);


    /**
     * 获取班级登记情况
     *
     * @param teacherId
     * @param classId
     * @param dataString
     * @return
     */
    @GET(InterfaceName.GET_SIGN_STATUS)
    Observable<HttpResult<List<SectionBean>>> getClassStatus(@Query("TeacherID") String teacherId, @Query("ClassID") String classId, @Query("SignDate") String dataString);


    /**
     * 获取学生名单
     *
     * @param classId
     * @param signData
     * @return
     */
    @GET(InterfaceName.GET_STUDENT_LIST)
    Observable<HttpResult<List<StudentListNewBean>>> getStudentList(@Query("ClassID") int classId, @Query("SignDate") String signData);


    /**
     * 上传添加的班级列表
     *
     * @param addClassBeen
     * @return
     */
    @POST(InterfaceName.ADD_TEACHER_CLASS)
    Observable<HttpResult<String>> addClass(@Body List<AddClassBean> addClassBeen);


    /**
     * 获取领导审核日
     *
     * @return
     */
    @GET(InterfaceName.GET_APPROVE_DATE)
    Observable<HttpResult<ApproveBean>> getApproveDate();


    /**
     * 上传头像
     *
     * @param map 文件对象
     * @return
     */
    @Multipart
    @POST(InterfaceName.USER_IMG_MODIFY)
    Observable<HttpResult<UserBean>> uploadAvtor(@PartMap Map<String, RequestBody> map);


    /**
     * 上传登记信息
     *
     * @param postSignBean
     * @return
     */
    @POST(InterfaceName.POST_SIGN_INFO)
    Observable<HttpResult<String>> postSignInfo(@Body PostSignBean postSignBean);

    /**
     * 上传登记信息 - 新
     *
     * @param submitSignInfoBean
     * @return
     */
    @POST(InterfaceName.POST_SIGN_INFO)
    Observable<HttpResult<String>> postSignInfoNew(@Body List<SubmitSignInfoBean> submitSignInfoBean);

    /**
     * 获取服务器时间
     */
    @GET(InterfaceName.GET_CURRENT_DATE)
    Observable<HttpResult<ServerTimeDto>> getServerTime();

    /**
     * 获取时间段内的课时和内容
     *
     * @param TeacherID
     * @param BeginDate
     * @param EndDate
     * @return
     */
    @GET(InterfaceName.GET_SIGN_LIST)
    Observable<HttpResult<RegisteredRecordDto>> getRegisteredRecordList(@Query("TeacherID") String TeacherID, @Query("BeginDate") String BeginDate, @Query("EndDate") String EndDate, @Query("PageCount") int PageCount);

    /**
     * 获取某一天内的课时和内容
     *
     * @param TeacherID
     * @param SignDate
     * @param ClassID
     * @return
     */
    @GET(InterfaceName.GET_SINGLE_SIGN_LIST)
    Observable<HttpResult<List<RegisteredRecordFromSignatureDto>>> getRegisteredRecordFromSignatureList(@Query("TeacherID") String TeacherID, @Query("SignDate") String SignDate, @Query("ClassID") String ClassID, @Query("GetAllInfo") boolean GetAllInfo);

    /**
     * 获取课时和内容详情
     *
     * @param TeacherID
     * @param SignDate
     * @param ClassID
     * @return
     */
    @GET(InterfaceName.GET_SIGN_DETAIL_LIST)
    Observable<HttpResult<DetailRegisteredRecordDto>> getRegisteredRecordDetailList(@Query("TeacherID") String TeacherID, @Query("SignDate") String SignDate, @Query("ClassID") String ClassID, @Query("SectionID") int SectionID);

    /**
     * 11.登记信息删除
     *
     * @return String
     */
    @POST(InterfaceName.POST_DELETE_SIGN_INFO)
    Observable<HttpResult<String>> deleteSignInfo(@Body List<DeleteRecordDto> deleteRecordDto);
//    @POST(InterfaceName.POST_DELETE_SIGN_INFO)
//    Observable<HttpResult<String>> deleteSignInfo(@Body List<DeleteRecordDto> deleteRecordDto);

    /**
     * 获取我的班级记录
     *
     * @param TeacherID
     * @param SignDate
     * @return
     */
    @GET(InterfaceName.GET_MY_CLASS)
    Observable<HttpResult<List<MyClassRecordDto>>> getMyClassRecordList(@Query("TeacherID") String TeacherID, @Query("date") String SignDate, @Query("ClassID") int ClassID);


    /**
     * 检查更新
     *
     * @return
     */
    @GET(InterfaceName.GET_VERSION_INFORMATION)
    Observable<HttpResult<UpdateBean>> checkUpdate();

    /**
     * 查看老师当前状态或者带班状态
     */
    @GET(InterfaceName.GET_TEACHER_CLASS)
    Observable<HttpResult<UserBean>> getTeacherClassUpdate(@Query("teacherId") String TeacherID);

    /**
     * 获取用户信息
     */
    @GET(InterfaceName.GET_USER_INFO)
    Observable<HttpResult<UserBean>> getUserInfoUpdate(@Query("userId") String UserID);

    /**
     * 删除添加班级
     */
    @POST(InterfaceName.DELETE_TEACHER_CLASS)
    Observable<HttpResult<String>> deleteTeacherClass(@Query("ClassId") String ClassId, @Query("TeacherId") String TeacherId);

    /**
     * 获取教师所教授的课程
     */
    @GET(InterfaceName.GET_TEACHER_TAUGHT_COURSE)
    Observable<HttpResult<List<DetailItemShowBean.CourseInfoBean>>> getTeacherTaughtCourse(@Query("teacherDbid") String teacherDbid,@Query("workDate") String workDate);

    /**
     * 搜索课程
     */
    @GET(InterfaceName.SEARCH_ALL_COURSE_BY_KEY_WORD)
    Observable<HttpResult<List<DetailItemShowBean.CourseInfoBean>>> searchAllCourseByKeyWord(@Query("keyWords") String keyWords);

    /**
     * 添加自定义课程
     */
    @POST(InterfaceName.SUBMIT_USER_DEFINED_COURSE)
    Observable<HttpResult<DetailItemShowBean.CourseInfoBean>> submitUserDefinedCourse(@Body AddCourseBeen addCourseBeen);

    /**
     * 课表默认的授课老师和课程
     * @param signDate
     * @param classId
     * @param sectionId
     * @return
     */
    @GET(InterfaceName.GET_SECTION_DEFAULT_INFO)
    Observable<HttpResult<DefaultRegistInfoBean>> GetClassSectionDefaultTeachersAndCoures(@Query("signDate") String signDate,@Query("classID") int classId,@Query("sectionId") int sectionId,@Query("teacherId") String teacherId);

    /**
     *获取新的学生列表
     */
    @GET(InterfaceName.GET_STUDENT_LIST_NEW)
    Observable<HttpResult<List<SectionStudentListBean>>> GetClassDiarySectionStudentMarkedDetail(@Query("signDate") String signDate,@Query("classID") int classId,@Query("sectionId") String sectionId);

    /**
     * 查询要登记的节次是否有被登记过
     */
    @GET(InterfaceName.CHECK_SECTION_IS_REGISTERED)
    Observable<HttpResult<List<String>>> checkSctionIsHaveBeenRegister(@Query("signDate") String signDate,@Query("classID") int classId,@Query("sectionId") String sectionId);

    /**
     * 日志统计-图表
     */
    @GET(InterfaceName.GET_LOG_CHART)
    Observable<HttpResult<LogChartBean>> getLogChart(@Query("data") String date);

    /**
     * 日志统计-部门列表
     */
    @GET(InterfaceName.GET_LOG_LIST)
    Observable<HttpResult<List<LogListBean>>> getLogList(@Query("data") String date);

    /**
     * 日志统计-班级列表
     */
    @GET(InterfaceName.GET_LOG_CLASS_LIST)
    Observable<HttpResult<List<LogListBean>>> getLogClassList(@Query("data") String date, @Query("mId") String mId);


}
