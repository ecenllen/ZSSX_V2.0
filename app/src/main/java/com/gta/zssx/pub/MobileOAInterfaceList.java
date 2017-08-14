package com.gta.zssx.pub;

import com.gta.zssx.fun.coursedaily.registerrecord.model.DTO.ServerTimeDto;
import com.gta.zssx.mobileOA.model.bean.ApplyFormInfo;
import com.gta.zssx.mobileOA.model.bean.BacklogInfo;
import com.gta.zssx.mobileOA.model.bean.ContactInfo;
import com.gta.zssx.mobileOA.model.bean.DeleteSchuduleInfo;
import com.gta.zssx.mobileOA.model.bean.DutyDateInfo;
import com.gta.zssx.mobileOA.model.bean.DutyTableInfo;
import com.gta.zssx.mobileOA.model.bean.DutyTimeListInfo;
import com.gta.zssx.mobileOA.model.bean.EventNoticeInfo;
import com.gta.zssx.mobileOA.model.bean.HomeInfo;
import com.gta.zssx.mobileOA.model.bean.MailInfor;
import com.gta.zssx.mobileOA.model.bean.MeetingInfo;
import com.gta.zssx.mobileOA.model.bean.MyApplyInfo;
import com.gta.zssx.mobileOA.model.bean.OfficeNoticeInfo;
import com.gta.zssx.mobileOA.model.bean.ProcessState;
import com.gta.zssx.mobileOA.model.bean.RemindCountInfo;
import com.gta.zssx.mobileOA.model.bean.Schedule;
import com.gta.zssx.mobileOA.model.bean.SemessterAndWeeklyInfo;
import com.gta.zssx.mobileOA.model.bean.TableInfor;
import com.gta.zssx.mobileOA.model.bean.TaskPeople;
import com.gta.zssx.mobileOA.model.bean.TermInfo;
import com.gta.zssx.mobileOA.model.bean.TermWeekInfo;
import com.gta.zssx.mobileOA.model.bean.UserDutyListInfo;
import com.gta.zssx.pub.http.HttpResult;

import java.util.List;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by lan.zheng on 2016/10/11.
 */
public interface MobileOAInterfaceList {

    /**
     * 登陆信息，个人中心信息不用这里的,例子
     */
    @GET(MobileOAInterfaceName.LOGIN)
    Observable<HttpResult<String>> getLoginUserData();

    /**
     * 获取服务器时间
     */
    @GET(MobileOAInterfaceName.GET_CURRENT_DATE)
    Observable<HttpResult<ServerTimeDto>> getServerTime();

    /**
     * 通讯录,List<ContactInfo>
     */
    @GET(MobileOAInterfaceName.GET_CONTACTS_LIST)
    Observable<HttpResult<List<ContactInfo>>> getContactsList(@Query("userId") String userId, @Query("conType") String conType);

    /**
     * 邮箱->邮件列表
     *
     * @param useId        用户id
     * @param type         邮件type
     * @param command      (1: 首次获取，2：获取更多,3: 获取最新)
     * @param limit        数量
     * @param mailId       非回收站时 邮件id
     * @param mailid       回收站 邮件id
     * @param searchString 搜索字符串
     * @return
     */
    @POST(MobileOAInterfaceName.GET_MAIL_LIST)
    Observable<HttpResult<List<MailInfor>>> getMailList(@Query("userId") String useId,
                                                        @Query("mailType") String type,
                                                        @Query("command") String command,
                                                        @Query("limit") String limit,
                                                        @Query("mailId") String mailId,
                                                        @Query("createTime") String mailid,
                                                        @Query("searchContent") String searchString);

    /**
     * 邮箱->删除邮件
     *
     * @param userId   用户id
     * @param mailIds  删除的邮件id  1,2,3
     * @param mailType 邮件类型
     * @return
     */
    @POST(MobileOAInterfaceName.DELETE_MAIL)
    Observable<HttpResult<String>> deleteMail(@Query("userId") String userId, @Query("mailIds") String mailIds,
                                              @Query("mailType") String mailType);

