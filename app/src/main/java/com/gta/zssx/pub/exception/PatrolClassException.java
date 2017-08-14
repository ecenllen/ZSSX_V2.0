package com.gta.zssx.pub.exception;

/**
 * [Description]
 * <p>
 * [How to use]
 * <p>
 * [Tips]
 *
 * @author Created by Zhimin.Huang on 2016/9/2.
 * @since 1.0.0
 */
public class PatrolClassException extends RuntimeException {
    //巡客code
    //-- 当前巡视日期不属于任何一个学年学期，请重新选择
    public static final int DATA_IS_EMPTY = 101;
    //当前巡视日期所在的常年学期未启用对应的考核指标项，请通知系统管理员启用
    public static final int DATA_IS_NULL = 102;
    //巡视的班级、日期、节次已有数据，提示本班级本节课已有巡课数据，是否继续提交
    public static final int HAS_PATROL_CLASS = 103;
    //您当天已送审过，每天只能送审一次
    public static final int IS_SUBMIT_NOW = 105;
    //班级已被添加，请重新选择
    public static final int HAS_CLASS_ADD = 110;
    //已有班级被添加，请重新选择
    public static final int HAS_CLASS_ADD_ALL = 111;

    public static final String DATA_IS_NULL_STRING = "当前巡视日期所在的常年学期未启用对应的考核指标项,请通知系统管理员启用";
    public static final String DATA_IS_EMPTY_STRING = "当前巡视日期不属于任何一个学年学期,请重新选择";
    public static final String HAS_PATROL_CLASS_STTING = "该巡视日期下本班级本节课已登记,是否继续提交?";

    public static final String HAS_CLASS_ADD_STRING = "班级已被添加,请重新选择";
    public static final String HAS_CLASS_ADD_ALL_STRING = "已有班级被添加,请重新选择";
    public static final String IS_SUBMIT_NOW_STRING = "每天只能送审一次,您当天已经送审过,不能再登记";

    private int Code;
    private String Message;

    public PatrolClassException(int code, String message) {
        Code = code;
        Message = message;
    }

    public PatrolClassException() {
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
