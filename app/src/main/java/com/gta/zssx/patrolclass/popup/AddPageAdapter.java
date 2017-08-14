package com.gta.zssx.patrolclass.popup;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gta.zssx.R;
import com.gta.zssx.fun.personalcenter.model.bean.UserBean;

import java.util.List;

/**
 * Created by liang.lu on 2017/7/20 14:55.
 */

class AddPageAdapter extends RecyclerView.Adapter {
    private Context mContext;
    private List<UserBean.MenuItemListBean> mSmallIconsList;
    private SmallIconsClickListener mListener;

    AddPageAdapter (Context context, List<UserBean.MenuItemListBean> listBeen, SmallIconsClickListener listener) {
        super ();
        this.mContext = context;
        this.mSmallIconsList = listBeen;
        this.mListener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {
        return AddPageViewHolder.establish (parent, mContext, mListener);
    }

    @Override
    public void onBindViewHolder (RecyclerView.ViewHolder holder, int position) {
        AddPageViewHolder mHolder = (AddPageViewHolder) holder;
        UserBean.MenuItemListBean bean = mSmallIconsList.get (position);
        String powerName = bean.getPowerName ();
        mHolder.setBackground (mHolder.mPictureTv, powerName);
    }

    @Override
    public int getItemCount () {
        return mSmallIconsList == null ? 0 : mSmallIconsList.size ();
    }

    private static class AddPageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        Context mContext;
        SmallIconsClickListener mListener;
        TextView mPictureTv;

        AddPageViewHolder (View itemView, Context context, SmallIconsClickListener listener) {
            super (itemView);
            this.mContext = context;
            this.mListener = listener;
            mPictureTv = (TextView) itemView.findViewById (R.id.tv_adjust_course);
            mPictureTv.setOnClickListener (this);
        }

        private static AddPageViewHolder establish (ViewGroup parent, Context context, SmallIconsClickListener listener) {
            View view = LayoutInflater.from (context).inflate (R.layout.adapter_item_add_page, parent, false);
            return new AddPageViewHolder (view, context, listener);
        }

        @Override
        public void onClick (View v) {
            mListener.clickSmallIconItem (getLayoutPosition ());
        }

        private void setBackground (TextView textView, String powerName) {
            switch (powerName) {
                case "日志登记":
                    set (powerName, textView, R.drawable.log_register_sel_bg);
                    break;
                case "随机巡课":
                    set (powerName, textView, R.drawable.random_class_sel_bg);
                    break;
                case "按计划巡课":
                    set (powerName, textView, R.drawable.plan_class_sel_bg);
                    break;
                case "调代课申请":
                    set (powerName, textView, R.drawable.the_substitute_for);
                    break;
                case "资产分配":
                    set (powerName, textView, R.drawable.tab_distribution);
                    break;
                case "资产转移":
                    set (powerName, textView, R.drawable.tab_transfer);
                    break;
                case "资产维修":
                    set (powerName, textView, R.drawable.tab_repair);
                    break;
                case "资产报废":
                    set (powerName, textView, R.drawable.tab_scrap);
                    break;
                default:
                    break;
            }
        }

        private void set (String powerName, TextView textView, int drawableId) {
            textView.setText (powerName);
            Drawable topDrawable = mContext.getResources ().getDrawable (drawableId);
            topDrawable.setBounds (0, 0, topDrawable.getMinimumWidth (), topDrawable.getMinimumHeight ());
            textView.setCompoundDrawables (null, topDrawable, null, null);
        }
    }

    interface SmallIconsClickListener {
        void clickSmallIconItem (int position);
    }
}
