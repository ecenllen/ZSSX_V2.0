package com.gta.zssx.mobileOA.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gta.utils.resource.L;
import com.gta.zssx.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lan.zheng on 2016/10/26.
 */
public class ScheduleRemindselectAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private Listener mListener;
    private List<String> mRemindList;
    private int mPosition;
    private CheckBox mCheckBox;


    public ScheduleRemindselectAdapter(Context context, Listener listener,List<String> list, int position) {
        mContext = context;
        mListener = listener;
        mRemindList = list;
//        mRemindList.addAll(list);
        mPosition = position;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return RemindHolder.newHolder(parent, mContext);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final RemindHolder remindHolder = (RemindHolder) holder;
        remindHolder.nameTv.setText(mRemindList.get(position));
        if (position == mPosition) {
            remindHolder.mCheckBox.setChecked(true);
            mCheckBox = remindHolder.mCheckBox;
        }

        //点击整个条目勾选
        remindHolder.sectionLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //點擊即立刻選中，因為後面的操作相關
                remindHolder.mCheckBox.setChecked(true);
                mPosition = position;
                mListener.itemClick(mPosition);
                if (mCheckBox != remindHolder.mCheckBox) {
                    mCheckBox.setChecked(false);
                    mCheckBox = remindHolder.mCheckBox;
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mRemindList.size();
    }

    private static class RemindHolder extends RecyclerView.ViewHolder {
        TextView nameTv;
        CheckBox mCheckBox;
        RelativeLayout sectionLayout;

        public RemindHolder(Context context, View itemView) {
            super(itemView);
            nameTv = (TextView) itemView.findViewById(R.id.remind_name_tv);
            mCheckBox = (CheckBox) itemView.findViewById(R.id.remind_choose_checkbox);
            sectionLayout = (RelativeLayout)itemView.findViewById(R.id.layout_remind_select_check);
        }

        private static RemindHolder newHolder(ViewGroup parent, Context context) {
            View view = LayoutInflater.from(context).inflate(R.layout.list_item_schedule_remind_select, parent, false);
            return new RemindHolder(context, view);
        }
    }

    public interface Listener {
        void itemClick(int position);
    }
}
