package com.gta.zssx.fun.coursedaily.registercourse.view.pageV2;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.TextView;

import com.gta.zssx.AppConfiguration;
import com.gta.zssx.R;
import com.gta.zssx.fun.coursedaily.registercourse.model.bean.SearchClassBean;
import com.gta.zssx.fun.coursedaily.registercourse.presenter.ClassSearchPresenter;
import com.gta.zssx.fun.coursedaily.registercourse.view.ClassSearchView;
import com.gta.zssx.fun.coursedaily.registercourse.view.adapter.ClassSearchAdapter;
import com.gta.zssx.fun.personalcenter.model.bean.UserBean;
import com.gta.zssx.pub.base.BaseActivity;
import com.gta.zssx.pub.widget.ClearEditText;
import com.gta.zssx.pub.widget.WrapContentLinearLayoutManager;

import java.util.List;

/**
 * [Description]
 * <p>
 * [How to use]
 * <p>
 * [Tips]
 *
 * @author Created by Zhimin.Huang on 2016/6/17.
 * @since 1.0.0
 */
public class ClassSearchActivity extends BaseActivity<ClassSearchView, ClassSearchPresenter> implements ClassSearchView {

    public ClearEditText mClearEditText;
    public RecyclerView mRecyclerView;
    public TextView mCancleSearchTv;
    public ClassSearchAdapter mAdapter;
    private String mClassName;
    private UserBean mUserBean;

    @NonNull
    @Override
    public ClassSearchPresenter createPresenter() {
        return new ClassSearchPresenter();
    }

    @Override
    public void showResult(List<SearchClassBean> searchClassBeans) {
        showList();
    }

    @Override
    public void addSuccess() {

        if (!AppConfiguration.getInstance().isFistIn()) {
            CourseDailyActivity.start(ClassSearchActivity.this);
            CourseDailyActivity.isRefresh = true;
        } else {
            AppConfiguration.getInstance().setFirstIn(mUserBean);
//            sendBroadcast(new Intent(BaseActivity.ACTION_FINISH));
            AppConfiguration.getInstance().finishAllActivity();
            CourseDailyActivity.start(ClassSearchActivity.this);
        }
    }

    private void showList() {
        ClassSearchAdapter.Listener lListener = searchClassBean -> presenter.addClass(presenter.getClassBean(searchClassBean, mUserBean.getUserId()));

        mAdapter = new ClassSearchAdapter(this, lListener, presenter.getSearchClassBeen(), mClassName);

        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initData();
        initView();
    }


    private void initView() {
//        presenter.attachView(this);
        findView();
        setOnInteractListener();

        mRecyclerView.setLayoutManager(new WrapContentLinearLayoutManager(this));
    }

    private void setOnInteractListener() {

        mCancleSearchTv.setOnClickListener(v -> finish());

        //监听edittext变化
        mClearEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (mAdapter != null) {
                    mAdapter.removeData();
                }
                mClassName = s.toString().trim();
                if (!mClassName.isEmpty()) {
                    presenter.search(mClassName);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void findView() {
        mClearEditText = (ClearEditText) findViewById(R.id.search_edittext);
        mRecyclerView = (RecyclerView) findViewById(R.id.search_class_rv);
        mCancleSearchTv = (TextView) findViewById(R.id.cancle_search_tv);
    }

    private void initData() {
        try {
            mUserBean = AppConfiguration.getInstance().getUserBean();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        showSoftInputOrNot(mClearEditText,false);
        super.onDestroy();
    }
}
