package com.gta.zssx.pub.widget;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.gta.zssx.R;

/**
 * [Description]
 * <p> 确定/删除，对话框
 * [How to use]
 * <p>
 * [Tips]
 * @author Create by Weiye.Chen on 2017/7/21.
 * @since 2.0.0
 */
public class ConfirmOrCancelDialog extends Dialog {

    public ConfirmOrCancelDialog(Context context, String content, Listener listener){
        super(context,R.style.dormitory_itemName_dialog);
        View contentView = LayoutInflater.from(context).inflate(R.layout.dialog_delete_comfirn, null);
        TextView contentTextView = (TextView) contentView.findViewById(R.id.dialog_content_text);
        contentTextView.setText(content);
        Button cancelButton = (Button)contentView.findViewById(R.id.dialog_btn_cancel);
        Button sureButton  = (Button)contentView.findViewById(R.id.dialog_btn_confirm);

        //确认
        sureButton.setOnClickListener(v -> {
            dismiss();
            if(listener != null)
                listener.onConfirmListener();
        });
        //取消
        cancelButton.setOnClickListener(v -> {
            dismiss();
            if(listener != null)
                listener.onCancelListener();
        });

        this.setOnDismissListener(dialog -> {
            if(listener != null)
                listener.onDismissListener();
        });
//        setCancelable(false);
        setContentView(contentView);
    }

    public interface Listener {
        void onCancelListener();
        void onConfirmListener();
        void onDismissListener();
    }
}
