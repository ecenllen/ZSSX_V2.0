package com.gta.zssx.fun.adjustCourse.view.page;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.TextView;

import com.gta.zssx.R;
import com.gta.zssx.fun.adjustCourse.model.bean.SearchClassBean;
import com.gta.zssx.fun.adjustCourse.model.bean.StuClassBean;
import com.gta.zssx.fun.adjustCourse.presenter.ClassSearchPresenter;
import com.gta.zssx.fun.adjustCourse.view.AdjustClassSearchView;
import com.gta.zssx.fun.adjustCourse.view.adapter.ClassSearchAdapter;
import com.gta.zssx.pub.base.BaseActivity;
import com.gta.zssx.pub.widget.ClearEditText;
import com.gta.zssx.pub.widget.WrapContentLinearLayoutManager;

import java.util.List;
import java.util.regex.Pattern;

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
public class AdjustClassSearchActivity extends BaseActivity<AdjustClassSearchView, ClassSearchPresenter> implements AdjustClassSearchView {

    public static final int CLASS_SEARCH_RESULT_CODE = 118;
    public ClearEditText mClearEditText;
    public RecyclerView mRecyclerView;
    public TextView mCancleSearchTv;
    public ClassSearchAdapter mAdapter;
    private String mClassName;
    private TextView mEmptyTv;

    @NonNull
    @Override
    public ClassSearchPresenter createPresenter() {
        return new ClassSearchPresenter();
    }

    @Override
    public void showResult(List<SearchClassBean> searchClassBeans) {
        mEmptyTv.setVisibility(View.GONE);
        showList();
    }

    @Override
    public void showEmpty() {
        mEmptyTv.setVisibility(View.VISIBLE);
    }


    private void showList() {
        ClassSearchAdapter.Listener lListener = searchClassBean -> {
            StuClassBean.ClassListBean mClassListBean = new StuClassBean.ClassListBean();
            mClassListBean.setId(searchClassBean.getId());
            mClassListBean.setClassName(searchClassBean.getClassName());
            Intent lIntent = getIntent();
            Bundle lBundle = new Bundle();
            lBundle.putSerializable(ChooseClassActivity.CLASSLISTBEAN, mClassListBean);
            lIntent.putExtras(lBundle);
            setResult(CLASS_SEARCH_RESULT_CODE, lIntent);
            showSoftInputOrNot(mClearEditText, false);
            finish();
        };

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
//                if (mAdapter != null) {
//                    mAdapter.removeData();
//                }
//                mClassName = s.toString().trim();
//                if (!mClassName.isEmpty()) {
//                    if (mClassName.equals("_") || mClassName.equals("%")) {
//                        mClassName = Pattern.quote(mClassName);
//                    }
//
//                    presenter.search(mClassName);
//                }


                if (mHandler.hasMessages(MSG_SEARCH)) {
                    mHandler.removeMessages(MSG_SEARCH);
                }
                //如果为空 直接显示搜索历史
                if (!TextUtils.isEmpty(s)) {
                    mHandler.sendEmptyMessageDelayed(MSG_SEARCH, 300); //自动搜索功能 删除
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (mAdapter != null) {
                mAdapter.removeData();
            }
            //搜索请求
            mClassName = mClearEditText.getText().toString().trim();
            if (!mClassName.isEmpty()) {
                if (mClassName.equals("_") || mClassName.equals("%")) {
                    mClassName = Pattern.quote(mClassName);
                }

                presenter.search(mClassName);
            }
        }
    };
    private static final int MSG_SEARCH = 1;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    private void findView() {
        mClearEditText = (ClearEditText) findViewById(R.id.search_edittext);
        mRecyclerView = (RecyclerView) findViewById(R.id.search_class_rv);
        mCancleSearchTv = (TextView) findViewById(R.id.cancle_search_tv);
        mEmptyTv = (TextView) findViewById(R.id.empty_tv);
    }

    private void initData() {
    }

    @Override
    protected void onDestroy() {
        showSoftInputOrNot(mClearEditText, false);
        super.onDestroy();
    }
}
