package com.gta.zssx.pub.exception;

/**
 * Created by liang.lu on 2016/11/18 16:39.
 */

public class AssetException extends RuntimeException {

    public static final int ASSET_NOT_IN_FROM_NUM = 10002;
    public static final String ASSET_NOT_IN_FROM_STRING = "此资产不在本次盘点列表中";

    private int Code;
    private String Message;

    public AssetException () {

    }

    public AssetException (int code, String message) {
        this.Code = code;
        this.Message = message;
    }

    public int getCode () {
        return Code;
    }

    public void setCode (int code) {
        Code = code;
    }

    @Override
    public String getMessage () {
        return Message;
    }

    public void setMessage (String message) {
        Message = message;
    }
}
