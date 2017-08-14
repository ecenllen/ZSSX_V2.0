package com.gta.zssx.mobileOA.view;

import com.gta.utils.mvp.BaseView;

import java.io.File;

/**
 * Created by xiaoxia.rang on 2016/10/27.
 */

public interface BacklogDetailView extends BaseView {

    void setTitle(String title);
    void setUrl(String url);
    void showApprovalHistoryButton(boolean show);
    void showDownloadNotification(int status, int progress, String fileName, File file);
}
