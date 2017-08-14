package com.gta.utils.helper;

import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.widget.EditText;

import org.joda.time.DateTime;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2015/9/23.
 */
public class Helper_String {

    public static String getXingNumber(String number) {
        if (number.equals("")) {
            return "";
        }
        String re = number.substring(3, number.length() - 4);
        String xing = "";
        for (int i = 0; i < (number.length() - 7); i++) {
            xing += "*";
        }
        return number.replace(re, xing);
    }

    /**
     * 合并图片地址
     *
     * @return String
     */
    public static String getImageUrl(ArrayList<String> imageUrls) {
        String Url = "";
        for (int i = 0; i < imageUrls.size(); i++) {
            if (i != 0) {
                Url += ",";
            }
            Url = Url + imageUrls.get(i);
        }
        return Url;
    }

    /**
     * 正则表达式
     *
     * @param str
     * @return
     */
    public static boolean isLetterDigit(String str) {
        boolean isDigit = false;//定义一个boolean值，用来表示是否包含数字
        boolean isLetter = false;//定义一个boolean值，用来表示是否包含字母
        for (int i = 0; i < str.length(); i++) {
            if (Character.isDigit(str.charAt(i))) {   //用char包装类中的判断数字的方法判断每一个字符
                isDigit = true;
            }
            if (Character.isLetter(str.charAt(i))) {  //用char包装类中的判断字母的方法判断每一个字符
                isLetter = true;
            }
        }
        String regex = "^[a-zA-Z0-9]+$";
        boolean isRight = isDigit && isLetter && str.matches(regex);
        return isRight;
    }

    public static boolean hasChinese(String string) {
        char[] s = string.toCharArray();
        for (int i = 0; i < string.length(); i++) {
            if (isChinese(s[i])) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判定输入汉字
     *
     * @param c
     * @return
     */
    public static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
            return true;
        }
        return false;
    }

    /**
     * 禁止空格
     *
     * @param editText
     */
    public static void withoutSpace(EditText editText) {
        editText.setFilters(new InputFilter[]{
                new InputFilter() {
                    @Override
                    public CharSequence filter(CharSequence source, int start,
                                               int end, Spanned dest,
                                               int dstart, int dend) {
                        //返回null表示接收输入的字符,返回空字符串表示不接受输入的字符
                        if (source.equals(" ")) return "";
                        else return null;
                    }
                }
        });
    }

    public static void inputFilterSpace(final EditText edit) {
        edit.setFilters(new InputFilter[]
                {
                        new InputFilter.LengthFilter(16),
                        new InputFilter() {
                            public CharSequence filter(CharSequence src, int start, int end, Spanned dst, int dstart, int dend) {
                                if (src.length() < 1) {
                                    return null;
                                } else {
                                    char temp[] = (src.toString()).toCharArray();
                                    char result[] = new char[temp.length];
                                    for (int i = 0, j = 0; i < temp.length; i++) {
                                        if (temp[i] == ' ') {
                                            continue;
                                        } else {
                                            result[j++] = temp[i];
                                        }
                                    }
                                    return String.valueOf(result).trim();
                                }

                            }
                        }
                });
    }

    /**
     * 保留两位小数
     *
     * @param num
     */
    public static double save2Point(Double num) {
        return (double) (Math.round(num * 10000) / 10000.00);
    }

    /**
     * 保留两位小数
     *
     * @param num
     */
    public static String save2PointBig(Double num) {
        DecimalFormat df = new DecimalFormat("###0.00");
        Double d = new Double(num);
        return df.format(d);

//        // 得到本地的缺省格式
//
//        DecimalFormat df1 = new DecimalFormat("####.00");
//
//        System.out.println(df1.format(1234.56));
//
//        // 得到德国的格式
//
//        Locale.setDefault(Locale.GERMAN);
//
//        DecimalFormat df2 = new DecimalFormat("####.000");
//
//        System.out.println(df2.format(1234.56));
    }

    public static void setPricePoint(final EditText editText) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if (s.toString().contains(".")) {
                    if (s.length() - 1 - s.toString().indexOf(".") > 2) {
                        s = s.toString().subSequence(0,
                                s.toString().indexOf(".") + 3);
                        editText.setText(s);
                        editText.setSelection(s.length());
                    }
                }
                if (s.toString().trim().substring(0).equals(".")) {
                    s = "0" + s;
                    editText.setText(s);
                    editText.setSelection(2);
                }

