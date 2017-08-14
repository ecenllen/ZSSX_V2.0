package com.gta.zssx.fun.coursedaily.registercourse.view.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import com.gta.zssx.fun.adjustCourse.model.ConfirmTestContract;


/**
 * Created by lan.zheng on 2017/3/24.
 */
public abstract class NoPresenterBaseFragment extends Fragment{
    private View lInflate;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(lInflate == null){
            lInflate = LayoutInflater.from(getActivity()).inflate(getLayoutId(), container, false);
        }

        initView(lInflate);
        return lInflate;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    //获取布局文件
    public abstract int getLayoutId();

    //初始化View
    protected abstract void initView(View view);

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
