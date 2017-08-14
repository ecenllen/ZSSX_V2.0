package com.gta.zssx.mobileOA.view.page;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gta.zssx.R;
import com.gta.zssx.mobileOA.model.bean.ApplyForm;
import com.gta.zssx.mobileOA.model.bean.MyApplyBean;
import com.gta.zssx.mobileOA.presenter.ApplyPresenter;
import com.gta.zssx.mobileOA.view.ApplyView;
import com.gta.zssx.mobileOA.view.adapter.ApplyFormAdapter;
import com.gta.zssx.mobileOA.view.adapter.MyApplyAdapter;
import com.gta.zssx.mobileOA.view.adapter.ui.titlePopupWindow;
import com.gta.zssx.pub.base.BaseActivity;
import com.gta.zssx.pub.common.Constant;
import com.gta.zssx.pub.hfrecyclerview.HFRecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * 发起申请Activity
 */
public class ApplyActivity extends BaseActivity<ApplyView, ApplyPresenter> implements HFRecyclerView.HFRecyclerViewListener, ApplyView {
    private TextView tvTitle;
    private RecyclerView rvApply;
    private HFRecyclerView rvMyApply;
    private TextView tvEmptyHint;

    String[] titles = {"事项申请", "我的申请"};
    private ApplyFormAdapter formAdapter;
    private MyApplyAdapter myApplyAdapter;
    private List<MyApplyBean> backlogs = new ArrayList<>();

    public static void start(Context context) {
        final Intent lIntent = new Intent(context, ApplyActivity.class);
//        Bundle lBundle = new Bundle();
        if (!(context instanceof Activity)) {
            lIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(lIntent);
    }


    @NonNull
    @Override
    public ApplyPresenter createPresenter() {
        return new ApplyPresenter();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oa_apply);
        inits();
    }

    private void inits() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        ImageView ivSearch = (ImageView) findViewById(R.id.iv_search);
        tvTitle = (TextView) findViewById(R.id.tv_select_title);
        rvApply = (RecyclerView) findViewById(R.id.rv_applyList);
        rvMyApply = (HFRecyclerView) findViewById(R.id.rv_myApplyList);
        tvEmptyHint = (TextView) findViewById(R.id.tv_emptyHint);
        toolbar.setNavigationOnClickListener((v)->onBackPressed());
        ivSearch.setVisibility(View.GONE);
        tvTitle.setText(titles[0]);
        tvTitle.setOnClickListener((view)-> showTitleItems());

        setOnInteractListener();
        myApplyAdapter = new MyApplyAdapter(ApplyActivity.this, backlogs);
        rvMyApply.setAdapter(myApplyAdapter);
        presenter.getApplyFormList();
    }

    /**
     * 显示title选项
     */
    private void showTitleItems() {
        String title = tvTitle.getText().toString();
        titlePopupWindow mTitlePopuWindow = new titlePopupWindow(this, titles, title, new titlePopupWindow.Listener() {
            @Override
            public void onPopupWindowDismissListener() {
                //无操作
                if (tvTitle.getText().equals(titles[0])) {
                    rvApply.setVisibility(View.VISIBLE);
                    rvMyApply.setVisibility(View.GONE);
                    tvEmptyHint.setVisibility(View.GONE);
                    if (formAdapter == null) {
                        presenter.getApplyFormList();
                    }
                } else {
                    rvApply.setVisibility(View.GONE);
                    rvMyApply.setVisibility(View.VISIBLE);
                    tvEmptyHint.setVisibility(View.GONE);
                    presenter.getMyApplyList(Constant.REFRESH);
                }
            }

            @Override
            public void onItemClickListener(int position) {
                if (title.equals(titles[position]))
                    return;
                tvTitle.setText(titles[position]);

            }
        });
        mTitlePopuWindow.showAsDropDown(tvTitle);

    }


    private void setOnInteractListener() {
        rvMyApply.setCanLoadMore(true);
        rvMyApply.setCanRefresh(true);
        rvMyApply.setFooterViewText("");
        rvMyApply.setRecyclerViewListener(this);
    }

    @Override
    public void showFormList(List<ApplyForm> forms) {
        tvEmptyHint.setVisibility(View.GONE);
        formAdapter = new ApplyFormAdapter(ApplyActivity.this, forms);
        rvApply.setAdapter(formAdapter);
    }

    @Override
    public void showRefreshApplyList(List<MyApplyBean> backlogList) {
        tvEmptyHint.setVisibility(View.GONE);
        rvMyApply.stopRefresh(true);
        backlogs.clear();
        backlogs.addAll(backlogList);
        myApplyAdapter.notifyDataSetChanged();
    }

    @Override
    public void showLoadMoreApplyList(List<MyApplyBean> backlogList) {
        rvMyApply.stopLoadMore(true);
        backlogs.addAll(backlogList);
        myApplyAdapter.notifyItemRangeChanged(backlogs.size(),backlogList.size());
    }


    @Override
    public void showEmpty() {
        rvApply.setVisibility(View.GONE);
        rvMyApply.setVisibility(View.GONE);
        tvEmptyHint.setVisibility(View.VISIBLE);
    }

    @Override
    public void onLoadMoreError() {
        rvMyApply.stopLoadMore(false);
    }

    @Override
    public void onRefreshError() {
        rvMyApply.stopRefresh(false);
//        ToastUtils.showShortToast(ApplyActivity.this, "请求失败！");
    }

    @Override
    public void onLoadMoreEmpty() {
        rvMyApply.stopLoadMore(false);
        rvMyApply.setFooterViewText("无更多信息");
    }

    @Override
    public void setServerTime(String serverTime) {
        myApplyAdapter.setServerTime(serverTime);
    }

    @Override
    public void onRefresh() {
        presenter.getMyApplyList(Constant.REFRESH);
    }

    @Override
    public void onLoadMore() {
        presenter.getMyApplyList(Constant.LOAD_MORE);
    }
}
