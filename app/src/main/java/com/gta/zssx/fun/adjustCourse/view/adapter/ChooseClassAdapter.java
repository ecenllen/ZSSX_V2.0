package com.gta.zssx.fun.adjustCourse.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gta.zssx.R;
import com.gta.zssx.fun.adjustCourse.model.bean.StuClassBean;

import java.util.ArrayList;
import java.util.List;

/**
 * [Description]
 * <p>
 * [How to use]
 * <p>
 * [Tips]
 *
 * @author Created by Zhimin.Huang on 2016/6/17.
 * @since 1.0.0
 */
public class ChooseClassAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private List<StuClassBean.ClassListBean> mClassListBeen;
    private Listener mListener;
    private List<Integer> checkList;
    private boolean onBind;
    private ChooseClassAdapter.ClassHolder mClassHolder;

    public ChooseClassAdapter(Context context, List<StuClassBean.ClassListBean> classListBeen,
                              Listener listener) {

        mContext = context;
        mClassListBeen = classListBeen;
        mListener = listener;
        checkList = new ArrayList<>();
    }

    public interface Listener {
        void itemClick(StuClassBean.ClassListBean classListBean);

        boolean prePosition(int position);

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return ClassHolder.newHolder(parent, mContext);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        final ClassHolder lClassHolder = (ClassHolder) holder;
        final StuClassBean.ClassListBean lClassListBean = mClassListBeen.get(position);
        lClassHolder.nameTv.setText(lClassListBean.getClassName());

        //头部显示逻辑
        final String lGrade = lClassListBean.getDeptName();

        if (position == 0) {
            this.setHeader(true, lClassHolder.mLinearLayout, lGrade, lClassHolder.headerTv);
        } else {
            StuClassBean.ClassListBean pre = this.mClassListBeen.get(position - 1);
            if (!lGrade.equals(pre.getDeptName())) {
                this.setHeader(true, lClassHolder.mLinearLayout, lGrade, lClassHolder.headerTv);
            } else {
                this.setHeader(false, lClassHolder.mLinearLayout, null, lClassHolder.headerTv);
            }
        }

        //防止recycleview复用带来的checkbox选中状态错乱
        lClassHolder.mCheckBox.setTag(position);

        //防止java.lang.IllegalStateException: Cannot call this method while RecyclerView is computing a layout or scrolling
        onBind = true;
        lClassHolder.mCheckBox.setChecked(checkList.contains(position));
        if (checkList.contains(position)) {
            mClassHolder = lClassHolder;
        }


        onBind = false;

        //点击单条条目
        lClassHolder.selectItemLayout.setOnClickListener(view -> {
            boolean lChecked = lClassHolder.mCheckBox.isChecked();
            lClassHolder.mCheckBox.setChecked(!lChecked);

            if (mClassHolder == null) {
                mClassHolder = lClassHolder;
                checkList.add(position);
                mListener.itemClick(lClassListBean);
            } else {
                if (!mClassHolder.equals(lClassHolder)) {
                    mClassHolder.mCheckBox.setChecked(false);
                    mListener.itemClick(lClassListBean);
                    checkList.clear();
                    checkList.add(position);
                } else {
                    mListener.itemClick(lChecked ? null : lClassListBean);
                    if (lChecked) {
                        checkList.clear();
                    } else {
                        checkList.clear();
                        checkList.add(position);
                    }
                }

                mClassHolder = lClassHolder;
            }

        });
    }

    private void setHeader(boolean visible, LinearLayout headerTv, String header, TextView textView) {
        if (visible) {
            textView.setText(header);
            headerTv.setVisibility(View.VISIBLE);
        } else {
            headerTv.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return mClassListBeen.size();
    }

    public static class ClassHolder extends RecyclerView.ViewHolder {
        TextView headerTv;
        TextView nameTv;
        // ImageView sectionIv;
        private CheckBox mCheckBox;
        LinearLayout mLinearLayout;
        CheckBox mCheckBoxAll;
        //類title點擊範圍增大
        LinearLayout selectWholeLayout;
        RelativeLayout selectItemLayout;

        private ClassHolder(Context context, View itemView) {
            super(itemView);
            headerTv = (TextView) itemView.findViewById(R.id.section_header_tv);
//            sectionIv = (ImageView) itemView.findViewById(R.id.section_iv);
            nameTv = (TextView) itemView.findViewById(R.id.section_name_tv);
            mCheckBox = (CheckBox) itemView.findViewById(R.id.checkbox);
            mLinearLayout = (LinearLayout) itemView.findViewById(R.id.choose_all_layout);
            mCheckBoxAll = (CheckBox) itemView.findViewById(R.id.checkbox_all);
            selectWholeLayout = (LinearLayout) itemView.findViewById(R.id.layout_select_whole);
            selectItemLayout = (RelativeLayout) itemView.findViewById(R.id.layout_select_item);
            mCheckBoxAll.setVisibility(View.GONE);
            mCheckBox.setVisibility(View.GONE);
        }

        private static ClassHolder newHolder(ViewGroup parent, Context context) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_class_choose, parent, false);
            return new ClassHolder(context, view);
        }

        public CheckBox getCheckBox() {
            return mCheckBox;
        }
    }
}
