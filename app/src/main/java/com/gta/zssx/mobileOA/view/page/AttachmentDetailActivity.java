package com.gta.zssx.mobileOA.view.page;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.gta.zssx.R;
import com.gta.zssx.mobileOA.model.bean.Attachment;
import com.gta.zssx.mobileOA.presenter.AttachmentDetailPresenter;
import com.gta.zssx.mobileOA.view.AttachmentDetailView;
import com.gta.zssx.pub.base.BaseActivity;
import com.gta.zssx.pub.manager.ToolBarManager;

/**
 * 附件详情
 */
public class AttachmentDetailActivity extends BaseActivity<AttachmentDetailView,AttachmentDetailPresenter> implements  AttachmentDetailView {

    private TextView tvName;
    private ImageView ivCancel;
    private TextView tvCurrentSize;
    private TextView tvTotalSize;

    @NonNull
    @Override
    public AttachmentDetailPresenter createPresenter() {
        return new AttachmentDetailPresenter();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oa_attachment_detail);
        inits();
    }

    private void inits(){
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        ImageView ivType = (ImageView) findViewById(R.id.iv_type);
        tvName = (TextView) findViewById(R.id.tv_name);
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
        TextView tvProgress = (TextView) findViewById(R.id.tv_progress);
        tvCurrentSize = (TextView)findViewById(R.id.tv_currentSize);
        tvTotalSize = (TextView)findViewById(R.id.tv_totalSize);
        TextView tvDownload = (TextView) findViewById(R.id.tv_download);
        ToolBarManager mToolBarManager = new ToolBarManager(mToolbar, this);
        mToolBarManager.init();
        if (mToolbar != null) {
            mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
        }
        mToolBarManager.showBack(true)
                .setTitle("附件详情");
        presenter.initAttachment(AttachmentDetailActivity.this);
    }

    @Override
    public void showAttachmentInfo(Attachment attachment) {
        tvName.setText(attachment.getName());
        tvCurrentSize.setText("0");
        tvTotalSize.setText("/" + attachment.getSize());
    }

    @Override
    public void showProgress(int progress) {

    }

}
