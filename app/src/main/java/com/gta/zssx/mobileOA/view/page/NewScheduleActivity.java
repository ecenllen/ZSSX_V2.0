package com.gta.zssx.mobileOA.view.page;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.bigkoo.pickerview.TimePickerView;
import com.gta.utils.resource.Toast;
import com.gta.zssx.R;
import com.gta.zssx.ZSSXApplication;
import com.gta.zssx.mobileOA.model.bean.Schedule;
import com.gta.zssx.mobileOA.presenter.NewSchedulePresenter;
import com.gta.zssx.mobileOA.view.NewScheduleView;
import com.gta.zssx.pub.base.BaseActivity;
import com.gta.zssx.pub.manager.ToolBarManager;
import com.gta.zssx.pub.util.LogUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by lan.zheng on 2016/10/17.
 */
public class NewScheduleActivity extends BaseActivity<NewScheduleView,NewSchedulePresenter>
        implements NewScheduleView, View.OnClickListener {
    private RadioButton mWorkRadioButton;
    private RadioButton mPersonRadioButton;
    private TextView mBeginTimeTextView;
    private TextView mEndTimeTextView;
    private TextView mRingTimeTextView;
    private TextView mRemindTextView;
    private EditText mContentEditText;
    private TextView mDeteleTextView;
    private int mRemind = 5; // 将选择的赋值给mRmind 提醒周期(1:单次提醒,5:不提醒,6:工作日提醒,2:每日提醒,3:每周提醒,4:每月提醒)
    private int selectPosition = 5; //默认是第五个Item“不提醒”
    private int mCheckState = 1;// 默认是工作事务
    private int id_1 =-1;  //用于开始
    private int id_2 =-1;  //用于真正的数据
    public final static int REQUEST_CODE_POSITION = 100;
    private final static int TYPE_SERVER_REMIND = 1;
    private final static int TYPE_ITEM_POSITION = 2;
    String[] list=new String[]{"单次提醒","工作日提醒","每日提醒","每周提醒","每月提醒","不提醒"};
    public Toolbar mToolbar;
    protected ToolBarManager mToolBarManager;
    private SimpleDateFormat sdf;
    private long beginTimeInMillis;
    private long endTimeInMillis;
    private boolean isCanSave = true;  //若服务器获取失败，无论怎样都不能被修改
//    public boolean isBeginBelowEnd = false;  //开始时间小于结束时间,一开始是非，因为时间是一样的
    private String title;

    @NonNull
    @Override
    public NewSchedulePresenter createPresenter() {
        return new NewSchedulePresenter();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_schedule);
        String format = "yyyy-MM-dd HH:mm";
        sdf = new SimpleDateFormat(format,Locale.CANADA);
        initView();
        initData();
    }

    private void initView(){
        mWorkRadioButton = (RadioButton) findViewById(R.id.schedule_new_rb1);
        mPersonRadioButton = (RadioButton)findViewById(R.id.schedule_new_rb2);
        mBeginTimeTextView = (TextView)findViewById(R.id.schedule_new_starttime_tv);
        mEndTimeTextView  = (TextView)findViewById(R.id.schedule_new_endtime_tv);
        mRingTimeTextView = (TextView)findViewById(R.id.schedule_new_spinner);
        mRemindTextView = (TextView)findViewById(R.id.schedule_new_remaining_tv);
        mContentEditText = (EditText)findViewById(R.id.schedule_new_content_et);
        mWorkRadioButton.setOnClickListener(this);
        mPersonRadioButton.setOnClickListener(this);
        mBeginTimeTextView.setOnClickListener(this);
        mEndTimeTextView.setOnClickListener(this);
        mRingTimeTextView.setOnClickListener(this);
        mDeteleTextView = (TextView)findViewById(R.id.tv_delete_schedule);
        mDeteleTextView.setOnClickListener(this);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setNavigationOnClickListener(view -> onBackPressed());
        mToolBarManager = new ToolBarManager(mToolbar, this);
        mToolBarManager.init();

        //默认的选中
        mWorkRadioButton.setChecked(true);
        mPersonRadioButton.setChecked(false);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
        String beginTime = df.format(new Date());
        String endTime = df.format(new Date());
        mBeginTimeTextView.setText(beginTime);
        mEndTimeTextView.setText(endTime);
        beginTimeInMillis = getTimeMillis(beginTime);
        endTimeInMillis = getTimeMillis(endTime);

        TextWatcher textWatcher = new TextWatcher() {

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
                int length = mContentEditText.getText().toString().length();
                int remainWords = 50-length;
                mRemindTextView.setText("您还可以输入"+remainWords+"个字");
                if(!isCanSave){
                    return;  //只要是无法保存都返回
                }
                if(remainWords < 50) {
                    mToolBarManager.getRightButton().setEnabled(true);
                }else {
                    mToolBarManager.getRightButton().setEnabled(false);
                }

            }
        };
        mContentEditText.addTextChangedListener(textWatcher);
    }

    private void initData(){
        id_1 = getIntent().getExtras().getInt("ScheduleId", -2);
        Schedule schedule = (Schedule) getIntent().getExtras().getSerializable("Schedule");
        mDeteleTextView.setVisibility(View.GONE);  //隐藏删除按钮
        title = getResources().getString(R.string.text_new_schedule);  //默认是“新建日程”
        LogUtil.Log("lenita","编辑"+id_1+" "+schedule);
        //从编辑进入
        if (id_1 > 0) {
            //标题显示为“编辑日程”，如果这个Id有传过来，证明获取服务器失败了，右边保存按钮置灰,删除按钮置隐藏
            Toast.Short(this,getResources().getString(R.string.text_get_single_schedule_failed));
            title = getResources().getString(R.string.text_edit_schedule);
            isCanSave = false;  //无法保存，传入的是Id不为负数证明获取信息失败了
            mToolBarManager.setTitle(title).getRightButton().setEnabled(false);
        }
        if (schedule != null) {
            //获取到详情的情况显示删除按钮
            title = getResources().getString(R.string.text_edit_schedule);
            id_2 = schedule.getId();
            bindViews(schedule);
            mToolBarManager.setTitle(title).getRightButton().setEnabled(true);
            mDeteleTextView.setVisibility(View.VISIBLE);  //可删除的显示删除按钮
        }
        //从新建进入
        if(id_1 < 0 && schedule == null){
            //证明不是编辑，去获取服务器时间，新建日程
            mToolBarManager.setTitle(title).getRightButton().setEnabled(false);
            presenter.getServerTime();
        }


        mToolBarManager.setTitle(title)
                .setRightText(R.string.finish).clickRightButton(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isCanSave){
                    mToolBarManager.getRightButton().setEnabled(false);
                    return;  //只要是不能保存的都返回
                }
                String start= mBeginTimeTextView.getText().toString();
                String end= mEndTimeTextView.getText().toString();
                 //再次判断时间,如果开始不小于结束时间，则需要重新选择
                if(!presenter.compareDate(start, end)){
                    Toast.Long(NewScheduleActivity.this,getString(R.string.text_starttime_need_below_endtime));
                    return;
                }
                if (! presenter.checkRemindValidity(start,end,mRemind)) {
                    Toast.Long(NewScheduleActivity.this,getString(R.string.text_cycle_must_in_same_day));
                    return;
                }
                scheduleAddOrEditFinish();
            }
        });

    }

    private void scheduleAddOrEditFinish(){
        Schedule schedule = new Schedule();
        schedule.setId(id_2);  //日程id(当id = -1时,新增日程)
        String beginTime = mBeginTimeTextView.getText().toString()+":00";
        String endTime = mEndTimeTextView.getText().toString()+":00";
        schedule.setStartTime(beginTime);
        schedule.setEndTime(endTime);
        schedule.setScheduleType(mCheckState);
        schedule.setRemind(mRemind);
        String content = mContentEditText.getText().toString().trim();
        schedule.setScheduleContent(content);
        presenter.scheduleAddOrUpdate(schedule);  //新增或修改日程
    }


    private void bindViews(Schedule s) {
        String startTime = s.getStartTime();
        String endTime = s.getEndTime();

        String[] arr1 = startTime.split(" ");
        if (arr1 != null && arr1.length == 2) {
            mBeginTimeTextView.setText(arr1[0]+" "+presenter.makeTime(arr1[1]));
        }
        String[] arr2 = endTime.split(" ");
        if (arr2 != null && arr2.length == 2) {
            mEndTimeTextView.setText(arr2[0]+" "+presenter.makeTime(arr2[1]));
        }

        int type = s.getScheduleType();
        mCheckState = type;
        if (type == 1) {
            mWorkRadioButton.setChecked(true);
            mPersonRadioButton.setChecked(false);
        } else if (type == 2) {
            mWorkRadioButton.setChecked(false);
            mPersonRadioButton.setChecked(true);
        }

        String content = s.getScheduleContent();
        mContentEditText.setText(content);

        setRemindTextView(s.getRemind(),TYPE_SERVER_REMIND);  //设置选中的提醒项
    }

    private void setRemindTextView(int r,int type){
        if(type == TYPE_SERVER_REMIND){  //服务器真正存的remind值
            mRemind = r;
            switch (r) {
                case 1:
                    selectPosition = 0;
                    mRingTimeTextView.setText(list[0]);
                    break;
                case 2:
                    selectPosition = 2;
                    mRingTimeTextView.setText(list[2]);
                    break;
                case 3:
                    selectPosition = 3;
                    mRingTimeTextView.setText(list[3]);
                    break;
                case 4:
                    selectPosition =4;
                    mRingTimeTextView.setText(list[4]);
                    break;
                case 5:
                    selectPosition = 5;
                    mRingTimeTextView.setText(list[5]);
                    break;
                case 6:
                    selectPosition = 1;
                    mRingTimeTextView.setText(list[1]);
                    break;
                default:
                    break;
            }
        }else {  //从选择来的位置
            selectPosition = r;
            mRingTimeTextView.setText(list[r]);
            switch (r){
                case 1:
                    mRemind = 6;
                    break;
                case 2:
                    mRemind = 2;
                    break;
                case 3:
                    mRemind = 3;
                    break;
                case 4:
                    mRemind = 4;
                    break;
                case 5:
                    mRemind = 5;
                    break;
                case 0:
                    mRemind = 1;
                    break;
                default:
                    break;
            }
        }

    }

    @Override
    public void getServerTime(String date, int year, int month, int day, int hour, int min) {
        if(!TextUtils.isEmpty(date)){
            beginTimeInMillis = getTimeMillis(date);
            endTimeInMillis = getTimeMillis(date);
        }
        mBeginTimeTextView.setText(date);
        mEndTimeTextView.setText(date);
    }

    @Override
    public void deleteLocalSchedulesSuccess() {
        Toast.Short(this,"删除日程成功");
        finish();
    }

    @Override
    public void saveScheduleDataSuccess() {
        if(title.equals(R.string.text_edit_schedule)){
            //如果是修改，首先修改本地集合的通知状态，将已通知过更改为未通知过。
            ZSSXApplication.instance.updateTodaySche(id_2, false);
        }
        //成功提示
        Toast.Short(this,title+"成功");
        //返回上一界面
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.schedule_new_rb1:
                mCheckState = 1;
                break;
            case R.id.schedule_new_rb2:
                mCheckState = 2;
                break;
            case R.id.schedule_new_starttime_tv:
                //弹出日期选择框
                hideSoftKeyboard();
                selectBeginTime();
                break;
            case R.id.schedule_new_endtime_tv:
                //弹出日期选择框
                hideSoftKeyboard();
                selectEndTime();
                break;
            case R.id.schedule_new_spinner:
                //TODO 改变选择提醒方式
                Intent intent = new Intent(this,ScheduleRemindSelectActivity.class);
                intent.putExtra("selectPosition",selectPosition);
                intent.putExtra("list",list);
                startActivityForResult(intent,REQUEST_CODE_POSITION);
                break;
            case R.id.tv_delete_schedule:
                //TODO 确认是否删除弹框
                presenter.popupConfirmDialog(this, "是否确认删除该日程？", new NewSchedulePresenter.DeleteListener() {
                    @Override
                    public void sureDelete() {
                        presenter.deleteSchedule(id_2);  //删除日程
                    }
                });
                break;
        }
    }

    //收起键盘
    protected void hideSoftKeyboard()
    {
        if (this.getCurrentFocus() != null)
        {
            InputMethodManager inputMethodManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(),0);
        }
    }

    public TimePickerView mBeginTimePickerView;
    private String beginTime;
    private void selectBeginTime() {
        mBeginTimePickerView = new TimePickerView(mActivity, TimePickerView.Type.ALL);
        beginTime = mBeginTimeTextView.getText().toString();
        Date lDate = null;
        try {
            lDate = sdf.parse(beginTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        mBeginTimePickerView.setTime(lDate);
        mBeginTimePickerView.setCyclic(true);
        mBeginTimePickerView.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date) {
                if (date.getTime() > endTimeInMillis) {
//                    Toast.makeText(getActivity(), "查询开始日期不能大于结束日期,请重新进行选择", Toast.LENGTH_SHORT).show();
                    Toast.Long(mActivity, mActivity.getResources().getString(R.string.text_date_error_begin));
                    return;
                }
                beginTime = sdf.format(date);
                mBeginTimeTextView.setText(beginTime);
                //拿到时间毫秒
                beginTimeInMillis = getTimeMillis(beginTime);
            }
        });
        mBeginTimePickerView.show();
    }

    public TimePickerView mEndTimePickerView;
    private String endTime;
    private void selectEndTime() {
        mEndTimePickerView = new TimePickerView(mActivity, TimePickerView.Type.ALL);
        endTime = mEndTimeTextView.getText().toString();
        Date lDate2 = null;
        try {
            lDate2 = sdf.parse(endTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        mEndTimePickerView.setTime(lDate2);
        mEndTimePickerView.setCyclic(true);
        mEndTimePickerView.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date) {
                if (date.getTime() < beginTimeInMillis) {
//                    Toast.makeText(getActivity(), "查询结束日期不能小于开始日期,请重新进行选择！", Toast.LENGTH_SHORT).show();
                    Toast.Long(mActivity, mActivity.getResources().getString(R.string.text_date_error_end));
                    return;
                }
                endTime = sdf.format(date);
                mEndTimeTextView.setText(endTime);
                //拿到时间毫秒
                endTimeInMillis = getTimeMillis(endTime);
            }
        });
        mEndTimePickerView.show();
    }

    private long getTimeMillis(String date) {
        long TimeInMillis = 0;
        SimpleDateFormat lSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm",Locale.CHINA);
        try {
            TimeInMillis = lSimpleDateFormat.parse(date).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return TimeInMillis;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE_POSITION && resultCode == RESULT_OK){
            int position = data.getIntExtra("positionSelect",5);
            LogUtil.Log("lenita","onActivityResult ="+position);
            setRemindTextView(position,TYPE_ITEM_POSITION);
        }
    }
}