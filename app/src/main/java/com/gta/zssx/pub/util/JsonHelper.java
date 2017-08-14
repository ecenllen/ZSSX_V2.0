package com.gta.zssx.pub.util;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.List;

/**
 *
 */
public class JsonHelper {
    private static String TAG = "JSONHelper";


    // 把JSON文本parse为JSONObject或者JSONArray
    public static final Object parse (String text) {
        return JSON.parse (text);
    }

    // 把JSON文本parse成JSONObject
    public static final JSONObject parseObject (String text) {
        return JSON.parseObject (text);
    }

    // 把JSON文本parse为JavaBean
    public static final <T> T parseObject (String text, Class<T> clazz) {
        return JSON.parseObject (text, clazz);
    }

    // 把JSON文本parse成JSONArray
    public static final JSONArray parseArray (String text) {
        return JSON.parseArray (text);
    }

    //把JSON文本parse成JavaBean集合
    public static final <T> List<T> parseArray (String text, Class<T> clazz) {
        return JSON.parseArray (text, clazz);
    }

    // 将JavaBean序列化为JSON文本
    public static final String toJSONString (Object object) {
        return JSON.toJSONString (object);
    }

    // 将JavaBean序列化为带格式的JSON文本
    public static final String toJSONString (Object object, boolean prettyFormat) {
        return JSON.toJSONString (object, prettyFormat);
    }

    //将JavaBean转换为JSONObject或者JSONArray。
    public static final Object toJSON (Object javaObject) {
        return JSON.toJSON (javaObject);
    }
}
