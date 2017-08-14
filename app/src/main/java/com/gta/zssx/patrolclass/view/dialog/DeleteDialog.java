package com.gta.zssx.patrolclass.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.gta.zssx.R;


/**
 * 对话框，可以显示确定（可变）和取消按钮
 * 默认点击确定和取消按钮对话框会消失
 * 如果需要在点击确定时做其他操作，需要实现OnErrorDialogClickListener接口
 * 如果 需要 在对话框消失的时候做操作时，需要实现OnDialogDismissListener接口
 * <p>
 * Created by tengfei.lv on 2016/6/17 13:10.
 */
public class DeleteDialog extends Dialog implements View.OnClickListener {

    private Context mContext;
    private TextView titleTv;
    private TextView errorMsgTv;
    private TextView confirmTv;
    private TextView exitTv;
    private View divideView;
    private OnErrorDialogClickListener listener;
    private OnDialogDismissListener dismissListener;

    public DeleteDialog (Context context) {
        super (context, R.style.center_dialog);
        mContext = context;

    }

    public void setListener (OnErrorDialogClickListener listener) {
        this.listener = listener;
    }

    public void setDismissListener (OnDialogDismissListener dismissListener) {
        this.dismissListener = dismissListener;
    }

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.layout_input_error_dialog);

        titleTv = (TextView) findViewById (R.id.tv_dialog_title);
        errorMsgTv = (TextView) findViewById (R.id.tv_dialog_msg);
        divideView = findViewById (R.id.view_dialog);

        exitTv = (TextView) findViewById (R.id.tv_dialog_exit);
        confirmTv = (TextView) findViewById (R.id.btn_dialog_confirm);
        exitTv.setText ("取消");
        exitTv.setOnClickListener (this);
        confirmTv.setOnClickListener (this);

        //        setOnDismissListener (new OnDismissListener () {
        //            @Override
        //            public void onDismiss (DialogInterface dialog) {
        //                if (dismissListener != null) {
        //                    dismissListener.onDismiss ();
        //                }
        //            }
        //        });

    }

    void setTitleTvText (String text) {
        if (text != null) {
            if (!text.equals ("")) {
                titleTv.setText (text);
                titleTv.setVisibility (View.VISIBLE);
            } else {
                titleTv.setVisibility (View.GONE);
            }
        } else {
            titleTv.setVisibility (View.GONE);
        }
    }

    void setErrorMsgTvText (String text) {
        errorMsgTv.setText (text);
    }

    void setConfirmTvText (String text, boolean isShowMianColor) {
        confirmTv.setText (text);
        if (isShowMianColor) {
            confirmTv.setTextColor (mContext.getResources ().getColor (R.color.main_color));
        }
    }

    void setIsShowExit (boolean isShowExit) {
        if (isShowExit) {
            titleTv.setVisibility (View.VISIBLE);
            divideView.setVisibility (View.VISIBLE);
            exitTv.setVisibility (View.VISIBLE);
        } else {
            //设置确认按钮的背景
            confirmTv.setBackgroundResource (R.drawable.selector_white_press_radiu_left_right_bottom);
        }
    }

    public void setExitText (String text) {
        exitTv.setText (text);
        exitTv.setTextColor (mContext.getResources ().getColor (R.color.main_color));
    }

    @Override
    public void onClick (View v) {
        switch (v.getId ()) {
            case R.id.tv_dialog_exit:
                if (dismissListener != null) {
                    dismissListener.onDismiss ();
                }
                break;
            case R.id.btn_dialog_confirm:
                if (listener != null) {
                    listener.onConfirmClick ();
                }
                break;
        }
        this.dismiss ();
    }

    public void showDialog (boolean isShowExit, String confirmText, String msg, DeleteDialog.OnErrorDialogClickListener listener, boolean isShowMainColor) {
        if (isShowing ()) {
            dismiss ();
        }
        show ();

        setIsShowExit (isShowExit);
        setConfirmTvText (confirmText, isShowMainColor);
        setErrorMsgTvText (msg);
        setTitleTvText (null);
        setCanceledOnTouchOutside (false);
        setCancelable(false);
        setListener (listener);

    }

    public void showDialog (String titleMsg, boolean isShowExit, String confirmText, String msg, OnErrorDialogClickListener listener, OnDialogDismissListener dismissListener, boolean isShowMainColor) {
        if (isShowing ()) {
            dismiss ();
        }
        show ();

        setIsShowExit (isShowExit);
        setConfirmTvText (confirmText, isShowMainColor);
        setErrorMsgTvText (msg);
        setTitleTvText (titleMsg);
        setCanceledOnTouchOutside (false);
        setListener (listener);
        setDismissListener (dismissListener);

    }

    private void setIsShowTitle (boolean isShowTitle) {
        titleTv.setVisibility (isShowTitle ? View.VISIBLE : View.GONE);
    }

    public interface OnErrorDialogClickListener {
        void onConfirmClick ();
    }

    public interface OnDialogDismissListener {
        void onDismiss ();
    }
}
