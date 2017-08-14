package com.gta.zssx.patrolclass.view.page;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.gta.zssx.R;
import com.gta.zssx.fun.personalcenter.view.page.PersonCenterActivity;
import com.gta.zssx.main.HomePageActivity;
import com.gta.zssx.patrolclass.model.entity.PatrolClassEntity;
import com.gta.zssx.patrolclass.popup.AddPageActivity;
import com.gta.zssx.patrolclass.presenter.SearchResultPresenter;
import com.gta.zssx.patrolclass.view.SearchResultView;
import com.gta.zssx.patrolclass.view.adapter.PatrolClassAdapter;
import com.gta.zssx.pub.base.BaseMvpActivity;
import com.gta.zssx.pub.hfrecyclerview.HFRecyclerView;
import com.gta.zssx.pub.hfrecyclerview.HFRecyclerViewItemClickListener;
import com.gta.zssx.pub.util.LogUtil;
import com.gta.zssx.pub.util.TimeUtils;

import java.util.List;

/**
 * Created by liang.lu1 on 2016/7/19.
 * 查询时间段巡课记录搜索结果页面。
 */
public class SearchResultActivity extends BaseMvpActivity<SearchResultView, SearchResultPresenter> implements
        SearchResultView, View.OnClickListener, HFRecyclerViewItemClickListener, HFRecyclerView.HFRecyclerViewListener {
    /**
     * 查询的时间段
     */
    private TextView tvDate, noDataTv;
    private TextView tab_home, tab_add, tab_my;
    private String startDate, endDate;
    private HFRecyclerView mRecyclerView;
    private List<PatrolClassEntity> entities;
    private PatrolClassAdapter adapter;

    @Override
    public int getLayoutId () {
        return R.layout.activity_search_result;
    }

    @Override
    protected void initData () {
        super.initData ();
        startDate = getIntent ().getStringExtra ("startDate");
        endDate = getIntent ().getStringExtra ("endDate");
    }

    @Override
    protected void initView () {
        findViews ();
        initToolBar ();
        setOnInteractListener ();
    }

    private void findViews () {
        tab_home = (TextView) findViewById (R.id.tab_home);
        tab_add = (TextView) findViewById (R.id.tab_add);
        tab_my = (TextView) findViewById (R.id.tab_my);
        tvDate = (TextView) findViewById (R.id.tv_date3);
        noDataTv = (TextView) findViewById (R.id.tv_non);
        mRecyclerView = (HFRecyclerView) findViewById (R.id.recycler);
        tvDate.setText (startDate + " 至 " + endDate);
    }

    @Override
    protected void requestData () {
        super.requestData ();
        presenter.getSearchResultData ();
    }

    private void initToolBar () {
        mToolBarManager.showBack (true)
                .setTitle ("已登记巡课记录")
                .showRightIcTime (true)
                .clickTime (v -> HistoryTimeActivity.start (SearchResultActivity.this));
    }

    private void setOnInteractListener () {
        tab_home.setOnClickListener (this);
        tab_add.setOnClickListener (this);
        tab_my.setOnClickListener (this);
        mRecyclerView.setCanLoadMore (true);
        mRecyclerView.setCanRefresh (true);
        mRecyclerView.setFooterViewText ("");
        mRecyclerView.setRecyclerViewListener (this);
        mRecyclerView.setItemClickListener (this);
    }


    @NonNull
    @Override
    public SearchResultPresenter createPresenter () {
        return new SearchResultPresenter ();
    }

    @Override
    public void showResult (List<PatrolClassEntity> list) {
        this.entities = list;
        noDataTv.setVisibility (View.GONE);
        mRecyclerView.setVisibility (View.VISIBLE);
        if (adapter == null) {
            adapter = new PatrolClassAdapter (this, 2);
            adapter.setEntities (list);
            mRecyclerView.setAdapter (adapter);
            return;
        }
        adapter.setEntities (list);
        adapter.notifyDataSetChanged ();
    }

    @Override
    public String getStartDate () {
        return startDate;
    }

    @Override
    public String getEndDate () {
        return endDate;
    }

    @Override
    public void showEmpty (String state, boolean isSuccess) {
        //        mRecyclerView.setVisibility (View.GONE);
        if (adapter == null) {
            adapter = new PatrolClassAdapter (this, 2);
            adapter.setEntities (null);
            mRecyclerView.setAdapter (adapter);
        } else {
            adapter.setEntities (null);
            adapter.notifyDataSetChanged ();
        }
        if (state.equals (NotFinishedFragment.REFRESH)) {
            mRecyclerView.stopRefresh (isSuccess);
        }
        noDataTv.setVisibility (View.VISIBLE);
    }

    @Override
    public void hideLoadMoreAndRefresh (List<PatrolClassEntity> mPatrolClassModels) {
        this.entities = mPatrolClassModels;
        noDataTv.setVisibility (View.GONE);
        mRecyclerView.stopRefresh (true);
        mRecyclerView.stopLoadMore (true);
        adapter.setEntities (mPatrolClassModels);
        adapter.notifyDataSetChanged ();
    }

    @Override
    public void onLoadMoreError () {
        mRecyclerView.stopLoadMore (false);
    }

    @Override
    public void onRefreshError () {
        mRecyclerView.stopRefresh (false);
    }

    @Override
    public void onLoadMoreEmpty () {
        mRecyclerView.stopLoadMore (false);
        mRecyclerView.setFooterViewText ("无更多信息");
    }

    public static void start (Context context, String startDate, String endDate) {
        final Intent lIntent = new Intent (context, SearchResultActivity.class);
        lIntent.putExtra ("startDate", startDate);
        lIntent.putExtra ("endDate", endDate);
        if (!(context instanceof Activity)) {
            lIntent.setFlags (Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity (lIntent);
    }

    @Override
    public void onClick (View v) {
        if (TimeUtils.isFastDoubleClick ()) {
            return;
        }
        switch (v.getId ()) {
            case R.id.tab_home:
                HomePageActivity.start (this);
                finish ();
                break;
            case R.id.tab_add:
                //                AddPageActivity.start4Result (this);
                //                new NewAddPopupWindow (SearchResultActivity.this, this, 3).onCreate ();
                startActivity (new Intent (this, AddPageActivity.class));
                //                overridePendingTransition (R.anim.add_page_enter_anim,R.anim.add_page_out_anim);
                break;
            case R.id.tab_my:
                PersonCenterActivity.start (this);
                break;
        }
    }

    @Override
    public void onLoadMore () {
        presenter.doLoadMore ();
    }

    @Override
    public void onRefresh () {
        presenter.doRefresh ();
    }

    @Override
    public void onItemClick (View v, int position) {
        LogUtil.e ("点击");
        int state = entities.get (position).getStatus ();
        int xId = entities.get (position).getxId ();
        String date = entities.get (position).getDate ();
        PatrolClassSelActivity.start (SearchResultActivity.this, state, 1, xId, date);
    }

    @Override
    public void onLongItemClick (View v, int position) {
        LogUtil.e ("长按" + position);
    }

    @Override
    protected void onResume () {
        super.onResume ();
        requestData ();
    }
}
