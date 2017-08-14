package com.gta.zssx.pub;

import com.gta.zssx.fun.classroomFeedback.model.bean.ClassroomFeedbackBean;
import com.gta.zssx.fun.classroomFeedback.model.bean.ClassroomSaveEvaluateBean;
import com.gta.zssx.fun.classroomFeedback.model.bean.RegisterDetailsBean;
import com.gta.zssx.fun.classroomFeedback.model.bean.RegisterPageBean;
import com.gta.zssx.fun.classroomFeedback.model.bean.SubmitOneWeekBean;
import com.gta.zssx.fun.classroomFeedback.model.bean.SubmitSaveScoreBean;
import com.gta.zssx.pub.http.HttpResult;

import java.util.List;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * 课堂教学反馈接口
 * Created by liang.lu on 2017/6/28 15:21.
 */

public interface ClassroomInterfaceList {
    /**
     * 课堂教学反馈首页
     *
     * @param classId 班级Id
     * @param type    区分已登记未登记的标示
     * @return 课堂教学首页列表
     */
    @GET (ClassroomInterfaceName.GET_CLASSROOM_FEEDBACK_DATA)
    Observable<HttpResult<List<ClassroomFeedbackBean>>> getClassroomFeedbackData (@Query ("classId") int classId, @Query ("type") int type);

    /**
     * 登记页详情
     *
     * @param classId 班级ID
     * @param weekly  周次
     * @return 登记页详情
     */
    @GET (ClassroomInterfaceName.GET_CLASSROOM_REGISTER_DATA)
    Observable<HttpResult<RegisterPageBean>> getClassroomRegisterData (@Query ("classId") int classId, @Query ("weekly") int weekly);

    /**
     * 获取登记扣分详情
     *
     * @param section  节次
     * @param date     日期
     * @param weekDate 周次
     * @param classId  班级ID
     * @return 登记扣分详情页数据
     */
    @GET (ClassroomInterfaceName.GET_REGISTER_DETAILS)
    Observable<HttpResult<RegisterDetailsBean>> getRegisterDetails (@Query ("classId") int classId, @Query ("section") int section, @Query ("date") String date, @Query ("weekDate") int weekDate);

    /**
     * 保存评分
     *
     * @param submitSaveScoreBean 实体
     * @return 成功或失败
     */
    @POST (ClassroomInterfaceName.UPLOAD_SCORE_DATA)
    Observable<HttpResult<String>> uploadScoreData (@Body SubmitSaveScoreBean submitSaveScoreBean);

    /**
     * 送审
     *
     * @param oneWeekBean bean
     * @return 成功或失败
     */
    @POST (ClassroomInterfaceName.SUBMIT_ONE_WEEK_DATA)
    Observable<HttpResult<String>> submitOneWeekData (@Body SubmitOneWeekBean oneWeekBean);

    /**
     * 保存评价
     *
     * @param evaluateBean 数据实体
     * @return 成功或失败
     */
    @POST (ClassroomInterfaceName.UPLOAD_EVALUATE)
    Observable<HttpResult<String>> saveAuditContent (@Body ClassroomSaveEvaluateBean evaluateBean);
}
