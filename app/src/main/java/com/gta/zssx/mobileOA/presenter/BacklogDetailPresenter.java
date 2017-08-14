package com.gta.zssx.mobileOA.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.gta.utils.mvp.BasePresenter;
import com.gta.zssx.ZSSXApplication;
import com.gta.zssx.fun.personalcenter.model.LoginDataManager;
import com.gta.zssx.fun.personalcenter.model.bean.UserBean;
import com.gta.zssx.mobileOA.model.OAMainModel;
import com.gta.zssx.mobileOA.view.BacklogDetailView;
import com.gta.zssx.pub.common.Constant;
import com.gta.zssx.pub.util.LogUtil;
import com.gta.zssx.pub.util.MIMETypeUtil;
import com.gta.zssx.pub.util.PreferencesUtils;
import com.gta.zssx.pub.util.RxBus;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import rx.Subscriber;
import rx.Subscription;

/**
 * Created by xiaoxia.rang on 2016/10/27.
 * 事项详情presenter
 */

public class BacklogDetailPresenter extends BasePresenter<BacklogDetailView> {

    private int type;//类型
    private int position;//位置
    private int id;//事项id(公文，公告、会议id)
    private String taskId;//待办事项id
    private String runId;//运行id
    private String applyFormId;//申请表id
    private int status;//阅读状态 0：未读，1：已读
    public Subscription mSubscribe;
    private String userId;
    private UserBean userBean;
    private OkHttpClient mOkHttpClient;
    private Activity activity;
    private String applyUrl;
    private String detailUrl;
    private String approvalUrl;

    public BacklogDetailPresenter () {
        userBean = ZSSXApplication.instance.getUser();
        userId = userBean.getUserId ();
         applyUrl = userBean.getBpmHostUrl() + "/platform/bpm/task/startFlowForm.ht?defId=%s&platForm=MOBILE&ticket=%s";
        //    private String applyUrl = "http://%s/bpm/platform/bpm/task/startFlowForm.ht?defId=%s&platForm=MOBILE&ticket=%s";
        /**
         * 事项详情
         */
        detailUrl = userBean.getBpmHostUrl() + "/platform/bpm/processRun/info.ht?link=1&runId=%s&prePage=myFinishedTask&platForm=MOBILE&ticket=%s";
        //    private String detailUrl = "http://%s/bpm/platform/bpm/processRun/info.ht?link=1&runId=%s&prePage=myFinishedTask&platForm=MOBILE&ticket=%s";
        /**
         * 事项审批
         */
         approvalUrl = userBean.getBpmHostUrl() + "/platform/bpm/task/toStart.ht?taskId=%s&platForm=MOBILE&ticket=%s";
        //    private String approvalUrl = "http://%s/bpm/platform/bpm/task/toStart.ht?taskId=%s&platForm=MOBILE&ticket=%s";
        //    String Localurl="10.1.34.29:8080";//彭国庆
    }

    //        String Localurl = "10.1.34.109:8080";//易新
    //    String Localurl = "192.168.105.192:7071";
    //    String Localurl ="10.1.34.119:8080";//丽云本地
    //
    /**
     * 发起申请
     */

