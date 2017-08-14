package com.gta.zssx.mobileOA.view.page;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gta.zssx.R;
import com.gta.zssx.mobileOA.presenter.DutyDateSelectPresenter;
import com.gta.zssx.mobileOA.view.DutyDateSelectView;
import com.gta.zssx.mobileOA.view.adapter.DutyTimePeriodAdapter;
import com.gta.zssx.mobileOA.view.base.BaseOAFragment;
import com.gta.zssx.pub.hfrecyclerview.HFRecyclerView;
import com.gta.zssx.pub.util.LogUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lan.zheng on 2016/11/15.
 */
public class DutyDateSelectFragment extends BaseOAFragment<DutyDateSelectView,DutyDateSelectPresenter> implements  DutyDateSelectView,HFRecyclerView.HFRecyclerViewListener{
    private HFRecyclerView hfRecyclerView;
    private TextView mEmptyTextView;
    private static final int REFRESH = 0;
    private static final int MORE = 1;
    private DutyTimePeriodAdapter mDutyTimePeriodAdapter;
    private int mRegisterOrCheck;
    private int mDutyDetailId;
    private int mPosition;
    private DateSelectListener mDateSelectListener;
    public static final String DUTY_TYPE = "duty_type";
    public static final String DUTY_DETAIL_ID = "duty_detail_id";
    private static final int DUTY_TYPE_REGISTER = 1;
    @NonNull
    @Override
    public DutyDateSelectPresenter createPresenter() {
        return new DutyDateSelectPresenter();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_duty_date_select, container, false);
        hfRecyclerView = (HFRecyclerView) view.findViewById(R.id.rv_duty_date_select);
        setOnInteractListener();
        mEmptyTextView  = (TextView)view.findViewById(R.id.tv_emptyHint);
        mEmptyTextView.setVisibility(View.GONE);
        mDutyDetailId = getArguments().getInt(DUTY_DETAIL_ID,0);
        mRegisterOrCheck = getArguments().getInt(DUTY_TYPE,1);  //登记还是检查
        mPosition  = getArguments().getInt("position",0);  //已还是未
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.attachView(this);
        initData();
    }

    private void initData(){
        List<Object> list = new ArrayList<>();
        if(mRegisterOrCheck == DutyDateSelectActivity.DUTY_TYPE_REGISTER){
            //登记
            if(mPosition == 0){  //已值班
                list = presenter.testList();
                presenter.getRegisterDateListData(mDutyDetailId,mPosition+1);
            }else {   //未值班
                list = presenter.testListRegister();
            }
        }else{
            //检查
            if(mPosition == 0){  //已值班
                list = presenter.testList1();
            }else {   //未值班
                list = presenter.testListCheck();
            }
        }
        mDutyTimePeriodAdapter = new DutyTimePeriodAdapter(getActivity(),list,listener,mRegisterOrCheck,mPosition);
        hfRecyclerView.setAdapter(mDutyTimePeriodAdapter);
    }

    DutyTimePeriodAdapter.Listener listener = new DutyTimePeriodAdapter.Listener() {
        @Override
        public void onClickListener(Object object) {
            Bundle bundle = new Bundle();
            bundle.putInt(DUTY_TYPE,mRegisterOrCheck);  //登记还是检查
            bundle.putSerializable("object", (Serializable) object);  //时间和状态
            bundle.putInt("duty_status",mPosition+1);  //已值班、未值班
            mDateSelectListener.DateSelect(bundle);
        }
    };

    @Override
    public void onRefresh() {

    }

    @Override
    public void onLoadMore() {

    }

    public void setDateSelectListener(DateSelectListener listener){
        mDateSelectListener = listener;
    }



    private void setOnInteractListener () {

        hfRecyclerView.setCanLoadMore (true);
        hfRecyclerView.setCanRefresh (true);
        hfRecyclerView.setFooterViewText ("");
        hfRecyclerView.setRecyclerViewListener (this);

    }

    @Override
    public void showEmpty() {

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

    public interface DateSelectListener{
        void DateSelect(Bundle b);
    }
}
