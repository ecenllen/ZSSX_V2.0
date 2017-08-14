package com.gta.zssx.fun.adjustCourse.model;

import android.util.Log;

import com.gta.utils.resource.Toast;
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
import com.gta.zssx.fun.adjustCourse.view.base.AdjustCourse;
import com.gta.zssx.pub.AdjustCourseInterfaceList;
import com.gta.zssx.pub.http.HttpMethod;

import java.util.List;

import rx.Observable;

import static android.R.attr.type;
import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;

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
public class AdjustDataManager {

    public static Observable<ApplyRecordBean> getRecordBean(int page, String teacherId) {
        return HttpMethod.getInstance().call(getClient().getApplyRecord(page, teacherId));
    }

    public static Observable<ApplyBean> getApplyBean(int page, String teacherId, String status) {
        return HttpMethod.getInstance().call(getClient().getApplyList(page, teacherId, status));
    }

    public static Observable<ApplyConfirmBean> getConfirmBean(int page, String teacherId, int type, String status) {
        return HttpMethod.getInstance().call(getClient().getApplyConfirm(page, teacherId, type, status));
    }

    public static Observable<ApplyDetailBean> getApplyDetail(int applyId, int type) {
        return HttpMethod.getInstance().call(getClient().getApplyDetail(applyId, type));
    }

    public static Observable<ApplyDetailBean> getRecordDetail(int applyId) {
        return HttpMethod.getInstance().call(getClient().getRecordDetail(applyId));
    }

    public static Observable<ApplyDetailBean> getConfirmDetail(int applyId) {
        return HttpMethod.getInstance().call(getClient().getConfirmDetail(applyId));
    }

    public static Observable<ScheduleBean> getSchedule(String teacherId, String date, String semesterId, int flag, String classId) {
        return HttpMethod.getInstance().call(getClient().getSchedule(teacherId, date, semesterId, flag, classId));
    }

    public static Observable<List<TeacherBean>> getAllTeacher(int deptId) {
        return HttpMethod.getInstance().call(getClient().getAllTeacher(deptId));
    }

    public static Observable<TeacherBean> SearchAllTeacher(String keyWord) {
        return HttpMethod.getInstance().call(getClient().searchAllTeacher(keyWord));
    }

    public static Observable<List<StuClassBean>> getAllClass() {
        return HttpMethod.getInstance().call(getClient().getAllClass());
    }

    public static Observable<List<AdjustSearchClassBean>> searchClass(String keyWord) {
        return HttpMethod.getInstance().call(getClient().searchClass(keyWord));
    }

    public static Observable<String> deleteApply(String applyId) {
        return HttpMethod.getInstance().call(getClient().deleteCourse(applyId));
    }

    public static Observable<String> confirmApply(ConfirmApplyBean confirmApplyBean) {
        return HttpMethod.getInstance().call(getClient().confirmApply(confirmApplyBean));
    }

    public static Observable<String> confirmAndAudit(String applyId, String userId, String userName, String option) {
        return HttpMethod.getInstance().call(getClient().confirmAndAudit(applyId, userId, userName, option));
    }

    public static Observable<CurrentSemesterBean> getSemester() {
        return HttpMethod.getInstance().call(getClient().getSemester());
    }

    public static Observable<ApplySuccessBean> applyCourseReplace(ReplaceCourseBean courseBean) {
        return HttpMethod.getInstance().call(getClient().applyCourseReplace(courseBean));
    }

    public static Observable<ApplySuccessBean> applyCourseAdjust(AdjustCourseBean courseBean) {
        return HttpMethod.getInstance().call(getClient().applyCourseAdjust(courseBean));
    }

    public static Observable<NoticeBean> getNotice(String userId) {
        return HttpMethod.getInstance().call(getClient().getNotice(userId));
    }

    public static Observable<TeacherRecommendBean> getTeacherRecommend(String date, int teacherNumber, String teacherId,
                                                                       int classId, String section, String type) {
        return HttpMethod.getInstance().call(getClient().getTeacherRecommend(date, teacherNumber, teacherId,
                classId, section, type));
    }

    public static Observable<List<TeacherBean>> getSomeTeacher(String date, int applyTeacherId, String section,
                                                               String codeOrNum, String applyType, String classId,
                                                               String deptId, int numberTeacher) {
        return HttpMethod.getInstance().call(getClient().getRecommendTeacher(date, codeOrNum, applyTeacherId, section, applyType, classId, deptId, numberTeacher));
    }

    public static Observable<HasTimeScheduleBean> getHasTimeSchedule(String semesterId, String teachId, String adjustDate,
                                                                     String classId, int roomParam, int roomId,
                                                                     int courseType) {
        return HttpMethod.getInstance().call(getClient().getHasTimeSchedule(semesterId, teachId, adjustDate,
                classId, roomParam, roomId, courseType));
    }


    private static AdjustCourseInterfaceList getClient() {
        AdjustCourseInterfaceList lAdjustCourseInterfaceList = HttpMethod.getInstance().retrofitClient(AdjustCourse.getInstance().getmServerAddress())
                .create(AdjustCourseInterfaceList.class);
        return lAdjustCourseInterfaceList;
    }
}
