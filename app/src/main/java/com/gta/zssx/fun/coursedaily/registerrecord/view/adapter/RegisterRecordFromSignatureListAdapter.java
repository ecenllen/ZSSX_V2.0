package com.gta.zssx.fun.coursedaily.registerrecord.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gta.zssx.R;
import com.gta.zssx.fun.coursedaily.registercourse.model.bean.DetailItemShowBean;
import com.gta.zssx.fun.coursedaily.registerrecord.model.DTO.RegisteredRecordFromSignatureDto;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lan.zheng on 2016/6/22.
 *
 */
public class RegisterRecordFromSignatureListAdapter extends RecyclerView.Adapter{
    private Context mContext;
    private Listener mListener;
    private List<RegisteredRecordFromSignatureDto> mRegisteredRecordFromSignatureDtos;
    private boolean mHaveBeenAprrove;
    private boolean mIsShowClassName;  //用于判断是否显示班级名称栏
    private DisplayHolder lHolder;

    public RegisterRecordFromSignatureListAdapter(Context context, Listener listener, List<RegisteredRecordFromSignatureDto> mList, boolean haveBeenAprrove, boolean mIsShowClassName) {
        mContext = context;
        mListener = listener;
        mRegisteredRecordFromSignatureDtos = new ArrayList<>();
        mRegisteredRecordFromSignatureDtos.addAll(mList);
        mHaveBeenAprrove = haveBeenAprrove;
        this.mIsShowClassName =mIsShowClassName;
    }

    public void changeStatus(boolean haveBeenApprove){
        mHaveBeenAprrove = haveBeenApprove;
        notifyItemRangeChanged(0,mRegisteredRecordFromSignatureDtos.size());
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


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return DisplayHolder.newHolder(parent, mContext);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        lHolder = (DisplayHolder) holder;
        final RegisteredRecordFromSignatureDto lRegisteredRecordFromSignatureDto = mRegisteredRecordFromSignatureDtos.get(position);
        //设置值
        if(lRegisteredRecordFromSignatureDto.getSection() != null){
            String lessonName = lRegisteredRecordFromSignatureDto.getSection().getLesson();
            lHolder.classSectionTv.setText(lessonName);
        }
        String courseName = getCourseString(lRegisteredRecordFromSignatureDto.getMultipleCoursesList());
        lHolder.courseNameTv.setText(courseName);
        String score = ""+lRegisteredRecordFromSignatureDto.getScore();
        lHolder.classScoreTv.setText(score);
        lHolder.classNameTv.setText(lRegisteredRecordFromSignatureDto.getClassName());
        if(mIsShowClassName){
            lHolder.classNameLayout.setVisibility(View.VISIBLE);
        }else {
            lHolder.classNameLayout.setVisibility(View.GONE);
        }
        //考勤情况
        lHolder.attendanceStateTv.setText(lRegisteredRecordFromSignatureDto.getSignInfo());
        if(mHaveBeenAprrove){
            lHolder.statusTv.setText(mContext.getResources().getString(R.string.text_have_been_approve));
        }else {
            lHolder.statusTv.setText(mContext.getResources().getString(R.string.text_no_approve));
        }
        String latePerson = ""+lRegisteredRecordFromSignatureDto.getLateCount();
        lHolder.latePersonTv.setText(latePerson);
        String leavePerson = ""+lRegisteredRecordFromSignatureDto.getLeaveCount();
        lHolder.leavePersonTv.setText(leavePerson);
        String truantPerson = ""+lRegisteredRecordFromSignatureDto.getTruantCount();
        lHolder.truantPersonTv.setText(truantPerson);
        String holidayPerson = ""+lRegisteredRecordFromSignatureDto.getHolidayCount();
        lHolder.holidayPersonTv.setText(holidayPerson);
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
     * @param courseInfoBeen 课程bean
     * @return 课程字符串
     */
    public String getCourseString(List<DetailItemShowBean.CourseInfoBean> courseInfoBeen){
        String courseString = "";
        if(courseInfoBeen.size() == 0){
            courseString = "无";
        }else {
            if(courseInfoBeen.size() == 1){
                courseString = courseInfoBeen.get(0).getCourseName();
            }else if(courseInfoBeen.size()== 2){
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
        TextView classNameTv;
        RelativeLayout classNameLayout;
        TextView courseNameTv;
        TextView classSectionTv;
        TextView attendanceStateTv;
        TextView classScoreTv;
        TextView statusTv;
        TextView latePersonTv;
        TextView leavePersonTv;
        TextView truantPersonTv;
        TextView holidayPersonTv;

        public DisplayHolder(Context context, View itemView) {
            super(itemView);
            classNameTv = (TextView) itemView.findViewById(R.id.tv_class_name);  //班级名称
            classNameLayout  = (RelativeLayout)itemView.findViewById(R.id.layout_class_name);
            courseNameTv = (TextView) itemView.findViewById(R.id.tv_course_name);
            classSectionTv = (TextView) itemView.findViewById(R.id.tv_class_section2);
            attendanceStateTv = (TextView) itemView.findViewById(R.id.tv_show_attendance_state);
            classScoreTv = (TextView) itemView.findViewById(R.id.tv_show_class_score);
            statusTv = (TextView)itemView.findViewById(R.id.tv_show_status) ;
            mItemLinearLayout = (LinearLayout)itemView.findViewById(R.id.layout_item_show);
            latePersonTv = (TextView) itemView.findViewById(R.id.tv_late_person);
            leavePersonTv = (TextView) itemView.findViewById(R.id.tv_leave_person);
            truantPersonTv = (TextView) itemView.findViewById(R.id.tv_truant_person);
            holidayPersonTv = (TextView) itemView.findViewById(R.id.tv_holiday_person);
        }

        private static DisplayHolder newHolder(ViewGroup parent, Context context) {
            View view = LayoutInflater.from(context).inflate(R.layout.list_item_registered_record, parent, false);
            return new DisplayHolder(context, view);
        }
    }
}
