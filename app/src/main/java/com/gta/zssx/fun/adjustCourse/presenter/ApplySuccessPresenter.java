package com.gta.zssx.fun.adjustCourse.presenter;

import android.content.Context;
import android.support.v7.app.AlertDialog;

import com.gta.utils.mvp.BasePresenter;
import com.gta.zssx.fun.adjustCourse.view.ApplySuccessView;
import com.gta.zssx.fun.adjustCourse.view.page.ApplySuccessActivity;

/**
 * [Description]
 * <p>
 * [How to use]
 * <p>
 * [Tips]
 *
 * @author Created by Zhimin.Huang on 2016/10/24.
 * @since 1.0.0
 */
public class ApplySuccessPresenter extends BasePresenter<ApplySuccessView> {
    private Context mContext;

    public ApplySuccessPresenter(Context context) {
        mContext = context;
    }

    public void showPromoptDialog(String weChat, Context activity) {
        AlertDialog.Builder lBuilder = new AlertDialog.Builder(activity);
        lBuilder.setTitle("已为您复制了通知内容")
                .setMessage(ApplySuccessActivity.SHARE_TEXT)
                .setPositiveButton("通知", (dialog, which) -> {
                    getView().confirmNotify(weChat);
                    dialog.dismiss();
                })
                .setNegativeButton("取消", null)
                .show();
    }
}
