package com.gta.zssx.fun.adjustCourse.presenter;

import android.content.Context;

import com.gta.utils.mvp.BasePresenter;
import com.gta.zssx.fun.adjustCourse.model.AdjustDataManager;
import com.gta.zssx.fun.adjustCourse.model.bean.CurrentSemesterBean;
import com.gta.zssx.fun.adjustCourse.view.ScheduleSearchView;

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
public class ScheduleSearchPresenter extends BasePresenter<ScheduleSearchView> {

    private Context mContext;

    public ScheduleSearchPresenter(Context context) {
        mContext = context;
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
                        getView().showResult(currentSemesterBean);
                    }
                }));
    }
}
