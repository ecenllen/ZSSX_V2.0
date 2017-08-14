package com.gta.zssx.patrolclass.view.page;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.gta.utils.resource.Toast;
import com.gta.zssx.R;
import com.gta.zssx.patrolclass.model.entity.PlanPatrolResultEntity;
import com.gta.zssx.patrolclass.presenter.PlanPatrolResultPresenter;
import com.gta.zssx.patrolclass.view.PlanPatrolResultView;
import com.gta.zssx.patrolclass.view.adapter.PlanPatrolResultAdapter;
import com.gta.zssx.patrolclass.view.dialog.DeleteDialog;
import com.gta.zssx.pub.base.BaseMvpActivity;

import java.util.List;

/**
 * [Description] <p/>   按计划巡课-显示选择班级之后的页面 [How to use]
 * <p>
 * [Tips] Created by tengfei.lv on 2016/7/14 16:33.
 */

public class PlanProtalResultActivity extends BaseMvpActivity<PlanPatrolResultView, PlanPatrolResultPresenter>
        implements PlanPatrolResultView, PlanPatrolResultAdapter.RecyclerViewLongItemClickListener,
        PlanPatrolResultAdapter.RecyclerViewItemClickListener {

    private RecyclerView recyclerView;
    private LinearLayout addClassLayout;
    private PlanPatrolResultAdapter mAdapter;
    private List<PlanPatrolResultEntity> entityList;
    //长按选中的item位置
    private int position = -1;
    private int deptId;
    private int classId;
    private String type;
    private String date;

    @Override
    public int getLayoutId () {
        return R.layout.activity_plan_protal_result;
    }

    @Override
    protected void initData () {
        super.initData ();
        type = getIntent ().getStringExtra ("type");
        date = getIntent ().getStringExtra ("date");
    }

    protected void initView () {
        findViews ();
        initToolBar ();
        setOnInteractListener ();
    }

    private void initToolBar () {
        mToolBarManager.showBack (true).setTitle ("按计划巡课班级");
    }

    @Override
    protected void requestData () {
        super.requestData ();
        presenter.loadPlanClassResultDatas ();
    }

    private void setOnInteractListener () {
        recyclerView.setHasFixedSize (true);
        recyclerView.setLayoutManager (new LinearLayoutManager (this));
        addClassLayout.setOnClickListener (v -> {
            AddClassChooseActivity.start (PlanProtalResultActivity.this, "plan", "1", date);
            //                FINISH ();
        });
    }

    private void findViews () {
        recyclerView = (RecyclerView) findViewById (R.id.recycler_plan_protal);
        addClassLayout = (LinearLayout) findViewById (R.id.layout_add_class_plan);
    }

    @NonNull
    @Override
    public PlanPatrolResultPresenter createPresenter () {
        return new PlanPatrolResultPresenter ();
    }


    @Override
    public void showSuccessData (List<PlanPatrolResultEntity> entityList) {
        this.entityList = entityList;
        if (mAdapter == null) {
            mAdapter = new PlanPatrolResultAdapter ();
            mAdapter.setClassResultDatas (entityList);
            mAdapter.setItemClickLister (this);
            mAdapter.setLongItemClickListener (this);
            recyclerView.setAdapter (mAdapter);
        } else {
            mAdapter.setClassResultDatas (entityList);
            mAdapter.notifyDataSetChanged ();
        }
    }

    @Override
    public void DeleteClassItemSuccess () {
        entityList.remove (position);
        mAdapter.setClassResultDatas (entityList);
        mAdapter.notifyItemRemoved (position);
        Toast.Short (PlanProtalResultActivity.this, "删除成功");
        requestData ();
    }

    @Override
    public int getDeptId () {
        return deptId;
    }

    @Override
    public int getClassId () {
        return classId;
    }

    @Override
    public void startAddClassPage () {
        finish ();
        AddClassChooseActivity.start (PlanProtalResultActivity.this, "plan", "1", date);
    }

    @Override
    public void onBackPressed () {
        finish ();
    }

    public static void start (Context context, String type, String date) {
        final Intent lIntent = new Intent (context, PlanProtalResultActivity.class);
        lIntent.putExtra ("type", type);
        lIntent.putExtra ("date", date);
        if (!(context instanceof Activity)) {
            lIntent.setFlags (Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity (lIntent);
    }

    @Override
    public void onItemClick (View v, int position) {
        String deptId = entityList.get (position).getDeptId () + "";
        int classId = entityList.get (position).getClassId ();
        String className = entityList.get (position).getClassName ();
        if (type.equals ("2")) {
            Intent lData = new Intent ();
            Bundle lBundle = new Bundle ();
            lBundle.putString ("deptId", deptId);
            lBundle.putInt ("classId", classId);
            lBundle.putString ("className", className);
            lBundle.putString ("isType", "1");//标明是随机还是按计划  0  随机  1  按计划
            lData.putExtras (lBundle);
            setResult (11, lData);
            finish ();
        } else {
            PatrolRegisterActivity.start (this, deptId, classId, date, 0, 0, "1", 0, "1", className);
        }
    }

    @Override
    public void onLongItemClick (View v, int position) {
        this.position = position;
        deptId = entityList.get (position).getDeptId ();
        classId = entityList.get (position).getClassId ();
        new DeleteDialog (this).showDialog (true, "删除", "你确定要删除此班级吗?",
                () -> {
                    //移除班级
                    presenter.deletePatrolClassItem ();
                }, false);
    }

    @Override
    protected void onResume () {
        super.onResume ();
        if (entityList != null) {
            requestData ();
        }
    }
}
