package com.gta.zssx.fun.coursedaily.registercourse.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gta.zssx.R;
import com.gta.zssx.fun.coursedaily.registercourse.model.bean.ClassChooseBean01;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
public class ClassChooseAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private List<ClassChooseBean01.ClassListBean> mClassListBeen;
    private List<ClassChooseBean01.ClassListBean> mRecordList;
    private Set<ClassChooseBean01.ClassListBean> mClassListBeanSet;
    private Listener mListener;
    private Set<Integer> checkList;
    private List<Integer> checkListAll;
    private boolean onBind;
    private SparseIntArray mSparseIntArray;

    public ClassChooseAdapter(Context context, List<ClassChooseBean01.ClassListBean> classListBeen,
                              Listener listener, SparseIntArray sparseIntArray) {
        mContext = context;
        mClassListBeen = classListBeen;
        mSparseIntArray = sparseIntArray;
        mListener = listener;
        checkList = new HashSet<>();
        checkListAll = new ArrayList<>();
        mRecordList = new ArrayList<>();
        mClassListBeanSet = new HashSet<>();
    }

    public interface Listener {

        void checkBoxAllClick(boolean isCheck, Set<ClassChooseBean01.ClassListBean> classListBeanSet,
                              int position, List<ClassChooseBean01.ClassListBean> recordList);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return ClassHolder.newHolder(parent, mContext);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        final ClassHolder lClassHolder = (ClassHolder) holder;
        final ClassChooseBean01.ClassListBean lClassListBean = mClassListBeen.get(position);
        lClassHolder.nameTv.setText(lClassListBean.getClassName());

        //头部显示逻辑
        final String lGrade = lClassListBean.getDeptName();
        lClassListBean.getYear();

        if (position == 0) {
            this.setHeader(true, lClassHolder.mLinearLayout, lGrade, lClassHolder.headerTv);
        } else {
            ClassChooseBean01.ClassListBean pre = this.mClassListBeen.get(position - 1);
            if (!lGrade.equals(pre.getDeptName())) {
//                if (!lClassListBean.getYear().equals(pre.getYear())) {
//
//                    this.setHeader(true, lClassHolder.mLinearLayout, lGrade, lClassHolder.headerTv);
//                }
                this.setHeader(true, lClassHolder.mLinearLayout, lGrade, lClassHolder.headerTv);
            } else {
//                if (!lClassListBean.getYear().equals(pre.getYear())) {
//                    this.setHeader(true, lClassHolder.mLinearLayout, lGrade, lClassHolder.headerTv);
//                } else {
//                    this.setHeader(false, lClassHolder.mLinearLayout, null, lClassHolder.headerTv);
//                }
                this.setHeader(false, lClassHolder.mLinearLayout, null, lClassHolder.headerTv);
            }
        }

        //防止recycleview复用带来的checkbox选中状态错乱
        lClassHolder.mCheckBox.setTag(new Integer(position));

        //防止java.lang.IllegalStateException: Cannot call this method while RecyclerView is computing a layout or scrolling
        onBind = true;
        lClassHolder.mCheckBox.setChecked(checkList.contains(new Integer(position)));

        lClassHolder.mCheckBoxAll.setTag(new Integer(position));

        lClassHolder.mCheckBoxAll.setChecked(checkListAll.contains(new Integer(position)));

        onBind = false;
//        lClassHolder.mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isGroupChecked) {
//                mListener.checkBoxClick(isGroupChecked, lClassListBean, position);
//                if (isGroupChecked) {
//                    if (!checkLi st.contains(buttonView.getTag())) {
//                        checkList.add(new Integer(position));
//                    }
//                } else {
//                    if (checkList.contains(buttonView.getTag())) {
//                        checkList.remove(new Integer(position));
//                    }
//                }
//            }
//        });

//        lClassHolder.mCheckBoxAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isGroupChecked) {
//                if (onBind)
//                    return;
//                if (isGroupChecked) {
//                    if (!checkListAll.contains(buttonView.getTag())) {
//                        checkListAll.add(new Integer(position));
//                    }
//
////                    mRecordList.clear();
//                    mClassListBeanSet.clear();
//                   /* *
//                     * 处理全选时子checkbox状态的变化和数据集合的变化
//                     * */
//
//                    int count = 0;
//                    for (int i = position; i < mClassListBeen.size(); i++) {
//                        ClassChooseBean01.ClassListBean lClassListBean1 = mClassListBeen.get(i);
//                        if (lGrade.equals(lClassListBean1.getDeptName())) {
////                            lClassListBean1.setCheck(true);
//                            if (!mRecordList.contains(lClassListBean1)) {
//                                mRecordList.add(lClassListBean1);
//                            }
//                            mClassListBeanSet.add(lClassListBean1);
//                            checkList.add(new Integer(i));
//                            count++;
//                        } else {
//                            break;
//                        }
//                    }
//                    notifyItemRangeChanged(position, count);
//
//                } else {
//                    if (checkListAll.contains(buttonView.getTag())) {
//                        checkListAll.remove(new Integer(position));
//                    }
//
//                   /* *
//                     * 处理全选取消时数据集合的变化和子checkbox状态的变化
//                     */
//                    int count = 0;
//                    for (int i = position; i < mClassListBeen.size(); i++) {
//                        ClassChooseBean01.ClassListBean lClassListBean1 = mClassListBeen.get(i);
//                        if (lGrade.equals(lClassListBean1.getDeptName())) {
////                            lClassListBean1.setCheck(false);
//                            mClassListBeanSet.remove(lClassListBean1);
//                            mRecordList.remove(lClassListBean1);
//                            count++;
//                            checkList.remove(new Integer(i));
//                        } else {
//                            break;
//                        }
//                    }
//                    notifyItemRangeChanged(position, count);
//                }
//
//                mListener.checkBoxAllClick(isGroupChecked, mClassListBeanSet, position, mRecordList);
//            }
//        });

        lClassHolder.selectWholeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean lChecked = lClassHolder.mCheckBoxAll.isChecked();
                lClassHolder.mCheckBoxAll.setChecked(!lChecked);
                if (onBind)
                    return;
                if (lClassHolder.mCheckBoxAll.isChecked()) {
                    if (!checkListAll.contains(lClassHolder.mCheckBoxAll.getTag())) {
                        checkListAll.add(new Integer(position));
                    }

//                    mRecordList.clear();
                    mClassListBeanSet.clear();
                   /* *
                     * 处理全选时子checkbox状态的变化和数据集合的变化
                     * */

                    int count = 0;
                    for (int i = position; i < mClassListBeen.size(); i++) {
                        ClassChooseBean01.ClassListBean lClassListBean1 = mClassListBeen.get(i);
                        if (lGrade.equals(lClassListBean1.getDeptName())) {
//                            lClassListBean1.setCheck(true);
                            if (!mRecordList.contains(lClassListBean1)) {
                                mRecordList.add(lClassListBean1);
                            }
                            mClassListBeanSet.add(lClassListBean1);
                            checkList.add(new Integer(i));
                            count++;
                        } else {
                            break;
                        }
                    }
                    notifyItemRangeChanged(position, count);

                } else {
                    if (checkListAll.contains(lClassHolder.mCheckBoxAll.getTag())) {
                        checkListAll.remove(new Integer(position));
                    }

                   /* *
                     * 处理全选取消时数据集合的变化和子checkbox状态的变化
                     */
                    int count = 0;
                    for (int i = position; i < mClassListBeen.size(); i++) {
                        ClassChooseBean01.ClassListBean lClassListBean1 = mClassListBeen.get(i);
                        if (lGrade.equals(lClassListBean1.getDeptName())) {
//                            lClassListBean1.setCheck(false);
                            mClassListBeanSet.remove(lClassListBean1);
                            mRecordList.remove(lClassListBean1);
                            count++;
                            checkList.remove(new Integer(i));
                        } else {
                            break;
                        }
                    }
                    notifyItemRangeChanged(position, count);
                }

                mListener.checkBoxAllClick(lChecked, mClassListBeanSet, position, mRecordList);
            }
        });
        //点击单条条目
        lClassHolder.selectItemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean lChecked = lClassHolder.mCheckBox.isChecked();
                lClassHolder.mCheckBox.setChecked(!lChecked);
                if (lClassHolder.mCheckBox.isChecked()) {
                    if (!checkList.contains(lClassHolder.mCheckBox.getTag())) {
                        checkList.add(new Integer(position));
                    }
                    mClassListBeanSet.add(lClassListBean);
                    mRecordList.add(lClassListBean);
//                    if (mClassListBeanSet.size() == lClassListBean.getSize()) {
//                        int lHeaderPosition = lClassListBean.getHeaderPosition();
//                        checkListAll.add(new Integer(lHeaderPosition));
//                        notifyItemChanged(lHeaderPosition);
//                    }

                    /**
                     * 处理单选后到达全选的条件时，自动选中头部的逻辑
                     */
//                    List<ClassChooseBean01.ClassListBean> lc = new ArrayList<>(mClassListBeanSet);
                    int count = 0;
                    for (int i = 0; i < mRecordList.size(); i++) {
//                        if (lc.get(i).getDeptName().equals(lClassListBean.getDeptName()) && lc.get(i).getYear().equals(lClassListBean.getYear())) {
//                            count++;
//                        }
                        if (mRecordList.get(i).getDeptName().equals(lClassListBean.getDeptName())) {
                            count++;
                        }
                    }
                    if (lClassListBean.getSize() == count) {
                        int lHeaderPosition = lClassListBean.getHeaderPosition();
                        checkListAll.add(new Integer(lHeaderPosition));
                        notifyItemChanged(lHeaderPosition);
                    }

                } else {
                    if (checkList.contains(lClassHolder.mCheckBox.getTag())) {
                        checkList.remove(new Integer(position));
                    }
                    mClassListBeanSet.remove(lClassListBean);
                    mRecordList.remove(lClassListBean);

                    /**
                     * 处理单选不选中时，自动取消头部全选状态的逻辑
                     */
                    int lHeaderPosition = lClassListBean.getHeaderPosition();
                    if(checkListAll.contains(new Integer(lHeaderPosition))) {
                        checkListAll.remove(new Integer(lHeaderPosition));
                        notifyItemChanged(lHeaderPosition);
                    }
                }

                mListener.checkBoxAllClick(lChecked, mClassListBeanSet, position, mRecordList);
            }
        });
    }

    public void setHeader(boolean visible, LinearLayout headerTv, String header, TextView textView) {
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

    private static class ClassHolder extends RecyclerView.ViewHolder {
        TextView headerTv;
        TextView nameTv;
        // ImageView sectionIv;
        CheckBox mCheckBox;
        LinearLayout mLinearLayout;
        CheckBox mCheckBoxAll;
        //類title點擊範圍增大
        LinearLayout selectWholeLayout;
        RelativeLayout selectItemLayout;

        public ClassHolder(Context context, View itemView) {
            super(itemView);
            headerTv = (TextView) itemView.findViewById(R.id.section_header_tv);
//            sectionIv = (ImageView) itemView.findViewById(R.id.section_iv);
            nameTv = (TextView) itemView.findViewById(R.id.section_name_tv);
            mCheckBox = (CheckBox) itemView.findViewById(R.id.checkbox);
            mLinearLayout = (LinearLayout) itemView.findViewById(R.id.choose_all_layout);
            mCheckBoxAll = (CheckBox) itemView.findViewById(R.id.checkbox_all);
            selectWholeLayout = (LinearLayout) itemView.findViewById(R.id.layout_select_whole);
            selectItemLayout = (RelativeLayout) itemView.findViewById(R.id.layout_select_item);
        }

        private static ClassHolder newHolder(ViewGroup parent, Context context) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_class_choose, parent, false);
            return new ClassHolder(context, view);
        }
    }
}