    /**
     * 邮箱->删除或恢复回收站邮件
     *
     * @param userId     用户id
     * @param command    // 1 是删除 2是恢复
     * @param sendIds    收件箱邮件ids
     * @param receiveIds 回收站邮件ids
     * @return
     */
    @POST(MobileOAInterfaceName.DELETE_OR_REBACK_RECYCLED)
    Observable<HttpResult<String>> deleteOrRebackRecycledMail(@Query("userId") String userId, @Query("command") String command,
                                                              @Query("sendIds") String sendIds,
                                                              @Query("receiveIds") String receiveIds);


    /**
     * 邮箱->快速回复
     *
     * @param useId         用户id
     * @param fullName      用户全名
     * @param mailId        邮件id
     * @param OutBoxContent 内容
     * @return
     */
    @POST(MobileOAInterfaceName.QUICK_REPLY_MAIL)
    Observable<HttpResult<String>> quickReplyMail(@Query("UserId") String useId,
                                                  @Query("UserName") String fullName,
                                                  @Query("Id") String mailId,
                                                  @Query("OutBoxContent") String OutBoxContent);

    /**
     * 邮箱->邮件详情
     *
     * @param userId   用户id
     * @param fullName 用户全名 （mailType ==1 时传入）
     * @param mailId   邮件id
     * @param mailType 邮件type
     * @return
     */
    @POST(MobileOAInterfaceName.GET_MAIL_DETAIL)
    Observable<HttpResult<String>> getMailDetail(@Query("userId") String userId,
                                                 @Query("userName") String fullName,
                                                 @Query("mailId") String mailId,
                                                 @Query("mailType") String mailType);


    /**
     * 邮箱->发送邮件
     *
     * @param userId
     * @param fullName
     * @param mailId
     * @param status
     * @param toIds
     * @param cc
     * @param bcc
     * @param receiverUsers
     * @param outBoxCopyer
     * @param outBoxSecret
     * @param OutBoxTheme
     * @param OutBoxContent
     * @param IsreturnReceipt
     * @param IsReplyOrTranspond
     * @param AccessoryIds
     * @return
     */
    @POST(MobileOAInterfaceName.SEND_MAIL)
    Observable<HttpResult<String>> sendMail(@Query("UserId") String userId,
                                            @Query("UserName") String fullName,
                                            @Query("Id") String mailId,
                                            @Query("Status") String status,
                                            @Query("TO") String toIds,
                                            @Query("CC") String cc,
                                            @Query("BCC") String bcc,
                                            @Query("ReceiverUsers") String receiverUsers,
                                            @Query("OutBoxCopyer") String outBoxCopyer,
                                            @Query("OutBoxSecret") String outBoxSecret,
                                            @Query("OutBoxTheme") String OutBoxTheme,
                                            @Query("OutBoxContent") String OutBoxContent,
                                            @Query("IsreturnReceipt") String IsreturnReceipt,
                                            @Query("IsReplyOrTranspond") String IsReplyOrTranspond,
                                            @Query("AccessoryIds") String AccessoryIds);

    /**
     * 代办/已办->代办、已办列表
     *
     * @param userName
     * @param limit
     * @param type      1:待办,2:已办,
     * @param endTime
     * @param TaskId    待办id
     * @param RunId     已办id
     * @param startTime
     * @param subject
     * @return
     */
    @POST(MobileOAInterfaceName.GET_TASK_LIST)
    Observable<HttpResult<BacklogInfo>> getTaskList(@Query("userName") String userName,
                                                    @Query("pageSize") int limit,
                                                    @Query("pageIndex") int pageIndex,
                                                    @Query("type") int type,
                                                    @Query("endTime") String endTime,
                                                    @Query("TaskId") String TaskId,
                                                    @Query("RunId") String RunId,
                                                    @Query("startTime") String startTime,
                                                    @Query("subject") String subject);

