package com.gta.zssx.mobileOA.presenter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.gta.utils.mvp.BasePresenter;
import com.gta.zssx.R;
import com.gta.zssx.ZSSXApplication;
import com.gta.zssx.fun.coursedaily.registerrecord.model.DTO.ServerTimeDto;
import com.gta.zssx.fun.coursedaily.registerrecord.model.RegisteredRecordManager;
import com.gta.zssx.mobileOA.model.OAMainModel;
import com.gta.zssx.mobileOA.model.bean.Schedule;
import com.gta.zssx.mobileOA.view.SchedulePlanView;
import com.gta.zssx.pub.exception.CustomException;
import com.gta.zssx.pub.util.LogUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import rx.Subscriber;
import rx.Subscription;

/**
 */
public class SchedulePlanPresenter extends BasePresenter<SchedulePlanView> {
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    public Subscription mSubscribe;
    public Subscription mSubscribeSchedule;
    public Subscription mSubscribeDelete;        //删除
    private String UserId;

    public SchedulePlanPresenter() {
        UserId = ZSSXApplication.instance.getUser().getUserId();
    }

    /**
     * 获取服务器时间
     */
    public void getServerTime() {
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
                            getView().getServerDate("",0,0,0);
                        }

                    }

                    @Override
                    public void onNext(ServerTimeDto serverTimeDto) {
                        if(isViewAttached()) {
                            getView().hideDialog();
                            String date = serverTimeDto.getDate();
                            Calendar lCalendar = Calendar.getInstance();
                            try {
                                long timeInMillis = simpleDateFormat.parse(date).getTime();
                                lCalendar.setTimeInMillis(timeInMillis);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            int year = lCalendar.get(Calendar.YEAR);
                            int month = lCalendar.get(Calendar.MONTH) + 1;
                            int day = lCalendar.get(Calendar.DAY_OF_MONTH);
                            String nowDate = year + "-" + (month > 9 ? month:"0"+month) +"-"+(day > 9 ? day :"0"+day );
                            getView().getServerDate(nowDate,year,month,day);
                        }
                    }
                });
        mCompositeSubscription.add(mSubscribe);
    }

    /**
     * 获取当月日程列表
     */
    public void getScheduleListData(String beginDate,String endDate){
        if (!isViewAttached()) {
            return;
        }
        getView().showLoadingDialog();
        mSubscribeSchedule = OAMainModel.getCalendarList(UserId,beginDate,endDate)
                .subscribe(new Subscriber<List<Schedule>>() {

                    @Override
                    public void onCompleted() {
                        if(isViewAttached()) {
                            getView().hideDialog();
                        }

                    }

                    @Override
                    public void onError(Throwable throwable) {
                        if(!isViewAttached()) {
                           return;
                        }
                        LogUtil.Log("lenita",throwable.toString());
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
                        List<Schedule> schedules = new ArrayList<Schedule>();
                        getView().getScheduleList(schedules);
                    }

                    @Override
                    public void onNext(List<Schedule> scheduleList) {
                        if(!isViewAttached()) {
                            return;
                        }
                        getView().hideDialog();
                        getView().getScheduleList(scheduleList);
                    }
                });
        mCompositeSubscription.add(mSubscribeSchedule);
    }

    /**
     *  日程状态变化
     */
    public void changeScheduleStatus(int id,int status){
        if (!isViewAttached()) {
            return;
        }
        getView().showLoadingDialog();
        mSubscribeSchedule = OAMainModel.changeScheduleStatus(id,status)
                .subscribe(new Subscriber<Object>() {

                    @Override
                    public void onCompleted() {
                        if(isViewAttached()) {
                            getView().hideDialog();
                        }

                    }

                    @Override
                    public void onError(Throwable throwable) {
                        if(!isViewAttached()) {
                            return;
                        }
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
                    }

                    @Override
                    public void onNext(Object s) {
                        if(!isViewAttached()) {
                            return;
                        }
                        getView().hideDialog();
                        getView().updateLocalSchedules(id,status);
                    }
                });
        mCompositeSubscription.add(mSubscribeSchedule);
    }

    //删除日程
    public void deleteSchedule(int id){
        if (!isViewAttached()) {
            return;
        }
        getView().showLoadingDialog();
        mSubscribeDelete = OAMainModel.deleteSchedule(id)
                .subscribe(new Subscriber<Object>() {

                    @Override
                    public void onCompleted() {
                        if(isViewAttached()) {
                            getView().hideDialog();
                        }

                    }

                    @Override
                    public void onError(Throwable throwable) {
                        if(!isViewAttached()) {
                            return;
                        }
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
                        getView().hideDialog();
                    }

                    @Override
                    public void onNext(Object s) {
                        if(!isViewAttached()) {
                            return;
                        }
                        getView().hideDialog();
                        getView().deleteLocalSchedules(id);
                    }
                });
        mCompositeSubscription.add(mSubscribeDelete);
    }

    /**
    *获取单条日程内容
     */
    public void getSingleScheduleById(int id){
        if (!isViewAttached()) {
            return;
        }
        getView().showLoadingDialog();
        mSubscribeSchedule = OAMainModel.getScheduleDetail(id)
                .subscribe(new Subscriber<Schedule>() {

                    @Override
                    public void onCompleted() {
                        if(isViewAttached()) {
                            getView().hideDialog();
                        }

                    }

                    @Override
                    public void onError(Throwable throwable) {
                        if(!isViewAttached()) {
                            return;
                        }
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
                    }

                    @Override
                    public void onNext(Schedule schedule) {
                        if(!isViewAttached()) {
                            return;
                        }
                        getView().hideDialog();
                        getView().getSingleSchedulesInfo(true,id,schedule);
                    }
                });
        mCompositeSubscription.add(mSubscribeSchedule);
    }

    /**
     * 根据年月日获取日，且不是"01"格式
     *
     * @param date
     */
    public int noZero(String date) {
        if (date.startsWith("0") && date.length() >= 2) {
            date = date.substring(1, date.length());
        }

        return Integer.parseInt(date);
    }

    public int parseInt(String value){
        return Integer.parseInt(value);
    }

    /**
     * 获取月份起始日期
     * @param date
     * @return
     * @throws ParseException
     */
    public String getMonthStartDay(String date) {
        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(simpleDateFormat.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
        String monthStart = simpleDateFormat.format(calendar.getTime());

        return monthStart.split("-")[2];
    }

    /**
     * 获取月份最后日期
     * @param date
     * @return
     * @throws ParseException
     */
    public String getMonthEndDay(String date){
        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(simpleDateFormat.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        String monthEnd = simpleDateFormat.format(calendar.getTime());
        return  monthEnd.split("-")[2];
    }

    /**
     * 分离服务器获得的日期和时间
     * @param string
     * @return
     */
    public int parseDate(String string){
        string = string.substring(0, string.indexOf(" "));
        String[] array = string.trim().split("-");

        return noZero(array[2]);
    }

    /**
     * 分离已完成的日程
     * @param all 总日程
     * @return
     */
    public ArrayList<Schedule> divideFinished(List<Schedule> all){
        ArrayList<Schedule> schedules = new ArrayList<Schedule>();
        for (int i = 0; i < all.size(); i++) {
            if (all.get(i).getStatus() == 1) {
                // 已完成
                schedules.add(all.get(i));
            }
        }
        return schedules;
    }
    /**
     * 分离未完成的日程
     * @param all 总日程
     * @return
     */
    public ArrayList<Schedule> divideUnfinished(List<Schedule> all){
        ArrayList<Schedule> schedules = new ArrayList<Schedule>();
        for (int i = 0; i < all.size(); i++) {
            if (all.get(i).getStatus() == 0) {
                // 未完成
                schedules.add(all.get(i));
            }
        }
        return schedules;
    }

    int year_c = 0;
    int month_c = 0;
    public List<Schedule> clone(Schedule item,String currentTime,int year,int month) {
        year_c = year;
        month_c = month;
        String[] startTimeArray = item.getStartTime().split(" ")[0].split("-");
        String[] endTimeArray = item.getEndTime().split(" ")[0].split("-");
        String[] currentTimeArray = currentTime.split("-");

        int startY = parseInt(startTimeArray[0]);
        int startM = noZero(startTimeArray[1]);
        int startD = noZero(startTimeArray[2]);

        int endY = parseInt(endTimeArray[0]);
        int endM = noZero(endTimeArray[1]);
        int endD = noZero(endTimeArray[2]);

        int currentY = parseInt(currentTimeArray[0]);
        int currentM = noZero(currentTimeArray[1]);

        //月初月末日子的值
        String m_s = getMonthStartDay(currentTime);
        String m_e = getMonthEndDay(currentTime);
        int monthStart = noZero(m_s);
        int monthEnd = noZero(m_e);


        if (currentY < endY) {

            if (startY < currentY) {
                //整个月
                return all_the_month(item, monthStart, monthEnd);
            } else if (startY == currentY) {
                //比较月份
                if (startM < currentM) {
                    //整个月
                    return all_the_month(item, monthStart, monthEnd);
                } else if (startM == currentM) {
                    //startD到月末
                    return startD_end_of_month(item, startD, monthEnd);
                }

            }

        } else if (currentY == endY) {

            if (startY < currentY) {
                //比较月
                if (currentM < endM) {
                    //整个月
                    return all_the_month(item, monthStart, monthEnd);
                } else if (currentM == endM) {
                    //月初到endD
                    return beginning_of_month_endD(item, monthStart, endD);
                }
            } else if (startY == currentY) {
                //比较月
                if (startM == currentM && currentM == endM) {
                    //startD到endD
                    return startD_endD(item, startD, endD);
                } else if (startM < currentM && currentM < endM) {
                    //整个月
                    return all_the_month(item, monthStart, monthEnd);
                } else if (startM < currentM && currentM == endM) {
                    //月初到endD
                    return beginning_of_month_endD(item, monthStart, endD);
                } else if (startM == currentM && currentM < endM) {
                    //startD到月末
                    return startD_end_of_month(item, startD, monthEnd);
                }


            }
        }
        return new ArrayList<Schedule>();
    }

    /**
     * 月初到endD
     * @return
     */
    List<Schedule> beginning_of_month_endD(Schedule item,int monthStart,int endD){
        //月初到endDay
        return doClone(item, monthStart, endD);
    }
    /**
     * startD到月末
     * @return
     */
    List<Schedule> startD_end_of_month(Schedule item,int startD,int monthEnd){
        //startDay到月末
        return doClone(item, startD, monthEnd);
    }
    /**
     * startD到endD
     * @return
     */
    List<Schedule> startD_endD(Schedule item,int startD,int endD){
        //startDay 到endDay
        return doClone(item, startD, endD);
    }
    /**
     * 整个月
     * @return
     */
    List<Schedule> all_the_month(Schedule item,int monthStart,int monthEnd){
        //整个currentM
        return doClone(item,monthStart,monthEnd);
    }

    public List<Schedule> doClone(Schedule item,int start,int end){
        List<Schedule> small = new ArrayList<Schedule>();

        for (int i = start; i <= end; i++) {

            String oldStartTime = item.getStartTime().split(" ")[1];
            String oldEndTime = item.getEndTime().split(" ")[1];
            String newStartTime = year_c+"-"+month_c+"-"+i+" "+oldStartTime;
            String newEndTime = year_c+"-"+month_c+"-"+i+" "+oldEndTime;

            Schedule schedule = new Schedule();
            schedule.setHasNoti(item.isHasNoti());
            schedule.setId(item.getId());
            schedule.setIsExternal(item.isIsExternal());
            schedule.setRemind(item.getRemind());
            schedule.setScheduleContent(item.getScheduleContent());
            schedule.setScheduleType(item.getScheduleType());
            schedule.setStatus(item.getStatus());

            schedule.setStartTime(newStartTime);
            schedule.setEndTime(newEndTime);

            small.add(schedule);
        }

        Log.i("wangbin", small.toString());
        return small;
    }


    private Dialog modifyDialog;
    public TextView modifyContent;
    private DeleteListener listener;
    public void popupConfirmDialog(Context context,String content,DeleteListener deleteListener) {
        modifyDialog = null; //置空然后重新赋值
        listener = deleteListener;
        backgroundAlpha(0.8f,(Activity)context);
        if (modifyDialog == null) {
            View contentView = LayoutInflater.from(context).inflate(R.layout.dialog_delete_comfirn, null);
            modifyContent = (TextView) contentView.findViewById(R.id.dialog_content_text);
            modifyContent.setText(content);
            View btnOK = contentView.findViewById(R.id.dialog_btn_confirm);
            View btnBack = contentView.findViewById(R.id.dialog_btn_cancel);
            btnOK.setOnClickListener(v -> {
                modifyDialog.dismiss();
                backgroundAlpha(1f,(Activity)context);
                listener.sureDelete();
            });
            btnBack.setOnClickListener(v -> {
                modifyDialog.dismiss();
                backgroundAlpha(1f,(Activity)context);
            });
            modifyDialog = new Dialog(context, R.style.myDialogTheme);  //使用自定义的样式
            modifyDialog.setCanceledOnTouchOutside(false);  //外围点击不消失
            modifyDialog.setContentView(contentView);
        }
        modifyDialog.show();
    }

    private void backgroundAlpha(float b,Activity activity) {
        WindowManager.LayoutParams layoutParams = activity.getWindow().getAttributes();
        layoutParams.alpha = b;
        activity.getWindow().setAttributes(layoutParams);
    }

    public interface DeleteListener{
        void sureDelete();
    }

    /*public void startSetData() {
        String year = calendarAdapter.getShowYear();
        String month = calendarAdapter.getShowMonth();

        if (month.startsWith("0") && month.length() == 2) {
            month = month.substring(1, month.length());
        }
        setData(Integer.parseInt(year), Integer.parseInt(month));
    }*/

    //TODO 十一月的日历测试数据
    public List<Schedule> testData(){
        List<Schedule> list = new ArrayList<>();
        Schedule schedule1 = new Schedule();
        Schedule schedule2 = new Schedule();
        schedule1.setId(9);
        schedule1.setScheduleContent("十一gshshsh");
        schedule1.setScheduleType(1);
        schedule1.setRemind(3);
        schedule1.setStartTime("2016-11-06T15:06:00");
        schedule1.setEndTime("2016-11-06T15:16:00");
        schedule1.setStatus(1);
        schedule1.setIsExternal(false);
        //======================
        schedule2.setId(10);
        schedule2.setScheduleContent("十一月dsfsdduhsfsh");
        schedule2.setScheduleType(1);
        schedule2.setRemind(1);
        schedule2.setStartTime("2016-11-07T15:16:00");
        schedule2.setEndTime("2016-11-10T15:26:00");
        schedule2.setStatus(0);
        schedule2.setIsExternal(false);
        //======================
        Schedule schedule3 = new Schedule();
        Schedule schedule4 = new Schedule();
        schedule3.setId(11);
        schedule3.setScheduleContent("发放的");
        schedule3.setScheduleType(1);
        schedule3.setRemind(5);
        schedule3.setStartTime("2016-11-07T15:16:00");
        schedule3.setEndTime("2016-11-07T15:26:00");
        schedule3.setStatus(0);
        schedule3.setIsExternal(false);
        //=====================
        schedule4.setId(12);
        schedule4.setScheduleContent("安分点");
        schedule4.setScheduleType(1);
        schedule4.setRemind(3);
        schedule4.setStartTime("2016-11-09T15:16:00");
        schedule4.setEndTime("2016-11-09T15:26:00");
        schedule4.setStatus(1);
        schedule4.setIsExternal(false);
        //========================
        Schedule schedule5 = new Schedule();
        schedule5.setId(9);
        schedule5.setScheduleContent("十一月saawrh");
        schedule5.setScheduleType(1);
        schedule5.setRemind(3);
        schedule5.setStartTime("2016-11-29T15:06:00");
        schedule5.setEndTime("2016-11-29T15:16:00");
        schedule5.setStatus(0);
        schedule5.setIsExternal(false);
        list.add(schedule1);
        list.add(schedule2);
        list.add(schedule3);  //每日提醒
        list.add(schedule4);  //每周提醒
        list.add(schedule5);   //每月提醒
        return list;
    }

    public List<String> getDateData(int year,int month){
        List<String> list = new ArrayList<>();
        String temp = "-01 00:00:00";
        String beginDate = null;
        String endDate = null;
        if (month == 12) {
            beginDate = year + "-" + "12" + temp;
            endDate = (year + 1) + "-" + "01" + temp;
        } else {
            beginDate = year + "-" + (month < 10 ? ("0" + month) : month)+ temp;
            int nextMonth = month + 1;
            endDate = year + "-"+ (nextMonth < 10 ? ("0" + nextMonth) : nextMonth) + temp;
        }
        list.add(beginDate);
        list.add(endDate);
        return list;
    }

}
