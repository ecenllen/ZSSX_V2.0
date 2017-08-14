package com.gta.zssx.fun.coursedaily.registercourse.presenter;

import com.gta.utils.mvp.BasePresenter;
import com.gta.zssx.fun.coursedaily.registercourse.model.ClassDataManager;
import com.gta.zssx.fun.coursedaily.registercourse.model.bean.SectionBean;
import com.gta.zssx.fun.coursedaily.registercourse.view.StudentSearchView;
import com.gta.zssx.pub.exception.CustomException;
import com.gta.zssx.pub.util.RxBus;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;

/**
 * Created by lan.zheng on 2017/2/28.
 */
public class StudentSearchPresenter  extends BasePresenter<StudentSearchView> {
    private Subscription mSubscription;

    public void test(){
        mSubscription = ClassDataManager.postSign(null)
                .flatMap(s -> Observable.just(s))
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {
                        getView().hideDialog();
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (getView() != null) {
                            if (e instanceof CustomException) {
                                int lCode = ((CustomException) e).getCode();
                                if (lCode == CustomException.HAS_SIGN) {
                                    RxBus.getDefault().post(new SectionBean());
                                }
                            }
                            getView().onErrorHandle(e);
                            getView().hideDialog();
                        }
                    }

                    @Override
                    public void onNext(String s) {

                    }
                });
        mCompositeSubscription.add(mSubscription);
    }
}
