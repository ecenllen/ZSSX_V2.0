package com.gta.zssx.patrolclass.view.page;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.gta.zssx.R;
import com.gta.zssx.patrolclass.model.entity.DockScoreEntity;
import com.gta.zssx.patrolclass.presenter.DockScorePresenter;
import com.gta.zssx.patrolclass.view.DockScoreView;
import com.gta.zssx.patrolclass.view.adapter.DockScoreAdapter;
import com.gta.zssx.patrolclass.view.dialog.DeleteDialog;
import com.gta.zssx.pub.base.BaseMvpActivity;

import java.io.Serializable;
import java.util.List;

/**
 * [Description]
 * <p/>   扣分详情
 * [How to use]
 * <p>
 * [Tips] Created by tengfei.lv on 2016/7/20 16:59.
 */

public class DockScoreActivity extends BaseMvpActivity<DockScoreView, DockScorePresenter> implements
        DockScoreAdapter.InputScoreErrorListener, DockScoreView, DockScoreAdapter.Scrolling {


    private RecyclerView recyclerView;
    private List<DockScoreEntity.DockScoreBean.OptionsBean> list;
    private boolean isLook;
    private DockScoreEntity mDockScoreEntity;
    private DockScoreAdapter mAdapter;
    private TextView noDataTv;

    @Override
    public int getLayoutId () {
        return R.layout.activity_dock_score;
    }

    @Override
    protected void initData () {
        super.initData ();
        isLook = getIntent ().getBooleanExtra ("isLook", false);
        list = (List<DockScoreEntity.DockScoreBean.OptionsBean>) getIntent ().getExtras ().getSerializable ("dockScoreList");
        mDockScoreEntity = (DockScoreEntity) getIntent ().getExtras ().getSerializable ("dockEntity");
    }

    @Override
    protected void initView () {
        findView ();
        initToolbar ();
    }

    private void findView () {
        recyclerView = (RecyclerView) findViewById (R.id.recycler_dock_score);
        noDataTv = (TextView) findViewById (R.id.tv_no_data);
        recyclerView.setHasFixedSize (true);
        recyclerView.setLayoutManager (new LinearLayoutManager (this));

        mAdapter = new DockScoreAdapter ();
        mAdapter.setListener (this);
        mAdapter.setScrolling (this);
        mAdapter.setDockScoreDatas (list);
        mAdapter.setIsLook (isLook);
        recyclerView.setAdapter (mAdapter);

        if (list == null) {
            recyclerView.setVisibility (View.GONE);
            noDataTv.setVisibility (View.VISIBLE);
        }

    }

    private void initToolbar () {
        mToolBarManager.getRightButton ().setEnabled (true);
        if (isLook) {
            mToolBarManager.getRightButton ().setEnabled (false);
        }
        mToolBarManager
                .showBack (true)
                .setTitle ("扣分详情")
                .showRightButton (true);

        if (list != null && !isLook) {
            mToolBarManager.showRightButton (true)
                    .setRightText ("完成")
                    .clickRightButton (v -> {
                        list = mAdapter.getDatas ();
                        boolean result = presenter.calculateScore (mDockScoreEntity, list);
                        if (result) {
                            Intent lIntent = new Intent ();
                            Bundle lBundle = new Bundle ();
                            lBundle.putSerializable ("entityMap", (Serializable) presenter.getResultMap ());
                            lBundle.putSerializable ("scoreListBeen", (Serializable) presenter.getGetScoreListBeens ());
                            lBundle.putSerializable ("checkList", (Serializable) list);
                            lIntent.putExtras (lBundle);
                            setResult (2, lIntent);
                            finish ();
                        } else {
                            new DeleteDialog (DockScoreActivity.this).showDialog (false, "确定", "输入的分数有误或者输入分数总和超过总分,请重新输入", null, false);
                        }
                    });
        }
    }

    @NonNull
    @Override
    public DockScorePresenter createPresenter () {
        return new DockScorePresenter ();
    }

    public static void start (Context context, Bundle bundle) {
        final Intent lIntent = new Intent (context, DockScoreActivity.class);
        if (!(context instanceof Activity)) {
            lIntent.setFlags (Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        lIntent.putExtras (bundle);
        context.startActivity (lIntent);
    }

    @Override
    public void onError (int position) {
        String msg = "输入的分数有误,请重新输入";
        new DeleteDialog (this).showDialog (false, "确定", msg, null, false);
    }

    @Override
    public boolean isScrolling () {
        return recyclerView.getScrollState () == RecyclerView.SCROLL_STATE_IDLE && !recyclerView.isComputingLayout ();
    }

    @Override
    public void onBackPressed () {
        Intent lIntent = new Intent ();
        Bundle lBundle = new Bundle ();
        lBundle.putSerializable ("checkList", (Serializable) list);
        lIntent.putExtras (lBundle);
        setResult (5, lIntent);
        super.onBackPressed ();
    }
}
