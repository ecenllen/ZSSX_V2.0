package com.gta.zssx.patrolclass.view.dialog;

import android.content.Context;

/**
 * [Description]
 * <p/>
 * [How to use]
 * <p/>
 * [Tips] Created by tengfei.lv on 2016/8/4 14:59.
 */

public class BottomDialogFactory {

    private BottomDialog dialog;
    public static BottomDialogFactory _instance;

//    private static class SingletonHolder {
//        private static final BottomDialogFactory INSTANCE = new BottomDialogFactory ();
//    }

    private BottomDialogFactory () {
    }

//    public static BottomDialogFactory getInstance () {
//        if (_instance == null) {
//            _instance = SingletonHolder.INSTANCE;
//        }
//        return _instance;
//    }

    public void showDialog (Context context, BottomDialog.BottomDialogClickListener listener) {
        if (dialog != null) {
            dialog.dismiss ();
            dialog = null;
        }
        dialog = new BottomDialog (context);

        dialog.setListener (listener);
        dialog.setCanceledOnTouchOutside (true);

        dialog.show ();
    }
}
