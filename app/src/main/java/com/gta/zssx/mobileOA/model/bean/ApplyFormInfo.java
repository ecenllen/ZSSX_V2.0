package com.gta.zssx.mobileOA.model.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by xiaoxia.rang on 2016/11/17.
 * 我的申请表单返回
 */

public class ApplyFormInfo implements Serializable {
     private List<ApplyForm>  ApplyFormList;

    public List<ApplyForm> getApplyFormList() {
        return ApplyFormList;
    }

    public void setApplyFormList(List<ApplyForm> applyFormList) {
        ApplyFormList = applyFormList;
    }
}
