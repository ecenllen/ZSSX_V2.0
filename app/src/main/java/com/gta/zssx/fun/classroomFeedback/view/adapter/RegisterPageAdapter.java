package com.gta.zssx.fun.classroomFeedback.view.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gta.zssx.R;
import com.gta.zssx.fun.classroomFeedback.model.bean.RegisterPageBean;
import com.gta.zssx.pub.util.TimeUtils;

import java.util.List;

/**
 * [Description]
 * <p> 登记页面 Adapter
 * [How to use]
 * <p>
 * [Tips]
 *
 * @author Created by liang.lu on 2017/06/20.
 * @since 2.0.0
 */
public class RegisterPageAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private List<RegisterPageBean.ClassroomRegisterListBean.SectionDataListBean> sectionDataListBeen;
    private RegisterPageItemListener listener;

    public void setListener (RegisterPageItemListener listener) {
        this.listener = listener;
    }

    public RegisterPageAdapter (Context context) {
        super ();
        mContext = context;
    }

    public void setSectionDataListBeen (List<RegisterPageBean.ClassroomRegisterListBean.SectionDataListBean> sectionDataListBeen) {
        this.sectionDataListBeen = sectionDataListBeen;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {
        return RegisterPageHolder.establish (parent, mContext, listener);
    }

    @Override
    public void onBindViewHolder (RecyclerView.ViewHolder holder, int position) {
        RegisterPageBean.ClassroomRegisterListBean.SectionDataListBean listBean = sectionDataListBeen.get (position);
        RegisterPageHolder mHolder = (RegisterPageHolder) holder;
        mHolder.mSectionTv.setText (listBean.getSection () + "");
        //        mHolder.mCourseNameTv.setText (GetCourseName (listBean));
        mHolder.mCourseNameTv.setText (listBean.getCourseName ());
        mHolder.mTeacherNameTv.setText (listBean.getTeacherName ());
        if (listBean.getScore () == -1) {
            mHolder.mScoreTv.setText ("");
        } else {
            mHolder.mScoreTv.setText (listBean.getScore () + "");
        }
        mHolder.mViewLine.setVisibility (View.GONE);
        mHolder.setItemBackground (mHolder.mLinearLayout, listBean.isSectionComplete ());
        mHolder.setItemColorOrSize (mHolder.mCourseNameTv);
        mHolder.setItemColorOrSize (mHolder.mTeacherNameTv);
        mHolder.setItemColorOrSize (mHolder.mScoreTv);
    }

    private static class RegisterPageHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mSectionTv;
        private TextView mCourseNameTv;
        private TextView mTeacherNameTv;
        private TextView mScoreTv;
        private View mViewLine;
        private LinearLayout mLinearLayout;
        private RegisterPageItemListener listener;
        private Context mContext;

        RegisterPageHolder (View itemView, Context context, RegisterPageItemListener listener) {
            super (itemView);
            this.mContext = context;
            this.listener = listener;
            mSectionTv = (TextView) itemView.findViewById (R.id.tv_section);
            mCourseNameTv = (TextView) itemView.findViewById (R.id.tv_course_name);
            mTeacherNameTv = (TextView) itemView.findViewById (R.id.tv_teacher_name);
            mScoreTv = (TextView) itemView.findViewById (R.id.tv_score);
            mViewLine = itemView.findViewById (R.id.view);
            mLinearLayout = (LinearLayout) itemView.findViewById (R.id.ll_register);
            mLinearLayout.setOnClickListener (this);
        }

        private void setItemBackground (View view, boolean isSectionComplete) {
            LinearLayout layout = (LinearLayout) view;
            if (!isSectionComplete) {
                layout.setBackgroundResource (R.drawable.classroom_selector_item_press_bg_one);
            } else {
                layout.setBackgroundResource (R.drawable.classroom_selector_item_press_bg_two);
            }
        }

        private void setItemColorOrSize (TextView textView) {
            textView.setTextColor (ContextCompat.getColor (mContext, R.color.gray_4c4c4c));
            textView.setTextSize (14);
        }

        private static RegisterPageHolder establish (ViewGroup parent, Context context, RegisterPageItemListener listener) {
            View view = LayoutInflater.from (context).inflate (R.layout.item_classroom_feedback_register_page, parent, false);
            return new RegisterPageHolder (view, context, listener);
        }

        @Override
        public void onClick (View v) {
            if (TimeUtils.isFastDoubleClick ()) {
                return;
            }
            listener.RegisterPageItemClick (getLayoutPosition ());
        }
    }

    //    private String GetCourseName (RegisterPageBean.ClassroomRegisterListBean.SectionDataListBean listBean) {
    //        List<RegisterPageBean.ClassroomRegisterListBean.SectionDataListBean.CourseListBean> courseList = listBean.getCourseList ();
    //        if (courseList == null) {
    //            return "";
    //        } else {
    //            StringBuilder mAddString = new StringBuilder ();
    //            for (int i = 0; i < courseList.size (); i++) {
    //                String courseName;
    //                if (i == 0) {
    //                    courseName = courseList.get (i).getCourseName ();
    //                } else {
    //                    courseName = "," + courseList.get (i).getCourseName ();
    //                }
    //                mAddString.append (courseName);
    //            }
    //            return mAddString.toString ();
    //        }
    //    }

    @Override
    public int getItemCount () {
        return sectionDataListBeen == null ? 0 : sectionDataListBeen.size ();
    }

    public interface RegisterPageItemListener {
        void RegisterPageItemClick (int position);
    }
}
