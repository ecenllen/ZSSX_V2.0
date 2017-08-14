package com.gta.zssx.pub.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.gta.zssx.R;
import com.gta.zssx.patrolclass.wheelview.DatePicker;

/**
 * 底部弹出时间选择器
 * Created by xiao.peng on 2016/11/11.
 */
public class PickerTimeDialog extends Dialog {

    private String currentTime;
    private TextView tv;
    private OnClickConfirmListener listener;
    private DatePicker datePicker;

    public PickerTimeDialog(Context context, OnClickConfirmListener listener) {
        super(context, R.style.SelectDialogStyle);
        this.listener = listener;
    }

    public PickerTimeDialog(Context context, TextView tv, OnClickConfirmListener listener) {
        super(context, R.style.SelectDialogStyle);
        this.listener = listener;
        this.tv = tv;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //去掉标题
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_picker_time);
        //改变dialog的显示
        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
        window.setAttributes(params);

        TextView tvCancel = (TextView) findViewById(R.id.tv_cancel);
        TextView tvConfirm = (TextView) findViewById(R.id.tv_confirm);
        datePicker = (DatePicker) findViewById(R.id.birthday_picker);


        tvCancel.setOnClickListener(view -> dismiss());
        datePicker.setDateTimeSelectListener(() ->
                currentTime = datePicker.getYear() + "年" + datePicker.getMonth() + "月" + datePicker.getDay() + "日");
        tvConfirm.setOnClickListener(view -> {
            listener.onClickConfirm(currentTime, Integer.valueOf(datePicker.getYear()), Integer.valueOf(datePicker.getMonth()), Integer.valueOf(datePicker.getDay()));
            if(tv != null)
                tv.setText("< " + currentTime + " >");
            dismiss();
        });
    }

    /**
     * 设置时间
     */
    public void setPickerTime(int year, int month, int day) {
        currentTime = year + "年" + month + "月" + day + "日";
        datePicker.setCurrentSelected(year, month, day);
    }

    public int getLastDay(int year, int month) {
        if (datePicker == null) {
            //加载数据
            show();
            dismiss();
        }
        return datePicker.getLastDay(year, month);
    }

    public interface OnClickConfirmListener {
        void onClickConfirm(String currentTime, int year, int month, int day);
    }
}
