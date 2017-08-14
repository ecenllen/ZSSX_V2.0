package com.gta.zssx.fun.coursedaily.registerrecord.presenter;

import com.gta.utils.mvp.BasePresenter;
import com.gta.zssx.AppConfiguration;
import com.gta.zssx.fun.coursedaily.registerrecord.model.DTO.MyClassRecordDto;
import com.gta.zssx.fun.coursedaily.registerrecord.model.DTO.ServerTimeDto;
import com.gta.zssx.fun.coursedaily.registerrecord.model.RegisteredRecordManager;
import com.gta.zssx.fun.coursedaily.registerrecord.view.MyClassRecordSelectTabView;
import com.gta.zssx.fun.personalcenter.model.bean.UserBean;
import com.gta.zssx.pub.exception.CustomException;
import com.gta.zssx.pub.util.LogUtil;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;
import rx.Subscription;
import rx.functions.Action1;

/**
 * Created by lan.zheng on 2016/7/1.
 */
public class MyClassRecordSelectTabPresenter extends BasePresenter<MyClassRecordSelectTabView> {

    /**
     * 获取服务器时间
     */
    public Subscription mSubscribe;

    public void getServerTime(final String TeacherID) {
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
                        if (getView() != null) {
                            boolean isNotNetwork = false;
                            if (throwable instanceof CustomException) {   //抛出自定义的异常
                                CustomException lE = (CustomException) throwable;
                                if (lE.getCode() == CustomException.NETWORK_UNAVAILABLE) {
                                    isNotNetwork = true;  //没有网络的时候该值为true
                                }
                            }
                            getView().onErrorHandle(throwable);  //抛出默认异常
                            getView().setServerTimeFailed(isNotNetwork);
                        }
                    }

                    @Override
                    public void onNext(ServerTimeDto serverTimeDto) {
                        String mDate = serverTimeDto.getDate();
                        String week = RegisteredRecordManager.getWeek(mDate);
                        //返回网络时间
                        if (getView() != null) {
                            getView().setServerTime(mDate, week);
                        }
                    }
                });
        mCompositeSubscription.add(mSubscribe);
    }

    private List<String> mClassId;

    /**
     * 在这里缓存多页面的班级数据，为了后面好用
     *
     * @param teacherId
     * @param date
     * @param classId
     * @param week
     * @param
     */
    public void getMyClass(String teacherId, final String date, int classId, final String week) {

        if (!isViewAttached())
            return;

        mClassId = new ArrayList<>();  //给予初始化
        try {
            UserBean lUserBean = AppConfiguration.getInstance().getUserBean();
            for (int l = 0; l < lUserBean.getTotalCount(); l++) {
                mClassId.add(lUserBean.getClassName().get(l).getId() + "");
            }
            //得到ClassId数组后，继续初始化缓存数组，为了安全
            RegisteredRecordManager.getMyClassRecordDataCache().initMyClassRecordDtos(mClassId);
        } catch (Exception e) {
            e.printStackTrace();
        }

        mCompositeSubscription.add(RegisteredRecordManager.getMyclass(teacherId, date, classId)
                .doOnNext(new Action1<List<MyClassRecordDto>>() {
                    @Override
                    public void call(List<MyClassRecordDto> recordFromSignatureDtos) {
                        //记得先判空
                        /*if(recordFromSignatureDtos == null){
                            for(int count = 0;count <mClassId.size();count++){
                                MyClassRecordDto lMyClassRecordDto = new MyClassRecordDto();
                                RegisteredRecordManager.getMyClassRecordDataCache().setMyClassRecordDtos(lMyClassRecordDto);
                            }
                            return;
                        }*/
                    }
                }).subscribe(new Subscriber<List<MyClassRecordDto>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(List<MyClassRecordDto> registeredRecordFromSignatureDtos) {

                        List<MyClassRecordDto> lDtos = RegisteredRecordManager.getMyClassRecordDataCache().getMyClassRecordDtos();
                        if (lDtos == null || registeredRecordFromSignatureDtos == null) {
                            return;
                        }
                        LogUtil.Log("lenita", " MyClassRecordSelectTabPresenter call lDtos.size = " + lDtos.size());
                        //TODO 整个列表全存储起来，后面的操作全部为更新
                        for (int i = 0; i < mClassId.size(); i++) {
                            for (int j = 0; j < registeredRecordFromSignatureDtos.size(); j++) {
                                int ClassID = Integer.valueOf(mClassId.get(i));
                                if (ClassID == registeredRecordFromSignatureDtos.get(j).getClassID()) {
                                    RegisteredRecordManager.getMyClassRecordDataCache().updateMyClassRecordDtos(ClassID, registeredRecordFromSignatureDtos.get(j), true, date, week);
                                }
                            }
                        }
                        if (getView() != null) {
                            getView().changeCacheStatus();
                        }
                    }
                }));
    }

}
