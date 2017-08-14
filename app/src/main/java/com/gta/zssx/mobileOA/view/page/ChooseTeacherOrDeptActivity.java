package com.gta.zssx.mobileOA.view.page;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ExpandableListView;

import com.blankj.utilcode.util.ToastUtils;
import com.gta.zssx.R;
import com.gta.zssx.mobileOA.view.adapter.ChooseTeacherOrDeptAdapter;
import com.gta.zssx.patrolclass.model.entity.ChooseTeacherEntity;
import com.gta.zssx.patrolclass.presenter.ChooseTeacherPresenter;
import com.gta.zssx.patrolclass.view.ChooseTeacherView;
import com.gta.zssx.patrolclass.view.page.ChooseTeacherSearchActivity;
import com.gta.zssx.pub.base.BaseActivity;
import com.gta.zssx.pub.manager.ToolBarManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 选择老师Activity
 */
public class ChooseTeacherOrDeptActivity extends BaseActivity<ChooseTeacherView, ChooseTeacherPresenter>
        implements ChooseTeacherView, ExpandableListView.OnChildClickListener {
    public static final String MOBILE_OA = "mobile_oa";
    private ExpandableListView mListView;
    private ToolBarManager mToolBarManager;
    private Toolbar mToolbar;
    private List<ChooseTeacherEntity> entities;
    private String title;
    //区分是登记页面进入还是第二层级进入   1  登记页面进入   2  第二层级进入
    private String status;

    private int type;

    private List<String> deptIds = new ArrayList<> ();//跳入过的层级
    private List<String> titles = new ArrayList<> ();
    private HashMap<String, List<ChooseTeacherEntity>> deptMap = new HashMap<> ();
    private static HashMap<String, String> chooseMap = new HashMap<> ();

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_choose_teacher_expandable);
        title = getIntent ().getStringExtra ("title");
        status = getIntent ().getStringExtra ("status");
        if (title == null) {
            title = "中山市沙溪理工学校";
            titles.add (title);
        }
        deptId = getIntent ().getStringExtra ("deptId");
        if (deptId == null) {
            deptId = "-1";
        }
        inis ();
        inisToolBar ();
        presenter.getChooseTeacherDatas ();
    }

    private void inisToolBar () {
        mToolbar = (Toolbar) findViewById (R.id.toolbar);
        mToolBarManager = new ToolBarManager (mToolbar, this);
        mToolBarManager.init ();
        mToolbar.setNavigationOnClickListener (v ->
        {
            if (deptIds.size () == 1) {
                onBackPressed ();
            } else {
                goBackToUpperLevel ();
            }
        });
        mToolBarManager.showBack (true)
                .setTitle (title)
                .showIvSearch (true)
                .clickIvSearch (v -> {
                    //点击搜索
                    ChooseTeacherSearchActivity.start (ChooseTeacherOrDeptActivity.this,MOBILE_OA);
                });
    }

    private void inis () {
        mListView = (ExpandableListView) findViewById (R.id.expendlist);
        mListView.setDivider (null);
        mListView.setOnChildClickListener (this);
    }

    @NonNull
    @Override
    public ChooseTeacherPresenter createPresenter () {
        return new ChooseTeacherPresenter ();
    }

    @Override
    public void showResult (List<ChooseTeacherEntity> entities) {
        this.entities = entities;
        deptIds.add (deptId);
        deptMap.put (deptId, entities);
        ChooseTeacherOrDeptAdapter mAdapter = new ChooseTeacherOrDeptAdapter (this, type);
        mAdapter.setEntities (entities);
        mListView.setAdapter (mAdapter);
        if (deptIds.size () > 1) {
            for (int i = 0; i < mAdapter.getGroupCount (); i++) {
                mListView.expandGroup (i);
            }
        }

    }

    @Override
    public String getDeptId () {
        return deptId;
    }

    public static void start (Context context) {
        final Intent lIntent = new Intent (context, ChooseTeacherOrDeptActivity.class);
        if (!(context instanceof Activity)) {
            lIntent.setFlags (Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity (lIntent);
    }

    String deptId = "-1";


    @Override
    public boolean onChildClick (ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
        ChooseTeacherEntity.DeptListBean deptListBean = entities.get (groupPosition).getDeptList ().get (childPosition);
        if (deptListBean.getType () == 0) {//点击的是老师

            ToastUtils.showShortToast ("点击的是老师！");

        } else {//点击的是部门，请求数据，跳转至另一个页面显示
            deptId = deptListBean.getDeptId ();
            title = deptListBean.getDeptName ();
            titles.add (title);
            mToolBarManager.setTitle (title);
            if (deptMap.containsKey (deptId)) {
                ChooseTeacherOrDeptAdapter mAdapter = new ChooseTeacherOrDeptAdapter (this, type);
                entities = deptMap.get (deptId);
                mAdapter.setEntities (entities);
                mListView.setAdapter (mAdapter);
                mAdapter.notifyDataSetChanged ();
                if (deptIds.size () > 1) {
                    for (int i = 0; i < mAdapter.getGroupCount (); i++) {
                        mListView.expandGroup (i);
                    }
                }
            } else {
                presenter.getChooseTeacherDatas ();
            }

        }
        return false;
    }

    private void goBackToUpperLevel () {
        deptIds.remove (deptIds.size () - 1);
        titles.remove (titles.size () - 1);
        String upperDeptId = deptIds.get (deptIds.size () - 1);
        mToolBarManager.setTitle (titles.get (titles.size () - 1));
        ChooseTeacherOrDeptAdapter mAdapter = new ChooseTeacherOrDeptAdapter (this, type);
        entities = deptMap.get (upperDeptId);
        mAdapter.setEntities (entities);
        mListView.setAdapter (mAdapter);
        if (deptIds.size () > 1) {
            for (int i = 0; i < mAdapter.getGroupCount (); i++) {
                mListView.expandGroup (i);
            }
        }
        mAdapter.notifyDataSetChanged ();
    }

}
