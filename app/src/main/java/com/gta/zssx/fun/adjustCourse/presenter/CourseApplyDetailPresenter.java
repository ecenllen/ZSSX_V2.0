package com.gta.zssx.fun.adjustCourse.presenter;

import com.gta.utils.mvp.BasePresenter;
import com.gta.zssx.fun.adjustCourse.model.AdjustDataManager;
import com.gta.zssx.fun.adjustCourse.model.bean.AdjustCourseBean;
import com.gta.zssx.fun.adjustCourse.model.bean.ApplySuccessBean;
import com.gta.zssx.fun.adjustCourse.model.bean.CurrentSemesterBean;
import com.gta.zssx.fun.adjustCourse.model.bean.ReplaceCourseBean;
import com.gta.zssx.fun.adjustCourse.model.bean.ScheduleBean;
import com.gta.zssx.fun.adjustCourse.view.CourseApplyDetailView;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import rx.Subscriber;

/**
 * [Description]
 * <p>
 * [How to use]
 * <p>
 * [Tips]
 *
 * @author Created by Zhimin.Huang on 2017/3/21.
 * @since 1.0.0
 */
public class CourseApplyDetailPresenter extends BasePresenter<CourseApplyDetailView> {

    public void applyCourseReplace(ReplaceCourseBean replaceCourseBean) {

        if (getView() == null)
            return;
        getView().showDialog("正在提交", false);

        mCompositeSubscription.add(AdjustDataManager.applyCourseReplace(replaceCourseBean)
                .subscribe(new Subscriber<ApplySuccessBean>() {
                    @Override
                    public void onCompleted() {
                        getView().hideDialog();
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (getView() != null) {
                            getView().hideDialog();
                            getView().onErrorHandle(e);
                        }
                    }

                    @Override
                    public void onNext(ApplySuccessBean applySuccessBean) {
                        getView().replaceCourseSuccess(applySuccessBean);

                    }
                }));
    }

    public void applyCourseAdjust(AdjustCourseBean adjustCourseBean) {
        if (getView() == null)
            return;
        getView().showDialog("正在提交", false);

        mCompositeSubscription.add(AdjustDataManager.applyCourseAdjust(adjustCourseBean)
                .subscribe(new Subscriber<ApplySuccessBean>() {
                    @Override
                    public void onCompleted() {
                        getView().hideDialog();
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (getView() != null) {
                            getView().hideDialog();
                            getView().onErrorHandle(e);
                        }
                    }

                    @Override
                    public void onNext(ApplySuccessBean applySuccessBean) {
                        getView().AdjustCourseSuccess(applySuccessBean);

                    }
                }));
    }

    public Map<String, List<ScheduleBean.SectionBean>> sortMap(Map<String, List<ScheduleBean.SectionBean>> appListMap) {
        TreeMap<String, List<ScheduleBean.SectionBean>> lTreeMap = new TreeMap<>(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                int section1 = Integer.valueOf(o1.substring(0, 1));
                int section2 = Integer.valueOf(o2.substring(0, 1));
                if (section2 > section1) {
                    return 1;
                } else {
                    return -1;
                }

            }
        });
        lTreeMap.putAll(appListMap);
        HashMap<String, List<ScheduleBean.SectionBean>> lHashMap = new HashMap<>(lTreeMap);
        return lHashMap;
    }

    public void getSemester() {
        if (getView() == null)
            return;
        getView().showLoadingDialog();
        mCompositeSubscription.add(AdjustDataManager.getSemester()
                .subscribe(new Subscriber<CurrentSemesterBean>() {
                    @Override
                    public void onCompleted() {
                        getView().hideDialog();
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (getView() != null) {

                            getView().hideDialog();
                            getView().onErrorHandle(e);
                        }

                    }

                    @Override
                    public void onNext(CurrentSemesterBean currentSemesterBean) {
                        getView().showSemester(currentSemesterBean);
                    }
                }));
    }
}
