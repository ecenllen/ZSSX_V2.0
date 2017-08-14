package com.gta.zssx.pub.update;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.widget.Button;
import android.widget.Toast;


import com.gta.zssx.R;

import java.io.File;
import java.net.SocketException;

/**
 * [Description]
 * <p>
 * [How to use]
 * <p>
 * [Tips]
 *
 * @author Created by Zhimin.Huang on 2015/10/26.
 * @since 1.0.0
 */
public class DownloadDialog {
    private final String TAG = this.getClass().getName();
    private final int UPDATA_NONEED = 0;
    private final int UPDATA_CLIENT = 1;
    private final int GET_UNDATAINFO_ERROR = 2;
    private final int SDCARD_NOMOUNTED = 3;
    private final int DOWN_ERROR = 4;
    private final int NETWORK_ERROR = 5;
    private Button getVersion;
    private UpdataInfo mInfo;
    private String localVersion;

    Context mContext;
    private AlertDialog mDialog;
    private CommonProgressDialog mProgressDialog;

    public DownloadDialog(final Context mContext, UpdataInfo mInfo) {
        this.mContext = mContext;
        this.mInfo = mInfo;
    }

    public static DownloadDialog getInstance(Context context, UpdataInfo info) {
        return new DownloadDialog(context, info);
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case UPDATA_NONEED:
                    Toast.makeText(mContext, "不需要更新",
                            Toast.LENGTH_SHORT).show();
                case UPDATA_CLIENT:
                    //对话框通知用户升级程序
                    showUpdataDialog();
                    break;
                case GET_UNDATAINFO_ERROR:
                    //服务器超时
                    Toast.makeText(mContext, "获取服务器更新信息失败", Toast.LENGTH_SHORT).show();
                    break;
                case DOWN_ERROR:
                    //下载apk失败
                    Toast.makeText(mContext, "下载新版本失败", Toast.LENGTH_SHORT).show();
                    showUpdataDialog();
                    break;
                case NETWORK_ERROR:
                    Toast.makeText(mContext, "下载新版本失败，请检查您的网络", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    /**
     * 检查软件是否有更新版本
     *
     * @return
     */
    public void isUpdate() {
        // 获取当前软件版本
//        int versionCode = Integer.valueOf(mInfo.getCurrentVersion());
        // 把version.xml放到网络上，然后获取文件信息
//        int serviceCode = Integer.valueOf(mInfo.getServerVersion());
        // 版本判断
//        if (serviceCode > versionCode) {
//            Message msg = new Message();
//            msg.what = UPDATA_CLIENT;
//            handler.sendMessage(msg);
//        }
        Message msg = new Message();
        msg.what = UPDATA_CLIENT;
        handler.sendMessage(msg);
    }


    /*
         *
         * 弹出对话框通知用户更新程序
         *
         * 弹出对话框的步骤：
         *  1.创建alertDialog的builder.
         *  2.要给builder设置属性, 对话框的内容,样式,按钮
         *  3.通过builder 创建一个对话框
         *  4.对话框show()出来
         */
    protected void showUpdataDialog() {
        Builder builer = new Builder(mContext);
        builer.setCancelable(false);
        builer.setTitle(R.string.util_dialog_version_update);
//        builer.setMessage(info.getDescription());
        builer.setMessage(mInfo.getDescription());
        //当点确定按钮时从服务器上下载 新的apk 然后安装
        builer.setPositiveButton(R.string.util_dialog_version_update_comfirm, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                downLoadApk();
            }
        });

        //1、普通更新 2、重点更新 3、强制更新
        int lUpdateLevel = mInfo.getUpdateLevel();
        if (lUpdateLevel == 1) {
            builer.setNegativeButton(R.string.util_dialog_version_update_cancel, new DialogInterface
                    .OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    //do sth
                }
            });
        } else if (lUpdateLevel == 2) {
            builer.setNegativeButton(R.string.util_dialog_version_update_cancel, new DialogInterface
                    .OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    //do sth
                }
            });
        } else if (lUpdateLevel == 3) {

        }

        mDialog = builer.create();
        if (lUpdateLevel == 1) {

        } else if (lUpdateLevel == 2) {

        } else if (lUpdateLevel == 3) {
            mDialog.setCanceledOnTouchOutside(false);
        }
        mDialog.show();
    }

    /*
     * 从服务器中下载APK
     */
    protected void downLoadApk() {
//        final ProgressDialog pd;    //进度条对话框
//        pd = new ProgressDialog(mContext);
//        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
//        pd.setMessage("正在下载更新");
        mProgressDialog = new CommonProgressDialog(mContext);

        mProgressDialog.setMessage("正在下载更新");
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {

            @Override
            public void onCancel(DialogInterface dialog) {
                // TODO Auto-generated method stub
                //cancel(true);
            }
        });

        //1、普通更新 2、重点更新 3、强制更新
        int lUpdateLevel = mInfo.getUpdateLevel();
        if (lUpdateLevel == 1) {

        } else if (lUpdateLevel == 2) {

        } else if (lUpdateLevel == 3) {
            mProgressDialog.setCancelable(false);
        }
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.show();
        new Thread() {
            @Override
            public void run() {
                try {
                    String url = mInfo.getUrl();
                    File file = DownLoadManager.getFileFromServer(url
                            , mProgressDialog);
                    sleep(1000);
                    installApk(file);
                    mProgressDialog.dismiss(); //结束掉进度条对话框
                } catch (Exception e) {
                    Message msg = new Message();
                    if (e instanceof SocketException) {
                        msg.what = NETWORK_ERROR;
                    } else {
                        msg.what = DOWN_ERROR;
                    }
                    handler.sendMessage(msg);
                    mProgressDialog.dismiss(); //结束掉进度条对话框
                    e.printStackTrace();
                }
            }
        }.start();
    }

    //安装apk
    protected void installApk(File file) {
        Intent intent = new Intent();
        //执行动作
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        //执行的数据类型
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        mContext.startActivity(intent);
        android.os.Process.killProcess(android.os.Process.myPid());
    }


}

