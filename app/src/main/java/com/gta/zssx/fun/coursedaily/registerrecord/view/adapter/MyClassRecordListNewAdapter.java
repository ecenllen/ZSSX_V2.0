package com.gta.zssx.fun.coursedaily.registerrecord.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gta.zssx.R;
import com.gta.zssx.fun.coursedaily.registercourse.model.bean.DetailItemShowBean;
import com.gta.zssx.fun.coursedaily.registerrecord.model.DTO.RegisteredRecordFromSignatureDto;
import java.util.List;

/**
 * Created by lan.zheng on 2017/3/1.
 * 我的班级list适配器
 */
public class MyClassRecordListNewAdapter extends RecyclerView.Adapter{
    private Context mContext;
    private Listener mListener;
    private List<RegisteredRecordFromSignatureDto> mRegisteredRecordFromSignatureDtos;
    private boolean mIsApprove = true;

    public MyClassRecordListNewAdapter(Context context, Listener listener, List<RegisteredRecordFromSignatureDto> registeredRecordFromSignatureDtoList) {
        mContext = context;
        mListener = listener;
        mRegisteredRecordFromSignatureDtos = registeredRecordFromSignatureDtoList;
    }

    public void clearUpData(){
        mRegisteredRecordFromSignatureDtos.clear();
        notifyDataSetChanged();
    }

    public void removeData(int position){
        mRegisteredRecordFromSignatureDtos.remove(position); //删除的数据位置
        notifyItemRemoved(position);  //动画效果
        notifyItemRangeChanged(position,mRegisteredRecordFromSignatureDtos.size());//view刷新
    }

    public void setApproveStatus(boolean isApprove){
        mIsApprove = isApprove;
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
        String score = ""+lRegisteredRecordFromSignatureDto.getScore();
        String late = ""+lRegisteredRecordFromSignatureDto.getLateCount();
        String leave = ""+lRegisteredRecordFromSignatureDto.getLeaveCount();
        String truant = ""+lRegisteredRecordFromSignatureDto.getTruantCount();
        String holiday = ""+lRegisteredRecordFromSignatureDto.getHolidayCount();
        //设置值
        lHolder.attendanceStateTv.setText(lRegisteredRecordFromSignatureDto.getSignInfo());
        lHolder.classScoreTv.setText(score);
        lHolder.classSectionTv.setText(lRegisteredRecordFromSignatureDto.getSection().getLesson());
        String courseName = getCourseString(lRegisteredRecordFromSignatureDto.getMultipleCoursesList());
        lHolder.courseNameTv.setText(courseName);
//        lHolder.latePersonTv.setText(late);
//        lHolder.leavePersonTv.setText(leave);
//        lHolder.truantPersonTv.setText(truant);
//        lHolder.holidayPersonTv.setText(holiday);
        //审核状态
        String approveString = mIsApprove ? "已审核":"未审核";
        lHolder.checkStatusTv.setText(approveString);

        /*if(lRegisteredRecordFromSignatureDto.getLateCount() > 0){
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
        }*/
        lHolder.mItemLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.itemClick(lRegisteredRecordFromSignatureDto);  //回调函数，点击的响应跳转
                }
            }
        });
        //暂时是都能长按，只是有没有弹框的区别而已
        lHolder.mItemLinearLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mListener != null) {
                    mListener.itemLongClick(lRegisteredRecordFromSignatureDto,position);  //回调函数，点击的响应跳转
                }
                return true; //长按功能不和短按相互影响，设置为true
            }
        });
    }

    @Override
    public int getItemCount() {
        return mRegisteredRecordFromSignatureDtos.size();
    }

    /**
     * 展示课程名称
     * @param courseInfoBeen
     * @return
     */
    public String getCourseString(List<DetailItemShowBean.CourseInfoBean> courseInfoBeen){
        String courseString = "";
        if(courseInfoBeen.size() == 0){
            courseString = "无";
        }else {

            if(courseInfoBeen.size() == 1){
                courseString = courseInfoBeen.get(0).getCourseName();
            }else if(courseInfoBeen.size() == 2){
                for(int i = 0;i < courseInfoBeen.size();i++){
                    String courseName = courseInfoBeen.get(i).getCourseName();
                    if(i != 0){
                        courseString += "，";
                    }
                    if(courseName.length() > 5){
                        courseString += courseName.substring(0,5)+"...";
                    }else {
                        courseString += courseName;
                    }
                }
            }else {
                for(int i = 0;i < 2;i++){
                    String courseName = courseInfoBeen.get(i).getCourseName();
                    if(i != 0){
                        courseString += "，";
                    }
                    if(courseName.length() > 5){
                        courseString += courseName.substring(0,5)+"...";
                    }else {
                        courseString += courseName;
                    }
                }
                courseString += "等"+courseInfoBeen.size()+"门课程";
            }

        }
        return courseString;
    }


    public interface Listener {
        void itemClick(RegisteredRecordFromSignatureDto registeredRecordFromSignatureDto);

        void itemLongClick(RegisteredRecordFromSignatureDto registeredRecordFromSignatureDto, int position);
    }

    private static class DisplayHolder extends RecyclerView.ViewHolder {

        LinearLayout mItemLinearLayout;
        TextView classSectionTv;  //节次
        TextView attendanceStateTv; //出勤
//        TextView courseTeacherTv;
        TextView classScoreTv;  //得分
//        TextView latePersonTv;  //迟到
//        TextView leavePersonTv; //请假
//        TextView truantPersonTv; //旷课
//        TextView holidayPersonTv; //公假
        TextView checkStatusTv; //审核状态
        TextView courseNameTv;  //课程名字

        public DisplayHolder(Context context, View itemView) {
            super(itemView);
            classSectionTv = (TextView) itemView.findViewById(R.id.tv_class_section_my_class);
            attendanceStateTv = (TextView) itemView.findViewById(R.id.tv_show_attendance_state_my_class);
            classScoreTv = (TextView) itemView.findViewById(R.id.tv_show_class_score_my_class);
//            latePersonTv = (TextView) itemView.findViewById(R.id.tv_late_person_my_class);
//            leavePersonTv = (TextView) itemView.findViewById(R.id.tv_leave_person_my_class);
//            truantPersonTv = (TextView) itemView.findViewById(R.id.tv_truant_person_my_class);
//            holidayPersonTv = (TextView) itemView.findViewById(R.id.tv_holiday_person_my_class);
            mItemLinearLayout = (LinearLayout)itemView.findViewById(R.id.layout_item_show_my_class);
            courseNameTv = (TextView)itemView.findViewById(R.id.tv_course_name_my_class);
            checkStatusTv = (TextView)itemView.findViewById(R.id.tv_show_status_my_class);
        }

        private static DisplayHolder newHolder(ViewGroup parent, Context context) {
            View view = LayoutInflater.from(context).inflate(R.layout.list_item_my_class_record_new, parent, false);
            return new DisplayHolder(context, view);
        }
    }
}
