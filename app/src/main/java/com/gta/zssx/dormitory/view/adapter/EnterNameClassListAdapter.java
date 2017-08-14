package com.gta.zssx.dormitory.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gta.zssx.R;
import com.gta.zssx.dormitory.model.bean.ClassInfoBean;
import com.gta.zssx.pub.common.Constant;
import com.gta.zssx.pub.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * [Description]
 * 录入姓名多项/单项 - 宿舍维度要显示的班级列表
 * [How to use]
 *
 * [Tips]
 *
 * Created by lan.zheng on 2017/7/25 11:28.
 */

public class EnterNameClassListAdapter extends RecyclerView.Adapter{
    private Context mContext;
    private List<ClassInfoBean> mClassInfoBeanList;
    private Listener mListener;
    private boolean mIsCanEdit;
    private int mAdditionOrSubtraction;

    public EnterNameClassListAdapter(Context context,List<ClassInfoBean> classInfoBeanList,boolean isCanEdit,int additionOrSubtraction,Listener listener){
        mContext = context;
        mClassInfoBeanList = classInfoBeanList;
        mListener = listener;
        mIsCanEdit = isCanEdit;
        mAdditionOrSubtraction = additionOrSubtraction;
    }

    public void refreshData(List<ClassInfoBean> classInfoBeanList){
        mClassInfoBeanList = classInfoBeanList;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View mItemView = LayoutInflater.from(mContext).inflate(R.layout.list_item_dormitory_class_item, parent, false);
        return new EnterNameClassListAdapter.DisplayHolder(mItemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        DisplayHolder displayHolder = (DisplayHolder) holder;
        ClassInfoBean classInfoBean = mClassInfoBeanList.get(position);

        displayHolder.nameClassTv.setText(classInfoBean.getClassName());
        String score = "";
        float f = 0;
        if(!TextUtils.isEmpty(classInfoBean.getClassSingleScore())) {
            f = Float.parseFloat(classInfoBean.getClassSingleScore());
            score = String.valueOf(f);
            score = StringUtils.formatScore(score);
        }

        //TODO 显示分数
        displayHolder.scoreClassTv.setText(score);
        //TODO 是否显示“-”号
        if(mAdditionOrSubtraction == Constant.Subtraction){
            if(!TextUtils.isEmpty(score)) {
                displayHolder.minusTextView.setVisibility(View.VISIBLE);
            }else {
                displayHolder.minusTextView.setVisibility(View.GONE);
            }
        }else {
            displayHolder.minusTextView.setVisibility(View.GONE);
        }
        displayHolder.remarkEditText.setText(classInfoBean.getClassSingleRemark());
        displayHolder.remarkEditText.setFocusableInTouchMode(mIsCanEdit);
        displayHolder.remarkEditText.setFocusable(mIsCanEdit);
        displayHolder.remarkEditText.setEnabled(mIsCanEdit);
    }


    @Override
    public int getItemCount() {
        return mClassInfoBeanList == null ? 0 : mClassInfoBeanList.size();
    }

    private class DisplayHolder extends RecyclerView.ViewHolder {
        TextView nameClassTv;
        TextView scoreClassTv;
        EditText remarkEditText;
        TextView minusTextView;

        public DisplayHolder(View itemView) {
            super(itemView);
            nameClassTv = (TextView)itemView.findViewById(R.id.tv_dormitory_class_name);
            scoreClassTv = (TextView) itemView.findViewById(R.id.tv_dormitory_class_score_show);
            minusTextView = (TextView)itemView.findViewById(R.id.tv_minus);
            remarkEditText = (EditText) itemView.findViewById(R.id.et_class_remark);
            remarkEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    //TODO 回调告知备注有被修改
                    int pos = getLayoutPosition();
                    mListener.enterRemark(pos,s.toString());
                }
            });
        }
    }

    public interface Listener{
        /**
         * 班级备注修改监听
         * @param position 位置
         * @param remark 备注
         */
        void enterRemark(int position, String remark);
    }

}
