package com.gta.zssx.fun.coursedaily.registercourse.presenter;

import android.content.Context;
import android.util.Log;

import com.gta.utils.mvp.BasePresenter;
import com.gta.utils.resource.L;
import com.gta.zssx.R;
import com.gta.zssx.fun.coursedaily.registercourse.view.MultiselectTeacherView;
import com.gta.zssx.patrolclass.model.PatrolClassManager;
import com.gta.zssx.patrolclass.model.entity.ChooseTeacherEntity;
import com.gta.zssx.patrolclass.model.entity.ChooseTeacherSearchEntity;
import com.gta.zssx.pub.exception.CustomException;

import java.util.List;

import rx.Subscriber;

/**
 * Created by lan.zheng on 2017/3/3.
 */
public class MultiselectTeacherPresenter extends BasePresenter<MultiselectTeacherView> {

    private Context mContext;
    public MultiselectTeacherPresenter(Context context){
        mContext = context;
    }

    public void getTeacherData(String DepId ,boolean isRequestHaveNext) {
        if (!isViewAttached())
            return;

        getView().showLoadingDialog();

//        HttpMethod.getInstance().setmRetrofit(null);
        mCompositeSubscription.add(PatrolClassManager.getChooseTeacherDatas(DepId)
                .subscribe(new Subscriber<List<ChooseTeacherEntity>>() {

                    @Override
                    public void onCompleted() {
                        if(isViewAttached()){
                            getView().hideDialog();
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        if (isViewAttached()){
                            getView().onErrorHandle(e);
                            getView().hideDialog();
                            Log.e("lenita","e = "+e.toString());
                            getView().getTeacherListFailed(isRequestHaveNext);
                        }

                    }

                    @Override
                    public void onNext(List<ChooseTeacherEntity> chooseTeacherEntities) {
                        if (isViewAttached()){
                            if (chooseTeacherEntities.get(0).getDeptList().size() == 0){
                                getView().showToast(chooseTeacherEntities.get(0).getName()+"部门下无部门或教师");
                            }else {
                                getView ().showResult (chooseTeacherEntities,isRequestHaveNext);
                            }
                        }
                    }

                }));
    }


    public void getTeacherSearchDatas(String keyWord){
        if (!isViewAttached())
            return;

        getView().showLoadingDialog();
        mCompositeSubscription.add( PatrolClassManager.getTeacherSearchDatas(keyWord)
                .subscribe(new Subscriber<List<ChooseTeacherSearchEntity>>() {

                    @Override
                    public void onCompleted() {
                        if(isViewAttached()){
                            getView().hideDialog();
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        if (isViewAttached()){
                            getView().onErrorHandle(e);
                            getView().hideDialog();
                            getView().showToast(mContext.getResources().getString(R.string.text_search_failed));
                        }

                    }

                    @Override
                    public void onNext(List<ChooseTeacherSearchEntity> chooseTeacherSearchEntities) {
                        if (isViewAttached()){
                            if(chooseTeacherSearchEntities != null && chooseTeacherSearchEntities.size() != 0){
                                getView().showResultSearch(chooseTeacherSearchEntities);
                            } else {
                                getView().showToast(mContext.getResources().getString(R.string.text_search_not_match_teacher));
                            }
                        }
                    }

                }));
    }


}
