package com.gta.zssx.mobileOA.view;

import com.gta.utils.mvp.BaseView;
import com.gta.zssx.mobileOA.model.bean.Attachment;

/**
 * Created by xiaoxia.rang on 2016/11/2.
 * 附件详情view
 */

public interface AttachmentDetailView extends BaseView {
    void showAttachmentInfo(Attachment attachment);
    void showProgress(int progress);
}
