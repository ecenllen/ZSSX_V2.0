package com.gta.zssx.pub;

/**
 * Created by lan.zheng on 2016/10/11.
 */
public class MobileOAInterfaceName {
    /**
     * 此段废弃，暂时先留下
     */
    public static String hostIp = "";  //登陆时的IP

    public static String getDefaultBaseURL() {
        return "http://" + hostIp + "/api/MobileApi";
    }

    /**
     * 流程任务表单接口http://192.168.193.146/API/MobileAPI/GetFormField
     */
    public static final String GET_FORM_FIELD = "GetFormField";

    /**
     * 通讯录http://192.168.193.146/api/MobileApi/GetContactsList
     */
    public static final String GET_CONTACTS_LIST = "GetContactsList";

    /**
     * 新增/更新个人通讯录http://192.168.193.146/api/MobileApi/AddOrUpdateContacts
     */
    public static final String ADD_OR_UPDATE_CONTACTS = "AddOrUpdateContacts";

    /**
     * 删除个人通讯录http://192.168.193.146/api/MobileApi/DeleteContacts
     */
    public static final String DELETE_CONTACTS = "DeleteContacts";

    /**
     * 登录信息
     */
    public static final String LOGIN = "Login";

    /**
     * 首页，移动OA首页快速了解有多少相关事务需要处理
     */
    public static final String GET_PENDING_COUNT = "GetPendingCount";

    /**
     * 192.168.193.120/api/MobileApi/UserImgModify
     */
    public static final String USER_IMA_MODIFY = "UserImgModify";

    /**
     * 日程安排列表  1
     */
    public static final String GET_CALENDAR_LIST = "GetCalendarList";

    /**
     * 新增/修改日程,http://192.168.193.120/api/MobileApi/AddOrUpdateSchedule  2
     */
    public static final String ADD_OR_UPDATE_SCHEDULE = "AddOrUpdateSchedule";

    /**
     * 更新日程状态,http://192.168.193.120/api/MobileApi/ChangeScheduleStatus  3
     */
    public static final String CHANGE_SCHEDULE_STATUS = "ChangeScheduleStatus";

    /**
     * 删除日程,http://192.168.193.120/api/MobileApi/DeleteSchedule  4
     */
    public static final String DELETE_SCHEDULE = "DeleteSchedule";

    /**
     * 获取一天的所有日程http://192.168.193.120/api/MobileApi/GetCalendarDetail   5
     */
    public static final String GET_CALENDAR_DETAIL = "GetCalendarDetail";

    /**
     * 日程安排详情  6
     */
    public static final String GET_SCHEDULE_DETAIL = "GetScheduleDetail";

    /**--以下是待办已办的3个接口的地址--*/
    /**
     * 获取下个任务节点执行人 http://192.168.193.120/api/MobileApi/GetTaskHandleUsers
     */
    public static final String GET_TASK_HANDLE_USERS = "GetTaskHandleUsers";

    /**
     * 流程任务审批处理http://192.168.193.120/api/MobileApi/ProcessDoNext
     */
    public static final String PROCESS_DO_NEXT = "ProcessDoNext";

    /**
     * 审批历史,http://192.168.193.120/API/MobileAPI/GetProcessOpinion
     */
    public static final String GET_PROCESS_OPINION = "GetProcessOpinion";

    //"http://" + "10.1.41.21" + "/OA_Version.html?time="+new Date().getTime(),不用在这里获取版本更新
//    public static final String OA_VERSION = "OA_Version.html?time="+new Date().getTime();

    /**
     * 邮箱->邮件列表
     */
    public static final String GET_MAIL_LIST = "GetMailList";
    /**
     * 邮箱->删除邮件
     */
    public static final String DELETE_MAIL = "DeleteMail";

    /**
     * 邮箱->删除或恢复已删除邮件
     */
    public static final String DELETE_OR_REBACK_RECYCLED = "DeleteOrRebackRecycled";

