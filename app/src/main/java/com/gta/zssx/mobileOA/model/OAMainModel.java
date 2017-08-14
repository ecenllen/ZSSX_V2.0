package com.gta.zssx.mobileOA.model;

import android.text.TextUtils;

import com.gta.zssx.mobileOA.model.bean.ApplyFormInfo;
import com.gta.zssx.mobileOA.model.bean.BacklogInfo;
import com.gta.zssx.mobileOA.model.bean.DeleteSchuduleInfo;
import com.gta.zssx.mobileOA.model.bean.DutyDateInfo;
import com.gta.zssx.mobileOA.model.bean.DutyTableInfo;
import com.gta.zssx.mobileOA.model.bean.DutyTimeListInfo;
import com.gta.zssx.mobileOA.model.bean.EventNoticeInfo;
import com.gta.zssx.mobileOA.model.bean.MeetingInfo;
import com.gta.zssx.mobileOA.model.bean.MyApplyInfo;
import com.gta.zssx.mobileOA.model.bean.OfficeNoticeInfo;
import com.gta.zssx.mobileOA.model.bean.RemindCountInfo;
import com.gta.zssx.mobileOA.model.bean.Schedule;
import com.gta.zssx.mobileOA.model.bean.SemessterAndWeeklyInfo;
import com.gta.zssx.mobileOA.model.bean.TermInfo;
import com.gta.zssx.mobileOA.model.bean.TermWeekInfo;
import com.gta.zssx.mobileOA.model.bean.UserDutyListInfo;
import com.gta.zssx.pub.http.HttpMethod;

import java.util.List;

import rx.Observable;

/**
 * Created by lan.zheng on 2016/10/11.
 */
public class OAMainModel extends BaseManager {
    private static final Class mInterfaceListClass = com.gta.zssx.pub.MobileOAInterfaceList.class;
//    private static final Class mInterfaceList = com.gta.zssx.pub.InterfaceList.class;
    /**
     * 例子
     *
     * @param
     * @return
     */
    public static Observable<String> getTestList() {
        return HttpMethod.getInstance().call(getInterfaceList(mInterfaceListClass)
                .getLoginUserData());
    }

    /**
     * 获取系统时间
     *
     * @param
     * @return
     */
   /* public static Observable<ServerTimeDto> getServerTime() {
        //去获取服务器时间
        return HttpMethod.getInstance().call(getInterfaceListLOG(mInterfaceList)
                .getServerTime());
    }*/

    /**
     * 学校公告
     * @param id
     * @param pageIndex
     * @param PageSize
     * @param searchStr
     * @return
     */
    public static Observable<OfficeNoticeInfo> getOfficialData(String id,int pageIndex,int PageSize,String searchStr) {
        return HttpMethod.getInstance().call(getInterfaceList(mInterfaceListClass)
                .getOfficialData(id,pageIndex,PageSize,searchStr));
    }

    /**
     * 学校公文
     * @param id
     * @param pageIndex
     * @param PageSize
     * @param searchStr
     * @return
     */
    public static Observable<OfficeNoticeInfo> getOfficialDocumentData(String id,int pageIndex,int PageSize,String searchStr) {
        return HttpMethod.getInstance().call(getInterfaceList(mInterfaceListClass)
                .getOfficialDocumentData(id,pageIndex,PageSize,searchStr));
    }

    /**
     * 获取申请表列表
     *
     * @param userId
     * @return
     */
    public static Observable<ApplyFormInfo> getApplyList(String userId) {
        return HttpMethod.getInstance().call(getInterfaceList(mInterfaceListClass).getApplyFormList(userId));
    }

    /**
     * 获取我的申请表列表
     *
     * @param userId
     * @return
     */
    public static Observable<MyApplyInfo> getMyApplyList(String userId, int pageIndex, int pageSize) {
        return HttpMethod.getInstance().call(getInterfaceList(mInterfaceListClass).getMyApplyList(userId, pageIndex, pageSize));
    }


    /**
     * 获取待办/已办事项
     */
    public static Observable<BacklogInfo> getTaskList(String username, int pageSize, int pageIndex, int type,
                                                      String endTime, String startTime,
                                                      String taskId, String runId, String subject) {
        return HttpMethod.getInstance().call(getInterfaceList(mInterfaceListClass).getTaskList(username, pageSize, pageIndex, type, subject));
    }

    /**
     * 我的会议列表
     *
     * @param UserId
     * @param PageIndex
     * @param PageSize
     * @param SearchStr
     * @return
     */
    public static Observable<MeetingInfo> getMeetingList(String UserId, int PageIndex, int PageSize, String SearchStr) {
        return HttpMethod.getInstance().call(getInterfaceList(mInterfaceListClass).getMeetingList(UserId, PageIndex, PageSize, SearchStr));
    }

    /**
     *  学期列表
     *  @return
     *  */
    public static Observable<List<TermInfo>> getSemesterList() {
        return HttpMethod.getInstance().call(getInterfaceList(mInterfaceListClass).getSemesterList());
    }

    /**
     *  学期周列表
     *  @return
     *  */
    public static Observable<List<TermWeekInfo>> getSemesterWeekList(int SemesterId) {
        return HttpMethod.getInstance().call(getInterfaceList(mInterfaceListClass).getSemesterWeekList(SemesterId));
    }

