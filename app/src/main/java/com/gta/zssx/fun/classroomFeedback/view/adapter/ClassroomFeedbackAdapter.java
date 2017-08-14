package com.gta.zssx.fun.classroomFeedback.view.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gta.zssx.R;
import com.gta.zssx.fun.classroomFeedback.model.bean.ClassroomFeedbackBean;
import com.gta.zssx.pub.util.TimeUtils;

import java.util.List;

/**
 * [Description]
 * <p> 课堂教学反馈未登记，已登记Adapter state "-1": 未登记；"0":未送审；"1":待审核；"3":已通过；"2":未通过；
 * [How to use]
 * <p>
 * [Tips]
 *
 * @author Created by liang.lu on 2017/06/20.
 * @since 2.0.0
 */
public class ClassroomFeedbackAdapter extends RecyclerView.Adapter {

    private List<ClassroomFeedbackBean.RegisterDataListBean> dataListBeen;
    private Context mContext;
    private OptionItemListener optionItemListener;

    public void setDataListBeen (List<ClassroomFeedbackBean.RegisterDataListBean> dataListBeen) {
        this.dataListBeen = dataListBeen;
    }

    public ClassroomFeedbackAdapter (Context context) {
        this.mContext = context;
    }

    public void setOptionItemListener (OptionItemListener optionItemListener) {
        this.optionItemListener = optionItemListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {
        if (viewType == ITEM_TYPE.TITLE_TYPE.ordinal ()) {
            return TitleHolder.establish (parent, mContext);
        }
        return OptionHolder.establish (parent, mContext, optionItemListener);
    }

    @Override
    public void onBindViewHolder (RecyclerView.ViewHolder holder, int position) {
        String mState = "";
        String mAllState = null;
        if (holder instanceof TitleHolder) {
            TitleHolder titleHolder = (TitleHolder) holder;
            titleHolder.mTitleTv.setText (dataListBeen.get (position).getClassName ());
        } else if (holder instanceof OptionHolder) {
            OptionHolder optionHolder = (OptionHolder) holder;
            switch (dataListBeen.get (position).getState ()) {
                case "-1":
                    mAllState = "登记";
                    break;
                case "0":
                    mState = "未送审";
                    mAllState = "修改";
                    break;
                case "1":
                    mState = "待审核";
                    mAllState = "查看";
                    break;
                case "2":
                    mState = "未通过";
                    mAllState = "修改";
                    break;
                case "3":
                    mState = "已通过";
                    mAllState = "查看";
                    break;
                default:
                    break;
            }
            optionHolder.mWeekTv.setText (dataListBeen.get (position).getWeekNumber ());
            optionHolder.mWeekDateTv.setText (dataListBeen.get (position).getTimeSlot ());
            optionHolder.mAllStateTv.setText (mAllState);
            optionHolder.mStateTv.setVisibility (mState.equals ("") ? View.GONE : View.VISIBLE);
            optionHolder.setBackground (optionHolder.mR,
                    optionHolder.mWeekTv,
                    optionHolder.mStateTv,
                    optionHolder.mAllStateTv,
                    optionHolder.mWeekDateTv,
                    optionHolder.mImageView,
                    mState);
        }
    }

    private static class TitleHolder extends RecyclerView.ViewHolder {
        TextView mTitleTv;
        Context mContext;

        TitleHolder (Context context, View itemView) {
            super (itemView);
            this.mContext = context;
            mTitleTv = (TextView) itemView.findViewById (R.id.tv_title_class_name);
        }

        private static TitleHolder establish (ViewGroup parent, Context context) {
            View view = LayoutInflater.from (context).inflate (R.layout.adapter_item_classroom_feedback_title, parent, false);
            return new TitleHolder (context, view);
        }
    }

    private static class OptionHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        Context mContext;
        TextView mWeekTv;
        TextView mWeekDateTv;
        TextView mAllStateTv;
        TextView mStateTv;
        TextView mImageView;
        OptionItemListener optionItemListener;
        RelativeLayout mR;

        OptionHolder (Context context, View itemView, OptionItemListener optionItemListener) {
            super (itemView);
            this.mContext = context;
            this.optionItemListener = optionItemListener;
            mR = (RelativeLayout) itemView.findViewById (R.id.ll_date);
            mWeekTv = (TextView) itemView.findViewById (R.id.tv_week_date);
            mWeekDateTv = (TextView) itemView.findViewById (R.id.tv_week_date_date);
            mAllStateTv = (TextView) itemView.findViewById (R.id.tv_all_state);
            mStateTv = (TextView) itemView.findViewById (R.id.tv_state);
            mImageView = (TextView) itemView.findViewById (R.id.iv);
            itemView.setOnClickListener (this);
        }

        private static OptionHolder establish (ViewGroup parent, Context context, OptionItemListener optionItemListener) {
            View view = LayoutInflater.from (context).inflate (R.layout.adapter_item_classroom_feedback_option, parent, false);
            return new OptionHolder (context, view, optionItemListener);
        }

        /**
         * 未通过状态设置背景字体颜色
         *
         * @param view        背景view
         * @param mWeekDateTv text view
         * @param mStateTv    text view
         * @param mAllStateTv text view
         * @param mDateTv     text view
         * @param imageView   text view
         */
        private void setBackground (View view, TextView mWeekDateTv, TextView mStateTv, TextView mAllStateTv, TextView mDateTv, TextView imageView, String mState) {
            switch (mState) {
                case "未通过":
                    view.setBackgroundColor (ContextCompat.getColor (mContext, R.color.f48585));
                    mWeekDateTv.setTextColor (ContextCompat.getColor (mContext, R.color.white));
                    mAllStateTv.setTextColor (ContextCompat.getColor (mContext, R.color.white));
                    mStateTv.setTextColor (ContextCompat.getColor (mContext, R.color.ffcbcb));
                    mDateTv.setTextColor (ContextCompat.getColor (mContext, R.color.ffcbcb));
                    imageView.setBackgroundResource (R.drawable.icon_more);
                    mStateTv.setText ("状态：" + mState);
                    break;
                case "已通过":
                    view.setBackgroundColor (ContextCompat.getColor (mContext, R.color.daf4e9));
                    mWeekDateTv.setTextColor (ContextCompat.getColor (mContext, R.color.gray_666666));
                    mAllStateTv.setTextColor (ContextCompat.getColor (mContext, R.color.gray_999999));
                    mStateTv.setTextColor (ContextCompat.getColor (mContext, R.color.gray_999999));
                    mDateTv.setTextColor (ContextCompat.getColor (mContext, R.color.gray_999999));
                    imageView.setBackgroundResource (R.drawable.right_arrow_normal);
                    SpannableStringBuilder style = new SpannableStringBuilder ("状态：" + mState);
                    style.setSpan (new ForegroundColorSpan (ContextCompat.getColor (mContext, R.color.main_color)), 3, 6, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    mStateTv.setText (style);
                    break;
                default:
                    view.setBackgroundColor (ContextCompat.getColor (mContext, R.color.transparent));
                    mWeekDateTv.setTextColor (ContextCompat.getColor (mContext, R.color.gray_666666));
                    mAllStateTv.setTextColor (ContextCompat.getColor (mContext, R.color.gray_999999));
                    mStateTv.setTextColor (ContextCompat.getColor (mContext, R.color.gray_999999));
                    mDateTv.setTextColor (ContextCompat.getColor (mContext, R.color.gray_999999));
                    imageView.setBackgroundResource (R.drawable.right_arrow_normal);
                    mStateTv.setText ("状态：" + mState);
                    break;
            }

        }

        @Override
        public void onClick (View v) {
            if (TimeUtils.isFastDoubleClick ()) {
                return;
            }
            optionItemListener.optionClickListener (getLayoutPosition () - 1);
        }
    }

    @Override
    public int getItemCount () {
        return dataListBeen == null ? 0 : dataListBeen.size ();
    }

    @Override
    public int getItemViewType (int position) {
        if (dataListBeen.get (position).isTitle ()) {
            return ITEM_TYPE.TITLE_TYPE.ordinal ();
        }
        return ITEM_TYPE.OPTION_TYPE.ordinal ();
    }

    private enum ITEM_TYPE {
        TITLE_TYPE, OPTION_TYPE
    }

    public interface OptionItemListener {
        void optionClickListener (int position);
    }
}
