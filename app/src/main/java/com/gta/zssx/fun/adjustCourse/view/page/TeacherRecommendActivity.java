package com.gta.zssx.fun.adjustCourse.view.page;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gta.zssx.R;
import com.gta.zssx.fun.adjustCourse.deprecated.view.CourseApplyFragment;
import com.gta.zssx.fun.adjustCourse.model.bean.GetTeacherBean;
import com.gta.zssx.fun.adjustCourse.model.bean.TeacherBean;
import com.gta.zssx.fun.adjustCourse.model.bean.TeacherRecommendBean;
import com.gta.zssx.fun.adjustCourse.presenter.TeacherRecommendPresenter;
import com.gta.zssx.fun.adjustCourse.view.TeacherRecommendView;
import com.gta.zssx.pub.base.BaseActivity;
import com.gta.zssx.pub.manager.ToolBarManager;
import com.gta.zssx.pub.widget.flowlayout.FlowLayout;
import com.gta.zssx.pub.widget.flowlayout.TagAdapter;
import com.gta.zssx.pub.widget.flowlayout.TagFlowLayout;


import java.util.List;

/**
 * [Description]
 * <p>
 * [How to use]
 * <p>
 * [Tips]
 *
 * @author Created by Zhimin.Huang on 2016/10/24.
 * @since 1.0.0
 */
public class TeacherRecommendActivity extends BaseActivity<TeacherRecommendView, TeacherRecommendPresenter> implements TeacherRecommendView {

    public static final String TEACHER_BEAN = "TeacherBean";
    public static final int ADJUST_TEACHER_RESULT_CODE = 105;
    public static final int CHOOSE_TEACHER_REQUEST_CODE = 113;
    private TagFlowLayout mClassTagFlowLayout;
    private TagFlowLayout mGroupsTagFlowLayout;
    private TagFlowLayout mDpTagFlowLayout;
    private RelativeLayout mCheckLayout;
    private TextView mTeacherNameTv;
    private GetTeacherBean mGetTeacherBean;
    private String mName;
    private TeacherRecommendBean.SameClassTeacherBean mSameClassTeacherBean;
    private TagAdapter<TeacherRecommendBean.SameClassTeacherBean> mClassTagAdapter;
    private TagAdapter<TeacherRecommendBean.SameClassTeacherBean> mSubjectTagAdapter;
    private TagAdapter<TeacherRecommendBean.SameClassTeacherBean> mDeptTagAdapter;
    private TeacherBean.TeacherListBean mTeacherListBean;
    private LinearLayout mClassLayout;
    private LinearLayout mGroupsLayout;
    private LinearLayout mDpLayout;

    @NonNull
    @Override
    public TeacherRecommendPresenter createPresenter() {
        return new TeacherRecommendPresenter(mActivity);
    }

    public Toolbar mToolbar;
    public ToolBarManager mToolBarManager;

