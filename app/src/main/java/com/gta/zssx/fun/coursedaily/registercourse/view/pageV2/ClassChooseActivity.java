package com.gta.zssx.fun.coursedaily.registercourse.view.pageV2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.blankj.utilcode.util.ToastUtils;
import com.gta.utils.resource.Toast;
import com.gta.zssx.AppConfiguration;
import com.gta.zssx.R;
import com.gta.zssx.fun.coursedaily.registercourse.model.bean.ClassChooseBean01;
import com.gta.zssx.fun.coursedaily.registercourse.model.bean.SortClassBean;
import com.gta.zssx.fun.coursedaily.registercourse.presenter.ClassChoosePresenter;
import com.gta.zssx.fun.coursedaily.registercourse.view.ClassChooseView;
import com.gta.zssx.fun.coursedaily.registercourse.view.adapter.ClassChooseAdapter;
import com.gta.zssx.fun.coursedaily.registercourse.view.adapter.ui.SureClickDialog;
import com.gta.zssx.fun.personalcenter.model.bean.UserBean;
import com.gta.zssx.pub.base.BaseActivity;
import com.gta.zssx.pub.manager.ToolBarManager;
import com.gta.zssx.pub.widget.easysidebar.EasyFloatingImageView;
import com.gta.zssx.pub.widget.easysidebar.EasyImageSection;
import com.gta.zssx.pub.widget.easysidebar.EasyRecyclerViewSidebar;
import com.gta.zssx.pub.widget.easysidebar.EasySection;
import com.gta.zssx.pub.widget.onDoubleClickListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * [Description]
 * <p>
 * [How to use]
 * <p>
 * [Tips]
 *
 * @author Created by Zhimin.Huang on 2016/7/4.
 * @since 1.0.0
 */
