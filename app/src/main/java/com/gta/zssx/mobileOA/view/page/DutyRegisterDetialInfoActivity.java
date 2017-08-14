package com.gta.zssx.mobileOA.view.page;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gta.utils.resource.Toast;
import com.gta.zssx.R;
import com.gta.zssx.mobileOA.model.bean.DutyRegisterDetailInfo;
import com.gta.zssx.mobileOA.model.bean.DutyTimeInfo;
import com.gta.zssx.mobileOA.presenter.DutyRegisterDetialInfoPresenter;
import com.gta.zssx.mobileOA.view.DutyRegisterDetialInfoView;
import com.gta.zssx.pub.base.BaseActivity;
import com.gta.zssx.pub.manager.ToolBarManager;

/**
 * Created by lan.zheng on 2016/11/16.
 */
public class DutyRegisterDetialInfoActivity extends BaseActivity<DutyRegisterDetialInfoView,DutyRegisterDetialInfoPresenter>
implements DutyRegisterDetialInfoView,View.OnClickListener{
    public Toolbar mToolbar;
    protected ToolBarManager mToolBarManager;
    private LinearLayout dutyDetailLinearLayout;
    private RelativeLayout dutyDetailEditLinearLayout;
    private TextView dutyPlaceTextView;  //只看
    private TextView dutyLateListTextView; //只看
    private TextView dutyBreakRuleListTextView;  //只看
    private TextView dutyAccidentTextView; //只看
    private TextView dutyPlaceEditTextView;  //编辑
    private EditText dutyLateListEditText;   //编辑
    private EditText dutyBreakRuleListEditText;//编辑
    private EditText dutyAccidentEditText;//编辑
    private TextView dutySaveTextView;//编辑
    private TextView dutyRegisterEmpty; //空页面

    public static final int STATUS_HAVE_BEEN_SUBMIT_1 = 1;  //已提交
    public static final int STATUS_NOT_SUBMIT_0= 0;       //未提交
    private static final String DUTY_TIME_INFO = "dutyTimeInfo";
    private static final String DUTY_REGISTER_STATUS = "dutyRegisterStatus";
    private static final String DUTY_DETAIL_ID = "dutyDetailId";
    private static final String DUTY_DATE = "dutyDate";
    private DutyTimeInfo mDutyTimeInfo;
    private int mDutyDetailId;
    private String mDutyDate;
    private int mRegisterStatus;  //0.未提交 1.已提交
//    private boolean isLateListEmpty;
//    private boolean isBreakRuleEmpty;
//    private boolean isAccidentEmpty;

    @NonNull
    @Override
    public DutyRegisterDetialInfoPresenter createPresenter() {
        return new DutyRegisterDetialInfoPresenter();
    }

    public static void start(Context context, DutyTimeInfo dutyTimeInfo,int dutyDetailId,String date ,int registerStatus) {
        final Intent lIntent = new Intent(context, DutyRegisterDetialInfoActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(DUTY_TIME_INFO,dutyTimeInfo);
        bundle.putInt(DUTY_DETAIL_ID,dutyDetailId);
        bundle.putString(DUTY_DATE,date);
        bundle.putInt(DUTY_REGISTER_STATUS,registerStatus);
        lIntent.putExtras(bundle);
        if (!(context instanceof Activity)) {
            lIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(lIntent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_duty_register_detail_info);
        mDutyTimeInfo = (DutyTimeInfo) getIntent().getExtras().getSerializable(DUTY_TIME_INFO);
        mDutyDetailId = getIntent().getExtras().getInt(DUTY_DETAIL_ID,0);
        mDutyDate = getIntent().getExtras().getString(DUTY_DATE);
        mRegisterStatus  = getIntent().getExtras().getInt(DUTY_REGISTER_STATUS,0);
        initView();
        initData();
    }

    private void initView(){
        dutyRegisterEmpty = (TextView)findViewById(R.id.tv_emptyHint);
        //只看
        dutyDetailLinearLayout = (LinearLayout) findViewById(R.id.duty_detail_info);
        dutyPlaceTextView = (TextView)findViewById(R.id.tv_duty_please_input);
        dutyBreakRuleListTextView = (TextView)findViewById(R.id.tv_duty_check_late_list);
        dutyAccidentTextView = (TextView)findViewById(R.id.tv_duty_check_break_rule_list);
        dutyAccidentTextView = (TextView)findViewById(R.id.tv_duty_check_accident_list);
        //编辑
        dutyDetailEditLinearLayout = (RelativeLayout)findViewById(R.id.duty_detaail_info_edit);
        dutyPlaceEditTextView = (TextView)findViewById(R.id.tv_duty_please_input_edit);
        dutyLateListEditText = (EditText) findViewById(R.id.edit_text_late_list);
        dutyLateListEditText = (EditText) findViewById(R.id.edit_text_break_rule_list);
        dutyLateListEditText = (EditText) findViewById(R.id.edit_text_accident_list);
        dutySaveTextView = (TextView)findViewById(R.id.tv_save_register);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO 弹出是否保存的对话框，点击“退出”和“保存” 处理
                onBackPressed();
            }
        });
        mToolBarManager = new ToolBarManager(mToolbar, this);
        mToolBarManager.init();
        mToolBarManager.setTitle(mDutyTimeInfo.getTime());
    }

    private void initData(){
        dutyDetailLinearLayout.setVisibility(View.GONE);
        dutyDetailEditLinearLayout.setVisibility(View.GONE);
        dutyRegisterEmpty.setVisibility(View.VISIBLE);
        if(mRegisterStatus == STATUS_HAVE_BEEN_SUBMIT_1){
            dutyDetailLinearLayout.setVisibility(View.VISIBLE);
            dutyDetailEditLinearLayout.setVisibility(View.GONE);
            dutyRegisterEmpty.setVisibility(View.GONE);
        }else if(mRegisterStatus == STATUS_NOT_SUBMIT_0){
            dutyDetailLinearLayout.setVisibility(View.GONE);
            dutyDetailEditLinearLayout.setVisibility(View.VISIBLE);
            dutyRegisterEmpty.setVisibility(View.GONE);
            dutySaveTextView.setOnClickListener(this);
        }else {
            Toast.Short(this,"未值班的无法进行登记");
        }
        presenter.getRegisterDetailData(mDutyDetailId,mDutyDate);  // 获取服务器
    }


    /**
     * 回调后显示
     * @param registerDetailInfo
     */
    private void showData(DutyRegisterDetailInfo registerDetailInfo){
        if(mRegisterStatus == STATUS_HAVE_BEEN_SUBMIT_1){
            //查看
            if(!TextUtils.isEmpty(registerDetailInfo.getAddress())){
                dutyPlaceTextView.setText(registerDetailInfo.getAddress());
            }
            if(!TextUtils.isEmpty(registerDetailInfo.getLateStudent())){
                dutyLateListTextView.setText(registerDetailInfo.getLateStudent());
            }
            if(!TextUtils.isEmpty(registerDetailInfo.getIllegalList())){
                dutyBreakRuleListTextView.setText(registerDetailInfo.getIllegalList());
            }
            if(!TextUtils.isEmpty(registerDetailInfo.getAccident())){
                dutyAccidentTextView.setText(registerDetailInfo.getAccident());
            }
        }else if(mRegisterStatus == STATUS_NOT_SUBMIT_0){
            //编辑
            if(!TextUtils.isEmpty(registerDetailInfo.getAddress())){
                dutyPlaceEditTextView.setText(registerDetailInfo.getAddress());
            }
            if(!TextUtils.isEmpty(registerDetailInfo.getLateStudent())){
                dutyLateListEditText.setText(registerDetailInfo.getLateStudent());
            }
            if(!TextUtils.isEmpty(registerDetailInfo.getIllegalList())){
                dutyBreakRuleListEditText.setText(registerDetailInfo.getIllegalList());
            }
            if(!TextUtils.isEmpty(registerDetailInfo.getAccident())){
                dutyAccidentEditText.setText(registerDetailInfo.getAccident());
            }
            dutyLateListEditText.addTextChangedListener(textWatcherLateList);
            dutyBreakRuleListEditText.addTextChangedListener(textWatcherBreakRuleList);
            dutyAccidentEditText.addTextChangedListener(textWatcherAccident);
        }
    }

    TextWatcher textWatcherLateList = new TextWatcher() {
        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    TextWatcher textWatcherBreakRuleList = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    TextWatcher textWatcherAccident = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence s, int start, int before,
                                  int count) {
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_save_register:
                Toast.Short(this,"保存--待处理");
                break;
        }
    }

    @Override
    public void getRegisterDetailInfo(DutyRegisterDetailInfo registerDetailInfo) {
        showData(registerDetailInfo);
    }

    @Override
    public void showEmptyView() {
        dutyDetailLinearLayout.setVisibility(View.GONE);
        dutyDetailEditLinearLayout.setVisibility(View.GONE);
        dutyRegisterEmpty.setVisibility(View.VISIBLE);
        Toast.Short(this,"获取登记内容失败");
    }
}
