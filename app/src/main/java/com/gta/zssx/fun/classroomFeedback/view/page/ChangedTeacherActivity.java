package com.gta.zssx.fun.classroomFeedback.view.page;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.gta.zssx.AppConfiguration;
import com.gta.zssx.R;
import com.gta.zssx.fun.classroomFeedback.presenter.ChangedTeacherPresenter;
import com.gta.zssx.fun.classroomFeedback.view.ChangedTeacherView;
import com.gta.zssx.patrolclass.model.entity.ChooseTeacherEntity;
import com.gta.zssx.patrolclass.model.entity.ChooseTeacherSearchEntity;
import com.gta.zssx.patrolclass.view.adapter.ChooseTeacherAdapter;
import com.gta.zssx.patrolclass.view.page.ChooseTeacherSearchActivity;
import com.gta.zssx.pub.base.BaseMvpActivity;
import com.gta.zssx.pub.util.RxBus;

import java.util.List;

import static com.gta.zssx.patrolclass.view.page.ChooseTeacherActivity.STATUS_TWO;

/**
 * [Description]
 * <p>教师选择页面
 * [How to use]
 * <p>
 * [Tips]
 *
 * @author Created by liang.lu on 2017/6/28 10:27.
 * @since 2.0.0
 */

public class ChangedTeacherActivity extends BaseMvpActivity<ChangedTeacherView, ChangedTeacherPresenter>
        implements ChangedTeacherView, ExpandableListView.OnChildClickListener {

    public static final String TITLE = "title";
    public static final String STATE = "state";
    public static final String DEPT_ID = "dept_id";
    public static final String TEACHER_ID = "teacher_id";
    public static final String CLASSROOM = "classroom";
    private TextView mEmptyTv;
    private ExpandableListView mListView;
    private List<ChooseTeacherEntity> mBeanList;
    private String mTitle;
    private String mState;
    private String mTeacherId;
    private String mDeptId;

    @Override
    public int getLayoutId () {
        return R.layout.activity_choose_teacher;
    }

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        intiData ();
        intiToolBar ();
        setInteractListener ();
        loadData ();
    }

    @Override
    protected void initView () {
        mEmptyTv = (TextView) findViewById (R.id.tv_non);
        mListView = (ExpandableListView) findViewById (R.id.expendlist);
        mListView.setDivider (null);
    }

    private void intiData () {
        mState = (String) getIntent ().getExtras ().get (STATE);
        mTitle = (String) getIntent ().getExtras ().get (TITLE);
        mTeacherId = (String) getIntent ().getExtras ().get (TEACHER_ID);
        mDeptId = getIntent ().getExtras ().getString (DEPT_ID);

        if (mTitle == null) {
            mTitle = getString (R.string.please_choose);
        }

        if (mDeptId == null) {
            mDeptId = "-1";
        }
    }

    private void intiToolBar () {
        mToolBarManager.setTitle (mTitle)
                .showIvSearch (true)
                .clickIvSearch (v -> {
                    //点击搜索
                    ChooseTeacherSearchActivity.start (ChangedTeacherActivity.this, CLASSROOM);
                });
    }

    private void setInteractListener () {
        mListView.setOnChildClickListener (this);
    }

    private void loadData () {
        presenter.loadData (mDeptId);
    }

    @NonNull
    @Override
    public ChangedTeacherPresenter createPresenter () {
        return new ChangedTeacherPresenter ();
    }

    @Override
    public boolean onChildClick (ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
        ChooseTeacherEntity.DeptListBean deptListBean = mBeanList.get (groupPosition).getDeptList ().get (childPosition);
        if (deptListBean.getType () == 0) {//点击的是老师
            if (mTeacherId != null) {
                if (mTeacherId.equals (deptListBean.getDeptId ())) {
                    ToastUtils.showShortToast("请选择不同上课教师");
                    return false;
                }
            }
            ChooseTeacherSearchEntity entity = new ChooseTeacherSearchEntity ();
            entity.setTeacherId (deptListBean.getDeptId ());
            entity.setName (deptListBean.getDeptName ());
            RxBus.getDefault ().post (entity);
            AppConfiguration.getInstance ().finishActivity (ChangedTeacherActivity.class);
        } else {//点击的是部门，请求数据，跳转至另一个页面显示
            mDeptId = deptListBean.getDeptId ();
            mTitle = deptListBean.getDeptName ();
            Intent mIntent = new Intent (this, ChangedTeacherActivity.class);
            Bundle mBundle = new Bundle ();
            mBundle.putString (DEPT_ID, mDeptId);
            mBundle.putString (TITLE, mTitle);
            mIntent.putExtra (STATE, STATUS_TWO);
            mIntent.putExtras (mBundle);
            startActivity (mIntent);
        }
        return false;
    }

    @Override
    public void showResult (List<ChooseTeacherEntity> beanList) {
        this.mBeanList = beanList;
        ChooseTeacherAdapter mAdapter = new ChooseTeacherAdapter (this);
        mAdapter.setEntities (beanList);
        mListView.setAdapter (mAdapter);
        if (mState.equals (STATUS_TWO)) {
            for (int i = 0; i < mAdapter.getGroupCount (); i++) {
                mListView.expandGroup (i);
            }
        }
    }

    @Override
    public void showEmpty () {
        mListView.setVisibility (View.GONE);
        mEmptyTv.setVisibility (View.VISIBLE);
    }
}
