package com.gta.zssx.mobileOA.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gta.zssx.R;


/**
 * Created by lan.zheng on 2016/10/31.
 */
public class TitleSpinnerAdapter extends BaseAdapter {
    private String[] s;
    private Context mContecxt;
//    private onCheckListener mListener;

    public TitleSpinnerAdapter(Context context, String[] strings) {
        mContecxt = context;
        s = strings;
//        mListener = listener;
    }

    @Override
    public int getCount() {
        return s.length;
    }

    @Override
    public Object getItem(int position) {
        return s[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContecxt).inflate(R.layout.list_item_spinner_item,null);
            holder = new ViewHolder();
                    /*得到各个控件的对象*/
            holder.title_tv = (TextView) convertView.findViewById(R.id.title_tv);
            convertView.setTag(holder);//绑定ViewHolder对象
        } else {
            holder = (ViewHolder) convertView.getTag();//取出ViewHolder对象
        }
        holder.title_tv.setText(s[position]);
        return convertView;
    }

    public final class ViewHolder {
        private TextView title_tv;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContecxt).inflate(R.layout.list_item_spinner_drop_down,null);
            convertView.setMinimumWidth(LinearLayout.LayoutParams.WRAP_CONTENT);
            holder = new ViewHolder();
                    /*得到各个控件的对象*/
            holder.title_tv = (TextView) convertView.findViewById(R.id.title_tv);
            convertView.setTag(holder);//绑定ViewHolder对象
        } else {
            holder = (ViewHolder) convertView.getTag();//取出ViewHolder对象
        }
        holder.title_tv.setText(s[position]);
        return convertView;
    }


}