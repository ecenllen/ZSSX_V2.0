package com.gta.zssx.fun.coursedaily.registercourse.view.adapter.ui;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gta.zssx.R;

/**
 * [Description]
 * 两个按钮，删除或取消弹框
 * [How to use]
 *
 * [Tips]
 *
 * Created by lan.zheng on 2017/7/25 18:19.
 */
public class IsDeleteSelectItemDialog extends Dialog {
    private Listener mListener;

    public IsDeleteSelectItemDialog(Context context) {
        super(context);
    }

    public IsDeleteSelectItemDialog(Context context, String content, Listener listener){
        super(context,R.style.dormitory_itemName_dialog);
        mListener = listener;
        View contentView = LayoutInflater.from(context).inflate(R.layout.dialog_delete_comfirn, null);
        TextView contentTextView = (TextView) contentView.findViewById(R.id.dialog_content_text);
        contentTextView.setText(content);
        Button cancelButton = (Button)contentView.findViewById(R.id.dialog_btn_cancel);
        Button sureButton  = (Button)contentView.findViewById(R.id.dialog_btn_confirm);

        //消失监听
        this.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                mListener.onDialogDismissListener();
            }
        });
        //确认
        sureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                mListener.onSureListerner();
            }
        });
        //取消
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        setContentView(contentView);
    }

    public interface Listener {
        void onDialogDismissListener();
        void onSureListerner();
    }
}
