package com.gta.zssx.dormitory.model.bean;

import java.io.Serializable;

/**
 * Created by weiye.chen on 2017/7/13.
 */

public class DormitoryEnterNameSingleHttpInfoBean extends SaveBaseHttpBean implements Serializable {
    private DormitoryEnterNameSingleItemInfoBean DetailContent;

    public DormitoryEnterNameSingleItemInfoBean getDetailContent() {
        return DetailContent;
    }

    public void setDetailContent(DormitoryEnterNameSingleItemInfoBean detailContent) {
        DetailContent = detailContent;
    }
}
