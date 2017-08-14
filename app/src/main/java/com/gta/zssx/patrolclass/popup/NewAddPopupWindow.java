package com.gta.zssx.patrolclass.popup;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.gta.zssx.R;
import com.gta.zssx.fun.assetmanagement.view.page.AssetManagementActivity;
import com.gta.zssx.fun.coursedaily.registercourse.view.pageV2.CourseDailyActivity;
import com.gta.zssx.main.HomePageActivity;
import com.gta.zssx.patrolclass.view.page.PatrolClassActivity;
import com.gta.zssx.patrolclass.view.page.SearchResultActivity;
import com.gta.zssx.pub.util.Screen;

/**
 * [Description]
 * <p>
 * [How to use]
 * <p>
 * [Tips] Created by tengfei.lv on 2016/8/1 16:58.
 */

public class NewAddPopupWindow implements View.OnClickListener {

    private Screen mScreen;
    private Context context;
    private PopupWindow popupWindow;

    private NewAddPopupItemClickListener listener;

    public NewAddPopupWindow(Context context, NewAddPopupItemClickListener listener, int which) {
        this.context = context;
        this.listener = listener;
        if (which == 1) {
            mScreen = new Screen((HomePageActivity) context);
        } else if (which == 2) {
            mScreen = new Screen((PatrolClassActivity) context);
        } else if (which == 3) {
            mScreen = new Screen((SearchResultActivity) context);
        } else if (which == 4) {
            mScreen = new Screen((CourseDailyActivity) context);
        } else if (which == 5) {
            mScreen = new Screen((AssetManagementActivity) context);
        }
    }

    public void onCreate() {
        View view = LayoutInflater.from(context).inflate(R.layout.home_add_page, null, false);

        popupWindow = new PopupWindow(view, mScreen.getWidth(), mScreen.getHeight() - getStatusBarHeight(), true);

        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.WHITE));

        popupWindow.setAnimationStyle(R.style.mypopwindow_anim_style);

        popupWindow.showAtLocation(view, Gravity.NO_GRAVITY, 0, getStatusBarHeight());

        initView(view);
    }

    private void initView(View view) {
        AnimationTextView lLog_register = (AnimationTextView) view.findViewById(R.id.tv_log_register);
        AnimationTextView lRandom_patrol = (AnimationTextView) view.findViewById(R.id.tv_random_class);
        AnimationTextView lPlan_patrol = (AnimationTextView) view.findViewById(R.id.tv_plan_class);
        AnimationTextView lAsset_dispatch = (AnimationTextView) view.findViewById(R.id.tv_asset_dispatch);
        AnimationTextView lAsset_transform = (AnimationTextView) view.findViewById(R.id.tv_asset_transform);
        AnimationTextView lAsset_repair = (AnimationTextView) view.findViewById(R.id.tv_asset_repair);
        RelativeLayout lRl_exit = (RelativeLayout) view.findViewById(R.id.rl_exit);

        lLog_register.setOnClickListener(this);
        lRandom_patrol.setOnClickListener(this);
        lPlan_patrol.setOnClickListener(this);
        lAsset_dispatch.setOnClickListener(this);
        lAsset_transform.setOnClickListener(this);
        lAsset_repair.setOnClickListener(this);
        lRl_exit.setOnClickListener(this);
    }

    private int getStatusBarHeight() {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_log_register:
                //跳转到日志登记页面
                listener.judgeToLogRegister();
                break;
            case R.id.tv_random_class:
                //跳转到随机巡课页面
                listener.judgeToRandomClass();
                break;
            case R.id.tv_plan_class:
                //跳转到按计划巡课页面
                listener.judgeToPlanClass();
                break;
            case R.id.rl_exit:
                break;
            case R.id.tv_asset_dispatch:
                break;
            case R.id.tv_asset_transform:
                break;
            case R.id.tv_asset_repair:
                break;
            default:
                break;
        }
        popupWindow.dismiss();
        listener.exitClick();
    }

    public interface NewAddPopupItemClickListener {
        void judgeToRandomClass();

        void judgeToPlanClass();

        void judgeToLogRegister();

        void exitClick();
    }
}
