package com.gta.zssx.mobileOA.view.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gta.zssx.R;
import com.gta.zssx.ZSSXApplication;
import com.gta.zssx.mobileOA.presenter.BaseOAPresenter;
import com.gta.zssx.mobileOA.view.BaseOAView;
import com.gta.zssx.pub.base.BaseActivity;
import com.gta.zssx.pub.manager.ToolBarManager;

import java.lang.ref.WeakReference;

/**
 * Created by lan.zheng on 2016/10/26.
 */
public class BaseOAActivity extends BaseActivity<BaseOAView, BaseOAPresenter>
        implements BaseOAView {

    public static final String PAGE_TAG = BaseOAActivity.class.getSimpleName();
    public static final int RESID = R.id.container;
    public ToolBarManager mToolBarManager;
    public Toolbar mToolbar;
    public String mGo_To;
    public Toolbar selectTitleToolbar;  //用于有选择项的标题栏

    @NonNull
    @Override
    public BaseOAPresenter createPresenter() {
        return new BaseOAPresenter();
    }

    public WeakReference<BaseOAFragment> mMyBaseFragmentWeakReference;

    public void setMyBaseFragmentWeakReference(BaseOAFragment myBaseFragment) {
        mMyBaseFragmentWeakReference = new WeakReference<>(myBaseFragment);
    }

    public ToolBarManager getToolBarManager() {
        return mToolBarManager;
    }

    //无数据参数
    public static void start(Context context, String go_to) {
        final Intent lIntent = new Intent(context, BaseOAActivity.class)
                .putExtra(ZSSXApplication.GOTO, go_to);
        if (!(context instanceof Activity)) {
            lIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(lIntent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oa_base_page);
        initView();
    }

    private void initView() {
        mGo_To = getIntent().getStringExtra(ZSSXApplication.GOTO);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolBarManager = new ToolBarManager(mToolbar, this);
        mToolBarManager.init();  //其他的设置进入到Fragment再给标题
    }

    private TextView todayTextView;
    private TextView selectYearTextView;
    private ImageView searchImageView;
    /**
     * 以下方法用于有选择的标题栏切换，用于“日程”模块
     * @param toolbar
     */
    public void setSelectTitleToolbar(Toolbar toolbar){
        selectTitleToolbar = toolbar;
        todayTextView = (TextView) selectTitleToolbar.findViewById(R.id.iv_right_three);
        selectYearTextView =(TextView) selectTitleToolbar.findViewById(R.id.iv_right_two);
        searchImageView = (ImageView) selectTitleToolbar.findViewById(R.id.iv_search);
    }

    public void setTodayTextViewListener(View.OnClickListener listener){
        todayTextView.setOnClickListener(listener);
    }

    public void setSelectYearTextViewListener(View.OnClickListener listener){
        selectYearTextView.setOnClickListener(listener);
    }

    public void setSearchImageViewListener(View.OnClickListener listener){
        searchImageView.setOnClickListener(listener);
    }

    /**
     * 以下方法用于“公文公告”,当页面生成和切换改变的时候用
     */
    private int position = 0;

    public void setShowTitlePage(int p){
        position = p;
    }

    public int getShowTitlePage(){
        return position;
    }


    /**
     * 以下方法用于“个人日程”的Fragment恢复
     */
    private boolean isOpen = false;

    public void setOpenOnResume(boolean open){
        isOpen = open;
    }

    public boolean getOpenOnResume(){
        return isOpen;
    }

}
