package com.gta.zssx.fun.coursedaily.registercourse.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gta.zssx.R;
import com.gta.zssx.fun.coursedaily.registercourse.model.bean.SectionBean;

import java.util.List;
import java.util.Set;

/**
 * [Description]
 * <p>
 * [How to use]
 * <p>
 * [Tips]
 *
 * @author Created by Zhimin.Huang on 2016/6/20.
 * @since 1.0.0
 */
public class SectionSingleChooseAdapter extends RecyclerView.Adapter {

    private List<SectionBean> mSectionBeanList;
    private Context mContext;
    private Listener mListener;
    private Set<SectionBean> mSectionBeanSet;
    //    private List<SectionBean> mSectionBeanList1;
    private int mSectionId;
    private CheckBox mCheckBox;


    public SectionSingleChooseAdapter(List<SectionBean> sectionBeanList, Context context, Listener listener, int sectionId) {
        mSectionBeanList = sectionBeanList;
        mContext = context;
        mListener = listener;
//        mSectionBeanSet = ClassDataManager.getDataCache().getSection();
//        if (mSectionBeanSet != null) {
//            mSectionBeanList1 = new ArrayList<>(mSectionBeanSet);
//        }
        mSectionId = sectionId;
    }

    public interface Listener {
        void itemClick(SectionBean sectionBean);

        void checkBoxClick(boolean isCheck, SectionBean sectionBean);
    }

    public void setHeader(boolean visible, TextView headerTv, String header, LinearLayout linearLayout) {
        if (visible) {
            headerTv.setText(header);
            linearLayout.setVisibility(View.VISIBLE);
        } else {
            linearLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return SectionHolder.newHolder(parent, mContext);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final SectionBean lSectionBean = mSectionBeanList.get(position);
        final SectionHolder lSectionHolder = (SectionHolder) holder;
        String lFlag = lSectionBean.getSignStatus() == 0 ? mContext.getString(R.string.unregister_string) : mContext.getString(R.string.has_register_string);


        /**
         * 第一个显示头部
         */
        if (position == 0) {
            setHeader(true, lSectionHolder.headerTv, lFlag, lSectionHolder.headerLayout);
        } else {
            SectionBean pre = this.mSectionBeanList.get(position - 1);
            String lFlag1 = pre.getSignStatus() == 0 ? mContext.getString(R.string.unregister_string) : mContext.getString(R.string.has_register_string);
            /**
             * 上一个和这个不相等显示头部，否则不显示
             */
            if (!lFlag1.equals(lFlag)) {
                setHeader(true, lSectionHolder.headerTv, lFlag, lSectionHolder.headerLayout);
            } else {
                setHeader(false, lSectionHolder.headerTv, null, lSectionHolder.headerLayout);
            }
        }
        lSectionHolder.mCheckBox.setVisibility(lSectionBean.getSignStatus() == 1 ? View.GONE : View.VISIBLE);


        if (lSectionBean.getSectionId() == mSectionId) {
            lSectionHolder.mCheckBox.setChecked(true);
            mListener.checkBoxClick(true, lSectionBean);
            mCheckBox = lSectionHolder.mCheckBox;
        }


        /**
         * 恢复checkbox选中状态
         */
//        if (mSectionBeanList1 != null) {
//            for (int i = 0; i < mSectionBeanList1.size(); i++) {
//                if (lSectionBean.getLesson().equals(mSectionBeanList1.get(i).getLesson())) {
//                    lSectionHolder.mCheckBox.setGroupChecked(true);
//                    mListener.checkBoxClick(true, lSectionBean);
//                    break;
//                } else {
//                    lSectionHolder.mCheckBox.setGroupChecked(false);
//                }
//            }
//        }

        lSectionHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.itemClick(lSectionBean);
                }
            }
        });

//        lSectionHolder.mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isGroupChecked) {
//                if (mListener != null) {
//                    mListener.checkBoxClick(isGroupChecked, lSectionBean);
//                }
//                lSectionHolder.mCheckBox.setGroupChecked(true);
//                mCheckBox.setGroupChecked(false);
//                mCheckBox = lSectionHolder.mCheckBox;
//            }
//        });

       /* lSectionHolder.mCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean lChecked = lSectionHolder.mCheckBox.isGroupChecked();
                if (mListener != null) {
                    if (lChecked) {
                        mListener.checkBoxClick(lChecked, lSectionBean);
                    }
                }
                lSectionHolder.mCheckBox.setGroupChecked(true);
                if (mCheckBox != lSectionHolder.mCheckBox) {
                    mCheckBox.setGroupChecked(false);
                    mCheckBox = lSectionHolder.mCheckBox;
                }
            }
        });*/

        //点击整个条目勾选
        lSectionHolder.sectionLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //只有未登记的可以选中
                if (lSectionBean.getSignStatus()==0){
                    //點擊即立刻選中，因為後面的操作相關
                    lSectionHolder.mCheckBox.setChecked(true);
                    boolean lChecked = lSectionHolder.mCheckBox.isChecked();
                    if (mListener != null) {
                        if (lChecked) {
                            mListener.checkBoxClick(lChecked, lSectionBean);
                        }
                    }
                    if (mCheckBox != lSectionHolder.mCheckBox) {
                        mCheckBox.setChecked(false);
                        mCheckBox = lSectionHolder.mCheckBox;
                    }
                }
            }
        });

        lSectionHolder.nameTv.setText(lSectionBean.getLesson());
    }

    @Override
    public int getItemCount() {
        return mSectionBeanList.size();
    }

    private static class SectionHolder extends RecyclerView.ViewHolder {
        TextView headerTv;
        TextView nameTv;
        CheckBox mCheckBox;
        LinearLayout headerLayout;
        RelativeLayout sectionLayout;

        public SectionHolder(Context context, View itemView) {
            super(itemView);
            headerTv = (TextView) itemView.findViewById(R.id.section_choose_header_tv);
            nameTv = (TextView) itemView.findViewById(R.id.section_name_tv);
            mCheckBox = (CheckBox) itemView.findViewById(R.id.section_choose_checkbox);
            headerLayout = (LinearLayout) itemView.findViewById(R.id.section_header_layout);
            sectionLayout = (RelativeLayout)itemView.findViewById(R.id.layout_section_select_check);
        }

        private static SectionHolder newHolder(ViewGroup parent, Context context) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_section_choose, parent, false);
            return new SectionHolder(context, view);
        }
    }
}
