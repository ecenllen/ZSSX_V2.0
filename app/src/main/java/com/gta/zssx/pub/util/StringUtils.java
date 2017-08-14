package com.gta.zssx.pub.util;

import android.os.Environment;
import android.text.TextUtils;

import java.util.Calendar;

/**
 * Created by lan.zheng on 2016/6/21.
 */
public class StringUtils {

    //个人中心
    public static String USER_ID = "";

    public static String IMAGE_SAVE_PATH2 = Environment
            .getExternalStorageDirectory ().getAbsolutePath ()
            + "/ZSSX1_picture";

    public static String IMAGE_SAVE_PATH = Environment
            .getExternalStorageDirectory ().getAbsolutePath ()
            + "/智慧校园";

    /**
     * 判断给定字符串是否空白串。 空白串是指由空格、制表符、回车符、换行符组成的字符串 若输入字符串为null或空字符串，返回true
     *
     * @param string
     * @return boolean
     */
    public static boolean isEmpty (String string) {
        if(TextUtils.isEmpty (string)){
            return true;
        }
        for (int i = 0; i < string.length(); i++) {
            char c = string.charAt(i);
            if (c != ' ' && c != '\t' && c != '\r' && c != '\n') {
                return false;
            }
        }
        return true;
//        return TextUtils.isEmpty (string);
    }

    public static String changeWeekDaytoString (int weekDay) {
        String day = "";
        switch (weekDay) {
            case Calendar.MONDAY:
                day = "星期一";
                break;
            case Calendar.TUESDAY:
                day = "星期二";
                break;
            case Calendar.WEDNESDAY:
                day = "星期三";
                break;
            case Calendar.THURSDAY:
                day = "星期四";
                break;
            case Calendar.FRIDAY:
                day = "星期五";
                break;
            case Calendar.SATURDAY:
                day = "星期六";
                break;
            case Calendar.SUNDAY:
                day = "星期日";
                break;
            default:
                break;
        }
        return day;
    }

    /**
     * 去掉末位多余的0
     * @param score score
     * @return score
     */
    public static String formatScore(String score){
        if(!score.isEmpty() && score.indexOf(".") > 0){
            score = score.replaceAll("0+?$", "");//去掉多余的0
            score = score.replaceAll("[.]$", "");//如最后一位是.则去掉
            return score;
        }else {
            return score;
        }

    }

}
