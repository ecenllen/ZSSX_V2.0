package com.gta.zssx.mobileOA.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gta.zssx.R;
import com.gta.zssx.mobileOA.model.bean.SubTask;

import java.util.List;

/**
 * Created by xiaoxia.rang on 2016/10/24.
 * 附件adapter
 */

public class SubTaskAdapter extends RecyclerView.Adapter<SubTaskAdapter.ViewHolder> {

    private Context context;
    private List<SubTask> subTasks;
    private LayoutInflater inflater;

    public SubTaskAdapter(Context context, List<SubTask> subTasks) {
        this.context = context;
        this.subTasks = subTasks;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder holder = new ViewHolder(inflater.inflate(R.layout.item_oa_subtask, null));
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        SubTask subTask = subTasks.get(position);
        holder.tvTask.setText(subTask.getTopic());
        holder.tvPerson.setText(subTask.getPerson());
        holder.tvTime.setText(subTask.getTime());
        switch (subTask.getStatus()){
            case 0:
                holder.tvStatus.setText("未开始");
                break;
            case 1:
                holder.tvStatus.setText("进行中");
                break;
            case 2:
                holder.tvStatus.setText("已完成");
                break;
        }
    }

    @Override
    public int getItemCount() {
        return subTasks == null ? 0 : subTasks.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout rlSubTask;
        TextView tvTask;
        TextView tvStatus;
        TextView tvPerson;
        TextView tvTime;

        public ViewHolder(View itemView) {
            super(itemView);
            rlSubTask = (RelativeLayout) itemView.findViewById(R.id.rl_subtask);
            tvTask = (TextView) itemView.findViewById(R.id.tv_task);
            tvStatus = (TextView) itemView.findViewById(R.id.tv_status);
            tvPerson = (TextView) itemView.findViewById(R.id.tv_person);
            tvTime = (TextView) itemView.findViewById(R.id.tv_time);
        }


    }
}
