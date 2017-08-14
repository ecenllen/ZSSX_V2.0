package com.gta.zssx.dormitory.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.gta.zssx.R;
import com.gta.zssx.dormitory.model.bean.OptionItemBean;
import com.gta.zssx.pub.common.Constant;
import com.gta.zssx.pub.util.StringUtils;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * [Description]
 * <p> 宿舍管理-班级维度或宿舍维度-不录入姓名-有或无选项设置-选项适配器
 * [How to use]
 * <p>
 * [Tips]
 *
 * @author Created by Weiye.Chen on 2017/07/24.
 * @since 2.0.0
 */
public class DormitoryOptionAdapter extends RecyclerView.Adapter<DormitoryOptionAdapter.ViewHolder> {

    private LayoutInflater mInflater;
    private Context mContext;
    private ArrayList<OptionItemBean> list;
    private OnItemClickListener listener;
    private boolean isCanEdit;
    /**
     * 宿舍得分
     */
    private float mDormitoryScore;
    /*增分或扣分*/
    private int isAddOrSub;

    public DormitoryOptionAdapter(Context context, OnItemClickListener listener) {
        this.mContext = context;
        mInflater = LayoutInflater.from(context);
        this.listener = listener;
    }

    public void setCanEdit(boolean canEdit) {
        isCanEdit = canEdit;
    }

    public void setIsAddOrSub(int isAddOrSub) {
        this.isAddOrSub = isAddOrSub;
    }

    public void setList(ArrayList<OptionItemBean> list) {
        this.list = list;
        mDormitoryScore = 0;
        notifyDataSetChanged();
    }

    public ArrayList<OptionItemBean> getList() {
        return list;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = mInflater.inflate(R.layout.adapter_item_option, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DormitoryOptionAdapter.ViewHolder holder, int position) {
        holder.position = position;
        OptionItemBean optionItemBean = list.get(position);
        holder.tvName.setText(optionItemBean.getItemName());
        holder.tvScore.setText(StringUtils.formatScore(String.valueOf(optionItemBean.getItemScoreSet())));
        if(!isCanEdit) { // 不能编辑
            holder.checkBox.setButtonDrawable(R.drawable.checkbox_selecter_gray);
        }
        if(isAddOrSub == Constant.Addition) // 增分或扣分
            holder.tvMinus.setText("+");
        else
            holder.tvMinus.setText(mContext.getString(R.string.string_minus));
        if (optionItemBean.isItemCheck()) {
            holder.checkBox.setChecked(true);
            mDormitoryScore += optionItemBean.getItemScoreSet(); // 累加已勾选选项的分数,注意：此方法只适用于recyclerView 没有内存复用情况
        } else {
            holder.checkBox.setChecked(false);
        }
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        int position;
        @Bind(R.id.tv_option_item_name)
        TextView tvName;
        @Bind(R.id.tv_option_item_score)
        TextView tvScore;
        @Bind(R.id.cb_option_checkbox)
        CheckBox checkBox;
        @Bind(R.id.tv_minus)
        TextView tvMinus;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            if (isCanEdit && !itemView.hasOnClickListeners())
                itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
                checkBox.setChecked(!checkBox.isChecked());
                list.get(position).setItemCheck(checkBox.isChecked());
                if (checkBox.isChecked()) {
                    mDormitoryScore += list.get(position).getItemScoreSet();
                } else {
                    mDormitoryScore -= list.get(position).getItemScoreSet();
                }
                String afterText;
                if(hasCheckedItem()) {
                    afterText = (float)(Math.round(mDormitoryScore*1000))/1000 + "";
                } else
                    afterText = (float)(Math.round(mDormitoryScore*1000))/1000 == 0 ? "" : (float)(Math.round(mDormitoryScore*1000))/1000 + "";
            if (listener != null) {
                listener.onItemClick(position, afterText, list.get(position), checkBox.isChecked());
            }
        }

        /**
         * 判断是否有选项选中
         * @return boolean
         */
        private boolean hasCheckedItem() {
            for(OptionItemBean item : list) {
                if(item.isItemCheck()){
                    return true;
                }
            }
            return false;
        }

    }

    public interface OnItemClickListener {
        void onItemClick(int position, String dormitoryScore, OptionItemBean optionItemBean, boolean isChecked);
    }

}
