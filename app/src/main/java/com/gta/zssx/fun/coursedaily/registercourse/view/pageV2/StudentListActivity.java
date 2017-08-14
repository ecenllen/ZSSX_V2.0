package com.gta.zssx.fun.coursedaily.registercourse.view.pageV2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RelativeLayout;

import com.gta.zssx.R;
import com.gta.zssx.fun.coursedaily.registercourse.model.bean.DataBean;
import com.gta.zssx.fun.coursedaily.registercourse.model.bean.RxStudentList;
import com.gta.zssx.fun.coursedaily.registercourse.model.bean.SectionBean;
import com.gta.zssx.fun.coursedaily.registercourse.model.bean.StudentListBean;
import com.gta.zssx.fun.coursedaily.registercourse.model.bean.StudentListNewBean;
import com.gta.zssx.fun.coursedaily.registercourse.presenter.StudentListPresenterV2;
import com.gta.zssx.fun.coursedaily.registercourse.view.StudentListViewV2;
import com.gta.zssx.fun.coursedaily.registercourse.view.adapter.CommonPageAdapter;
import com.gta.zssx.fun.coursedaily.registercourse.view.base.RegisterCourseActivity;
import com.gta.zssx.fun.coursedaily.registercourse.view.page.StudentListInnerFragment;
import com.gta.zssx.pub.base.BaseActivity;
import com.gta.zssx.pub.util.RxBus;
import com.gta.zssx.pub.manager.ToolBarManager;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * [Description]
 * <p>
 * [How to use]
 * <p>
 * [Tips]
 *
 * @author Created by Zhimin.Huang on 2016/7/4.
 * 学生考勤列表显示
 * @since 1.0.0
 */
@Deprecated
public class StudentListActivity extends BaseActivity<StudentListViewV2, StudentListPresenterV2> implements StudentListViewV2 {

    public static String sDateBean = "mDateBean";
    public DataBean mDataBean;
    public List<StudentListNewBean> rxStudentListStudentListBeen;

    public Toolbar mToolbar;
    public ToolBarManager mToolBarManager;
    public TabLayout mTabLayout;
    private RelativeLayout noDataTv;
    public ViewPager mViewPager;
    private ArrayList<String> mTitleList;
    public Set<SectionBean> mSection;
    public String mTitle;
    public static String sTITLE = "TITLE";
    private boolean mIsModify;

    @NonNull
    @Override
    public StudentListPresenterV2 createPresenter() {
        return new StudentListPresenterV2();
    }

    @Override
    public void showResult(List<StudentListNewBean> studentListBeen) {
        mTabLayout.setVisibility(View.VISIBLE);
        mToolBarManager.getRightButton().setEnabled(true);
        showList(studentListBeen);
    }

    @Override
    public void emptyUI() {
        mTabLayout.setVisibility(View.GONE);
        noDataTv.setVisibility(View.VISIBLE);
        mToolBarManager.getRightButton().setEnabled(false);
    }

    private void showList(List<StudentListNewBean> studentListBeen) {
        mTitleList = new ArrayList<>();
        ArrayList<Fragment> lViewList = new ArrayList<>();
        List<StudentListNewBean> lStudentListBeen = presenter.setStudentDefaultState(studentListBeen, mSection);
        rxStudentListStudentListBeen = lStudentListBeen;
        if (mSection != null) {
            List<SectionBean> lSectionBeen = new ArrayList<>(presenter.sortSet(mSection));
            for (int i = 0; i < lSectionBeen.size(); i++) {
                mTitleList.add(lSectionBeen.get(i).getLesson());
                StudentListInnerFragment lInnerFragment = new StudentListInnerFragment.Builder(this,i, lStudentListBeen, lSectionBeen.get(i)).create();
                lViewList.add(lInnerFragment);
            }
        }

        assert mSection != null;
        if (mSection.size() > 4) {
            mTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        } else {
            mTabLayout.setTabMode(TabLayout.MODE_FIXED);
        }
        for (int i = 0; i < mTitleList.size(); i++) {
            mTabLayout.addTab(mTabLayout.newTab().setText(mTitleList.get(i)));
        }

        CommonPageAdapter adapter = new CommonPageAdapter(getSupportFragmentManager(), lViewList,mTitleList);
        mViewPager.setAdapter(adapter);
        mTabLayout.setupWithViewPager(mViewPager);


    }


