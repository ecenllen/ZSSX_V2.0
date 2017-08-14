package com.gta.zssx.fun.coursedaily.registercourse.view.adapter.ui;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.YuvImage;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gta.zssx.R;

/**
 * [Description]
 * 更新默认课程提示框
 * [How to use]
 *
 * [Tips]
 *
 * Created by lan.zheng on 2017/7/25 18:26.
 */

public class UpdateTeacherAndCourseDialog extends Dialog {
    private Listener mListener;

    public UpdateTeacherAndCourseDialog(Context context) {
        super(context);
    }

    public UpdateTeacherAndCourseDialog(Context context,String oldCourse, String newCourse, String oldTeacher, String newTeacher, String newDateandSection, Listener listener){
        super(context, R.style.myDialogTheme);
        mListener = listener;
        View contentView = LayoutInflater.from(context).inflate(R.layout.dialog_update_course_comfirn, null);
        LinearLayout linearLayout = (LinearLayout)contentView.findViewById(R.id.content_layout);
        linearLayout.setBackground(context.getResources().getDrawable(R.color.half_transparent));  //全屏背景半透明，并且点击不能消失
        TextView oldCourseTextView = (TextView) contentView.findViewById(R.id.tv_old_course_info);
        TextView oldTeacherTextView = (TextView) contentView.findViewById(R.id.tv_old_teacher_info);
        TextView updateTextView = (TextView)contentView.findViewById(R.id.tv_course_info_update_show);
        TextView newCourseTextView = (TextView)contentView.findViewById(R.id.tv_new_course_info);
        TextView newTeacherTextView = (TextView)contentView.findViewById(R.id.tv_new_teacher_info);
        oldCourseTextView.setText(oldCourse);
        oldTeacherTextView.setText(oldTeacher);
        updateTextView.setText(newDateandSection);
        newCourseTextView.setText(newCourse);
        newTeacherTextView.setText(newTeacher);
        View btnUpdate = contentView.findViewById(R.id.dialog_btn_confirm);
        View btnBack = contentView.findViewById(R.id.dialog_btn_cancel);
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                mListener.onUpdateDataClickListener();
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        //消失监听
        this.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                mListener.onDialogDismissListener();
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
        void onUpdateDataClickListener();
    }
}
