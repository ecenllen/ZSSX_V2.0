package com.gta.zssx.fun.coursedaily.registercourse.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gta.zssx.AppConfiguration;
import com.gta.zssx.R;
import com.gta.zssx.fun.coursedaily.registercourse.model.bean.ClassDisplayBean;
import com.gta.zssx.fun.personalcenter.model.bean.UserBean;

import java.util.ArrayList;
import java.util.List;

/**
 * [Description]
 * <p>
 * [How to use]
 * <p>
 * [Tips]
 *
 * @author Created by Zhimin.Huang on 2016/6/15.
 * @since 1.0.0
 */
public class ClassDisplayAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private Listener mListener;
    private List<ClassDisplayBean> mClassDisplayBeans = new ArrayList<>();
    private static int footer_type = 2;
    private static int item_type = 1;
    public UserBean mUserBean;

    public ClassDisplayAdapter(Context context, Listener listener, List<ClassDisplayBean> classDisplayBeans) {
        mContext = context;
        mListener = listener;
        mClassDisplayBeans.clear();
        mClassDisplayBeans.addAll(classDisplayBeans);

    }

    public void updateList(String ClassId) {
        for (int i = 0; i < mClassDisplayBeans.size(); i++) {
            if (String.valueOf(mClassDisplayBeans.get(i).getClassId()).equals(ClassId)) {
                mClassDisplayBeans.remove(mClassDisplayBeans.get(i));
                //显示删除的动画
                notifyItemRemoved(i);
            }
        }
    }

    public interface Listener {

        void itemClick(ClassDisplayBean item);

        void addClick();

        void myClassClick();

        void itemLongClick(ClassDisplayBean classDisplayBean, int position);
    }

    @Override
    public int getItemViewType(int position) {
        if (position < mClassDisplayBeans.size()) {
            return item_type;
        }

        return footer_type;
    }

    public void notifyDelete(int position) {
        if (mClassDisplayBeans != null) {
            mClassDisplayBeans.remove(position);
            notifyItemRemoved(position);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        switch (viewType) {
            default:
            case 1:
                return DisplayHolder.newHolder(parent, mContext);
            case 2:
                return FooterHolder.newHolder(parent, mContext);
        }
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        int lItemViewType = getItemViewType(position);
        if (lItemViewType == item_type) {
            DisplayHolder lHolder = (DisplayHolder) holder;
            ClassDisplayBean lClassDisplayBean = mClassDisplayBeans.get(position);
            lHolder.classNameTv.setText(lClassDisplayBean.getClassName());
            lHolder.departmentNameTv.setText(lClassDisplayBean.getDeptName());
            lHolder.numberTv.setText(String.valueOf(lClassDisplayBean.getStudentCount()));
            lHolder.teacherNameTv.setText(lClassDisplayBean.getTeacherName());

//            if (position == mClassDisplayBeans.size() - 1) {
//                lHolder.addTv.setVisibility(View.VISIBLE);
//            } else {
//                lHolder.addTv.setVisibility(View.GONE);
//            }
//
//            lHolder.addTv.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (mListener != null) {
//
//                    }
//                }
//            });

            lHolder.itemView.setOnClickListener(v -> {
                if (mListener != null) {
                    mListener.itemClick(lClassDisplayBean);
                }
            });

            //TODO 只有一条，需要看看什么问题
            lHolder.itemView.setOnLongClickListener(v -> {
                String className = lClassDisplayBean.getClassName();
                int classId = lClassDisplayBean.getClassId();
                if (mListener != null) {
                    mListener.itemLongClick(lClassDisplayBean, lHolder.getLayoutPosition());
                }
                return true;
            });
        } else {
            FooterHolder lFooterHolder = (FooterHolder) holder;
            try {
                mUserBean = AppConfiguration.getInstance().getUserBean();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (mUserBean != null) {
                if (!mUserBean.isTeacherName()) {
                    lFooterHolder.myClassTv.setVisibility(View.GONE);
                    lFooterHolder.addTv.setVisibility(View.GONE);
                    lFooterHolder.addTv01.setVisibility(View.VISIBLE);
                } else {
                    lFooterHolder.myClassTv.setVisibility(View.VISIBLE);
                    lFooterHolder.addTv.setVisibility(View.VISIBLE);
                    lFooterHolder.addTv01.setVisibility(View.GONE);
                }
            }
            lFooterHolder.addTv.setOnClickListener(v -> mListener.addClick());


            lFooterHolder.myClassTv.setOnClickListener(v -> mListener.myClassClick());

            lFooterHolder.addTv01.setOnClickListener(view -> mListener.addClick());
        }

    }

    @Override
    public int getItemCount() {
        return mClassDisplayBeans.size() + 1;
    }

    private static class DisplayHolder extends RecyclerView.ViewHolder {

        TextView classNameTv;
        TextView departmentNameTv;
        TextView teacherNameTv;
        TextView numberTv;
//        TextView addTv;

        public DisplayHolder(Context context, View itemView) {
            super(itemView);
            classNameTv = (TextView) itemView.findViewById(R.id.class_name_tv);
            departmentNameTv = (TextView) itemView.findViewById(R.id.department_name_tv);
            teacherNameTv = (TextView) itemView.findViewById(R.id.teacher_name_tv);
            numberTv = (TextView) itemView.findViewById(R.id.student_number_tv);
//            addTv = (TextView) itemView.findViewById(R.id.add_tv);
        }

        private static DisplayHolder newHolder(ViewGroup parent, Context context) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_class_display, parent, false);
            return new DisplayHolder(context, view);
        }
    }

    private static class FooterHolder extends RecyclerView.ViewHolder {
        TextView addTv;
        TextView addTv01;
        TextView myClassTv;

        public FooterHolder(Context context, View itemView) {
            super(itemView);
            addTv = (TextView) itemView.findViewById(R.id.add_tv);
            addTv01 = (TextView) itemView.findViewById(R.id.add_tv_01);
            myClassTv = (TextView) itemView.findViewById(R.id.my_class_tv);
        }

        private static FooterHolder newHolder(ViewGroup parent, Context context) {
            View view = LayoutInflater.from(context).inflate(R.layout.footer_class_display, parent, false);
            return new FooterHolder(context, view);
        }
    }
}
