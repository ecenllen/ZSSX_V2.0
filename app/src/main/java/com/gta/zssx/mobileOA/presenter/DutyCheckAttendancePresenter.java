package com.gta.zssx.mobileOA.presenter;

import com.gta.utils.mvp.BasePresenter;
import com.gta.zssx.ZSSXApplication;
import com.gta.zssx.fun.coursedaily.registerrecord.model.DTO.ServerTimeDto;
import com.gta.zssx.fun.coursedaily.registerrecord.model.RegisteredRecordManager;
import com.gta.zssx.mobileOA.model.OAMainModel;
import com.gta.zssx.mobileOA.model.bean.DutyCheckAttendanceInfo;
import com.gta.zssx.mobileOA.model.bean.DutyRegisterOrCheckInfo;
import com.gta.zssx.mobileOA.model.bean.DutyTimeInfo;
import com.gta.zssx.mobileOA.model.bean.UserDutyListInfo;
import com.gta.zssx.mobileOA.view.DutyCheckAttendanceView;
import com.gta.zssx.pub.common.Constant;
import com.gta.zssx.pub.exception.CustomException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import rx.Subscriber;
import rx.Subscription;

/**
 * Created by lan.zheng on 2016/11/21.  公文公告
 */
public class DutyCheckAttendancePresenter extends BasePresenter<DutyCheckAttendanceView> {
    private String UserId;
    public Subscription mSubscribe;
    public Subscription mSubscribeCheckTime;   //获取时间段
    public Subscription mSubscribeCheckInfo;   //获取值班考勤
    public Subscription mSubscribeCheckDetail; //获取值班登记信息（TimeId = 0时去获取当天所有）
    public boolean mIsFutureDate = false;  //是否是未来时间
    private List<DutyCheckAttendanceInfo.CheckAttendanceEntity> mCheckAttendanceList = new ArrayList<>();

    public DutyCheckAttendancePresenter() {
        UserId = ZSSXApplication.instance.getUser().getUserId();
    }

