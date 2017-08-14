package com.gta.zssx.fun.coursedaily.registercourse.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.captain_miao.recyclerviewutils.BaseLoadMoreRecyclerAdapter;
import com.gta.zssx.R;
import com.gta.zssx.fun.coursedaily.registerrecord.model.DTO.RegisteredRecordDto;
import com.gta.zssx.pub.util.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * [Description]
 * <p>
 * [How to use]
 * <p>
 * [Tips]
 *
 * @author Created by Zhimin.Huang on 2016/7/20.
 * @since 1.0.0
 */
public class MultiPagerAdapter extends BaseLoadMoreRecyclerAdapter<RegisteredRecordDto.recordEntry, RecyclerView.ViewHolder> {

    private Context mContext;
    private Listener mListener;

    public interface Listener {
        void itemClick(RegisteredRecordDto.recordEntry recordEntry);
    }

    public MultiPagerAdapter(Context context, Listener listener) {
        mContext = context;
        mListener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
        return DisplayHolder.newHolder(parent, mContext);
    }

    @Override
    public void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position) {
        DisplayHolder lHolder = (DisplayHolder) holder;
        RegisteredRecordDto.recordEntry lRecordEntry = getItem(position);
        String date = lRecordEntry.getSignDate() + " ( " + getWeek(lRecordEntry.getSignDate()) + " ) ";
        lHolder.classDateTv.setText(date);
        lHolder.classSectionTv.setText("第" + lRecordEntry.getSection() + "节");
        lHolder.classNameTv.setText(lRecordEntry.getClassName());
        lHolder.mItemLinearLayout.setOnClickListener(v -> {
            if (mListener != null) {
                mListener.itemClick(lRecordEntry);  //回掉函数，点击的响应跳转
            }
        });
    }


    private String getWeek(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar lCalendar = Calendar.getInstance();
        String WeekDay = "";
        //获取星期几
        try {
            long beginTimeInMillis = sdf.parse(date).getTime();
            lCalendar.setTimeInMillis(beginTimeInMillis);
            int weekDay = lCalendar.get(Calendar.DAY_OF_WEEK);
            WeekDay = StringUtils.changeWeekDaytoString(weekDay);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return WeekDay;
    }

    private static class DisplayHolder extends RecyclerView.ViewHolder {

        TextView classDateTv;
        TextView classSectionTv;
        TextView classNameTv;
        LinearLayout mItemLinearLayout;

        public DisplayHolder(Context context, View itemView) {
            super(itemView);
            classDateTv = (TextView) itemView.findViewById(R.id.tv_clss_date);
            classSectionTv = (TextView) itemView.findViewById(R.id.tv_class_section);
            classNameTv = (TextView) itemView.findViewById(R.id.tv_class_name2);
            mItemLinearLayout = (LinearLayout) itemView.findViewById(R.id.layout_item);
        }

        private static DisplayHolder newHolder(ViewGroup parent, Context context) {
            View view = LayoutInflater.from(context).inflate(R.layout.list_item_registered_record_from_class_log_mainpage, parent, false);
            return new DisplayHolder(context, view);
        }
    }

}