    @POST(MobileOAInterfaceName.GET_TASK_LIST)
    Observable<HttpResult<BacklogInfo>> getTaskList(@Query("UserName") String userName,
                                                    @Query("PageSize") int limit,
                                                    @Query("PageIndex") int pageIndex,
                                                    @Query("Type") int type,
                                                    @Query("Subject") String subject);

    /**
     * 会议、公文公告->列表
     *
     * @param userName
     * @param limit
     * @param type      3:公文公告 4会议通知 5会议纪要
     * @param endTime
     * @param RunId
     * @param startTime
     * @param subject
     * @return
     */
    @POST(MobileOAInterfaceName.GET_PRO_COPY_LIST)
    Observable<HttpResult<String>> getMeetingOrNoticeList(@Query("userName") String userName,
                                                          @Query("limit") String limit,
                                                          @Query("type") String type,
                                                          @Query("endTime") String endTime,
                                                          @Query("RunId") String RunId,
                                                          @Query("startTime") String startTime,
                                                          @Query("subject") String subject);

    /**
     * 会议->会议通知列表
     *
     * @param userName
     * @param subject
     * @param limit
     * @param noticeId
     * @param IsMore
     * @return
     */
    @POST(MobileOAInterfaceName.GET_MEET_NOTICE_LIST)
    Observable<HttpResult<String>> getMeetingOrNoticeList(@Query("userName") String userName,
                                                          @Query("subject") String subject,
                                                          @Query("limit") String limit,
                                                          @Query("noticeId") String noticeId,
                                                          @Query("IsMore") String IsMore);

    /**
     * 流程任务表单,List<TableInfor>
     */
    @POST(MobileOAInterfaceName.GET_FORM_FIELD)
    Observable<HttpResult<List<TableInfor>>> getFormField(@Query("UserName") String UserName,
                                                          @Query("TaskId") String TaskId,
                                                          @Query("runId") String runId,
                                                          @Query("noticeId") String noticeId,
                                                          @Query("copyId") String copyId);
//    Observable<HttpResult<List<TableInfor>>> getFormField(@Body TaskNewInfor taskNewInfor);

    /**
     * 新增/更新个人通讯录http://192.168.193.146/api/MobileApi/AddOrUpdateContacts,ContactInfo
     */
    @POST(MobileOAInterfaceName.ADD_OR_UPDATE_CONTACTS)
    Observable<HttpResult<ContactInfo>> addOrUpdateContacts(@Query("Id") String Id,
                                                            @Query("Name") String Name,
                                                            @Query("Gender") String Gender,
                                                            @Query("MobilePhone") String MobilePhone,
                                                            @Query("Email") String Email,
                                                            @Query("UnitName") String UnitName,
                                                            @Query("Duty") String Duty,
                                                            @Query("WorkPhone") String WorkPhone,
                                                            @Query("WorkAddress") String WorkAddress,
                                                            @Query("HomeAddress") String HomeAddress,
                                                            @Query("HomePhone") String HomePhone,
                                                            @Query("CreateBy") String CreateBy);
//    Observable<HttpResult<ContactInfo>> addOrUpdateContacts(@Body ContactInfo contactInfo);

    /**
     * 删除个人通讯录http://192.168.193.146/api/MobileApi/DeleteContacts,Successed
     */
    @POST(MobileOAInterfaceName.DELETE_CONTACTS)
    Observable<HttpResult<String>> deleteContacts(@Query("Id") String Id);
//    Observable<HttpResult<String>> deleteContacts(@Body ContactInfo contactInfo);

    /**
     * 首页，移动OA首页快速了解有多少相关事务需要处理,HomeInfo
     */
    @POST(MobileOAInterfaceName.GET_PENDING_COUNT)
    Observable<HttpResult<HomeInfo>> getPendingCount(@Query("userId") String userId, @Query("userName") String userName);

    /**
     * 日程安排列表,List<Schedule>  1
     */
    @POST(MobileOAInterfaceName.GET_CALENDAR_LIST)
    Observable<HttpResult<List<Schedule>>> getCalendarList(@Query("userId") String userId, @Query("beginTime") String beginTime, @Query("endTime") String endTime);

