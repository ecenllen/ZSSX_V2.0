package com.gta.zssx.mobileOA.view.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gta.utils.resource.L;
import com.gta.zssx.R;
import com.gta.zssx.mobileOA.model.bean.DutyTableInfo;

import java.util.ArrayList;
import java.util.List;

import static com.gta.zssx.mobileOA.model.bean.DutyTableInfo.*;

/**
 * Created by lan.zheng on 2016/11/21.
 */
public class DutyTableAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private List<DutyTableInfo.DutyArrange> mDutyTableList;

    public DutyTableAdapter(Context context,List<DutyTableInfo.DutyArrange> list) {
        mContext = context;
        mDutyTableList = new ArrayList<>();
        mDutyTableList = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return TableHolder.newHolder(parent, mContext);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final TableHolder tableHolder = (TableHolder) holder;
        DutyTableInfo.DutyArrange dutyArrange = mDutyTableList.get(position);

        tableHolder.WeekTv.setText(dutyArrange.getWeek());
        tableHolder.WeekPeriodTv.setText("（"+dutyArrange.getWeekDuration()+"）");

        DutyTableDetailsAdapter dutyTableDetailsAdapter = new DutyTableDetailsAdapter(mContext,dutyArrange.getDetails());
        tableHolder.DetailRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        tableHolder.DetailRecyclerView.setAdapter(dutyTableDetailsAdapter);
//        List<DutyTableInfo.DutyArrange.DutyDtail> dtails = testData();
//        DutyTableDetailsAdapter dutyTableDetailsAdapter = new DutyTableDetailsAdapter(mContext,dtails); //测试数据
    }

    @Override
    public int getItemCount() {
        return mDutyTableList.size();
    }

    private static class TableHolder extends RecyclerView.ViewHolder {
        TextView WeekTv;
        TextView WeekPeriodTv;
        RecyclerView DetailRecyclerView;

        public TableHolder(Context context, View itemView) {
            super(itemView);
            WeekTv = (TextView) itemView.findViewById(R.id.tv_week);
            WeekPeriodTv = (TextView) itemView.findViewById(R.id.tv_week_period);
            DetailRecyclerView = (RecyclerView)itemView.findViewById(R.id.recyclerView);
        }

        private static TableHolder newHolder(ViewGroup parent, Context context) {
            View view = LayoutInflater.from(context).inflate(R.layout.list_item_duty_table_info, parent, false);
            return new TableHolder(context, view);
        }
    }

}
