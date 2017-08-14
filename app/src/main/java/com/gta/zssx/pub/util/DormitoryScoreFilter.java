package com.gta.zssx.pub.util;

import android.text.Editable;
import android.widget.EditText;

import com.blankj.utilcode.util.ToastUtils;

/**
 * Created by weiye.chen on 2017/7/18.
 */

public class DormitoryScoreFilter {

    /**
     * 宿舍评分，过虑输入不合法，不标准的分数，保留3位小数
     * @param s Editable
     * @param editText EditText
     * @return 是否不合法
     */
    public static boolean filterScore(Editable s, EditText editText) {
        if(!s.toString().trim().isEmpty()) {  // 宿舍得分输入框，限制0-100分数，并且最多保留三位小数
            if(s.toString().trim().startsWith("00")) {
                s.replace(0, 2, "");
//                    etDormitoryScore.setText("");
                ToastUtils.showShortToast("请输入0 - 100的分数(可保留三位小数)!");
                return true;
            } else if(s.toString().trim().startsWith("0") && s.toString().length() > 1 && !s.toString().contains(".")) {  // 此判断，可跟产品商量，是否去掉
                s.replace(0, 1, "");
            } else if(Float.valueOf(s.toString()) > 100) {
                editText.setText("100");
                editText.setSelection(editText.getText().toString().length());
                ToastUtils.showShortToast("请输入0 - 100的分数(可保留三位小数)!");
                return true;
            } else {
                if(s.toString().trim().contains(".")) {
                    String[] split = s.toString().split("\\.");
                    if(split.length > 1 && split[1] != null && split[1].length() > 3) {
                        s.replace(s.toString().trim().length() - 1, s.toString().trim().length(), "");
                        editText.setSelection(editText.getText().toString().length());
                        ToastUtils.showShortToast("最多只能输入三位小数!");
                        return true;
                    }
                }

            }

        }
        return false;
    }
}
