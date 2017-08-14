package com.gta.zssx.mobileOA.view.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.gta.zssx.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lan.zheng on 2016/8/30.
 */
public class ListPopupWindowAdapter extends BaseAdapter {
    private List<String> mStringList = new ArrayList<>();
    private Context mContext;
    private String nowSeleteName;
//    private int itemHeight;

    public ListPopupWindowAdapter(Context context, List<String> list, String selectName){
        mContext = context;
        mStringList = list;
        nowSeleteName = selectName;
    }

    @Override
    public int getCount() {
        return mStringList.size();
    }

    @Override
    public Object getItem(int i) {
        return mStringList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder lViewHolder = null;  //一开始为null
        if(view  == null){
            lViewHolder = new ViewHolder();
            view = View.inflate(mContext, R.layout.list_item_title_content,null);
            lViewHolder.itemTextView = (TextView) view.findViewById(R.id.tv_title_content);
            view.setTag(lViewHolder);
        }else {
            lViewHolder = (ViewHolder) view.getTag();
        }
        //文字内容设置
        lViewHolder.itemTextView.setText(mStringList.get(i));
        return view;
    }

    private class ViewHolder {
        TextView itemTextView;
    }
}
