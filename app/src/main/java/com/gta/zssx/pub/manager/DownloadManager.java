package com.gta.zssx.pub.manager;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.webkit.MimeTypeMap;

import com.gta.zssx.pub.common.Constant;
import com.gta.zssx.pub.util.MIMETypeUtil;
import com.gta.zssx.pub.util.PreferencesUtils;

import java.io.File;

/**
 * Created by xiaoxia.rang on 2016/12/27.
 */

public class DownloadManager {

    private static DownloadManager downloadManager;
    private Context context;

    public static DownloadManager getInstance() {
        if (downloadManager == null) {
            synchronized (DownloadManager.class) {
                if (downloadManager == null) {
                    downloadManager = new DownloadManager();
                }
            }
        }
        return downloadManager;
    }

    public void init(Context context) {
        this.context = context;
    }

    public void downloadFile(String url) {
        if (PreferencesUtils.hasKey(context, url)) {
            long fileId = PreferencesUtils.getLong(context, url, -1);
            if (fileId == -1) {
                downFile(url);
            } else {
                        /*查询下载的状态*/
                if (isNeedDownload(url, fileId)) {
                    downFile(url);
                }
            }
        } else { //key不存在的情况下
            if (canDownLoadFile(url)) {
                downFile(url);
            }
        }
    }


    @SuppressLint("NewApi")
    private void downFile(String url) {
        android.app.DownloadManager downloadManager = (android.app.DownloadManager) context.getSystemService(Service.DOWNLOAD_SERVICE);
        Uri resource = Uri.parse(url);
        android.app.DownloadManager.Request request = new android.app.DownloadManager.Request(resource);
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();   //获取文件类型实例
        String mimeString = mimeTypeMap.getMimeTypeFromExtension(MimeTypeMap.getFileExtensionFromUrl(url));   //获取文件类型
        request.setMimeType(mimeString);  //制定下载文件类型
        request.setAllowedNetworkTypes(android.app.DownloadManager.Request.NETWORK_MOBILE | android.app.DownloadManager.Request.NETWORK_WIFI);
        //设置下载中通知栏提示的标题
        request.setTitle(url.substring(url.lastIndexOf("/")+1));
        request.setDescription("下载中...");
        request.setNotificationVisibility(android.app.DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        //表示下载允许的网络类型，默认在任何网络下都允许下载。有NETWORK_MOBILE、NETWORK_WIFI、NETWORK_BLUETOOTH三种及其组合可供选择。
        request.setAllowedNetworkTypes(android.app.DownloadManager.Request.NETWORK_WIFI | android.app.DownloadManager.Request.NETWORK_MOBILE);
        //移动网络情况下是否允许漫游。
        request.setAllowedOverRoaming(false);
        request.setVisibleInDownloadsUi(true);
        request.setDestinationInExternalPublicDir(Constant.downLoadPath,url.substring(url.lastIndexOf("/")));  //制定下载的目录里
        long fileId = downloadManager.enqueue(request);  //开始去下载
        /*将数据保存*/
        PreferencesUtils.putLong(context,url, fileId);
    }


    /**
     * 判断是否需要调用系统下载
     *
     * @param id 调用downloadManager.enqueue(request)时返回的id，that means  an ID for the download, unique across the system. This ID is used to make future calls related to this download
     * @return true if need download，else return false
     */
    private boolean isNeedDownload(String url, long id) {
        android.app.DownloadManager downloadManager = (android.app.DownloadManager) context.getSystemService(Service.DOWNLOAD_SERVICE);
        if (downloadManager == null) {
            return true;
        }
        boolean isNeedDownloadAgain = true;
        android.app.DownloadManager.Query query = new android.app.DownloadManager.Query();
        query.setFilterById(id);
        Cursor cursor = downloadManager.query(query);
        if (cursor != null && cursor.moveToFirst()) {
            int columnStatus = cursor.getColumnIndex(android.app.DownloadManager.COLUMN_STATUS);
            int status = cursor.getInt(columnStatus);
            switch (status) {
                case android.app.DownloadManager.STATUS_FAILED:
                    downloadManager.remove(id);
                    isNeedDownloadAgain = true;  //需要重新下载
                    break;
                case android.app.DownloadManager.STATUS_PAUSED:   //暂停
                    /*去除列表  停止下载*/
                    downloadManager.remove(id);
                    isNeedDownloadAgain = true;
                    break;
                case android.app.DownloadManager.STATUS_PENDING:  //网络状态改变
                	/*去除列表  停止下载*/
                    downloadManager.remove(id);
                    isNeedDownloadAgain = true;
                    break;
                case android.app.DownloadManager.STATUS_RUNNING:
                    isNeedDownloadAgain = false;
                    break;
                case android.app.DownloadManager.STATUS_SUCCESSFUL:
                    if (canDownLoadFile(url)) {  //打不开
                		/*去除列表  停止下载*/
                        downloadManager.remove(id);
                        isNeedDownloadAgain = true;  //需要重新下载
                    } else {  //能打开
                        isNeedDownloadAgain = false;  //不需要重新下载
                    }
                    break;
                default:
                	/*去除列表  停止下载*/
                    downloadManager.remove(id);
                    isNeedDownloadAgain = true;
                    break;
            }
        } else {
            if (canDownLoadFile(url)) {
                isNeedDownloadAgain = true;
            } else {
                isNeedDownloadAgain = false;
            }
        }
        return isNeedDownloadAgain;
    }


    private void deleteFileID(String url) {
        PreferencesUtils.deleteKey(context, url);
    }

    private boolean canDownLoadFile(String url) {
        if (!Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            return false;
        }
        String storedir = Environment.getExternalStorageDirectory() + Constant.downLoadPath;
        File newDoc = new File(storedir);
        if (!newDoc.exists()) {
			/*文件夹路径不存在*/
            newDoc.mkdirs();
        }
        String fileName = url.substring(url.lastIndexOf("/")+1);
        String filePath = storedir + fileName;
        File storeFile = new File(filePath);
        if (!storeFile.exists()) {
			/*文件不存在的情况下*/
            Log.e("file", storeFile.exists() +" "  +  storeFile.getAbsoluteFile());
        } else {
            deleteFileID(url);
            openFile(storeFile);
            return false;
        }
        return true;
    }


    private void openFile(File file) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //设置intent的Action属性
        intent.setAction(Intent.ACTION_VIEW);
        //获取文件file的MIME类型
        String type = MIMETypeUtil.getMIMEType(file);
        //设置intent的data和Type属性。
        intent.setDataAndType(/*uri*/Uri.fromFile(file), type);
        //跳转
        context.startActivity(intent);
    }
}
