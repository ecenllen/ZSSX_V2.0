package com.gta.zssx.fun.coursedaily.registercourse.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gta.zssx.R;
import com.gta.zssx.fun.coursedaily.registercourse.model.ClassDataManager;
import com.gta.zssx.fun.coursedaily.registercourse.model.bean.SectionBean;
import com.gta.zssx.fun.coursedaily.registercourse.model.bean.StateBean;
import com.gta.zssx.fun.coursedaily.registercourse.model.bean.StudentListBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * [Description]
 * <p>
 * [How to use]
 * <p>
 * [Tips]
 *
 * @author Created by Zhimin.Huang on 2016/6/20.
 * @since 1.0.0
 */
public class StudentListAdapter extends RecyclerView.Adapter {

    public void refreshData(List<StudentListBean> studentList, int position) {
//        mStudentListBeans.clear();
//        mStudentListBeans.addAll(studentList);
//        notifyItemRangeChanged(0, mStudentListBeans.size());
        notifyItemChanged(position);
//        notifyDataSetChanged();
    }

    public interface Listener {
        void itemClick(StudentListBean studentListBean, int position);
    }

    private Context mContext;

    private List<StudentListBean> mStudentListBeans;

    private Listener mListener;

    public StudentListAdapter(Context context, List<StudentListBean> studentListBeans, Listener listener) {
        mContext = context;
        mStudentListBeans = studentListBeans;
        mListener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return StudentHolder.newHolder(parent, mContext);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final StudentListBean lStudentListBean = mStudentListBeans.get(position);
        StudentHolder lStudentHolder = (StudentHolder) holder;

        Set<SectionBean> lSection = ClassDataManager.getDataCache().getSection();
        List<SectionBean> lSectionBeen = new ArrayList<>(lSection);
        List<Integer> lIntegers = new ArrayList<>();

        /**
         * 计算每个学生的上课状态
         * 若教师选择多节次登记，同一学生不同课时的考勤状态不同

         （1）当考勤状态都为异常时，则此页面优先显示第1节课的考勤状态。
         举例：老师登记1、2、3节课，蔡晓雯第1节迟到，第2、3节旷课，则此页面考勤状态显示为迟到；

         （2）当考勤状态为正常、异常都有时，则此页面显示异常考勤状态。
         举例：老师登记1、2、3、4节课，蔡晓雯第一节迟到，第2、3、4节正常，则此页面考勤状态显示为迟到；

         （3）当多节课考勤状态相同时，则显示考勤状态即可。
         举例：老师登记1、2节课，蔡晓雯均为公假，则此页面考勤状态显示为公假。
         *
         */
        for (int i = 0; i < lSectionBeen.size(); i++) {
            int lSectionId = lSectionBeen.get(i).getSectionId();
            switch (lSectionId) {
                case 1:
                    lIntegers.add(lStudentListBean.getLESSON1());
                    break;
                case 2:
                    lIntegers.add(lStudentListBean.getLESSON2());
                    break;
                case 3:
                    lIntegers.add(lStudentListBean.getLESSON3());
                    break;
                case 4:
                    lIntegers.add(lStudentListBean.getLESSON4());
                    break;
                case 5:
                    lIntegers.add(lStudentListBean.getLESSON5());
                    break;
                case 6:
                    lIntegers.add(lStudentListBean.getLESSON6());
                    break;
                case 7:
                    lIntegers.add(lStudentListBean.getLESSON7());
                    break;
                default:
                    break;
            }
        }
        int sum = 0;
        for (int i = 0; i < lIntegers.size(); i++) {
            sum = sum + lIntegers.get(i);
        }
        String state = StateBean.NORMAL_S;
        if (lIntegers.size() > 0) {
            if (sum > lIntegers.size()) {
                if (lIntegers.contains(1)) {
                    state = mContext.getString(R.string.unusual_state);
                } else {
                    state = StateBean.getState(lIntegers.get(0));
                }
            } else {
                state = StateBean.getState(lIntegers.get(0));
            }
        }

        lStudentHolder.classState.setText(state);
        if (!state.equals(StateBean.NORMAL_S)) {
            lStudentHolder.classState.setTextColor(mContext.getResources().getColor(R.color.red_ff0000));
        } else {
            lStudentHolder.classState.setTextColor(mContext.getResources().getColor(R.color.black_323232));
        }

        lStudentHolder.studentName.setText(lStudentListBean.getStundentName());
        lStudentHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.itemClick(lStudentListBean, position);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return mStudentListBeans.size();
    }

    private static class StudentHolder extends RecyclerView.ViewHolder {
        TextView studentName;
        TextView classState;

        public StudentHolder(Context context, View itemView) {
            super(itemView);
            studentName = (TextView) itemView.findViewById(R.id.student_name_tv);
            classState = (TextView) itemView.findViewById(R.id.class_state_tv);
        }

        private static StudentHolder newHolder(ViewGroup parent, Context context) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_student_list, parent, false);
            return new StudentHolder(context, view);
        }
    }
}
