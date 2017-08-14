package com.gta.zssx.fun.coursedaily.registerrecord.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gta.zssx.R;
import com.gta.zssx.fun.coursedaily.registerrecord.model.DTO.RegisteredRecordFromSignatureDto;

import java.util.List;

/**
 * Created by lan.zheng on 2016/6/25.
 */
@Deprecated
public class MyClassRecordListAdapter extends RecyclerView.Adapter{
    private Context mContext;
    private Listener mListener;
    private List<RegisteredRecordFromSignatureDto> mRegisteredRecordFromSignatureDtos;

    public MyClassRecordListAdapter(Context context, Listener listener, List<RegisteredRecordFromSignatureDto> registeredRecordFromSignatureDtoList) {
        mContext = context;
        mListener = listener;
        mRegisteredRecordFromSignatureDtos = registeredRecordFromSignatureDtoList;
    }

    public void clearUpData(){
        mRegisteredRecordFromSignatureDtos.clear();
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return DisplayHolder.newHolder(parent, mContext);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        DisplayHolder lHolder = (DisplayHolder) holder;
        final RegisteredRecordFromSignatureDto lRegisteredRecordFromSignatureDto = mRegisteredRecordFromSignatureDtos.get(position);
        //设置值
        lHolder.courseTeacherTv.setText(lRegisteredRecordFromSignatureDto.getCourseTeacher());
        lHolder.attendanceStateTv.setText(lRegisteredRecordFromSignatureDto.getSignInfo());
        lHolder.classScoreTv.setText(""+lRegisteredRecordFromSignatureDto.getScore());
        lHolder.classSectionTv.setText("第"+lRegisteredRecordFromSignatureDto.getSectionID()+"节");
        lHolder.latePersonTv.setText(""+lRegisteredRecordFromSignatureDto.getLateCount());
        lHolder.leavePersonTv.setText(""+lRegisteredRecordFromSignatureDto.getLeaveCount());
        lHolder.truantPersonTv.setText(""+lRegisteredRecordFromSignatureDto.getTruantCount());
        lHolder.holidayPersonTv.setText(""+lRegisteredRecordFromSignatureDto.getHolidayCount());
        if(lRegisteredRecordFromSignatureDto.getLateCount() > 0){
            lHolder.latePersonTv.setTextColor(mContext.getResources().getColor(R.color.main_color));
        }else {
            lHolder.latePersonTv.setTextColor(mContext.getResources().getColor(R.color.word_color_999999));
        }
        if(lRegisteredRecordFromSignatureDto.getLeaveCount() > 0){
            lHolder.leavePersonTv.setTextColor(mContext.getResources().getColor(R.color.main_color));
        }else {
            lHolder.leavePersonTv.setTextColor(mContext.getResources().getColor(R.color.word_color_999999));
        }
        if(lRegisteredRecordFromSignatureDto.getTruantCount() > 0){
            lHolder.truantPersonTv.setTextColor(mContext.getResources().getColor(R.color.main_color));
        }else {
            lHolder.truantPersonTv.setTextColor(mContext.getResources().getColor(R.color.word_color_999999));
        }
        if(lRegisteredRecordFromSignatureDto.getHolidayCount() > 0){
            lHolder.holidayPersonTv.setTextColor(mContext.getResources().getColor(R.color.main_color));
        }else {
            lHolder.holidayPersonTv.setTextColor(mContext.getResources().getColor(R.color.word_color_999999));
        }
        lHolder.mItemLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.itemClick(lRegisteredRecordFromSignatureDto);  //回调函数，点击的响应跳转
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mRegisteredRecordFromSignatureDtos.size();
    }

    public interface Listener {
        void itemClick(RegisteredRecordFromSignatureDto registeredRecordFromSignatureDto);
    }

    private static class DisplayHolder extends RecyclerView.ViewHolder {

        LinearLayout mItemLinearLayout;
        TextView classSectionTv;
        TextView attendanceStateTv;
        TextView courseTeacherTv;
        TextView classScoreTv;
        TextView latePersonTv;
        TextView leavePersonTv;
        TextView truantPersonTv;
        TextView holidayPersonTv;

        public DisplayHolder(Context context, View itemView) {
            super(itemView);
            classSectionTv = (TextView) itemView.findViewById(R.id.tv_class_section_my_class);
            attendanceStateTv = (TextView) itemView.findViewById(R.id.tv_show_attendance_state_my_class);
            classScoreTv = (TextView) itemView.findViewById(R.id.tv_show_class_score_my_class);
            courseTeacherTv = (TextView)itemView.findViewById(R.id.tv_course_teacher_my_class) ;
            latePersonTv = (TextView) itemView.findViewById(R.id.tv_late_person_my_class);
            leavePersonTv = (TextView) itemView.findViewById(R.id.tv_leave_person_my_class);
            truantPersonTv = (TextView) itemView.findViewById(R.id.tv_truant_person_my_class);
            holidayPersonTv = (TextView) itemView.findViewById(R.id.tv_holiday_person_my_class);
            mItemLinearLayout = (LinearLayout)itemView.findViewById(R.id.layout_item_show_my_class);
        }

        private static DisplayHolder newHolder(ViewGroup parent, Context context) {
            View view = LayoutInflater.from(context).inflate(R.layout.list_item_my_class_record, parent, false);
            return new DisplayHolder(context, view);
        }
    }
}
