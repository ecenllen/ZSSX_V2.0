package com.gta.zssx.fun.adjustCourse.view.page;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gta.zssx.R;
import com.gta.zssx.dormitory.view.page.DormitoryListActivity;
import com.gta.zssx.fun.adjustCourse.model.bean.ScheduleBean;
import com.gta.zssx.fun.adjustCourse.view.adapter.AllMessageAdapter;
import com.gta.zssx.fun.assetmanagement.view.page.AssetWebViewShowActivity;
import com.gta.zssx.pub.util.Screen;

import java.util.List;

/**
 * [Description]
 * <p>
 * [How to use]
 * <p>
 * [Tips]
 *
 * @author Created by Zhimin.Huang on 2016/11/14.
 * @since 1.0.0
 */
public class DetailSchedulePopupWindow implements View.OnClickListener {

    private Screen mScreen;
    private Context context;
    private PopupWindow popupWindow;
    private List<ScheduleBean.SectionBean> mBeanList;
    private String type;
    private String message;

    public DetailSchedulePopupWindow (Context context, String type) {
        this.context = context;
        this.type = type;
        if (type.equals ("adjust")) {
            mScreen = new Screen ((CourseScheduleActivity) context);
        } else if(type.equals("dormitory")){
            mScreen = new Screen ((DormitoryListActivity) context);
        } else {
            mScreen = new Screen ((AssetWebViewShowActivity) context);
        }
    }

    public DetailSchedulePopupWindow setmBeanList (List<ScheduleBean.SectionBean> mBeanList) {
        this.mBeanList = mBeanList;
        return this;
    }

    public DetailSchedulePopupWindow setMessage (String message) {
        this.message = message;
        return this;
    }

    public void onCreate () {
        View view = LayoutInflater.from (context).inflate (R.layout.activity_all_message, null, false);

        popupWindow = new PopupWindow (view, mScreen.getWidth (), mScreen.getHeight () - getStatusBarHeight (), true);

        popupWindow.setBackgroundDrawable (new ColorDrawable (Color.WHITE));

        popupWindow.setAnimationStyle (R.style.mypopwindow_anim_style);

        popupWindow.showAtLocation (view, Gravity.NO_GRAVITY, 0, getStatusBarHeight ());

        initView (view);
    }

    private void initView (View view) {
        RelativeLayout lExit = (RelativeLayout) view.findViewById (R.id.rl_exit);
        if (type.equals ("adjust")) {
            RecyclerView lRecyclerView = (RecyclerView) view.findViewById (R.id.recyclerView);
            lRecyclerView.setVisibility (View.VISIBLE);
            lRecyclerView.setLayoutManager (new LinearLayoutManager (context));
            lRecyclerView.setAdapter (new AllMessageAdapter (context, mBeanList));
        } else {
            TextView tv_message = (TextView) view.findViewById (R.id.tv_message);
            tv_message.setVisibility (View.VISIBLE);
            tv_message.setText (message);
        }


        lExit.setOnClickListener (this);
    }


    private int getStatusBarHeight () {
        int result = 0;
        int resourceId = context.getResources ().getIdentifier ("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources ().getDimensionPixelSize (resourceId);
        }
        return result;
    }

    @Override
    public void onClick (View v) {
        switch (v.getId ()) {
            default:
            case R.id.rl_exit:
                popupWindow.dismiss ();
                break;
        }
    }
}
