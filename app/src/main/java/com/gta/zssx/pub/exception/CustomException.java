package com.gta.zssx.pub.exception;

/**
 * [Description]
 * <p>
 * [How to use]
 * <p>
 * [Tips]
 *
 * @author Created by Zhimin.Huang on 2016/6/16.
 * @since 1.0.0
 */
public class CustomException extends RuntimeException {

    public static int SUCCESS = 0;
    public static int FAIL = 1;
    public static int TIMEOUT = 2;
    public static int NETWORK_UNAVAILABLE = 3;
    //分页时没有下一页
    public static int NO_MORE_RECORD = 9;
    //签名确认的时候已经被登记
    public static int HAS_SIGN = 10;
    public static int EMPTY_DATA = 4;

    public static int HAS_ADD = 8;
    public static int HAS_ADD_ALL = 11;




    private int Code;
    private String Message;

    public CustomException(int code, String message) {
        Code = code;
        Message = message;
    }

    public CustomException() {
    }

    public int getCode() {
        return Code;
    }

    public void setCode(int code) {
        Code = code;
    }

    @Override
    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }
}
