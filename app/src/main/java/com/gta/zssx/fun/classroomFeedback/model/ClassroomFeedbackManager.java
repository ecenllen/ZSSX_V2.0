package com.gta.zssx.fun.classroomFeedback.model;

import com.gta.zssx.fun.classroomFeedback.model.bean.ClassroomFeedbackBean;
import com.gta.zssx.fun.classroomFeedback.model.bean.ClassroomSaveEvaluateBean;
import com.gta.zssx.fun.classroomFeedback.model.bean.RegisterDetailsBean;
import com.gta.zssx.fun.classroomFeedback.model.bean.RegisterPageBean;
import com.gta.zssx.fun.classroomFeedback.model.bean.SubmitOneWeekBean;
import com.gta.zssx.fun.classroomFeedback.model.bean.SubmitSaveScoreBean;
import com.gta.zssx.fun.classroomFeedback.view.base.ClassroomFeedback;
import com.gta.zssx.pub.ClassroomInterfaceList;
import com.gta.zssx.pub.http.HttpMethod;

import java.util.List;

import rx.Observable;

/**
 * [Description]
 * <p> 课堂教学接口访问管理类
 * [How to use]
 * <p>
 * [Tips]
 *
 * @author Created by liang.lu on 2017/6/28 10:27.
 * @since 2.0.0
 */
public class ClassroomFeedbackManager {
    /**
     * 生成 interface 接口对象
     *
     * @return interface接口对象
     */
    private static ClassroomInterfaceList getClassroomInterfaceList () {
        String lUrl = ClassroomFeedback.getInstance ().getmServerAddress ();
        return HttpMethod
                .getInstance ()
                .retrofitClient (lUrl)
                .create (ClassroomInterfaceList.class);
    }

    /**
     * 获取课堂教学反馈首页数据
     *
     * @param classId 班级id
     * @param type    区分已登记未登记标示
     * @return 课堂教学反馈首页数据
     */
    public static Observable<List<ClassroomFeedbackBean>> getClassroomFeedbackData (int classId, int type) {
        ClassroomInterfaceList interfaceList = getClassroomInterfaceList ();
        return HttpMethod.getInstance ().call (interfaceList.getClassroomFeedbackData (classId, type));
    }

    /**
     * 登记页详情
     *
     * @param classId 班级ID
     * @param weekly  周次
     * @return 登记页详情
     */
    public static Observable<RegisterPageBean> getClassroomRegisterData (int classId, int weekly) {
        ClassroomInterfaceList interfaceList = getClassroomInterfaceList ();
        return HttpMethod.getInstance ().call (interfaceList.getClassroomRegisterData (classId, weekly));
    }

    /**
     * 获取登记扣分详情
     *
     * @param classId  班级ID
     * @param section  节次
     * @param date     日期 如 2017-05-03
     * @param weekDate 周次 如 2
     * @return 登记详情
     */
    public static Observable<RegisterDetailsBean> getRegisterDetails (int classId, int section, String date, int weekDate) {
        ClassroomInterfaceList interfaceList = getClassroomInterfaceList ();
        return HttpMethod.getInstance ().call (interfaceList.getRegisterDetails (classId, section, date, weekDate));
    }

    /**
     * 保存评分
     *
     * @param submitSaveScoreBean 数据实体
     * @return 成功或失败
     */
    public static Observable<String> uploadScoreData (SubmitSaveScoreBean submitSaveScoreBean) {
        ClassroomInterfaceList interfaceList = getClassroomInterfaceList ();
        return HttpMethod.getInstance ().call (interfaceList.uploadScoreData (submitSaveScoreBean));
    }

    /**
     * 送审
     *
     * @param oneWeekBean bean
     * @return 成功 或 失败
     */
    public static Observable<String> submitOneWeekData (SubmitOneWeekBean oneWeekBean) {
        ClassroomInterfaceList interfaceList = getClassroomInterfaceList ();
        return HttpMethod.getInstance ().call (interfaceList.submitOneWeekData (oneWeekBean));
    }

    /**
     * 保存评价
     *
     * @param evaluateBean 实体
     * @return 成功 或 失败
     */
    public static Observable<String> saveAuditContent (ClassroomSaveEvaluateBean evaluateBean) {
        ClassroomInterfaceList interfaceList = getClassroomInterfaceList ();
        return HttpMethod.getInstance ().call (interfaceList.saveAuditContent (evaluateBean));
    }
}
