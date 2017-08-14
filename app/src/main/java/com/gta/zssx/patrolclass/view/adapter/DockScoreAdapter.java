package com.gta.zssx.patrolclass.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gta.zssx.R;
import com.gta.zssx.patrolclass.model.entity.DockScoreEntity;

import java.util.List;

/**
 * [Description]
 * <p>
 * [How to use]
 * <p>
 * [Tips] Created by tengfei.lv on 2016/7/20 17:20.
 */

public class DockScoreAdapter extends RecyclerView.Adapter {

    private boolean isLook;

    public void setIsLook (boolean isLook) {
        this.isLook = isLook;
    }

    public interface InputScoreErrorListener {
        void onError (int position);
    }

    private Scrolling scrolling;

    public void setScrolling (Scrolling scrolling) {
        this.scrolling = scrolling;
    }

    public DockScoreAdapter () {
    }

    public interface Listener {
        void checkList (List<DockScoreEntity.DockScoreBean.OptionsBean> optionsBeen);
    }

    private List<DockScoreEntity.DockScoreBean.OptionsBean> datas;

    private InputScoreErrorListener listener;

    public void setListener (InputScoreErrorListener listener) {
        this.listener = listener;
    }

    public void setDockScoreDatas (List<DockScoreEntity.DockScoreBean.OptionsBean> datas) {
        this.datas = datas;
    }

    public List<DockScoreEntity.DockScoreBean.OptionsBean> getDatas () {
        return datas;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {
        if (viewType == ITEM_TYPE.OPTION_TYPE.ordinal ()) {
            return OptionsViewHolder.create (isLook, parent, (isCheck, position) -> {
                datas.get (position).setCheck (isCheck);
                //                    if (mListener != null) {
                //                        mListener.checkList (datas);
                //                    }
                notifyItemChanged (position);
            }, (text, position, editText) -> {
                datas.get (position).setChangeScoer (text);
                if (text.length () > 1) {

                    char fir = text.charAt (0);
                    if (fir == '0') {
                        listener.onError (position);
                        editText.setText ("");
                        return;
                    }
                }
                if (text.length () > 0) {
                    Integer integer = Integer.valueOf (text);
                    Integer score = Integer.valueOf (datas.get (position).getAllScore ());
                    if (integer > score) {    //输入的分数要小于题目的最大分数值
                        datas.get (position).setChangeScoer (datas.get (position).getScore ());
                        listener.onError (position);
                        editText.setText ("");
                    }

                }

            }, (isShow, position, view) -> {
                EditText editText = (EditText) view;
                if (editText.getText ().toString ().length () == 0 && !isShow) {
                    datas.get (position).setCheck (false);
                    if (datas.get (position).getChangeScoer ().length () == 0) {
                        datas.get (position).setChangeScoer (datas.get (position).getScore ());
                    } else {
                        datas.get (position).setChangeScoer (datas.get (position).getChangeScoer ());
                    }
                    if (scrolling.isScrolling ()) {
                        notifyItemChanged (position);
                    }
                }
            });
        }
        return TitleViewHolder.create (parent);
    }

    @Override
    public void onBindViewHolder (RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof TitleViewHolder) {
            ((TitleViewHolder) holder).titleTv.setText (datas.get (position).getTitle ());
        } else if (holder instanceof OptionsViewHolder) {
            DockScoreEntity.DockScoreBean.OptionsBean optionsBean = datas.get (position);

            if (isLook) {
                ((OptionsViewHolder) holder).checkBox.setChecked (false);
                ((OptionsViewHolder) holder).scoreLayout.setVisibility (View.GONE);
                ((OptionsViewHolder) holder).nameTv.setText (optionsBean.getTitle ());
                return;
            }

            boolean check = datas.get (position).isCheck ();
            ((OptionsViewHolder) holder).checkBox.setChecked (check);
            ((OptionsViewHolder) holder).scoreLayout.setVisibility (View.GONE);
            String singleScore;
            if (optionsBean.getChangeScoer () != null && optionsBean.getChangeScoer ().length () > 0) {
                singleScore = optionsBean.getChangeScoer ();
            } else {
                singleScore = optionsBean.getScore ();
            }


            if (check) {
                ((OptionsViewHolder) holder).scoreLayout.setVisibility (View.VISIBLE);
                ((OptionsViewHolder) holder).scoreEt.setText (singleScore);
            }
            ((OptionsViewHolder) holder).nameTv.setText (optionsBean.getTitle ());
        }
    }

