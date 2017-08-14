package com.gta.zssx.fun.adjustCourse.presenter;

import android.content.Context;

import com.gta.utils.mvp.BasePresenter;
import com.gta.zssx.fun.adjustCourse.model.AdjustDataManager;
import com.gta.zssx.fun.adjustCourse.model.bean.ChooseTeacherSearchEntity;
import com.gta.zssx.fun.adjustCourse.model.bean.GetTeacherBean;
import com.gta.zssx.fun.adjustCourse.model.bean.TeacherBean;
import com.gta.zssx.fun.adjustCourse.view.ChooseTeacherSearchView;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;

/**
 * Created by liang.lu on 2016/8/17 15:00.
 */
public class ChooseTeacherSearchPresenter extends BasePresenter<ChooseTeacherSearchView> {


    private Context mContext;

    public ChooseTeacherSearchPresenter(Context context) {
        mContext = context;
    }

    public void searchAllTeacher(String keyWork) {
        if (getView() == null)
            return;
        getView().showLoadingDialog();
        mCompositeSubscription.add(AdjustDataManager.SearchAllTeacher(keyWork)
                .flatMap(new Func1<TeacherBean, Observable<List<ChooseTeacherSearchEntity>>>() {
                    @Override
                    public Observable<List<ChooseTeacherSearchEntity>> call(TeacherBean teacherBean) {
                        List<TeacherBean.TeacherListBean> lTeacherList = teacherBean.getTeacherList();
                        List<ChooseTeacherSearchEntity> lSearchEntities = new ArrayList<>();

                        for (int i = 0; i < lTeacherList.size(); i++) {
                            TeacherBean.TeacherListBean lTeacherListBean = lTeacherList.get(i);
                            String[] lSplit = lTeacherListBean.getTeacherName().split(Pattern.quote("["));
                            ChooseTeacherSearchEntity lSearchEntity = new ChooseTeacherSearchEntity();
                            lSearchEntity.setName(lSplit[0]);
                            lSearchEntity.setTeacherId(lTeacherListBean.getTeacherBId());
                            lSearchEntity.setTeacherCode(lSplit[1].replace("[", "").replace("]", ""));
                            lSearchEntities.add(lSearchEntity);
                        }
                        return Observable.just(lSearchEntities);
                    }
                })
                .subscribe(new Subscriber<List<ChooseTeacherSearchEntity>>() {
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
                    public void onNext(List<ChooseTeacherSearchEntity> teacherBean) {
                        if (teacherBean == null || teacherBean.size() == 0) {
                            getView().showError("该教师不存在");
                            getView().showEmpty();
                        } else {
                            getView().showResult(teacherBean);
                        }
                    }
                })
        );
    }


    public void searchSomeTeacher(String keyWord, GetTeacherBean getTeacherBean) {
        if (getView() == null)
            return;
        getView().showLoadingDialog();
        mCompositeSubscription.add(AdjustDataManager.getSomeTeacher(getTeacherBean.getDate(), getTeacherBean.getApplyTechId(),
                getTeacherBean.getSelectUnitApply(), keyWord, getTeacherBean.getApplyType(), getTeacherBean.getApplyClassId() + "",
                "", getTeacherBean.getNumberTeacher())
                .flatMap(new Func1<List<TeacherBean>, Observable<List<TeacherBean.TeacherListBean>>>() {
                    @Override
                    public Observable<List<TeacherBean.TeacherListBean>> call(List<TeacherBean> teacherBeen) {
                        List<TeacherBean.TeacherListBean> lTeacherListBeen = new ArrayList<>();
                        for (int i = 0; i < teacherBeen.size(); i++) {
                            TeacherBean lTeacherBean = teacherBeen.get(i);
                            lTeacherListBeen.addAll(lTeacherBean.getTeacherList());
                        }
                        return Observable.just(lTeacherListBeen);
                    }
                })
                .flatMap(new Func1<List<TeacherBean.TeacherListBean>, Observable<List<ChooseTeacherSearchEntity>>>() {
                    @Override
                    public Observable<List<ChooseTeacherSearchEntity>> call(List<TeacherBean.TeacherListBean> teacherBean) {
                        List<ChooseTeacherSearchEntity> lSearchEntities = new ArrayList<>();
                        for (int i = 0; i < teacherBean.size(); i++) {
                            TeacherBean.TeacherListBean lTeacherListBean = teacherBean.get(i);
                            ChooseTeacherSearchEntity lSearchEntity = new ChooseTeacherSearchEntity();
                            String[] lSplit = lTeacherListBean.getTeacherName().split(Pattern.quote("["));
                            lSearchEntity.setName(lSplit[0]);
                            lSearchEntity.setTeacherId(lTeacherListBean.getTeacherId());
                            lSearchEntity.setTeacherBId(lTeacherListBean.getTeacherBId());
                            lSearchEntity.setTeacherCode(lSplit[1].replace("[", "").replace("]", ""));
                            lSearchEntities.add(lSearchEntity);
                        }
                        return Observable.just(lSearchEntities);
                    }
                })
                .subscribe(new Subscriber<List<ChooseTeacherSearchEntity>>() {
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
                    public void onNext(List<ChooseTeacherSearchEntity> teacherBeen) {
                        if (teacherBeen == null || teacherBeen.size() == 0) {
                            getView().showError("该教师不存在");
                            getView().showEmpty();
                        } else {
                            getView().showResult(teacherBeen);
                        }
                    }
                }));
    }
}