    /**
     * 邮箱->快速回复邮件
     */
    public static final String QUICK_REPLY_MAIL = "QuickReplyMail";

    /**
     * 邮箱->邮件详情
     */
    public static final String GET_MAIL_DETAIL = "GetMailDetail";

    /**
     * 邮箱->发送邮件
     */
    public static final String SEND_MAIL = "SendMail";

    /**
     * 模块-》待办/已办
     * 代办->代办/已办列表
     */
    public static final String GET_TASK_LIST = "GetTaskList";

    /**
     * 会议纪要/公文公告 ->列表
     */
    public static final String GET_PRO_COPY_LIST = "GetProCopyList";

    /**
     * 会议通知->列表
     */
    public static final String GET_MEET_NOTICE_LIST = "GetMeetNoticeList";


    /**
     * 获取服务器时间
     */
    public static final String GET_CURRENT_DATE = "GetCurrentDate";

    /***************************************OA新接口*******************************************/

    /**
     * 模块-》会议
     * 会议列表
     */
    public static final String GET_MEETING_LIST = "GetMeetingList";

    /**
     * 模块-》发起申请
     * 申请表列表
     */
    public static final String GET_APPLY_FORM_LIST = "GetApplyFormList";

    /**
     * 模块-》我的申请
     * 申请表列表
     */
    public static final String GET_MY_APPLY_LIST = "GetMyApplyFormDetailList";

    /**
     * 日程--》获取学期
     */
    public static final String GET_SEMESTER_LIST = "GetSemesterList";

    /**
     * 日程--》获取当前学期的周
     */
    public static final String GET_SEMESTER_WEEK_LIST = "GetSemesterWeekList";

    /**
     * 日程--》获取当前周的周程表
     */
    public static final String GET_WEEK_PLAN_DETAIL = "GetWeekPlanDetail";


    /**
     * 模块-》事务提醒 12
     */
    public static final String GET_EVENT_NOTICE_LIST = "GetEventRemindList";

    /**
     * 模块-》公文  14
     */
    public static final String GET_NOTICE_LIST = "GetNoticeList";

    /**
     * 模块-》公告  15
     */
    public static final String GET_OFFICIAL_DOCUMENT_LIST = "GetDocList";

    /**
     * 模块-》值班
     * 用户的值班列表  16
     */
    public static final String GET_USER_DUTY_LIST = "GetUserDutyList";

    /**
     * 模块-》值班
     * 值班表的值班安排 17
     */
    public static final String GET_ALL_DUTY_PLAN = "GetAllDutyPlan";

    /**
     * 模块-》值班
     * 值班登记日期列表  18
     */
    public static final String GET_DUTY_REGISTER_DATE_LIST = "GetDutyRegisterDateList";

    /**
     * 模块-》值班
     * 值班提交登记--整天  19
     */
    public static final String SUBMIT_DUTY_REGISTER = "SUBMIT_DUTY_REGISTER";

    /**
     * 模块-》值班
     * 值班检查日期列表 20
     */
    public static final String GET_DUTY_CHECK_DATE_LIST = "GetDutyCheckDateList";

    /**
     * 模块-》值班
     * 值班某个日期的时间段表 21
     */
    public static final String GET_DUTY_DATE_TIME_LIST = "GetDutyDateTimeList";

    /**
     * 模块-》值班
     * 值班登记详情--传TimeId获取单条，不传获取一天 22
     */
    public static final String GET_DUTY_REGISTER_DETAIL = "GetOrdinaryDutyDetail";

    /**
     * 模块-》值班
     * 值班调班审批 26
     */
    public static final String DUTY_APPROVE_WORK_SWITCH = "ApproveWorkSwitch";

    /**
     * 公文公告-》修改公告为已读
     */
    public static final String SET_NOTICE_READ = "SetMyMessageReaded";

    /**
     * 会议-》修改会议为已读
     */
    public static final String SET_MEETING_READ = "SetMyMeetingReaded";



}
