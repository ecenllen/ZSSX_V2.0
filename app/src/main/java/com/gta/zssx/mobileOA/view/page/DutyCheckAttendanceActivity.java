package com.gta.zssx.mobileOA.view.page;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gta.zssx.R;
import com.gta.zssx.mobileOA.model.bean.DutyRegisterOrCheckInfo;
import com.gta.zssx.mobileOA.presenter.DutyCheckAttendancePresenter;
import com.gta.zssx.mobileOA.view.DutyCheckAttendanceView;
import com.gta.zssx.mobileOA.view.adapter.DutyTimeCheckAdapter;
import com.gta.zssx.pub.base.BaseActivity;
import com.gta.zssx.pub.manager.ToolBarManager;
import com.gta.zssx.pub.util.LogUtil;

import java.util.List;

/**
 * Created by lan.zheng on 2016/11/21.
 */
public class DutyCheckAttendanceActivity extends BaseActivity<DutyCheckAttendanceView,DutyCheckAttendancePresenter>
        implements DutyCheckAttendanceView,View.OnClickListener{
    public Toolbar mToolbar;
    protected ToolBarManager mToolBarManager;
    private TextView mAllEmptyTextView;                   //全部为空,只有第一次网络有问题才显示
    private TextView mDutyDateTextView;                   //日期
    private TextView mDutyStatusTextView;                 //状态
    private RelativeLayout mRelativeLayoutCheck;          //值日检查所有内容页
    private RelativeLayout mRelativeLayoutCanNotCheck;    //无法检查，无网络或是未值日时显示该页面
    private TextView mTextViewGetDetailInfoFailed;        //拿不到要检查的登记数据时显示该页面

    private static final String DUTY_TYPE = "dutyType";
    private static final int DUTY_TYPE_CHECK_ATTENDANCE = 2;   //值日检查
//    private int mRegisterOrCheck;  //登记还是检查
    private boolean isCanCheck = true; //是否可以检查，用于考勤块显示用，不可考勤为false
    private boolean isFirstEnter = true;//第一次进入
    private int isCheckedOrNeverCheckBefore = 0;  //0：第一次检查 ，1：检查过
    public static final int REQUEST_CODE = 1000;       //回调结果
    public int registerStatus; //1.可登记 2.不可登记 0.未值班
    public static final int STATUS_CAN_1 = 1;
    public static final int STATUS_CAN_NOT_2 = 2;
    public static final int STATUS_NOT_COME_DUTY_0 = 0;
    public String dateString;  //日期显示

    //以下是检查相关
    private DutyTimeCheckAdapter mDutyTimeCheckAdapter;  //适配器
    private RecyclerView mRecyclerViewCheck;     //  值日检查
    private RadioGroup mCheckRadioGroup1;        //  第一栏
    private RadioGroup mCheckRadioGroup2;        //  第二栏
    private RadioGroup mCheckRadioGroup3;        //  第三栏
    private RadioGroup mCheckRadioGroup4;        //  第四栏
    private RadioButton mCheckRadioButton1_1;    //准到
    private RadioButton mCheckRadioButton1_2;    //迟到
    private RadioButton mCheckRadioButton1_3;    //中途离校
    private RadioButton mCheckRadioButton1_4;    //缺席
    private RadioButton mCheckRadioButton2_1;    //有
    private RadioButton mCheckRadioButton2_2;    //没有
    private RadioButton mCheckRadioButton3_1;    //优
    private RadioButton mCheckRadioButton3_2;    //良
    private RadioButton mCheckRadioButton3_3;    //中
    private RadioButton mCheckRadioButton3_4;    //差
    private RadioButton mCheckRadioButton4_1;    //优
    private RadioButton mCheckRadioButton4_2;    //良
    private RadioButton mCheckRadioButton4_3;    //中
    private RadioButton mCheckRadioButton4_4;    //差
    //    private TagFlowLayout mCheck1FlowLayout;    //考勤第一栏:准到、迟到、中途离校、缺席
//    private TagAdapter mCheck1TagAdapter;       //考勤第一栏适配器
    private TextView mCheckPlaceTextView;  //值班地点
    private TextView mCheckLateTextView; //迟到列表
    private TextView mCheckBreakRuleTextView; //违规列表
    private TextView mCheckAccidentTextView;//偶发事件
    private TextView mPreviousTextView;  //上一时段
    private TextView mNextTextView;      //下一时段
    private int mCheckLineOneValue = 1;  //准到 ,为9时表示全灰
    private int mCheckLineTwoValue = 1;  //有   ,为9时表示全灰
    private int mCheckLineThreeValue = 1; //优  ,为9时表示全灰
    private int mCheckLineFourValue = 1;  //优  ,为9时表示全灰
    private boolean isHaveNext = false;     //否有下一个,默认没有
    private boolean isHavePrevious = false; //是否有前一个，默认没有
    private boolean isEnableStep = false; //点击是否有效控制
    private int indicateTimePeriod = 0;  //默认第一时段
    private int indicateTimeChecked = 0;   //第一时段打钩

    @NonNull
    @Override
    public DutyCheckAttendancePresenter createPresenter() {
        return new DutyCheckAttendancePresenter();
    }

    public static void start(Context context) {
        final Intent lIntent = new Intent(context, DutyCheckAttendanceActivity.class);
        if (!(context instanceof Activity)) {
            lIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(lIntent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_duty_check_attendance);
        initView();
        initData();
    }

    private void initView(){
        mAllEmptyTextView = (TextView)findViewById(R.id.tv_all_empty_view) ;
        mRelativeLayoutCheck = (RelativeLayout)findViewById(R.id.layout_duty_check);
        mRelativeLayoutCanNotCheck = (RelativeLayout)findViewById(R.id.layout_duty_can_not_check);
        mTextViewGetDetailInfoFailed = (TextView)findViewById(R.id.tv_get_attendance_and_detail_fail);
        mDutyStatusTextView = (TextView)findViewById(R.id.tv_duty_status);
        mDutyDateTextView = (TextView) findViewById(R.id.tv_duty_date);
        mAllEmptyTextView.setVisibility(View.VISIBLE);  //默认显示暂无数据
        mDutyDateTextView.setOnClickListener(this);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setNavigationOnClickListener(view -> onBackPressed());
        mToolBarManager = new ToolBarManager(mToolbar, this);
        mToolBarManager.init();
        mToolBarManager.setRightText(getResources().getString(R.string.text_duty_table)).clickRightButton(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DutyTableInfoActivity.start(DutyCheckAttendanceActivity.this);
            }
        });
        //其他页面默认全都隐藏
        mRelativeLayoutCheck.setVisibility(View.GONE);
        mRelativeLayoutCanNotCheck.setVisibility(View.GONE);
    }

    private void initData(){
        //值日检查页面显示
        mToolBarManager.setTitle(getString(R.string.text_duty_check));
        initDutyCheckView(); //初始化界面元素指示器、考勤、登记详情　
//            presenter.getCheckTimeListData();  //TODO 去服务器获取真正的最近时间信息
        //TODO 测试数据--模拟获取数据成功第一个接口
        DutyRegisterOrCheckInfo dutyRegisterOrCheckInfo = presenter.testDataTime();
        if(isFirstEnter){  //把不相关的界面隐藏
            isFirstEnter = false;
            mAllEmptyTextView.setVisibility(View.GONE);
        }
        mRelativeLayoutCheck.setVisibility(View.VISIBLE);  //可以显示考勤页面
        dateString = presenter.getDate(dutyRegisterOrCheckInfo.getDate());
        mDutyDateTextView.setText(dateString);

        //==以下都是要根据日期选择改变的数据==
        showCheckDataDependOnDate(dutyRegisterOrCheckInfo.getTimeList());
    }

    private void showCheckDataDependOnDate(List<Object> list){
        //未来日期不可检查，不是未来日期则可以检查
        isCanCheck = !presenter.mIsFutureDate;
        LogUtil.Log("lenita","isCanCheck  ="+isCanCheck);
        //初始化时间段列表
        mDutyTimeCheckAdapter = new DutyTimeCheckAdapter(this,list,isCanCheck,isCheckedOrNeverCheckBefore);  //获取时间
        mRecyclerViewCheck.setAdapter(mDutyTimeCheckAdapter);
        if(!isCanCheck){   //若是未来的日期设置完日期后不需要往下执行
            //不给步骤开启
            isEnableStep = false;
            //全置灰
            setLineValueAllGray();
            //考勤栏赋值
            initRadioButtonCheckStatus();
            mRelativeLayoutCanNotCheck.setVisibility(View.VISIBLE);
            mDutyStatusTextView.setText(getResources().getString(R.string.text_duty_undo));
            return;
        }
        mRelativeLayoutCanNotCheck.setVisibility(View.GONE);

        //TODO 不是未来的日期就要去获取考勤和登记内容
//            presenter.getCheckAttendanceListData();  //考勤
//            presenter.getCheckDetailListData();   //登记内容
        //TODO 测试数据
        //步骤开启
        isEnableStep = true;
        //考勤栏赋值
        initRadioButtonCheckStatus();
        //登记信息显示
        initRegisterInfo();
        //根据时间段初始化标识
        initStepView(list.size());
    }

    //值日检查相关的参数
    private void initDutyCheckView(){
        //指示器
        mRecyclerViewCheck = (RecyclerView)findViewById(R.id.recycler_view_check_list) ;
        mRecyclerViewCheck.setLayoutManager(new LinearLayoutManager(this));
        //考勤信息和登记信息
        initDutyCheckAttendanceView();
        //上一时段下一时段
        mPreviousTextView = (TextView)findViewById(R.id.tv_step_previous);
        mNextTextView = (TextView)findViewById(R.id.tv_step_next);
        WindowManager manager = this.getWindowManager();
        DisplayMetrics outMetrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(outMetrics);
        int width = outMetrics.widthPixels/2-10;
        LinearLayout.LayoutParams layoutParams1 = (LinearLayout.LayoutParams) mPreviousTextView.getLayoutParams();
        layoutParams1.width = width;
        mPreviousTextView.setLayoutParams(layoutParams1);
        LinearLayout.LayoutParams layoutParams2 = (LinearLayout.LayoutParams) mNextTextView.getLayoutParams();
        layoutParams2.width = width;
        mNextTextView.setLayoutParams(layoutParams2);
        mPreviousTextView.setOnClickListener(this);
        mNextTextView.setOnClickListener(this);
        mPreviousTextView.setVisibility(View.INVISIBLE);
    }

    private void initDutyCheckAttendanceView(){
        mCheckRadioGroup1 = (RadioGroup) findViewById(R.id.rg_check1);
        mCheckRadioGroup2 = (RadioGroup) findViewById(R.id.rg_check2);
        mCheckRadioGroup3 = (RadioGroup) findViewById(R.id.rg_check3);
        mCheckRadioGroup4 = (RadioGroup) findViewById(R.id.rg_check4);
        mCheckRadioGroup1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                LogUtil.Log("lenita","check id1 = "+checkedId);
                switch (checkedId){
                    case R.id.rb_check1_1:
                        mCheckLineOneValue = 1;
                        break;
                    case R.id.rb_check1_2:
                        mCheckLineOneValue = 2;
                        break;
                    case R.id.rb_check1_3:
                        mCheckLineOneValue = 3;
                        break;
                    case R.id.rb_check1_4:
                        mCheckLineOneValue = 4;
                        break;
                }
            }
        });
        mCheckRadioGroup2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                LogUtil.Log("lenita","check id2 = "+checkedId);
                switch (checkedId){
                    case R.id.rb_check2_1:
                        mCheckLineTwoValue = 1;  //有
                        break;
                    case R.id.rb_check2_2:
                        mCheckLineTwoValue = 0; //没有
                        break;
                }
            }
        });
        mCheckRadioGroup3.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                LogUtil.Log("lenita","check id3 = "+checkedId);
                switch (checkedId){
                    case R.id.rb_check3_1:
                        mCheckLineOneValue = 1;
                        break;
                    case R.id.rb_check3_2:
                        mCheckLineOneValue = 2;
                        break;
                    case R.id.rb_check3_3:
                        mCheckLineOneValue = 3;
                        break;
                    case R.id.rb_check3_4:
                        mCheckLineOneValue = 4;
                        break;
                }
            }
        });
        mCheckRadioGroup4.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                LogUtil.Log("lenita","check id4 = "+checkedId);
                switch (checkedId){
                    case R.id.rb_check4_1:
                        mCheckLineOneValue = 1;
                        break;
                    case R.id.rb_check4_2:
                        mCheckLineOneValue = 2;
                        break;
                    case R.id.rb_check4_3:
                        mCheckLineOneValue = 3;
                        break;
                    case R.id.rb_check4_4:
                        mCheckLineOneValue = 4;
                        break;
                }
            }
        });
        //1
        mCheckRadioButton1_1 = (RadioButton)findViewById(R.id.rb_check1_1);
        mCheckRadioButton1_2 = (RadioButton)findViewById(R.id.rb_check1_2);
        mCheckRadioButton1_3 = (RadioButton)findViewById(R.id.rb_check1_3);
        mCheckRadioButton1_4 = (RadioButton)findViewById(R.id.rb_check1_4);
        //2
        mCheckRadioButton2_1 = (RadioButton)findViewById(R.id.rb_check2_1);
        mCheckRadioButton2_2 = (RadioButton)findViewById(R.id.rb_check2_2);
        //3
        mCheckRadioButton3_1 = (RadioButton)findViewById(R.id.rb_check3_1);
        mCheckRadioButton3_2 = (RadioButton)findViewById(R.id.rb_check3_2);
        mCheckRadioButton3_3 = (RadioButton)findViewById(R.id.rb_check3_3);
        mCheckRadioButton3_4 = (RadioButton)findViewById(R.id.rb_check3_4);
        //4
        mCheckRadioButton4_1 = (RadioButton)findViewById(R.id.rb_check4_1);
        mCheckRadioButton4_2 = (RadioButton)findViewById(R.id.rb_check4_2);
        mCheckRadioButton4_3 = (RadioButton)findViewById(R.id.rb_check4_3);
        mCheckRadioButton4_4 = (RadioButton)findViewById(R.id.rb_check4_4);
        //登记详情
        mCheckPlaceTextView =(TextView)findViewById(R.id.tv_duty_please_input) ;
        mCheckLateTextView =(TextView)findViewById(R.id.tv_duty_check_late_list) ;
        mCheckBreakRuleTextView =(TextView)findViewById(R.id.tv_duty_check_break_rule_list) ;
        mCheckAccidentTextView =(TextView)findViewById(R.id.tv_duty_check_accident_list) ;
    }

    @Override
    public void showAllEmptyPage() {
        if(isFirstEnter){
            mAllEmptyTextView.setVisibility(View.VISIBLE);
            return;
        }
        mDutyDateTextView.setText(dateString);//设置日期
        //检查显示空页面
        mRelativeLayoutCanNotCheck.setVisibility(View.VISIBLE);
        mRelativeLayoutCheck.setVisibility(View.INVISIBLE);
        mDutyStatusTextView.setText("");  //设置状态
    }

    @Override
    public void getCheckTimePeriodSuccess(DutyRegisterOrCheckInfo dutyRegisterOrCheckInfo, boolean isCanCheck) {
        if(isFirstEnter){
            isFirstEnter = false;
            mAllEmptyTextView.setVisibility(View.GONE);
        }
        //设置界面并且获取另外两个接口
    }

    private void setLineValue(List<Integer> list){
        mCheckLineOneValue = list.get(0);
        mCheckLineTwoValue = list.get(1);
        mCheckLineThreeValue = list.get(2);
        mCheckLineFourValue = list.get(3);
    }

    private void setLineValueAllGray(){
        mCheckLineOneValue = 9;
        mCheckLineTwoValue = 9;
        mCheckLineThreeValue = 9;
        mCheckLineFourValue = 9;
    }

    /**
     *考勤状态
     */
    private void initRadioButtonCheckStatus(){
        switch (mCheckLineOneValue){
            case 1:
                mCheckRadioButton1_1.setChecked(true);
                break;
            case 2:
                mCheckRadioButton1_2.setChecked(true);
                break;
            case 3:
                mCheckRadioButton1_3.setChecked(true);
                break;
            case 4:
                mCheckRadioButton1_4.setChecked(true);
                break;
            default:
                mCheckRadioButton1_1.setChecked(false);
                mCheckRadioButton1_2.setChecked(false);
                mCheckRadioButton1_3.setChecked(false);
                mCheckRadioButton1_4.setChecked(false);
                break;
        }
        switch (mCheckLineTwoValue){
            case 0:
                mCheckRadioButton2_2.setChecked(true);
                break;
            case 1:
                mCheckRadioButton2_1.setChecked(true);
                break;
            default:
                mCheckRadioButton2_1.setChecked(false);
                mCheckRadioButton2_2.setChecked(false);
                break;
        }
        switch (mCheckLineThreeValue){
            case 1:
                mCheckRadioButton3_1.setChecked(true);
                break;
            case 2:
                mCheckRadioButton3_2.setChecked(true);
                break;
            case 3:
                mCheckRadioButton3_3.setChecked(true);
                break;
            case 4:
                mCheckRadioButton3_4.setChecked(true);
                break;
            default:
                mCheckRadioButton3_1.setChecked(false);
                mCheckRadioButton3_2.setChecked(false);
                mCheckRadioButton3_3.setChecked(false);
                mCheckRadioButton3_4.setChecked(false);
                break;
        }
        switch (mCheckLineFourValue){
            case 1:
                mCheckRadioButton4_1.setChecked(true);
                break;
            case 2:
                mCheckRadioButton4_2.setChecked(true);
                break;
            case 3:
                mCheckRadioButton4_3.setChecked(true);
                break;
            case 4:
                mCheckRadioButton4_4.setChecked(true);
                break;
            default:
                mCheckRadioButton4_1.setChecked(false);
                mCheckRadioButton4_2.setChecked(false);
                mCheckRadioButton4_3.setChecked(false);
                mCheckRadioButton4_4.setChecked(false);
                break;
        }
    }

    /**
     *登记内容
     */
    private void initRegisterInfo(){
        //TODO 初始化登记内容
    }


    /**
     * 上一时段、下一时段
     * @param count
     */
    private void initStepView(int count){
        isHavePrevious = false;
        if(count == 1){
            isHaveNext = false;
        }else {
            isHaveNext = true;
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_duty_date:
                Intent intent = new Intent(this,DutyDateSelectActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt(DUTY_TYPE,DUTY_TYPE_CHECK_ATTENDANCE);
                intent.putExtras(bundle);
                startActivityForResult(intent,REQUEST_CODE);
                break;
            case R.id.tv_step_previous:
                if(!isEnableStep){  //不允许操作
                    return;
                }
                if(!isHavePrevious)
                    return;  //没有上一个时不需要监听

                indicateTimePeriod--;  //标粗时段
                mDutyTimeCheckAdapter.changeCurrentTimePeriod(indicateTimePeriod);
                if(isCanCheck){
                    //设置进入前时段的参数
                    presenter.setCheckListInfo(indicateTimePeriod+1,mCheckLineOneValue,mCheckLineTwoValue,mCheckLineThreeValue,mCheckLineFourValue);
                    setLineValue(presenter.getCheckListInfo(indicateTimePeriod) );//TODO 拿到列表要赋值,赋值后才能设置勾选
                    initRadioButtonCheckStatus();  //根据值改变勾选状态
                }
                break;
            case R.id.tv_step_next:
                if(!isEnableStep){  //不允许操作
                    return;
                }
                if(!isCanCheck && !isHaveNext)
                    return;  //不可保存且没有下一个不需要监听
                indicateTimePeriod++;  //标粗时段
                mDutyTimeCheckAdapter.changeCurrentTimePeriod(indicateTimePeriod);
                indicateTimeChecked++;  //打钩
                mDutyTimeCheckAdapter.changeChecked(indicateTimeChecked); //只有从未登记过才会改变打钩
                if(isCanCheck){
                    //设置进入前时段的参数
                    presenter.setCheckListInfo(indicateTimePeriod-1,mCheckLineOneValue,mCheckLineTwoValue,mCheckLineThreeValue,mCheckLineFourValue);
                    setLineValue(presenter.getCheckListInfo(indicateTimePeriod));   //TODO 拿到列表要赋值,赋值之后再做勾选
                    initRadioButtonCheckStatus();//根据值改变勾选状态
                }
                break;
        }
    }

        /*private void initRadioButtonCheck(boolean isCan){
        mCheckRadioButton1_1.setClickable(isCan);
        mCheckRadioButton1_2.setClickable(isCan);
        mCheckRadioButton1_3.setClickable(isCan);
        mCheckRadioButton1_4.setClickable(isCan);
        mCheckRadioButton2_1.setClickable(isCan);
        mCheckRadioButton2_2.setClickable(isCan);
        mCheckRadioButton3_1.setClickable(isCan);
        mCheckRadioButton3_2.setClickable(isCan);
        mCheckRadioButton3_3.setClickable(isCan);
        mCheckRadioButton3_4.setClickable(isCan);
        mCheckRadioButton4_1.setClickable(isCan);
        mCheckRadioButton4_2.setClickable(isCan);
        mCheckRadioButton4_3.setClickable(isCan);
        mCheckRadioButton4_4.setClickable(isCan);
    }*/
}
