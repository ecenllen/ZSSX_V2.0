package com.gta.zssx.patrolclass.model.entity;

import java.io.Serializable;

/**
 * 巡课登记提交成功后返回的结果实体
 * Created by liang.lu on 2016/9/6 10:52.
 */
public class SubmitEntity implements Serializable {
    private int xId;

    public int getxId () {
        return xId;
    }

    public void setxId (int xId) {
        this.xId = xId;
    }
}
