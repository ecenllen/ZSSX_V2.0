package com.gta.zssx.pub.base;

import android.content.Context;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.LinearLayout;

import com.gta.utils.mvp.BasePresenter;
import com.gta.utils.mvp.BaseView;
import com.gta.zssx.R;
import com.gta.zssx.pub.manager.ToolBarManager;
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
public abstract class BaseMvpActivity<V extends BaseView, P extends BasePresenter<V>> extends BaseActivity<V, P> {

    protected ToolBarManager mToolBarManager;
    private View mRootView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (isNeedToolbar()) {
            setContentView(R.layout.activity_base);

            LinearLayout lBaseLayout = (LinearLayout) findViewById(R.id.base_activity_ll);
            View lInflate = getLayoutInflater().inflate(getLayoutId(), lBaseLayout, false);
            assert lBaseLayout != null;
            lBaseLayout.addView(lInflate);
            assert lInflate != null;
//            mRootView = (View) lInflate.getParent();
            Toolbar lToolbar = (Toolbar) findViewById(R.id.toolbar);
            mToolBarManager = new ToolBarManager(lToolbar, this).init();
            mToolBarManager.getToolbar().setNavigationOnClickListener(v -> onBackPressed());
        } else {
            setContentView(getLayoutId());
        }
        mRootView = getWindow().getDecorView();
        initWindow();
        ButterKnife.bind(this);
        initData();
        initView();
        requestData();
    }

    //获取布局文件
    @LayoutRes
    public abstract int getLayoutId();

    //初始化View
    protected abstract void initView();

    //初始化数据
    protected  void initData(){

    }

    protected void initWindow() {
    }

    protected void requestData(){

    }

    @Override
    public void showDialog(String title, String message, boolean isCancelable) {
        // 新的加载样式
        LoadingBar.make(mRootView, message).show();
    }

    @Override
    public void showOnFailReloading(String error) {
        if(TextUtils.isEmpty(error)) error = getResourceString(R.string.string_request_fail_reload);
        // 新的请求失败样式
        LoadingBar.make(mRootView, new ConnectionFailFactory(), error).setOnReloadButtonListener(v -> {
            hideDialog();
            requestData();
        }).show();
    }

    @Override
    public void showErrorLong(String error) {
        super.showErrorLong(error);

    }

    @Override
    public void hideDialog() {
        LoadingBar.cancel(mRootView);
    }

    /**
     *重写可控制是否需要标题栏
     */
    protected boolean isNeedToolbar() {
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
        if(mToolBarManager != null)
            mToolBarManager.destroy();
        LoadingBar.cancelAll();
        // 不知道有没有效
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        try {
            InputMethodManager.class.getDeclaredMethod("windowDismissed", IBinder.class).invoke(imm,
                    getWindow().getDecorView().getWindowToken());
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
