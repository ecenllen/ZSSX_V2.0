package com.gta.zssx.fun.coursedaily.registercourse.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gta.zssx.R;
import com.gta.zssx.fun.coursedaily.registercourse.model.bean.DetailItemShowBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lan.zheng on 2017/3/9.
 *
 */
public class MyCourseAdapter extends RecyclerView.Adapter{
    private List<DetailItemShowBean.CourseInfoBean> mCourseInfoBeenList = new ArrayList<>();
    private Listener mListener;
    private Context mContext;

    public MyCourseAdapter(Context context,List<DetailItemShowBean.CourseInfoBean> courseInfoBeenList, Listener listener){
        mCourseInfoBeenList = courseInfoBeenList;
        mListener = listener;
        mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return MyCourseHolder.newHolder(parent, mContext);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MyCourseHolder myCourseHolder = (MyCourseHolder) holder;
        DetailItemShowBean.CourseInfoBean courseInfoBean = mCourseInfoBeenList.get(position);
        myCourseHolder.mTextView.setText(courseInfoBean.getCourseName());
        myCourseHolder.mTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mListener != null){
                    mListener.itemClickListener(courseInfoBean);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mCourseInfoBeenList.size();
    }

    private static class MyCourseHolder extends RecyclerView.ViewHolder {
        TextView mTextView;

        public MyCourseHolder( View itemView) {
            super(itemView);
            mTextView = (TextView) itemView.findViewById(R.id.tv_my_course_content);
        }

        private static MyCourseHolder newHolder(ViewGroup parent, Context context) {
            View view = LayoutInflater.from(context).inflate(R.layout.list_item_my_course_content, parent, false);
            return new MyCourseHolder( view);
        }
    }

    public interface Listener {
        void itemClickListener(DetailItemShowBean.CourseInfoBean courseInfoBean);
    }
}
