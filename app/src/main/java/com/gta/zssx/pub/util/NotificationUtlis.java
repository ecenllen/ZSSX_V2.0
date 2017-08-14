package com.gta.zssx.pub.util;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.gta.zssx.R;
import com.gta.zssx.ZSSXApplication;
import com.gta.zssx.mobileOA.model.bean.HomeInfo;
import com.gta.zssx.mobileOA.model.bean.Schedule;
import com.gta.zssx.mobileOA.view.page.SchedulePlanActivity;
import com.gta.zssx.pub.common.Constant;

import java.text.ParseException;
import java.text.SimpleDateFormat;

@SuppressLint("SimpleDateFormat")
public class NotificationUtlis {

	private static NotificationManager mNotiManager =(NotificationManager) ZSSXApplication.instance.
			getSystemService(Context.NOTIFICATION_SERVICE);

	public static void showNotification(Context context, HomeInfo homeInfo) {
		if (!isShowNotice(context)) {
			return;
		}		
//		showMailNotification(context, homeInfo); // 邮件
//		showMeetingNotification(context, homeInfo); // 会议
//		showNoticeNotification(context, homeInfo); // 公文公告
//		showTasksNotification(context, homeInfo); // 待办
		showScheduleNotification(context, homeInfo);
	}

	/* 邮件的 */
	/*public static void showMailNotification(Context context, HomeInfo homeInfo) {
		int nums = 0;
		try {
			nums = Integer.parseInt(homeInfo.getMail());
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (nums <= 0)
			return;

		long preId = PreferencesUtils.getLong(context, Constant.MailId, 0);
		long curId = homeInfo.getMailId();
		if (preId >= curId)
			return;
		PreferencesUtils.putLong(context, Constant.MailId, curId);

		Notification mailNoti = new Notification();
		mailNoti.icon = R.drawable.app_icon;
		String showText = "未读邮件" + nums + "条";
		mailNoti.tickerText = showText;
		setNotiFiCation(context, mailNoti); // 设置通知是否震动和响铃

		// 点击通知跳转
		Intent notificationIntent = new Intent(context,
				MailMainNewActivity.class);
		notificationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
				| Intent.FLAG_ACTIVITY_SINGLE_TOP);
		PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
				notificationIntent, 0);
		mailNoti.setLatestEventInfo(context, "移动OA", showText, contentIntent);
		mailNoti.flags = Notification.FLAG_AUTO_CANCEL; // 点击通知，自动清除
	
		mNotiManager.notify(0, mailNoti);
	}*/

	/* 待办已办的 */
	/*public static void showTasksNotification(Context context, HomeInfo homeInfo) {
		int nums = 0;
		try {
			nums = Integer.parseInt(homeInfo.getTasks());
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (nums <= 0)
			return;

		long preId = PreferencesUtils.getLong(context, Constant.TasksId, 0);
		long curId = homeInfo.getTasksId();
		if (preId >= curId)
			return;
		PreferencesUtils.putLong(context, Constant.TasksId, curId);

		Notification tasksNoti = new Notification();
		tasksNoti.icon = R.drawable.app_icon;
		String showText = "待办事宜" + nums + "条";
		tasksNoti.tickerText = showText;
		setNotiFiCation(context, tasksNoti);// 设置通知是否震动和响铃
		// 点击通知跳转
		Intent notificationIntent = new Intent(context, TaskMainActivity.class);
		notificationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
				| Intent.FLAG_ACTIVITY_SINGLE_TOP);
		PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
				notificationIntent, 0);
		tasksNoti.setLatestEventInfo(context, "移动OA", showText, contentIntent);
		tasksNoti.flags = Notification.FLAG_AUTO_CANCEL; // 点击通知，自动清除
		mNotiManager.notify(1, tasksNoti);
	}
*/
	/* 公文公告的 */
	/*public static void showNoticeNotification(Context context, HomeInfo homeInfo) {
		int nums = 0;
		try {
			nums = Integer.parseInt(homeInfo.getNotice());
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (nums <= 0)
			return;

		long preId = PreferencesUtils.getLong(context, Constant.NoticeId, 0);
		long curId = homeInfo.getNoticeId();
		if (preId >= curId)
			return;
		PreferencesUtils.putLong(context, Constant.NoticeId, curId);

		Notification noticeNoti = new Notification();
		noticeNoti.icon = R.drawable.app_icon;
		String showText = "未读公文公告" + nums + "条";
		noticeNoti.tickerText = showText;
		setNotiFiCation(context, noticeNoti);// 设置通知是否震动和响铃
		// 点击通知跳转
		Intent notificationIntent = new Intent(context,
				OfficialNoticeMainActivity.class);
		notificationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
				| Intent.FLAG_ACTIVITY_SINGLE_TOP);
		PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
				notificationIntent, 0);
		noticeNoti.setLatestEventInfo(context, "移动OA", showText, contentIntent);
		noticeNoti.flags = Notification.FLAG_AUTO_CANCEL; // 点击通知，自动清除
	
		mNotiManager.notify(2, noticeNoti);
	}*/

