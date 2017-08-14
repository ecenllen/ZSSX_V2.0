package com.gta.zssx.dormitory.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.gta.zssx.R;
import com.gta.zssx.dormitory.model.bean.ClassInfoBean;
import com.gta.zssx.pub.common.Constant;
import com.gta.zssx.pub.util.StringUtils;

import java.util.ArrayList;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * [Description]
 * <p> 宿舍管理-班级维度/宿舍维度-录入姓名-单项设置-班级适配器
 * [How to use]
 * <p>
 * [Tips]
 *
 * @author Created by Weiye.Chen on 2017/07/24.
 * @since 2.0.0
 */
public class SingleItemClassListAdapterV10 extends RecyclerView.Adapter<SingleItemClassListAdapterV10.ViewHolder> {

    private LayoutInflater mInflater;
    private Context mContext;
    private ArrayList<ClassInfoBean> list;
    private OnClassItemTextChangeListener listener;
    /** 默认班级得分*/
    private int defaultClassScore;
    private Map<Integer, String> classScoreMap;
    private boolean isCanEdit;
    private int isAdditionOrSub;


    public SingleItemClassListAdapterV10(Context context, OnClassItemTextChangeListener listener) {
        this.mContext = context;
        mInflater = LayoutInflater.from(context);
        this.listener = listener;
    }

    public void setCanEdit(boolean canEdit) {
        isCanEdit = canEdit;
    }

    public void setIsAdditionOrSub(int isAdditionOrSub) {
        this.isAdditionOrSub = isAdditionOrSub;
    }

    public void setList(ArrayList<ClassInfoBean> list) {
        defaultClassScore = 0;
        this.list = list;
        notifyDataSetChanged();
    }

    public void setClassScoreMap(Map<Integer, String> classScoreMap) {
        this.classScoreMap = classScoreMap;
        notifyDataSetChanged();
    }

    public ArrayList<ClassInfoBean> getList() {
        return list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        final View view = mInflater.inflate(R.layout.list_item_dormitory_class_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SingleItemClassListAdapterV10.ViewHolder holder, int position) {
        ClassInfoBean classInfoBean = list.get(position);
//        if(0 != defaultClassScore) {
//            classInfoBean.setClassSingleScore(defaultClassScore);
//        }
        if(!isCanEdit) {
            holder.remarkEditText.setEnabled(false);
        }
        if(classScoreMap != null) {
            int classId = classInfoBean.getClassId();
            if(classScoreMap.containsKey(classId)) {
//                Float classSingleScore = classScoreMap.get(classId);
                classInfoBean.setClassSingleScore(classScoreMap.get(classId));
            } else {
                classInfoBean.setClassSingleScore("");
            }
        }
        if(isAdditionOrSub == Constant.Addition) { // 增分项， 隐藏-号
            holder.tvMinus.setVisibility(View.INVISIBLE);
        } else {
            if(classInfoBean.getClassSingleScore().isEmpty() || Float.valueOf(classInfoBean.getClassSingleScore()) == 0) { // 扣分项，分数为空，隐藏-号
                holder.tvMinus.setVisibility(View.INVISIBLE);
            } else {
                holder.tvMinus.setVisibility(View.VISIBLE);
            }
//            if(classInfoBean.getClassSingleScore().isEmpty() || Float.valueOf(classInfoBean.getClassSingleScore()) == 0) { // 扣分项，分数为0，隐藏-号和隐藏分数显示
//                holder.tvMinus.setVisibility(View.INVISIBLE);
////            classInfoBean.setClassSingleScore("");
//            }
        }

        holder.scoreClassTv.setText(StringUtils.formatScore(classInfoBean.getClassSingleScore()));
        holder.remarkEditText.setText(classInfoBean.getClassSingleRemark());
        holder.nameClassTv.setText(classInfoBean.getClassName());
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.tv_dormitory_class_name)
        TextView nameClassTv;
        @Bind(R.id.tv_dormitory_class_score_show)
        TextView scoreClassTv;
        @Bind(R.id.et_class_remark)
        EditText remarkEditText;
        @Bind(R.id.tv_minus)
        TextView tvMinus;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            scoreClassTv.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    String text = s.toString().trim();
//                    if(!text.isEmpty()) {
                        if(listener != null) {
                            listener.onTextChange(getLayoutPosition(), text, true);
                        }
                        list.get(getLayoutPosition()).setClassSingleScore(s.toString().trim());
//                    }
                }
            });
            remarkEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    String text = s.toString().trim();
//                    if(!text.isEmpty()) {
                        if(listener != null) {
                            listener.onTextChange(getLayoutPosition(), text, false);
                        }
                        list.get(getLayoutPosition()).setClassSingleRemark(text);
                    }
//                }
            });
        }

    }

    public interface OnClassItemTextChangeListener {
        /**
         \* @param position
         * @param afterText 修改后字符
         * @param isScoreOrRemark true 为得分， false 为备注
         */
        void onTextChange(int position, String afterText, boolean isScoreOrRemark);
    }

}
