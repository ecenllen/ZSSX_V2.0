package com.gta.zssx.mobileOA.view.page;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.gta.zssx.R;
import com.gta.zssx.ZSSXApplication;
import com.gta.zssx.mobileOA.model.bean.DutyNoticeInfo;
import com.gta.zssx.mobileOA.presenter.DutyNoticePresenter;
import com.gta.zssx.mobileOA.view.DutyNoticeView;
import com.gta.zssx.mobileOA.view.adapter.DutyNoticeAdapter;
import com.gta.zssx.pub.base.BaseActivity;
import com.gta.zssx.pub.hfrecyclerview.HFRecyclerView;
import com.gta.zssx.pub.manager.ToolBarManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lan.zheng on 2016/11/7.  值班
 */
public class DutyNoticeActivity extends BaseActivity<DutyNoticeView,DutyNoticePresenter>
        implements DutyNoticeView,HFRecyclerView.HFRecyclerViewListener{

    private HFRecyclerView mRecyclerView;
    private DutyNoticeAdapter mDutyNoticeAdapter;
    private TextView emptyTextView;
    public Toolbar mToolbar;
    protected ToolBarManager mToolBarManager;

    public static final int DUTY_REGISTER = 1;
    public static final int SHIFT_STATUS_NOT_APPROVED = 1;
    public static final int REFRESH = 0;
    public static final int MORE = 1;
    private int pageIndex = 0;  //第一页
    private String UserId = ZSSXApplication.instance.getUser().getUserId();

    @NonNull
    @Override
    public DutyNoticePresenter createPresenter() {
        return new DutyNoticePresenter();
    }

    public static void start(Context context) {
        final Intent lIntent = new Intent(context, DutyNoticeActivity.class);
//        Bundle lBundle = new Bundle();
        if (!(context instanceof Activity)) {
            lIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(lIntent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_duty_notice);
        initView();
        initData();
    }

    private void initView(){
        emptyTextView = (TextView)findViewById(R.id.tv_emptyHint);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setNavigationOnClickListener(view -> onBackPressed());
        mToolBarManager = new ToolBarManager(mToolbar, this);
        mToolBarManager.init();
        mToolBarManager.setTitle(getString(R.string.oa_duty_notice));
        mRecyclerView = (HFRecyclerView) findViewById(R.id.rv_duty_notice);
        setOnInteractListener ();
    }

    private void initData(){
        emptyTextView.setVisibility(View.VISIBLE);
        //初始化适配器
        List<DutyNoticeInfo> dutyNoticeInfoArrayList = new ArrayList<>();
        mDutyNoticeAdapter = new DutyNoticeAdapter(this, listener ,dutyNoticeInfoArrayList);
        mRecyclerView.setAdapter(mDutyNoticeAdapter);
        //获取服务器数据
//        presenter.getUserDutyList(REFRESH, pageIndex);
        //测试数据
        List<DutyNoticeInfo> eventNoticeInfoList1 = presenter.testData();
        mDutyNoticeAdapter = new DutyNoticeAdapter(this, listener ,eventNoticeInfoList1);
        mRecyclerView.setAdapter(mDutyNoticeAdapter);
    }

    DutyNoticeAdapter.Listener listener = new DutyNoticeAdapter.Listener() {
        @Override
        public void onClickListener(DutyNoticeInfo dutyNoticeInfo) {
            if(dutyNoticeInfo.getArrangeType() == 1){      //值班安排
                if(dutyNoticeInfo.getDutyOrCheckDuty() == DUTY_REGISTER){
                    //值班登记
                    DutyRegisterOrCheckActivity.start(DutyNoticeActivity.this,dutyNoticeInfo.getDutyId(),dutyNoticeInfo.getDutyDetailId());
                }else {
                    //值班检查
                    DutyCheckAttendanceActivity.start(DutyNoticeActivity.this);
                }
            }else {
                if( TextUtils.equals(UserId,dutyNoticeInfo.getSwitchUserId()) && dutyNoticeInfo.getShiftStatus() == SHIFT_STATUS_NOT_APPROVED ){  //申请人且申请未被审批
                    DutyRegisterOrCheckActivity.start(DutyNoticeActivity.this,dutyNoticeInfo.getDutyId(),dutyNoticeInfo.getDutyDetailId()); //值班登记
                }else if(TextUtils.equals(UserId,dutyNoticeInfo.getToSwitchUserId())){  //被申请人，未审批进行审批，审批了进行登记
                    if(dutyNoticeInfo.getShiftStatus() == SHIFT_STATUS_NOT_APPROVED){
                        ChangeShiftDetailActivity.start(DutyNoticeActivity.this);  //值班审批
                    }else {
                        DutyRegisterOrCheckActivity.start(DutyNoticeActivity.this,dutyNoticeInfo.getDutyId(),dutyNoticeInfo.getDutyDetailId()); //值班登记
                    }
                }else{
                    //无操作
                }
            }

        }
    };

    private void setOnInteractListener () {
        mRecyclerView.setCanLoadMore (true);
        mRecyclerView.setCanRefresh (true);
        mRecyclerView.setFooterViewText ("");
        mRecyclerView.setRecyclerViewListener (this);

    }
    @Override
    public void onRefresh() {
        presenter.getUserDutyList(REFRESH, pageIndex);
    }

    @Override
    public void onLoadMore() {
        presenter.getUserDutyList(MORE, pageIndex);
    }

    @Override
    public void showEmptyView() {
        emptyTextView.setVisibility(View.VISIBLE);
    }

    @Override
    public void showDutyListData(List<DutyNoticeInfo> list,int action) {
        emptyTextView.setVisibility(View.GONE);
        if(action == REFRESH){
            mRecyclerView.stopRefresh(true);
            //TODO 刷新数据

        }else {
            mRecyclerView.stopLoadMore(true);
            //TODO 加载更多

        }
        pageIndex++;
    }

    @Override
    public void onLoadMoreError() {
        mRecyclerView.stopLoadMore(false);
    }

    @Override
    public void onRefreshError() {
        mRecyclerView.stopRefresh(false);
    }

    @Override
    public void onLoadMoreEmpty() {
        mRecyclerView.stopLoadMore(false);
        mRecyclerView.setFooterViewText("无更多信息");
    }
}