	/* 会议的 */
	/*public static void showMeetingNotification(Context context,
			HomeInfo homeInfo) {
		int nums = 0;
		try {
			nums = Integer.parseInt(homeInfo.getMeeting());
		} catch (Exception e) {
			e.printStackTrace();
		}

		
		if (nums <= 0)
			return;

		long preId = PreferencesUtils.getLong(context, Constant.MeetingId, 0);
		long curId = homeInfo.getMeetingId();
		
		long PreRecordId = PreferencesUtils.getLong(context, Constant.RecordId, 0);
		long curRecordId = homeInfo.getRecordId();
		
		if (preId >= curId && PreRecordId>=curRecordId)
			return;
		PreferencesUtils.putLong(context, Constant.MeetingId, curId);
		PreferencesUtils.putLong(context, Constant.RecordId, curRecordId);
		
		Notification meeting = new Notification();
		meeting.icon = R.drawable.app_icon;
		String showText = "未读会议" + nums + "条";
		meeting.tickerText = showText;
		setNotiFiCation(context, meeting);// 设置通知是否震动和响铃
		// 点击通知跳转
		Intent notificationIntent = new Intent(context,
				MeetingMainActivity.class);
		notificationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
				| Intent.FLAG_ACTIVITY_SINGLE_TOP);
		PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
				notificationIntent, 0);
		meeting.setLatestEventInfo(context, "移动OA", showText, contentIntent);
		meeting.flags = Notification.FLAG_AUTO_CANCEL; // 点击通知，自动清除
		
		mNotiManager.notify(3, meeting);
	}*/

