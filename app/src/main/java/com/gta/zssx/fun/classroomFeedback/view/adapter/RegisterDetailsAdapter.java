package com.gta.zssx.fun.classroomFeedback.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.gta.zssx.R;
import com.gta.zssx.fun.classroomFeedback.model.bean.RegisterDetailsBean;

import java.util.List;

/**
 * [Description]
 * <p> 登记详情页面 Adapter
 * [How to use]
 * <p>
 * [Tips]
 *
 * @author Created by liang.lu on 2017/06/20.
 * @since 2.0.0
 */
public class RegisterDetailsAdapter extends RecyclerView.Adapter {
    private Context mContext;
    private List<RegisterDetailsBean.ScoreOptionsListBean.OptionsBean> optionsBeanList;
    private int mTotalScore;
    private ChangedUIScoreListener listener;
    private String mState;

    public RegisterDetailsAdapter (Context context, String state) {
        super ();
        this.mContext = context;
        this.mState = state;
    }

    public void setTotalScore (int totalScore) {
        this.mTotalScore = totalScore;
    }

    public void setListener (ChangedUIScoreListener listener) {
        this.listener = listener;
    }

    public void setOptionsBeanList (List<RegisterDetailsBean.ScoreOptionsListBean.OptionsBean> optionsBeanList) {
        this.optionsBeanList = optionsBeanList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {
        if (viewType == ITEM_TYPE.TITLE_TYPE.ordinal ()) {
            return TitleHolder.establish (parent, mContext);
        }
        return OptionHolder.establish (parent, mContext, this :: upData, mState);
    }

    @Override
    public void onBindViewHolder (RecyclerView.ViewHolder holder, int position) {
        RegisterDetailsBean.ScoreOptionsListBean.OptionsBean optionsBean = optionsBeanList.get (position);
        if (holder instanceof TitleHolder) {
            ((TitleHolder) holder).mTitleTv.setText (optionsBean.getOptionTitle ());
        } else if (holder instanceof OptionHolder) {
            if (optionsBean.isCheckState ()) {
                ((OptionHolder) holder).mCheckBox.setChecked (true);
            } else {
                ((OptionHolder) holder).mCheckBox.setChecked (false);
            }
            ((OptionHolder) holder).mTitleNameTv.setText (optionsBean.getOptionTitle ());
            if (position == optionsBeanList.size () - 1) {
                ((OptionHolder) holder).mLine.setVisibility (View.VISIBLE);
            } else {
                ((OptionHolder) holder).mLine.setVisibility (View.GONE);
            }
        }
    }

    @Override
    public int getItemCount () {
        return optionsBeanList == null ? 0 : optionsBeanList.size ();
    }

    @Override
    public int getItemViewType (int position) {
        if (optionsBeanList.get (position).isTitle ()) {
            return ITEM_TYPE.TITLE_TYPE.ordinal ();
        }
        return ITEM_TYPE.OPTION_TYPE.ordinal ();
    }

    private static class TitleHolder extends RecyclerView.ViewHolder {
        Context mContext;
        TextView mTitleTv;

        TitleHolder (View itemView, Context context) {
            super (itemView);
            this.mContext = context;
            mTitleTv = (TextView) itemView.findViewById (R.id.tv_title_name);
        }

        private static TitleHolder establish (ViewGroup parent, Context context) {
            View view = LayoutInflater.from (context).inflate (R.layout.adapter_item_classroom_title, parent, false);
            return new TitleHolder (view, context);
        }
    }

    public void upData (int position, boolean isChecked) {
        optionsBeanList.get (position).setCheckState (isChecked);
        int score = 0;
        for (int i = 0; i < optionsBeanList.size (); i++) {
            if (!optionsBeanList.get (i).isTitle ()) {
                if (optionsBeanList.get (i).isCheckState ()) {
                    score += optionsBeanList.get (i).getOptionScore ();
                }
            }
        }
        listener.changeScore (String.valueOf (mTotalScore - score), position, true);
        notifyItemChanged (position);
    }

    private static class OptionHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        Context mContext;
        TextView mTitleNameTv;
        CheckBox mCheckBox;
        View mLine;
        OptionItemListener listener;
        String mState;

        OptionHolder (View itemView, Context context, OptionItemListener listener, String state) {
            super (itemView);
            this.mContext = context;
            this.listener = listener;
            this.mState = state;
            mTitleNameTv = (TextView) itemView.findViewById (R.id.tv_title_name);
            mCheckBox = (CheckBox) itemView.findViewById (R.id.cb_option);
            mLine = itemView.findViewById (R.id.view);
            itemView.setOnClickListener (this);
            if (mState.equals ("1") || mState.equals ("3")) {
                itemView.setEnabled (false);
                mCheckBox.setEnabled (false);
            } else {
                itemView.setEnabled (true);
                mCheckBox.setEnabled (true);
            }
        }

        private static OptionHolder establish (ViewGroup parent, Context context, OptionItemListener listener, String state) {
            View view = LayoutInflater.from (context).inflate (R.layout.adapter_item_classroom_option, parent, false);
            return new OptionHolder (view, context, listener, state);
        }

        @Override
        public void onClick (View v) {
            mCheckBox.setChecked (!mCheckBox.isChecked ());
            listener.clickOptionItem (getLayoutPosition (), mCheckBox.isChecked ());
        }
    }

    private enum ITEM_TYPE {
        TITLE_TYPE, OPTION_TYPE
    }

    interface OptionItemListener {
        void clickOptionItem (int position, boolean isChecked);
    }

    public interface ChangedUIScoreListener {
        void changeScore (String score, int position, boolean isChanged);
    }
}
