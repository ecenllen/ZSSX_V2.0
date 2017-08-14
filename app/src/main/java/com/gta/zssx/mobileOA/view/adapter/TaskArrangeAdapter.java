package com.gta.zssx.mobileOA.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bigkoo.pickerview.TimePickerView;
import com.gta.zssx.R;
import com.gta.zssx.mobileOA.model.bean.TaskArrange;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by xiaoxia.rang on 2016/10/24.
 * 任务安排adapter
 */

public class TaskArrangeAdapter extends RecyclerView.Adapter<TaskArrangeAdapter.ViewHolder> {

    private Context context;
    private List<TaskArrange> taskArranges;
    private LayoutInflater inflater;
    private OnChooseClickListener listener;
    String format = "yyyy-MM-dd HH:mm";
    SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.CANADA);

    private static final int TYPE_ITEM = 0;
    private static final int TYPE_DEPT = 1;

    public TaskArrangeAdapter(Context context, List<TaskArrange> taskArranges,OnChooseClickListener listener) {
        this.context = context;
        this.taskArranges = taskArranges;
        this.inflater = LayoutInflater.from(context);
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder holder = new ViewHolder(inflater.inflate(R.layout.item_oa_task_arrange, null));
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.position = position;
        TaskArrange taskArrange = taskArranges.get(position);
        holder.tvTaskIndex.setText("任务" + (position + 1));
        holder.etItem.setText(taskArrange.getItem() == null ? "" : taskArrange.getItem());
        holder.tvPlanTime.setText(taskArrange.getPlanTime() == null ? "" : taskArrange.getPlanTime());
        holder.tvPerson.setText(taskArrange.getPerson() == null ? "" : taskArrange.getPerson());
        holder.tvDept.setText(taskArrange.getDept() == null ? "" : taskArrange.getDept());
        holder.etItem.addTextChangedListener(new MyTextWatcher(position, TYPE_ITEM));
        holder.tvDept.addTextChangedListener(new MyTextWatcher(position, TYPE_DEPT));
        holder.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                taskArranges.remove(position);
                notifyDataSetChanged();
            }
        });
        holder.tvPlanTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerView timePickerView = new TimePickerView(context, TimePickerView.Type.ALL);
                timePickerView.setTime(new Date());
                timePickerView.setCyclic(true);
                timePickerView.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date) {
                        holder.tvPlanTime.setText(sdf.format(date));
                        taskArranges.get(position).setPlanTime(holder.tvPlanTime.getText().toString());
                    }
                });
                timePickerView.show();
            }
        });

        holder.tvPerson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onChoosePersonClick(position);
            }
        });

        holder.tvDept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onChooseDepartmentClick(position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return taskArranges == null ? 0 : taskArranges.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTaskIndex;
        EditText etItem;
        TextView tvPlanTime;
        TextView tvPerson;
        TextView tvDept;
        ImageView ivDelete;
        int position;

        public ViewHolder(View itemView) {
            super(itemView);
            tvTaskIndex = (TextView) itemView.findViewById(R.id.tv_taskIndex);
            etItem = (EditText) itemView.findViewById(R.id.et_item);
            tvPlanTime = (TextView) itemView.findViewById(R.id.tv_time);
            tvPerson = (TextView) itemView.findViewById(R.id.tv_person);
            tvDept = (TextView) itemView.findViewById(R.id.tv_dept);
            ivDelete = (ImageView) itemView.findViewById(R.id.iv_delete);
        }


    }

    public class MyTextWatcher implements TextWatcher {
        private int position;
        private int type;

        public MyTextWatcher(int position, int type) {
            this.position = position;
            this.type = type;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (type == TYPE_ITEM) {
                taskArranges.get(position).setItem(s.toString());
            } else {
                taskArranges.get(position).setDept(s.toString());
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }

    public interface OnChooseClickListener{
        void onChoosePersonClick(int position);
        void onChooseDepartmentClick(int position);
    }
}
