package com.gta.zssx.patrolclass.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.gta.zssx.patrolclass.model.entity.PatrolRegisterBeanNew;

import java.util.List;

/**
 * Created by liang.lu on 2017/3/10 17:34.
 */

public class DockScoreNewAdapter2 extends
        BaseMultiItemQuickAdapter<PatrolRegisterBeanNew.TeacherListBean.DockScoreBean.OptionsBean, BaseViewHolder> {

    private InputScoreErrorListener mInputScoreErrorListener;
    private RecyclerView mRecyclerView;
    private List<PatrolRegisterBeanNew.TeacherListBean.DockScoreBean.OptionsBean> data;
    private DockScoreListChangedListener mListChangedListener;

    public void setListChangedListener (DockScoreListChangedListener mListChangedListener) {
        this.mListChangedListener = mListChangedListener;
    }

    public void setRecyclerView (RecyclerView recyclerView) {
        mRecyclerView = recyclerView;
    }

    public void setInputScoreErrorListener (InputScoreErrorListener inputScoreErrorListener) {
        mInputScoreErrorListener = inputScoreErrorListener;
    }

    public DockScoreNewAdapter2 (
            List<PatrolRegisterBeanNew.TeacherListBean.DockScoreBean.OptionsBean> data) {
        super (data);
        this.data = data;
        addItemType (1, com.gta.zssx.R.layout.adapter_dock_score_item_title);
        addItemType (0, com.gta.zssx.R.layout.adapter_dock_score_item_01);
    }

    @Override
    protected void convert (BaseViewHolder baseViewHolder,
                            PatrolRegisterBeanNew.TeacherListBean.DockScoreBean.OptionsBean optionsBean) {

        switch (baseViewHolder.getItemViewType ()) {
            case 1: //标题
                doTitle (baseViewHolder, optionsBean);
                break;
            case 0:  //选项
                doOption (baseViewHolder, optionsBean);
                break;
        }
    }

    private void doOption (BaseViewHolder baseViewHolder,
                           PatrolRegisterBeanNew.TeacherListBean.DockScoreBean.OptionsBean optionsBean) {

        EditText etScore = baseViewHolder.getView (com.gta.zssx.R.id.et_score);
        if (etScore.getTag () instanceof TextWatcher) {
            etScore.removeTextChangedListener ((TextWatcher) etScore.getTag ());
        }

        baseViewHolder.setText (com.gta.zssx.R.id.tv_dock_score_title, optionsBean.getTitle ());
        //如果是选中状态，设置checkbox为选中，并且显示得分
        //得分的显示情况有：1.输入了空串，或者是第一次选中item时，那么显示的分数为服务器返回的getScore或者是score的分数
        //2.输入了正常的数字，显示即可
        if (optionsBean.isIsCheck ()) {
            baseViewHolder.setChecked (com.gta.zssx.R.id.cb_dock_score, true);
            String score = "";
            //如果是之前输入了空串，或者是还没有修改过分值，那么就用getScore或者score的属性值代替
            if (optionsBean.isInputNull ()
                    || optionsBean.getInputScore () == null
                    || optionsBean.getInputScore ().equals ("-1")) {
                score = optionsBean.getGetScore ().equals ("") ? optionsBean.getScore ()
                        : optionsBean.getGetScore ();

                baseViewHolder.setText (com.gta.zssx.R.id.et_score, score);
                optionsBean.setInputScore (score);
            } else {
                baseViewHolder.setText (com.gta.zssx.R.id.et_score, optionsBean.getInputScore ());
            }
            optionsBean.setInputNull (etScore.getText ().toString ().equals (""));
        } else {
            baseViewHolder.setChecked (com.gta.zssx.R.id.cb_dock_score, false);
            optionsBean.setInputNull (false);
        }
        //根据选中情况是否显示分数的view
        baseViewHolder.setVisible (com.gta.zssx.R.id.ll_score, optionsBean.isIsCheck ());

        //选中情况下设置监听器
        if (optionsBean.isIsCheck ()) {

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
                    if (beforeText[0].equals (ss.toString ()) ||
                            etScore.getText ().toString ().equals (optionsBean.getInputScore ()) &&
                                    !ss.toString ().equals ("0")) {
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
                    //更改之前的数字
                    int beforeValue = Integer.valueOf (beforeText[0]);
                    //更改之后的數字
                    String text = s.toString ();
                    if ("".equals (text)) {
                        text = "0";
                    }

                    int itemScore = Integer.parseInt (text);
                    //根据输入的分数，来计算大标题的总得分，计算方法为当前的总得分+修改之前的分数-修改之后的分数
                    for (int i = baseViewHolder.getLayoutPosition (); i >= 0; i--) {
                        if (getItem (i).isIsTitle ()) {
                            //获取标题当前的得分
                            String getScore = getItem (i).getGetScore ();
                            int titleScore = Integer.parseInt (getScore);
                            titleScore = titleScore - itemScore + beforeValue;

                            if (titleScore < 0) {
                                mInputScoreErrorListener.onInputScoreError (
                                        baseViewHolder.getLayoutPosition ());
                                etScore.setText (optionsBean.getInputScore ());
                                etScore.setSelection (optionsBean.getInputScore ().length ());
                                return;
                            }
                            getItem (i).setGetScore (titleScore + "");
                            notifyItemChanged (i);
                            break;
                        }
                    }

                    optionsBean.setInputNull ("".equals (s.toString ()));
                    optionsBean.setInputScore ("".equals (s.toString ()) ? "0" : text);

                    etScore.setText (s);
                    etScore.setSelection (s.length ());
                    mListChangedListener.setList (data);
                }
            };

            etScore.setTag (textWatcher);
            etScore.addTextChangedListener (textWatcher);
        }

        baseViewHolder.convertView.setOnClickListener (v -> {
            if (optionsBean.isIsCheck () && !optionsBean.getGetScore ().equals ("")) {
                optionsBean.setGetScore ("");
            }
            optionsBean.setIsCheck (!optionsBean.isIsCheck ());
            notifyItemChanged (baseViewHolder.getLayoutPosition ());
            String score = optionsBean.getGetScore ().equals ("") ? optionsBean.getScore ()
                    : optionsBean.getGetScore ();

            for (int i = baseViewHolder.getLayoutPosition (); i >= 0; i--) {
                if (getItem (i).isIsTitle ()) {  //如果是标题，改变总分
                    //当前标题的总分
                    String getScore = getItem (i).getGetScore ();
                    //当前item的得分
                    String itemScore = optionsBean.getInputScore () != null
                            && optionsBean.getInputScore ().length () > 0
                            && Integer.parseInt (optionsBean.getInputScore ()) >= 0
                            ? optionsBean.getInputScore () : score;

                    int titleScore = Integer.parseInt (getScore);
                    int itemScore1 = Integer.parseInt (itemScore);
                    //如果选中，用总分减去optionsBean的得分

                    if (optionsBean.isIsCheck ()) {
                        if (optionsBean.isInputNull ()) {
                            itemScore1 = Integer.parseInt (score);
                        }
                        titleScore -= itemScore1;
                        if (titleScore < 0) {
                            //提示用户输入错误
                            mInputScoreErrorListener.onInputScoreError (
                                    baseViewHolder.getLayoutPosition ());
                            optionsBean.setInputScore ("0");
                            etScore.setText ("0");
                            return;
                        }
                    } else {  //如果没有选中，就加上optionsBean的得分
                        if (optionsBean.isInputNull ()) {
                            itemScore1 = 0;
                        }
                        titleScore += itemScore1;
                    }
                    getItem (i).setGetScore (titleScore + "");
                    notifyItemChanged (i);
                    break;
                }
            }
        });

        etScore.setOnFocusChangeListener ((v, hasFocus) -> {
            if (etScore.getText ().toString ().equals ("") && !hasFocus) {
                optionsBean.setIsCheck (false);
                if (mRecyclerView.getScrollState () == RecyclerView.SCROLL_STATE_IDLE && !mRecyclerView.isComputingLayout ()) {
                    notifyItemChanged (baseViewHolder.getLayoutPosition ());
                }
            }
        });
        mListChangedListener.setList (data);
    }

    private void doTitle (BaseViewHolder baseViewHolder,
                          PatrolRegisterBeanNew.TeacherListBean.DockScoreBean.OptionsBean optionsBean) {
        baseViewHolder.setText (com.gta.zssx.R.id.tv_dock_score_adapter_title, optionsBean.getTitle ())
                .setText (com.gta.zssx.R.id.tv_dock_score_adapter_score, optionsBean.getGetScore ())
                .setTextColor (com.gta.zssx.R.id.tv_dock_score_adapter_score, optionsBean.getGetScore ().
                        equals (optionsBean.getAllScore ()) ? mContext.getResources ()
                        .getColor (com.gta.zssx.R.color.black) : mContext.getResources ().getColor (com.gta.zssx.R.color.red));
    }

    /**
     * option扣分项输入框监听
     */
    public interface InputScoreErrorListener {
        void onInputScoreError (int position);
    }

    /**
     * 把数据data回传给Activity
     */
    public interface DockScoreListChangedListener {
        void setList (List<PatrolRegisterBeanNew.TeacherListBean.DockScoreBean.OptionsBean> data);
    }
}
