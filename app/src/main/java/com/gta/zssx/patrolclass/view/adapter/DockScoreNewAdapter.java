package com.gta.zssx.patrolclass.view.adapter;

import android.content.Context;
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
import com.gta.zssx.patrolclass.model.entity.PatrolRegisterBeanNew;

import java.util.List;

/**
 * Created by liang.lu on 2017/3/10 17:34.
 */

public class DockScoreNewAdapter extends RecyclerView.Adapter {

    private List<PatrolRegisterBeanNew.TeacherListBean.DockScoreBean.OptionsBean> mOptionBeans;
    private Context context;
    private InputScoreErrorListener inputScoreErrorListener;
    private TitleTextChangeListener titleTextChangeListener;
    public static final String AFTER_CHANGED = "after_changed";
    public static final String IN_CHANGED = "in_changed";

    public void setListener (InputScoreErrorListener inputScoreErrorListener) {
        this.inputScoreErrorListener = inputScoreErrorListener;
    }

    public List<PatrolRegisterBeanNew.TeacherListBean.DockScoreBean.OptionsBean> getmOptionBeans () {
        return mOptionBeans;
    }

    public void setTitleTextChangeListener (TitleTextChangeListener titleTextChangeListener) {
        this.titleTextChangeListener = titleTextChangeListener;
    }

    public DockScoreNewAdapter (Context context) {
        this.context = context;
    }

