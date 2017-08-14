package com.gta.zssx.patrolclass.view.dialog;

import android.content.Context;

import com.bigkoo.pickerview.TimePickerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * [Description]
 * <p/>  显示时间选择器
 * [How to use]
 * <p/>
 * [Tips] Created by tengfei.lv on 2016/7/19 17:57.
 */

public class DatePickerView {
    /**
     * @param context
     * @param beginDate 起始时间
     * @param listener  时间选择好之后的监听器
     */
    public static void showView (Context context, String beginDate, final TimePickerView.OnTimeSelectListener listener) {
        TimePickerView mPickerView = new TimePickerView (context, TimePickerView.Type.YEAR_MONTH_DAY);
        if (beginDate != null) {
            SimpleDateFormat sdf = new SimpleDateFormat ("yyyy年MM月dd日");
            Date lDate = null;
            try {
                lDate = sdf.parse (beginDate);
            } catch (ParseException e) {
                e.printStackTrace ();
            }
            mPickerView.setTime (lDate);
        }

        mPickerView.setCyclic (true);
        mPickerView.setCancelable(true);
        mPickerView.setOnTimeSelectListener (listener);
        mPickerView.setCancelable(true);
        mPickerView.show ();
    }

}
