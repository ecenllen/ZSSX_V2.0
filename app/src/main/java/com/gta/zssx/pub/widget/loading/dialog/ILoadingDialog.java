package com.gta.zssx.pub.widget.loading.dialog;

import android.app.Dialog;

import com.gta.zssx.pub.widget.loading.ILoading;


/**
 * author  dengyuhan
 * created 2017/4/16 03:25
 */
public interface ILoadingDialog extends ILoading {


    Dialog create();

    ILoadingDialog setCancelable(boolean flag);

    /**
     * 设置Message
     * @param message
     * @return
     */
    ILoadingDialog setMessage(CharSequence message);


}
