package com.gta.zssx.fun.coursedaily.registercourse.view.adapter.ui;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gta.zssx.R;
import com.gta.zssx.fun.coursedaily.registercourse.model.bean.SectionBean;

import java.util.List;

/**
 * [Description]
 * 用于只有一部分节次能登记的情况
 * [How to use]
 *
 * [Tips]
 *
 * Created by lan.zheng on 2017/7/25 18:19.
 */

public class IsNormalSubmitCanSignPartDataDialog extends Dialog {
    private Listener mListener;

    public IsNormalSubmitCanSignPartDataDialog(Context context) {
        super(context);
    }

    public IsNormalSubmitCanSignPartDataDialog(Context context,  List<String> stringList, Listener listener){
        super(context, R.style.myDialogTheme);
        mListener = listener;
        View contentView = LayoutInflater.from(context).inflate(R.layout.dialog_delete_comfirn, null);
        LinearLayout linearLayout = (LinearLayout)contentView.findViewById(R.id.content_layout);
        linearLayout.setBackground(context.getResources().getDrawable(R.color.half_transparent));  //全屏背景半透明,并且点击不能消失
        TextView contentTextView = (TextView) contentView.findViewById(R.id.dialog_content_text);
        String content = "第";
        for(int i = 0;i<stringList.size();i++){
            if(i != 0){
                content += ",";
            }
            content += stringList.get(i);
        }
        content += "节已经被登记过，是否放弃已被登记的节次，并提交其他节次的登记?";
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

    @Override
    public void show() {
        super.show();
        /**
         * 设置宽度全屏，要设置在show的后面
         */
        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.width= ViewGroup.LayoutParams.MATCH_PARENT;
        layoutParams.height= ViewGroup.LayoutParams.MATCH_PARENT;
        getWindow().getDecorView().setPadding(0, 0, 0, 0);
        getWindow().setAttributes(layoutParams);
    }

    public interface Listener {
        void onDialogDismissListener();
        void onSureListerner();
    }
}

