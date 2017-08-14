package com.gta.zssx.dormitory.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gta.zssx.R;
import com.gta.zssx.dormitory.model.bean.DetailItemBean;
import com.gta.zssx.pub.common.Constant;
import com.gta.zssx.pub.util.StringUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * [Description]
 * 录入姓名多项每个学生下的指标项选项
 * [How to use]
 * <p>
 * [Tips]
 * <p>
 * Created by lan.zheng on 2017/7/25 11:30.
 */

public class EnterNameMultipleStudentDetailItemAdapter extends RecyclerView.Adapter {
    private Context mContext;
    private List<DetailItemBean> mDetailItemBeanList = new ArrayList<>();
    private TextView scoreTextView;  //上层分数
    private TextView minusTextView; //上层减号
    //    private TextView studentNameTextView; //上层学生姓名
    private int mGroupPosition;
    private float indexTotalScore;
    private Listener mListener;   //这个适配器Listener
    private Set<Integer> mCheckList;
    private boolean mIsCanEdit;
    private int mAdditionOrSubtraction;

    /**
     * 适配器
     */
    public EnterNameMultipleStudentDetailItemAdapter(Context context, List<DetailItemBean> detailItemBeanList, Set<Integer> checkList, int groupPosition,
                                                     float nowScore, boolean isCanEdit, int additionOrSubtraction, Listener listener) {
        mContext = context;
        mDetailItemBeanList.clear();
        mDetailItemBeanList.addAll(detailItemBeanList);
        mCheckList = new HashSet<>();
        mCheckList.addAll(checkList);  //一定要用AddAll方法，不能用mCheckList = checkList;会导致错乱
        mGroupPosition = groupPosition;
        indexTotalScore = nowScore;
        mIsCanEdit = isCanEdit;
        mAdditionOrSubtraction = additionOrSubtraction;
        mListener = listener;
    }

    private float setScore() {
        float score = 0f;
        boolean isHaveCheckItem = false;
        for (int i = 0; i < mDetailItemBeanList.size(); i++) {
            if (mDetailItemBeanList.get(i).getDetailItemCheck()) {
                isHaveCheckItem = true;
                score += Float.parseFloat(mDetailItemBeanList.get(i).getDetailItemScoreSet());
            }
        }
        float f = (float) (Math.round(score * 1000)) / 1000;  //防止错乱
        if (isHaveCheckItem)
            return f;
        else
            return -1;
    }


    public void setScoreTextView(TextView scoreTextView) {
        this.scoreTextView = scoreTextView;
    }

    public void setMinusTextView(TextView minusTextView) {
        this.minusTextView = minusTextView;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_item_enter_name_multiple_student_detail_item, parent, false);
        return new DisplayHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        DisplayHolder displayHolder = (DisplayHolder) holder;

        //防止recycleview复用带来的checkbox选中状态错乱,代码中不能设置Checkbox的setClickable(true),否则会出现和整个Layout的监听冲突
        displayHolder.checkBox.setTag(new Integer(position));

        DetailItemBean detailItemBean = mDetailItemBeanList.get(position);
        displayHolder.indexNameTv.setText(detailItemBean.getDetailItemName() == null ? "" : detailItemBean.getDetailItemName());
        String score = detailItemBean.getDetailItemScoreSet();
        displayHolder.indexScoreTv.setText(StringUtils.formatScore(score));

        if (mIsCanEdit) {
            displayHolder.checkBox.setButtonDrawable(R.drawable.checkbox_selector);
        } else {
            displayHolder.checkBox.setButtonDrawable(R.drawable.checkbox_selecter_gray);
        }

        if (mCheckList.contains(position)) {  //包含说明要勾选，不包含说明不用勾选
            displayHolder.checkBox.setChecked(true);
        } else {
            displayHolder.checkBox.setChecked(false);
        }

        if(mAdditionOrSubtraction == Constant.Subtraction) {
            displayHolder.tvMinus.setVisibility(View.VISIBLE);
        } else {
            displayHolder.tvMinus.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return mDetailItemBeanList == null ? 0 : mDetailItemBeanList.size();
    }

    private class DisplayHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView indexNameTv;
        TextView indexScoreTv;
        CheckBox checkBox;
        RelativeLayout mRelativeLayout;
        private TextView tvMinus;

        public DisplayHolder(View itemView) {
            super(itemView);
            indexNameTv = (TextView) itemView.findViewById(R.id.tv_student_item_name);
            indexScoreTv = (TextView) itemView.findViewById(R.id.tv_student_score_show);
            checkBox = (CheckBox) itemView.findViewById(R.id.cb_option_checkbox);
            tvMinus = (TextView) itemView.findViewById(R.id.tv_minus);
            mRelativeLayout = (RelativeLayout) itemView.findViewById(R.id.layout_student_detail_item);
            if (!mRelativeLayout.hasOnClickListeners() && mIsCanEdit) {
                mRelativeLayout.setOnClickListener(this);
            }
        }

        @Override
        public void onClick(View v) {
            //TODO 查看进入的无法点击
//                if(!mIsCanEdit){
//                    boolean lChecked = displayHolder.checkBox.isGroupChecked();
//                    displayHolder.checkBox.setGroupChecked(lChecked);
//                    return;
//                }
            int position = getLayoutPosition();
            boolean lChecked = checkBox.isChecked();
            checkBox.setChecked(!lChecked);  //设置状态
            if (checkBox.isChecked()) {
                if (!mCheckList.contains(checkBox.getTag())) {
                    mCheckList.add(new Integer(getLayoutPosition()));
                }
                //TODO 加上勾选分，并告知上一层分数改变
                mDetailItemBeanList.get(position).setDetailItemCheck(true);
                indexTotalScore = setScore();
                mListener.addClickListener(mGroupPosition, position, indexTotalScore, mCheckList, true);
            } else {
                if (mCheckList.contains(checkBox.getTag())) {
                    mCheckList.remove(new Integer(getLayoutPosition()));
                }
                //TODO 减去勾选分，并告知上一层分数改变
                mDetailItemBeanList.get(position).setDetailItemCheck(false);
                indexTotalScore = setScore();
                mListener.addClickListener(mGroupPosition, position, indexTotalScore, mCheckList, false);
            }
            //TODO 直接在这里附外面的分数值
            if (indexTotalScore >= 0) {
                String score = String.valueOf(indexTotalScore);
                scoreTextView.setText(StringUtils.formatScore(score));
            } else {
                scoreTextView.setText("");
            }
            //TODO 是否显示“-”号
            if (mAdditionOrSubtraction == Constant.Subtraction) {
                if (indexTotalScore >= 0) {
                    minusTextView.setVisibility(View.VISIBLE);
                } else {
                    minusTextView.setVisibility(View.GONE);
                }
            } else {
                minusTextView.setVisibility(View.GONE);
            }
        }
    }

    public interface Listener {
        void addClickListener(int groupPosition, int childPosition, float newScore, Set<Integer> checkList, boolean isCheck);
    }
}
