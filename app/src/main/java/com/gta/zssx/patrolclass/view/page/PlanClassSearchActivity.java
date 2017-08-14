package com.gta.zssx.patrolclass.view.page;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.TextView;

import com.gta.zssx.R;
import com.gta.zssx.patrolclass.model.entity.AddClassEntity;
import com.gta.zssx.patrolclass.model.entity.PlanClassSearchEntity;
import com.gta.zssx.patrolclass.presenter.PlanClassSearchPresenter;
import com.gta.zssx.patrolclass.view.PlanClassSearchView;
import com.gta.zssx.patrolclass.view.adapter.PlanClassSearchAdapter;
import com.gta.zssx.pub.base.BaseMvpActivity;
import com.gta.zssx.pub.util.LogUtil;
import com.gta.zssx.pub.widget.ClearEditText;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liang.lu on 2016/8/23 14:02.
 */
public class PlanClassSearchActivity extends BaseMvpActivity<PlanClassSearchView, PlanClassSearchPresenter>
        implements PlanClassSearchView, PlanClassSearchAdapter.PlanClassSearchListener, View.OnClickListener, TextWatcher {
    private ClearEditText mClearEditText;
    private RecyclerView mRecyclerView;
    private TextView mCancleSearchTv;
    private List<PlanClassSearchEntity> entities;
    private PlanClassSearchAdapter mAdapter;
    private String mKeyWord;
    private String classType;
    private List<AddClassEntity> addClassEntities;
    private String date;

    @Override
    public int getLayoutId () {
        return R.layout.activity_search;
    }

    @Override
    protected void initData () {
        super.initData ();
        classType = getIntent ().getStringExtra ("classType");
        date = getIntent ().getStringExtra ("date");
    }

    @Override
    protected void initView () {
        findViews ();
        initToolBar ();
    }

    private void initToolBar () {
        mToolBarManager.setTitle (getString (R.string.patrol_search_class));
    }

    private void findViews () {
        mClearEditText = (ClearEditText) findViewById (R.id.search_edittext);
        mRecyclerView = (RecyclerView) findViewById (R.id.search_class_rv);
        mCancleSearchTv = (TextView) findViewById (R.id.cancle_search_tv);

        mClearEditText.addTextChangedListener (this);
        mCancleSearchTv.setOnClickListener (this);
    }


    @NonNull
    @Override
    public PlanClassSearchPresenter createPresenter () {
        return new PlanClassSearchPresenter ();
    }

    @Override
    public void showResult (List<PlanClassSearchEntity> entities) {
        this.entities = entities;
        LogUtil.e ("size==" + entities.size ());
        mAdapter = new PlanClassSearchAdapter (this);
        mAdapter.setEntities (entities);
        mAdapter.setmSearchString (mKeyWord);
        mAdapter.setListener (this);
        mRecyclerView.setLayoutManager (new LinearLayoutManager (this));
        mRecyclerView.setAdapter (mAdapter);
    }

    @Override
    public String getKeyWord () {
        return mKeyWord;
    }

    @Override
    public List<AddClassEntity> getEntity () {
        return addClassEntities;
    }

    @Override
    public void intoClassesPage () {
        PatrolClassActivity.start (this);
        PlanProtalResultActivity.start (this, "1", date);
        finish ();
    }

    @Override
    public void onClick (View view) {
        finish ();
    }

    @Override
    public void itemClick (View view, int position) {
        String deptId = entities.get (position).getDeptId ();
        int classId = entities.get (position).getId ();
        String className = entities.get (position).getClassName ();
        if (classType.equals ("random")) {
            //跳转到巡课登记页面
            PatrolClassActivity.start (this);
            PatrolRegisterActivity.start (this, deptId, classId, date, 0, 0, "1", 0, "0", className);
            finish ();
        } else {
            addClassEntities = new ArrayList<> ();
            AddClassEntity entity = new AddClassEntity ();
            entity.setClassID (classId);
            entity.setTeacherID (presenter.getUid ());
            addClassEntities.add (entity);
            presenter.uploadChooseClass ();
        }

    }

    @Override
    public void beforeTextChanged (CharSequence charSequence, int start, int before, int count) {

    }

    @Override
    public void onTextChanged (CharSequence s, int start, int before, int count) {
        if (mAdapter != null) {
            mAdapter.removeData ();
        }
        mKeyWord = s.toString ().trim ();
        if (!mKeyWord.isEmpty ()) {
            presenter.getPlanClassSearchDatas ();
        }
    }

    @Override
    public void afterTextChanged (Editable editable) {

    }

    public static void start (Context context, String classType, String date) {
        final Intent lIntent = new Intent (context, PlanClassSearchActivity.class);
        lIntent.putExtra ("classType", classType);
        lIntent.putExtra ("date", date);
        if (!(context instanceof Activity)) {
            lIntent.setFlags (Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity (lIntent);
    }
}
