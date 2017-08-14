package com.gta.zssx.mobileOA.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gta.zssx.R;
import com.gta.zssx.mobileOA.model.bean.Tasks;
import com.gta.zssx.mobileOA.view.page.TaskDetailActivity;

import java.util.List;

/**
 * Created by xiaoxia.rang on 2016/10/24.
 */

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {

    private Context context;
    private List<Tasks.TaskInfo> taskInfos;
    private LayoutInflater inflater;

    public TaskAdapter(Context context, List<Tasks.TaskInfo> taskInfos) {
        this.context = context;
        this.taskInfos = taskInfos;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder holder = new ViewHolder(inflater.inflate(R.layout.item_oa_task, null));
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Tasks.TaskInfo taskInfo = taskInfos.get(position);
        holder.position = position;
        holder.tvTopic.setText(taskInfo.getTopic());
        holder.tvCategory.setText(taskInfo.getCategory());
        holder.tvDepartment.setText(taskInfo.getDepartment());
        holder.tvTime.setText(taskInfo.getTaskTime());
        if (taskInfo.getStatus() == 0) {
            holder.tvStatus.setText("未开始");
        }else if(taskInfo.getStatus() ==1){
            holder.tvStatus.setText("进行中");
        }else{
            holder.tvStatus.setText("已结束");
        }

    }

    @Override
    public int getItemCount() {
        return taskInfos == null ? 0 : taskInfos.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        RelativeLayout rlTask;
        TextView tvTopic;
        TextView tvCategory;
        TextView tvDepartment;
        TextView tvTime;
        TextView tvStatus;
        ImageView ivMore;
        int position;

        public ViewHolder(View itemView) {
            super(itemView);
            rlTask = (RelativeLayout)itemView.findViewById(R.id.rl_task);
            tvTopic = (TextView) itemView.findViewById(R.id.tv_topic);
            tvCategory = (TextView) itemView.findViewById(R.id.tv_category);
            tvDepartment = (TextView) itemView.findViewById(R.id.tv_department);
            tvTime = (TextView) itemView.findViewById(R.id.tv_time);
            tvStatus = (TextView) itemView.findViewById(R.id.tv_status);
            ivMore = (ImageView) itemView.findViewById(R.id.iv_more);
            rlTask.setOnClickListener(this);
            ivMore.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(context, TaskDetailActivity.class);
            intent.putExtra("task",taskInfos.get(position));
            context.startActivity(intent);
        }
    }
}
