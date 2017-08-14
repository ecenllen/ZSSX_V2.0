package com.gta.zssx.fun.coursedaily.registercourse.presenter;

import com.gta.utils.mvp.BasePresenter;
import com.gta.zssx.fun.coursedaily.registercourse.model.ClassDataManager;
import com.gta.zssx.fun.coursedaily.registercourse.model.bean.AddClassBean;
import com.gta.zssx.fun.coursedaily.registercourse.model.bean.SearchClassBean;
import com.gta.zssx.fun.coursedaily.registercourse.view.ClassSearchView;
import com.gta.zssx.pub.exception.CustomException;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;
import rx.Subscription;

/**
 * [Description]
 * <p/>
 * [How to use]
 * <p/>
 * [Tips]
 *
 * @author Created by Zhimin.Huang on 2016/6/17.
 * @since 1.0.0
 */
public class ClassSearchPresenter extends BasePresenter<ClassSearchView> {

    public Subscription mSubscribe;
    public List<SearchClassBean> mSearchClassBeen;

    public void search(String className) {

        if (!isViewAttached()) {
            return;
        }
        getView().showLoadingDialog();

        mSubscribe = ClassDataManager.search(className)
                .subscribe(new Subscriber<List<SearchClassBean>>() {
                    @Override
                    public void onCompleted() {
                        getView().hideDialog();
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (e instanceof CustomException) {
                            CustomException lErrorCodeException = (CustomException) e;
                            getView().showWarning(lErrorCodeException.getMessage());
                        }

                        getView().hideDialog();
                    }

                    @Override
                    public void onNext(List<SearchClassBean> searchClassBeen) {
                        mSearchClassBeen = searchClassBeen;
                        if (searchClassBeen!=null){
                            getView().showResult(searchClassBeen);
                        }else {
                            getView ().showInfo ("无匹配的搜索记录");
                        }
                    }
                });

        mCompositeSubscription.add(mSubscribe);
    }

    /**
     * 上传班级
     *
     * @param addClassBeen
     * @param
     */
    public void addClass(List<AddClassBean> addClassBeen) {

        if (!isViewAttached()) {
            return;
        }

        getView().showLoadingDialog();

        mCompositeSubscription.add(ClassDataManager.addClass(addClassBeen)
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {
                        getView().hideDialog();
                    }

                    @Override
                    public void onError(Throwable e) {
                        getView().onErrorHandle(e);
                        getView().hideDialog();
                    }

                    @Override
                    public void onNext(String s) {
                        getView().addSuccess();
                    }
                }));
    }

    public List<SearchClassBean> getSearchClassBeen() {
        return mSearchClassBeen;
    }


    public List<AddClassBean> getClassBean(SearchClassBean searchClassBean, String teacherId) {
        List<AddClassBean> lAddClassBeen = new ArrayList<>();
        AddClassBean lAddClassBean = new AddClassBean();
        lAddClassBean.setClassID(searchClassBean.getId());
        lAddClassBean.setTeacherID(teacherId);
        lAddClassBeen.add(lAddClassBean);
        return lAddClassBeen;
    }
}
