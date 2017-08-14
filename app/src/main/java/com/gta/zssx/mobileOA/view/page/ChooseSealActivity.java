package com.gta.zssx.mobileOA.view.page;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.gta.zssx.R;
import com.gta.zssx.mobileOA.model.bean.SealInfo;
import com.gta.zssx.mobileOA.presenter.ChooseSealPresenter;
import com.gta.zssx.mobileOA.view.ChooseSealView;
import com.gta.zssx.mobileOA.view.adapter.SealAdapter;
import com.gta.zssx.pub.base.BaseActivity;
import com.gta.zssx.pub.manager.ToolBarManager;
import com.gta.zssx.pub.widget.ClearEditText;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by xiaoxia.rang on 2017/4/24.
 * 手机端选择签章
 */
public class ChooseSealActivity extends BaseActivity<ChooseSealView, ChooseSealPresenter> implements ChooseSealView {

    private Toolbar mToolbar;
    private TextView tvTitle;
    private ClearEditText mClearEditText;
    private RecyclerView recyclerView;
    private TextView tvEmptyHint;
    private ToolBarManager mToolBarManager;
    private SealAdapter sealAdapter;
    private List<SealInfo> sealInfos = new ArrayList<>();
    private List<SealInfo> allSealInfos;
    private ISealSelectListener iSealSelectListener;


    @NonNull
    @Override
    public ChooseSealPresenter createPresenter() {
        return new ChooseSealPresenter();
    }

    public interface ISealSelectListener {
        void onSealSelected(SealInfo sealInfo);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oa_choose_seal);
        initView();
    }

    public void initView() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        tvTitle = (TextView) findViewById(R.id.title);
        mToolbar.setNavigationOnClickListener(view -> finish());
        tvTitle.setText(getString(R.string.title_seal));

        mClearEditText = (ClearEditText) findViewById(R.id.et_search);
        recyclerView = (RecyclerView) findViewById(R.id.rv_seals);
        tvEmptyHint = (TextView) findViewById(R.id.tv_emptyHint);
        // 根据输入框输入值的改变来过滤搜索
        mClearEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                // 当输入框里面的值为空，更新为原来的列表，否则为过滤数据列表
                filterData(s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        iSealSelectListener = new ISealSelectListener() {
            @Override
            public void onSealSelected(SealInfo sealInfo) {
                Intent intent = new Intent();
                intent.putExtra("data", new Gson().toJson(sealInfo));
                setResult(RESULT_OK, intent);
                finish();
            }
        };
        sealAdapter = new SealAdapter(this, sealInfos, iSealSelectListener);
        recyclerView.setAdapter(sealAdapter);
        presenter.getSealInfoList();
    }


    /**
     * 根据输入框中的值来过滤数据并更新ListView
     *
     * @param filterStr
     */
    private void filterData(String filterStr) {
        sealInfos.clear();
        for (SealInfo sealInfo : allSealInfos) {
            if (sealInfo.getSealName().contains(filterStr)) {
                sealInfos.add(sealInfo);
            }
        }
        sealAdapter.notifyDataSetChanged();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void showSealInfos(List<SealInfo> seals) {
        allSealInfos = seals;
        sealInfos.addAll(seals);
        sealAdapter.notifyDataSetChanged();
    }

    @Override
    public void showEmpty() {
        tvEmptyHint.setVisibility(View.VISIBLE);
    }
}
