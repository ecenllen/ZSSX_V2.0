package com.gta.zssx.fun.classroomFeedback.presenter;

import android.content.Context;

import com.gta.utils.mvp.BasePresenter;
import com.gta.zssx.R;
import com.gta.zssx.fun.classroomFeedback.view.CourseSearchView;
import com.gta.zssx.fun.coursedaily.registercourse.model.ClassDataManager;
import com.gta.zssx.fun.coursedaily.registercourse.model.bean.DetailItemShowBean;

import java.util.List;

import rx.Subscriber;

/**
 * [Description]
 * <p> 课堂教学反馈课程选择界面
 * [How to use]
 * <p>
 * [Tips]
 *
 * @author Created by liang.lu on 2017/06/20.
 * @since 2.0.0
 */

public class CourseSearchPresenter extends BasePresenter<CourseSearchView> {
    private Context mContext;

    public CourseSearchPresenter (Context context) {
        super ();
        this.mContext = context;
    }

    public void loadCourseSearchData (String keyWord) {
        if (!isViewAttached ())
            return;
        getView ().showLoadingDialog ();
        mCompositeSubscription.add (ClassDataManager.searchAllCourseByKeyWord (keyWord)
                .subscribe (new Subscriber<List<DetailItemShowBean.CourseInfoBean>> () {

                    @Override
                    public void onCompleted () {
                        if (isViewAttached ()) {
                            getView ().hideDialog ();
                        }

                    }

                    @Override
                    public void onError (Throwable e) {
                        if (isViewAttached ()) {
                            getView ().onErrorHandle (e);
                            getView ().hideDialog ();
                        }

                    }

                    @Override
                    public void onNext (List<DetailItemShowBean.CourseInfoBean> courseInfoBeanList) {
                        if (isViewAttached ()) {
                            if (courseInfoBeanList == null || courseInfoBeanList.size () == 0) {
                                getView ().showToast (mContext.getResources ().getString (R.string.text_search_not_match_course));
                            } else {
                                getView ().showSearchResult (courseInfoBeanList);
                            }
                        }
                    }

                }));

    }

    public void loadMyCourseData (String teacherId, String date) {
        if (!isViewAttached ())
            return;

        getView ().showLoadingDialog ();
        mCompositeSubscription.add (ClassDataManager.getTeacherTaughtCourse (teacherId, date)
                .subscribe (new Subscriber<List<DetailItemShowBean.CourseInfoBean>> () {

                    @Override
                    public void onCompleted () {
                        if (isViewAttached ()) {
                            getView ().hideDialog ();
                        }

                    }

                    @Override
                    public void onError (Throwable e) {
                        if (isViewAttached ()) {
                            getView ().onErrorHandle (e);
                            getView ().hideDialog ();
                        }

                    }

                    @Override
                    public void onNext (List<DetailItemShowBean.CourseInfoBean> courseInfoBeanList) {
                        if (isViewAttached ()) {
                            if (courseInfoBeanList != null) {
                                getView ().showMyCourseResult (courseInfoBeanList);
                            }
                        }
                    }
                }));

    }
}
