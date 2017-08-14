package com.gta.zssx.patrolclass.wheelview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.aigestudio.wheelpicker.WheelPicker;
import com.gta.zssx.R;
import com.gta.zssx.pub.InterfaceList;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Created by liang.lu1 on 2016/7/18.
 */
public class DatePicker extends LinearLayout implements WheelPicker.OnWheelChangeListener {

    private Context context;
    private int currentYear;
    private int currentMonth;
    private int currentDay;

    public DatePicker(Context context) {
        this(context, null);
    }

    public DatePicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    /**
     * 获取选择的年
     *
     * @return
     */
    public String getYear() {
        return mWheelYear.getData().get(mWheelYear.getCurrentItemPosition()).toString();
    }

    /**
     * 获取选择的月
     *
     * @return
     */
    public String getMonth() {
        return mWheelMonth.getData().get(mWheelMonth.getCurrentItemPosition()).toString();
    }

    /**
     * 获取选择的日
     *
     * @return
     */
    public String getDay() {
        return mWheelDay.getData().get(mWheelDay.getCurrentItemPosition()).toString();
    }

    private WheelPicker mWheelYear;
    private WheelPicker mWheelMonth;
    private WheelPicker mWheelDay;

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        LayoutInflater.from(getContext()).inflate(R.layout.date_picker, this);

        mWheelYear = (WheelPicker) findViewById(R.id.year);
        mWheelMonth = (WheelPicker) findViewById(R.id.month);
        mWheelDay = (WheelPicker) findViewById(R.id.day);


        // 格式化当前时间，并转换为年月日整型数据
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String[] split = sdf.format(new Date(System.currentTimeMillis())).split("-");

        currentYear = Integer.parseInt(split[0]);
        currentMonth = Integer.parseInt(split[1]);
        currentDay = Integer.parseInt(split[2]);

        mWheelYear.setData(getYearData(currentYear));
        mWheelYear.setSelectedItemPosition(0);
        mWheelYear.setVisibleItemCount(5);
        mWheelYear.setItemTextColor(getResources().getColor(R.color.gray_999999));
        mWheelYear.setSelectedItemTextColor(getResources().getColor(R.color.black_999999));


        mWheelMonth.setData(getMonthData(currentMonth));
        mWheelMonth.setVisibleItemCount(5);
        mWheelMonth.setSelectedItemPosition(currentMonth - 1);
        mWheelMonth.setItemTextColor(getResources().getColor(R.color.gray_999999));
        mWheelMonth.setSelectedItemTextColor(getResources().getColor(R.color.black_999999));


        mWheelDay.setData(getDayData(currentDay));
        mWheelDay.setVisibleItemCount(5);
        mWheelDay.setSelectedItemPosition(currentDay - 1);
        mWheelDay.setItemTextColor(getResources().getColor(R.color.gray_999999));
        mWheelDay.setSelectedItemTextColor(getResources().getColor(R.color.black_999999));


        mWheelMonth.setOnWheelChangeListener(this);
        mWheelYear.setOnWheelChangeListener(this);
        mWheelDay.setOnWheelChangeListener(new WheelPicker.OnWheelChangeListener() {
            @Override
            public void onWheelScrolled(int offset) {

            }

            @Override
            public void onWheelSelected(int position) {
                dateTimeSelectListener.timeSelect();
            }

            @Override
            public void onWheelScrollStateChanged(int state) {

            }
        });

    }

    /**
     * 设置当前选择的年月日
     */
    public void setCurrentSelected(int year, int month, int day) {
        mWheelYear.setSelectedItemPosition(currentYear - year);
        mWheelMonth.setData(getMonthData(month));
        mWheelMonth.setSelectedItemPosition(month - 1);
        mWheelDay.setData(getDayData(day));
        mWheelDay.setSelectedItemPosition(day - 1);
        onWheelSelected(0);
    }

    /**
     * 年范围在：1900~今年
     *
     * @param currentYear
     * @return
     */
    private ArrayList<String> getYearData(int currentYear) {
        ArrayList<String> list = new ArrayList<>();
        for (int i = currentYear; i >= 1900; i--) {
            list.add(String.valueOf(i));
        }
        return list;
    }

    private ArrayList<String> getMonthData() {
        ArrayList<String> list = new ArrayList<>();
        for (int i = 1; i <= 12; i++) {
            list.add(String.valueOf(i));
        }
        return list;
    }

    private ArrayList<String> getMonthData(int currentMonth) {
        ArrayList<String> list = new ArrayList<>();
        for (int i = 1; i <= currentMonth; i++) {
            list.add(String.valueOf(i));
        }
        return list;
    }

    /**
     * 日范围在1~lastDay
     *
     * @param lastDay
     * @return
     */
    private ArrayList<String> getDayData(int lastDay) {
        //ignore condition
        ArrayList<String> list = new ArrayList<>();
        for (int i = 1; i <= lastDay; i++) {
            list.add(String.valueOf(i));
        }
        return list;
    }

    /**
     * 判断是否闰年
     *
     * @param year
     * @return
     */
    private boolean isLeapYear(int year) {
        return (year % 100 == 0 && year % 400 == 0) || (year % 100 != 0 && year % 4 == 0);
    }

    /**
     * 获取特定年月对应的天数
     *
     * @param year
     * @param month
     * @return
     */
    public int getLastDay(int year, int month) {
        if (month == 2) {
            // 2月闰年的话返回29，防止28
            return isLeapYear(year) ? 29 : 28;
        }
        // 一三五七八十腊，三十一天永不差
        return month == 1 || month == 3 || month == 5 || month == 7 || month == 8 || month == 10 || month == 12 ? 31 : 30;
    }


    private DateTimeSelectListener dateTimeSelectListener;

    public void setDateTimeSelectListener(DateTimeSelectListener dateTimeSelectListener) {
        this.dateTimeSelectListener = dateTimeSelectListener;
    }

    @Override
    public void onWheelScrolled(int offset) {

    }

    @Override
    public void onWheelSelected(int position) {
        try {
            //记录当前选择的月数
            int selectMonth = Integer.parseInt(getMonth().trim());
            //记录当前选择的年
            int selectYear = Integer.parseInt(getYear().trim());
            // 根据当前选择的年月获取对应的天数
            int lastDay = getLastDay(selectYear, selectMonth);
            //判断是选中的年是否为今年
            if (selectYear == currentYear) {
                //设置月数
                ArrayList<String> monthData = getMonthData(currentMonth);
                mWheelMonth.setData(monthData);
                if (selectMonth >= currentMonth) {
                    mWheelMonth.setSelectedItemPosition(monthData.size() - 1);
                    lastDay = currentDay;
                } else {
                    mWheelMonth.setSelectedItemPosition(selectMonth - 1);
                }
            } else {
                ArrayList<String> monthData = getMonthData();
                mWheelMonth.setData(monthData);
            }
            // 记录当前选择的天数
            int selectDay = Integer.parseInt(getDay().trim());
            // 设置天数
            ArrayList<String> dayData = getDayData(lastDay);
            mWheelDay.setData(dayData);
            // 如果选中的天数大于实际天数，那么将默认天数设为实际天数;否则还是设置默认天数为选中的天数
            if (selectDay > lastDay) {
                mWheelDay.setSelectedItemPosition(dayData.size() - 1);
            } else {
                mWheelDay.setSelectedItemPosition(selectDay - 1);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        dateTimeSelectListener.timeSelect();
    }

    @Override
    public void onWheelScrollStateChanged(int state) {

    }

    public interface DateTimeSelectListener {
        void timeSelect();
    }
}
