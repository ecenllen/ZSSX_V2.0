package com.gta.zssx.patrolclass.view.page;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.TextView;

import com.gta.zssx.AppConfiguration;
import com.gta.zssx.R;
import com.gta.zssx.fun.classroomFeedback.view.page.ChangedTeacherActivity;
import com.gta.zssx.patrolclass.model.entity.ChooseTeacherSearchEntity;
import com.gta.zssx.patrolclass.presenter.ChooseTeacherSearchPresenter;
import com.gta.zssx.patrolclass.view.ChooseTeacherSearchView;
import com.gta.zssx.patrolclass.view.adapter.ChooseTeacherSearchAdapter;
import com.gta.zssx.pub.base.BaseMvpActivity;
import com.gta.zssx.pub.util.LogUtil;
import com.gta.zssx.pub.util.RxBus;
import com.gta.zssx.pub.widget.ClearEditText;

import java.util.List;

import static com.gta.zssx.fun.classroomFeedback.view.page.ChangedTeacherActivity.CLASSROOM;

/**
 * Created by liang.lu on 2016/8/17 14:46.
 */
public class ChooseTeacherSearchActivity extends BaseMvpActivity<ChooseTeacherSearchView, ChooseTeacherSearchPresenter>
        implements ChooseTeacherSearchAdapter.ClickTeacherItemListener, ChooseTeacherSearchView, View.OnClickListener, TextWatcher {

    public static final String FLAG = "flag";
    private ClearEditText mClearEditText;
    private RecyclerView mRecyclerView;
    private TextView mCancleSearchTv;
    private List<ChooseTeacherSearchEntity> entities;
    private ChooseTeacherSearchAdapter mAdapter;
    private String mKeyWord;
    private String flag;

    @Override
    public int getLayoutId () {
        return R.layout.activity_choose_teacher_search;
    }

    @Override
    protected void initView () {
        findViews ();
        initToolBar();
    }

    private void initToolBar () {
        mToolBarManager.setTitle (R.string.patrol_search_teacher);
    }

    protected void initData () {
        flag = getIntent ().getExtras ().getString (FLAG);
    }

    @Override
    protected void requestData () {
        super.requestData ();
//        presenter.getTeacherSearchDatas ();
    }

    private void findViews () {
        mClearEditText = (ClearEditText) findViewById (R.id.search_edittext);
        mCancleSearchTv = (TextView) findViewById (R.id.cancle_search_tv);
        mRecyclerView = (RecyclerView) findViewById (R.id.search_teacher_rv);

        mClearEditText.addTextChangedListener (this);
        mCancleSearchTv.setOnClickListener (this);
    }


    @NonNull
    @Override
    public ChooseTeacherSearchPresenter createPresenter () {
        return new ChooseTeacherSearchPresenter ();
    }

    public static void start (Context context, String flag) {
        final Intent lIntent = new Intent (context, ChooseTeacherSearchActivity.class);
        Bundle mBundle = new Bundle ();
        mBundle.putString (FLAG, flag);
        lIntent.putExtras (mBundle);
        if (!(context instanceof Activity)) {
            lIntent.setFlags (Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity (lIntent);
    }

    @Override
    public void clickTeacherChooseItem (int position) {
        ChooseTeacherSearchEntity entity = entities.get (position);
        if (flag.equals (CLASSROOM)) {
            finish ();
            AppConfiguration.getInstance ().finishActivity (ChangedTeacherActivity.class);
            RxBus.getDefault ().post (entity);
            // chooseTeacher FINISH
            return;
        }
        //        String teacherName = entity.getName ();
        //        String teacherId = entity.getTeacherId ()+"";
        Intent intent = new Intent ();
        intent.setClass (ChooseTeacherSearchActivity.this, PatrolRegisterActivity.class);
        //        intent.putExtra ("teacherName",teacherName);
        //        intent.putExtra ("teacherId",teacherId);
        startActivity (intent);

        // chooseTeacher FINISH
        RxBus.getDefault ().post (entity);
        finish ();
    }

    @Override
    public void showResult (List<ChooseTeacherSearchEntity> entities) {
        this.entities = entities;
        LogUtil.e ("size==" + entities.size ());
        mAdapter = new ChooseTeacherSearchAdapter (this, this);
        mAdapter.setEntities (entities);
        mAdapter.setmSearchString (mKeyWord);
        mRecyclerView.setLayoutManager (new LinearLayoutManager (this));
        mRecyclerView.setAdapter (mAdapter);
    }

    @Override
    public String getKeyWord () {
        return mKeyWord;
    }

    @Override
    public void onClick (View v) {
        finish ();
    }

    @Override
    public void beforeTextChanged (CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged (CharSequence s, int start, int before, int count) {
        if (mAdapter != null) {
            mAdapter.removeData ();
        }
        mKeyWord = s.toString ().trim ();
        if (!mKeyWord.isEmpty ()) {
            presenter.getTeacherSearchDatas ();
        }
    }

    @Override
    public void afterTextChanged (Editable s) {

    }

}
