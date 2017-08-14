package com.gta.zssx.mobileOA.presenter;

import android.content.Intent;

import com.gta.utils.mvp.BasePresenter;
import com.gta.zssx.mobileOA.model.bean.Attachment;
import com.gta.zssx.mobileOA.view.AttachmentListView;
import com.gta.zssx.mobileOA.view.page.AttachmentListActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiaoxia.rang on 2016/11/1.
 * 附件列表presenter
 */

public class AttachmentListPresenter extends BasePresenter<AttachmentListView> {


    public void getAttachment(AttachmentListActivity activity){
        Intent intent = activity.getIntent();
//        if(intent == null){
            List<Attachment> attachments = new ArrayList<>();
            for(int i = 0;i<4;i++){
                Attachment attachment = new Attachment();
                attachment.setName("附件"+(i+1));
                attachment.setType(i);
                attachment.setUrl("");
                attachment.setSize((i+1)+"M");
                attachments.add(attachment);
            }
            getView().showAttachments(attachments);
//        }
    }
}
