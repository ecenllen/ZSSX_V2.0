package com.gta.zssx.patrolclass.view.page;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.gta.zssx.R;
import com.gta.zssx.patrolclass.model.entity.PatrolClassEntity;
import com.gta.zssx.patrolclass.presenter.PatrolFinishedPresenter;
import com.gta.zssx.patrolclass.view.PatrolClassView;
import com.gta.zssx.patrolclass.view.adapter.PatrolClassAdapter;
import com.gta.zssx.pub.base.BaseMvpFragment;
import com.gta.zssx.pub.hfrecyclerview.HFRecyclerView;
import com.gta.zssx.pub.hfrecyclerview.HFRecyclerViewItemClickListener;

import java.util.List;


/**
 * Created by liang.lu1 on 2016/6/28.
 */
public class FinishedFragment extends BaseMvpFragment<PatrolClassView, PatrolFinishedPresenter> implements
        PatrolClassView, HFRecyclerViewItemClickListener, HFRecyclerView.HFRecyclerViewListener {
    private TextView noDataTv;
    private PatrolClassAdapter adapter;
    private List<PatrolClassEntity> entities;
    private HFRecyclerView mRecyclerView;

    @Override
    public int getLayoutId () {
        return R.layout.fragment_finished;
    }

    @Override
    protected void initView (View view) {
        mRecyclerView = (HFRecyclerView) view.findViewById (R.id.recycler);
        noDataTv = (TextView) view.findViewById (R.id.tv_non);
        setOnInteractListener ();
    }

    @Override
    protected void requestData () {
        super.requestData ();
        presenter.getFinishedData ();
    }

    private void setOnInteractListener () {
        mRecyclerView.setCanLoadMore (true);
        mRecyclerView.setCanRefresh (true);
        mRecyclerView.setFooterViewText ("");
        mRecyclerView.setRecyclerViewListener (this);
        mRecyclerView.setItemClickListener (this);
    }

    @NonNull
    @Override
    public PatrolFinishedPresenter createPresenter () {
        return new PatrolFinishedPresenter ();
    }

    @Override
    public void showResult (List<PatrolClassEntity> lists) {
        this.entities = lists;
        showList (lists);
    }

    @Override
    public void showEmpty (String state, boolean isSuccess) {
        //        mRecyclerView.setVisibility (View.GONE);
        if (adapter == null) {
            adapter = new PatrolClassAdapter (mActivity, 1);
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
        noDataTv.setVisibility (View.GONE);
        mRecyclerView.stopRefresh (true);
        mRecyclerView.stopLoadMore (true);
        entities = mPatrolClassModels;
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

    private void showList (List<PatrolClassEntity> lists) {
        noDataTv.setVisibility (View.GONE);
        if (adapter == null) {
            adapter = new PatrolClassAdapter (mActivity, 1);
            adapter.setEntities (lists);
            mRecyclerView.setAdapter (adapter);
            return;
        }
        adapter.setEntities (lists);
        adapter.notifyDataSetChanged ();
    }

    @Override
    public void onRefresh () {
        presenter.doRefresh ();
    }

    @Override
    public void onLoadMore () {
        presenter.doLoadMore ();
    }

    @Override
    public void onLongItemClick (View v, int position) {

    }

    @Override
    public void onItemClick (View v, int position) {
        int state = entities.get (position).getStatus ();
        int xId = entities.get (position).getxId ();
        String date = entities.get (position).getDate ();
        PatrolClassSelActivity.start (mActivity, state, 1, xId, date);
    }
}
