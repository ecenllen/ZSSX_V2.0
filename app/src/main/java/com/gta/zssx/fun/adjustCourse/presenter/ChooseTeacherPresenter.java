package com.gta.zssx.fun.adjustCourse.presenter;

import android.content.Context;

import com.gta.utils.mvp.BasePresenter;
import com.gta.zssx.fun.adjustCourse.model.AdjustDataManager;
import com.gta.zssx.fun.adjustCourse.model.bean.GetTeacherBean;
import com.gta.zssx.fun.adjustCourse.model.bean.TeacherBean;
import com.gta.zssx.fun.adjustCourse.view.ChooseTeacherView;

import java.util.List;

import rx.Subscriber;

/**
 * Created by liang.lu on 2016/8/16 14:54.
 */
public class ChooseTeacherPresenter extends BasePresenter<ChooseTeacherView> {


    private Context mContext;

    public ChooseTeacherPresenter(Context context) {
        mContext = context;
    }

    public void getAllTeacher(int deptId) {
        if (getView() == null)
            return;
        getView().showLoadingDialog();
        mCompositeSubscription.add(AdjustDataManager.getAllTeacher(deptId)
                .subscribe(new Subscriber<List<TeacherBean>>() {
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
                    public void onNext(List<TeacherBean> teacherBeans) {
                        getView().showResult(teacherBeans);
                    }
                }));
    }

    public void getSomeTeacher(GetTeacherBean getTeacherBean) {
        if (getView() == null)
            return;
        getView().showLoadingDialog();
        mCompositeSubscription.add(AdjustDataManager.getSomeTeacher(getTeacherBean.getDate(),
                getTeacherBean.getApplyTechId(), getTeacherBean.getSelectUnitApply(), ""
                , getTeacherBean.getApplyType(), getTeacherBean.getApplyClassId() + "",
                getTeacherBean.getDeptId()+"", getTeacherBean.getNumberTeacher())
                .subscribe(new Subscriber<List<TeacherBean>>() {
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
                    public void onNext(List<TeacherBean> teacherBeen) {
                        getView().showResult(teacherBeen);
                    }
                }));
    }
}
