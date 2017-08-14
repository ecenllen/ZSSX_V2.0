package com.gta.zssx.fun.coursedaily.registercourse.presenter;

import com.gta.utils.mvp.BasePresenter;
import com.gta.zssx.fun.coursedaily.registercourse.model.ClassDataManager;
import com.gta.zssx.fun.coursedaily.registercourse.model.bean.StudentListBean;
import com.gta.zssx.fun.coursedaily.registercourse.model.bean.StudentListNewBean;
import com.gta.zssx.fun.coursedaily.registercourse.view.StudentListView;
import com.gta.zssx.pub.exception.CustomException;

import java.util.List;

import rx.Subscriber;
import rx.Subscription;

/**
 * [Description]
 * <p>
 * [How to use]
 * <p>
 * [Tips]
 *
 * @author Created by Zhimin.Huang on 2016/6/20.
 * @since 1.0.0
 */
@Deprecated
public class StudentListPresenter extends BasePresenter<StudentListView> {

    public List<StudentListNewBean> mStudentListBeen;
    public Subscription mSubscription;

    public void getStudentList(int classId, String signData) {

        if (!isViewAttached())
            return;
        List<StudentListNewBean> lStudentList = ClassDataManager.getDataCache().getStudentList();
        if (lStudentList != null) {
            getView().showResult(lStudentList);
            return;
        }

        getView().showLoadingDialog();
        // TODO: 2016/6/22 死数据
        mSubscription = ClassDataManager.getStudentList(70, "2016-05-05")
                .subscribe(new Subscriber<List<StudentListNewBean>>() {
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
                    public void onNext(List<StudentListNewBean> studentListBeen) {

                        mStudentListBeen = studentListBeen;
                        getView().showResult(studentListBeen);
                    }
                });

        mCompositeSubscription.add(mSubscription);

    }

    public List<StudentListNewBean> getStudentListBeen() {
        return mStudentListBeen;
    }


}
