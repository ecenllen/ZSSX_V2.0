package com.gta.utils.helper;

import android.annotation.SuppressLint;

import java.math.BigDecimal;


public class PriceUtils {
    @SuppressLint("DefaultLocale")
    public static String getPriceString2show(float price) {
        if (price == (long) price) {
            return String.format("%d", (long) price);
        } else {
            return String.format("%.2f", price);
        }
    }

    @SuppressLint("DefaultLocale")
    public static String getPriceString2show(double price) {
        if (price == (long) price) {
            return String.format("%d", (long) price);
        } else {
            return String.format("%.2f", price);
        }
    }

    public static String getPriceString2showCN(double price) {
        return "￥" + getPriceString2show(price);
    }
    public static String getPriceString2showCN(float price) {
        return "￥" + getPriceString2show(price);
    }
    public static String getPriceString2showCN(BigDecimal price) {
        return "￥" + getPriceString2show(price);
    }
    public static String getPriceString2showCN(String price) {
        return "￥" + price;
    }

    public static String getPriceString2show(BigDecimal price) {
        return getPriceString2show(price.floatValue());
    }
}
