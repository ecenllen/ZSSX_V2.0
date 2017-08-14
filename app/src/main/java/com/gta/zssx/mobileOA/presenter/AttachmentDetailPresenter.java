package com.gta.zssx.mobileOA.presenter;

import android.content.Intent;

import com.gta.utils.mvp.BasePresenter;
import com.gta.zssx.mobileOA.model.bean.Attachment;
import com.gta.zssx.mobileOA.view.AttachmentDetailView;
import com.gta.zssx.mobileOA.view.page.AttachmentDetailActivity;

/**
 * Created by xiaoxia.rang on 2016/11/2.
 * 附件详情presenter
 */

public class AttachmentDetailPresenter extends BasePresenter<AttachmentDetailView> {

    public void initAttachment(AttachmentDetailActivity activity) {
        Intent intent = activity.getIntent();
        Attachment attachment = (Attachment) intent.getSerializableExtra("attachment");
        getView().showAttachmentInfo(attachment);
    }

    public void downloadFile(String url) {

    }
}