    /**
     * 新增/修改日程  2
     */
    @POST(MobileOAInterfaceName.ADD_OR_UPDATE_SCHEDULE)
    Observable<HttpResult<Object>> addOrUpdateSchedule(@Body Schedule schedule);

    /**
     * 更新日程状态  3
     */
    @POST(MobileOAInterfaceName.CHANGE_SCHEDULE_STATUS)
    Observable<HttpResult<Object>> changeScheduleStatus(@Body DeleteSchuduleInfo deleteSchuduleInfo);

    /**
     * 删除日程 4
     */
    @POST(MobileOAInterfaceName.DELETE_SCHEDULE)
    Observable<HttpResult<Object>> deleteSchedule(@Body DeleteSchuduleInfo deleteSchuduleInfo);

    /**
     * 获取一天的所有日程 5   --用于推送今天的日程消息提醒，暂时待定
     */
    @POST(MobileOAInterfaceName.GET_CALENDAR_DETAIL)
    Observable<HttpResult<List<Schedule>>> getCalendarDetail(@Query("userId") String userId, @Query("selectTime") String selectTime);

    /**
     * 日程安排详情  6
     */
    @GET(MobileOAInterfaceName.GET_SCHEDULE_DETAIL)
    Observable<HttpResult<Schedule>> getScheduleDetail(@Query("id") int id);

    /**
     * 获取下个任务节点执行人
     */
    @POST(MobileOAInterfaceName.GET_TASK_HANDLE_USERS)
    Observable<HttpResult<List<TaskPeople>>> getTaskHandleUsers(@Query("UserName") String UserName, @Query("TaskId") String TaskId);

    /**
     * 流程任务审批处理
     */
    @POST(MobileOAInterfaceName.PROCESS_DO_NEXT)
    Observable<HttpResult<String>> processDoNext(@Query("TaskId") String TaskId,
                                                 @Query("UserName") String UserName,
                                                 @Query("voteAgree") String voteAgree,
                                                 @Query("voteContent") String voteContent,
                                                 @Query("nextUser") String nextUser,
                                                 @Query("nextNodeId") String nextNodeId,
                                                 @Query("isBack") String isBack);

    /**
     * 审批历史
     */
    @POST(MobileOAInterfaceName.GET_PROCESS_OPINION)
    Observable<HttpResult<List<ProcessState>>> getProcessOpinion(@Query("ActInstId") String ActInstId, @Query("RunId") String RunId);


    /**
     * 获取学校公告
     */
    @POST(MobileOAInterfaceName.GET_NOTICE_LIST)
    Observable<HttpResult<OfficeNoticeInfo>> getOfficialData(@Query("UserId") String UserId, @Query("PageIndex") int PageIndex, @Query("PageSize") int PageSize, @Query("SearchStr") String SearchStr);

    /**
     * 获取学校公文
     */
    @POST(MobileOAInterfaceName.GET_OFFICIAL_DOCUMENT_LIST)
    Observable<HttpResult<OfficeNoticeInfo>> getOfficialDocumentData(@Query("UserId") String UserId, @Query("PageIndex") int PageIndex, @Query("PageSize") int PageSize, @Query("SearchStr") String SearchStr);

    /**
     * 获取我的申请表列表
     */
    @POST(MobileOAInterfaceName.GET_APPLY_FORM_LIST)
    Observable<HttpResult<ApplyFormInfo>> getApplyFormList(@Query("UserId") String UserId);

    /**
     * 获取我的申请事项列表
     */
    @POST(MobileOAInterfaceName.GET_MY_APPLY_LIST)
    Observable<HttpResult<MyApplyInfo>> getMyApplyList(@Query("UserId") String UserId, @Query("PageIndex") int pageIndex, @Query("PageSize")int PageSize);

    /**
     * 获取会议列表
     */
    @POST(MobileOAInterfaceName.GET_MEETING_LIST)
    Observable<HttpResult<MeetingInfo>> getMeetingList(@Query("UserId") String UserId, @Query("PageIndex") int PageIndex, @Query("PageSize") int PageSize, @Query("SearchStr") String SearchStr);

