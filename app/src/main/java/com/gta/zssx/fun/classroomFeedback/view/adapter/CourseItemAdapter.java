package com.gta.zssx.fun.classroomFeedback.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gta.zssx.R;
import com.gta.zssx.fun.classroomFeedback.model.bean.RegisterPageBean;

import java.util.List;

/**
 * [Description]
 * <p> 课程adapter
 * [How to use]
 * <p>
 * [Tips]
 *
 * @author Created by liang.lu on 2017/06/20.
 * @since 2.0.0
 */
public class CourseItemAdapter extends RecyclerView.Adapter {
    private Context mContext;
    private List<RegisterPageBean.ClassroomRegisterListBean.SectionDataListBean.CourseListBean> courseListBeen;
    private DeleteCourseListener listener;

    public CourseItemAdapter (Context context, DeleteCourseListener listener) {
        super ();
        this.mContext = context;
        this.listener = listener;
    }

    public void setCourseListBeen (List<RegisterPageBean.ClassroomRegisterListBean.SectionDataListBean.CourseListBean> courseListBeen) {
        this.courseListBeen = courseListBeen;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {
        return CourseItemHolder.establish (parent, mContext, listener);
    }

    @Override
    public void onBindViewHolder (RecyclerView.ViewHolder holder, int position) {
        CourseItemHolder mHolder = (CourseItemHolder) holder;
        mHolder.mCourseNameTv.setText (courseListBeen.get (position).getCourseName ());
    }

    @Override
    public int getItemCount () {
        return courseListBeen.size () == 0 ? 0 : courseListBeen.size ();
    }

    private static class CourseItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView mCourseNameTv;
        ImageView mDeleteIv;
        DeleteCourseListener listener;

        CourseItemHolder (View itemView, Context context, DeleteCourseListener listener) {
            super (itemView);
            this.listener = listener;
            mCourseNameTv = (TextView) itemView.findViewById (R.id.tv_course_name);
            mDeleteIv = (ImageView) itemView.findViewById (R.id.iv_delete);
            mDeleteIv.setOnClickListener (this);
        }

        private static CourseItemHolder establish (ViewGroup parent, Context context, DeleteCourseListener listener) {
            View view = LayoutInflater.from (context).inflate (R.layout.adapter_item_classroom_course, parent, false);
            return new CourseItemHolder (view, context, listener);
        }

        @Override
        public void onClick (View v) {
            listener.clickDeleteCourseItem (getLayoutPosition ());
        }
    }

    public interface DeleteCourseListener {
        void clickDeleteCourseItem (int position);
    }
}
