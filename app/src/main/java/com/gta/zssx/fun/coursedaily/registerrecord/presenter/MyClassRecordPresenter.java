package com.gta.zssx.fun.coursedaily.registerrecord.presenter;

import com.gta.utils.mvp.BasePresenter;
import com.gta.zssx.fun.coursedaily.registercourse.model.ClassDataManager;
import com.gta.zssx.fun.coursedaily.registercourse.model.bean.ApproveBean;
import com.gta.zssx.fun.coursedaily.registerrecord.model.DTO.DeleteRecordDto;
import com.gta.zssx.fun.coursedaily.registerrecord.model.DTO.MyClassRecordDto;
import com.gta.zssx.fun.coursedaily.registerrecord.model.DTO.ServerTimeDto;
import com.gta.zssx.fun.coursedaily.registerrecord.model.RegisteredRecordManager;
import com.gta.zssx.fun.coursedaily.registerrecord.view.MyClassRecordView;
import com.gta.zssx.pub.exception.CustomException;
import com.gta.zssx.pub.util.LogUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import rx.Subscriber;
import rx.Subscription;
import rx.functions.Action1;

/**
 * Created by lan.zheng on 2016/6/25.
 */
public class MyClassRecordPresenter extends BasePresenter<MyClassRecordView> {

//    public static boolean mIsShowCache  = false;
    /**
     * 获取我的班级--唯一一个班级数据
     * @param teacherId
     * @param date
     * @param classId
     * @param week
     */
    public void getMyClass(String teacherId, final String date, int classId, final String week ) {

        if (!isViewAttached())
            return;

        /*List<MyClassRecordDto> lRecordFromSignatureDtos = RegisteredRecordManager.getDataCache().getRecordFromSignatureDtos();
        if (mIsShowCache && lRecordFromSignatureDtos != null) {
            getView().showResult(lRecordFromSignatureDtos);
            mIsShowCache = false;
            return;
        }*/
        getView().showLoadingDialog();
        mCompositeSubscription.add(RegisteredRecordManager.getMyclass(teacherId, date, classId)
                .doOnNext(new Action1<List<MyClassRecordDto>>() {
                    @Override
                    public void call(List<MyClassRecordDto> recordFromSignatureDtos) {
                        //这里要注意，需要是有课程才会进行缓存
                        if( recordFromSignatureDtos!=null && recordFromSignatureDtos.get(0).getCourse() != null){
                            RegisteredRecordManager.getDataCache().setRecordFromSignatureDtos(recordFromSignatureDtos);
                            RegisteredRecordManager.getDataCache().setDateandWeek(date,week);
                        }
                    }
                }).subscribe(new Subscriber<List<MyClassRecordDto>>() {
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
                            getView().hideDialog();
                            getView().notResult();
                        }

                    }

                    @Override
                    public void onNext(List<MyClassRecordDto> registeredRecordFromSignatureDtos) {
                        if(isViewAttached()) {
                            getView().showResult(registeredRecordFromSignatureDtos);
                        }

                    }
                }));
    }

    /**
     * 获取服务器时间
     */
    public Subscription mSubscribe;
    public void getServerTime( final String TeacherID) {
        if (!isViewAttached()) {
            return;
        }
        mSubscribe = RegisteredRecordManager.getServerTime()
                .subscribe(new Subscriber<ServerTimeDto>() {

                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        if(isViewAttached()) {
                            boolean isNotNetwork = false;
                            if (throwable instanceof CustomException) {   //抛出自定义的异常
                                CustomException lE = (CustomException) throwable;
                                if (lE.getCode() == CustomException.NETWORK_UNAVAILABLE) {
                                    isNotNetwork = true;
                                }
                            }
                            getView().onErrorHandle(throwable);  //抛出默认异常
                            getView().setServerTimeFailed(isNotNetwork);
                        }

                    }

                    @Override
                    public void onNext(ServerTimeDto serverTimeDto) {
                        String mDate = serverTimeDto.getDate();
                        String week =  RegisteredRecordManager.getWeek(mDate);
                        if(isViewAttached()) {
                            //返回网络时间
                            getView().setServerTime(mDate,week);
                        }

                    }
                });
        mCompositeSubscription.add(mSubscribe);
    }

    /**
     * 获取领导审核日
     * @param signDate
     */
    public void getApproveDate(String signDate) {
        if (!isViewAttached()) {
            return;
        }
        getView().showLoadingDialog();
        mCompositeSubscription.add(ClassDataManager.getApproveDate()
                .subscribe(new Subscriber<ApproveBean>() {
                    @Override
                    public void onCompleted() {
                        getView().hideDialog();
                    }

                    @Override
                    public void onError(Throwable throwable) {
//                        if (e instanceof CustomException) {
//                            CustomException lErrorCodeException = (CustomException) e;
//                            getView().showWarning(lErrorCodeException.getMessage());
//                        }
                        if(isViewAttached()){
                            getView().hideDialog();
                            if (throwable instanceof CustomException) {   //抛出自定义的异常
                                getView().onErrorHandle(throwable);  //抛出默认异常
                                getView().getApproveFailed(false);
                            }else {
                                getView().getApproveFailed(true);
                            }

                        }

                    }

                    @Override
                    public void onNext(ApproveBean approveBean) {
                        if(isViewAttached()){
                            String approveDate = approveBean.getDate();
                            boolean isApprove = setApproveStatus(signDate,approveDate);
                            getView().showApproveResult(isApprove);
                        }

                    }
                }));
    }

    /**
     * 比对是否审核
     * @param SignDate
     * @param ApproveDate
     * @return
     */
    private boolean setApproveStatus(String SignDate,String ApproveDate) {
        boolean haveBeenApprove = false;
        SimpleDateFormat lSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date signTime = lSimpleDateFormat.parse(SignDate);
            Date approveTime = lSimpleDateFormat.parse(ApproveDate);
            if (signTime.getTime() >= approveTime.getTime()) {
                //在审核日期之后，含领导审核日
                haveBeenApprove = false;
            } else {
                haveBeenApprove = true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return haveBeenApprove;
    }

    /**
     * 删除一条记录
     * @param deleteRecordDto
     */
    public void deleteSignInfo(List<DeleteRecordDto> deleteRecordDto) {
        if (!isViewAttached()) {
            return;
        }
        getView().showLoadingDialog();
        mCompositeSubscription.add(RegisteredRecordManager.deleteSignInfo(deleteRecordDto)
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {
                        if(isViewAttached()){
                            getView().hideDialog();
                        }

                        LogUtil.Log("lenita"," deleteSignInfo(DeleteRecordDto deleteRecordDto)--complete");
                    }

                    @Override
                    public void onError(Throwable e) {
//                        if (e instanceof CustomException) {
//                            CustomException lErrorCodeException = (CustomException) e;
//                            getView().showWarning(lErrorCodeException.getMessage());
//                        }
                        if(isViewAttached()){
                            getView().onErrorHandle(e);
                            getView().hideDialog();
                            getView().deleteFailed();
                        }

                    }

                    @Override
                    public void onNext(String s) {
                        if(isViewAttached()){
                            getView().deleteResult();
                        }

                    }
                }));
    }
}
