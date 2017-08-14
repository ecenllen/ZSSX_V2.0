package com.gta.zssx.fun.coursedaily.registercourse.view.page;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gta.zssx.R;
import com.gta.zssx.fun.coursedaily.registercourse.model.bean.LogStatisticsBean;
import com.gta.zssx.fun.coursedaily.registercourse.presenter.LogStatisticsPresenter;
import com.gta.zssx.fun.coursedaily.registercourse.view.LogStatisticsView;
import com.gta.zssx.fun.coursedaily.registercourse.view.adapter.LogStatisticeAdapter;
import com.gta.zssx.pub.base.BaseFragment;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 日志统计
 * Created by xiao.peng on 2016/11/9.
 */
public class LogStatisticsFragment extends BaseFragment<LogStatisticsView, LogStatisticsPresenter> implements LogStatisticsView, LogStatisticeAdapter.OnTimeNotifyListener {

    @Bind(R.id.class_display_rv)
    RecyclerView mRecyclerView;
    @Bind(R.id.swipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @Bind(R.id.no_data_tv)
    TextView noDataTv;

    private LogStatisticeAdapter mLogStatisticeAdapter;
    private String mDate;


    public static LogStatisticsFragment newInstance() {

        Bundle args = new Bundle();

        LogStatisticsFragment fragment = new LogStatisticsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public LogStatisticsPresenter createPresenter() {
        return new LogStatisticsPresenter();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_log_statiscs, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // 格式化当前时间，并转换为年月日整型数据
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        this.mDate = sdf.format(new Date(System.currentTimeMillis()));

        initView();
        loadData();
    }

    //初始化view
    private void initView() {
        mSwipeRefreshLayout.setColorSchemeResources(R.color.main_color, R.color.blue, R.color.purple);
        //刷新事件
        mSwipeRefreshLayout.setOnRefreshListener(() -> presenter.getLogChart(mDate));

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void loadData() {
        //刷新事件
        mSwipeRefreshLayout.post(() -> presenter.getLogChart(mDate));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void showLoadingDialog() {
        mSwipeRefreshLayout.post(() -> mSwipeRefreshLayout.setRefreshing(true));
    }


    @Override
    public void showResult(LogStatisticsBean logStatisticsBean) {
        noDataTv.setVisibility(View.GONE);
        mSwipeRefreshLayout.post(() -> mSwipeRefreshLayout.setRefreshing(false));
        showList(logStatisticsBean);
    }

    private void showList(LogStatisticsBean logStatisticsBean) {
        if (mLogStatisticeAdapter == null) {
            mLogStatisticeAdapter = new LogStatisticeAdapter(getActivity(), logStatisticsBean, this);
            mRecyclerView.setAdapter(mLogStatisticeAdapter);
        } else {
            mLogStatisticeAdapter.setNotifyDatas(logStatisticsBean);
        }
    }

    @Override
    public void emptyUI(boolean isEmpty) {
        mSwipeRefreshLayout.post(() -> mSwipeRefreshLayout.setRefreshing(false));
//        if (mLogStatisticeAdapter != null) {
            if (isEmpty) {
                noDataTv.setVisibility(View.VISIBLE);
            }
//        }
    }

    @Override
    public void timeNotify(String date) {
        this.mDate = date;
        presenter.getLogList(date);
    }
}
