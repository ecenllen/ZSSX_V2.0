package com.gta.zssx.fun.adjustCourse.view.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.TimeUtils;
import com.github.captain_miao.recyclerviewutils.BaseLoadMoreRecyclerAdapter;
import com.gta.zssx.R;
import com.gta.zssx.fun.adjustCourse.model.bean.ApplyBean;
import com.gta.zssx.fun.adjustCourse.view.base.Constant;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * [Description]
 * <p>
 * [How to use]
 * <p>
 * [Tips]
 *
 * @author Created by Zhimin.Huang on 2016/10/27.
 * @since 1.0.0
 */
public class ApplyCourseAdapter extends BaseLoadMoreRecyclerAdapter<ApplyBean.ListBean, RecyclerView.ViewHolder> {

    private Context mContext;
    private Listener mListener;

    public ApplyCourseAdapter(Context context, Listener listener) {
        mContext = context;
        mListener = listener;
    }

    public void notifyDeleteItem(int position) {
        getList().remove(position);
        notifyItemRemoved(position);
    }

    public interface Listener {
        void itemLongClick(ApplyBean.ListBean listBean);

        void itemClick(ApplyBean.ListBean listBean, int position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
        return ListHolder.newHolder(parent, mContext);
    }

    @Override
    public void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position) {
        ApplyBean.ListBean lItem = getItem(position);
        ApplyCourseAdapter.ListHolder lListHolder = (ApplyCourseAdapter.ListHolder) holder;
        if (lItem.getApplyType().equals("T")) {
            lListHolder.mApplyType.setText("调课申请");
            String lAdjustDate = TimeUtils.millis2String(lItem.getAdjustDate(), Constant.DATE_TYPE_01);
            lListHolder.mAcceptTimeCourse.setText(lAdjustDate + "  第" + lItem.getAdjustPolysyllabicWord() + "节");
            lListHolder.mTypeImage.setImageResource(R.drawable.exchange_of_people);
        } else {
            lListHolder.mApplyType.setText("代课申请");
            lListHolder.mAcceptTimeCourse.setText("");
            lListHolder.mTypeImage.setImageResource(R.drawable.people);
        }
        String lApplyDate = TimeUtils.millis2String(lItem.getApplyDate(), Constant.DATE_TYPE_01);
        lListHolder.mApplyTime.setText(lApplyDate);
        lListHolder.mApplyName.setText(lItem.getApplyTeacherName());
        lListHolder.mApplyTimeCourse.setText(lApplyDate + "  第" + lItem.getApplyPolysyllabicWord() + "节");
        lListHolder.mStatusTv.setText(lItem.getAuditStatus().equals("N") ? "未确认" : "已确认");
        lListHolder.mStatusTv.setTextColor(lItem.getAuditStatus().equals("N") ? ContextCompat.getColor(mContext, R.color.gray_666666) : ContextCompat.getColor(mContext, R.color.main_color));

        lListHolder.mAcceptName.setText(lItem.getAdjustTeacherName());


        lListHolder.itemView.setOnClickListener(v -> mListener.itemClick(lItem, lListHolder.getAdapterPosition()));

        lListHolder.itemView.setOnLongClickListener(v -> {
            mListener.itemLongClick(lItem);
            return true;
        });
    }

    private static class ListHolder extends RecyclerView.ViewHolder {
        TextView mApplyType;
        TextView mApplyTime;
        TextView mStatusTv;
        ImageView mTypeImage;
        TextView mApplyName;
        TextView mApplyTimeCourse;
        TextView mAcceptName;
        TextView mAcceptTimeCourse;

        public ListHolder(Context context, View itemView) {
            super(itemView);
            mApplyType = (TextView) itemView.findViewById(R.id.adjust_type_tv);
            mApplyTime = (TextView) itemView.findViewById(R.id.apply_time_tv);
            mStatusTv = (TextView) itemView.findViewById(R.id.status_tv);
            mApplyName = (TextView) itemView.findViewById(R.id.apply_name_tv);
            mAcceptName = (TextView) itemView.findViewById(R.id.accept_name_tv);
            mApplyTimeCourse = (TextView) itemView.findViewById(R.id.apply_time_course_tv);
            mAcceptTimeCourse = (TextView) itemView.findViewById(R.id.accept_time_course_tv);
            mTypeImage = (ImageView) itemView.findViewById(R.id.type_image);

        }

        private static ListHolder newHolder(ViewGroup parent, Context context) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_adjust_course, parent, false);
            return new ListHolder(context, view);
        }
    }
}
