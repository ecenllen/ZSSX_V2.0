package com.gta.zssx.patrolclass.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.gta.zssx.R;

/**
 * [Description]
 * <p/>
 * [How to use]
 * <p/>
 * [Tips] Created by tengfei.lv on 2016/8/4 14:48.
 */

public class BottomDialog extends Dialog implements View.OnClickListener{

    private BottomDialogClickListener listener;

    public void setListener (BottomDialogClickListener listener) {
        this.listener = listener;
    }

    public BottomDialog (Context context) {
        super (context,R.style.bottomDialog);
    }

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.popup_window_change_photo);

        findView();
    }

    private void findView () {
        TextView modifyTv = (TextView) findViewById(R.id.tv_modify);
        TextView deleteTv = (TextView) findViewById(R.id.tv_delete);
        TextView cancelTv = (TextView) findViewById(R.id.tv_cancel_show);

        modifyTv.setOnClickListener (this);
        deleteTv.setOnClickListener (this);
        cancelTv.setOnClickListener (this);
    }


    @Override
    public void onClick (View v) {
        switch (v.getId ()){
            case R.id.tv_modify:
                listener.modifyClick ();
                break;
            case R.id.tv_delete:
                listener.deleteClick ();
                break;
            case R.id.tv_cancel_show:
                break;
        }
        dismiss ();
    }

    public interface BottomDialogClickListener{
        void modifyClick();
        void deleteClick();
    }
    
    public void showBottomDialog(BottomDialog.BottomDialogClickListener listener) {
        if (isShowing()) {
            dismiss ();
        }
        show ();
        setListener (listener);
        setCanceledOnTouchOutside (true);

        
    }

    @Override
    public void show () {
        super.show ();

        Window window = getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.BOTTOM;
        wlp.width = ViewGroup.LayoutParams.MATCH_PARENT;
        window.setAttributes(wlp);
    }
}
