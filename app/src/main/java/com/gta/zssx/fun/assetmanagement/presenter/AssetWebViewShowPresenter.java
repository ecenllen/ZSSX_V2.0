package com.gta.zssx.fun.assetmanagement.presenter;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import com.blankj.utilcode.util.EncryptUtils;
import com.gta.utils.mvp.BasePresenter;
import com.gta.utils.resource.Toast;
import com.gta.zssx.R;
import com.gta.zssx.ZSSXApplication;
import com.gta.zssx.fun.assetmanagement.model.AssetManagerInterface;
import com.gta.zssx.fun.assetmanagement.model.bean.ApprovalDetailBean;
import com.gta.zssx.fun.assetmanagement.model.bean.BatchAddNotesBean;
import com.gta.zssx.fun.assetmanagement.model.bean.UpLoadRemarksAssetBean;
import com.gta.zssx.fun.assetmanagement.view.AssetWebViewShowView;
import com.gta.zssx.fun.personalcenter.model.LoginDataManager;
import com.gta.zssx.fun.personalcenter.model.bean.UserBean;
import com.gta.zssx.pub.util.AdvancedFileUtils;
import com.gta.zssx.pub.util.ImageUtils;
import com.gta.zssx.pub.util.LogUtil;
import com.gta.zssx.pub.util.StringUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Func1;

/**
 * Created by lan.zheng on 2016/8/9.
 */
public class AssetWebViewShowPresenter extends BasePresenter<AssetWebViewShowView> {
    String savePath = StringUtils.IMAGE_SAVE_PATH;  //默认是这个，如果是另一种情况可能不存在这个路径
    public Uri cropUri;  //需要裁剪的Uri
    public Subscription mSubscribeList;
    private UserBean mUserBean;

    public AssetWebViewShowPresenter() {
        mUserBean = ZSSXApplication.instance.getUser();
    }

    public UserBean getmUserBean() {
        return mUserBean;
    }

    /**
     * 获取审批详情数据
     * @param approvalId 审批ID,PC openApprovalDetailWindow 方法提供
     */
    public void getApprovalDetailInfo(int approvalId) {
        if (getView() == null)
            return;
        getView().showLoadingDialog();
        mCompositeSubscription.add(AssetManagerInterface.getApprovalDetailInfo(approvalId)
                .subscribe(new Subscriber<ApprovalDetailBean>() {
                    @Override
                    public void onCompleted() {
                        if(getView() == null)
                            return;
                        getView().hideDialog();
                    }

                    @Override
                    public void onError(Throwable e) {
                        if(getView() == null)
                            return;
                        getView().showError(e.getMessage());
                        getView().hideDialog();
                    }

                    @Override
                    public void onNext(ApprovalDetailBean approvalDetailBean) {
                        if(getView() == null)
                            return;
                        getView().GetApprovalDetailInfo(approvalDetailBean);
                    }
                }));
    }
    
