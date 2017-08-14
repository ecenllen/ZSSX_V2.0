package com.gta.zssx.fun.adjustCourse.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gta.zssx.R;
import com.gta.zssx.fun.adjustCourse.model.bean.ScheduleBean;

import java.util.List;

/**
 * [Description]
 * <p>
 * [How to use]
 * <p>
 * [Tips]
 *
 * @author Created by Zhimin.Huang on 2016/11/9.
 * @since 1.0.0
 */
public class AllMessageAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private List<ScheduleBean.SectionBean> mSectionBeen;

    public AllMessageAdapter(Context context, List<ScheduleBean.SectionBean> sectionBeen) {
        mContext = context;
        mSectionBeen = sectionBeen;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return AddHolder.newHolder(parent, mContext);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        AddHolder lAddHolder = (AddHolder) holder;
        ScheduleBean.SectionBean lSectionBean = mSectionBeen.get(position);
        lAddHolder.mCourseName.setText(lSectionBean.getCourseName());
        lAddHolder.mTeacherName.setText(lSectionBean.getTeacherName());
        lAddHolder.mClassName.setText(lSectionBean.getClassName());
        if (lSectionBean.getOpenCourseType() != 0 && lSectionBean.getOpenCourseType() != 15) {
            lAddHolder.mClassRoomName.setVisibility(View.VISIBLE);
            String lGiveCourseName = lSectionBean.getGiveCourseName();
            if (lGiveCourseName != null && !lGiveCourseName.isEmpty()) {
                lAddHolder.mClassRoomName.setText(lGiveCourseName);
            } else {
                lAddHolder.mClassRoomName.setText(lSectionBean.getOpenCourseTypeName());
            }
        } else {
            lAddHolder.mClassRoomName.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return mSectionBeen.size();
    }

    private static class AddHolder extends RecyclerView.ViewHolder {
        TextView mCourseName;
        TextView mTeacherName;
        TextView mClassRoomName;
        TextView mClassName;

        public AddHolder(Context context, View itemView) {
            super(itemView);
            mCourseName = (TextView) itemView.findViewById(R.id.course_name_tv);
            mTeacherName = (TextView) itemView.findViewById(R.id.teacher_name_tv);
            mClassRoomName = (TextView) itemView.findViewById(R.id.classRoom_name_tv);
            mClassName = (TextView) itemView.findViewById(R.id.class_name_tv);

        }

        private static AddHolder newHolder(ViewGroup parent, Context context) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_all_message, parent, false);
            return new AddHolder(context, view);
        }
    }
}
