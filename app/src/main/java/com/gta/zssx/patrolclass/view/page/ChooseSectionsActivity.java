package com.gta.zssx.patrolclass.view.page;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.gta.zssx.R;
import com.gta.zssx.patrolclass.model.entity.DockScoreEntity;
import com.gta.zssx.patrolclass.presenter.PatrolRegisterPresenter;
import com.gta.zssx.patrolclass.view.PatrolRegisterView;
import com.gta.zssx.patrolclass.view.adapter.ChooseSectionsAdapter;
import com.gta.zssx.pub.base.BaseMvpActivity;

import java.io.Serializable;
import java.util.List;

/**
 * [Description]
 * <p/>    节次选择页面
 * [How to use]
 * <p>
 * [Tips] Created by tengfei.lv on 2016/7/25 16:36.
 */

public class ChooseSectionsActivity extends BaseMvpActivity<PatrolRegisterView, PatrolRegisterPresenter> implements ChooseSectionsAdapter.Listener {
    private RecyclerView mRecyclerView;
    private List<DockScoreEntity.SectionsListBean> beanList;
    private TextView noDataTv;
    String deptId;
    int classId;
    String date;
    int xId;
    int pId;
    String type;
    String mType;
    String className;

    @Override
    public int getLayoutId () {
        return R.layout.activity_choose_sections;
    }

    @Override
    protected void initView () {
        findViews ();
        initToolbar ();
        setAdapter ();
    }

    @Override
    protected void initData () {
        super.initData ();
        deptId = getIntent ().getStringExtra ("deptId");
        classId = getIntent ().getIntExtra ("classId", -1);
        date = getIntent ().getStringExtra ("date");
        xId = getIntent ().getIntExtra ("xId", -1);
        type = getIntent ().getStringExtra ("type");
        pId = getIntent ().getIntExtra ("pId", 0);
        mType = getIntent ().getStringExtra ("mType");
        className = getIntent ().getStringExtra ("className");
        if (beanList != null && beanList.size () != 0) {
            beanList.removeAll (beanList);
        }
        beanList = (List<DockScoreEntity.SectionsListBean>) getIntent ().getExtras ().getSerializable ("beanList");
    }

    @Override
    protected void requestData () {
        super.requestData ();
    }

    private void setAdapter () {
        if (beanList == null || beanList.size () == 0) {
            showEmpty ();
        } else {
            ChooseSectionsAdapter mAdapter = new ChooseSectionsAdapter ();
            mAdapter.setBeanList (beanList);
            mAdapter.setListener (this);
            mRecyclerView.setAdapter (mAdapter);
        }
    }

    //绑定控件
    private void findViews () {
        mRecyclerView = (RecyclerView) findViewById (R.id.rv_setion_choose);
        noDataTv = (TextView) findViewById (R.id.tv_non);


        mRecyclerView.setHasFixedSize (true);
        mRecyclerView.setLayoutManager (new LinearLayoutManager (this));

        if (beanList == null) {
            mRecyclerView.setVisibility (View.GONE);
            noDataTv.setVisibility (View.VISIBLE);
        }
    }

    private void initToolbar () {
        mToolBarManager.setTitle (this.getString (R.string.section_choose));
        if (beanList == null || beanList.size () == 0) {
            return;
        }
        mToolBarManager.showRightButton (true)
                .setRightText (this.getString (R.string.finish))
                .clickRightButton (v -> {
                    boolean result = presenter.finishSectionChoose (beanList);
                    if (result) {
                        Intent lData = new Intent ();
                        Bundle lBundle = new Bundle ();
                        lBundle.putInt ("sectionId", presenter.getSectionId2 ());
                        lBundle.putString ("sectionName", presenter.getSectionName ());
                        lBundle.putString ("mType", mType);
                        lBundle.putSerializable ("beanList", (Serializable) beanList);
                        lData.putExtras (lBundle);
                        setResult (6, lData);
                        finish ();
                    } else {
                        ToastUtils.showShortToast ("请选择节次");
                    }
                });


    }


    @NonNull
    @Override
    public PatrolRegisterPresenter createPresenter () {
        //        return PatrolRegisterPresenter.getInstance ();
        return new PatrolRegisterPresenter ();
    }

    private void showEmpty () {
        mRecyclerView.setVisibility (View.GONE);
        noDataTv.setVisibility (View.VISIBLE);
    }

    @Override
    public void setEntities (List<DockScoreEntity.SectionsListBean> sectionsListBeen) {
        beanList = sectionsListBeen;
    }
}
