package com.gta.zssx.dormitory.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.gta.zssx.R;
import com.gta.zssx.dormitory.model.bean.EnterNameSingleItemBean;
import com.gta.zssx.dormitory.model.bean.OptionItemBean;
import com.gta.zssx.pub.common.Constant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * [Description]
 * <p> 录入姓名单项 - 宿舍维度或者班级维度
 * [How to use]
 * <p>
 * [Tips]
 *
 * @author Created by Weiye.Chen on 2017/7/22.
 * @since 2.0.0
 */
public class SingleItemStudentListAdapter extends RecyclerView.Adapter<SingleItemStudentListAdapter.DisplayHolder> {

    private Context mContext;
    private List<EnterNameSingleItemBean> mEnterNameSingleItemBeanList = new ArrayList<>();
    private OnItemClickListener listener;
    /**
     * 1 为宿舍，2 为班级
     */
    private int isDormitoryOrClass;
    /**
     * 是否能编辑分数
     */
    private boolean isCanEditScore;
    /**
     * 宿舍得分
     */
    private float mDormitoryScore;
    /**
     * 选中同一班级的学生，分数累加
     */
    private HashMap<Integer, String> selectedStuScoreSumMap = new HashMap<>();

    public SingleItemStudentListAdapter(Context context, OnItemClickListener listener) {
        this.listener = listener;
        mContext = context;
    }

    public void setDormitoryOrClass(int dormitoryOrClass) {
        isDormitoryOrClass = dormitoryOrClass;
        selectedStuScoreSumMap.clear();
    }

    public void setCanEditScore(boolean canEditScore) {
        isCanEditScore = canEditScore;
    }

    public void setmEnterNameSingleItemBeanList(List<EnterNameSingleItemBean> mEnterNameSingleItemBeanList) {
        this.mEnterNameSingleItemBeanList = mEnterNameSingleItemBeanList;
        mDormitoryScore = 0;
        selectedStuScoreSumMap.clear();
        notifyDataSetChanged();
    }


