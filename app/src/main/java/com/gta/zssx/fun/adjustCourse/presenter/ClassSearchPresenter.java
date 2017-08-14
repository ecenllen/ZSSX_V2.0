package com.gta.zssx.fun.adjustCourse.presenter;

import com.gta.utils.mvp.BasePresenter;
import com.gta.zssx.fun.adjustCourse.model.AdjustDataManager;
import com.gta.zssx.fun.adjustCourse.model.bean.AdjustSearchClassBean;
import com.gta.zssx.fun.adjustCourse.model.bean.SearchClassBean;
import com.gta.zssx.fun.adjustCourse.view.AdjustClassSearchView;
import com.gta.zssx.pub.exception.CustomException;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Func1;

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
public class ClassSearchPresenter extends BasePresenter<AdjustClassSearchView> {

    public Subscription mSubscribe;
    public List<SearchClassBean> mSearchClassBeen;

    public void search(String className) {

        if (!isViewAttached()) {
            return;
        }
        getView().showLoadingDialog();

        mSubscribe = AdjustDataManager.searchClass(className)
                .flatMap(new Func1<List<AdjustSearchClassBean>, Observable<List<SearchClassBean>>>() {
                    @Override
                    public Observable<List<SearchClassBean>> call(List<AdjustSearchClassBean> adjustSearchClassBeen) {
                        List<SearchClassBean> lSearchClassBeen = new ArrayList<>();
                        for (int i = 0; i < adjustSearchClassBeen.size(); i++) {
                            SearchClassBean lSearchClassBean = new SearchClassBean();
                            lSearchClassBean.setClassName(adjustSearchClassBeen.get(i).getClassName());
                            lSearchClassBean.setId(adjustSearchClassBeen.get(i).getId());
                            lSearchClassBeen.add(lSearchClassBean);
                        }
                        return Observable.just(lSearchClassBeen);
                    }
                })
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
                        if (searchClassBeen != null && mSearchClassBeen.size() != 0) {
                            getView().showResult(searchClassBeen);
                        } else {
                            getView().showInfo("该班级不存在");
                            getView().showEmpty();
                        }
                    }
                });

        mCompositeSubscription.add(mSubscribe);
    }


    public List<SearchClassBean> getSearchClassBeen() {
        return mSearchClassBeen;
    }


}
