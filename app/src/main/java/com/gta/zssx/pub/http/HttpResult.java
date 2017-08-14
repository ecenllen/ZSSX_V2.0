package com.gta.zssx.pub.http;

/**
 * [Description]
 * <p>
 * [How to use]
 * <p>
 * [Tips]
 *
 * @author Created by Jim.Huang on 2016/6/14.
 * @since 1.0.0
 */
public class HttpResult<T> {

    private int Code;
    private String Message;
    private T Data;

    public int getCode() {
        return Code;
    }

    public void setCode(int code) {
        Code = code;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public T getData() {
        return Data;
    }

    public void setData(T data) {
        Data = data;
    }

}
