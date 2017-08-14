package com.gta.zssx.mobileOA.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gta.zssx.R;
import com.gta.zssx.mobileOA.model.bean.SemessterAndWeeklyInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lan.zheng on 2016/11/2.
 */
public class SemesterScheduleAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private List<SemessterAndWeeklyInfo> mScheduleList;
    private long todayTimeMillis;

    public SemesterScheduleAdapter(Context context, List<SemessterAndWeeklyInfo> infoList) {
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
        //TODO 显示内容
    }

    @Override
    public int getItemCount() {
        return mScheduleList.size();
    }

    private static class RemindHolder extends RecyclerView.ViewHolder {
        TextView ContentTv;
        TextView TimeShowTv;
        TextView PlaceShowTv;
        TextView HostShowTv;
        TextView JoinShowTv;

        public RemindHolder(Context context, View itemView) {
            super(itemView);
            TimeShowTv = (TextView) itemView.findViewById(R.id.tv_time_show);
            ContentTv = (TextView) itemView.findViewById(R.id.tv_content_show);
            PlaceShowTv = (TextView) itemView.findViewById(R.id.tv_place_show);
            HostShowTv = (TextView) itemView.findViewById(R.id.tv_host_show);
            JoinShowTv = (TextView) itemView.findViewById(R.id.tv_join_show);
        }

        private static RemindHolder newHolder(ViewGroup parent, Context context) {
            View view = LayoutInflater.from(context).inflate(R.layout.list_item_schedule_term_plan, parent, false);
            return new RemindHolder(context, view);
        }
    }
}

