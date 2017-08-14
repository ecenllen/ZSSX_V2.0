package com.gta.zssx.mobileOA.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.blankj.utilcode.util.TimeUtils;
import com.gta.zssx.R;
import com.gta.zssx.mobileOA.model.bean.SemessterAndWeeklyInfo;
import org.joda.time.DateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lan.zheng on 2016/11/2.
 */
public class WeeklyScheduleAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private List<SemessterAndWeeklyInfo> mScheduleList;
    private long todayTimeMillis;

    public WeeklyScheduleAdapter(Context context, List<SemessterAndWeeklyInfo> infoList) {
        mContext = context;
        mScheduleList = new ArrayList<>();
        mScheduleList = infoList;
    }

    public void clearData(){
        mScheduleList.clear();
        notifyDataSetChanged();
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return RemindHolder.newHolder(parent, mContext);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final RemindHolder remindHolder = (RemindHolder) holder;
        SemessterAndWeeklyInfo semessterAndWeeklyInfo = mScheduleList.get(position);
        String[] strings = semessterAndWeeklyInfo.getTime().split("T");
        DateTime dateTime1 = new DateTime(strings[0]);
        String lDate1 = dateTime1.toString("MM月dd日");
        String[] time = strings[1].split(":");
        String showTime = lDate1 + "（"+ TimeUtils.getWeek(dateTime1.toDate())+"）"+time[0]+":"+time[1];
        remindHolder.TimeShowTv.setText(showTime);
        remindHolder.ContentTv.setText(semessterAndWeeklyInfo.getContent());
        remindHolder.PlaceShowTv.setText(semessterAndWeeklyInfo.getPlace());
        remindHolder.RespShowTv.setText(semessterAndWeeklyInfo.getResponsibleDepartment());

    }

    @Override
    public int getItemCount() {
        return mScheduleList.size();
    }

    private static class RemindHolder extends RecyclerView.ViewHolder {
        TextView TimeShowTv;
        TextView ContentTv;
        TextView PlaceShowTv;
        TextView RespShowTv;

        public RemindHolder(Context context, View itemView) {
            super(itemView);
            TimeShowTv = (TextView) itemView.findViewById(R.id.tv_time_show);
            ContentTv = (TextView) itemView.findViewById(R.id.tv_content_show);
            PlaceShowTv = (TextView) itemView.findViewById(R.id.tv_place_show);
            RespShowTv = (TextView) itemView.findViewById(R.id.tv_dept_show);

        }

        private static RemindHolder newHolder(ViewGroup parent, Context context) {
            View view = LayoutInflater.from(context).inflate(R.layout.list_item_schedule_weekly_plan, parent, false);
            return new RemindHolder(context, view);
        }
    }
}

