package com.gta.zssx.fun.classroomFeedback.presenter;

import com.gta.utils.mvp.BasePresenter;
import com.gta.zssx.AppConfiguration;
import com.gta.zssx.fun.classroomFeedback.model.ClassroomFeedbackManager;
import com.gta.zssx.fun.classroomFeedback.model.bean.ClassroomFeedbackBean;
import com.gta.zssx.fun.classroomFeedback.view.ClassroomUnRegisterView;
import com.gta.zssx.fun.personalcenter.model.bean.UserBean;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;

/**
 * [Description]
 * <p> 课堂教学反馈首页，未登记Fragment
 * [How to use]
 * <p>
 * [Tips]
 *
 * @author Created by liang.lu on 2017/06/20.
 * @since 2.0.0
 */

public class ClassroomUnRegisterPresenter extends BasePresenter<ClassroomUnRegisterView> {
    private UserBean mUserBean;

    public ClassroomUnRegisterPresenter () {
        super ();
        try {
            mUserBean = AppConfiguration.getInstance ().getUserBean ();
        } catch (Exception e) {
            e.printStackTrace ();
        }
    }

    public void loadData (int type,String state) {
        if (!isViewAttached ())
            return;
        getView ().showLoadingDialog ();

        mCompositeSubscription.add (ClassroomFeedbackManager.getClassroomFeedbackData (mUserBean.getClassId (), type)
                .flatMap (new Func1<List<ClassroomFeedbackBean>, Observable<List<ClassroomFeedbackBean.RegisterDataListBean>>> () {
                    @Override
                    public Observable<List<ClassroomFeedbackBean.RegisterDataListBean>> call (List<ClassroomFeedbackBean> classroomFeedbackBeen) {
                        List<ClassroomFeedbackBean.RegisterDataListBean> registerDataListBean = new ArrayList<> ();
                        if(classroomFeedbackBeen != null) {
                            for (ClassroomFeedbackBean bean : classroomFeedbackBeen) {
                                ClassroomFeedbackBean.RegisterDataListBean registerBean = new ClassroomFeedbackBean.RegisterDataListBean ();
                                registerBean.setClassName (bean.getClassName ());
                                registerBean.setTitle (true);
                                registerBean.setState ("");
                                registerDataListBean.add (registerBean);
                                registerDataListBean.addAll (bean.getRegisterDataList ());
                            }
                        }
                        return Observable.just (registerDataListBean);
                    }
                }).subscribe (new Subscriber<List<ClassroomFeedbackBean.RegisterDataListBean>> () {
                    @Override
                    public void onCompleted () {
                        if (isViewAttached ()) {
                            getView ().hideDialog ();
                        }
                    }

                    @Override
                    public void onError (Throwable e) {
                        if (isViewAttached ()) {
                            getView ().hideDialog ();
                            getView ().onErrorHandle (e);
                            getView ().showEmpty (state, false);
                        }
                    }

                    @Override
                    public void onNext (List<ClassroomFeedbackBean.RegisterDataListBean> dataListBeen) {
                        if (isViewAttached ()) {
                            if (dataListBeen == null||dataListBeen.size () ==0) {
                                getView ().showEmpty (state,true);
                                getView ().showToast();
                                return;
                            }
                            getView ().showResult (dataListBeen,state);
                        }
                    }
                }));
    }

    //    public void loadData () {
    //        List<ClassroomFeedbackBean> list = ClassroomFeedbackBean.getClassroomFeedbackData (AppConfiguration.mContext);
    //        List<ClassroomFeedbackBean.RegisterDataListBean> registerDataListBean = new ArrayList<> ();
    //        for (ClassroomFeedbackBean bean : list) {
    //            ClassroomFeedbackBean.RegisterDataListBean registerBean = new ClassroomFeedbackBean.RegisterDataListBean ();
    //            registerBean.setClassName (bean.getClassName ());
    //            registerBean.setTitle (true);
    //            registerBean.setState ("");
    //            registerDataListBean.add (registerBean);
    //            registerDataListBean.addAll (bean.getRegisterDataList ());
    //
    //        }
    //        getView ().showResult (registerDataListBean);
    //    }
}
