package com.gta.zssx.fun.coursedaily.registerrecord.presenter;

import com.gta.utils.mvp.BasePresenter;
import com.gta.zssx.fun.coursedaily.registercourse.model.bean.DetailItemShowBean;
import com.gta.zssx.fun.coursedaily.registerrecord.model.DTO.ClassInfoDto;
import com.gta.zssx.fun.coursedaily.registerrecord.model.DTO.DetailRegisteredRecordDto;
import com.gta.zssx.fun.coursedaily.registerrecord.model.RegisteredRecordManager;
import com.gta.zssx.fun.coursedaily.registerrecord.view.DetailRegisteredRecordView;
import com.gta.zssx.pub.util.LogUtil;

import java.util.List;

import rx.Subscriber;
import rx.Subscription;

/**
 * Created by lan.zheng on 2016/6/22.
 */
public class DetailRegisteredRecordPresenter extends BasePresenter<DetailRegisteredRecordView> {
    public Subscription mSubscribeList;
    public void getRegisteredRecordDetailData(ClassInfoDto classInfoDto){
        if (!isViewAttached()) {
            return;
        }
        getView().showLoadingDialog();
        mSubscribeList = RegisteredRecordManager.getRegisteredRecordDetailList(classInfoDto)
                .subscribe(new Subscriber<DetailRegisteredRecordDto>() {

                    @Override
                    public void onCompleted() {
                        if(isViewAttached()) {
                            getView().hideDialog();
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
//                        if (e instanceof CustomException) {
//                            CustomException lErrorCodeException = (CustomException) e;
//                            getView().showWarning(lErrorCodeException.getMessage());
//                        }
                        if(isViewAttached()) {
                            getView().onErrorHandle(e);
                            getView().updateExpandableList();
                        }

                    }

                    @Override
                    public void onNext(DetailRegisteredRecordDto detailRegisteredRecordDto) {
                        if(detailRegisteredRecordDto.getMemo() != null)
                            LogUtil.Log("lenita","getRegisteredRecordDetailData --> "+detailRegisteredRecordDto.getMemo());
                        if(isViewAttached()) {
                            getView().showResultList(detailRegisteredRecordDto);
                        }

                    }
                });
        mCompositeSubscription.add(mSubscribeList);
    }

    /**
     * 展示弹框的老师字符串
     * @param teacherInfoBeen
     * @return
     */
    public String getTeacherString(List<DetailItemShowBean.TeacherInfoBean> teacherInfoBeen){
        int maxShowNum = 10; //默认最多显示10个
        String teacherString = "";
        if(teacherInfoBeen.size() == 0){
            teacherString = "无";
        }else {
            if(teacherInfoBeen.size() <= maxShowNum){
                for(int i = 0;i < teacherInfoBeen.size();i++){
                    if(i != 0){
                        teacherString += "，";
                    }
                    teacherString += teacherInfoBeen.get(i).getTeacherName();
                }
            }else {
                for(int i = 0;i < maxShowNum;i++){
                    if(i != 0){
                        teacherString += "，";
                    }
                    teacherString += teacherInfoBeen.get(i).getTeacherName();
                }
                teacherString += "...等"+teacherInfoBeen.size()+"名教师";
            }

        }
        return teacherString;
    }

    /**
     * 展示课程名称
     * @param courseInfoBeen
     * @return
     */
    public String getCourseString(List<DetailItemShowBean.CourseInfoBean> courseInfoBeen){
        int maxShowNum = 10;  //默认最多显示10个
        String courseString = "";
        if(courseInfoBeen.size() == 0){
            courseString = "无";
        }else {
            if(courseInfoBeen.size() <= maxShowNum){
                for(int i = 0;i < courseInfoBeen.size();i++){
                    if(i != 0){
                        courseString += "，";
                    }
                    courseString += courseInfoBeen.get(i).getCourseName();
                }
            }else {
                for(int i = 0;i < maxShowNum;i++){
                    if(i != 0){
                        courseString += "，";
                    }
                    courseString += courseInfoBeen.get(i).getCourseName();
                }
                courseString += "...等"+courseInfoBeen.size()+"门课程";
            }

        }
        return courseString;
    }
}
