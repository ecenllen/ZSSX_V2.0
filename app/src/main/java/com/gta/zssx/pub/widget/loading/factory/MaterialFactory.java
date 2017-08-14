package com.gta.zssx.pub.widget.loading.factory;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gta.zssx.R;


/**
 * Material默认loading样式
 * author  dengyuhan
 * created 2017/4/15 23:04
 */
public class MaterialFactory implements LoadingFactory{
    private TextView textView;

    @Override
    public View onCreateView(ViewGroup parent) {
        if(parent == null) return null;
        View loadingView = LayoutInflater.from(parent.getContext().getApplicationContext()).inflate(R.layout.base_loading_progress, parent,false);
        loadingView.setOnTouchListener((v, event) -> true);
        textView = (TextView) loadingView.findViewById(R.id.tv_load_text);
        return loadingView;
    }

    public void setMessage(String message) {
        if(textView != null && !TextUtils.isEmpty(message)) textView.setText(message);
    }
}
