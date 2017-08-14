package com.gta.zssx.fun.assetmanagement.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.gta.zssx.AppConfiguration;
import com.gta.zssx.R;
import com.gta.zssx.fun.personalcenter.model.bean.UserBean;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by lan.zheng on 2016/8/8.
 */
public class AssetItemListAdapter extends RecyclerView.Adapter {

    private Map<String, String> mStringMap;
    private Context mContext;
    private Listener mListener;
    private String[] mStrings;
    private UserBean mUserBean;

    public AssetItemListAdapter (Context context, Listener listener, String[] strings) {
        try {
            mUserBean = AppConfiguration.getInstance ().getUserBean ();
        } catch (Exception e) {
            e.printStackTrace ();
        }
        mStringMap = new HashMap<> ();
        mStringMap.put (context.getString (R.string.text_applying_a_post), "http://" + AppConfiguration.getInstance().getServerAddress().getIp() + context.getString (R.string.url_applying_a_post) + getInfo ());
        mStringMap.put (context.getString (R.string.text_deal_with_post), context.getString (R.string.url_deal_with_post) + getInfo ());
        mStringMap.put (context.getString (R.string.text_search_asset), context.getString (R.string.url_search_asset));
        mStringMap.put (context.getString (R.string.text_distribute_asset), context.getString (R.string.url_distribute_asset) + getInfo ());
        mStringMap.put (context.getString (R.string.text_maintain_asset), context.getString (R.string.url_maintain_asset) + getInfo ());
        mStringMap.put (context.getString (R.string.text_check_asset), context.getString (R.string.url_check_asset) + getInfo ());
        mContext = context;
        mListener = listener;
        mStrings = strings;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {
        return DisplayHolder.newHolder (parent, mContext);
    }

    @Override
    public void onBindViewHolder (RecyclerView.ViewHolder holder, int position) {
        DisplayHolder lHolder = (DisplayHolder) holder;
        //设置值
        if (mStrings[position].equals (mContext.getString (R.string.text_applying_a_post))) {
            lHolder.list_item_show.setBackgroundDrawable (mContext.getResources ().getDrawable (R.drawable.ic_asset_applying_for));
        } else if (mStrings[position].equals (mContext.getString (R.string.text_deal_with_post))) {
            lHolder.list_item_show.setBackgroundDrawable (mContext.getResources ().getDrawable (R.drawable.ic_asset_deal_with));
        } else if (mStrings[position].equals (mContext.getString (R.string.text_search_asset))) {
            lHolder.list_item_show.setBackgroundDrawable (mContext.getResources ().getDrawable (R.drawable.ic_asset_view));
            lHolder.tv_list_item_show.setVisibility (View.GONE);
        } else if (mStrings[position].equals (mContext.getString (R.string.text_distribute_asset))) {
            lHolder.list_item_show.setBackgroundDrawable (mContext.getResources ().getDrawable (R.drawable.ic_asset_distribution));
        } else if (mStrings[position].equals (mContext.getString (R.string.text_maintain_asset))) {
            lHolder.list_item_show.setBackgroundDrawable (mContext.getResources ().getDrawable (R.drawable.ic_asset_maintenance));
        } else if (mStrings[position].equals (mContext.getString (R.string.text_check_asset))) {
            lHolder.list_item_show.setBackgroundDrawable (mContext.getResources ().getDrawable (R.drawable.ic_asset_inventory));
        }

        //设置监听
        lHolder.layout_container.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                if (mListener != null) {
//                    String url = "";
//                    Set set = mStringMap.entrySet ();
//                    Iterator iter = set.iterator ();
//                    while (iter.hasNext ()) {
//                        Map.Entry entry = (Map.Entry) iter.next ();
//                        String key = (String) entry.getKey ();
//                        if (key.equals (mStrings[position])) {
//                            url = (String) entry.getValue ();
//                            LogUtil.Log ("lenita", "key = " + key);
//                        }
//                    }
                    String url = mStringMap.get(mStrings[position]);
                    
                    mListener.itemClick (url);
                }
            }
        });
    }

    @Override
    public int getItemCount () {
        return mStrings.length;
    }

    public interface Listener {
        void itemClick (String s);
    }

    private String getInfo () {
        String userName = mUserBean.getUserName ();
        String passWord = mUserBean.getMD5Password();
        return "?username=" + userName + "&password=" + passWord + "&platForm=MOBILE";
    }

    private static class DisplayHolder extends RecyclerView.ViewHolder {

        FrameLayout layout_container;
        TextView tv_list_item_show;
        ImageView list_item_show;

        public DisplayHolder (Context context, View itemView) {
            super (itemView);
            layout_container = (FrameLayout) itemView.findViewById (R.id.layout_container);
            list_item_show = (ImageView) itemView.findViewById (R.id.list_item_show);
            tv_list_item_show = (TextView) itemView.findViewById (R.id.tv_list_item_show);
        }

        private static DisplayHolder newHolder (ViewGroup parent, Context context) {
            View view = LayoutInflater.from (context).inflate (R.layout.list_item_asset_grid_view, parent, false);
            return new DisplayHolder (context, view);
        }
    }

}