    public void initData(Activity activity) {
        this.activity = activity;
//        getView().showLoadingDialog();
//        String lMD5password = EncryptUtils.encryptMD5ToString(userBean.getMD5Password() + "{000000}");
//        String lPowerHostUrl = userBean.getPowerHostUrl() + "SignIn/";
        mSubscribe = LoginDataManager.getTicket(userBean.getUserName().toUpperCase(), userBean.getMD5Password(), userBean.getPowerHostUrl(), userBean.getTicket()).subscribe(new Subscriber<String>() {
            @Override
            public void onCompleted() {
                getView().hideDialog();
            }

            @Override
            public void onError(Throwable e) {
                getView().hideDialog();
                getView().showError("加载失败");
            }

            @Override
            public void onNext(String s) {
                userBean.setTicket(s);
                Intent intent = activity.getIntent();
                String url = null;
                String title = null;
                id = intent.getIntExtra("id", 0);
                applyFormId = intent.getStringExtra("applyFormId");
                runId = intent.getStringExtra("runId");
                taskId = intent.getStringExtra("taskId");
                position = intent.getIntExtra("position", 0);
                type = intent.getIntExtra("type", 0);
                status = intent.getIntExtra("status", 0);
                boolean showHistory = true;

                switch (type) {
                    case Constant.DETAIL_TYPE_APPLY:
                        title = "发起申请";
                        url = String.format(applyUrl, applyFormId, userBean.getTicket());
//                        url = String.format(applyUrl, Localurl, applyFormId, userBean.getTicket());
                        showHistory = false;
                        break;
                    case Constant.DETAIL_TYPE_MYAPPLY:
                        title = "申请详情";
                        url = String.format(detailUrl, runId, userBean.getTicket());
//                        url = String.format(detailUrl, Localurl, runId, userBean.getTicket());
                        showHistory = true;
                        break;
                    case Constant.DETAIL_TYPE_APPROVAL:
                        title = "待办审核";
                        Log.i("taskId", taskId + "");
                        url = String.format(approvalUrl, taskId, userBean.getTicket());
//                        url = String.format(approvalUrl, Localurl, taskId, userBean.getTicket());
                        showHistory = true;
                        break;
                    case Constant.DETAIL_TYPE_FINISHED:
                        title = "已办审核";
                        url = String.format(detailUrl, runId, userBean.getTicket());
//                        url = String.format(detailUrl, Localurl, runId, userBean.getTicket());
                        showHistory = true;
                        break;
                    case Constant.DETAIL_TYPE_NOTICE:
                        title = "公告详情";
                        url = String.format(detailUrl, runId, userBean.getTicket());
//                        url = String.format(detailUrl, Localurl, runId, userBean.getTicket());
                        showHistory = false;
                        break;
                    case Constant.DETAIL_TYPE_DOC:
                        title = "公文详情";
                        url = String.format(detailUrl, runId, userBean.getTicket());
//                        url = String.format(detailUrl, Localurl, runId, userBean.getTicket());
                        showHistory = false;
                        break;
                    case Constant.DETAIL_TYPE_MEETING:
                        title = "会议详情";
                        url = String.format(detailUrl, runId, userBean.getTicket());
//                        url = String.format(detailUrl, Localurl, runId, userBean.getTicket());
                        showHistory = false;
                        break;
                    default:
                        break;

                }
                getView().setTitle(title);
                getView().showApprovalHistoryButton(showHistory);
                getView().setUrl(url);
                LogUtil.Log("url", url);

            }
        });
        mCompositeSubscription.add(mSubscribe);
    }


    /**
     * 修改已读状态
     */
    public void changeReadState() {
        if (status == 1) return;
        if (type == Constant.DETAIL_TYPE_MEETING) {
            setMeetingRead();
        } else if (type == Constant.DETAIL_TYPE_NOTICE || type == Constant.DETAIL_TYPE_DOC) {
            setNoticeRead();
        } else {
            return;
        }
    }