    /**
     *  学期周程表详情列表
     *  @return
     *  */
    public static Observable<List<SemessterAndWeeklyInfo>> getWeekPlanList(String UserId, String BeginDate, String EndDate) {
        return HttpMethod.getInstance().call(getInterfaceList(mInterfaceListClass).getWeekPlanList(UserId,BeginDate,EndDate));
    }

    /**
     * 事务提醒列表
     * @param UserId
     * @param PageIndex
     * @param PageSize
     * @return
     */
    public static Observable<EventNoticeInfo> getEventNoticeList(String UserId, int PageIndex, int PageSize){
        return HttpMethod.getInstance().call(getInterfaceList(mInterfaceListClass).getEventNoticeList(UserId, PageIndex, PageSize));
    }

    /**
     * 用户值班表列表  16
     */
    public static Observable<UserDutyListInfo> getUserDutyList(String UserId, int PageIndex, int PageSize){
        return HttpMethod.getInstance().call(getInterfaceList(mInterfaceListClass).getUserDutyList(UserId, PageIndex, PageSize));
    }

    /**
     * 用户值班表值班安排 17
     */
    public static Observable<DutyTableInfo> getDutyTable(int tableId){
        return HttpMethod.getInstance().call(getInterfaceList(mInterfaceListClass).getDutyTableById(tableId));
    }


    /**
     * 首页提醒
     */
    public static Observable<RemindCountInfo> getRemindCount(String userId, String userName){
        return HttpMethod.getInstance().call(getInterfaceList(mInterfaceListClass).getRemindCount(userId,userName));
    }

    /**
     *日程安排列表  1
     */
    public static Observable<List<Schedule>> getCalendarList(String userId, String beginDate,String endDate){
        return HttpMethod.getInstance().call(getInterfaceList(mInterfaceListClass).getCalendarList(userId,beginDate,endDate));
    }

    /**
     *新增/修改日程   2
     */
    public static Observable<Object> addOrUpdateSchedule(Schedule schedule){
        return HttpMethod.getInstance().call(getInterfaceList(mInterfaceListClass).addOrUpdateSchedule(schedule));
    }

    /**
     *更新日程状态  3
     */
    public static Observable<Object> changeScheduleStatus(int id, int status){
        DeleteSchuduleInfo deleteSchuduleInfo = new DeleteSchuduleInfo();
        deleteSchuduleInfo.setId(id);
        deleteSchuduleInfo.setStatus(status);
        return HttpMethod.getInstance().call(getInterfaceList(mInterfaceListClass).changeScheduleStatus(deleteSchuduleInfo));
    }

    /**
     *删除日程  4
     */
    public static Observable<Object> deleteSchedule(int id){
        DeleteSchuduleInfo deleteSchuduleInfo = new DeleteSchuduleInfo();
        deleteSchuduleInfo.setId(id);
        return HttpMethod.getInstance().call(getInterfaceList(mInterfaceListClass).deleteSchedule(deleteSchuduleInfo));
    }

    /**
     *获取一天的所有日程  5   --用于推送今天的日程消息提醒，暂时待定
     */
    public static Observable<List<Schedule>> getCalendarDetail(String userId,String selectTime){
        return HttpMethod.getInstance().call(getInterfaceList(mInterfaceListClass).getCalendarDetail(userId,selectTime));
    }

    /**
     *日程安排详情  6
     */
    public static Observable<Schedule> getScheduleDetail(int id){
        return HttpMethod.getInstance().call(getInterfaceList(mInterfaceListClass).getScheduleDetail(id));
    }


    /**
     * 值班登记日期列表  18
     */
    public static Observable<List<DutyDateInfo>> getDutyRegisterDate(String UserId, int DutyDetailId, int Status){
        return HttpMethod.getInstance().call(getInterfaceList(mInterfaceListClass).getDutyRegisterDate(UserId,DutyDetailId,Status));
    }

    /**
     * 值班登记提交--整天 19
     */
    public static Observable<String> submitDutyRegister(String UserId, int DutyDetailId, String Date){
        return HttpMethod.getInstance().call(getInterfaceList(mInterfaceListClass).submitDutyRegister(UserId,DutyDetailId,Date));
    }

    /**
     * 值班登记某天时间段获取 21
     */
    public static Observable<DutyTimeListInfo> getDutyDateTimeListData(String UserId, int DutyDetailId, String Date){
        if(Date == null || TextUtils.isEmpty(Date)){
            Date = null;
        }
        return HttpMethod.getInstance().call(getInterfaceList(mInterfaceListClass).getDutyDateTimeListData(UserId,DutyDetailId,Date));
    }

    /**
     * 设置公文公告为已读
     */
    public static Observable<String> setNoticeRead(String userId,int id){
        return  HttpMethod.getInstance().call(getInterfaceList(mInterfaceListClass).setNoticeRead(userId,id));
    }

    /**
     * 设置会议为已读
     */
    public static Observable<String> setMeetingRead(String userId,int id){
        return  HttpMethod.getInstance().call(getInterfaceList(mInterfaceListClass).setMeetingRead(userId,id));
    }

}
