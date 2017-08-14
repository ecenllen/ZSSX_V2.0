package com.gta.zssx.mobileOA.view.page;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gta.zssx.R;
import com.gta.zssx.mobileOA.model.bean.Meeting;
import com.gta.zssx.mobileOA.presenter.MeetingMainPresenter;
import com.gta.zssx.mobileOA.view.MeetingMainView;
import com.gta.zssx.mobileOA.view.adapter.MeetingAdapter;
import com.gta.zssx.mobileOA.view.base.BaseOAFragment;
import com.gta.zssx.pub.hfrecyclerview.HFRecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiaoxia.rang on 2016/10/26.
 */
@Deprecated
public class MeetingMainFragment extends BaseOAFragment<MeetingMainView,MeetingMainPresenter> implements  MeetingMainView,HFRecyclerView.HFRecyclerViewListener{
    private HFRecyclerView hfRecyclerView;
    private TextView tvEmpty;
    int position;
    private MeetingAdapter adapter;
    private List<Meeting> meetings = new ArrayList<>();
    private static final int REFRESH = 0;
    private static final int MORE = 1;

    @NonNull
    @Override
    public MeetingMainPresenter createPresenter() {
        return new MeetingMainPresenter();
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
        adapter = new MeetingAdapter(getActivity(),meetings);
        hfRecyclerView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.attachView(this);
        presenter.getMeetingList(REFRESH);
    }

    private void setOnInteractListener () {

        hfRecyclerView.setCanLoadMore (true);
        hfRecyclerView.setCanRefresh (true);
        hfRecyclerView.setFooterViewText ("");
        hfRecyclerView.setRecyclerViewListener (this);

    }

    @Override
    public void onRefresh() {
        presenter.getMeetingList(REFRESH);
    }

    @Override
    public void onLoadMore() {
        presenter.getMeetingList(MORE);
    }


    @Override
    public void showEmpty() {

    }

    @Override
    public void refreshMeetingList(List<Meeting> meetingList) {

    }

    @Override
    public void appendMeetingList(List<Meeting> meetingList) {

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

    @Override
    public void setServerTime(String serverTime) {
        adapter.setServerTime(serverTime);
    }
}
