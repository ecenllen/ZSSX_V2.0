package com.gta.zssx.fun.coursedaily.registercourse.presenter;

import android.content.Context;

import com.gta.utils.mvp.BasePresenter;
import com.gta.zssx.R;
import com.gta.zssx.fun.coursedaily.registercourse.model.ClassDataManager;
import com.gta.zssx.fun.coursedaily.registercourse.model.bean.DetailItemShowBean;
import com.gta.zssx.fun.coursedaily.registercourse.view.MultiselectCourseView;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;

/**
 * Created by lan.zheng on 2017/3/9.
 */
public class MultiselectCoursePresenter extends BasePresenter<MultiselectCourseView> {
    private Context mContext;

    public MultiselectCoursePresenter(Context context){
        mContext = context;
    }

    public void getMyCourseData(String teacherId ,String date) {
        if (!isViewAttached())
            return;

        getView().showLoadingDialog();
        mCompositeSubscription.add(ClassDataManager.getTeacherTaughtCourse(teacherId,date)
                .subscribe(new Subscriber<List<DetailItemShowBean.CourseInfoBean>>() {

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
                            getView().getMyCourseFailed();
                        }

                    }

                    @Override
                    public void onNext(List<DetailItemShowBean.CourseInfoBean> courseInfoBeanList) {
                        if (isViewAttached()){
                            if (courseInfoBeanList.size() == 0){
                                getView().getMyCourseFailed();
                            }else {
                                getView().showMyCourse (courseInfoBeanList);
                            }
                        }
                    }

                }));
    }


   public void submitUserDefinedCourse(String courseName,String teacherId){
       if (!isViewAttached())
           return;

       getView().showLoadingDialog();
       mCompositeSubscription.add(ClassDataManager.submitUserDefinedCourse(courseName,teacherId)
               .subscribe(new Subscriber<DetailItemShowBean.CourseInfoBean>() {

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
                       }

                   }

                   @Override
                   public void onNext(DetailItemShowBean.CourseInfoBean courseInfoBean) {
                       if (isViewAttached()){
                           //TODO 后台给错了 code，记得叫他改过来
                           getView().addCustomCourse(courseInfoBean);
                       }
                   }

               }));
   }

    public void getSearchCourseData(String keyWord){
        if (!isViewAttached())
            return;
        getView().showLoadingDialog();
        mCompositeSubscription.add(ClassDataManager.searchAllCourseByKeyWord(keyWord)
                .subscribe(new Subscriber<List<DetailItemShowBean.CourseInfoBean>>() {

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
                    public void onNext(List<DetailItemShowBean.CourseInfoBean> courseInfoBeanList) {
                        if (isViewAttached()){
                            if(courseInfoBeanList != null && courseInfoBeanList.size() != 0){
                                getView().showResultSearch(courseInfoBeanList);
                            }else {
                                getView().showToast(mContext.getResources().getString(R.string.text_search_not_match_course));
                            }
                        }
                    }

                }));
    }

    private void testData(){
        List<DetailItemShowBean.CourseInfoBean> courseInfoBeanList = new ArrayList<>();
        for(int i = 1;i<5;i++){
            DetailItemShowBean.CourseInfoBean courseInfoBean = new DetailItemShowBean.CourseInfoBean();
            courseInfoBean.setCourseName("课程测试"+i);
            courseInfoBean.setCourseId(""+i);
            courseInfoBeanList.add(courseInfoBean);
        }
        DetailItemShowBean.CourseInfoBean courseInfoBean1 = new DetailItemShowBean.CourseInfoBean();
        courseInfoBean1.setCourseName("11121122");
        courseInfoBean1.setCourseId("99");
        DetailItemShowBean.CourseInfoBean courseInfoBean2 = new DetailItemShowBean.CourseInfoBean();
        courseInfoBean2.setCourseName("1测试1测试1");
        courseInfoBean2.setCourseId("100");
        courseInfoBeanList.add(courseInfoBean1);
        courseInfoBeanList.add(courseInfoBean2);
        getView().showResultSearch(courseInfoBeanList);
    }
}
