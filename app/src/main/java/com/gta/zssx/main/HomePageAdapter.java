package com.gta.zssx.main;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gta.zssx.R;
import com.gta.zssx.fun.personalcenter.model.bean.UserBean;

import java.util.List;

/**
 * 首页列表Adapter;
 * Created by liang.lu on 2017/7/19 13:01.
 */

class HomePageAdapter extends RecyclerView.Adapter {
    private Context mContext;
    private List<UserBean.MenuItemListBean> menuItemListBeen;
    private HomePageItemListener mListener;

    HomePageAdapter (Context context, HomePageItemListener listener) {
        super ();
        this.mContext = context;
        this.mListener = listener;
    }

    void setMenuItemListBeen (List<UserBean.MenuItemListBean> menuItemListBeen) {
        this.menuItemListBeen = menuItemListBeen;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {
        return HomePageViewHolder.establish (parent, mContext, mListener);
    }

    @Override
    public void onBindViewHolder (RecyclerView.ViewHolder holder, int position) {
        HomePageViewHolder mHolder = (HomePageViewHolder) holder;
        UserBean.MenuItemListBean menuItemBean = menuItemListBeen.get (position);
        int pushCount = Integer.valueOf (menuItemBean.getPushCount ());
        String powerCode = menuItemBean.getPowerCode ();
        if (pushCount > 0) {
            mHolder.mPushCountTv.setVisibility (View.VISIBLE);
            mHolder.mPushCountTv.setText (menuItemBean.getPushCount ());
        } else {
            mHolder.mPushCountTv.setVisibility (View.GONE);
        }
        mHolder.setBackground (mHolder.mRelativeLayout, mHolder.mTitleTv, mHolder.mBackgroundTv, powerCode);
    }

    @Override
    public int getItemCount () {
        return menuItemListBeen.size () == 0 ? 0 : menuItemListBeen.size ();
    }

    private static class HomePageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        Context mContext;
        RelativeLayout mRelativeLayout;
        TextView mTitleTv;
        TextView mBackgroundTv;
        TextView mPushCountTv;
        HomePageItemListener listener;

        HomePageViewHolder (View itemView, Context context, HomePageItemListener listener) {
            super (itemView);
            this.mContext = context;
            this.listener = listener;
            mRelativeLayout = (RelativeLayout) itemView.findViewById (R.id.ll_adjust_course);
            mTitleTv = (TextView) itemView.findViewById (R.id.tv_title);
            mBackgroundTv = (TextView) itemView.findViewById (R.id.tv_background);
            mPushCountTv = (TextView) itemView.findViewById (R.id.indicator_adjust_course_home);
            mRelativeLayout.setOnClickListener (this);
        }

        private void setBackground (RelativeLayout relativeLayout, TextView mTitleTv, TextView mBackgroundTv, String powerCode) {
            String powerName;
            switch (powerCode) {
                case "B00":
                    powerName = "课堂日志";
                    set (relativeLayout, mTitleTv, mBackgroundTv, powerName, R.drawable.class_log_home_selector, R.drawable.classroom_log_2);
                    break;
                case "E00200":
                    powerName = "课堂巡视";
                    set (relativeLayout, mTitleTv, mBackgroundTv, powerName, R.drawable.tour_class_home_selector, R.drawable.tour_class_3);
                    break;
                case "200":
                    powerName = "OA协同";
                    set (relativeLayout, mTitleTv, mBackgroundTv, powerName, R.drawable.oa_home_selector, R.drawable.oa_6);
                    break;
                case "0106":
                    powerName = "调代课";
                    set (relativeLayout, mTitleTv, mBackgroundTv, powerName, R.drawable.adjust_course_home_selector, R.drawable.the_substitute_5);
                    break;
                case "ZC":
                    powerName = "资产管理";
                    set (relativeLayout, mTitleTv, mBackgroundTv, powerName, R.drawable.assert_home_selector, R.drawable.assets_management_4);
                    break;
                case "0107":
                    powerName = "课表查询";
                    set (relativeLayout, mTitleTv, mBackgroundTv, powerName, R.drawable.adjust_course_home_selector, R.drawable.schedule_the_query_1);
                    break;
                case "406402":
                    powerName = "宿舍管理";
                    set (relativeLayout, mTitleTv, mBackgroundTv, powerName, R.drawable.dormitory_home_selector, R.drawable.ic_dormitory_main);
                    break;
                case "E00300":
                    powerName = "课堂教学反馈";
                    set (relativeLayout, mTitleTv, mBackgroundTv, powerName, R.drawable.classroom_home_selector, R.drawable.icon_classroom_main);
                    break;
                default:
                    break;
            }
        }

        private void set (RelativeLayout relativeLayout, TextView mTitleTv, TextView mBackgroundTv, String powerName, int idBackground, int idImage) {
            relativeLayout.setBackgroundResource (idBackground);
            mTitleTv.setText (powerName);
            mBackgroundTv.setBackgroundResource (idImage);
        }

        private static HomePageViewHolder establish (ViewGroup parent, Context context, HomePageItemListener listener) {
            View view = LayoutInflater.from (context).inflate (R.layout.adapter_item_home_page, parent, false);
            return new HomePageViewHolder (view, context, listener);
        }

        @Override
        public void onClick (View v) {
            listener.clickHomePageItem (getLayoutPosition ());
        }
    }

    interface HomePageItemListener {
        void clickHomePageItem (int position);
    }
}
