package com.gta.zssx.mobileOA.presenter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
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
import com.gta.zssx.mobileOA.view.NewScheduleView;
import com.gta.zssx.pub.exception.CustomException;
import com.gta.zssx.pub.util.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import rx.Subscriber;
import rx.Subscription;

/**
 * Created by lan.zheng on 2016/10/20.
 */
public class NewSchedulePresenter extends BasePresenter<NewScheduleView> {

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private String UserId;
    public Subscription mSubscribe;
    public Subscription mSubscribeDelete;        //删除
    public Subscription mSubscribeAddOrUpdate;  //新建或更新

    public NewSchedulePresenter() {
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
                            getView().getServerTime("",0,0,0,0,0);
                        }

                    }

                    @Override
                    public void onNext(ServerTimeDto serverTimeDto) {
                        if(isViewAttached()) {
                            getView().hideDialog();
                            String[] s = serverTimeDto.getDate().split(" ");
                            String time = makeTime(s[1]);
                            String date = s[0]+" "+time;
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
                            int hour = lCalendar.get(Calendar.HOUR_OF_DAY);
                            int min = lCalendar.get(Calendar.MINUTE);
//                            String nowDate = year + "-" + (month > 9 ? month:"0"+month) +"-"+(day > 9 ? day :"0"+day );
                            getView().getServerTime(date,year,month,day,hour,min);
                        }
                    }
                });
        mCompositeSubscription.add(mSubscribe);
    }

    /**
     * 更新或新建日程
     */
    public void scheduleAddOrUpdate(Schedule schedule){
        if (!isViewAttached()) {
            return;
        }
        getView().showLoadingDialog();
        schedule.setCreateBy(UserId);
        mSubscribeAddOrUpdate = OAMainModel.addOrUpdateSchedule( schedule)
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
                        getView().saveScheduleDataSuccess();
                    }
                });
        mCompositeSubscription.add(mSubscribeAddOrUpdate);
    }

    /**
    * 删除日程
     */
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
                        getView().deleteLocalSchedulesSuccess();
                    }
                });
        mCompositeSubscription.add(mSubscribeDelete);
    }

    public boolean compareDate(String start,String end){
        String[] startArray = start.split(" ");
        String[] endArray = end.split(" ");


        String[] startDate = startArray[0].split("-");
        String[] endDate =endArray[0].split("-");

        int startYear = Integer.parseInt(startDate[0]);
        int startMonth = Integer.parseInt(deleteZero(startDate[1]));
        int startDay = Integer.parseInt(deleteZero(startDate[2]));

        int endYear = Integer.parseInt(endDate[0]);
        int endMonth = Integer.parseInt(deleteZero(endDate[1]));
        int endDay = Integer.parseInt(deleteZero(endDate[2]));

        if (startYear < endYear) {
            return true;
        }else if (startYear == endYear) {

            if (startMonth < endMonth) {
                return true;
            }else if (startMonth == endMonth) {
                if (startDay < endDay) {
                    return true;
                }else if (startDay == endDay) {
                    return compareTime(startArray[1], endArray[1]);
                }
            }


        }
        return false;
    }

    boolean compareTime(String s,String e){

        String[] startArr = s.split(":");
        String[] endArr = e.split(":");

        int startHour = Integer.parseInt(deleteZero(startArr[0]));
        int startMin = Integer.parseInt(deleteZero(startArr[1]));

        int endHour = Integer.parseInt(deleteZero(endArr[0]));
        int endMin = Integer.parseInt(deleteZero(endArr[1]));

        if (startHour < endHour) {
            return true;
        }else if (startHour == endHour) {
            int d = endMin - startMin;
            if (d > 0 ) {
                return true;
            }
        }
        return false;
    }

    private String deleteZero(String t) {
        String s = null;
        if (!StringUtils.isEmpty(t)) {
            if (t.startsWith("0") && t.length() == 2) {
                s = t.substring(1, t.length());
            } else {
                s = t;
            }
        }
        return s;
    }

    private Dialog modifyDialog;
    public TextView modifyContent;
    private DeleteListener listener;
    public void popupConfirmDialog(Context context, String content, DeleteListener deleteListener) {
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

    public String makeTime(String time){
        String result="";
        String[] str=time.split(":");
        if (str.length==2 ) {
            result=time;
        }else if (str.length==3) {
            result=str[0]+":"+str[1];
        }
        return result;
    }

    public boolean checkRemindValidity(String start,String end,int remind){
        boolean b = false;

        if (remind == 1 || remind == 5) {
            b= true;//除了1和5不检查，其他都检查
        }else {

            String[] startArray = start.split(" ");
            String[] endArray = end.split(" ");
            String startDate = startArray[0];
            String endDate = endArray[0];
            if (startDate.equals(endDate)) {
                b = true;
            }
        }

        return b;
    }
}
