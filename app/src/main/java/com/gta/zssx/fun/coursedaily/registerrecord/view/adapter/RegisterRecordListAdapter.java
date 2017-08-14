package com.gta.zssx.fun.coursedaily.registerrecord.view.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gta.zssx.R;
import com.gta.zssx.fun.coursedaily.registerrecord.model.DTO.RegisteredRecordDto;
import com.gta.zssx.pub.util.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

/**
 * Created by lan.zheng on 2016/6/22.
 */
public class RegisterRecordListAdapter extends RecyclerView.Adapter {
    private Context mContext;
    private Listener mListener;
    private List<RegisteredRecordDto.recordEntry> mRecordEntries;
    //    private MyLoadMoreRecyclerView recycleeView;
    private static final int ANIMATED_ITEMS_COUNT = 2;
    private int lastAnimatedPosition = -1;

    public RegisterRecordListAdapter(Context context, Listener listener, List<RegisteredRecordDto.recordEntry> recordEntryList) {
        mContext = context;
        mListener = listener;
        mRecordEntries = recordEntryList;
    }

    public void loadMoreData(List<RegisteredRecordDto.recordEntry> recordEntryList) {
//        int lSize = mRecordEntries.size();
        mRecordEntries.addAll(recordEntryList);
//        notifyItemRangeInserted(lSize, mRecordEntries.size());
        notifyDataSetChanged();
    }

    public void clearUpData(){
        mRecordEntries.clear();
        notifyDataSetChanged();
    }


    public interface Listener {
        void itemClick(RegisteredRecordDto.recordEntry recordEntry);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        /*if (viewType == MyLoadMoreRecyclerView.TYPE_FOOTER) {
            return new FooterViewHolder(
                    LayoutInflater.from(parent.getContext()).inflate(
                            R.layout.list_foot_loading, parent, false));
        } else { // type normal
            return DisplayHolder.newHolder(parent, mContext);
//            return mInternalAdapter.onCreateViewHolder(parent, viewType);
        }*/
        return DisplayHolder.newHolder(parent, mContext);

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        DisplayHolder lHolder = (DisplayHolder) holder;
        final RegisteredRecordDto.recordEntry lRecordEntry = mRecordEntries.get(position);
        //设置值
        String date = lRecordEntry.getSignDate()+" ( "+getWeek(lRecordEntry.getSignDate())+" ) ";
        lHolder.classDateTv.setText(date);
        lHolder.classSectionTv.setText(lRecordEntry.getSection());
        lHolder.classNameTv.setText(lRecordEntry.getClassName());
        lHolder.mItemLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.itemClick(lRecordEntry);  //回掉函数，点击的响应跳转
                }
            }
        });
    }

    private String getWeek(String date){
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        Calendar lCalendar = Calendar.getInstance();
        String WeekDay ="";
        //获取星期几
        try {
            long beginTimeInMillis = sdf.parse(date).getTime();
            lCalendar.setTimeInMillis(beginTimeInMillis);
            int weekDay =  lCalendar.get(Calendar.DAY_OF_WEEK);
            WeekDay = StringUtils.changeWeekDaytoString(weekDay);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return WeekDay;
    }

    @Override
    public int getItemCount() {
        /*int count = mRecordEntries.size();
        if(MyLoadMoreRecyclerView.hasMore){
            count++;
        }*/
        return mRecordEntries.size();
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

   /* private static class FooterViewHolder extends RecyclerView.ViewHolder {
        ProgressWheel rcvLoadMore;

        public FooterViewHolder(View itemView) {
            super(itemView);
            rcvLoadMore = (ProgressWheel)itemView.findViewById(R.id.rcv_load_more);
        }
    }*/

    @SuppressLint("NewApi")
    private void runEnterAnimation(View view, int position) {
        if (position >= ANIMATED_ITEMS_COUNT - 1) {
            return;
        }

        if (position > lastAnimatedPosition) {
            lastAnimatedPosition = position;
            DisplayMetrics dm = mContext.getResources().getDisplayMetrics();
            view.setTranslationY(dm.heightPixels);
            view.animate().translationY(0)
                    .setInterpolator(new DecelerateInterpolator(3.f))
                    .setDuration(700).start();
        }
    }

}
