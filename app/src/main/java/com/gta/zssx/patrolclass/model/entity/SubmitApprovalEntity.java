package com.gta.zssx.patrolclass.model.entity;

import java.io.Serializable;

/**
 * Created by liang.lu on 2016/8/4 15:06.
 */
public class SubmitApprovalEntity implements Serializable {
    private String uid;
    private String ids;

    public String getUid () {
        return uid;
    }

    public void setUid (String uid) {
        this.uid = uid;
    }

    public String getIds () {
        return ids;
    }

    public void setIds (String ids) {
        this.ids = ids;
    }
}
