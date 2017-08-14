package com.gta.zssx.mobileOA.view.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gta.zssx.R;
import com.gta.zssx.mobileOA.model.bean.DutyDateInfo;
import com.gta.zssx.mobileOA.model.bean.DutyTimeInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lan.zheng on 2016/11/14.  注意：时间和日期共用该适配器，小心修改
 */
public class DutyTimePeriodAdapter extends RecyclerView.Adapter{
    private Context mContext;
    private int mTimeType;  //时间还是日期
    private int mDutyType;  //登记还是检查
    private int mPosition;  //已值班还是未值班
    private int mRegisterStatus;  //1.已提交 2.未提交 0.未值班
    private List<Object> mTimeList  = new ArrayList<>();
    private Listener mListener;
    public final static int TYPE_TIME = 1;
    public final static int TYPE_DATE = 2;
    private final static int TYPE_DUTY_REGISTER = 1;
    private final static int TYPE_DUTY_CHECK = 2;

    //用于日期的列表
    public DutyTimePeriodAdapter(Context context, List<Object> mTimeList ,Listener listener,int registerOrCheck,int position){
        mContext = context;
        mTimeType = TYPE_DATE;  //日期才有registerOrCheck这个参数
        mListener = listener;
        this.mTimeList = mTimeList;
        mDutyType = registerOrCheck;
        mPosition = position;
    }

    //用于时间的列表
    public DutyTimePeriodAdapter(Context context, List<Object> mTimeList, Listener listener,int mRegisterStatus){
        mContext = context;
        mTimeType = TYPE_TIME;  //时间没有registerOrCheck这个参数
        this.mRegisterStatus = mRegisterStatus;
        mListener = listener;
        this.mTimeList = mTimeList;
    }

    public void clearData(){
        mTimeList.clear();
        notifyDataSetChanged();
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
        if(mTimeType == TYPE_DATE){
            //日期选择
            DutyDateInfo dutyDateInfo = (DutyDateInfo) mTimeList.get(position);
            timeHolder.DutyTimeTv.setText(dutyDateInfo.getDate());
            if(mDutyType == TYPE_DUTY_REGISTER){  //登记的是要分可登记和不可登记的
                //登记分未值班和已值班
                switch (dutyDateInfo.getStatus()){
                    case 1:
                        timeHolder.DutyRightTv.setText(mContext.getResources().getString(R.string.text_duty_register_can_not));  //已提交的不可登记
                        break;
                    case 0:
                        timeHolder.DutyRightTv.setText(mContext.getResources().getString(R.string.text_duty_register_can));  //未提交的可登记
                        break;
                }
                switch (mPosition){
                    case 0:
                        timeHolder.DutyRightTv.setVisibility(View.VISIBLE);
                        break;
                    case 1:
                        timeHolder.DutyRightTv.setVisibility(View.GONE);
                        break;
                }
            }else if(mDutyType == TYPE_DUTY_CHECK){  //检查的是分已检查和未检查
                //检查分未值班和已值班
                switch (dutyDateInfo.getStatus()){
                    case 1:
                        timeHolder.DutyRightTv.setText(mContext.getResources().getString(R.string.text_duty_check_yes));  //已检查
                        break;
                    case 2:
                        timeHolder.DutyRightTv.setText(mContext.getResources().getString(R.string.text_duty_check_no));  //未检查
                        break;
                }
                switch (mPosition){
                    case 0:
                        timeHolder.DutyRightTv.setVisibility(View.VISIBLE);
                        break;
                    case 1:
                        timeHolder.DutyRightTv.setVisibility(View.GONE);
                        break;
                }
            }
        }else if(mTimeType == TYPE_TIME){
            //时间选择
            DutyTimeInfo dutyTimeInfo = (DutyTimeInfo) mTimeList.get(position);
            String time = dutyTimeInfo.getTime();
            timeHolder.DutyTimeTv.setText(time);
            //根据状态判断右边如何显示
            if(mRegisterStatus == 0){  //未提交
                timeHolder.DutyRightTv.setVisibility(View.VISIBLE);
                timeHolder.DutyRightTv.setText(dutyTimeInfo.getStatus() == 0 ? "未登记":"已登记" );
                Drawable drawable= mContext.getResources().getDrawable(R.drawable.right_arrow_normal);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                timeHolder.DutyRightTv.setCompoundDrawables(null,null,drawable,null);
            }else if(mRegisterStatus == 1){  //已提交
                timeHolder.DutyRightTv.setVisibility(View.VISIBLE);
                timeHolder.DutyRightTv.setText("");
                Drawable drawable= mContext.getResources().getDrawable(R.drawable.right_arrow_normal);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                timeHolder.DutyRightTv.setCompoundDrawables(null,null,drawable,null);
            }else {  //未值班
                timeHolder.DutyRightTv.setVisibility(View.GONE);
            }
        }

        timeHolder.time_period_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onClickListener(mTimeList.get(position));
            }
        });
    }

    private static class TimeHolder extends RecyclerView.ViewHolder {
        TextView DutyTimeTv;
        TextView DutyRightTv;
        RelativeLayout time_period_layout;
        public TimeHolder(Context context, View itemView) {
            super(itemView);
            DutyTimeTv = (TextView) itemView.findViewById(R.id.tv_duty_time_period);
            DutyRightTv = (TextView) itemView.findViewById(R.id.tv_right);
            time_period_layout  = (RelativeLayout)itemView.findViewById(R.id.time_period_layout);
        }

        private static TimeHolder newHolder(ViewGroup parent, Context context) {
            View view = LayoutInflater.from(context).inflate(R.layout.list_item_duty_time_period, parent, false);
            return new TimeHolder(context, view);
        }
    }

    public interface Listener{
        void onClickListener(Object object);
    }
}
