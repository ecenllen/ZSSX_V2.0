package com.gta.zssx.fun.coursedaily.registerrecord.presenter;

import com.gta.utils.mvp.BasePresenter;
import com.gta.zssx.fun.coursedaily.registercourse.model.ClassDataManager;
import com.gta.zssx.fun.coursedaily.registercourse.model.bean.ApproveBean;
import com.gta.zssx.fun.coursedaily.registerrecord.model.DTO.DeleteRecordDto;
import com.gta.zssx.fun.coursedaily.registerrecord.model.DTO.RegisteredRecordFromSignatureDto;
import com.gta.zssx.fun.coursedaily.registerrecord.model.DTO.SaveCacheDto;
import com.gta.zssx.fun.coursedaily.registerrecord.model.RegisteredRecordManager;
import com.gta.zssx.fun.coursedaily.registerrecord.view.AlreadyRegisteredRecordFromSignatureView;
import com.gta.zssx.pub.exception.CustomException;
import com.gta.zssx.pub.util.LogUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import rx.Subscriber;
import rx.Subscription;

/**
 * Created by lan.zheng on 2016/6/22.
 */
public class AlreadyRegisterRecordFromSignaturePresenter extends BasePresenter<AlreadyRegisteredRecordFromSignatureView> {
    public Subscription mSubscribeList;
    public void getRegisteredRecordFromSignatureData(String teacherId,String signDate,String classId,boolean isGetClassIDAllInfo){
        if (!isViewAttached()) {
            return;
        }
        getView().showLoadingDialog();
        mSubscribeList = RegisteredRecordManager.getRegisteredRecordFromSignatureList(teacherId,signDate,classId,isGetClassIDAllInfo)
                .subscribe(new Subscriber<List<RegisteredRecordFromSignatureDto>>() {

                    @Override
                    public void onCompleted() {
                        if(isViewAttached()){
                            getView().hideDialog();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
//                        if (e instanceof CustomException) {
//                            CustomException lErrorCodeException = (CustomException) e;
//                            getView().showWarning(lErrorCodeException.getMessage());
//                        }
                        if(isViewAttached()){
                            getView().onErrorHandle(e);
                            getView().notResult();
                            getView().hideDialog();
                        }

                    }

                    @Override
                    public void onNext(List<RegisteredRecordFromSignatureDto> registeredRecordFromSignatureDtos) {
                        LogUtil.Log("lenita","getRegisteredRecordFromSignatureData");
                        if(isViewAttached()){
                            getView().showResultList(registeredRecordFromSignatureDtos);
                        }

                    }
                });
        mCompositeSubscription.add(mSubscribeList);
    }

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

    public int getSectionNum(List<RegisteredRecordFromSignatureDto> registeredRecordFromSignatureDtos){
        Set<RegisteredRecordFromSignatureDto> registeredRecordFromSignatureDtoSet = new HashSet<>();
        for(int i = 0;i<registeredRecordFromSignatureDtos.size();i++){
            registeredRecordFromSignatureDtoSet.add(registeredRecordFromSignatureDtos.get(i));
        }
        return registeredRecordFromSignatureDtoSet.size();
    }

    public static SaveCacheDto mSaveCacheDto;

    public SaveCacheDto getSaveCacheDto() {
            return mSaveCacheDto;
        }

    public void setSaveCacheDto(SaveCacheDto saveCacheDto) {
            mSaveCacheDto = saveCacheDto;
        }


}
