package com.gta.zssx.mobileOA.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gta.zssx.R;

/**
 * Created by lan.zheng on 2016/10/13.
 */
public class MobileOAItemListAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private Listener mListener;
    private String[] mStrings;
    private int mSingleItemWidth;
    private int[] mIcon;
    private int[] counts;

    public MobileOAItemListAdapter(Context context, Listener listener, String[] strings, int singleItemWidth) {
        mContext = context;
        mListener = listener;
        mStrings = strings;
        mSingleItemWidth = singleItemWidth;
        mIcon = new int[]{R.drawable.ic_oa_apply_icon, R.drawable.ic_oa_deal_icon, R.drawable.ic_oa_official_notice_icon,
                R.drawable.ic_oa_meeting_icon, R.drawable.ic_oa_schudule_plan_icon,
                R.drawable.ic_oa_event_remind_icon};
//        mIcon = new int[]{R.drawable.ic_oa_apply_icon, R.drawable.ic_oa_deal_icon, R.drawable.ic_oa_official_notice_icon,
//                R.drawable.ic_oa_meeting_icon, R.drawable.ic_oa_schudule_plan_icon, R.drawable.ic_oa_arrangement_icon,
//                R.drawable.ic_oa_duty_icon, R.drawable.ic_oa_mailbox_icon, R.drawable.ic_oa_contact_list_icon, R.drawable.ic_oa_event_remind_icon};
    }

    public void updateRemindCount(int[] counts) {
        this.counts = counts;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return DisplayHolder.newHolder(parent, mContext);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        DisplayHolder lHolder = (DisplayHolder) holder;
        //设置值
//        ViewGroup.LayoutParams layoutParams = lHolder.layout_item.getLayoutParams();
        lHolder.layout_item.setMinimumWidth(mSingleItemWidth);
        lHolder.list_item_show.setBackgroundResource(mIcon[position]);
        if (!TextUtils.isEmpty(mStrings[position])) {
            lHolder.tv_oa_item_name.setText(mStrings[position]);
        }
        if (counts != null && counts[position] != 0) {
            if (position != 5) {
                lHolder.tv_list_item_show.setVisibility(View.VISIBLE);
                lHolder.tv_list_item_show.setText(String.valueOf(counts[position]));
                lHolder.iv_event_indicator.setVisibility(View.GONE);
            }else{
                lHolder.tv_list_item_show.setVisibility(View.GONE);
            }
        } else {
            lHolder.tv_list_item_show.setVisibility(View.INVISIBLE);
            lHolder.iv_event_indicator.setVisibility(View.GONE);
        }
        //设置监听
        lHolder.layout_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.itemClick(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mStrings.length;
    }

    public interface Listener {
        void itemClick(int position);
    }

    private static class DisplayHolder extends RecyclerView.ViewHolder {
        LinearLayout layout_item;
        FrameLayout layout_container;
        TextView tv_list_item_show;
        ImageView list_item_show;
        TextView tv_oa_item_name;
        ImageView iv_event_indicator;

        public DisplayHolder(Context context, View itemView) {
            super(itemView);
            layout_item = (LinearLayout) itemView.findViewById(R.id.layout_item);
            layout_container = (FrameLayout) itemView.findViewById(R.id.layout_container);
            list_item_show = (ImageView) itemView.findViewById(R.id.list_item_show);
            tv_list_item_show = (TextView) itemView.findViewById(R.id.tv_list_item_show);
            tv_oa_item_name = (TextView) itemView.findViewById(R.id.tv_oa_item_name);
            iv_event_indicator = (ImageView) itemView.findViewById(R.id.iv_event_indicator);
        }

        private static DisplayHolder newHolder(ViewGroup parent, Context context) {
            View view = LayoutInflater.from(context).inflate(R.layout.list_item_oa_grid_view, parent, false);
            return new DisplayHolder(context, view);
        }
    }
}