    /**
     * @param context  上下文
     * @param title    课程名称-标题
     * @param isModify 是否是修改
     * @param dataBean 登记用的数据
     */
    public static void start(Context context, String title, boolean isModify, DataBean dataBean) {
        final Intent lIntent = new Intent(context, StudentListActivity.class);
        Bundle lBundle = new Bundle();
        lBundle.putBoolean(RegisterCourseActivity.sIsModify, isModify);
        lBundle.putString(sTITLE, title);
        lBundle.putSerializable(sDateBean, dataBean);
        lIntent.putExtras(lBundle);

        if (!(context instanceof Activity)) {
            lIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(lIntent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_student_list_01);
        initData();
        initView();
        loadData();

//        Toast.makeText(mActivity,"",Toast.LENGTH_SHORT).show();

    }



    private void loadData() {

        presenter.getStudentList(mDataBean.getClassId(), mDataBean.getSignDate());
    }

    //初始化view
    private void initView() {
        findViews();
        initToolbar();
        setOnInteractListener();

    }

    private void initToolbar() {
        mToolBarManager.init();
        mToolbar.setNavigationOnClickListener(v -> onBackPressed());

        mToolBarManager.getRightButton().setEnabled(false);
        mToolBarManager.setTitle(mTitle)
                .showRightButton(true)
                .setRightText(this.getString(R.string.next_step))
                .clickRightButton(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //旧的登记所需数据
                        CourseSignActivity.start(StudentListActivity.this, mIsModify, mDataBean, setBean());
                    }
                });
    }

    private List<StudentListBean> setBean(){
        List<StudentListBean> listBeen = new ArrayList<StudentListBean>();
        //转成旧的登记所数据
        for(int i = 0;i <rxStudentListStudentListBeen.size();i++){
            StudentListNewBean studentListNewBean = rxStudentListStudentListBeen.get(i);
            StudentListBean studentListBean = new StudentListBean();
            studentListBean.setStudentID(studentListNewBean.getStudentID());
            studentListBean.setStudentNO(studentListNewBean.getStudentNO());
            studentListBean.setStundentName(studentListNewBean.getStundentName());
//            studentListBean.setLESSON1(studentListNewBean.getLESSON1());
//            studentListBean.setLESSON2(studentListNewBean.getLESSON2());
//            studentListBean.setLESSON3(studentListNewBean.getLESSON3());
//            studentListBean.setLESSON4(studentListNewBean.getLESSON4());
//            studentListBean.setLESSON5(studentListNewBean.getLESSON5());
//            studentListBean.setLESSON6(studentListNewBean.getLESSON6());
//            studentListBean.setLESSON7(studentListNewBean.getLESSON7());
            listBeen.add(studentListBean);
        }
        return listBeen;
    }

    //事件处理
    private void setOnInteractListener() {
        //StudentListInnerAdapter 306 行
        //Adapter数据更新后会回调到此函数，用来提交登记
        presenter.mCompositeSubscription.add(RxBus.getDefault().toObserverable(RxStudentList.class)
                .subscribe(rxStudentList -> {
                    rxStudentListStudentListBeen = rxStudentList.getStudentListBeen();
                },throwable -> {

                }));
    }

    //绑定控件
    private void findViews() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolBarManager = new ToolBarManager(mToolbar, this);
        mTabLayout = (TabLayout) findViewById(R.id.student_list_tablayout);
        mViewPager = (ViewPager) findViewById(R.id.student_list_viewpager);
        noDataTv = (RelativeLayout) findViewById(R.id.no_data_tv);
    }

    //用于页面间数据接收
    private void initData() {
//        presenter.attachView(this);
        mTitle = getIntent().getExtras().getString(sTITLE);
        mDataBean = (DataBean) getIntent().getSerializableExtra(sDateBean);
        mIsModify = getIntent().getExtras().getBoolean(RegisterCourseActivity.sIsModify);
//        mSection = ClassDataManager.getDataCache().getSection();
        mSection = new HashSet<>(mDataBean.getSectionBeen());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        presenter.detachView(false);
        mToolBarManager.destroy();
    }
}