    @Override
    public DisplayHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (isDormitoryOrClass == Constant.DimensionType_Dormitory) { // 宿舍维度
            View mItemView = LayoutInflater.from(mContext).inflate(R.layout.list_item_enter_name_single_item_dormitory, parent, false);
            return new DisplayHolder(mItemView);
        } else { //  班级维度
            View mItemView = LayoutInflater.from(mContext).inflate(R.layout.list_item_enter_name_single_item_class, parent, false);
            return new DisplayHolder(mItemView);
        }
    }

    @Override
    public void onBindViewHolder(DisplayHolder holder, int position) {
        EnterNameSingleItemBean itemBean = mEnterNameSingleItemBeanList.get(position);
        holder.studentNameTv.setText(itemBean.getStudentName());
        if (!isCanEditScore) {  //  不能编辑，只查看状态
            holder.checkBox.setButtonDrawable(R.drawable.checkbox_selecter_gray);
        } else {
            if (isDormitoryOrClass == Constant.DimensionType_Dormitory) { // 宿舍维度，学生床号赋值
                if (itemBean.getStudentId() == 0) { // 空床位，没学生睡的
                    holder.checkBox.setButtonDrawable(R.drawable.checkbox_selecter_gray);
                    holder.itemView.setClickable(false);
                } else {
                    holder.checkBox.setButtonDrawable(R.drawable.checkbox_selector);
                    holder.itemView.setClickable(true);
                }
                holder.studentBedTv.setText(String.valueOf(itemBean.getBedName()));
            } else  // 班级维度，学生学号赋值
                holder.studentNoTv.setText(String.valueOf(itemBean.getStudentNo()));
        }
        if (itemBean.getStudentCheck()) {
            holder.checkBox.setChecked(true);
            if (!itemBean.getStudentScore().isEmpty()) {
                mDormitoryScore += Float.valueOf(itemBean.getStudentScore());
                int classKey = itemBean.getClassId();
                if (selectedStuScoreSumMap.get(classKey) != null) {
                    selectedStuScoreSumMap.put(classKey, (Float.valueOf(selectedStuScoreSumMap.get(classKey)) + Float.valueOf(itemBean.getStudentScore()))+"");
                } else
                    selectedStuScoreSumMap.put(classKey, itemBean.getStudentScore());
            }
        } else {
            holder.checkBox.setChecked(false);
        }
    }

    @Override
    public int getItemCount() {
        return mEnterNameSingleItemBeanList == null ? 0 : mEnterNameSingleItemBeanList.size();
    }


    class DisplayHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView studentBedTv;
        TextView studentNameTv;
        TextView studentNoTv;
        CheckBox checkBox;
        View itemView;

        public DisplayHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            if (isCanEditScore && !itemView.hasOnClickListeners())
                itemView.setOnClickListener(this);
            if (isDormitoryOrClass == Constant.DimensionType_Dormitory) // 宿舍维度 - 单项
                studentBedTv = (TextView) itemView.findViewById(R.id.tv_option_student_bed);
            else  // 班级维度-单项
                studentNoTv = (TextView) itemView.findViewById(R.id.tv_option_student_no);
            studentNameTv = (TextView) itemView.findViewById(R.id.tv_option_student_name);
            checkBox = (CheckBox) itemView.findViewById(R.id.cb_option_checkbox);
        }

        @Override
        public void onClick(View v) {
            checkBox.setChecked(!checkBox.isChecked());
            mEnterNameSingleItemBeanList.get(getLayoutPosition()).setStudentCheck(checkBox.isChecked());
            int classKey = mEnterNameSingleItemBeanList.get(getLayoutPosition()).getClassId();
            String studentScore = mEnterNameSingleItemBeanList.get(getLayoutPosition()).getStudentScore();
            if (!studentScore.isEmpty()) {
                if (checkBox.isChecked()) {  // 选中，则加分处理
                    mDormitoryScore += Float.valueOf(studentScore);
                    mDormitoryScore = (float) (Math.round(mDormitoryScore * 1000)) / 1000;
                    if (selectedStuScoreSumMap.get(classKey) != null && !selectedStuScoreSumMap.get(classKey).isEmpty()) {
                        float f = (float) (Math.round(( Float.valueOf(selectedStuScoreSumMap.get(classKey)) + Float.valueOf(studentScore)) * 1000)) / 1000;
                        if(f == 0)
                            selectedStuScoreSumMap.put(classKey, "");
                        else
                            selectedStuScoreSumMap.put(classKey, f + "");
                    } else {
                        selectedStuScoreSumMap.put(classKey, studentScore);
                    }
                } else {  // 反选，减分处理
                    mDormitoryScore -= Float.valueOf(studentScore);
                    mDormitoryScore = (float) (Math.round(mDormitoryScore * 1000)) / 1000;
                    if (selectedStuScoreSumMap.get(classKey) != null && !selectedStuScoreSumMap.get(classKey).isEmpty()) {
                        float f = (float) (Math.round(( Float.valueOf(selectedStuScoreSumMap.get(classKey)) - Float.valueOf(studentScore)) * 1000)) / 1000;
                        if(f <= 0)
                            selectedStuScoreSumMap.put(classKey, "");
                        else
                            selectedStuScoreSumMap.put(classKey, f + "");
                    }
                }
            }
            String afterText;
            if(hasCheckedItem()) {
                afterText = mDormitoryScore + "";
            } else
                 afterText = mDormitoryScore == 0 ? "" : (mDormitoryScore + "");
            if (listener != null) {
                listener.onItemClick(getAdapterPosition(), afterText, selectedStuScoreSumMap, checkBox.isChecked());
            }
        }

        private boolean hasCheckedItem() {
            for(EnterNameSingleItemBean item : mEnterNameSingleItemBeanList) {
                if(item.getStudentCheck()){
                    return true;
                }
            }
            return false;
        }
    }

    public interface OnItemClickListener {
        /**
         * @param position               位置
         * @param dormitoryScore         根据勾选，自动计算出宿舍得分
         * @param selectedStuScoreSumMap 已选择同班学生分数累加集合，key  为班级ID
         * @param isChecked              选项是否被选中
         */
        void onItemClick(int position, String dormitoryScore, HashMap<Integer, String> selectedStuScoreSumMap, boolean isChecked);
    }
}
