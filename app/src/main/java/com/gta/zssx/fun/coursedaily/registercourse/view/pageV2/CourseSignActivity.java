package com.gta.zssx.fun.coursedaily.registercourse.view.pageV2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.inputmethod.InputMethodManager;

import com.gta.zssx.AppConfiguration;
import com.gta.zssx.R;
import com.gta.zssx.fun.coursedaily.registercourse.model.ClassDataManager;
import com.gta.zssx.fun.coursedaily.registercourse.model.bean.DataBean;
import com.gta.zssx.fun.coursedaily.registercourse.model.bean.PostSignBean;
import com.gta.zssx.fun.coursedaily.registercourse.model.bean.SectionStudentListBean;
import com.gta.zssx.fun.coursedaily.registercourse.model.bean.StudentListBean;
import com.gta.zssx.fun.coursedaily.registercourse.model.bean.StudentListNewBean;
import com.gta.zssx.fun.coursedaily.registercourse.presenter.SignPresenter;
import com.gta.zssx.fun.coursedaily.registercourse.view.SignView;
import com.gta.zssx.fun.coursedaily.registercourse.view.adapter.SignAdapter;
import com.gta.zssx.fun.coursedaily.registercourse.view.base.RegisterCourseActivity;
import com.gta.zssx.fun.coursedaily.registerrecord.model.DTO.ClassInfoDto;
import com.gta.zssx.fun.coursedaily.registerrecord.view.page.AlreadyRegisteredRecordFromSignatureFragment;
import com.gta.zssx.fun.coursedaily.registerrecord.view.page.AlreadyRegisteredRecordActivity;
import com.gta.zssx.fun.personalcenter.model.bean.UserBean;
import com.gta.zssx.pub.base.BaseActivity;
import com.gta.zssx.pub.util.RxBus;
import com.gta.zssx.pub.manager.ToolBarManager;
import com.gta.zssx.pub.util.LogUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import rx.subscriptions.CompositeSubscription;

/**
 * [Description]
 * <p>
 * [How to use]
 * <p>
 * [Tips]
 * 签名确认页面-旧
 * @author Created by Zhimin.Huang on 2016/7/4.
 * @since 1.0.0
 */
@Deprecated
public class CourseSignActivity extends BaseActivity<SignView, SignPresenter> implements SignView {

    public static String sStudentlist = "studentlist";
    public RecyclerView mRecyclerView;
    public UserBean mUserBean;
    public ClassDataManager.DataCache mDataCache;
    public PostSignBean mPostSignBean;
    private boolean mIsModify;
    public DataBean mDataBean;
    private CompositeSubscription allSubscription = new CompositeSubscription();
    public List<StudentListBean> mStudentListBeen;
    public List<PostSignBean.SectionBean> mSectionBeanList;
//    public String mToJson;

    @NonNull
    @Override
    public SignPresenter createPresenter() {
        return new SignPresenter(this);
    }

    @Override
    public void showResult(String s) {
        finish();
        ClassInfoDto lClassInfoDto = new ClassInfoDto();
//        lClassInfoDto.setClassName(mDataBean.getCourseName());
        lClassInfoDto.setClassID(mDataBean.getClassId() + "");
        lClassInfoDto.setSignDate(mDataBean.getSignDate());
        lClassInfoDto.setTeacherID(mUserBean.getUserId());
        lClassInfoDto.setIsFromClassLogMainpage(false);
        CourseDailyActivity.start(this);
        AlreadyRegisteredRecordActivity.start(this, AlreadyRegisteredRecordFromSignatureFragment.PAGE_TAG, lClassInfoDto);
        ClassDataManager.destroyDataCache();

    }

    @Override
    public void showHaveBeenSignMassage(String s) {

    }


    @Override
    public void emptyUI() {

    }

    @Override
    public void getStudentListInfo(List<SectionStudentListBean> sectionStudentListBeanList) {

    }

    @Override
    public void isHaveSectionHaveBeenSign(int unSignNum) {

    }

    public Toolbar mToolbar;
    public ToolBarManager mToolBarManager;

