package com.gta.zssx.mobileOA.view.page;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.gta.utils.resource.Toast;
import com.gta.zssx.R;
import com.gta.zssx.mobileOA.model.bean.DutyDateInfo;
import com.gta.zssx.mobileOA.model.bean.DutyTimeInfo;
import com.gta.zssx.mobileOA.model.bean.DutyTimeListInfo;
import com.gta.zssx.mobileOA.presenter.DutyRegisterOrCheckPresenter;
import com.gta.zssx.mobileOA.view.DutyRegisterOrCheckView;
import com.gta.zssx.mobileOA.view.adapter.DutyTimePeriodAdapter;
import com.gta.zssx.pub.base.BaseActivity;
import com.gta.zssx.pub.manager.ToolBarManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lan.zheng on 2016/11/14.  值班登记
 */
public class DutyRegisterOrCheckActivity  extends BaseActivity<DutyRegisterOrCheckView,DutyRegisterOrCheckPresenter>
        implements DutyRegisterOrCheckView,View.OnClickListener {
    public Toolbar mToolbar;
    protected ToolBarManager mToolBarManager;
    private DutyTimePeriodAdapter mDutyTimePeriodAdapter; //值班登记时段适配器
    private TextView mAllEmptyTextView;                   //全部为空,只有第一次网络有问题才显示
    private TextView mListEmptyTextView;                  //列表空页面
    private RecyclerView mRecyclerViewRegister;           //值日登记
    private TextView mDutyDateTextView;                   //日期
    private TextView mDutyStatusTextView;                 //状态
    private TextView mDutyRegisterSubmitTextView;

    private static final int DUTY_TYPE_REGISTER = 1;   //值日登记
    private static final String DUTY_ID = "dutyID";
    private static final String DUTY_DETAIL_ID = "dutyDetailID";
    public static final int REQUEST_CODE = 1000;       //回调结果
    public static final int STATUS_HAVE_BEEN_SUBMIT_1 = 1;
    public static final int STATUS_NOT_SUBMIT_0 = 0;
    public static final int STATUS_NOT_COME_DUTY_2 = 2;

    private int mRegisterOrCheck;  //登记还是检查,用于获取日期的时候区分要获取的接口
    private int mDutyId;   //值班id
    private int mDutyDetailId; //值班详细id

    public int mRegisterStatus; //1.已提交 0.未提交 2.未值班
    private String mDateShow ;  //日期显示,用于获取数据
    private boolean mIsFutureDuty = true;  //未值班还是已值班,默认true

    @NonNull
    @Override
    public DutyRegisterOrCheckPresenter createPresenter() {
        return new DutyRegisterOrCheckPresenter();
    }

    public static void start(Context context,int dutyId,int dutyDetailId) {
        final Intent lIntent = new Intent(context, DutyRegisterOrCheckActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt(DUTY_ID,dutyId);
        bundle.putInt(DUTY_DETAIL_ID,dutyDetailId);
        lIntent.putExtras(bundle);
        if (!(context instanceof Activity)) {
            lIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(lIntent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_duty_register_or_check);
        mDutyId = getIntent().getExtras().getInt(DUTY_ID,0);
        mDutyDetailId = getIntent().getExtras().getInt(DUTY_DETAIL_ID,0);
        mRegisterOrCheck = DUTY_TYPE_REGISTER;
        initView();
        initData();
    }

    private void initView(){
        mAllEmptyTextView = (TextView)findViewById(R.id.tv_all_empty_view) ;
        mAllEmptyTextView.setOnClickListener(this);
        mRecyclerViewRegister = (RecyclerView)findViewById(R.id.recycler_view_register);
        mDutyStatusTextView = (TextView)findViewById(R.id.tv_duty_status);
        mDutyDateTextView = (TextView) findViewById(R.id.tv_duty_date);
        mDutyDateTextView.setOnClickListener(this);
        mListEmptyTextView = (TextView) findViewById(R.id.tv_list_empty_view);
        mDutyRegisterSubmitTextView = (TextView)findViewById(R.id.tv_submit_register);
        mDutyRegisterSubmitTextView.setOnClickListener(this);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mToolBarManager = new ToolBarManager(mToolbar, this);
        mToolBarManager.init();
        mToolBarManager.setTitle(getString(R.string.text_duty_register))
                .setRightText(getResources().getString(R.string.text_duty_table)).clickRightButton(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DutyTableInfoActivity.start(DutyRegisterOrCheckActivity.this);
                }
        });
    }

    private void initData(){
        mRecyclerViewRegister.setLayoutManager(new LinearLayoutManager(this));
        //默认显示暂无数据VISIBLE
        mAllEmptyTextView.setVisibility(View.VISIBLE);
        //初始化数据界面开始的数据

        renewTimeView();
        List<Object> list = new ArrayList<>();
        mDutyTimePeriodAdapter = new DutyTimePeriodAdapter(this,list,listener,STATUS_NOT_COME_DUTY_2);
        mRecyclerViewRegister.setAdapter(mDutyTimePeriodAdapter);
//        presenter.getRegisterTimeListData(mDutyDetailId,null);  //TODO 去服务器获取真正的最近时间信息
        mDateShow = "2016年12月30日";
        //TODO 测试数据--模拟获取数据成功
        mAllEmptyTextView.setVisibility(View.GONE);  //默认时间段列表为空
        DutyTimeListInfo dutyRegisterOrCheckInfo = presenter.testDataTime();
        //设置日期和状态
        mDateShow = presenter.getFormatDate(dutyRegisterOrCheckInfo.getDate());
        mDutyDateTextView.setText(mDateShow);
        //是否是未来的日期，未来日期或者已提交的都隐藏“提交”按钮
        mIsFutureDuty = presenter.compareDate(dutyRegisterOrCheckInfo.getServiceDate(),dutyRegisterOrCheckInfo.getDate());
        if(mIsFutureDuty || dutyRegisterOrCheckInfo.getStatus() == STATUS_HAVE_BEEN_SUBMIT_1){
            mDutyRegisterSubmitTextView.setVisibility(View.GONE);
        }else {
            mDutyRegisterSubmitTextView.setVisibility(View.VISIBLE);
        }
        //设置右边的显示状态
        setStatusTextView(dutyRegisterOrCheckInfo.getStatus());
        //设置时间段
        List<Object> list2 = dutyRegisterOrCheckInfo.getTimeList();
        mDutyTimePeriodAdapter = new DutyTimePeriodAdapter(this,list2,listener,mRegisterStatus);
        mRecyclerViewRegister.setAdapter(mDutyTimePeriodAdapter);
    }

    private void renewTimeView(){
        //还原别的数据值
        mDutyDateTextView.setText(mDateShow);
        mDutyStatusTextView.setText("");
    }

    DutyTimePeriodAdapter.Listener listener = new DutyTimePeriodAdapter.Listener() {
        @Override
        public void onClickListener(Object object) {
            if(mIsFutureDuty){  //未值班的不需要看详情
                Toast.Short(DutyRegisterOrCheckActivity.this,"未值班无法进行登记相关操作");
                return;
            }
            DutyRegisterDetialInfoActivity.start(DutyRegisterOrCheckActivity.this,(DutyTimeInfo)object,mDutyDetailId,mDateShow,mRegisterStatus);
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_duty_date:
                Intent intent = new Intent(this,DutyDateSelectActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("duty_detail_id",mDutyDetailId);
                bundle.putInt("duty_type",mRegisterOrCheck);  //未提交、已提交、未值班
                intent.putExtras(bundle);
                startActivityForResult(intent,REQUEST_CODE);
                break;
            case R.id.tv_all_empty_view:
                //点击重新去获取数据
                presenter.getRegisterTimeListData(mDutyDetailId,mDateShow);
                break;
            case R.id.tv_submit_register:
                //TODO 提交登记信息
                break;

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            DutyDateInfo dutyDateInfo = (DutyDateInfo) data.getExtras().getSerializable("object");
            int dutyStatus = data.getExtras().getInt("duty_status",2);//已值班，未值班
            if (dutyStatus == 1){
                mIsFutureDuty = false;  //非未来的值班
            }else {
                mIsFutureDuty  = true;  //未来的值班
            }
            String date = dutyDateInfo.getDate();
            mDateShow = presenter.getFormatDate(date);  //TODO 格式有待处理
            renewTimeView();  //重新设置时间和状态
            mListEmptyTextView.setVisibility(View.VISIBLE);  //默认时间段列表为空
//            presenter.getRegisterTimeListData(mDutyDetailId,mDateShow);  //TODO 登记
            //TODO 测试数据
            mListEmptyTextView.setVisibility(View.GONE);  //默认时间段列表为空
            DutyTimeListInfo dutyRegisterOrCheckInfo = presenter.testDataTime();
            //是否是未来的日期，未来日期或者已提交的都隐藏“提交”按钮
            if(mIsFutureDuty || dutyDateInfo.getStatus() == STATUS_HAVE_BEEN_SUBMIT_1){
                mDutyRegisterSubmitTextView.setVisibility(View.GONE);
            }else {
                mDutyRegisterSubmitTextView.setVisibility(View.VISIBLE);
            }
            //设置右边的显示状态
            setStatusTextView(dutyDateInfo.getStatus());
            //设置时间段
            List<Object> list2 = dutyRegisterOrCheckInfo.getTimeList();
            mDutyTimePeriodAdapter = new DutyTimePeriodAdapter(this,list2,listener,mRegisterStatus);
            mRecyclerViewRegister.setAdapter(mDutyTimePeriodAdapter);
        }
    }

    /**
     * 状态TextView
     */
    private void setStatusTextView(int status){
        if(mIsFutureDuty){
            mRegisterStatus = STATUS_NOT_COME_DUTY_2;
            mDutyStatusTextView.setText("未值班");
        }else {
            if(status == STATUS_HAVE_BEEN_SUBMIT_1){
                mRegisterStatus = STATUS_HAVE_BEEN_SUBMIT_1;
                mDutyStatusTextView.setText("已提交");
            }else {
                mRegisterStatus = STATUS_NOT_SUBMIT_0;
                mDutyStatusTextView.setText("未提交");
            }
        }
    }

    @Override
    public void getRegisterTimePeriodSuccess(DutyTimeListInfo dutyTimeListInfo, boolean isFutureDuty) {
        mAllEmptyTextView.setVisibility(View.GONE);
        //设置日期和状态
        mDateShow = presenter.getFormatDate(dutyTimeListInfo.getDate());
        mDutyDateTextView.setText(mDateShow);
        mIsFutureDuty = isFutureDuty;
        setStatusTextView(dutyTimeListInfo.getStatus());
        if(mIsFutureDuty){
            mDutyRegisterSubmitTextView.setVisibility(View.GONE);
        }else {
            mDutyRegisterSubmitTextView.setVisibility(View.VISIBLE);
        }
        //设置时间段
        List<Object> list2 = dutyTimeListInfo.getTimeList();
        mDutyTimePeriodAdapter = new DutyTimePeriodAdapter(this,list2,listener,mRegisterStatus);
        mRecyclerViewRegister.setAdapter(mDutyTimePeriodAdapter);
    }

    @Override
    public void showAllEmptyView() {
        mAllEmptyTextView.setVisibility(View.VISIBLE);
        renewTimeView();
        mDutyTimePeriodAdapter.clearData();
    }

    @Override
    public void showListEmptyView() {
        mListEmptyTextView.setVisibility(View.VISIBLE);
        renewTimeView();
        mDutyTimePeriodAdapter.clearData();
    }
}