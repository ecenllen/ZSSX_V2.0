package com.gta.zssx.pub.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by xiaoxia.rang on 2016/11/18.
 */

public class DateUtils {

    /**
     * 格式化时间
     *
     * @param time
     * @return
     */
    public static String formatDateTime(String serverTime, String time) {
        SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm");
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
        if (time == null || "".equals(time)) {
            return "";
        }
        Date serverDate = null;
        Date date = null;
        try {
            serverDate = format.parse(serverTime);
            date = format.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar current = Calendar.getInstance();

        Calendar today = Calendar.getInstance();    //今天
        if (serverDate != null) {
            today.setTime(serverDate);
        }else{
            today.set(Calendar.YEAR, current.get(Calendar.YEAR));
            today.set(Calendar.MONTH, current.get(Calendar.MONTH));
            today.set(Calendar.DAY_OF_MONTH, current.get(Calendar.DAY_OF_MONTH));
}
        //  Calendar.HOUR——12小时制的小时数 Calendar.HOUR_OF_DAY——24小时制的小时数
        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);

        Calendar yesterday = Calendar.getInstance();    //昨天

        yesterday.set(Calendar.YEAR, current.get(Calendar.YEAR));
        yesterday.set(Calendar.MONTH, current.get(Calendar.MONTH));
        yesterday.set(Calendar.DAY_OF_MONTH, current.get(Calendar.DAY_OF_MONTH) - 1);
        yesterday.set(Calendar.HOUR_OF_DAY, 0);
        yesterday.set(Calendar.MINUTE, 0);
        yesterday.set(Calendar.SECOND, 0);

        current.setTime(date);

//        if (current.after(today)) {
//            return time.split(" ")[1];
//        } else if (current.before(today) && current.after(yesterday)) {
//            return "昨天 " + time.split(" ")[1];
//        } else {
////            int index = time.indexOf("-")+1;
////            return time.substring(index, time.length());
//            return time.split(" ")[0];
//        }

        if (current.after(today)) {
            return  dateFormat.format(current.getTime());
        } else {
//            int index = time.indexOf("-")+1;
//            return time.substring(index, time.length());
            return time.split(" ")[0];
        }
    }

}
