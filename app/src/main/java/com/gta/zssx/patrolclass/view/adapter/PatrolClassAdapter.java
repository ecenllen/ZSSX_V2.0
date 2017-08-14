package com.gta.zssx.patrolclass.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gta.zssx.R;
import com.gta.zssx.patrolclass.model.entity.PatrolClassEntity;
import com.gta.zssx.pub.util.LogUtil;

import java.util.List;


/**
 * Created by liang.lu1 on 2016/6/28.
 */
public class PatrolClassAdapter extends RecyclerView.Adapter {
    Context context;
    private List<PatrolClassEntity> entities;
    private int type;//区分是时间段记录页面，还是课堂巡视首页  1  课堂巡视首页   2 时间段记录页

    public PatrolClassAdapter (Context context, int type) {
        this.type = type;
        this.context = context;
    }

    public void setEntities (List<PatrolClassEntity> entities) {
        this.entities = entities;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {
        int aa;
        if (type == 1) {
            aa = R.layout.item_patrol_main_page;
        } else {
            aa = R.layout.item_patrol_main_page2;
        }
        View view = LayoutInflater.from (parent.getContext ()).inflate (aa, parent, false);
        MainClassViewHolder holder = new MainClassViewHolder (view);
        return holder;
    }

    @Override
    public void onBindViewHolder (RecyclerView.ViewHolder holder, final int position) {
        final PatrolClassEntity model = entities.get (position);
        String date = model.getDate ();
        LogUtil.e ("date==" + date + "----" + position);
        int state = model.getStatus ();
        if (holder instanceof MainClassViewHolder) {
            ((MainClassViewHolder) holder).tvDate.setText (date);
            switch (state) {
                case 0:
                    ((MainClassViewHolder) holder).tvState.setText ("未送审");
                    break;
                case 1:
                    ((MainClassViewHolder) holder).tvState.setText ("待审核");
                    break;
                case 2:
                    ((MainClassViewHolder) holder).tvState.setText ("未通过");
                    break;
                case 3:
                    ((MainClassViewHolder) holder).tvState.setText ("已通过");
                    break;
            }
            ((MainClassViewHolder) holder).setRedColor (position);
        }
    }

    @Override
    public int getItemCount () {
        if (entities == null) {
            return 0;
        }
        return entities.size ();
    }


    private class MainClassViewHolder extends RecyclerView.ViewHolder {
        private TextView tvDate;
        private TextView tvState;

        public MainClassViewHolder (View itemView) {
            super (itemView);
            tvDate = (TextView) itemView.findViewById (R.id.tv_date);
            tvState = (TextView) itemView.findViewById (R.id.tv_state);
        }

        private void setRedColor (int positon) {
            PatrolClassEntity model = entities.get (positon);
            if (model.getStatus () == 2) {
                tvState.setTextColor (context.getResources ().getColor (R.color.red_d80019));
            } else {
                tvState.setTextColor (context.getResources ().getColor (R.color.gray_666666));
            }

        }

    }

}
