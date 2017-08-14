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
import com.gta.zssx.fun.coursedaily.registercourse.model.bean.SectionStudentListBean;
import com.gta.zssx.fun.coursedaily.registercourse.model.bean.StudentListNewBean;


/**
 * [Description]
 * 显示学生信息弹框
 * [How to use]
 *
 * [Tips]
 *
 * Created by lan.zheng on 2017/7/25 18:25.
 */

public class StudentInfoDialog extends Dialog {
    private Listener mListener;
    private StudentListNewBean mStudentListNewBean;

    public StudentInfoDialog(Context context) {
        super(context);
    }

    public StudentInfoDialog(Context context, StudentListNewBean studentListNewBean,Listener listener){
        super(context, R.style.dormitory_itemName_dialog);
        mListener = listener;
        mStudentListNewBean = studentListNewBean;
        View contentView = LayoutInflater.from(context).inflate(R.layout.dialog_student_info_show, null);
        TextView nameTextView = (TextView) contentView.findViewById(R.id.tv_student_name_info);
        TextView sexTextView = (TextView)contentView.findViewById(R.id.tv_student_sex_info);
        TextView noTextView = (TextView)contentView.findViewById(R.id.tv_student_no_info);
        TextView sureTextView  = (TextView)contentView.findViewById(R.id.tv_student_info_sure);
        nameTextView.setText(studentListNewBean.getStundentName());
        String sex = studentListNewBean.getStudentSex() == 1?"男":"女";
        sexTextView.setText(sex);
        noTextView.setText(studentListNewBean.getStudentNO());
        sureTextView.setOnClickListener(new View.OnClickListener() {
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


    public interface Listener {
        void onDialogDismissListener();
    }
}