    public static void start(Context context, boolean isModify, DataBean dataBean, List<StudentListBean> studentListBeen) {
        final Intent lIntent = new Intent(context, CourseSignActivity.class);
        Bundle lBundle = new Bundle();

        lBundle.putBoolean(RegisterCourseActivity.sIsModify, isModify);
//        lBundle.putSerializable(StudentListActivity.sDateBean, dataBean);
        lBundle.putSerializable(CourseSignWithAttendanceStatusActivity.sDateBean, dataBean);
        lBundle.putSerializable(sStudentlist, (Serializable) studentListBeen);

        lIntent.putExtras(lBundle);
        if (!(context instanceof Activity)) {
            lIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(lIntent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_sign);
        initData();
        initView();
        loadData();
    }

    private void loadData() {

    }

    //初始化view
    private void initView() {
        findViews();
        initToolbar();
        setOnInteractListener();

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        allSubscription.add(RxBus.getDefault().toObserverable(String.class).subscribe(s -> {
            mToolBarManager.getRightButton().setEnabled(!s.isEmpty());
        }));
    }

    private void initToolbar() {
        mToolBarManager.init();
        mToolbar.setNavigationOnClickListener(v -> onBackPressed());

        mToolBarManager.setTitle(getString(R.string.course_score))
                .showRightButton(true)
                .setRightText(getString(R.string.sign_confirm))
                .clickRightButton(v -> {
                    hideSoftKeyboard();//跳转收起软件盘
                    mPostSignBean.setClassID(mDataBean.getClassId()); //班级Id
                    mPostSignBean.setSignDate(mDataBean.getSignDate());  //登记日期
                    mPostSignBean.setTeacherID(mUserBean.getUserId());  //登记老师的ID
                    mPostSignBean.setSection(mSectionBeanList);     //节次信息,SectionID: 1,CourseName,Score,Remark

                    mPostSignBean.setDetail(mStudentListBeen);
                    if (mIsModify) {
                        mPostSignBean.setType(PostSignBean.MODIFY);
                        mPostSignBean.setOriginalSignDate(mDataBean.getOriginalSignDate());
                        mPostSignBean.setOriginalClassID(mDataBean.getOriginalClassId());
                        mPostSignBean.setOriginalSectionID(mDataBean.getOriginalSectionId());
                    } else {
                        LogUtil.Log("lenita","mDataBean.getSignDate() = "+mDataBean.getSignDate());
                        mPostSignBean.setType(PostSignBean.ADD);
                    }
//                    mToJson = new Gson().toJson(mPostSignBean);
//                    presenter.postSignData(mPostSignBean);
                    presenter.postSignData(null,null);
                });
    }

    //收起键盘
    protected void hideSoftKeyboard()
    {
        if (this.getCurrentFocus() != null)
        {
            InputMethodManager inputMethodManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(),0);
        }
    }


    //事件处理
    private void setOnInteractListener() {
        SignAdapter.Listener lListener = new SignAdapter.Listener() {
            @Override
            public void itemClick() {

            }

            @Override
            public void postSectionBean(List<PostSignBean.SectionBean> sectionBeanList) {
                mSectionBeanList = sectionBeanList;
            }
        };
//        Set<SectionBean> lSection = ClassDataManager.getDataCache().getSection();
        SignAdapter lAdapter = new SignAdapter(this, lListener,
                mDataBean.getSectionBeen(),
                mStudentListBeen, mDataBean);
        mRecyclerView.setAdapter(lAdapter);

    }

    //绑定控件
    private void findViews() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolBarManager = new ToolBarManager(mToolbar, this);
        mRecyclerView = (RecyclerView) findViewById(R.id.sign_rv);
    }

    //用于页面间数据接收
    private void initData() {
//        presenter.attachView(this);

        try {
            mUserBean = AppConfiguration.getInstance().getUserBean();
        } catch (Exception e) {
            e.printStackTrace();
        }
        mIsModify = getIntent().getExtras().getBoolean(RegisterCourseActivity.sIsModify);
//        mDataBean = (DataBean) getIntent().getSerializableExtra(StudentListActivity.sDateBean);
        mDataBean = (DataBean) getIntent().getSerializableExtra(CourseSignWithAttendanceStatusActivity.sDateBean);
        mStudentListBeen = (List<StudentListBean>) getIntent().getSerializableExtra(sStudentlist);
        mDataCache = ClassDataManager.getDataCache();
        mPostSignBean = new PostSignBean();

        mDataBean = presenter.getSignSection(mDataBean, mStudentListBeen);
//        mSectionBeanList = mDataBean.getPostSectionBean();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        presenter.detachView(false);
        mToolBarManager.destroy();
        allSubscription.unsubscribe();
    }
}
