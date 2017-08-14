package com.gta.zssx.mobileOA.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gta.zssx.R;
import com.gta.zssx.mobileOA.model.bean.OfficeNoticeInfo;
import com.gta.zssx.pub.util.LogUtil;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lan.zheng on 2016/11/4.   学校公文公告
 */
public class OfficeNoticeAdapter  extends RecyclerView.Adapter {

    private Context mContext;
    private List<OfficeNoticeInfo.OfficeNoticeRemindEntity> mRemindList;
    private long todayTimeMillis;
    private Listener mListener;
//    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);

    public OfficeNoticeAdapter(Context context, Listener listener, OfficeNoticeInfo officeNoticeInfo) {
        mContext = context;
        mListener = listener;
        mRemindList = new ArrayList<>();
        mRemindList = officeNoticeInfo.getOfficeNoticeReminds();
        String[] strings = officeNoticeInfo.getServerTime().split(" ");
        DateTime mServerDate = new DateTime(strings[0]);  //得到年月日
        todayTimeMillis = mServerDate.getMillis();
        /*try {
            Date date = simpleDateFormat.parse(officeNoticeInfo.getServerTime());
            todayTimeMillis = date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }*/
    }

    public void setData(OfficeNoticeInfo officeNoticeInfo){
//        int start = mRemindList.size()-1;
//        int end = start+eventNoticeInfo.getEventRemind().size();
        mRemindList.addAll(officeNoticeInfo.getOfficeNoticeReminds());
        String[] strings = officeNoticeInfo.getServerTime().split(" ");
        DateTime mServerDate = new DateTime(strings[0]);  //得到年月日
        todayTimeMillis = mServerDate.getMillis();
        notifyDataSetChanged();  //全部更新，因为服务器时间会更新
    }

    public void refreshData(OfficeNoticeInfo officeNoticeInfo){
        mRemindList.clear();
        mRemindList.addAll(officeNoticeInfo.getOfficeNoticeReminds());
        String[] strings = officeNoticeInfo.getServerTime().split(" ");
        DateTime mServerDate = new DateTime(strings[0]);  //得到年月日
        todayTimeMillis = mServerDate.getMillis();
        notifyDataSetChanged();
    }

    public void clearData(){
        mRemindList.clear();
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return RemindHolder.newHolder(parent, mContext);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final RemindHolder remindHolder = (RemindHolder) holder;
        OfficeNoticeInfo.OfficeNoticeRemindEntity Remind = mRemindList.get(position);
        String time = showTime(Remind.getTime());

        if(Remind.getStatus() == 0){
            remindHolder.StatusIv.setVisibility(View.VISIBLE);
        }else {
            remindHolder.StatusIv.setVisibility(View.INVISIBLE);
        }
        remindHolder.ContentTv.setText(Remind.getContent());
        remindHolder.TimeShowTv.setText(time);

        remindHolder.NoticeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtil.Log("lenita","Remind = "+Remind.getContent());
                mListener.onClickListener(Remind,position);
            }
        });
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
        TextView ContentTv;
        TextView TimeShowTv;
        ImageView StatusIv;
        LinearLayout NoticeLayout;

        public RemindHolder(Context context, View itemView) {
            super(itemView);
            ContentTv = (TextView) itemView.findViewById(R.id.tv_notice_content);
            TimeShowTv = (TextView) itemView.findViewById(R.id.tv_time_show);
            StatusIv = (ImageView) itemView.findViewById(R.id.iv_notice_status);
            NoticeLayout = (LinearLayout)itemView.findViewById(R.id.layout_item_show_official_notice);
        }

        private static RemindHolder newHolder(ViewGroup parent, Context context) {
            View view = LayoutInflater.from(context).inflate(R.layout.list_item_office_notice, parent, false);
            return new RemindHolder(context, view);
        }
    }

    public interface Listener{
        void onClickListener(OfficeNoticeInfo.OfficeNoticeRemindEntity remind,int position);
    }

    public void changeNoticeStatus(int positon){
        mRemindList.get(positon).setStatus(1);
        notifyDataSetChanged();
    }
}