                if (s.toString().startsWith("0")
                        && s.toString().trim().length() > 1) {
                    if (!s.toString().substring(1, 2).equals(".")) {
                        editText.setText(s.subSequence(0, 1));
                        editText.setSelection(1);
                        return;
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    /**
     * 将每三个数字加上逗号处理（通常使用金额方面的编辑）
     *
     * @param str 无逗号的数字
     *            <a href="http://home.51cto.com/index.php?s=/space/34010" target="_blank">@return</a> 加上逗号的数字
     */
    public static String addComma(String str) {
// 将传进数字反转
        String reverseStr = new StringBuilder(str).reverse().toString();
        String strTemp = "";
        for (int i = 0; i < reverseStr.length(); i++) {
            if (i * 3 + 3 > reverseStr.length()) {
                strTemp += reverseStr.substring(i * 3, reverseStr.length());
                break;
            }
            strTemp += reverseStr.substring(i * 3, i * 3 + 3) + ",";
        }
// 将[789,456,] 中最后一个[,]去除
        if (strTemp.endsWith(",")) {
            strTemp = strTemp.substring(0, strTemp.length() - 1);
        }
// 将数字重新反转
        String resultStr = new StringBuilder(strTemp).reverse().toString();
        return resultStr;
    }

    /**
     * 检测是否有emoji表情 * @param source * @return
     */
    public static boolean containsEmoji(String source) {
        int len = source.length();
        for (int i = 0; i < len; i++) {
            char codePoint = source.charAt(i);
            if (!isEmojiCharacter(codePoint)) {
                //如果不能匹配,则该字符是Emoji表情
                return true;
            }
        }
        return false;
    }

    /**
     * 判断是否是Emoji * @param codePoint 比较的单个字符 * @return
     */
    public static boolean isEmojiCharacter(char codePoint) {
        return (codePoint == 0x0) || (codePoint == 0x9) || (codePoint == 0xA) || (codePoint == 0xD) ||
                ((codePoint >= 0x20) && (codePoint <= 0xD7FF)) || ((codePoint >= 0xE000) && (codePoint <= 0xFFFD)) ||
                ((codePoint >= 0x10000) && (codePoint <= 0x10FFFF));
    }

//    /** *删除Emoji* @return */
//    public static String deleteEmoji(String s) {
//        for (int lI = 0; lI < s.length(); lI++) {
//            if (isEmojiCharacter(s.charAt(lI))) {
//                s.replace(String.valueOf(s.charAt(lI)),"");
//            }
//        }
//        return s;
//    }


    public static String getDayOfWeek(Date date) {
        String[] weekDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        Calendar lCalendar = Calendar.getInstance();
        lCalendar.setTime(date);
        int w = lCalendar.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0) {
            w = 0;
        }
        return weekDays[w];
    }

    /**
     * 根据日期活动星期几
     *
     * @param date
     * @return
     */
    public static String getDayOfWeek(DateTime date) {
        String[] weekDays = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
        Calendar lCalendar = Calendar.getInstance();
        lCalendar.setTime(date.toDate());
        int w = lCalendar.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0) {
            w = 0;
        }
        return weekDays[w];
    }

    public static boolean isURL(String url) {
        boolean flag = false;
        String text = "^(http|https|ftp)\\://([a-zA-Z0-9\\.\\-]+(\\:[a-zA-Z0-9\\.&amp;%\\$\\-]+)*@)?((25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9])\\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[0-9])|([a-zA-Z0-9\\-]+\\.)*[a-zA-Z0-9\\-]+\\.[a-zA-Z]{2,4})(\\:[0-9]+)?(/[^/][a-zA-Z0-9\\.\\,\\?\\'\\\\/\\+&amp;%\\$#\\=~_\\-@]*)*$";
        Pattern pattern1 = Pattern.compile(text);
        Matcher isNo = pattern1.matcher(url);
        if (isNo.matches()) {
            flag = true;
        }
        return flag;
    }
}
