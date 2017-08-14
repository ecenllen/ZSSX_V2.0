package com.gta.zssx.patrolclass.popup;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.gta.zssx.R;

/**
 * [Description]  出现随机巡课和按计划巡课的popup
 * <p/>
 * [How to use]
 * <p/>
 * [Tips] Created by tengfei.lv on 2016/7/13 17:39.
 */

public class AddProtalPopupWindow implements View.OnClickListener{
    private Context context;
    private View dropView;
    private int[] location;
    private PopupWindowItemClickListener listener;
    private PopupWindow popupWindow;


    public void setListener (PopupWindowItemClickListener listener) {
        this.listener = listener;
    }

    public AddProtalPopupWindow (Context context, View view, int[] x) {
        dropView = view;
        location =  x;
        this.context = context;
        onCreate();
    }


    private void onCreate(){
        View view = LayoutInflater.from (context).inflate (R.layout.popup_layout_add_protal, null, false);

        popupWindow = new PopupWindow (view, LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,true);

        popupWindow.setBackgroundDrawable (new BitmapDrawable ());

        popupWindow.showAsDropDown (dropView, location[0],-18);

        LinearLayout planProtalLayout = (LinearLayout) view.findViewById(R.id.layout_plan_protal);
        LinearLayout randomProtalLayout = (LinearLayout) view.findViewById(R.id.layout_random_protal);

        planProtalLayout.setOnClickListener (this);
        randomProtalLayout.setOnClickListener (this);
    }

    @Override
    public void onClick (View v) {
        switch (v.getId ()){
            case R.id.layout_plan_protal:
                if(listener!=null){
                    listener.clickPlanItem ();
                }
                break;
            case R.id.layout_random_protal:
                if(listener!=null){
                    listener.clickRandomItem ();
                }
                break;
        }
        popupWindow.dismiss ();
    }

    public interface PopupWindowItemClickListener{
        void clickPlanItem();
        void clickRandomItem();
    }
}
