package com.gta.zssx.mobileOA.view;

import com.gta.utils.mvp.BaseView;
import com.gta.zssx.mobileOA.model.bean.Attachment;

import java.util.List;

/**
 * Created by xiaoxia.rang on 2016/11/1.
 */

public interface AttachmentListView extends BaseView {

    void showAttachments(List<Attachment> attachments);
}
