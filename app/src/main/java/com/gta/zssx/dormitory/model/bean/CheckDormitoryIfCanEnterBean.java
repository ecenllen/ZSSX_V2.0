package com.gta.zssx.dormitory.model.bean;

import com.gta.zssx.pub.common.Constant;

import java.io.InterruptedIOException;
import java.io.Serializable;
import java.util.List;

/**
 * Created by lan.zheng on 2017/7/18.
 * 新增录入是否能进入下一步
 */

public class CheckDormitoryIfCanEnterBean implements Serializable{
    private int Code;
    private List<Integer> Data;

    public void setCode(int Code){
        this.Code = Code;
    }

    public int getCode() {
        return Code;
    }

    public void setData(List<Integer> Data){
        this.Data = Data;
    }

    public List<Integer> getData() {
        return Data;
    }
}
