package com.gta.zssx.fun.coursedaily.registercourse.view.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
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
import com.gta.zssx.fun.coursedaily.registercourse.model.bean.StudentListNewBean;

import java.util.ArrayList;
import java.util.List;

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
public class StudentStateAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private List<SectionBean> mSectionBeanList;
    private StudentListBean mStudentListBean;
//    private StudentListNewBean mStudentListBean;

    public void updateStudentList(int position) {
        List<StudentListNewBean> studentListNewBeanList = ClassDataManager.getDataCache().getStudentList();
        List<StudentListBean> lStudentList = new ArrayList<>();
        for(int i = 0;i < studentListNewBeanList.size();i++){
            //TODO Bean转换
        }
        lStudentList.set(position, mStudentListBean);
    }

    public interface Listener {
        void itemClick();
    }

    private Listener mListener;

    public StudentStateAdapter(Context context, List<SectionBean> sectionBeanList, Listener listener, StudentListBean studentListBean) {
        mContext = context;
        mSectionBeanList = sectionBeanList;
        mListener = listener;
        mStudentListBean = studentListBean;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return StudentStateHolder.newHolder(parent, mContext);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        StudentStateHolder lHolder = (StudentStateHolder) holder;
        final SectionBean lSectionBean = mSectionBeanList.get(position);

        lHolder.mTextView.setText(lSectionBean.getLesson());

        final int lSectionID = lSectionBean.getSectionId();
        StudentStateInnerAdapter.Listener lListener = new StudentStateInnerAdapter.Listener() {
            @Override
            public void itemClick(StateBean stateBean) {

                /**
                 * 根据点击状态更新学生的上课状态
                 */
                switch (lSectionID) {
                    case 1:
                        mStudentListBean.setLESSON1(stateBean.getStateCode());
                        break;
                    case 2:
                        mStudentListBean.setLESSON2(stateBean.getStateCode());
                        break;
                    case 3:
                        mStudentListBean.setLESSON3(stateBean.getStateCode());
                        break;
                    case 4:
                        mStudentListBean.setLESSON4(stateBean.getStateCode());
                        break;
                    case 5:
                        mStudentListBean.setLESSON5(stateBean.getStateCode());
                        break;
                    case 6:
                        mStudentListBean.setLESSON6(stateBean.getStateCode());
                        break;
                    case 7:
                        mStudentListBean.setLESSON7(stateBean.getStateCode());
                        break;
                    default:
                        break;
                }
            }
        };
        int lessonState = StudentListBean.getLessonState(lSectionID, mStudentListBean);

        StudentStateInnerAdapter lStateInnerAdapter = new StudentStateInnerAdapter(StateBean.getStateList(), mContext,
                lListener, lessonState);
        lHolder.mRecyclerView.setAdapter(lStateInnerAdapter);
    }


    @Override
    public int getItemCount() {
        return mSectionBeanList.size();
    }

    private static class StudentStateHolder extends RecyclerView.ViewHolder {
        TextView mTextView;
        RecyclerView mRecyclerView;

        public StudentStateHolder(Context context, View itemView) {
            super(itemView);
            mTextView = (TextView) itemView.findViewById(R.id.section_header_tv);
            mRecyclerView = (RecyclerView) itemView.findViewById(R.id.student_state_item_rv);

            mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        }

        private static StudentStateHolder newHolder(ViewGroup parent, Context context) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_student_state, parent, false);
            return new StudentStateHolder(context, view);
        }
    }
}
