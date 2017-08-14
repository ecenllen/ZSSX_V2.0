package com.gta.zssx.fun.adjustCourse.view.page;

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
import com.gta.zssx.fun.adjustCourse.deprecated.view.CourseApplyFragment;
import com.gta.zssx.fun.adjustCourse.model.bean.ChooseTeacherSearchEntity;
import com.gta.zssx.fun.adjustCourse.model.bean.GetTeacherBean;
import com.gta.zssx.fun.adjustCourse.model.bean.TeacherBean;
import com.gta.zssx.fun.adjustCourse.presenter.ChooseTeacherSearchPresenter;
import com.gta.zssx.fun.adjustCourse.view.ChooseTeacherSearchView;
import com.gta.zssx.fun.adjustCourse.view.adapter.ChooseTeacherSearchAdapter;
import com.gta.zssx.pub.base.BaseActivity;
import com.gta.zssx.pub.util.LogUtil;
import com.gta.zssx.pub.widget.ClearEditText;
import com.jakewharton.rxbinding.widget.RxTextView;

import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;

/**
 * [Description]
 * <p>
 * [How to use]
 * <p>
 * [Tips]
 *
 * @author Created by Zhimin.Huang on 2016/8/17 14:46.
 * @since 1.0.0
 */
public class ChooseTeacherSearchActivity extends BaseActivity<ChooseTeacherSearchView, ChooseTeacherSearchPresenter>
        implements ChooseTeacherSearchAdapter.ClickTeacherItemListener, ChooseTeacherSearchView, View.OnClickListener {

    public static final int SEARCH_ALL_TEACHER_RESULT_CODE = 116;
    public static final int SEARCH_SOME_TEACHER_RESULT_CODE = 119;
    private ClearEditText mClearEditText;
    private RecyclerView mRecyclerView;
    private List<ChooseTeacherSearchEntity> entities;
    private GetTeacherBean mGetTeacherBean;
    public String mType;
    private TextView mEmptyTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_teacher_search);
        initData();
        findViews();
    }

    private void initData() {
        mType = getIntent().getStringExtra(ChooseTeacherActivity.TYPE);
        mGetTeacherBean = (GetTeacherBean) getIntent().getExtras().getSerializable(CourseApplyFragment.GET_TEACHER_BEAN);
    }

    private void findViews() {
        mClearEditText = (ClearEditText) findViewById(R.id.search_edittext);
        TextView lCancleSearchTv = (TextView) findViewById(R.id.cancle_search_tv);
        mRecyclerView = (RecyclerView) findViewById(R.id.search_teacher_rv);
        mEmptyTv = (TextView) findViewById(R.id.empty_tv);

        assert lCancleSearchTv != null;
        lCancleSearchTv.setOnClickListener(this);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        RxTextView.textChanges(mClearEditText)
                .debounce(400, TimeUnit.MILLISECONDS)
                .subscribeOn(AndroidSchedulers.mainThread())
                .filter(charSequence -> charSequence.toString().trim().length() > 0)
                .switchMap(new Func1<CharSequence, Observable<String>>() {
                    @Override
                    public Observable<String> call(CharSequence charSequence) {
                        return Observable.just(charSequence.toString());
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(String s) {
                        if (mType.equals(ChooseTeacherActivity.ALL_TEACHER)) {
                            presenter.searchAllTeacher(s);
                        } else {
                            presenter.searchSomeTeacher(s, mGetTeacherBean);
                        }
                    }
                });

    }


    @NonNull
    @Override
    public ChooseTeacherSearchPresenter createPresenter() {
        return new ChooseTeacherSearchPresenter(mActivity);
    }

    public static void start(Context context) {
        final Intent lIntent = new Intent(context, ChooseTeacherSearchActivity.class);
        if (!(context instanceof Activity)) {
            lIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(lIntent);
    }

    @Override
    public void clickTeacherChooseItem(int position) {
        ChooseTeacherSearchEntity entity = entities.get(position);
        if (mType.equals(ChooseTeacherActivity.ALL_TEACHER)) {
            TeacherBean.TeacherListBean lListBean = new TeacherBean.TeacherListBean();
            lListBean.setTeacherName(entity.getName() + "[" + entity.getTeacherCode() + "]");
            lListBean.setTeacherBId(entity.getTeacherId());
            Intent intent = getIntent();
            Bundle lBundle = new Bundle();
            lBundle.putSerializable(ChooseTeacherActivity.TEACHER_BEAN, lListBean);
            intent.putExtras(lBundle);
            showSoftInputOrNot(mClearEditText, false);
            setResult(SEARCH_ALL_TEACHER_RESULT_CODE, intent);
        } else {
            TeacherBean.TeacherListBean lTeacherListBean = new TeacherBean.TeacherListBean();
            lTeacherListBean.setTeacherName(entity.getName() + "[" + entity.getTeacherCode() + "]");
            lTeacherListBean.setTeacherId(entity.getTeacherId());
            lTeacherListBean.setTeacherBId(entity.getTeacherBId());
            Intent intent = getIntent();
            Bundle lBundle = new Bundle();
            lBundle.putSerializable(ChooseTeacherActivity.TEACHER_BEAN, lTeacherListBean);
            intent.putExtras(lBundle);
            showSoftInputOrNot(mClearEditText, false);
            setResult(SEARCH_SOME_TEACHER_RESULT_CODE, intent);
        }

        finish();
    }

    @Override
    public void showResult(List<ChooseTeacherSearchEntity> entities) {
        mEmptyTv.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.VISIBLE);
        this.entities = entities;
        LogUtil.e("size==" + entities.size());
        ChooseTeacherSearchAdapter lAdapter = new ChooseTeacherSearchAdapter(this, this);
        lAdapter.setEntities(entities);
        lAdapter.setmSearchString(mClearEditText.getText().toString().trim());

        mRecyclerView.setAdapter(lAdapter);
    }

    @Override
    public void showEmpty() {
        mRecyclerView.setVisibility(View.GONE);
        mEmptyTv.setVisibility(View.VISIBLE);
    }


    @Override
    public void onClick(View v) {
        finish();
    }
}