public class ClassChooseActivity extends BaseActivity<ClassChooseView, ClassChoosePresenter>
        implements ClassChooseView, EasyRecyclerViewSidebar.OnTouchSectionListener {

    private EasyRecyclerViewSidebar mSidebar;
    private TextView mFloatingTv;
    private EasyFloatingImageView imageFloatingIv;
    private RecyclerView mRecyclerView;
    public RelativeLayout mFloatingRl;
    public Set<SortClassBean.ClassByYear> mClassByYearRecords;
    public Set<ClassChooseBean01.ClassListBean> mClassListBeanSet;
    public List<ClassChooseBean01.ClassListBean> mClassListBeanList;
    public TextView mSearchTv;
    private TextView noDataTv;
    public static final String FILTER_HAVE_SHOW_IN_MAINPAGE_CLASS_LIST = "FILTER_HAVE_SHOW_IN_MAINPAGE_CLASS_LIST";
    private Map<Integer,String> mClassMap;
    private static boolean isNeedTofilterClass = false; //默认都是不筛选，当筛选时置为true

    @NonNull
    @Override
    public ClassChoosePresenter createPresenter() {
        return new ClassChoosePresenter();
    }

    @Override
    public void addSuccess() {
        UserBean lUserBean = null;
        try {
            lUserBean = AppConfiguration.getInstance().getUserBean();
        } catch (Exception e) {
            e.printStackTrace();
        }
        AppConfiguration.getInstance().setFirstIn(lUserBean);
        CourseDailyActivity.isRefresh = true;
        CourseDailyActivity.start(this);
        finish();
    }

    @Override
    public void showClass01(List<ClassChooseBean01.ClassListBean> classListBeen) {
        //TODO 如果所有的班级都被添加过了，弹出对话框让用户确定
        if(presenter.getIsAllClassHaveSelect()){
            sureBackClickDialog();
            return;
        }
        displayList01(classListBeen);
    }

    public SureClickDialog sureBackClickDialog;
    private void sureBackClickDialog(){
        if (sureBackClickDialog != null)
            sureBackClickDialog = null;
//        backgroundAlpha(0.8f);
        String content = "您已添加了所有的班级，当前已经没有可以选择的班级，请点击“确定”返回主页。";
        sureBackClickDialog = new SureClickDialog(this, content,false, new SureClickDialog.Listener() {
            @Override
            public void onDialogDismissListener() {
//                backgroundAlpha(1f);
            }

            @Override
            public void onSureListerner() {
                finish();
            }
        });
        sureBackClickDialog.setCanceledOnTouchOutside(false);
        sureBackClickDialog.setCancelable(false);
        sureBackClickDialog.show();
    }

   /* private void backgroundAlpha(float b) {
        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.alpha = b;
        getWindow().setAttributes(layoutParams);
    }
*/

    @Override
    public void showEmpty() {
        noDataTv.setVisibility(View.VISIBLE);

    }

    private void displayList01(List<ClassChooseBean01.ClassListBean> classListBeen) {
        ClassChooseAdapter.Listener lListener = (isCheck, classListBeanSet, position, recordList) -> {
            mClassListBeanSet = classListBeanSet;
            mClassListBeanList = recordList;
            mToolBarManager.getRightButton().setEnabled(mClassListBeanList.size() > 0);
        };
        ClassChooseAdapter lChooseAdapter = new ClassChooseAdapter(this, classListBeen, lListener, presenter.getSparseIntArray());
        mSidebar.setSections(presenter.getEasySections());
        mRecyclerView.setAdapter(lChooseAdapter);
    }

    public Toolbar mToolbar;
    public ToolBarManager mToolBarManager;

    public static void start(Context context) {
        final Intent lIntent = new Intent(context, ClassChooseActivity.class);
        isNeedTofilterClass = false;
        if (!(context instanceof Activity)) {
            lIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(lIntent);
    }


    /**
     * 用于排除有已经在主页显示的班级
     * @param context
     * @param classMap
     */
    public static void start(Context context,Map<Integer,String> classMap) {
        final Intent lIntent = new Intent(context, ClassChooseActivity.class);
        isNeedTofilterClass = true;
        Bundle bundle = new Bundle();
        bundle.putSerializable(FILTER_HAVE_SHOW_IN_MAINPAGE_CLASS_LIST, (Serializable) classMap);
        lIntent.putExtras(bundle);
        if (!(context instanceof Activity)) {
            lIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(lIntent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_class_choose);
        initData();
        initView();
        loadData();
    }


    private void loadData() {
        //网络请求
        presenter.loadClass01(mClassMap);
    }

    //初始化view
    private void initView() {
        findViews();
        initToolbar();
        setOnInteractListener();

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        this.mSidebar.setFloatView(mFloatingRl);
        this.mSidebar.setOnTouchSectionListener(this);
    }

    private void initToolbar() {
        mToolBarManager.init();
        mToolbar.setNavigationOnClickListener(v -> onBackPressed());
        mToolBarManager.getRightButton().setEnabled(false);
        mToolBarManager
//                .showBack(!AppConfiguration.getInstance().isFistIn())
//                .showLeftImage(AppConfiguration.getInstance().isFistIn())
//                .clickLeftImage(v -> PersonCenterActivity.start4Result(ClassChooseActivity.this))
                .setTitle(getString(R.string.add_class))
                .showRightButton(true)
                .setRightText(getString(R.string.finish))
                .clickRightButton(v -> {
//                    List<ClassChooseBean01.ClassListBean> lClassByYears = presenter.sortSet(mClassListBeanSet);
                    presenter.addClass(presenter.getAddClassBean(mClassListBeanList));
                });
    }

    //事件处理
    private void setOnInteractListener() {
        mSearchTv.setOnClickListener(v -> {
            Intent lIntent = new Intent(ClassChooseActivity.this, ClassSearchActivity.class);
            startActivity(lIntent);

        });

        mToolbar.setOnTouchListener(new onDoubleClickListener(() -> mRecyclerView.smoothScrollToPosition(0)));
    }

    //绑定控件
    private void findViews() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolBarManager = new ToolBarManager(mToolbar, this);

//        mRecyclerView = (RecyclerView) findViewById(R.id.section_rv_01);
        mRecyclerView = getViewById(R.id.section_rv_01);
        mSidebar = (EasyRecyclerViewSidebar) findViewById(R.id.section_sidebar);
        mFloatingTv = (TextView) findViewById(R.id.section_floating_tv);
        imageFloatingIv = (EasyFloatingImageView) findViewById(R.id.section_floating_iv);
        mFloatingRl = (RelativeLayout) findViewById(R.id.section_floating_rl);
        mSearchTv = (TextView) findViewById(R.id.search_tv);

        noDataTv = (TextView) findViewById(R.id.no_data_tv);
    }

    //用于页面间数据接收
    private void initData() {
//        presenter.attachView(this);

        mClassMap = new HashMap<>();
        //TODO 是否要进行筛选班级
        if(isNeedTofilterClass){
            mClassMap = (Map<Integer, String>) getIntent().getExtras().getSerializable(FILTER_HAVE_SHOW_IN_MAINPAGE_CLASS_LIST);
        }
        mClassByYearRecords = new HashSet<>();
        mClassListBeanSet = new HashSet<>();
        mClassListBeanList = new ArrayList<>();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        presenter.detachView(false);
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

    /**
     * 指定recycleview滚动的位置
     *
     * @param position 位置
     */
    private void scrollToPosition(int position) {
        ((LinearLayoutManager) (this.mRecyclerView.getLayoutManager())).scrollToPositionWithOffset(position, 0);
    }
}
