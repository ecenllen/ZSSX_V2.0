package com.gta.zssx.mobileOA.view.page;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.gta.zssx.R;
import com.gta.zssx.mobileOA.model.bean.Attachment;
import com.gta.zssx.mobileOA.presenter.AttachmentListPresenter;
import com.gta.zssx.mobileOA.view.AttachmentListView;
import com.gta.zssx.mobileOA.view.adapter.AttachmentAdapter;
import com.gta.zssx.pub.base.BaseActivity;
import com.gta.zssx.pub.manager.ToolBarManager;

import java.util.ArrayList;
import java.util.List;

/**
 * 附件列表
 */
public class AttachmentListActivity extends BaseActivity<AttachmentListView, AttachmentListPresenter> implements AttachmentListView {

    private Toolbar mToolbar;
    private RecyclerView rvAttachments;
    private ToolBarManager mToolBarManager;
    private AttachmentAdapter adapter;
    private List<Attachment> attachments = new ArrayList<>();

    @NonNull
    @Override
    public AttachmentListPresenter createPresenter() {
        return new AttachmentListPresenter();
    }

    public static void start(Context context) {
        final Intent lIntent = new Intent(context, AttachmentListActivity.class);
//        Bundle lBundle = new Bundle();
        if (!(context instanceof Activity)) {
            lIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(lIntent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oa_attachment_list);
        inits();
    }

    private void inits() {
        //初始化标题栏
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        rvAttachments = (RecyclerView) findViewById(R.id.rv_attachments);

        mToolBarManager = new ToolBarManager(mToolbar, this);
        mToolBarManager.init();
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        mToolBarManager.showBack(true)
                .setTitle(getResources().getString(R.string.oa_attachment));

        adapter = new AttachmentAdapter(AttachmentListActivity.this, attachments);
        rvAttachments.setAdapter(adapter);
        presenter.getAttachment(this);
    }

    @Override
    public void showAttachments(List<Attachment> attachmentList) {
        attachments.addAll(attachmentList);
        adapter.notifyDataSetChanged();
    }
}