    /**
     * 获取ticket 值
     */
    public void getTicket() {
        if (getView() == null)
            return;
//        String lMD5password = EncryptUtils.encryptMD5ToString(mUserBean.getMD5Password() + "{000000}");
//        String lPowerHostUrl = mUserBean.getPowerHostUrl() + "SignIn/";
        getView().showLoadingDialog();
        // 获取ticket 值
        mCompositeSubscription.add(LoginDataManager.getTicket(mUserBean.getUserName().toUpperCase(), mUserBean.getMD5Password(), mUserBean.getPowerHostUrl(), mUserBean.getTicket())
                .flatMap(new Func1<String, Observable<String>>() {
                    @Override
                    public rx.Observable<String> call(String ticketBean) {
                        return rx.Observable.just(ticketBean);
                    }
                }).subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {
                        if(getView() != null) {
                            getView().hideDialog();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (getView() == null)
                            return;
                        getView().hideDialog();
                        getView().onErrorHandle(e);
                    }

                    @Override
                    public void onNext(String s) {
                        if (getView() == null)
                            return;
                        mUserBean.setTicket(s);
                        getView().onSuccessTicket(s);
                    }
                }));
    }

    public void userPhotoUpdate(Context context) {

        if (!isViewAttached()) {
            return;
        }

        LogUtil.Log("lenita", "cropUri = " + cropUri.toString());
        if (cropUri == null) {
            //            Toast.makeText(context, "裁剪失败，无法上传", Toast.LENGTH_SHORT).show();
            Toast.Short(context, "裁剪失败，无法上传");
            return;
        }
        String lRealFilePath = getRealFilePath(context, cropUri);  //将URL转成真实的路径
        File lFile = new File(lRealFilePath);
        Map<String, RequestBody> lBodyMap = new HashMap<>();
        RequestBody lUserId = RequestBody.create(MediaType.parse("text/plain"), mUserBean.getUserId());
        RequestBody lAssetsId = RequestBody.create(MediaType.parse("text/plain"), getView().getmId() + "");
        lBodyMap.put("userId", lUserId);
        lBodyMap.put("assetId", lAssetsId);
        RequestBody lPicture = RequestBody.create(MediaType.parse("multipart/form-data"), lFile);
        lBodyMap.put("image\"; filename=\"" + lFile.getName() + "", lPicture);

        getView().showDialog("正在上传...", false);
        mCompositeSubscription.add(AssetManagerInterface.mobileUploadAssetFile(lBodyMap)
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {
                        if(getView() != null) {
                            getView().hideDialog();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if(getView() != null) {
                            getView().hideDialog();
                            getView().onErrorHandle(e);
                        }
                    }

                    @Override
                    public void onNext(String s) {
                        if(getView() != null) {
                            getView().showPicture(s);
                        }
                    }
                }));

    }

    /**
     * 拍照保存的绝对路径,当点击拍照的时候，不能存在系统默认的路径，有可能无法访问
     */
    public Uri getCameraTempFile(Context mContext) {
        String storageState = Environment.getExternalStorageState();
        LogUtil.Log("lenita", storageState);
        if (storageState.equals(Environment.MEDIA_MOUNTED)) {
            File savedir = new File(savePath);
            if (!savedir.exists()) {
                savedir.mkdir();
                LogUtil.Log("lenita", "savedir.mkdir() = " + savedir.exists());
            }
        } else {
            if (storageState.equals(Environment.MEDIA_REMOVED)) {
                //TODO remove的话先去获取能存储的路径
                String path = getExternalPath();
                if (path.equals("null")) {
                    Toast.Long(mContext, R.string.text_save_photo_failed);
                    return null;
                }
                savePath = path + "/ZSSX1_picture";
                File savedir = new File(savePath);
                if (!savedir.exists()) {
                    savedir.mkdir();
                    LogUtil.Log("lenita", "savedir.mkdir() = " + savedir.exists());
                }
            } else {
                Toast.Long(mContext, R.string.text_save_photo_failed);
                return null;
            }

        }
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss", Locale.CHINA)
                .format(new Date());
        // 照片命名
        String cropFileName = "virtual" + StringUtils.USER_ID
                + "_" + timeStamp + ".jpg";
        LogUtil.Log("lenita", "cropFileName = " + cropFileName);

        // 裁剪头像的绝对路径
        File lImageSaveFile = new File(savePath, cropFileName);
        Uri tempUri = Uri.fromFile(lImageSaveFile);
        return tempUri;
    }

    /**
     * 当Environment.MEDIA_REMOVED时使用该段代码
     */
    private static String getExternalPath() {
        File file2 = new File("/etc/vold.fstab");
        ArrayList<String> strings = new ArrayList<>();
        InputStream instream = null;
        try {
            instream = new FileInputStream(file2);
            if (instream != null) {
                InputStreamReader inputReader = new InputStreamReader(instream);
                BufferedReader buffReader = new BufferedReader(inputReader);
                String line;
                //分行读取
                while ((line = buffReader.readLine()) != null) {
                    LogUtil.Log("test11", "line = " + line);
                    strings.add(line);
                }
                instream.close();

            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String path = "null";  //默认没有路径
        if (strings.size() > 0) {
            ArrayList<String> out = new ArrayList<>();
            for (int i = 0; i < strings.size(); i++) {
                String f = strings.get(i);
                String f1 = "/storage/sdcard0";
                String f2 = "/storage/emmc";
                if (f.contains("dev_mount")) {
                    if (f.contains("emmc")) {
                        if (new File(f2).exists()) {
                            out.add(f2);
                        }
                    } else if (f.contains("sdcard0")) {
                        if (new File(f1).exists()) {
                            out.add(f1);
                        }
                    }
                }
            }
            if (out.size() > 0) {
                LogUtil.Log("test11", "size = " + out.size());
                for (String devMount : out) {
                    File file = new File(devMount);
                    if (file.isDirectory() && file.canWrite()) {
                        path = file.getAbsolutePath();
                    }
                }
            }
        }
        return path;
    }

    /**
     * 裁剪图片
     * 保存原图和剪裁图
     */
    public void doCropImage(Uri uri, Context context, int requestCode) {
        try {
            Intent mIntent = new Intent("com.android.camera.action.CROP");
            //            mIntent.setDataAndType(uri, "image/*");
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                String url = ImageUtils.getPath(context, uri);
                mIntent.setDataAndType(Uri.fromFile(new File(url)), "image/*");
            } else {
                mIntent.setDataAndType(uri, "image/*");
            }

            cropUri = null;
            Uri uri1 = getUploadTempFile(context, uri);
            LogUtil.Log("lenita", "uri1 e = " + uri1.toString());
            mIntent.putExtra("crop", "true");
            mIntent.putExtra("scale", true);// 去黑边
            mIntent.putExtra("scaleUpIfNeeded", true);// 去黑边
            mIntent.putExtra("aspectX", 1);   // 设置裁剪的宽、高比例为9：9
            mIntent.putExtra("aspectY", 1);
            mIntent.putExtra("outputX", 300); // outputX，outputY是裁剪的宽、高度
            mIntent.putExtra("outputY", 300);
            mIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri1);
            mIntent.putExtra("return-data", false); //若为false则表示不返回数据
            //            mIntent.putExtra(MediaStore.EXTRA_OUTPUT,uri);
            //            mIntent.putExtra("return-data", false);
            mIntent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
            mIntent.putExtra("noFaceDetection", true); // 关闭人脸检测
            ((Activity) context).startActivityForResult(mIntent, requestCode);
        } catch (Exception e) {
            LogUtil.Log("lenita", "Exception e = " + e.toString());
            Toast.Short(context, "裁剪失败，无法上传");
        }
    }

    /**
     * 裁剪头像的绝对路径
     */
    private Uri getUploadTempFile(Context context, Uri uri) {
        String storageState = Environment.getExternalStorageState();
        if (storageState.equals(Environment.MEDIA_MOUNTED)) {
            File savedir = new File(StringUtils.IMAGE_SAVE_PATH);
            if (!savedir.exists()) {
                savedir.mkdirs();
            }
        } else {
            if (storageState.equals(Environment.MEDIA_REMOVED)) {
                //先判断有没有内存卡路径，没有的话存在别的地方
                if (savePath.equals(StringUtils.IMAGE_SAVE_PATH)) {
                    File savedir = new File(savePath);
                    if (!savedir.exists()) {
                        savedir.mkdirs();
                        savePath = "/storage/emmc/ZSSX1_picture";
                    }
                }
                File savedir = new File(savePath);
                if (!savedir.exists()) {
                    savedir.mkdirs();
                }
            } else {
                Toast.Long(context, R.string.text_save_photo_failed);
                return null;
            }

        }
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss", Locale.CHINA).format(new Date());
        String thePath = ImageUtils.getAbsolutePathFromNoStandardUri(uri);

        // 如果是标准Uri
        if (StringUtils.isEmpty(thePath)) {
            thePath = ImageUtils.getAbsoluteImagePath((Activity) context, uri);
        }
        String ext = AdvancedFileUtils.getFileFormat(thePath);
        ext = StringUtils.isEmpty(ext) ? "jpg" : ext;

        // 照片命名
        String cropFileName = "virtual" + StringUtils.USER_ID
                + "_" + timeStamp + "." + ext;
        // copyProtraitName = cropFileName;

        // 裁剪头像的绝对路径
        // protraitPath = IMAGE_SAVE_PATH + cropFileName;

        File lImageSaveFile = new File(savePath, cropFileName);
        Uri cropUri1 = Uri.fromFile(lImageSaveFile);
        cropUri = cropUri1;
        return cropUri1;
    }

    /**
     * 根据Uri得到真实路径
     *
     * @param context 上下文
     * @param uri     绝对路径
     * @return 真实路径
     */
    private String getRealFilePath(final Context context, final Uri uri) {
        if (null == uri)
            return null;
        final String scheme = uri.getScheme();
        String data = null;
        if (scheme == null)
            data = uri.getPath();
        else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = context.getContentResolver().query(uri, new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return data;
    }

    public void upLoadAssetData(UpLoadRemarksAssetBean upLoadRemarksAssetBean) {
        if (!isViewAttached()) {
            return;
        }   
        if(getView() != null)
            getView().showDialog("正在保存...");

        mSubscribeList = AssetManagerInterface.upLoadRemarksAsset(upLoadRemarksAssetBean)
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        getView().hideDialog();
                        getView().onErrorHandle(e);
                    }

                    @Override
                    public void onNext(String s) {
                        getView().hideDialog();
                        //                        if (dismissDialog) {
                        //                            getView ().hideDismissDialog ();
                        //                        }
                        getView().upLoadRemark();
                    }
                });
        mCompositeSubscription.add(mSubscribeList);

    }

    public void BatchAddNotes(BatchAddNotesBean batchAddNotesBean) {
        if (!isViewAttached()) {
            return;
        }
        if(getView() != null)
            getView().showDialog("正在保存...");

        mSubscribeList = AssetManagerInterface.batchAddNotes(batchAddNotesBean)
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        getView().hideDialog();
                        getView().onErrorHandle(e);
                    }

                    @Override
                    public void onNext(String s) {
                        getView().hideDialog();
                        getView().uploadRemarks();
                    }
                });
        mCompositeSubscription.add(mSubscribeList);
    }
}
