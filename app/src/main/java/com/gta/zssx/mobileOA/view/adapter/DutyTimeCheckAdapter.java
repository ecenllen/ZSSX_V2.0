package com.gta.zssx.mobileOA.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gta.utils.resource.L;
import com.gta.zssx.R;
import com.gta.zssx.mobileOA.model.bean.DutyDateInfo;
import com.gta.zssx.mobileOA.model.bean.DutyTimeInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lan.zheng on 2016/11/16.
 */
public class DutyTimeCheckAdapter extends RecyclerView.Adapter{
    private Context mContext;
    private List<Object> mTimeList  = new ArrayList<>();
    private List<Integer> mSelectList;  //打钩列表状态
    private int mCurrentTimePeriod; //选中时段
    private static final int STATUS_NOT_ON_DUTY = 2;
    private static final int STATUS_ON_DUTY_CHECKED = 1;
    private static final int STATUS_ON_DUTY_NEVERCHECK = 0;
    private int mStatus;

    public DutyTimeCheckAdapter(Context context, List<Object> mTimeList,boolean isCanCheck,int isCheckedOrNeverCheckBefore){
        mContext = context;
        this.mTimeList = mTimeList;
        mSelectList = new ArrayList<>();
        List<Integer> SelectList = new ArrayList<>();
        if(isCanCheck){
            if(isCheckedOrNeverCheckBefore == STATUS_ON_DUTY_NEVERCHECK){
                mStatus = STATUS_ON_DUTY_NEVERCHECK;
                for(int i = 0;i <mTimeList.size() ;i++){
                    if(i == 0){
                        SelectList.add(1);
                    }else {
                        SelectList.add(0);
                    }

                }
            }else if(isCheckedOrNeverCheckBefore == STATUS_ON_DUTY_CHECKED){
                mStatus = STATUS_ON_DUTY_CHECKED;
                for(int i = 0;i <mTimeList.size() ;i++){
                    SelectList.add(1);
                }
            }
        }else {
            mStatus = STATUS_NOT_ON_DUTY;
            for(int i = 0;i <mTimeList.size() ;i++){
                SelectList.add(0);
            }
        }
        mSelectList.clear();
        mSelectList.addAll(SelectList);
        mCurrentTimePeriod = 0;
    }

    //选中时间段
    public void changeCurrentTimePeriod(int current){
        mCurrentTimePeriod = current;
        //TODO 更新数据显示？？？
        notifyDataSetChanged();
    }

    public void changeChecked(int position){
        if(mStatus == STATUS_ON_DUTY_NEVERCHECK){
            mSelectList.set(position,1);
            notifyDataSetChanged();
        }
    }


    @Override
    public int getItemCount() {
        return mTimeList.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return TimeHolder.newHolder(parent, mContext);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final TimeHolder timeHolder = (TimeHolder) holder;
        //打钩情况处理
        if(mStatus == STATUS_ON_DUTY_NEVERCHECK){
            if(mSelectList.get(position) == 0) {
                timeHolder.DutyCheckIv.setVisibility(View.INVISIBLE);
            }else {
                timeHolder.DutyCheckIv.setVisibility(View.VISIBLE);
            }
        }else if (mStatus == STATUS_ON_DUTY_CHECKED){
            //已检查都显示全打钩
            timeHolder.DutyCheckIv.setVisibility(View.VISIBLE);
        }else {
            //未值班都不显示
            timeHolder.DutyCheckIv.setVisibility(View.GONE);
        }

        //选中时段粗体，未选中时段正常字体
        if(position == mCurrentTimePeriod){
            timeHolder.DutyRightTv.setTypeface(Typeface.DEFAULT_BOLD);
            timeHolder.DutyRightTv.setTextColor(mContext.getResources().getColor(R.color.word_color_444444));
        }else {
            timeHolder.DutyRightTv.setTypeface(Typeface.DEFAULT);
            timeHolder.DutyRightTv.setTextColor(mContext.getResources().getColor(R.color.word_color_666666));
        }
        //文字
        DutyTimeInfo dutyTimeInfo = (DutyTimeInfo) mTimeList.get(position);
        String time = dutyTimeInfo.getTime();
        timeHolder.DutyRightTv.setText(time);

    }

    private static class TimeHolder extends RecyclerView.ViewHolder {
        ImageView DutyCheckIv;
        TextView DutyRightTv;

        public TimeHolder(Context context, View itemView) {
            super(itemView);
            DutyCheckIv = (ImageView) itemView.findViewById(R.id.iv_duty_time_check);
            DutyRightTv = (TextView) itemView.findViewById(R.id.tv_right);
        }

        private static TimeHolder newHolder(ViewGroup parent, Context context) {
            View view = LayoutInflater.from(context).inflate(R.layout.list_item_duty_time_check, parent, false);
            return new TimeHolder(context, view);
        }
    }
}

