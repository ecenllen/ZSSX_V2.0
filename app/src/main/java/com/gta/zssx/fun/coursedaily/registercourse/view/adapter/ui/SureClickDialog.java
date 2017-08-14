package com.gta.zssx.fun.coursedaily.registercourse.view.adapter.ui;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gta.zssx.R;
import com.gta.zssx.fun.coursedaily.registercourse.model.bean.SectionBean;

import java.util.List;

/**
 * [Description]
 * 只有确认button
 * [How to use]
 * isClickOutsideCanDismiss必须给值
 * [Tips]
 *
 * Created by lan.zheng on 2017/7/25 18:26.
 */

public class SureClickDialog extends Dialog {
    private Listener mListener;

    public SureClickDialog(Context context, String content, Listener listener){
        super(context, R.style.dormitory_itemName_dialog);
        init(context, content, true, listener);
    }

    public SureClickDialog(Context context, String content, boolean isClickOutsideCanDismiss,Listener listener){
        super(context, R.style.dormitory_itemName_dialog);
        init(context, content, isClickOutsideCanDismiss, listener);
    }

    private void init(Context context, String content, final boolean isClickOutsideCanDismiss, Listener listener) {
        mListener = listener;
        View contentView = LayoutInflater.from(context).inflate(R.layout.dialog_have_been_sign_section_show, null);
        TextView contentTextView = (TextView) contentView.findViewById(R.id.dialog_content_text);
        contentTextView.setText(content);
        TextView sureButton  = (TextView) contentView.findViewById(R.id.tv_sure);

        //消失监听
        this.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if(mListener != null)
                    mListener.onDialogDismissListener();
            }
        });
        //确认
        sureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if(mListener != null)
                    mListener.onSureListerner();
            }
        });
        setCancelable(isClickOutsideCanDismiss);
        setCanceledOnTouchOutside(isClickOutsideCanDismiss);
        setContentView(contentView);
    }

    public interface Listener {
        void onDialogDismissListener();
        void onSureListerner();
    }
}

