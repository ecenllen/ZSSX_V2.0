package com.gta.zssx.pub.widget.loading.factory;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.gta.zssx.R;


/**
 * Material默认loading样式
 * author  dengyuhan
 * created 2017/4/15 23:04
 */
public class ConnectionFailFactory implements LoadingFactory{
    private TextView textView;

    @Override
    public View onCreateView(ViewGroup parent) {
        if(parent == null) return null;
        View loadingView = LayoutInflater.from(parent.getContext().getApplicationContext()).inflate(R.layout.base_loading_fail, parent,false);
        textView = (TextView) loadingView.findViewById(R.id.tv_load_fail_text);
        return loadingView;
    }

    public void setMessage(String msg) {
        if(textView != null) textView.setText(msg);
    }

}