    public void setmOptionBeans (List<PatrolRegisterBeanNew.TeacherListBean.DockScoreBean.OptionsBean> mOptionBeans) {
        this.mOptionBeans = mOptionBeans;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {
        if (viewType == ITEM_TYPE.OPTION_TYPE.ordinal ()) {
            return OptionsViewHolder.creat (parent, new OptionsItemClickListener () {
                @Override
                public void onClick (boolean isCheck, int position) {
                    PatrolRegisterBeanNew.TeacherListBean.DockScoreBean.OptionsBean optionsBean = mOptionBeans.get (position);
                    optionsBean.setIsCheck (isCheck);

                    for (int i = position; i >= 0; i--) {
                        if (mOptionBeans.get (i).isIsTitle ()) {
                            String allScore = mOptionBeans.get (i).getIncomeScore ();
                            String itemScore = getItemScore (optionsBean);
                            int score = 0;
                            if (isCheck) {
                                score = Integer.parseInt (allScore) - Integer.parseInt (itemScore);
                                mOptionBeans.get (i).setIncomeScore (score + "");
                            } else {
                                score = Integer.parseInt (allScore) + Integer.parseInt (itemScore);
                                mOptionBeans.get (i).setIncomeScore (score + "");
                            }
                            notifyItemChanged (i);
                            break;
                        }
                    }
                    notifyItemChanged (position);
                }
            }, new OnFocusChangeListener () {
                @Override
                public void onFocusChange (EditText editText, int position, boolean hasFocus) {
                    if (editText.getText ().toString ().equals ("") && !hasFocus) {
                        mOptionBeans.get (position).setIsCheck (false);
                        notifyItemChanged (position);
                    }
                }
            });
        }
        return TitleViewHolder.create (parent, titleTextChangeListener, mOptionBeans);
    }

    @Override
    public void onBindViewHolder (RecyclerView.ViewHolder holder, int position) {
        PatrolRegisterBeanNew.TeacherListBean.DockScoreBean.OptionsBean optionsBean = mOptionBeans.get (position);

        if (holder instanceof OptionsViewHolder) {
            boolean check = optionsBean.isIsCheck ();

            ((OptionsViewHolder) holder).checkBox.setChecked (check);
            ((OptionsViewHolder) holder).scoreLayout.setVisibility (View.GONE);
            ((OptionsViewHolder) holder).nameTv.setText (optionsBean.getTitle ());

            if (check) {

                if (((OptionsViewHolder) holder).scoreEt.getTag () instanceof TextWatcher) {
                    ((OptionsViewHolder) holder).scoreEt.removeTextChangedListener ((TextWatcher) ((OptionsViewHolder) holder).scoreEt.getTag ());
                }

                ((OptionsViewHolder) holder).scoreLayout.setVisibility (View.VISIBLE);

                ((OptionsViewHolder) holder).scoreEt.setText (getItemScore (optionsBean));

                final String[] beforeText = {""};
                TextWatcher textWatcher = new TextWatcher () {
                    @Override
                    public void beforeTextChanged (CharSequence s, int start, int count, int after) {
                        beforeText[0] = s.toString ();
                    }

                    @Override
                    public void onTextChanged (CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged (Editable ss) {
                        if (beforeText[0].equals (ss.toString ())) {
                            return;
                        }
                        CharSequence s = ss.toString ();

                        if (s.length () > 1) {
                            char fir = s.charAt (0);
                            if (fir == '0') {
                                if (beforeText[0].equals ("0") && s.charAt (1) == '0') {
                                    s = "0";
                                } else {
                                    s = s.subSequence (1, s.length ());
                                }
                            }
                        }

                        if ("".equals (beforeText[0])) {
                            beforeText[0] = "0";
                        }
                        int beforeValue = Integer.valueOf (beforeText[0]);
                        String text = s.toString ();
                        if ("".equals (text)) {
                            text = "0";
                        }
                        mOptionBeans.get (position).setChangeScore (text);
                        int changeScore = 0;

                        Integer integer = Integer.valueOf (text);
                        String incomeScore = "0";
                        for (int i = position; i >= 0; i--) {
                            if (mOptionBeans.get (i).isIsTitle ()) {
                                incomeScore = mOptionBeans.get (i).getIncomeScore ();
                                changeScore = Integer.valueOf (incomeScore) + beforeValue - integer;
                                mOptionBeans.get (i).setIncomeScore (changeScore + "");
                                notifyItemChanged (i);
                                break;
                            }
                        }

                        if (s.length () > 0) {
                            if (Integer.parseInt (s.toString ()) > Integer.parseInt (optionsBean.getAllScore ())) {
                                inputScoreErrorListener.onError (position);
                                ((OptionsViewHolder) holder).scoreEt.setText ("");
                                return;
                            }
                        }

                        ((OptionsViewHolder) holder).scoreEt.setText (s);
                        ((OptionsViewHolder) holder).scoreEt.setSelection (s.length ());


                    }
                };
                ((OptionsViewHolder) holder).scoreEt.addTextChangedListener (textWatcher);
                ((OptionsViewHolder) holder).scoreEt.setTag (textWatcher);

            }
        } else if (holder instanceof TitleViewHolder) {
            ((TitleViewHolder) holder).titleTv.setText (optionsBean.getTitle ());
            ((TitleViewHolder) holder).scoreTv.setText (optionsBean.getIncomeScore ());
            if (!optionsBean.getIncomeScore ().equals (optionsBean.getAllScore ())) {
                ((TitleViewHolder) holder).scoreTv.setTextColor (context.getResources ().getColor (R.color.red));
            } else {
                ((TitleViewHolder) holder).scoreTv.setTextColor (context.getResources ().getColor (R.color.black));
            }
        }
    }

    @Override
    public int getItemCount () {
        return mOptionBeans == null ? 0 : mOptionBeans.size ();
    }

    @Override
    public int getItemViewType (int position) {
        if (!mOptionBeans.get (position).isIsTitle ()) {
            return ITEM_TYPE.OPTION_TYPE.ordinal ();
        }
        return ITEM_TYPE.TITLE_TYPE.ordinal ();
    }

    private static class OptionsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final CheckBox checkBox;
        private final TextView nameTv;
        private final EditText scoreEt;
        private OptionsItemClickListener listener;
        private final LinearLayout scoreLayout;


        public OptionsViewHolder (View itemView, OptionsItemClickListener listener, OnFocusChangeListener onFocusChangeListener) {
            super (itemView);
            this.listener = listener;
            checkBox = (CheckBox) itemView.findViewById (R.id.cb_dock_score);
            nameTv = (TextView) itemView.findViewById (R.id.tv_dock_score_title);
            scoreEt = (EditText) itemView.findViewById (R.id.et_score);
            scoreLayout = (LinearLayout) itemView.findViewById (R.id.ll_score);

            itemView.setOnClickListener (this);
            scoreEt.setOnFocusChangeListener (new View.OnFocusChangeListener () {
                @Override
                public void onFocusChange (View v, boolean hasFocus) {
                    onFocusChangeListener.onFocusChange ((EditText) v, getLayoutPosition (), hasFocus);
                }
            });
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


        private static OptionsViewHolder creat (ViewGroup parent, OptionsItemClickListener listener, OnFocusChangeListener onFocusChangeListener) {
            View view = LayoutInflater.from (parent.getContext ()).inflate (R.layout.adapter_dock_score_item_01, parent, false);
            return new OptionsViewHolder (view, listener, onFocusChangeListener);
        }
    }

    private static class TitleViewHolder extends RecyclerView.ViewHolder implements TextWatcher {
        private final TextView titleTv;
        private final TextView scoreTv;
        private TitleTextChangeListener titleTextChangeListener;
        List<PatrolRegisterBeanNew.TeacherListBean.DockScoreBean.OptionsBean> mOptionBeans;

        public TitleViewHolder (View itemView, TitleTextChangeListener titleTextChangeListener, List<PatrolRegisterBeanNew.TeacherListBean.DockScoreBean.OptionsBean> mOptionBeans) {
            super (itemView);
            this.titleTextChangeListener = titleTextChangeListener;
            this.mOptionBeans = mOptionBeans;
            titleTv = (TextView) itemView.findViewById (R.id.tv_dock_score_adapter_title);
            scoreTv = (TextView) itemView.findViewById (R.id.tv_dock_score_adapter_score);
            scoreTv.addTextChangedListener (this);
        }

        @Override
        public void beforeTextChanged (CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged (CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged (Editable s) {
            if (titleTextChangeListener != null) {
                titleTextChangeListener.onChanged (mOptionBeans, getLayoutPosition ());
            }
        }

        private static TitleViewHolder create (ViewGroup parent, TitleTextChangeListener titleTextChangeListener, List<PatrolRegisterBeanNew.TeacherListBean.DockScoreBean.OptionsBean> mOptionBeans) {
            View view = LayoutInflater.from (parent.getContext ()).inflate (R.layout.
                    adapter_dock_score_item_title, parent, false);
            return new TitleViewHolder (view, titleTextChangeListener, mOptionBeans);
        }
    }

    private String getItemScore (PatrolRegisterBeanNew.TeacherListBean.DockScoreBean.OptionsBean optionsBean) {
        String singleScore = "";
        if (optionsBean.getChangeScore () != null && optionsBean.getChangeScore ().length () > 0) {
            singleScore = optionsBean.getChangeScore ();
        } else if (optionsBean.getGetScore () != null && optionsBean.getGetScore ().length () > 0) {
            singleScore = optionsBean.getGetScore ();
        } else {
            singleScore = optionsBean.getScore ();
        }

        return singleScore;
    }

    private interface OptionsItemClickListener {
        void onClick (boolean isCheck, int position);
    }

    public interface InputScoreErrorListener {
        void onError (int position);
    }

    public interface TitleTextChangeListener {
        void onChanged (List<PatrolRegisterBeanNew.TeacherListBean.DockScoreBean.OptionsBean> mOptionBeans, int position);
    }

    private interface OnFocusChangeListener {
        void onFocusChange (EditText editText, int position, boolean hasFocus);
    }

    enum ITEM_TYPE {
        TITLE_TYPE, OPTION_TYPE
    }

}
