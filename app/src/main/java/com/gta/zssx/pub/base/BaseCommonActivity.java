package com.gta.zssx.pub.base;

import android.content.Context;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;

import com.gta.zssx.R;
import com.gta.zssx.pub.manager.ToolBarManager;

import butterknife.ButterKnife;

/**
 * [Description]
 * <p>
 * [How to use]
 * <p>
 * [Tips]
 *
 * @author Created by Zhimin.Huang on 2017/3/20.
 * @since 1.0.0
 */
public abstract class BaseCommonActivity extends BaseAppCompatActivity {

    protected ToolBarManager mToolBarManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (isNeedToolbar()) {
            setContentView(R.layout.activity_base);

            LinearLayout lBaseLayout = (LinearLayout) findViewById(R.id.base_activity_ll);
            View lInflate = getLayoutInflater().inflate(getLayoutId(), lBaseLayout, false);
            assert lBaseLayout != null;
            lBaseLayout.addView(lInflate);
            Toolbar lToolbar = (Toolbar) findViewById(R.id.toolbar);
            mToolBarManager = new ToolBarManager(lToolbar, this).init();
            mToolBarManager.getToolbar().setNavigationOnClickListener(v -> onBackPressed());
        } else {
            setContentView(getLayoutId());
        }
        initWindow();
        ButterKnife.bind(this);
        initData();
        initView();
        requestData();
    }

    protected void requestData() {

    }

    //获取布局文件
    public abstract int getLayoutId();

    //初始化View
    protected abstract void initView();

    //初始化数据
    protected  void initData(){

    }

    protected void initWindow() {
    }

    protected  boolean isNeedToolbar(){
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