    public void getUserDutyList(){
        if (!isViewAttached()) {
            return;
        }
        getView().showLoadingDialog();
        mSubscribe = OAMainModel.getUserDutyList(UserId,0,Constant.LOAD_DATA_SIZE)
                .subscribe(new Subscriber<UserDutyListInfo>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable throwable) {
                        if (isViewAttached()) {
                            getView().hideDialog();
                            if (throwable instanceof CustomException) {   //抛出自定义的异常
                                CustomException lE = (CustomException) throwable;
                                if (lE.getCode() == CustomException.NETWORK_UNAVAILABLE) {
                                    getView().showError(throwable.getMessage());
                                } else {
                                    getView().onErrorHandle(throwable);  //抛出默认异常
                                }
                            } else {
                                getView().onErrorHandle(throwable);  //抛出默认异常
                            }
                        }
                    }

                    @Override
                    public void onNext(UserDutyListInfo officeNoticeInfo) {

                    }
                });
        mCompositeSubscription.add(mSubscribe);
    }

    /**
     * 获取时间段列表--值班检查
     */
    public void getCheckTimeListData() {
        if (!isViewAttached()) {
            return;
        }
        getView().showLoadingDialog();
        mSubscribeCheckTime = RegisteredRecordManager.getServerTime()
                .subscribe(new Subscriber<ServerTimeDto>() {

                    @Override
                    public void onCompleted() {
                        if(isViewAttached()) {
                        }

                    }

                    @Override
                    public void onError(Throwable throwable) {
                        if(isViewAttached()) {
                            getView().hideDialog();
                            if (throwable instanceof CustomException) {   //抛出自定义的异常
                                CustomException lE = (CustomException) throwable;
                                if (lE.getCode() == CustomException.NETWORK_UNAVAILABLE) {
                                    getView().showError(throwable.getMessage());
                                }else {
                                    getView().onErrorHandle(throwable);  //抛出默认异常
                                }
                            }else {
                                getView().onErrorHandle(throwable);  //抛出默认异常
                            }
//                            getView().getServerDate("",0,0,0);
                        }

                    }

                    @Override
                    public void onNext(ServerTimeDto serverTimeDto) {
                        if(isViewAttached()) {
                            getView().hideDialog();
//                            getView().getServerDate(nowDate,year,month,day);
                        }
                    }
                });
        mCompositeSubscription.add(mSubscribeCheckTime);  //时间段
    }

    /**
     * 拿到所有的时间下的考勤
     */
    public void getCheckAttendanceListData() {
        if (!isViewAttached()) {
            return;
        }
        getView().showLoadingDialog();
        mSubscribeCheckInfo = RegisteredRecordManager.getServerTime()
                .subscribe(new Subscriber<ServerTimeDto>() {

                    @Override
                    public void onCompleted() {
                        if(isViewAttached()) {
                        }

                    }

                    @Override
                    public void onError(Throwable throwable) {
                        if(isViewAttached()) {
                            getView().hideDialog();
                            if (throwable instanceof CustomException) {   //抛出自定义的异常
                                CustomException lE = (CustomException) throwable;
                                if (lE.getCode() == CustomException.NETWORK_UNAVAILABLE) {
                                    getView().showError(throwable.getMessage());
                                }else {
                                    getView().onErrorHandle(throwable);  //抛出默认异常
                                }
                            }else {
                                getView().onErrorHandle(throwable);  //抛出默认异常
                            }
//                            getView().getServerDate("",0,0,0);
                        }

                    }

                    @Override
                    public void onNext(ServerTimeDto serverTimeDto) {
                        if(isViewAttached()) {
                            getView().hideDialog();
//                            getView().getServerDate(nowDate,year,month,day);
                        }
                    }
                });
        mCompositeSubscription.add(mSubscribeCheckInfo);  //考勤信息
    }

    /**
     * 拿到所有的时间下的详细内容
     */
    public void getCheckDetailListData() {
        if (!isViewAttached()) {
            return;
        }
        getView().showLoadingDialog();
        mSubscribeCheckTime = RegisteredRecordManager.getServerTime()
                .subscribe(new Subscriber<ServerTimeDto>() {

                    @Override
                    public void onCompleted() {
                        if(isViewAttached()) {
                        }

                    }

                    @Override
                    public void onError(Throwable throwable) {
                        if(isViewAttached()) {
                            getView().hideDialog();
                            if (throwable instanceof CustomException) {   //抛出自定义的异常
                                CustomException lE = (CustomException) throwable;
                                if (lE.getCode() == CustomException.NETWORK_UNAVAILABLE) {
                                    getView().showError(throwable.getMessage());
                                }else {
                                    getView().onErrorHandle(throwable);  //抛出默认异常
                                }
                            }else {
                                getView().onErrorHandle(throwable);  //抛出默认异常
                            }
//                            getView().getServerDate("",0,0,0);
                        }

                    }

                    @Override
                    public void onNext(ServerTimeDto serverTimeDto) {
                        if(isViewAttached()) {
                            getView().hideDialog();
//                            getView().getServerDate(nowDate,year,month,day);
                        }
                    }
                });
        mCompositeSubscription.add(mSubscribeCheckDetail);  //登记信息
    }


    public void setCheckListInfo(int indicateTimePeriod,int line_1,int line_2,int line_3,int line_4){
        //TODO 根据indicateTimePeriod设置到对应该时段的值
        mCheckAttendanceList.get(indicateTimePeriod).setAttendance(line_1);
        mCheckAttendanceList.get(indicateTimePeriod).setTour(line_2);
        mCheckAttendanceList.get(indicateTimePeriod).setTourGradee(line_3);
        mCheckAttendanceList.get(indicateTimePeriod).setTotalGrade(line_4);
    }

    public List<Integer> getCheckListInfo(int indicateTimePeriod){
        //TODO 根据indicateTimePeriod调取到对应该时段的值
        List<Integer> list = new ArrayList<>();
        list.add(mCheckAttendanceList.get(indicateTimePeriod).getAttendance());
        list.add(mCheckAttendanceList.get(indicateTimePeriod).getTour());
        list.add(mCheckAttendanceList.get(indicateTimePeriod).getTourGrade());
        list.add(mCheckAttendanceList.get(indicateTimePeriod).getTotalGrade());
        return list;
    }

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
    //日期对比，是否是未值班
    private boolean compareDate(String serverDate, String selectDate){
        boolean isFutureDuty = false;
        try {
            Date date1 = simpleDateFormat.parse(serverDate) ;
            Date date2 = simpleDateFormat.parse(selectDate) ;
            if(date1.getTime() < date2.getTime()){
                isFutureDuty = true;  //未来时间，是未值班
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return isFutureDuty;
    }

    public String getDate(String date){
        String[] s1 = date.split(" ");
        String date1 =  s1[0];
        String[] s2 = date1.split("-");
        return s2[0]+"年"+s2[1]+"月"+s2[2]+"日";
    }

    public DutyRegisterOrCheckInfo testDataTime(){
        DutyRegisterOrCheckInfo dutyRegisterOrCheckInfo = new DutyRegisterOrCheckInfo();
        List<Object> list = new ArrayList<>();
        DutyTimeInfo timeEntity1 = new DutyTimeInfo();
        DutyTimeInfo timeEntity2 = new DutyTimeInfo();
        DutyTimeInfo timeEntity3 = new DutyTimeInfo();
        DutyTimeInfo timeEntity4 = new DutyTimeInfo();
        timeEntity1.setTimesId(1);
        timeEntity2.setTimesId(2);
        timeEntity3.setTimesId(3);
        timeEntity4.setTimesId(4);
        timeEntity1.setTime("7:40 -- 8:15");
        timeEntity2.setTime("12：00 -- 12:20");
        timeEntity3.setTime("13；50 -- 14;25");
        timeEntity4.setTime("一、二、四 (16:55 -- 17:15)");
        list.add(timeEntity1);
        list.add(timeEntity2);
        list.add(timeEntity3);
        list.add(timeEntity4);
        dutyRegisterOrCheckInfo.setStatus(1);
        dutyRegisterOrCheckInfo.setServiceDate("2016-11-08 00:00:00");
        dutyRegisterOrCheckInfo.setDate("2016-11-10 00:00:00");  //check
        dutyRegisterOrCheckInfo.setTimeList(list);
//        mIsFutureDate = compareDate(dutyRegisterOrCheckInfo.getServiceDate(),dutyRegisterOrCheckInfo.getDate());
        checkAttendance();//TODO 顺便初始考勤测试
        return dutyRegisterOrCheckInfo;
    }

    private void checkAttendance(){
        List<DutyCheckAttendanceInfo.CheckAttendanceEntity> list1 = new ArrayList<>();
        DutyCheckAttendanceInfo.CheckAttendanceEntity entity1 = new DutyCheckAttendanceInfo.CheckAttendanceEntity();
        DutyCheckAttendanceInfo.CheckAttendanceEntity entity2 = new DutyCheckAttendanceInfo.CheckAttendanceEntity();
        DutyCheckAttendanceInfo.CheckAttendanceEntity entity3 = new DutyCheckAttendanceInfo.CheckAttendanceEntity();
        DutyCheckAttendanceInfo.CheckAttendanceEntity entity4 = new DutyCheckAttendanceInfo.CheckAttendanceEntity();
        entity1.setTimesId(1);
        entity1.setAttendance(1);
        entity1.setTour(1);
        entity1.setTourGradee(1);
        entity1.setTotalGrade(1);

        entity2.setTimesId(2);
        entity2.setAttendance(1);
        entity2.setTour(1);
        entity2.setTourGradee(1);
        entity2.setTotalGrade(1);

        entity3.setTimesId(3);
        entity3.setAttendance(1);
        entity3.setTour(1);
        entity3.setTourGradee(1);
        entity3.setTotalGrade(1);

        entity4.setTimesId(4);
        entity4.setAttendance(1);
        entity4.setTour(0);
        entity4.setTourGradee(1);
        entity4.setTotalGrade(1);
        list1.add(entity1);
        list1.add(entity2);
        list1.add(entity3);
        list1.add(entity4);
        DutyCheckAttendanceInfo info = new DutyCheckAttendanceInfo();
        info.setStatus(0);  //未检查
        info.setCheckList(list1);
        mCheckAttendanceList = info.getCheckList();
    }

}
