package com.gta.zssx.mobileOA.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gta.zssx.R;
import com.gta.zssx.mobileOA.model.bean.EventNoticeInfo;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lan.zheng on 2016/10/31.  事务提醒
 */
public class EventNoticeAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private List<EventNoticeInfo.EventRemindEntity> mRemindList;
    private String serverTime;
    private long todayTimeMillis;
//    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
//    DateTime mServerDate;

    public EventNoticeAdapter(Context context, EventNoticeInfo eventNoticeInfo) {
        mContext = context;
        mRemindList = new ArrayList<>();
        mRemindList.clear();
        mRemindList = eventNoticeInfo.getEventRemind();
        String[] strings = eventNoticeInfo.getServerTime().split(" ");
        DateTime mServerDate = new DateTime(strings[0]);  //得到年月日
        todayTimeMillis = mServerDate.getMillis();
        this.serverTime = eventNoticeInfo.getServerTime();
//        try {
//            Date date = simpleDateFormat.parse(eventNoticeInfo.getServerTime());

//            todayTimeMillis = date.getTime();
        /*} catch (ParseException e) {
            e.printStackTrace();
        }*/
    }

    public void setData(EventNoticeInfo eventNoticeInfo){
//        int start = mRemindList.size()-1;
//        int end = start+eventNoticeInfo.getEventRemind().size();
        mRemindList.addAll(eventNoticeInfo.getEventRemind());
        String[] strings = eventNoticeInfo.getServerTime().split(" ");
        DateTime mServerDate = new DateTime(strings[0]);  //得到年月日
        todayTimeMillis = mServerDate.getMillis();
        notifyDataSetChanged();  //全部更新，因为服务器时间会更新
    }

/*
    public void clearData(){
        mRemindList.clear();
        notifyDataSetChanged();
    }

*/

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return RemindHolder.newHolder(parent, mContext);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final RemindHolder remindHolder = (RemindHolder) holder;
        EventNoticeInfo.EventRemindEntity eventRemindList = mRemindList.get(position);
        remindHolder.tvTime.setText(eventRemindList.getEndTime() == null?"": eventRemindList.getEndTime());
        remindHolder.tvApprover.setText(eventRemindList.getCreator() == null?"-" :eventRemindList.getCreator());
        remindHolder.tvStatus.setText(eventRemindList.getStatus() == null?"-" :eventRemindList.getStatus());
        remindHolder.tvResult.setText(eventRemindList.getCheckStatus() == null?"-" :eventRemindList.getCheckStatus());
        remindHolder.tvContent.setText(eventRemindList.getSubject() == null?"-" :eventRemindList.getSubject());
    }

    private String showTime(String time){
        if(TextUtils.isEmpty(time)){
            return "";
        }
        //返回显示时间的样式
        String showTime = time;
        String[] array = time.split(" ");
        String dateNotTime = array[0];
        String timeNotDate = array[1];
        DateTime date = new DateTime(dateNotTime);
        if(date.getMillis() < todayTimeMillis){
            showTime = dateNotTime;
        }else {
            String[] strings = timeNotDate.split(":");
            showTime = strings[0]+":"+strings[1];
        }
        /*try {
            Date date = simpleDateFormat.parse(time);
            if(date.getTime() < todayTimeMillis ){
                showTime = dateNotTime;
            }else {
                showTime = timeNotDate;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }*/
        return showTime;
    }


    @Override
    public int getItemCount() {
        return mRemindList.size();
    }

    private static class RemindHolder extends RecyclerView.ViewHolder {
        TextView tvTime;
        TextView tvApprover;
        TextView tvStatus;
        TextView tvResult;
        TextView tvContent;

        public RemindHolder(Context context, View itemView) {
            super(itemView);
            tvTime = (TextView) itemView.findViewById(R.id.tv_completeTime);
            tvApprover = (TextView) itemView.findViewById(R.id.tv_approver);
            tvStatus = (TextView) itemView.findViewById(R.id.tv_status);
            tvResult = (TextView) itemView.findViewById(R.id.tv_result);
            tvContent = (TextView) itemView.findViewById(R.id.tv_content);
        }

        private static RemindHolder newHolder(ViewGroup parent, Context context) {
            View view = LayoutInflater.from(context).inflate(R.layout.list_item_event_notice, parent, false);
            return new RemindHolder(context, view);
        }
    }
}

