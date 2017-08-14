package com.gta.zssx.fun.coursedaily.registerrecord.presenter;

import android.content.Context;

import com.gta.utils.mvp.BasePresenter;
import com.gta.zssx.AppConfiguration;
import com.gta.zssx.R;
import com.gta.zssx.fun.coursedaily.registercourse.model.ClassDataManager;
import com.gta.zssx.fun.coursedaily.registerrecord.model.DTO.RegisteredRecordDto;
import com.gta.zssx.fun.coursedaily.registerrecord.model.DTO.ServerTimeDto;
import com.gta.zssx.fun.coursedaily.registerrecord.model.RegisteredRecordManager;
import com.gta.zssx.fun.coursedaily.registerrecord.view.AlreadyRegisteredRecordView;
import com.gta.zssx.fun.personalcenter.model.bean.UserBean;
import com.gta.zssx.pub.exception.CustomException;
import com.gta.zssx.pub.util.LogUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import rx.Subscriber;
import rx.Subscription;

/**
 * Created by lan.zheng on 2016/6/16.
 */
public class AlreadyRegisterRecordPresenter extends BasePresenter<AlreadyRegisteredRecordView> {
    public Subscription mSubscribe;
    public Subscription mSubscribeList;
    public String beginDate;
    public String endDate;
    public Context mContext;

    public AlreadyRegisterRecordPresenter(Context context){
        mContext = context;
    }
    /**
     * 获取服务器时间
     *
     * @param TeacherID
     */
    public void getServerTime(final String TeacherID) {
        if (!isViewAttached()) {
            return;
        }
        getView().showLoadingDialog();
        mSubscribe = RegisteredRecordManager.getServerTime()
                .subscribe(new Subscriber<ServerTimeDto>() {

                    @Override
                    public void onCompleted() {
                        if(isViewAttached()) {
                            getView().hideDialog();
                        }

                    }

                    @Override
                    public void onError(Throwable throwable) {
                        if(isViewAttached()) {
                            getView().hideDialog();
                            getView().notResult();
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
                        }

                    }

                    @Override
                    public void onNext(ServerTimeDto serverTimeDto) {
                        if(isViewAttached()) {
                            getView().setDate(serverTimeDto.getDate());
                            beginDate = "2017-05-01";
                            endDate  = "2017-05-31";
                            setBeginAndEndDate(serverTimeDto.getDate());
                            LogUtil.Log("lenita", "开始时间 = " + beginDate + " , 结束时间 = " + endDate);
                            //下一个网络请求需要先转换成时间段
                            getView().getRegisterData(beginDate,endDate);
                        }
                    }
                });
        mCompositeSubscription.add(mSubscribe);
    }

/*
    public void testData() {
        RegisteredRecordDto lRegisteredRecordDto = new RegisteredRecordDto();
        lRegisteredRecordDto.setTotal(1);
        List<RegisteredRecordDto.recordEntry> lRecordEntries = new ArrayList<>();
        RegisteredRecordDto.recordEntry lRecordEntry = new RegisteredRecordDto.recordEntry();
        lRecordEntry.setClassID(22);
        lRecordEntry.setSignDate("2016-06-10");
        lRecordEntry.setSection("第1,2节");
        lRecordEntry.setClassName("13会计1");
        lRecordEntries.add(lRecordEntry);
        lRecordEntries.add(lRecordEntry);
        lRecordEntries.add(lRecordEntry);
        lRecordEntries.add(lRecordEntry);
        lRecordEntries.add(lRecordEntry);
        lRecordEntries.add(lRecordEntry);
        lRegisteredRecordDto.setDetail(lRecordEntries);
        LogUtil.Log("lenita", "lRegisteredRecordDto" + lRegisteredRecordDto.getTotal());
        getView().showResultList(lRegisteredRecordDto);
    }
*/

