package com.gta.zssx.fun.adjustCourse.presenter;

import android.content.Context;

import com.gta.utils.mvp.BasePresenter;
import com.gta.zssx.fun.adjustCourse.model.AdjustDataManager;
import com.gta.zssx.fun.adjustCourse.model.bean.GetTeacherBean;
import com.gta.zssx.fun.adjustCourse.model.bean.TeacherRecommendBean;
import com.gta.zssx.fun.adjustCourse.view.TeacherRecommendView;

import rx.Subscriber;

/**
 * [Description]
 * <p>
 * [How to use]
 * <p>
 * [Tips]
 *
 * @author Created by Zhimin.Huang on 2016/10/24.
 * @since 1.0.0
 */
public class TeacherRecommendPresenter extends BasePresenter<TeacherRecommendView> {
    private Context mContext;

    public TeacherRecommendPresenter(Context context) {
        mContext = context;
    }

    public void getTeacherRecommend(GetTeacherBean getTeacherBean) {

        if (getView() == null)
            return;
        getView().showLoadingDialog();

        mCompositeSubscription.add(AdjustDataManager.getTeacherRecommend(getTeacherBean.getDate(), getTeacherBean.getNumberTeacher()
                , getTeacherBean.getApplyTechBId(),
                getTeacherBean.getApplyClassId(), getTeacherBean.getSelectUnitApply(), getTeacherBean.getApplyType())
                .subscribe(new Subscriber<TeacherRecommendBean>() {
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
                    public void onNext(TeacherRecommendBean teacherRecommendBean) {
                        getView().showResult(teacherRecommendBean);
                    }
                }));
    }
}
