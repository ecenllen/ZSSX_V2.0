package com.gta.zssx.pub.widget.loading.factory;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Build;
import android.view.View;

import com.gta.zssx.R;


/**
 * author  dengyuhan
 * created 2017/4/16 04:08
 */
public class MaterialDialogFactory implements DialogFactory<ProgressDialog> {

    @Override
    public ProgressDialog onCreateDialog(Context context) {
        return onCreateDialog(context, null);
    }

    @Override
    public ProgressDialog onCreateDialog(Context context, View view) {
        ProgressDialog dialog = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            dialog = new ProgressDialog(context, R.style.Widget_AppCompat_ProgressBar);
        } else {
            dialog = new ProgressDialog(context);
            dialog.setProgressStyle(android.support.v7.appcompat.R.style.Widget_AppCompat_ProgressBar);
        }
//        dialog.setMessage(context.getText(R.string.loading_default_message));
        dialog.setCancelable(false);
        if(null != view) dialog.setContentView(view);
        return dialog;
    }

    @Override
    public void setMessage(ProgressDialog dialog, CharSequence message) {
        dialog.setMessage(message);
    }

    @Override
    public int getAnimateStyleId() {
        return 0;
    }
}
