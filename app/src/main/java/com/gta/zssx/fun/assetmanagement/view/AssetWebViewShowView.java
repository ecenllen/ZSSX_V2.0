package com.gta.zssx.fun.assetmanagement.view;

import com.gta.utils.mvp.BaseView;
import com.gta.zssx.fun.assetmanagement.model.bean.ApprovalDetailBean;

/**
 * Created by lan.zheng on 2016/8/9.
 */
public interface AssetWebViewShowView extends BaseView {
    int getmId();

    void showPicture (String s);

    void upLoadRemark ();

    void uploadRemarks ();

    void onSuccessTicket(String ticket);

    void GetApprovalDetailInfo(ApprovalDetailBean approvalDetailBean);
}
