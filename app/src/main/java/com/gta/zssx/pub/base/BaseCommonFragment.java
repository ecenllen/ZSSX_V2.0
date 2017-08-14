package com.gta.zssx.pub.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;

/**
 * [Description]
 * <p>
 * [How to use]
 * <p>
 * [Tips]
 *
 * @author Created by Zhimin.Huang on 2017/3/15.
 * @since 1.0.0
 */
public abstract class BaseCommonFragment extends Fragment {

    public Bundle mBundle;
    public View mRootView;
    private boolean isNeedRequest = true;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mRootView != null) {
            isNeedRequest = false;
            ViewGroup parent = (ViewGroup) mRootView.getParent();
            if (parent != null)
                parent.removeView(mRootView);
        } else {
            isNeedRequest = true;
            mRootView = LayoutInflater.from(getActivity()).inflate(getLayoutId(), container, false);
            ButterKnife.bind(this, mRootView);
            // Get savedInstanceState
            if (savedInstanceState != null)
                onRestartInstance(savedInstanceState);
            initView(mRootView);
        }
        return mRootView;
    }

    private void onRestartInstance(Bundle savedInstanceState) {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBundle = getArguments();
        if(mBundle != null)
         initBundle(mBundle);
    }

    //获取布局文件
    public abstract int getLayoutId();

    //初始化View
    protected abstract void initView(View view);

    //初始化数据,比如界面间传递数据
    protected void initData() {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        initData();
    }


    protected void initBundle(Bundle bundle) {

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (isNeedRequest)
            requestData();
    }

    //网络请求,注意网络请求放到这个回调
    protected void requestData() {

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
        mBundle = null;
    }
}
