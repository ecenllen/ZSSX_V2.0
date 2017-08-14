package com.gta.zssx.mobileOA.presenter;

import android.text.TextUtils;

import com.gta.utils.mvp.BasePresenter;
import com.gta.zssx.ZSSXApplication;
import com.gta.zssx.mobileOA.model.OAMainModel;
import com.gta.zssx.mobileOA.model.bean.DutyTimeInfo;
import com.gta.zssx.mobileOA.model.bean.DutyTimeListInfo;
import com.gta.zssx.mobileOA.view.DutyRegisterOrCheckView;
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
 * Created by lan.zheng on 2016/11/14.
 */
public class DutyRegisterOrCheckPresenter extends BasePresenter<DutyRegisterOrCheckView> {
    public Subscription mSubscribe;
    private String UserId = ZSSXApplication.instance.getUser().getUserId();
//    public boolean mIsFutureDate = false;  //是否是未来时间


    public DutyRegisterOrCheckPresenter() {
        UserId = ZSSXApplication.instance.getUser().getUserId();
    }

    /**
     * 获取时间段列表--值班登记
     */
    public void getRegisterTimeListData(int DutyDetailId,String Date) {
        if (!isViewAttached()) {
            return;
        }
        getView().showLoadingDialog();
        mSubscribe = OAMainModel.getDutyDateTimeListData(UserId,DutyDetailId,Date)
                .subscribe(new Subscriber<DutyTimeListInfo>() {

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
                        if(TextUtils.isEmpty(Date) || Date == null){
                            getView().showAllEmptyView();  //获取最近时间失败整个页面空
                        }else {
                            getView().showListEmptyView();  //获取有日期的数据显示列表为空
                        }

                    }

                    @Override
                    public void onNext(DutyTimeListInfo dutyTimeListInfo) {
                        if(!isViewAttached()) {
                            return;
                        }

                        if(dutyTimeListInfo.getTimeList().size() == 0){
                            getView().showListEmptyView();
                        }else {
                            //返回时间数据和是否是未来标识
                            String serverDate = dutyTimeListInfo.getServiceDate();
                            String showDate = dutyTimeListInfo.getDate();
                            boolean mIsFutureDate = compareDate(serverDate,showDate);
                            getView().getRegisterTimePeriodSuccess(dutyTimeListInfo,mIsFutureDate);
                        }

                    }
                });
        mCompositeSubscription.add(mSubscribe);
    }

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
    //日期对比，是否是未值班
    public boolean compareDate(String serverDate, String selectDate){
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

    public String getFormatDate(String date){
        String[] s1 = date.split(" ");
        String date1 =  s1[0];
        String[] s2 = date1.split("-");
        return s2[0]+"年"+s2[1]+"月"+s2[2]+"日";
    }


    public DutyTimeListInfo testDataTime(){
        DutyTimeListInfo dutyRegisterOrCheckInfo = new DutyTimeListInfo();
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
        timeEntity2.setTime("12:00 -- 12:20");
        timeEntity3.setTime("13:50 -- 14:25");
        timeEntity4.setTime("一、二、四 (16:55 -- 17:15)");
        timeEntity1.setStatus(0);  //未登记
        timeEntity2.setStatus(1);
        timeEntity3.setStatus(0);
        timeEntity4.setStatus(1);
        list.add(timeEntity1);
        list.add(timeEntity2);
        list.add(timeEntity3);
        list.add(timeEntity4);
        dutyRegisterOrCheckInfo.setStatus(0);  //0.未提交,1.已提交
        dutyRegisterOrCheckInfo.setServiceDate("2016-11-24 00:00:00");
        dutyRegisterOrCheckInfo.setDate("2016-11-24 00:00:00");  //check
        dutyRegisterOrCheckInfo.setTimeList(list);
        return dutyRegisterOrCheckInfo;
    }

        /*private void setAdapterData(int line){
        switch (line){
            case CHECK_LINE_ONE:
                List<String> textList1 = new ArrayList<>();
                textList1.add("准到");
                textList1.add("迟到");
                textList1.add("中途离校");
                textList1.add("缺席");
                mCheck1TagAdapter = new TagAdapter<String>(textList1) {
                    @Override
                    public View getView(FlowLayout parent, int position, String s) {
                        TextView lTextView = (TextView) LayoutInflater.from(DutyRegisterOrCheckActivity.this).inflate(R.layout.list_item_duty_check_btn, mCheck1FlowLayout, false);
                        ViewGroup.MarginLayoutParams paramTest = (ViewGroup.MarginLayoutParams) lTextView.getLayoutParams();
                        paramTest.setMargins(0, 0, 12, 0);
                        lTextView.requestLayout();
                        int res = 0;
                        if(isCanCheck) {
                            switch (position){
                                case 0:
                                    res = R.drawable.radio_button_duty_btn1;
                                    break;
                                case 1:
                                    res = R.drawable.radio_button_duty_btn2;
                                    break;
                                case 2:
                                    res = R.drawable.radio_button_duty_btn3;
                                    break;
                                case 3:
                                    res = R.drawable.radio_button_duty_btn4;
                                    break;
                            }
                        }else {
                            switch (position){
                                case 0:
                                    res = R.drawable.ic_duty_btn1;
                                    break;
                                case 1:
                                    res = R.drawable.ic_duty_btn2;
                                    break;
                                case 2:
                                    res = R.drawable.ic_duty_btn3;
                                    break;
                                case 3:
                                    res = R.drawable.ic_duty_btn4;
                                    break;
                            }
                        }
                        lTextView.setBackgroundDrawable(null);
                        if(res != 0){
                            lTextView.setBackgroundDrawable(getResources().getDrawable(res));
                        }
                        return lTextView;
                    }

                };
                mCheck1FlowLayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
                    @Override
                    public boolean onTagClick(View view, int position, FlowLayout parent) {
                        mCheckLineOneValue = position+1;
                        LogUtil.Log("lenita","position = "+textList1.get(position));
                        return true;
                    }
                });
                if(isCanCheck){
                    mCheck1TagAdapter.setSelectedList(0);
                }
                mCheck1FlowLayout.setAdapter(mCheck1TagAdapter);
                break;
        }
    }*/
}

