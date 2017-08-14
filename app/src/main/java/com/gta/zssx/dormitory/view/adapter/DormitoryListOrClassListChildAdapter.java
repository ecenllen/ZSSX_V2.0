/*
 * Copyright 2014 Eduardo Barrenechea
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.gta.zssx.dormitory.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gta.utils.view.CircleImageView;
import com.gta.zssx.R;
import com.gta.zssx.dormitory.model.bean.LevelList;
import com.gta.zssx.pub.common.Constant;
import com.gta.zssx.pub.util.StringUtils;

import java.util.List;


/**
 * [Description]
 * <p> 宿舍列表/班级列表外部适配器中的子适配器
 * [How to use]
 * <p>
 * [Tips]
 *
 * @author Create by Weiye.Chen on 2017/7/21.
 * @since 2.0.0
 */
public class DormitoryListOrClassListChildAdapter extends RecyclerView.Adapter<DormitoryListOrClassListChildAdapter.ViewHolder> {
    private int registered = 1;

    private LayoutInflater mInflater;
    private List<LevelList> list;
    private Context mContext;
    private OnItemClickListener listener;
    private int mParentPosition;
    private int DimensionType;
    /** 1 增分， 2 扣分*/
    private int isAdditionOrSubtraction;

    public DormitoryListOrClassListChildAdapter(Context context, List<LevelList> list, OnItemClickListener listener, int parentPosition) {
        mInflater = LayoutInflater.from(context);
        this.list = list;
        this.listener = listener;
        this.mContext = context;
        this.mParentPosition = parentPosition;
    }

    public void setIsAdditionOrSubtraction(int isAdditionOrSubtraction) {
        this.isAdditionOrSubtraction = isAdditionOrSubtraction;
    }

    public void setDimensionType(int dimensionType) {
        DimensionType = dimensionType;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        if (DimensionType == Constant.DimensionType_Dormitory) {
            View view = mInflater.inflate(R.layout.adapter_item_dormitory_list_inner, viewGroup, false);
            return new ViewHolder(view);
        } else {
            View view = mInflater.inflate(R.layout.adapter_item_class_list_inner, viewGroup, false);
            return new ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        viewHolder.position = i;
        LevelList level = list.get(i);
        viewHolder.tvName.setText(level.getName());
//        if(level.getScore().isEmpty())
//            viewHolder.tvScore.setText("");
//        else
        viewHolder.tvScore.setText(StringUtils.formatScore(level.getScore()));
        if (level.getStatus() == registered) { // 已登记
            if (DimensionType == Constant.DimensionType_Dormitory) { // 宿舍
                viewHolder.circleImageView.setImageResource(R.color.bg_dormitory_class_list_s);
                viewHolder.tvScore.setTextColor(mContext.getResources().getColor(R.color.text_dormitory_score_s));
                viewHolder.tvMinus.setTextColor(mContext.getResources().getColor(R.color.text_dormitory_score_s));
            } else {
                viewHolder.tvName.setBackgroundResource(R.color.bg_dormitory_class_list_s);
                viewHolder.tvScore.setTextColor(mContext.getResources().getColor(R.color.main_color));
                viewHolder.tvMinus.setTextColor(mContext.getResources().getColor(R.color.main_color));
            }
        } else {    // 未登记
            if (DimensionType == Constant.DimensionType_Dormitory) { // 宿舍
                viewHolder.circleImageView.setImageResource(R.color.textColor_gray);
            } else {
                viewHolder.tvName.setBackgroundResource(R.color.textColor_gray);
            }
            viewHolder.tvScore.setTextColor(mContext.getResources().getColor(R.color.textColor_gray));
            viewHolder.tvMinus.setTextColor(mContext.getResources().getColor(R.color.textColor_gray));
        }
        if(isAdditionOrSubtraction == Constant.Addition) {
            viewHolder.tvMinus.setVisibility(View.GONE);
        } else {
            if(TextUtils.isEmpty(viewHolder.tvScore.getText().toString()) || Float.valueOf(viewHolder.tvScore.getText().toString()) == 0) {
                viewHolder.tvMinus.setVisibility(View.GONE);
            } else
                viewHolder.tvMinus.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private int position;
        private TextView tvScore;
        private TextView tvName;
        private CircleImageView circleImageView;
        private TextView tvMinus;

        public ViewHolder(View itemView) {
            super(itemView);
            tvScore = (TextView) itemView.findViewById(R.id.tv_dormitory_class_score);
            tvName = (TextView) itemView.findViewById(R.id.tv_dormitory_class_name);
            tvMinus = (TextView) itemView.findViewById(R.id.tv_minus);
            if (DimensionType == Constant.DimensionType_Dormitory) { // 宿舍列表
                circleImageView = (CircleImageView) itemView.findViewById(R.id.civ_dormitory);
            }
            if (!itemView.hasOnClickListeners())
                itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (listener != null) {
                listener.onItemClick(mParentPosition, position, list.get(position).getId());
            }
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int parentPosition, int childPosition, int id);
    }

}