    @Override
    public int getItemCount () {
        return datas == null ? 0 : datas.size ();
    }

    @Override
    public int getItemViewType (int position) {
        if (!datas.get (position).isTitle ()) {
            return ITEM_TYPE.OPTION_TYPE.ordinal ();
        }
        return ITEM_TYPE.TITLE_TYPE.ordinal ();
    }

    private enum ITEM_TYPE {
        TITLE_TYPE, OPTION_TYPE
    }

    private static class TitleViewHolder extends RecyclerView.ViewHolder {

        private final TextView titleTv;

        TitleViewHolder (View itemView) {
            super (itemView);
            titleTv = (TextView) itemView.findViewById (R.id.tv_dock_score_adapter_title);
        }

        private static TitleViewHolder create (ViewGroup parent) {
            View view = LayoutInflater.from (parent.getContext ()).inflate (R.layout.
                    adapter_dock_score_item_title, parent, false);
            return new TitleViewHolder (view);
        }
    }

    private static class OptionsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final CheckBox checkBox;
        private final TextView nameTv;
        private final EditText scoreEt;
        private OptionsItemClickListener listener;
        private final LinearLayout scoreLayout;

        OptionsViewHolder (boolean isLook, View itemView, OptionsItemClickListener listener, final TextChangedListener textChangedListener, IsShowCheced isShowCheced) {
            super (itemView);
            this.listener = listener;
            checkBox = (CheckBox) itemView.findViewById (R.id.cb_dock_score);
            nameTv = (TextView) itemView.findViewById (R.id.tv_dock_score_title);
            scoreEt = (EditText) itemView.findViewById (R.id.et_score);
            scoreLayout = (LinearLayout) itemView.findViewById (R.id.ll_score);

            if (isLook) {
                checkBox.setEnabled (false);
                scoreEt.setEnabled (false);
                return;
            }

            itemView.setOnClickListener (this);

            scoreEt.setOnFocusChangeListener ((view, b) -> {
                if (isShowCheced != null) {
                    isShowCheced.isShow (b, getLayoutPosition (), view);
                }
            });

            scoreEt.addTextChangedListener (new TextWatcher () {
                @Override
                public void beforeTextChanged (CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged (CharSequence s, int start, int before, int count) {
                }

                @Override
                public void afterTextChanged (Editable s) {
                    if (textChangedListener != null) {
                        textChangedListener.onChanged (s.toString (), getLayoutPosition (), scoreEt);
                    }
                }
            });

        }

        private static OptionsViewHolder create (boolean isLook, ViewGroup parent, OptionsItemClickListener listener, TextChangedListener textChangedListener, IsShowCheced isShowCheced) {
            View view = LayoutInflater.from (parent.getContext ()).inflate (R.layout.
                    adapter_dock_score_item_01, parent, false);
            return new OptionsViewHolder (isLook, view, listener, textChangedListener, isShowCheced);
        }

        @Override
        public void onClick (View v) {
            switch (v.getId ()) {
                default:
                    checkBox.setChecked (!checkBox.isChecked ());
                    if (listener != null) {
                        listener.onClick (checkBox.isChecked (), getLayoutPosition ());
                    }
                    break;
            }
        }
    }

    private interface OptionsItemClickListener {
        void onClick (boolean isCheck, int position);
    }

    private interface TextChangedListener {
        void onChanged (String text, int position, EditText editText);
    }

    private interface IsShowCheced {
        void isShow (boolean isShow, int position, View view);
    }

    public interface Scrolling {
        boolean isScrolling ();
    }
}
