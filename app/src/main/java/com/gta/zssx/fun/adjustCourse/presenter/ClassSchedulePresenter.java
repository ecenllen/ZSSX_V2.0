package com.gta.zssx.fun.adjustCourse.presenter;

import android.content.Context;
import android.support.v7.app.AlertDialog;

import com.gta.utils.mvp.BasePresenter;
import com.gta.zssx.fun.adjustCourse.model.AdjustDataManager;
import com.gta.zssx.fun.adjustCourse.model.bean.HasTimeScheduleBean;
import com.gta.zssx.fun.adjustCourse.view.ClassScheduleView;

import org.joda.time.DateTime;

import java.util.Calendar;
import java.util.Date;

import rx.Subscriber;

/**
 * [Description]
 * <p>
 * [How to use]
 * <p>
 * [Tips]
 *
 * @author Created by Zhimin.Huang on 2017/3/13.
 * @since 1.0.0
 */
public class ClassSchedulePresenter extends BasePresenter<ClassScheduleView> {

    public void getSchedule(String semesterId, String teachId, String adjustDate,
                            String classId, int roomParam, int roomId,
                            int courseType) {
        AdjustDataManager.getHasTimeSchedule(semesterId, teachId, adjustDate, classId, roomParam, roomId, courseType)
                .subscribe(new Subscriber<HasTimeScheduleBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(HasTimeScheduleBean hasTimeScheduleBean) {
                        if (getView() != null) {
                            getView().getSchedule(hasTimeScheduleBean);
                        }
                    }
                });
    }

    /**
     * @param date 日期
     *             计算某一天所在星期的全部日期
     */
    public void getWeekDay(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        //判断要计算的日期是否是周日，如果是则减一天计算周六的，否则会出问题，计算到下一周去了
        int dayWeek = cal.get(Calendar.DAY_OF_WEEK);//获得当前日期是一个星期的第几天
        if (1 == dayWeek) {
            cal.add(Calendar.DAY_OF_MONTH, -1);
        }

        cal.setFirstDayOfWeek(Calendar.MONDAY);//设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一

        int day = cal.get(Calendar.DAY_OF_WEEK);//获得当前日期是一个星期的第几天
        cal.add(Calendar.DATE, cal.getFirstDayOfWeek() - day);//根据日历的规则，给当前日期减去星期几与一个星期第一天的差值
        Date lTime = cal.getTime();
        DateTime lDateTime = new DateTime(lTime);
        String weekDate[] = getweekDays(lDateTime);

        String lBasePath = lDateTime.toString("MM/dd");
//        System.out.println(cal.getFirstDayOfWeek() + "-" + day + "+6=" + (cal.getFirstDayOfWeek() - day + 6));

        cal.add(Calendar.DATE, 6);
        Date lTime1 = cal.getTime();
        DateTime lDateTime1 = new DateTime(lTime1);


        String lBasePath2 = lDateTime1.toString("MM/dd");


        if (getView() != null) {
            String lDate = lBasePath + " - " + lBasePath2;
            if (isThisWeek(new DateTime(date).getMillis())) {
                lDate += "(本周)";
            }
            getView().showDateString(lDate, lDateTime, weekDate);
        }
    }

    //得到一周对应的日期
    private String[] getweekDays(DateTime dateTime) {
        String days[] = new String[6];
        for (int i = 0; i < days.length; i++) {
            days[i] = dateTime.plusDays(i).toString("dd");
        }
        return days;
    }

    public boolean isThisWeek(long time) {
        Calendar calendar = Calendar.getInstance();
        int currentWeek = calendar.get(Calendar.WEEK_OF_YEAR);
        calendar.setTime(new Date(time));
        int paramWeek = calendar.get(Calendar.WEEK_OF_YEAR);
        return paramWeek == currentWeek;
    }

    public void showCourseMismatchDialog(int courseCount, int size, Context context) {
        AlertDialog.Builder lBuilder = new AlertDialog.Builder(context);
        lBuilder.setTitle("提示")
                .setMessage("你申请调课的课程数：" + courseCount + "节课" + "\n" + "\n" + "当前已选被调课的课程数：" + size + "节课")
                .setPositiveButton("确认", (dialog, which) -> dialog.dismiss())
                .show();
    }
}
