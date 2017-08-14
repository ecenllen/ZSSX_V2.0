package com.gta.zssx.patrolclass.view.dialog;

import android.content.Context;

/**
 * [Description]
 * <p/>
 * [How to use]
 * <p/>
 * [Tips] Created by tengfei.lv on 2016/7/19 11:38.
 */

public class DeleteDialogFactory {

    private DeleteDialog dialog;
    private static DeleteDialogFactory factory;

    private static class SingletonHolder {
        private static DeleteDialogFactory INSTANCE = new DeleteDialogFactory ();
    }

    private DeleteDialogFactory () {
    }

    public static DeleteDialogFactory getInstance () {
        if (factory == null) {
            factory = SingletonHolder.INSTANCE;
        }
        return factory;
    }

    /**
     * 显示对话框，适用于有2个按钮或者一个按钮
     *
     * @param isShowExit  true 显示右边退出按钮（文字可以设置）
     * @param confirmText 右边按钮上的文字
     * @param msg         提示信息
     * @param listener    右边按钮点击监听器
     */
    public void showDialog (Context context, boolean isShowExit, String confirmText, String msg, DeleteDialog.OnErrorDialogClickListener listener, boolean isShowMainColor) {
        if (dialog != null) {
            if (dialog.isShowing ()) {
                dialog.dismiss ();
            }
            dialog = null;
        }
        dialog = new DeleteDialog (context);
        dialog.show ();
        dialog.setIsShowExit (isShowExit);
        dialog.setConfirmTvText (confirmText, isShowMainColor);
        dialog.setErrorMsgTvText (msg);
        dialog.setTitleTvText (null);
        dialog.setCanceledOnTouchOutside (false);
        dialog.setListener (listener);
    }
}
