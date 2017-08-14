package com.gta.zssx.fun.coursedaily.registerrecord.presenter;

import android.content.Context;

import com.gta.utils.mvp.BasePresenter;
import com.gta.zssx.R;
import com.gta.zssx.fun.coursedaily.registercourse.model.ClassDataManager;
import com.gta.zssx.fun.coursedaily.registercourse.model.bean.ApproveBean;
import com.gta.zssx.fun.coursedaily.registerrecord.model.DTO.DeleteRecordDto;
import com.gta.zssx.fun.coursedaily.registerrecord.model.DTO.MyClassRecordDto;
import com.gta.zssx.fun.coursedaily.registerrecord.model.RegisteredRecordManager;
import com.gta.zssx.fun.coursedaily.registerrecord.view.MyClassRecordSubTabView;
import com.gta.zssx.pub.exception.CustomException;
import com.gta.zssx.pub.util.LogUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import rx.Subscriber;
import rx.functions.Action1;

/**
 * Created by lan.zheng on 2016/7/2.
 */
public class MyClassRecordSubTabPresenter extends BasePresenter<MyClassRecordSubTabView> {

    public void getMyClass(String teacherId, final String date, final int classId, final String week, boolean isNeedToast,boolean isUpdateData, Context context) {

        if (!isViewAttached())
            return;

        if(isUpdateData)
            getView().showLoadingDialog();
        mCompositeSubscription.add(RegisteredRecordManager.getMyclass(teacherId, date, classId)
                .doOnNext(new Action1<List<MyClassRecordDto>>() {
                    @Override
                    public void call(List<MyClassRecordDto> recordFromSignatureDtos) {
                        LogUtil.Log("lenita","MyClassRecordSubTabPresenter call");
                        MyClassRecordDto lMyClassRecordDto = new MyClassRecordDto();
                        if(recordFromSignatureDtos == null){
                            RegisteredRecordManager.getMyClassRecordDataCache().updateMyClassRecordDtos(classId,lMyClassRecordDto ,true,date,week);
                            return;
                        }
                        if(recordFromSignatureDtos.size() > 1){
                            RegisteredRecordManager.getMyClassRecordDataCache().updateMyClassRecordDtos(classId,lMyClassRecordDto ,true,date,week);
                            return;
                        }
                        lMyClassRecordDto = recordFromSignatureDtos.get(0);
                        RegisteredRecordManager.getMyClassRecordDataCache().updateMyClassRecordDtos(classId,lMyClassRecordDto ,true,date,week);
                    }
                }).subscribe(new Subscriber<List<MyClassRecordDto>>() {
                    @Override
                    public void onCompleted() {
                        if(isViewAttached()) {
                            if (isUpdateData)
                                getView().hideDialog();
                        }

                    }

                    @Override
                    public void onError(Throwable throwable) {

                        if(isViewAttached()) {
                            if (isUpdateData)  //需要收起加载框
                                getView().hideDialog();
                            if(isNeedToast) {  //需要弹提示的时候弹出提示
                                getView().onErrorHandle(throwable);  //抛出默认异常
                            }
                            if(throwable instanceof CustomException){  //要特殊处理的异常
                                CustomException lCustomException = (CustomException) throwable;
                                if(lCustomException.getMessage().equals(context.getResources().getString(R.string.text_not_match_class_info))){
                                    getView().notResult(false,context.getResources().getString(R.string.text_you_are_not_headmaster) ); //不需要弹toast,这种情况下需要修改文字成别的信息
                                    return;
                                }
                            }
                            getView().notResult(false, "");      //其他情况都显示“暂无记录”

                        }

                    }

                    @Override
                    public void onNext(List<MyClassRecordDto> registeredRecordFromSignatureDtos) {
                        MyClassRecordDto lMyClassRecordDto = registeredRecordFromSignatureDtos.get(0);
                        if(isViewAttached()) {
                            if(lMyClassRecordDto == null){
                                getView().notResult(isNeedToast,"");
                            }
                            getView().showResult(isNeedToast,lMyClassRecordDto);
                        }
                    }
                }));
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