    /**
     * 获取学期列表
     */
    @GET(MobileOAInterfaceName.GET_SEMESTER_LIST)
    Observable<HttpResult<List<TermInfo>>> getSemesterList();

    /**
     *获取学期周列表
     */
    @GET(MobileOAInterfaceName.GET_SEMESTER_WEEK_LIST)
    Observable<HttpResult<List<TermWeekInfo>>> getSemesterWeekList(@Query("SemesterId") int SemesterId);

    /**
     * 获取周对应的周程表
     */
    @GET(MobileOAInterfaceName.GET_WEEK_PLAN_DETAIL)
    Observable<HttpResult<List<SemessterAndWeeklyInfo>>> getWeekPlanList(@Query("UserId") String UserId, @Query("BeginDate") String BeginDate, @Query("EndDate") String EndDate);

    /**
     * 获取事务提醒列表
     */
    @POST(MobileOAInterfaceName.GET_EVENT_NOTICE_LIST)
    Observable<HttpResult<EventNoticeInfo>> getEventNoticeList(@Query("UserId") String UserId, @Query("PageIndex") int PageIndex, @Query("PageSize") int PageSize);

    /**
     * 获取用户值班列表  16
     */
    @POST(MobileOAInterfaceName.GET_USER_DUTY_LIST)
    Observable<HttpResult<UserDutyListInfo>> getUserDutyList(@Query("UserId") String UserId, @Query("PageIndex") int PageIndex, @Query("PageSize") int PageSize);

    /**
     * 获取值班表  17
     */
    @POST(MobileOAInterfaceName.GET_ALL_DUTY_PLAN)
    Observable<HttpResult<DutyTableInfo>> getDutyTableById(@Query("DutyId") int DutyId);

    /**
     * 获取首页提醒
     */
    @POST(MobileOAInterfaceName.GET_PENDING_COUNT)
    Observable<HttpResult<RemindCountInfo>> getRemindCount(@Query("userId") String userId,  @Query("userName") String userName);


    /**
     *获取值班登记日期列表  18
     */
    @POST(MobileOAInterfaceName.GET_DUTY_REGISTER_DATE_LIST)
    Observable<HttpResult<List<DutyDateInfo>>> getDutyRegisterDate(@Query("UserId") String UserId, @Query("DutyDetailId") int DutyDetailId, @Query("Status") int Status);

    /**
     * 提交值班登记--整天 19
     */
    @POST(MobileOAInterfaceName.SUBMIT_DUTY_REGISTER)
    Observable<HttpResult<String>> submitDutyRegister(@Query("UserId") String UserId,@Query("DutyDetailId") int DutyDetailId,@Query("Date") String Date);

    /**
     *获取值班检查日期列表  20
     */
    @POST(MobileOAInterfaceName.GET_DUTY_CHECK_DATE_LIST)
    Observable<HttpResult<List<DutyDateInfo>>> getDutyCheckDate(@Query("UserId") String UserId , @Query("DutyDetailId") int DutyDetailId, @Query("Status") int Status);

    /**
     *获取值班某天时间段--传日期 21
     */
    @POST(MobileOAInterfaceName.GET_DUTY_DATE_TIME_LIST)
    Observable<HttpResult<DutyTimeListInfo>> getDutyDateTimeListData(@Query("UserId") String UserId , @Query("DutyDetailId") int DutyDetailId, @Query("Date") String Date);

    /**
     *设置公文公告已读
     */
    @POST(MobileOAInterfaceName.SET_NOTICE_READ)
    Observable<HttpResult<String>> setNoticeRead(@Query("UserId") String UserId,@Query("Id") int id);

    /**
     * 设置会议已读
     */
    @POST(MobileOAInterfaceName.SET_MEETING_READ)
    Observable<HttpResult<String>> setMeetingRead(@Query("UserId") String UserId,@Query("Id") int id);
}