    /**
     * 修改公文公告状态
     */
    public void setNoticeRead() {
        mSubscribe = OAMainModel.setNoticeRead(userId, id).subscribe(new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                RxBus.getDefault().post(new Integer(position));
            }

            @Override
            public void onNext(String s) {
                //修改状态
                RxBus.getDefault().post(position);
//                RxBus.getDefault().post(Integer.decode());
            }
        });

        mCompositeSubscription.add(mSubscribe);
    }

    /**
     * 修改会议状态
     */
    public void setMeetingRead() {
        mSubscribe = OAMainModel.setMeetingRead(userId, id).subscribe(new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                RxBus.getDefault().post(position);
            }

            @Override
            public void onNext(String s) {
                //修改状态
                RxBus.getDefault().post(position);
            }
        });

        mCompositeSubscription.add(mSubscribe);
    }

    public String getRunId() {
        return runId;
    }


    /**
     * 下载附件
     *
     * @param url
     * @param fileName
     */
    public void downloadAttachment(String url, String fileName) {
        String attachmentId = null;
        if (fileName == null) {
            attachmentId = url.substring(url.indexOf("=") + 1);
            fileName = attachmentId + ".doc";
        } else {
            attachmentId = getAttachmentId(url);
        }
        String filePath = PreferencesUtils.getString(activity, attachmentId);
        if (isFileHasDownload(filePath)) {
            openFile(activity, new File(filePath));
        } else {
            PreferencesUtils.deleteKey(activity, attachmentId);
            doDownload(url, fileName, attachmentId);
        }
//    }
    }

    /**
     * 下载
     *
     * @param url
     * @param fileName
     * @param fileId
     */
    public void doDownload(String url, String fileName, String fileId) {
        String destFileDir = Environment.getExternalStorageDirectory() + "/zssx";
        File dirFirstFolder = new File(destFileDir);
        if (!dirFirstFolder.exists()) {
            dirFirstFolder.mkdirs();
        }
        File file = new File(destFileDir, fileName);
        if (file.exists()) {
            fileName = getNewFileName(fileName);
        }
        String realFileName = fileName;
//        notificationUtil.showNotification(Integer.parseInt(fileId),realFileName,0,Constant.DOWNLOAD_START);
        getView().showDownloadNotification(Constant.DOWNLOAD_START, 0, fileName, null);
        if (mOkHttpClient == null) {
            mOkHttpClient = new OkHttpClient().newBuilder()
                    .connectTimeout(10, TimeUnit.SECONDS)//设置超时时间
                    .readTimeout(10, TimeUnit.SECONDS)//设置读取超时时间
                    .writeTimeout(10, TimeUnit.SECONDS)//设置写入超时时间
                    .build();
        }
        Request request = new Request.Builder().get().url(url).build();
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                getView().showDownloadNotification(Constant.DOWNLOAD_ERROR, 0, realFileName, null);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                InputStream is = null;
                byte[] buf = new byte[2048];
                int len = 0;
                long size = response.body().contentLength();
                long readSize = 0;
                FileOutputStream fos = null;
                try {
                    is = response.body().byteStream();
                    File file = new File(destFileDir, realFileName);
                    fos = new FileOutputStream(file);
                    while ((len = is.read(buf)) != -1) {
                        fos.write(buf, 0, len);
                        readSize += len;
                        getView().showDownloadNotification(Constant.DOWNLOAD_PROGRESS, (int) (readSize / size * 100), realFileName, null);

                    }
                    fos.flush();
                    PreferencesUtils.putString(activity, fileId, file.getAbsolutePath());
                    getView().showDownloadNotification(Constant.DOWNLOAD_SUCCESS, 100, realFileName, file);
                    Looper.prepare();
                    Toast.makeText(activity.getApplicationContext(), realFileName + "下载成功", Toast.LENGTH_SHORT).show();
                    Looper.loop();

                } catch (IOException e) {
                    getView().showDownloadNotification(Constant.DOWNLOAD_ERROR, 0, realFileName, null);
                    Looper.prepare();
                    Toast.makeText(activity.getApplicationContext(), realFileName + "下载失败", Toast.LENGTH_SHORT).show();
                    Looper.loop();
                } finally {
                    try {
                        if (is != null) is.close();
                    } catch (IOException e) {
                    }
                    try {
                        if (fos != null) fos.close();
                    } catch (IOException e) {
                    }
                }
            }
        });
    }

    /**
     * 判断文件是否存在
     *
     * @return
     */
    public boolean isFileHasDownload(String filePath) {
        if (!TextUtils.isEmpty(filePath)) {
            File file = new File(filePath);
            if (file.exists()) {
                return true;
            }
        }
        return false;
    }

    public static String getNewFileName(String fileName) {
        String[] name = fileName.split("\\.");
        if (name[0].contains("-")) {
            String[] str1 = name[0].split("-");
            String index = str1[str1.length - 1];
            if (isInteger(index)) {
                if (name.length > 1) {
                    fileName = name[0].substring(0, name[0].lastIndexOf("-")) + "-" + (Integer.parseInt(index) + 1) + "." + name[1];
                } else {
                    fileName = name[0].substring(0, name[0].lastIndexOf("-")) + "-" + (Integer.parseInt(index) + 1);
                }
            } else {
                if (name.length == 2) {
                    fileName = name[0] + "-1." + name[1];
                } else {
                    fileName = name[0] + "-1" + name[1];
                }
            }
        } else {
            if (name.length == 2) {
                fileName = name[0] + "-1." + name[1];
            } else {
                fileName = name[0] + "-1";
            }
        }
        return fileName;
    }


    /**
     * 打开文件
     *
     * @param context
     * @param file
     */
    public void openFile(Context context, File file) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //设置intent的Action属性
        intent.setAction(Intent.ACTION_VIEW);
        //获取文件file的MIME类型
        String type = MIMETypeUtil.getMIMEType(file);
        //设置intent的data和Type属性。
        intent.setDataAndType(Uri.fromFile(file), type);
        //跳转
        context.startActivity(intent);
    }

    public String getAttachmentId(String url) {
        return url.substring(url.indexOf("_") + 1, url.lastIndexOf("."));
    }

    public static boolean isInteger(String value) {
        try {
            Integer.parseInt(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

}
