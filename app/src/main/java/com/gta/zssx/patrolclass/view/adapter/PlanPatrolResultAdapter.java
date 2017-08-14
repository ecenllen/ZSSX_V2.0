package com.gta.zssx.patrolclass.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gta.zssx.R;
import com.gta.zssx.patrolclass.model.entity.PlanPatrolResultEntity;

import java.util.List;

/**
 * [Description]
 * <p/>
 * [How to use]
 * <p/>
 * [Tips] Created by tengfei.lv on 2016/7/14 17:52.
 */

public class PlanPatrolResultAdapter extends RecyclerView.Adapter {

    private List<PlanPatrolResultEntity> datas;
    private RecyclerViewItemClickListener lister;
    private RecyclerViewLongItemClickListener longItemClickListener;

    public void setLongItemClickListener (RecyclerViewLongItemClickListener longItemClickListener) {
        this.longItemClickListener = longItemClickListener;
    }

    public void setItemClickLister (RecyclerViewItemClickListener lister) {
        this.lister = lister;
    }

    public void setClassResultDatas (List<PlanPatrolResultEntity> datas) {
        this.datas = datas;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {
        View view = LayoutInflater.from (parent.getContext ()).inflate (R.layout.adapter_item_plan_patrol_result, parent, false);
        return new PlanClassViewHolder (view);
    }

    @Override
    public void onBindViewHolder (RecyclerView.ViewHolder holder, int position) {
        PlanPatrolResultEntity entity = datas.get (position);
        if (holder instanceof PlanClassViewHolder) {
            ((PlanClassViewHolder) holder).classNameTv.setText (entity.getClassName ());
        }
    }

    @Override
    public int getItemCount () {
        return datas == null ? 0 : datas.size ();
    }


    class PlanClassViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        private final TextView classNameTv;

        public PlanClassViewHolder (View itemView) {
            super (itemView);

            classNameTv = (TextView) itemView.findViewById (R.id.tv_plan_patrol_adapter);
            itemView.setOnClickListener (this);
            itemView.setOnLongClickListener (this);
        }

        @Override
        public void onClick (View v) {
            if (lister != null) {
                lister.onItemClick (v, getLayoutPosition ());
            }
        }

        @Override
        public boolean onLongClick (View v) {
            if (longItemClickListener != null) {
                longItemClickListener.onLongItemClick (v, getLayoutPosition ());
            }
            return false;
        }
    }

    /**
     * 短按点击事件
     */
    public interface RecyclerViewItemClickListener {
        void onItemClick (View v, int position);
    }

    /**
     * 长按点击事件
     */
    public interface RecyclerViewLongItemClickListener {
        void onLongItemClick (View v, int position);
    }
}
