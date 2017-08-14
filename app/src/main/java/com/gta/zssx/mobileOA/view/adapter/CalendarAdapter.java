package com.gta.zssx.mobileOA.view.adapter;

import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.gta.zssx.R;
import com.gta.zssx.mobileOA.model.bean.CalendarBox;
import com.gta.zssx.mobileOA.view.base.BaseOAActivity;
import com.gta.zssx.mobileOA.view.page.SchedulePlanActivity;
import com.gta.zssx.mobileOA.view.page.SchedulePlanFragment;
import com.gta.zssx.pub.util.LogUtil;
import com.gta.zssx.pub.util.LunarCalendarUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class CalendarAdapter extends BaseAdapter {
    //这里精确到具体Activity，因为有操作是共同的
	private SchedulePlanActivity mActivitySchedulePlan;   //用于日程为Activity的情况mActivitySchedulePlan.currentPressedPositon = position;mActivitySchedulePlan.isFilp
	//下面用于日程为Fragment的情况
	private BaseOAActivity mActivity;
	private SchedulePlanFragment mFragment;  //mFragment.currentPressedPositon = position;mFragment.isFilp
	private boolean isActivity = true;

	private int today = 0;
	private int thisMonth = 0;
	private int thisYear = 0;

	private ArrayList<Integer> finishedStartDays;
	private ArrayList<Integer> unfinishedStartDays;
	
	private int todayPosition;

	/** 某月的总天数 */
	private int daysOfMonth = 0;
	/** 某月第一天为星期几 */
	private int dayOfWeek = 0;
	/** 上一个月的总天数 */
	private int lastDaysOfMonth = 0;

	private String[] dayNumber = new String[42]; // 控制多少GridView多少行，至少5行，共5*7=35个格子
	private int[] monthNumberChina = new int[42];//农历
	private int[] dayNumberChina  = new int[42]; //农历
	private CalendarBox sc = null;

	private int currentFlag = -1; // 用于标记当天
	private int pressedPosition = -1;  //点击位置
	private int todayMonth = -1;  //本月为-1，其他月为0

	private String showYear = ""; // 用于在头部显示的年份
	private String showMonth = ""; // 用于在头部显示的月份

	// 系统当前时间
	private String sys_year = "";
	private String sys_month = "";
	private String sys_day = "";

	public CalendarAdapter() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-d",	Locale.CHINA);
		String sysDate = sdf.format(new Date()); // 当期日期
		sys_year = sysDate.split("-")[0];
		sys_month = sysDate.split("-")[1];
		sys_day = sysDate.split("-")[2];
	}

	/**
	 * 用于Activity
	 * @param activity
	 * @param jumpMonth
	 * @param jumpYear
	 * @param currentYear
	 * @param currentMonth
     * @param today
     */
	public CalendarAdapter(SchedulePlanActivity activity, int jumpMonth,
						   int jumpYear, int currentYear, int currentMonth, int today) {
		this();
		this.mActivitySchedulePlan = activity;
		sc = new CalendarBox();
		//手机系统时间有可能是错误的，这里使用服务器返回的时间
		this.sys_year = String.valueOf(currentYear);
		this.sys_month = String.valueOf(currentMonth);
		this.sys_day = String.valueOf(today);
		this.today = today;
		this.thisMonth = currentMonth;
		this.thisYear = currentYear;
		setData(jumpMonth, jumpYear, currentYear, currentMonth);

	}

	/**
	 * 用于Fragment
	 * @param activity
	 * @param fragment
	 * @param jumpMonth
	 * @param jumpYear
	 * @param currentYear
	 * @param currentMonth
     * @param today
     */
	public CalendarAdapter(BaseOAActivity activity,SchedulePlanFragment fragment, int jumpMonth,
						   int jumpYear, int currentYear, int currentMonth, int today) {
		this();
		isActivity = false;
		this.mActivity = activity;
		this.mFragment = fragment;
		sc = new CalendarBox();
		//手机系统时间有可能是错误的，这里使用服务器返回的时间
		this.sys_year = String.valueOf(currentYear);
		this.sys_month = String.valueOf(currentMonth);
		this.sys_day = String.valueOf(today);
		this.today = today;
		this.thisMonth = currentMonth;
		this.thisYear = currentYear;
		setData(jumpMonth, jumpYear, currentYear, currentMonth);

	}


	public void setScheduleData(ArrayList<Integer> finished, ArrayList<Integer> unfinishedStart) {

		if (null != finished) {
			this.finishedStartDays.clear();
			this.finishedStartDays = finished;
		} else {
			this.finishedStartDays = new ArrayList<Integer>();
		}

		if (null != unfinishedStart) {		
			this.unfinishedStartDays = unfinishedStart;
		} else {
			this.unfinishedStartDays = new ArrayList<Integer>();
		}
	}
	


	public void setData(int jumpMonth, int jumpYear, int currentYear,
			int currentMonth) {
		currentFlag = -1;
		pressedPosition = -1;
		todayMonth = -1;
		initCalendar(jumpMonth, jumpYear, currentYear, currentMonth);
	}

	/**
	 * 初始化目标日历
	 * 
	 * @param jumpMonth
	 * @param jumpYear
	 * @param currentYear
	 * @param currentMonth
	 */
	private void initCalendar(int jumpMonth, int jumpYear, int currentYear,
			int currentMonth) {
		int stepYear = currentYear + jumpYear;
		int stepMonth = currentMonth + jumpMonth;
		if (stepMonth > 0) {
			// 往下一个月滑动
			if (stepMonth % 12 == 0) {
				stepYear = currentYear + stepMonth / 12 - 1;
				stepMonth = 12;
			} else {
				stepYear = currentYear + stepMonth / 12;
				stepMonth = stepMonth % 12;
			}
		} else {
			// 往上一个月滑动
			stepYear = currentYear - 1 + stepMonth / 12;
			stepMonth = stepMonth % 12 + 12;
			if (stepMonth % 12 == 0) {
			}
		}
		// (jumpMonth为滑动的次数，每滑动一次就增加一月或减一月)
		String targetYear = String.valueOf(stepYear);
		String targetMonth = String.valueOf(stepMonth);
		int willShowYear = stepYear;
		int willShowMonth = stepMonth;
		if(willShowYear == thisYear && willShowMonth == thisMonth){
			todayMonth = -1;
		}else {
			todayMonth = 0;
		}
		getCalendar(Integer.parseInt(targetYear), Integer.parseInt(targetMonth));
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return dayNumber.length;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final View view;
		final ViewHolder holder;
		if (null == convertView) {
			if(isActivity){
				view = mActivitySchedulePlan.getLayoutInflater().inflate(R.layout.item_schedule_gridview, parent, false);
			}else {
				view = mActivity.getLayoutInflater().inflate(R.layout.item_schedule_gridview, parent, false);
			}
			holder = new ViewHolder();
			assert view != null;
			holder.mContainerFL = (FrameLayout) view
					.findViewById(R.id.schedule_item_container);
			holder.mDateTV = (TextView) view
					.findViewById(R.id.schedule_date_tv);
			holder.mColorTV = (TextView) view
					.findViewById(R.id.schedule_bgcolor_tv);
			holder.mBackground = (ImageView)view
					.findViewById(R.id.schedule_date_iv);
			holder.mDateLunarTV = (TextView)view.findViewById(R.id.schedule_date_tv_lunar);
			holder.mDateLunarMonthView = view.findViewById(R.id.schedule_date_lunar_month);
			view.setTag(holder);
		} else {
			// 可复用
			view = convertView;
			holder = (ViewHolder) view.getTag();
		}

		bindView(holder, position);

		return view;

	}

	private void bindView(ViewHolder holder, int position) {
		String d = dayNumber[position];

		SpannableString sp = new SpannableString(d);
		sp.setSpan(new StyleSpan(android.graphics.Typeface.NORMAL), 0,
				d.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		sp.setSpan(new RelativeSizeSpan(1.2f), 0, d.length(),
				Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

		String d2 = "";  //用于判断是否显示显示农历
		holder.mDateLunarMonthView.setVisibility(View.INVISIBLE);//农历月份标示
		if(dayNumberChina[position] != -1){
			if(dayNumberChina[position] == 0){
				//特殊处理
				holder.mDateLunarTV.setVisibility(View.INVISIBLE);
			}else {
				d2 = changeToChinaDay(monthNumberChina[position],dayNumberChina[position]);
				LogUtil.Log("lenita2","d2 = "+d2+" todayMonth = "+todayMonth);
				holder.mDateLunarTV.setVisibility(View.VISIBLE);
				if (showMonthFlag){
					holder.mDateLunarMonthView.setVisibility(View.VISIBLE);
				}else {
					holder.mDateLunarMonthView.setVisibility(View.INVISIBLE);
				}
			}
		}else {
			holder.mDateLunarTV.setVisibility(View.GONE);
		}
		holder.mDateLunarTV.setText(d2);
		holder.mDateLunarTV.setTextColor(Color.parseColor("#C6C6C6"));
		holder.mDateTV.setText(sp);// 设置日历数字
		holder.mDateTV.setTextColor(Color.parseColor("#C6C6C6"));
		holder.mColorTV.setBackgroundResource(0);//0 to remove the background.
		holder.mColorTV.setVisibility(View.GONE);

		/**
		 * 设置本月的颜色、背景等。
		 */
		if (position < daysOfMonth + dayOfWeek && position >= dayOfWeek) {
			int currentDay = getDateByClickItem(position);
			
			
			// 针对已完成的日程赋予颜色
			if (finishedStartDays.contains(getDateByClickItem(position))) {
				holder.mColorTV.setVisibility(View.VISIBLE);
				holder.mColorTV.setBackgroundResource(R.drawable.schedule_c2);
			}

			// 针对未完成的日程，根据过去和将来赋予不同的颜色
			int currentMonth = cutOutZero(getShowMonth());
			int currentYear = cutOutZero(getShowYear());
	
			if (unfinishedStartDays.contains(currentDay)) {

				if (currentYear == thisYear) {
					if (currentMonth == thisMonth) {
						/** 核心比较开始 */
						holder.mColorTV.setVisibility(View.VISIBLE);
						if (currentDay < today) {
							// 以前未完成 红色
							holder.mColorTV	.setBackgroundResource(R.drawable.schedule_c1);
						} else if (today <= currentDay) {// 今日如果有未完成的，也属于将来未完成
							// 将来未完成 蓝色
							holder.mColorTV	.setBackgroundResource(R.drawable.schedule_c3);
						}
						/** 核心比较结束 */
					} else if (currentMonth < thisMonth) {
						
						
						// 当前月在本月之前，过去，一律显示红色
						drawRed(holder);
						
						
						
					} else if (thisMonth < currentMonth) {
						
						
						// 当前月在本月之后，将来一律显示蓝色
						drawBlue(holder);
						
						
					}
				} else if (currentYear < thisYear) {
					// 当前年在今年之前，过去，一律显示红色
					drawRed(holder);
				} else if (thisYear < currentYear) {
					// 当前年在今年之后，将来，一律显示蓝色
					drawBlue(holder);
				}

			}

			// holder.mDateTV.setText(sp);// 仅设置本月的日历数字，非本月无数字
			holder.mDateLunarTV.setTextColor(Color.BLACK);
			holder.mDateTV.setTextColor(Color.BLACK);// 设置本月的数字的字体颜色
			/*
			 * 如果该天是周末
			 */
			if (position % 7 == 0 || position % 7 == 6) {

			}
		}


		
		//设置被点击的颜色
		if (position == pressedPosition) {
//			holder.mContainerFL	.setBackgroundResource(R.drawable.schedule_calendar_select_two);
			holder.mBackground.setImageDrawable(mActivity.getResources().getDrawable(R.drawable.schedule_today_select));
			holder.mDateLunarTV.setTextColor(Color.WHITE);
			holder.mDateTV.setTextColor(Color.WHITE);
			mFragment.currentPressedPositon = position;
		}else {
			/**
			 * 今日背景
			 */
			//非本月的日期哪怕是同一天也不能显示今天的灰色背景
			if(todayMonth == -1 && getDateByClickItem(position) == today && position < daysOfMonth + dayOfWeek && position >= dayOfWeek){
				holder.mBackground.setImageDrawable(mActivity.getResources().getDrawable(R.drawable.ic_today_background));
				holder.mDateLunarTV.setTextColor(Color.WHITE);
				holder.mDateTV.setTextColor(Color.WHITE);
			}else {
				holder.mBackground.setImageDrawable(mActivity.getResources().getDrawable(R.drawable.schedule_calendar_bg));
			}
//			holder.mContainerFL	.setBackgroundResource(R.drawable.schedule_white_bg_box);
//			holder.mDateTV.setTextColor(Color.BLACK);
		}
		
		
		// 设置当天的背景
		if (currentFlag == position) {
			todayPosition = position;  //当天的选中
//			holder.mContainerFL	.setBackgroundResource(R.drawable.schedule_calendar_select_two);
			holder.mBackground.setImageDrawable(mActivity.getResources().getDrawable(R.drawable.schedule_today_select));
			holder.mDateLunarTV.setTextColor(Color.WHITE);
			holder.mDateTV.setTextColor(Color.WHITE);
			mFragment.currentPressedPositon=position;
		} else {
			// 滑动到别的月份，设置选中第一天
			if(mFragment.isFilp){
				if(dayOfWeek == position){
//					holder.mContainerFL	.setBackgroundResource(R.drawable.schedule_calendar_select_two);
					holder.mBackground.setImageDrawable(mActivity.getResources().getDrawable(R.drawable.schedule_today_select));
					holder.mDateLunarTV.setTextColor(Color.WHITE);
					holder.mDateTV.setTextColor(Color.WHITE);
				}else{
					//非本月的日期哪怕是同一天也不能显示今天的灰色背景
					if(todayMonth == -1 && getDateByClickItem(position) == today && position < daysOfMonth + dayOfWeek && position >= dayOfWeek){
						holder.mBackground.setImageDrawable(mActivity.getResources().getDrawable(R.drawable.ic_today_background));
						holder.mDateLunarTV.setTextColor(Color.WHITE);
						holder.mDateTV.setTextColor(Color.WHITE);
					}else {
						holder.mBackground.setImageDrawable(mActivity.getResources().getDrawable(R.drawable.schedule_calendar_bg));
//						holder.mContainerFL	.setBackgroundResource(R.drawable.schedule_white_bg_box);
						holder.mDateLunarTV.setTextColor(Color.BLACK);
						holder.mDateTV.setTextColor(Color.BLACK);
					}

					if (position >=  daysOfMonth + dayOfWeek || position < dayOfWeek){
						holder.mDateLunarTV.setTextColor(Color.parseColor("#C6C6C6"));
						holder.mDateTV.setTextColor(Color.parseColor("#C6C6C6"));
					}
				}
			}else {
				//当非滑动的时候要还原值，选中的是“今天”,其他曾经选择值还原
				if (position != pressedPosition){
					//非本月的日期哪怕是同一天也不能显示今天的灰色背景
					if(todayMonth == -1 && getDateByClickItem(position) == today && position < daysOfMonth + dayOfWeek && position >= dayOfWeek){
						holder.mBackground.setImageDrawable(mActivity.getResources().getDrawable(R.drawable.ic_today_background));
						holder.mDateLunarTV.setTextColor(Color.WHITE);
						holder.mDateTV.setTextColor(Color.WHITE);
					}else {
						holder.mBackground.setImageDrawable(mActivity.getResources().getDrawable(R.drawable.schedule_calendar_bg));
					}
//					holder.mContainerFL	.setBackgroundResource(R.drawable.schedule_white_bg_box);
					if(dayOfWeek == position){
						holder.mDateLunarTV.setTextColor(Color.BLACK);
						holder.mDateTV.setTextColor(Color.BLACK);
					}
				}

			}
		}
	}


	public void setPressedPosition(int positon) {
		pressedPosition = positon;
		currentFlag = -1;
	}

	private void drawBlue(ViewHolder holder) {
		holder.mColorTV.setVisibility(View.VISIBLE);
		// 一般设置为将来未完成
		holder.mColorTV.setBackgroundResource(R.drawable.schedule_c3);
	}

	private void drawRed(ViewHolder holder) {
		holder.mColorTV.setVisibility(View.VISIBLE);
		holder.mColorTV.setBackgroundResource(R.drawable.schedule_c1);
	}

	// 得到某年的某月的天数且这月的第一天是星期几
	public void getCalendar(int year, int month) {
		/* 是否为闰年 */
		boolean isLeapyear = sc.isLeapYear(year);
		daysOfMonth = sc.getDaysOfMonth(isLeapyear, month);
		dayOfWeek = sc.getWeekdayOfMonth(year, month);
		lastDaysOfMonth = sc.getDaysOfMonth(isLeapyear, month - 1);
		getweek(year, month);
	}

	SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
	// 将一个月中的每一天的值添加入数组dayNumber中
	private void getweek(int year, int month) {
		int j = 1;
		// 得到当前月的所有日程日期(这些日期需要标记)

		for (int i = 0; i < dayNumber.length; i++) {
			int mYear = year;  //用于农历
			int mMonth = month;  //用于农历
			int mDay = 1;
			// 周一
			if (i < dayOfWeek) { // 前一个月
				if(month == 1){
					mYear = year-1;
					mMonth = 12;
				}else{
					mMonth = month-1;
				}
				int temp = lastDaysOfMonth - dayOfWeek + 1;
				dayNumber[i] = (temp + i) + "";
				mDay = temp + i;
			} else if (i < daysOfMonth + dayOfWeek) { // 本月
				String day = String.valueOf(i - dayOfWeek + 1); // 得到的日期

				dayNumber[i] = i - dayOfWeek + 1 + "";
				mDay = i - dayOfWeek + 1;
				// 对于当前月才去标记当前日期
				if (sys_year.equals(String.valueOf(year))
						&& sys_month.equals(String.valueOf(month))
						&& sys_day.equals(day)) {
					// 标记当前日期
					currentFlag = i;
				}
				setShowYear(String.valueOf(year));
				setShowMonth(String.valueOf(month));
			} else { // 下一个月
				if(month == 12){
					mYear = year+1;
					mMonth = 1;
				}else {
					mMonth = month+1;
				}
				dayNumber[i] = j + "";
				mDay = j;
				j++;
			}
			dayNumberChina[i] = -1;
			monthNumberChina[i] = -1;
			LogUtil.Log("lenita2","chinaDay = "+mYear+" "+mMonth+" "+mDay);
			//最后存一份农历时间
			if(mYear < 2100 ){
				int chinaDay[] = LunarCalendarUtil.solarToLunar(mYear,mMonth,mDay);
				if(chinaDay.length > 0 ){
					monthNumberChina[i] = chinaDay[1];
					dayNumberChina[i] = chinaDay[2];
				}
			}
			//如果是2100年1月的日历，2099的最后那几天也不用显示农历
			if(year == 2100 && mYear == 2099 ){
				dayNumberChina[i] = -1;
				monthNumberChina[i] = -1;
			}
			//如果是2099年12月的日历，2100年最后几天不用显示农历,但是注意，为了布局统一，日期要上移
			if(year == 2099 && mYear == 2100){
				dayNumberChina[i] = 0;
				monthNumberChina[i] = 0;
			}
		}
	}

	boolean showMonthFlag = false;
	private String changeToChinaDay(int month,int day) {
        String chinaDay = "";
		showMonthFlag = false;
		if(day == 1){
			showMonthFlag = true;
			switch (month){
				case 1:
					chinaDay = "正月";
					break;
				case 2:
					chinaDay = "二月";
					break;
				case 3:
					chinaDay = "三月";
					break;
				case 4:
					chinaDay = "四月";
					break;
				case 5:
					chinaDay = "五月";
					break;
				case 6:
					chinaDay = "六月";
					break;
				case 7:
					chinaDay = "七月";
					break;
				case 8:
					chinaDay = "八月";
					break;
				case 9:
					chinaDay = "九月";
					break;
				case 10:
					chinaDay = "十月";
					break;
				case 11:
					chinaDay = "冬月";
					break;
				case 12:
					chinaDay = "腊月";
					break;
			}
		}else {
			if(day == 20){
				chinaDay = "二十";
			}else if(day == 30){
				chinaDay = "三十";
			}else {
				int change = day;
				if(day < 11){
					change = day;
					chinaDay = "初";
				}else if(day > 10 && day < 20){
					change = day-10;
					chinaDay ="十";
				}else if(day > 20 && day < 30){
					change = day-20;
					chinaDay = "廿";
				}else {
					chinaDay = "三一";
				}
				switch (change){
					case 1:
						chinaDay += "一";
						break;
					case 2:
						chinaDay += "二";
						break;
					case 3:
						chinaDay += "三";
						break;
					case 4:
						chinaDay += "四";
						break;
					case 5:
						chinaDay += "五";
						break;
					case 6:
						chinaDay += "六";
						break;
					case 7:
						chinaDay += "七";
						break;
					case 8:
						chinaDay += "八";
						break;
					case 9:
						chinaDay += "九";
						break;
					case 10:
						chinaDay += "十";
						break;
				}
			}

		}
		return chinaDay;
	}
	
	public int getTodayPosition(){
		return todayPosition;
	}
	/**
	 * 获取本月的开始位置
	 * @return
	 */
	public int getMonthStartPosition(){
		return dayOfWeek;
	}
	/**
	 * 获取本月的结束位置
	 * @return
	 */
	public int getMonthEndPosition(){
		return daysOfMonth + dayOfWeek;
	}
	
	/**
	 * 点击每一个item时返回item中的日期
	 * 
	 * @param position
	 * @return
	 */
	public int getDateByClickItem(int position) {
		return Integer.parseInt((dayNumber[position]));
	}

	public int getMonthFirstDay(){
		return dayOfWeek;
	}
	/**
	 * 在点击gridView时，得到这个月中第一天的位置
	 * 
	 * @return
	 */
	public int getStartPositon() {
		return dayOfWeek + 7;
	}

	/**
	 * 在点击gridView时，得到这个月中最后一天的位置
	 * 
	 * @return
	 */
	public int getEndPosition() {
		return (dayOfWeek + daysOfMonth + 7) - 1;
	}

	public String getShowYear() {
		return showYear;
	}

	public void setShowYear(String showYear) {
		this.showYear = showYear;
	}

	public String getShowMonth() {
		return showMonth;
	}

	public void setShowMonth(String showMonth) {
		this.showMonth = showMonth;
	}

	/**
	 * 根据年月日获取日，且不是"01"格式
	 * 
	 * @param string
	 */
	private int cutOutZero(String string) {
		if (string.startsWith("0") && string.length() == 2) {
			string = string.substring(1, string.length());
		}
		return Integer.parseInt(string);
	}

	class ViewHolder {
		FrameLayout mContainerFL;
		TextView mDateTV;
		TextView mColorTV;
		ImageView mBackground;
		TextView mDateLunarTV;  //农历
		View mDateLunarMonthView; //农历月份第一天
//		ImageView mTodayBackground;  //今天的灰色默认标记
	}
}
