package com.gta.zssx.fun.adjustCourse.view.page;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.gta.zssx.R;
import com.gta.zssx.fun.adjustCourse.model.bean.StuClassBean;
import com.gta.zssx.fun.adjustCourse.presenter.ChooseClassPresenter;
import com.gta.zssx.fun.adjustCourse.view.ChooseClassView;
import com.gta.zssx.fun.adjustCourse.view.adapter.ChooseClassAdapter;
import com.gta.zssx.pub.base.BaseActivity;
import com.gta.zssx.pub.manager.ToolBarManager;
import com.gta.zssx.pub.widget.easysidebar.EasyFloatingImageView;
import com.gta.zssx.pub.widget.easysidebar.EasyImageSection;
import com.gta.zssx.pub.widget.easysidebar.EasyRecyclerViewSidebar;
import com.gta.zssx.pub.widget.easysidebar.EasySection;

import java.util.List;

/**
 * [Description]
 * <p>
 * [How to use]
 * <p>
 * [Tips]
 *
 * @author Created by Zhimin.Huang on 2016/11/3.
 * @since 1.0.0
 */
public class ChooseClassActivity extends BaseActivity<ChooseClassView, ChooseClassPresenter>
        implements ChooseClassView, EasyRecyclerViewSidebar.OnTouchSectionListener {

    public static final String CLASSLISTBEAN = "classlistbean";
    public static final int CHOOSE_CLASS_RESULT_CODE = 112;
    public static final int CLASS_SEARCH_REQUEST_CODE = 117;
    private EasyRecyclerViewSidebar mSidebar;
    private TextView mFloatingTv;
    private EasyFloatingImageView imageFloatingIv;
    private RecyclerView mRecyclerView;
    private RelativeLayout mFloatingRl;
    private TextView mSearchTv;
    private StuClassBean.ClassListBean mClassListBean;

    @NonNull
    @Override
    public ChooseClassPresenter createPresenter() {
        return new ChooseClassPresenter(mActivity);
    }

    public Toolbar mToolbar;
    public ToolBarManager mToolBarManager;

    public static void start(Context context) {
        final Intent lIntent = new Intent(context, ChooseClassActivity.class);
        if (!(context instanceof Activity)) {
            lIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(lIntent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_choose);
        initData();
        initView();
        loadData();
    }


    //用于页面间数据接收
    private void initData() {

    }


    //初始化view
    private void initView() {
        findViews();
        initToolbar();
        setOnInteractListener();


    }

    private void loadData() {
        presenter.getAllClass();
    }


    private void initToolbar() {
        mToolBarManager.init();
        mToolbar.setNavigationOnClickListener(v -> onBackPressed());
        mToolBarManager.setTitle(getString(R.string.choose_class));

    }

    //事件处理
    private void setOnInteractListener() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        this.mSidebar.setFloatView(mFloatingRl);
        this.mSidebar.setOnTouchSectionListener(this);

        mSearchTv.setOnClickListener(v -> {
            Intent lIntent = new Intent(mActivity, AdjustClassSearchActivity.class);
            startActivityForResult(lIntent, CLASS_SEARCH_REQUEST_CODE);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //从班级搜索界面得到数据并返回到课表搜索界面
        if (requestCode == CLASS_SEARCH_REQUEST_CODE && resultCode == AdjustClassSearchActivity.CLASS_SEARCH_RESULT_CODE) {
            StuClassBean.ClassListBean lClassListBean = (StuClassBean.ClassListBean) data.getExtras().getSerializable(CLASSLISTBEAN);
            Intent lIntent = getIntent();
            Bundle lBundle = new Bundle();
            lBundle.putSerializable(CLASSLISTBEAN, lClassListBean);
            lIntent.putExtras(lBundle);
            setResult(CHOOSE_CLASS_RESULT_CODE, lIntent);
            finish();
        }
    }

    //绑定控件
    private void findViews() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolBarManager = new ToolBarManager(mToolbar, this);

        mRecyclerView = getViewById(R.id.section_rv_01);
        mSidebar = (EasyRecyclerViewSidebar) findViewById(R.id.section_sidebar);
        mFloatingTv = (TextView) findViewById(R.id.section_floating_tv);
        imageFloatingIv = (EasyFloatingImageView) findViewById(R.id.section_floating_iv);
        mFloatingRl = (RelativeLayout) findViewById(R.id.section_floating_rl);
        mSearchTv = (TextView) findViewById(R.id.search_tv);

        TextView noDataTv = (TextView) findViewById(R.id.no_data_tv);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mToolBarManager.destroy();
    }

    @Override
    public void onTouchImageSection(int sectionIndex, EasyImageSection imageSection) {

    }

    @Override
    public void onTouchLetterSection(int sectionIndex, EasySection letterSection) {
        int lI = presenter.getSparseIntArray().get(sectionIndex);
        this.mFloatingTv.setVisibility(View.VISIBLE);
        this.imageFloatingIv.setVisibility(View.INVISIBLE);
        this.mFloatingTv.setText(letterSection.letter);
        scrollToPosition(lI);
    }

    @Override
    public void showResult(List<StuClassBean.ClassListBean> listBeen) {
        ChooseClassAdapter.Listener lListener = new ChooseClassAdapter.Listener() {
            @Override
            public void itemClick(StuClassBean.ClassListBean classListBean) {
                mClassListBean = classListBean;
//                mToolBarManager.getRightButton().setEnabled(mClassListBean != null);

                Intent lIntent = getIntent();
                Bundle lBundle = new Bundle();
                lBundle.putSerializable(CLASSLISTBEAN, mClassListBean);
                lIntent.putExtras(lBundle);
                setResult(CHOOSE_CLASS_RESULT_CODE, lIntent);
                finish();
            }

            @Override
            public boolean prePosition(int position) {
//                View lChildAt = mRecyclerView.getChildAt(position);
                ChooseClassAdapter.ClassHolder lClassHolder = (ChooseClassAdapter.ClassHolder) mRecyclerView.findViewHolderForAdapterPosition(position);
//                ChooseClassAdapter.ClassHolder lClassHolder = (ChooseClassAdapter.ClassHolder) mRecyclerView.getChildViewHolder(lChildAt);
                CheckBox lCheckBox = lClassHolder.getCheckBox();
                if (lCheckBox != null) {
                    lCheckBox.setChecked(false);
                }
                return true;
            }
        };

        ChooseClassAdapter lAdapter = new ChooseClassAdapter(mActivity, listBeen, lListener);
        mSidebar.setSections(presenter.getEasySections());
        mRecyclerView.setAdapter(lAdapter);
    }

    /**
     * 指定recycleview滚动的位置
     *
     * @param position 位置
     */
    private void scrollToPosition(int position) {
        ((LinearLayoutManager) (this.mRecyclerView.getLayoutManager())).scrollToPositionWithOffset(position, 0);
    }
}