    /**
     * 设置开始时间和结束时间
     *
     * @param date
     */
    public void setBeginAndEndDate(String date) {
        SimpleDateFormat lSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        try {
            long timeInMillis = lSimpleDateFormat.parse(date).getTime();
            Calendar lCalendar = Calendar.getInstance();
            lCalendar.setTimeInMillis(timeInMillis);
            int maxDayofMonth = lCalendar.getActualMaximum(Calendar.DATE);
            beginDate = lCalendar.get(Calendar.YEAR) + "-"
                    + ((lCalendar.get(Calendar.MONTH) + 1) > 9 ? (lCalendar.get(Calendar.MONTH) + 1) : "0" + (lCalendar.get(Calendar.MONTH) + 1)) + "-01";
            endDate = lCalendar.get(Calendar.YEAR) + "-"
                    + ((lCalendar.get(Calendar.MONTH) + 1) > 9 ? (lCalendar.get(Calendar.MONTH) + 1) : "0" + (lCalendar.get(Calendar.MONTH) + 1)) + "-" + maxDayofMonth;
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }


    /**
     * 获取记录
     *
     * @param teacherId
     * @param beginDate
     * @param endDate
     * @param pageCount
     */
    public void getRegisteredRecordData(String teacherId, String beginDate, String endDate, int pageCount,boolean changeDate,boolean isRefreshData) {
        if (!isViewAttached()) {
            return;
        }
        mSubscribeList = RegisteredRecordManager.getRegisterRecordList(teacherId, beginDate, endDate, pageCount)
                .subscribe(new Subscriber<RegisteredRecordDto>() {

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        if(!isViewAttached()) {
                             return;
                        }

                        if(isRefreshData){
                            //下拉刷新
                            getView().onRefreshError();
                        }else {
                            //上拉加载更多
                            getView().onErrorHandle(e);
                            String msg = "";
                            if(changeDate){
                                msg = "change_date";  //日期转换的时候
                            }else {
                                if (e instanceof CustomException) {   //抛出自定义的异常
                                    CustomException lE = (CustomException) e;
                                    if (lE.getCode() == CustomException.NETWORK_UNAVAILABLE) {
                                        msg = "not_network";  //非转换日期没网络
                                    }else {
                                        msg = "other_status";  //非转换日期有网络
                                    }
                                }
                            }
                            getView().setRefreshFalseandDealWithView(true,msg);    //没有数据的各种情况返回
                        }

                    }

                    @Override
                    public void onNext(RegisteredRecordDto registeredRecordDto) {
                        if(!isViewAttached()) {
                            return;
                        }

                        LogUtil.Log("lenita", "getRegisteredRecordData " + registeredRecordDto.getDetail().size());
                        if(isRefreshData) {
                            //TODO 下拉刷新
                            getView().showResultList(registeredRecordDto,isRefreshData);
                        }else {
                            //TODO 上拉加载更多
                            if(registeredRecordDto.getDetail().size() > 0){
                                getView().showResultList(registeredRecordDto,isRefreshData);
                                getView().setRefreshFalseandDealWithView(false,"");  //正常的情况
                            }else {
                                getView().setRefreshFalseandDealWithView(true,mContext.getResources().getString(R.string.text_not_more_result));    //没有数据的各种情况返回
                            }

                        }
                    }
                });
        mCompositeSubscription.add(mSubscribeList);
    }


    private UserBean mUserBean;
    public Subscription mSubscribeListClass;
    public void getTeacherClassUpdate(String TeacherID){
        if (!isViewAttached()) {
            return;
        }

        mSubscribeListClass = ClassDataManager.getTeacherClassUpdate(TeacherID)
                .subscribe(new Subscriber<UserBean>() {

                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        if(isViewAttached()){
//                            getView().onErrorHandle(e);
                            getView().getTeacherClassInfoReturn(false);
                        }

                    }

                    @Override
                    public void onNext(UserBean userBean) {
                        if(userBean == null){
                            return;
                        }
                        try {
                            mUserBean = AppConfiguration.getInstance().getUserBean();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        boolean IsTeacherName = userBean.isTeacherName();
                        int TotalCount = userBean.getTotalCount();
                        List<UserBean.ClassList> lClassList = new ArrayList<>();
                        if(userBean.getClassName() != null){
                            lClassList = userBean.getClassName();
                        }
                        mUserBean.setTeacherName(IsTeacherName);  //更新老师状态
                        mUserBean.setTotalCount(TotalCount);
                        mUserBean.setClassName(lClassList);
                        AppConfiguration.getInstance().setUserLoginBean(mUserBean).saveAppConfiguration();
                        if(isViewAttached()){
                            getView().getTeacherClassInfoReturn(true);
                        }

                    }
                });

        mCompositeSubscription.add(mSubscribeListClass);
    }

}
