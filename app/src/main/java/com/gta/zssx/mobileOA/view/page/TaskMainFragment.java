package com.gta.zssx.mobileOA.view.page;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gta.zssx.R;
import com.gta.zssx.mobileOA.model.bean.Tasks;
import com.gta.zssx.mobileOA.presenter.TaskMainPresenter;
import com.gta.zssx.mobileOA.view.TaskMainView;
import com.gta.zssx.mobileOA.view.adapter.TaskAdapter;
import com.gta.zssx.mobileOA.view.base.BaseOAFragment;
import com.gta.zssx.pub.hfrecyclerview.HFRecyclerView;
import com.gta.zssx.pub.hfrecyclerview.HFRecyclerViewItemClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiaoxia.rang on 2016/10/19.
 */

public class TaskMainFragment extends BaseOAFragment<TaskMainView, TaskMainPresenter> implements TaskMainView,HFRecyclerViewItemClickListener, HFRecyclerView.HFRecyclerViewListener {

    private HFRecyclerView hfRecyclerView;
    private TextView tvEmpty;
    int position;
    private TaskAdapter adapter;
    private List<Tasks.TaskInfo> taskInfos = new ArrayList<>();
    private static final int REFRESH = 0;
    private static final int MORE = 1;

    @NonNull
    @Override
    public TaskMainPresenter createPresenter() {
        return new TaskMainPresenter();
    }

    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);
        position = args.getInt("position");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        position = getArguments().getInt("position");
        View view = inflater.inflate(R.layout.fragment_oa_task, container, false);
        hfRecyclerView = (HFRecyclerView) view.findViewById(R.id.recycler);
        setOnInteractListener();
        tvEmpty = (TextView) view.findViewById(R.id.tv_emptyHint);
        tvEmpty.setText("页面" + position);
        adapter = new TaskAdapter(getActivity(),taskInfos);
        hfRecyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.attachView(this);
        presenter.getTasks(REFRESH);
    }

    private void setOnInteractListener () {

        hfRecyclerView.setCanLoadMore (true);
        hfRecyclerView.setCanRefresh (true);
        hfRecyclerView.setFooterViewText ("");
        hfRecyclerView.setRecyclerViewListener (this);
        hfRecyclerView.setItemClickListener (this);

    }

    @Override
    public void onRefresh() {
        presenter.getTasks(REFRESH);
    }

    @Override
    public void onLoadMore() {
        presenter.getTasks(MORE);
    }

    @Override
    public void onItemClick(View v, int position) {

    }

    @Override
    public void onLongItemClick(View v, int position) {

    }

    @Override
    public void showEmpty() {

    }

    @Override
    public void showTasksList(int opStatus, List<Tasks.TaskInfo> tasks) {
        switch (opStatus){
            case REFRESH:
                hfRecyclerView.stopRefresh(true);
                taskInfos.clear();
                taskInfos.addAll(tasks);
                adapter.notifyDataSetChanged();
                break;
            case MORE:
                hfRecyclerView.stopLoadMore(true);
                taskInfos.addAll(tasks);
                adapter.notifyDataSetChanged();
                break;
        }
    }

    @Override
    public void onLoadMoreError() {
        hfRecyclerView.stopLoadMore(false);
    }

    @Override
    public void onRefreshError() {
        hfRecyclerView.stopRefresh(false);
    }

    @Override
    public void onLoadMoreEmpty() {
        hfRecyclerView.stopRefresh(false);
        hfRecyclerView.setFooterViewText ("无更多信息");
    }
}
