package com.gta.zssx.mobileOA.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gta.zssx.R;
import com.gta.zssx.mobileOA.model.bean.DutyTableInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lan.zheng on 2016/11/21.
 */
public class DutyTableDetailsAdapter extends RecyclerView.Adapter {
    private Context mContext;
    private List<DutyTableInfo.DutyArrange.DutyDtail> mDutyTableDetailList;

    public DutyTableDetailsAdapter(Context context,List<DutyTableInfo.DutyArrange.DutyDtail> list) {
        mContext = context;
        mDutyTableDetailList = new ArrayList<>();
        mDutyTableDetailList = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return TableDetailsHolder.newHolder(parent, mContext);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final TableDetailsHolder tableDetailsHolder = (TableDetailsHolder) holder;
        DutyTableInfo.DutyArrange.DutyDtail dutyDtail = mDutyTableDetailList.get(position);

        tableDetailsHolder.AddressTv.setText(dutyDtail.getAddress()+"：");
        tableDetailsHolder.DetailTv.setText(dutyDtail.getPeople());
    }

    @Override
    public int getItemCount() {
        return mDutyTableDetailList.size();
    }

    private static class TableDetailsHolder extends RecyclerView.ViewHolder {
        TextView AddressTv;
        TextView DetailTv;

        public TableDetailsHolder(Context context, View itemView) {
            super(itemView);
            AddressTv = (TextView) itemView.findViewById(R.id.tv_duty_address);
            DetailTv = (TextView) itemView.findViewById(R.id.tv_duty_person);
        }

        private static TableDetailsHolder newHolder(ViewGroup parent, Context context) {
            View view = LayoutInflater.from(context).inflate(R.layout.list_item_duty_table_detail, parent, false);
            return new TableDetailsHolder(context, view);
        }
    }
}