    public static void start(Context context) {
        final Intent lIntent = new Intent(context, TeacherRecommendActivity.class);
        if (!(context instanceof Activity)) {
            lIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(lIntent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_recommend);
        initData();
        initView();
        loadData();
    }


    //用于页面间数据接收
    private void initData() {
        mGetTeacherBean = (GetTeacherBean) getIntent().getExtras().getSerializable(CourseApplyFragment.GET_TEACHER_BEAN);
    }


    //初始化view
    private void initView() {
        findViews();
        initToolbar();
        setOnInteractListener();

    }

    private void loadData() {
        presenter.getTeacherRecommend(mGetTeacherBean);
    }


    private void initToolbar() {
        mToolBarManager.init();
        mToolbar.setNavigationOnClickListener(v -> onBackPressed());

        mToolBarManager.setTitle(getString(R.string.choose_teacher))
                .setRightText(getString(R.string.confirm))
                .clickRightButton(v -> {
                    Intent lIntent = getIntent();
                    Bundle lBundle = new Bundle();
                    lBundle.putSerializable(TEACHER_BEAN, mSameClassTeacherBean);
                    lIntent.putExtras(lBundle);
                    setResult(ADJUST_TEACHER_RESULT_CODE, lIntent);
                    finish();
                });

        setEnable();

    }

    private void setEnable() {
        mToolBarManager.getRightButton().setEnabled(mSameClassTeacherBean != null);
    }

    //事件处理
    private void setOnInteractListener() {
        mCheckLayout.setOnClickListener(v -> {
//            ChooseTeacherActivity.start(mActivity, ChooseTeacherActivity.SOME_TEACHER, mGetTeacherBean);
            final Intent lIntent = new Intent(mActivity, ChooseTeacherActivity.class);
            Bundle lBundle = new Bundle();
            lBundle.putSerializable(CourseApplyFragment.GET_TEACHER_BEAN, mGetTeacherBean);
            lIntent.putExtras(lBundle);
            lIntent.putExtra(ChooseTeacherActivity.TYPE, ChooseTeacherActivity.SOME_TEACHER);
            startActivityForResult(lIntent, CHOOSE_TEACHER_REQUEST_CODE);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //从教师列表拿到数据并且返回到申请页面
        if (requestCode == CHOOSE_TEACHER_REQUEST_CODE && resultCode == ChooseTeacherActivity.CHOOSE_TEACHER_4_APPLY_RESULT_CODE) {
            mTeacherListBean = (TeacherBean.TeacherListBean) data.getExtras().getSerializable(ChooseTeacherActivity.TEACHER_BEAN);
            TeacherRecommendBean.SameClassTeacherBean lSameClassTeacherBean = new TeacherRecommendBean.SameClassTeacherBean();
            lSameClassTeacherBean.setId(mTeacherListBean.getTeacherId());
            lSameClassTeacherBean.setName(mTeacherListBean.getTeacherName());
            lSameClassTeacherBean.setTeacherBId(mTeacherListBean.getTeacherBId());
            Intent lIntent = getIntent();
            Bundle lBundle = new Bundle();
            lBundle.putSerializable(TEACHER_BEAN, lSameClassTeacherBean);
            lIntent.putExtras(lBundle);
            setResult(ADJUST_TEACHER_RESULT_CODE, lIntent);
            finish();
        }
    }

    //绑定控件
    private void findViews() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolBarManager = new ToolBarManager(mToolbar, this);

        mClassTagFlowLayout = (TagFlowLayout) findViewById(R.id.same_class_flowLayout);
        mClassLayout = (LinearLayout) findViewById(R.id.same_class_layout);
        mGroupsTagFlowLayout = (TagFlowLayout) findViewById(R.id.same_group_flowLayout);
        mGroupsLayout = (LinearLayout) findViewById(R.id.same_group_layout);
        mDpTagFlowLayout = (TagFlowLayout) findViewById(R.id.same_department_flowLayout);
        mDpLayout = (LinearLayout) findViewById(R.id.same_department_layout);
        mCheckLayout = (RelativeLayout) findViewById(R.id.check_all_teacher);
        mTeacherNameTv = (TextView) findViewById(R.id.teacher_name_tv);

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mToolBarManager.destroy();
    }

    @Override
    public void showResult(TeacherRecommendBean teacherRecommendBean) {
        List<TeacherRecommendBean.SameClassTeacherBean> lSameClassTeacher = teacherRecommendBean.getSameClassTeacher();
        List<TeacherRecommendBean.SameClassTeacherBean> lSameSubjectTeacher = teacherRecommendBean.getSameSubjectTeacher();
        List<TeacherRecommendBean.SameClassTeacherBean> lSameDeptTeacher = teacherRecommendBean.getSameDeptTeacher();
        setSameClass(lSameClassTeacher);
        setSameSubject(lSameSubjectTeacher);
        setSameDept(lSameDeptTeacher);
    }

    private void setSameDept(List<TeacherRecommendBean.SameClassTeacherBean> sameDeptTeacher) {
        if (sameDeptTeacher == null || sameDeptTeacher.size() == 0) {
            mDpLayout.setVisibility(View.GONE);
            return;
        } else {
            mDpLayout.setVisibility(View.VISIBLE);
        }
        mDeptTagAdapter = new TagAdapter<TeacherRecommendBean.SameClassTeacherBean>(sameDeptTeacher) {
            @Override
            public View getView(FlowLayout parent, int position, TeacherRecommendBean.SameClassTeacherBean sameClassTeacherBean) {
                TextView lTextView = (TextView) LayoutInflater.from(mActivity).inflate(R.layout.text_view, mDpTagFlowLayout, false);
                lTextView.setText(sameClassTeacherBean.getName());
                return lTextView;
            }
        };

        mDpTagFlowLayout.setOnTagClickListener((view, position, parent) -> {
            for (int i = 0; i < parent.getChildCount(); i++) {
                if (i != position) {
                    parent.getChildAt(i).setClickable(false);
                } else {
                    parent.getChildAt(i).setClickable(true);
                }
            }
            mName = sameDeptTeacher.get(position).getName();
            mTeacherNameTv.setText(mName);
            mSameClassTeacherBean = sameDeptTeacher.get(position);
            setEnable();
            if (mClassTagAdapter != null) {
                mClassTagFlowLayout.setAdapter(mClassTagAdapter);
            }
            if (mSubjectTagAdapter != null) {
                mGroupsTagFlowLayout.setAdapter(mSubjectTagAdapter);
            }
            return true;
        });
        mDpTagFlowLayout.setAdapter(mDeptTagAdapter);
    }

    private void setSameSubject(List<TeacherRecommendBean.SameClassTeacherBean> sameSubjectTeacher) {

        if (sameSubjectTeacher == null || sameSubjectTeacher.size() == 0) {
            mGroupsLayout.setVisibility(View.GONE);
            return;
        } else {
            mGroupsLayout.setVisibility(View.VISIBLE);
        }

        mSubjectTagAdapter = new TagAdapter<TeacherRecommendBean.SameClassTeacherBean>(sameSubjectTeacher) {
            @Override
            public View getView(FlowLayout parent, int position, TeacherRecommendBean.SameClassTeacherBean sameClassTeacherBean) {
                TextView lTextView = (TextView) LayoutInflater.from(mActivity).inflate(R.layout.text_view, mGroupsTagFlowLayout, false);
                lTextView.setText(sameClassTeacherBean.getName());
                return lTextView;
            }
        };

        mGroupsTagFlowLayout.setOnTagClickListener((view, position, parent) -> {
            for (int i = 0; i < parent.getChildCount(); i++) {
                if (i != position) {
                    parent.getChildAt(i).setClickable(false);
                } else {
                    parent.getChildAt(i).setClickable(true);
                }
            }
            mName = sameSubjectTeacher.get(position).getName();
            mTeacherNameTv.setText(mName);
            mSameClassTeacherBean = sameSubjectTeacher.get(position);
            setEnable();
            if (mClassTagAdapter != null) {
                mClassTagFlowLayout.setAdapter(mClassTagAdapter);
            }
            if (mDeptTagAdapter != null) {
                mDpTagFlowLayout.setAdapter(mDeptTagAdapter);
            }
            return true;
        });
        mGroupsTagFlowLayout.setAdapter(mSubjectTagAdapter);
    }

    private void setSameClass(final List<TeacherRecommendBean.SameClassTeacherBean> sameClassTeacher) {

        if (sameClassTeacher == null || sameClassTeacher.size() == 0) {
            mClassLayout.setVisibility(View.GONE);
            return;
        } else {
            mClassLayout.setVisibility(View.VISIBLE);
        }
        mClassTagAdapter = new TagAdapter<TeacherRecommendBean.SameClassTeacherBean>(sameClassTeacher) {
            @Override
            public View getView(FlowLayout parent, int position, TeacherRecommendBean.SameClassTeacherBean sameClassTeacherBean) {
                TextView lTextView = (TextView) LayoutInflater.from(mActivity).inflate(R.layout.text_view, mClassTagFlowLayout, false);
                lTextView.setText(sameClassTeacherBean.getName());
                return lTextView;
            }
        };

        mClassTagFlowLayout.setOnTagClickListener((view, position, parent) -> {
            for (int i = 0; i < parent.getChildCount(); i++) {
                if (i != position) {
                    parent.getChildAt(i).setClickable(false);
                } else {
                    parent.getChildAt(i).setClickable(true);
                }
            }

            mName = sameClassTeacher.get(position).getName();
            mSameClassTeacherBean = sameClassTeacher.get(position);
            mTeacherNameTv.setText(mName);
            setEnable();
            if (mSubjectTagAdapter != null) {
                mGroupsTagFlowLayout.setAdapter(mSubjectTagAdapter);
            }
            if (mDeptTagAdapter != null) {
                mDpTagFlowLayout.setAdapter(mDeptTagAdapter);
            }

            return true;
        });
        mClassTagFlowLayout.setAdapter(mClassTagAdapter);
    }
}
