package com.gta.zssx.fun.classroomFeedback.presenter;

import com.gta.utils.mvp.BasePresenter;
import com.gta.zssx.fun.classroomFeedback.view.ChangedTeacherView;
import com.gta.zssx.patrolclass.model.PatrolClassManager;
import com.gta.zssx.patrolclass.model.entity.ChooseTeacherEntity;

import java.util.List;

import rx.Subscriber;

/**
 * [Description]
 * <p> 教师选择页面
 * [How to use]
 * <p>
 * [Tips]
 *
 * @author Created by liang.lu on 2017/6/28 10:27.
 * @since 2.0.0
 */

public class ChangedTeacherPresenter extends BasePresenter<ChangedTeacherView> {

    public void loadData (String deptId) {
        if (!isViewAttached ())
            return;
        getView ().showLoadingDialog ();

        mCompositeSubscription.add (PatrolClassManager.getChooseTeacherDatas (deptId)
                .subscribe (new Subscriber<List<ChooseTeacherEntity>> () {
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
                            getView ().showError (e.getMessage ());
                            getView ().showEmpty ();
                        }
                    }

                    @Override
                    public void onNext (List<ChooseTeacherEntity> entities) {
                        if (isViewAttached ()) {
                            if (entities == null) {
                                getView ().showEmpty ();
                                return;
                            }
                            getView ().showResult (entities);
                        }
                    }
                }));
    }
}
