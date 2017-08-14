package com.gta.zssx.fun.adjustCourse.view.page;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ExpandableListView;

import com.gta.utils.helper.Helper_Drawable;
import com.gta.zssx.R;
import com.gta.zssx.fun.adjustCourse.deprecated.view.CourseApplyFragment;
import com.gta.zssx.fun.adjustCourse.model.bean.GetTeacherBean;
import com.gta.zssx.fun.adjustCourse.model.bean.TeacherBean;
import com.gta.zssx.fun.adjustCourse.presenter.ChooseTeacherPresenter;
import com.gta.zssx.fun.adjustCourse.view.ChooseTeacherView;
import com.gta.zssx.fun.adjustCourse.view.adapter.ChooseTeacherAdapter;
import com.gta.zssx.pub.base.BaseActivity;
import com.gta.zssx.pub.manager.ToolBarManager;

import java.util.List;

/**
 * [Description]
 * <p>
 * [How to use]
 * <p>
 * [Tips]
 *
 * @author Created by Zhimin.Huang on 2016/8/16 14:55.
 * @since 1.0.0
 */
public class ChooseTeacherActivity extends BaseActivity<ChooseTeacherView, ChooseTeacherPresenter>
        implements ChooseTeacherView, ExpandableListView.OnChildClickListener {
    public static final String TEACHER_BEAN = "teacherBean";
    public static final String TYPE = "type";
    public static final String ALL_TEACHER = "all";
    public static final String SOME_TEACHER = "some";
    public static final int CHOOSE_TEACHER_4_APPLY_RESULT_CODE = 114;
    public static final int SEARCH_ALL_TEACHER_REQUEST_CODE = 115;
    public static final int SEARCH_SOME_TEACHER_REQUEST_CODE = 118;
    public static final int TEACHER_SCHEDULE_RESULT_CODE = 110;
    private ExpandableListView mListView;
    private List<TeacherBean> entities;
    public String mType;
    private GetTeacherBean mGetTeacherBean;
    public int mDeptId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_teacher_expandable);

        mDeptId = getIntent().getIntExtra(ScheduleSearchFragment.DEPT_ID, 1);

        inis();
        inisToolBar();
        loadData();

    }

    private void loadData() {
        if (mType.equals(ALL_TEACHER)) {
            presenter.getAllTeacher(mDeptId);
        } else {
            presenter.getSomeTeacher(mGetTeacherBean);
        }
    }

    private void inisToolBar() {
        Toolbar lToolbar = (Toolbar) findViewById(R.id.toolbar);
        ToolBarManager lToolBarManager = new ToolBarManager(lToolbar, this);
        lToolBarManager.init();
        assert lToolbar != null;
        lToolbar.setNavigationOnClickListener(v -> onBackPressed());
        lToolBarManager.showBack(true)
                .setTitle(getString(R.string.choose_teacher))
                .showRightImage(true)
                .setRightImage(Helper_Drawable.tintDrawable(ContextCompat.getDrawable(mActivity, R.drawable.search_search),
                        ColorStateList.valueOf(Color.WHITE)))
                .clickRightImage(v -> {
                    //点击搜索
                    if (mType.equals(ALL_TEACHER)) {
                        Intent lIntent = new Intent(mActivity, ChooseTeacherSearchActivity.class);
                        lIntent.putExtra(TYPE, mType);
                        startActivityForResult(lIntent, SEARCH_ALL_TEACHER_REQUEST_CODE);
                    } else {
                        Intent lIntent = new Intent(mActivity, ChooseTeacherSearchActivity.class);
                        lIntent.putExtra(TYPE, mType);
                        Bundle lBundle = new Bundle();
                        lBundle.putSerializable(CourseApplyFragment.GET_TEACHER_BEAN, mGetTeacherBean);
                        lIntent.putExtras(lBundle);
                        startActivityForResult(lIntent, SEARCH_SOME_TEACHER_REQUEST_CODE);
                    }
//                    ChooseTeacherSearchActivity.start(ChooseTeacherActivity.this);
                });
    }

    private void inis() {
        mListView = (ExpandableListView) findViewById(R.id.expendlist);
        mListView.setDivider(null);
        mListView.setOnChildClickListener(this);
        mType = getIntent().getStringExtra(TYPE);
        mGetTeacherBean = (GetTeacherBean) getIntent().getExtras().getSerializable(CourseApplyFragment.GET_TEACHER_BEAN);
    }

    @NonNull
    @Override
    public ChooseTeacherPresenter createPresenter() {
        return new ChooseTeacherPresenter(mActivity);
    }


    public static void start(Context context, String type, GetTeacherBean getTeacherBean) {
        final Intent lIntent = new Intent(context, ChooseTeacherActivity.class);
        Bundle lBundle = new Bundle();
        lBundle.putSerializable(CourseApplyFragment.GET_TEACHER_BEAN, getTeacherBean);
        lIntent.putExtras(lBundle);
        lIntent.putExtra(TYPE, type);
        if (!(context instanceof Activity)) {
            lIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(lIntent);
    }


    @Override
    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

        TeacherBean.TeacherListBean lTeacherListBean = entities.get(groupPosition).getTeacherList().get(childPosition);
        //点击的是老师
        if (lTeacherListBean.getType() == 0) {
            Intent lIntent = getIntent();
            Bundle lBundle = new Bundle();
            lBundle.putSerializable(TEACHER_BEAN, lTeacherListBean);
            lIntent.putExtras(lBundle);
            if (mType.equals(ALL_TEACHER)) {
                //第一层不是部门的情况，回到教室课表搜索界面
                if (mDeptId == 1) {
                    setResult(TEACHER_SCHEDULE_RESULT_CODE, lIntent);
                } else {
                    //其他层级，回到自己的上一级
                    setResult(ChooseTeacherSearchActivity.SEARCH_ALL_TEACHER_RESULT_CODE, lIntent);
                }
            } else {
                setResult(mDeptId == 1 ? CHOOSE_TEACHER_4_APPLY_RESULT_CODE : ChooseTeacherSearchActivity.SEARCH_SOME_TEACHER_RESULT_CODE, lIntent);
            }
//            setResult(mType.equals(ALL_TEACHER) ? TEACHER_SCHEDULE_RESULT_CODE : CHOOSE_TEACHER_4_APPLY_RESULT_CODE, lIntent);
            finish();
        } else {
            Intent lIntent = getIntent();
            lIntent.setClass(this, ChooseTeacherActivity.class);
            int lTeacherId = Integer.valueOf(lTeacherListBean.getTeacherId());
            lIntent.putExtra(ScheduleSearchFragment.DEPT_ID, lTeacherId);
            lIntent.putExtra(TYPE, mType);
            if (mType.equals(SOME_TEACHER)) {
                Bundle lBundle = new Bundle();
                mGetTeacherBean.setDeptId(lTeacherId);
                lBundle.putSerializable(CourseApplyFragment.GET_TEACHER_BEAN, mGetTeacherBean);
                lIntent.putExtras(lBundle);
            }
            startActivityForResult(lIntent, mType.equals(ALL_TEACHER) ? SEARCH_ALL_TEACHER_REQUEST_CODE : SEARCH_SOME_TEACHER_REQUEST_CODE);
        }

        return false;
    }

    @Override
    public void showResult(List<TeacherBean> entities) {
        this.entities = entities;
        ChooseTeacherAdapter mAdapter = new ChooseTeacherAdapter(this);
        mAdapter.setEntities(entities);
        mListView.setAdapter(mAdapter);

        mListView.expandGroup(0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //从搜索界面拿到数据返回到搜索界面，并且把数据传回去
        if (requestCode == SEARCH_ALL_TEACHER_REQUEST_CODE && resultCode == ChooseTeacherSearchActivity.SEARCH_ALL_TEACHER_RESULT_CODE) {
            TeacherBean.TeacherListBean lTeacherListBean = (TeacherBean.TeacherListBean) data.getExtras().getSerializable(ChooseTeacherActivity.TEACHER_BEAN);
            Intent lIntent = getIntent();
            Bundle lBundle = new Bundle();
            lBundle.putSerializable(TEACHER_BEAN, lTeacherListBean);
            lIntent.putExtras(lBundle);
            setResult(mType.equals(ALL_TEACHER) ? TEACHER_SCHEDULE_RESULT_CODE : CHOOSE_TEACHER_4_APPLY_RESULT_CODE, lIntent);
            finish();
            //从第一级回去
        } else if (requestCode == SEARCH_SOME_TEACHER_REQUEST_CODE && resultCode == ChooseTeacherSearchActivity.SEARCH_SOME_TEACHER_RESULT_CODE) {
            TeacherBean.TeacherListBean lTeacherListBean = (TeacherBean.TeacherListBean) data.getExtras().getSerializable(ChooseTeacherActivity.TEACHER_BEAN);
            Intent lIntent = getIntent();
            Bundle lBundle = new Bundle();
            lBundle.putSerializable(TEACHER_BEAN, lTeacherListBean);
            lIntent.putExtras(lBundle);
            setResult(mType.equals(ALL_TEACHER) ? TEACHER_SCHEDULE_RESULT_CODE : CHOOSE_TEACHER_4_APPLY_RESULT_CODE, lIntent);
            finish();
            //从教师课表进入，从上一级回来
        } else if (requestCode == SEARCH_ALL_TEACHER_REQUEST_CODE && resultCode == TEACHER_SCHEDULE_RESULT_CODE) {
            TeacherBean.TeacherListBean lTeacherListBean = (TeacherBean.TeacherListBean) data.getExtras().getSerializable(ChooseTeacherActivity.TEACHER_BEAN);
            Intent lIntent = getIntent();
            Bundle lBundle = new Bundle();
            lBundle.putSerializable(TEACHER_BEAN, lTeacherListBean);
            lIntent.putExtras(lBundle);
            setResult(mType.equals(ALL_TEACHER) ? TEACHER_SCHEDULE_RESULT_CODE : CHOOSE_TEACHER_4_APPLY_RESULT_CODE, lIntent);
            finish();
            //从推荐教师进入，从上一级回来
        } else if (requestCode == SEARCH_SOME_TEACHER_REQUEST_CODE && resultCode == CHOOSE_TEACHER_4_APPLY_RESULT_CODE) {
            TeacherBean.TeacherListBean lTeacherListBean = (TeacherBean.TeacherListBean) data.getExtras().getSerializable(ChooseTeacherActivity.TEACHER_BEAN);
            Intent lIntent = getIntent();
            Bundle lBundle = new Bundle();
            lBundle.putSerializable(TEACHER_BEAN, lTeacherListBean);
            lIntent.putExtras(lBundle);
            setResult(mType.equals(ALL_TEACHER) ? TEACHER_SCHEDULE_RESULT_CODE : CHOOSE_TEACHER_4_APPLY_RESULT_CODE, lIntent);
            finish();
        }
    }
}
