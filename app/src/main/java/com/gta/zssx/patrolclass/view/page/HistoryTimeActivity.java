package com.gta.zssx.patrolclass.view.page;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.gta.zssx.AppConfiguration;
import com.gta.zssx.R;
import com.gta.zssx.patrolclass.wheelview.DatePicker;
import com.gta.zssx.pub.base.BaseCommonActivity;

/**
 * Created by liang.lu1 on 2016/7/18.
 * 时间段巡课记录搜索页面
 */
public class HistoryTimeActivity extends BaseCommonActivity implements DatePicker.DateTimeSelectListener {
    private TextView TvStartTime;
    private TextView TvEndTime;
    private DatePicker mPickerOne, mPickerTwo;
    private int startYear, startMonth, startDay, endYear, endMonth, endDay;
    boolean isFromOA = false;

    @Override
    public int getLayoutId () {
        return R.layout.activity_history_time;
    }

    @Override
    protected void initData () {
        super.initData ();
        isFromOA = getIntent ().getBooleanExtra ("isFromOA", false);
    }

    @Override
    protected void initView () {
        mPickerOne = (DatePicker) findViewById (R.id.birthday_picker);
        mPickerTwo = (DatePicker) findViewById (R.id.birthday_picker2);
        TvEndTime = (TextView) findViewById (R.id.tv_end_time);
        TvStartTime = (TextView) findViewById (R.id.tv_start_time);

        initToolBar ();
        mPickerOne.setDateTimeSelectListener (this);
        mPickerTwo.setDateTimeSelectListener (this);

        TvStartTime.setText (mPickerOne.getYear () + "年" + mPickerOne.getMonth () + "月" + mPickerOne.getDay () + "日");
        TvEndTime.setText (mPickerTwo.getYear () + "年" + mPickerTwo.getMonth () + "月" + mPickerTwo.getDay () + "日");
    }

    private void initToolBar () {
        //初始化标题栏
        mToolBarManager.showBack (true)
                .setTitle (getResources ().getString (R.string.history))
                .showRightButton (true)
                .setRightText ("完成")
                .clickRightButton (v -> {
                    //点击完成查询记录
                    if (isEndTimeBig ()) {
                        if (isFromOA) {
                            Intent intent = new Intent ();
                            intent.putExtra ("startDate", startYear + "-" + startMonth + "-" + startDay);
                            intent.putExtra ("endDate", endYear + "-" + endMonth + "-" + endDay);
                            setResult (RESULT_OK, intent);
                        } else {
                            AppConfiguration.getInstance ().finishActivity (SearchResultActivity.class);
                            //                                PatrolClassActivity.start (HistoryTimeActivity.this);
                            SearchResultActivity.start (HistoryTimeActivity.this, startYear + "-" + startMonth + "-" + startDay, endYear + "-" + endMonth + "-" + endDay);
                        }
                        finish ();
                    } else {
                        ToastUtils.showShortToast ("查询开始日期不能大于查询结束日期，请重新进行选择！");
                        //                            Toast.makeText (HistoryTimeActivity.this, "查询开始日期不能大于查询结束日期，请重新进行选择！", Toast.LENGTH_SHORT).show ();
                    }
                });
    }

    public static void start (Context context) {
        final Intent lIntent = new Intent (context, HistoryTimeActivity.class);
        if (!(context instanceof Activity)) {
            lIntent.setFlags (Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity (lIntent);
    }

    private boolean isEndTimeBig () {
        startYear = Integer.parseInt (mPickerOne.getYear ().trim ());
        startMonth = Integer.parseInt (mPickerOne.getMonth ().trim ());
        startDay = Integer.parseInt (mPickerOne.getDay ().trim ());

        endYear = Integer.parseInt (mPickerTwo.getYear ().trim ());
        endMonth = Integer.parseInt (mPickerTwo.getMonth ().trim ());
        endDay = Integer.parseInt (mPickerTwo.getDay ().trim ());

        return endYear > startYear || endYear == startYear && endMonth > startMonth || endYear == startYear && endMonth >= startMonth && endDay >= startDay;
    }

    @Override
    public void timeSelect () {
        mHandler.sendEmptyMessage (1);
    }

    private Handler mHandler = new Handler () {
        @Override
        public void handleMessage (Message msg) {
            super.handleMessage (msg);
            TvStartTime.setText (mPickerOne.getYear () + "年" + mPickerOne.getMonth () + "月" + mPickerOne.getDay () + "日");
            TvEndTime.setText (mPickerTwo.getYear () + "年" + mPickerTwo.getMonth () + "月" + mPickerTwo.getDay () + "日");
        }
    };

}
