package com.gta.zssx.pub.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.gta.utils.mvp.BasePresenter;
import com.gta.utils.mvp.BaseView;
import com.gta.zssx.R;
import com.gta.zssx.pub.widget.loading.bar.LoadingBar;
import com.gta.zssx.pub.widget.loading.factory.ConnectionFailFactory;

import butterknife.ButterKnife;

/**
 * [Description]
 * <p>
 * [How to use]
 * <p>
 * [Tips]
 *
 * @author Created by Zhimin.Huang on 2017/3/6.
 * @since 1.0.0
 */
public abstract class BaseMvpFragment<V extends BaseView, P extends BasePresenter<V>> extends BaseFragment<V, P> {

    public Bundle mBundle;
    public View mRootView;
    private boolean isNeedRequest = true;
    protected View mLoadingBaseView;

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
            if(findSuitableParent(mRootView) == null) {
                mLoadingBaseView = getActivity().getWindow().getDecorView();
            } else
                mLoadingBaseView = mRootView;
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
        initBundle(mBundle);
    }


    //获取布局文件
    public abstract int getLayoutId();

    //初始化View，注意当mRootView不为空时不会触发此方法
    protected abstract void initView(View view);

    //初始化数据,比如界面间传递数据
    protected void initData() {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        mLoadingBaseView = getActivity().getWindow ().getDecorView ();
        initData();
    }

    public View getmRootView() {
        return mLoadingBaseView;
    }

    @Override
    public void showDialog(String title, String message, boolean isCancelable) {
        // 新的加载样式
        LoadingBar.make(getmRootView(), message).show();
    }

    @Override
    public void showOnFailReloading(String error) {
        if (TextUtils.isEmpty(error))
            error = getResourceString(R.string.string_request_fail_reload);
        // 新的请求失败样式
        LoadingBar.make(getmRootView(), new ConnectionFailFactory(), error).setOnReloadButtonListener(v -> {
            hideDialog();
            requestData();
        }).show();
    }

    @Override
    public void hideDialog() {
        LoadingBar.cancel(getmRootView());
    }


    protected void initBundle(Bundle bundle) {

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.attachView((V) BaseMvpFragment.this);
        if (isNeedRequest)
            requestData();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && isNeedRequest) {
//            requestData();
        }
    }

    //网络请求,注意网络请求放到这个函数
    protected void requestData() {

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (presenter != null)
            presenter.detachView(false);
        ButterKnife.unbind(this);
        mBundle = null;
        LoadingBar.cancel(getmRootView());
    }

    /**
     * 找到合适的父布局
     *
     * @param parent
     * @return
     */
    private ViewGroup findSuitableParent(View parent) {
        if (parent == null) {
            return null;
        }
        if (parent instanceof FrameLayout || parent instanceof RelativeLayout ||
                "android.support.v4.widget.DrawerLayout".equals(parent.getClass().getName()) ||
                "android.support.design.widget.CoordinatorLayout".equals(parent.getClass().getName()) ||
                "android.support.v7.widget.CardView".equals(parent.getClass().getName())) {
            return (ViewGroup) parent;
        }
        return null;
    }
}
