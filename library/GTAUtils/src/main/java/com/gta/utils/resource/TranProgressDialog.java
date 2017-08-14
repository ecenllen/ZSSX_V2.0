/**
 * @author Woode Wang E-mail:wangwoode@qq.com
 * @version 创建时间：2015-2-13 下午3:21:13
 */
package com.gta.utils.resource;

import android.app.Dialog;
import android.content.Context;
import android.view.ViewGroup.LayoutParams;
import android.widget.ProgressBar;

import com.example.gtalutils.R;


/**
 * @description
 * @author
 * @since 1.0.0
 */
public class TranProgressDialog extends Dialog {

    public static TranProgressDialog show(Context context) {
        return show(context, null, true, null);
    }

    public static TranProgressDialog show(Context context, CharSequence title, boolean cancelable,
                                          OnCancelListener cancelListener) {
        TranProgressDialog dialog = new TranProgressDialog(context);
        dialog.setTitle(title);
        dialog.setCancelable(cancelable);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setOnCancelListener(cancelListener);
        /* The next line will add the ProgressBar to the dialog. */
        ProgressBar lView = new ProgressBar(context);
        dialog.addContentView(lView, new LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT));
        dialog.show();

        return dialog;
    }

    // public MyProgressDialog(Context context) {
    // super(context);
    // }
    //
    public TranProgressDialog(Context context) {
        super(context, R.style.TranDialog);
    }
}