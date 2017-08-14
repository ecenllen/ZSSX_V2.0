package com.gta.zssx.patrolclass.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.gta.zssx.R;

import java.util.List;


/**
 * Created by liang.lu on 2017/3/21 14:22.
 */

public class ReuseScoreDialog extends Dialog implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {
    private RadioGroup mRadioGroup;
    private TextView mCancelTv;
    private TextView mOkTv;
    private List<String> mNames;
    private Context mContext;
    private ReuseScoreDialogListener mListener;
    private RadioButton mCheckRadio;


    public ReuseScoreDialog (Context context) {
        super (context, R.style.center_dialog);
        this.mContext = context;
    }



    public void setmNames (List<String> mNames) {
        this.mNames = mNames;
    }

    public void setmListener (ReuseScoreDialogListener mListener) {
        this.mListener = mListener;
    }

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.reuse_score_dialog);
        findViews ();
        loadData ();
        setonClick ();
    }

    private void loadData () {
        for (int i = 0; i < mNames.size (); i++) {
            RadioButton mRadioButton = new RadioButton (mContext);
            mRadioButton.setPadding (20, 20, 20, 20);
            mRadioButton.setButtonDrawable (R.drawable.checkbox_selector);
            mRadioButton.setOnCheckedChangeListener ((buttonView, isChecked) -> {
                if (isChecked) {
                    mOkTv.setEnabled (true);
                }
            });

            mRadioButton.setText (mNames.get (i));
            mRadioGroup.addView (mRadioButton, LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        }
    }

    private void setonClick () {
        mRadioGroup.setOnCheckedChangeListener (this);
        mCancelTv.setOnClickListener (this);
        mOkTv.setOnClickListener (this);
        mOkTv.setEnabled (false);
    }

    private void findViews () {
        mRadioGroup = (RadioGroup) findViewById (R.id.radio_group_reuse_score);
        mCancelTv = (TextView) findViewById (R.id.text_view_reuse_score_cancel);
        mOkTv = (TextView) findViewById (R.id.text_view_reuse_score_ok);
    }

    @Override
    public void onClick (View v) {
        switch (v.getId ()) {
            case R.id.text_view_reuse_score_cancel:
                //取消选择
                break;
            case R.id.text_view_reuse_score_ok:
                //确定
                mListener.onConfirm (mCheckRadio.getText ().toString ());
                break;
            default:
                break;
        }
        dismiss ();
    }

    @Override
    public void onCheckedChanged (RadioGroup group, int checkedId) {
        mCheckRadio = (RadioButton) findViewById (checkedId);
    }

    public interface ReuseScoreDialogListener {
        void onConfirm (String name);
    }
}
