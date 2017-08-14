package com.gta.zssx.pub.util;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.TextView;

import com.gta.zssx.R;

/**
 * Created by weiye.chen on 2017/7/21.
 */

public class DormitoryItemNameDialog {
    public static void dormitoryItemNameDialog(Activity context, String content) {
        Dialog dialog = new Dialog(context, R.style.dormitory_itemName_dialog);
        dialog.setCanceledOnTouchOutside(true);
        TextView textView = new TextView(context);
        textView.setText(content);
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(Color.WHITE);
        textView.setTextSize(18);
        dialog.setContentView(textView);
        dialog.show();
    }
}
