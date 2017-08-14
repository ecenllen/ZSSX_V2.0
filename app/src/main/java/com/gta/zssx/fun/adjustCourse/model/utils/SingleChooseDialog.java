package com.gta.zssx.fun.adjustCourse.model.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.gta.zssx.R;
import com.gta.zssx.fun.adjustCourse.model.bean.ScheduleBean;

import java.util.List;

/**
 * [Description]
 * <p>
 * [How to use]
 * <p>
 * [Tips]
 *
 * @author Created by Zhimin.Huang on 2017/3/31.
 * @since 1.0.0
 */
public class SingleChooseDialog {

    private Context mContext;
    private int mDefaultChecked;
    private onCheckListener mOnCheckListener;
    private List<ScheduleBean.SectionBean> mSectionBeen;
    public int mPosition = -1;

    public SingleChooseDialog(Context context, int defaultChecked, List<ScheduleBean.SectionBean> sectionBeen) {
        mContext = context;
        mDefaultChecked = defaultChecked;
        mSectionBeen = sectionBeen;
    }

    public interface onCheckListener {
        void onCheck(int position);
    }

    public void create() {
        AlertDialog.Builder lBuilder = new AlertDialog.Builder(mContext);
        View lInflate = LayoutInflater.from(mContext).inflate(R.layout.dialog_center_item, null);

        RecyclerView lRecyclerView = (RecyclerView) lInflate.findViewById(R.id.recyclerView);
        TextView lTextView = (TextView) lInflate.findViewById(R.id.title_tv);
        TextView lExit = (TextView) lInflate.findViewById(R.id.tv_dialog_exit);
        TextView lConfirm = (TextView) lInflate.findViewById(R.id.btn_dialog_confirm);
        LinearLayout lOptionLayout = (LinearLayout) lInflate.findViewById(R.id.option_layout);

        lOptionLayout.setVisibility(View.VISIBLE);
        lTextView.setVisibility(View.VISIBLE);
        lRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        lBuilder.setView(lInflate);
        lBuilder.setCancelable(false);
        lRecyclerView.setAdapter(new ItemAdapter(mSectionBeen, new ItemAdapter.Listener() {
            @Override
            public void itemClick(int position) {
                mPosition = position;
            }
        }));
        AlertDialog lShow = lBuilder.show();
        lExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lShow.dismiss();
            }
        });
        lConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPosition >= 0) {
                    mOnCheckListener.onCheck(mPosition);
                }
                lShow.dismiss();
            }
        });
//        lShow.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

    }

    public SingleChooseDialog setOnCheckListener(onCheckListener onCheckListener) {
        mOnCheckListener = onCheckListener;
        return this;
    }

    private static class ItemAdapter extends BaseQuickAdapter<ScheduleBean.SectionBean, BaseViewHolder> {

        CheckBox mCheckBox;
        Listener mListener;

        public interface Listener {
            void itemClick(int position);
        }

        public ItemAdapter(List<ScheduleBean.SectionBean> data, Listener listener) {
            super(R.layout.item_single_check_dialog, data);
            mListener = listener;
        }

        @Override
        protected void convert(BaseViewHolder helper, ScheduleBean.SectionBean item) {
            CheckBox lCheckBox = helper.getView(R.id.checkBox);
            helper.setText(R.id.period, item.getOpenCourseTypeName());
            String lDetail = item.getCourseName() + "\n" + item.getClassName();
            helper.setText(R.id.detail, item.getGiveCourseName() == null || item.getGiveCourseName().isEmpty() ? lDetail : lDetail + "\n" + item.getGiveCourseName());
            helper.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean lChecked = lCheckBox.isChecked();
                    lCheckBox.setChecked(!lChecked);
                    if (!lChecked) {
                        if (mCheckBox != null) {
                            mCheckBox.setChecked(false);
                        }
                        mCheckBox = lCheckBox;
                        mListener.itemClick(helper.getAdapterPosition());
                    } else {
                        mCheckBox = null;
                        mListener.itemClick(-1);
                    }
                }
            });
        }
    }
}