	/* 日程的 */
	public static void showScheduleNotification(Context context,
			HomeInfo homeInfo) {
		int nums = 0;
		try {
			nums = Integer.parseInt(homeInfo.getSchedule());
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (nums <= 0)
			return;

		long preId= PreferencesUtils.getLong(context, Constant.ScheduleId, 0);
		String ScheduleTime = homeInfo.getSchUpdateTime();
		if(ScheduleTime == null || ScheduleTime.equals("")){
			return;
		}
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
		long curId;
		try {
			curId = sdf.parse(ScheduleTime).getTime();
		} catch (ParseException e) {
			e.printStackTrace();
			return ;
		}
		if (preId >= curId)return;
		PreferencesUtils.putLong(context, Constant.ScheduleId, curId);

		//		Schedule.icon = R.drawable.app_icon;
//		String showText = "待处理日程" + nums + "条";
//		Schedule.tickerText = showText;
//		setNotiFiCation(context, Schedule);// 设置通知是否震动和响铃
		// 点击通知跳转
//		Intent notificationIntent = new Intent(context, SchedulePlanActivity.class);
//		notificationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
//				| Intent.FLAG_ACTIVITY_SINGLE_TOP);
//		PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
//				notificationIntent, 0);
//		Schedule.setLatestEventInfo(context, "移动OA", showText, contentIntent);
//		Schedule.flags = Notification.FLAG_AUTO_CANCEL; // 点击通知，自动清除

		Notification.Builder Schedule = new Notification.Builder(context);
		Schedule.setSmallIcon(R.drawable.app_icon);
		Schedule.setContentTitle(context.getString(R.string.app_name));  //标题
		String showText = "待处理日程" + nums + "条";
		Schedule.setContentText(showText);  //内容
		Schedule.setTicker(showText);
		Schedule.setAutoCancel(true);
		Intent notificationIntent = new Intent(context, SchedulePlanActivity.class);
		notificationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
				| Intent.FLAG_ACTIVITY_SINGLE_TOP);
		PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
				notificationIntent, 0);
		Schedule.setContentIntent(contentIntent);
		Notification nt = null;
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
			nt = Schedule.build();  //高于16
		}else {
			nt = Schedule.getNotification();
		}
		setNotiFiCation(context, nt);// 设置通知是否震动和响铃
		mNotiManager.notify(4, nt); //低于16
	}

	/* 根据设置提示 */
	private static void setNotiFiCation(Context context,
			Notification notification) {

//		ZSSXApplication app = (ZSSXApplication) context.getApplicationContext();
//		String ringString = app.getProperty(Constant.PROP_KEY_SWITCHER_RING);
//		String shakeString = app.getProperty(Constant.PROP_KEY_SWITCHER_SHAKE);
//		boolean isRing = Boolean.parseBoolean(ringString);
//		boolean isShake = Boolean.parseBoolean(shakeString);

		//如今默认都有
		boolean isRing = true;
		boolean isShake = true;
		/* 有震动有响铃 */
		if (isRing && isShake) {
			notification.defaults = Notification.DEFAULT_SOUND
					| Notification.DEFAULT_VIBRATE;
			return;
		}

		/* 有响铃没有震动 */
		if (isRing && !isShake) {
			notification.defaults = Notification.DEFAULT_SOUND;
			return;
		}

		/* 有震动没有响铃 */
		if (!isRing && isShake) {
			notification.defaults = Notification.DEFAULT_VIBRATE;
			return;
		}
	}

	public static boolean isShowNotice(Context context) {
		ZSSXApplication app = (ZSSXApplication) context.getApplicationContext();
		String noticeString = app
				.getProperty(Constant.PROP_KEY_SWITCHER_NOTICE);
		return Boolean.parseBoolean(noticeString);
	}

	public static void showScheduleNotification(Context context, Schedule schedule, int num) {

//		Notification noti = new Notification();
//		noti.icon = R.drawable.app_icon;
//		String content = schedule.getScheduleContent();
//		String startT = schedule.getStartTime().replace("T", " ");
//		startT = startT.replace(":00", "");
//		String endT = schedule.getEndTime().replace("T", " ");
//		endT = endT.replace(":00", "");
//
//		String showText = startT + "至" + endT + "   " + content;

//		noti.tickerText = showText;
//		noti.defaults = Notification.DEFAULT_SOUND
//				| Notification.DEFAULT_VIBRATE;// 有震动有响铃
		// 点击通知跳转
//		Intent notificationIntent = new Intent(context, SchedulePlanActivity.class);
//		notificationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
//				| Intent.FLAG_ACTIVITY_SINGLE_TOP);
//		PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
//				notificationIntent, 0);
//		noti.setLatestEventInfo(context, "移动OA", showText, contentIntent);
//		noti.flags = Notification.FLAG_AUTO_CANCEL; // 点击通知，自动清除
//		mNotiManager.notify(num, noti);

		Notification.Builder Schedule = new Notification.Builder(context);
		Schedule.setSmallIcon(R.drawable.app_icon);
		String content = schedule.getScheduleContent();
		String startT = schedule.getStartTime().replace("T", " ");
		startT = startT.replace(":00", "");
		String endT = schedule.getEndTime().replace("T", " ");
		endT = endT.replace(":00", "");

		String showText = startT + "至" + endT + "   " + content;
		Schedule.setContentTitle(context.getString(R.string.app_name));
		Schedule.setContentText(showText);
		Schedule.setTicker(showText);
		Schedule.setAutoCancel(true);
		Intent notificationIntent = new Intent(context, SchedulePlanActivity.class);
		notificationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
				| Intent.FLAG_ACTIVITY_SINGLE_TOP);
		PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
				notificationIntent, 0);
		Schedule.setContentIntent(contentIntent);
		Notification nt = null;
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
			nt = Schedule.build();  //高于16
		}else {
			nt = Schedule.getNotification();
		}
		setNotiFiCation(context, nt);// 设置通知是否震动和响铃
		mNotiManager.notify(num, nt);
	}

	public static void clearAllNotify() {
		mNotiManager.cancelAll();
	}
}
