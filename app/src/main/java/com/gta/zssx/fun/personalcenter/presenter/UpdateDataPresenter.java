package com.gta.zssx.fun.personalcenter.presenter;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import com.gta.utils.mvp.BasePresenter;
import com.gta.utils.resource.Toast;
import com.gta.zssx.pub.update.DownloadDialog;
import com.gta.zssx.pub.update.UpdataInfo;
import com.gta.zssx.AppConfiguration;
import com.gta.zssx.R;
import com.gta.zssx.fun.personalcenter.model.UpdateDataManager;
import com.gta.zssx.fun.personalcenter.model.bean.UpdateBean;
import com.gta.zssx.fun.personalcenter.model.bean.UserBean;
import com.gta.zssx.fun.personalcenter.view.UpdateDataView;
import com.gta.zssx.pub.exception.CustomException;
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
import rx.Subscriber;
import rx.functions.Action1;

/**
 * Created by lan.zheng on 2016/6/18.
 */
public class UpdateDataPresenter extends BasePresenter<UpdateDataView> {

    public void userPhotoUpdate(Context context) {

        if (!isViewAttached()) {
            return;
        }

        UserBean lUserBean = null;
        try {
            lUserBean = AppConfiguration.getInstance().getUserBean();
        } catch (Exception e) {
            e.printStackTrace();
        }

        LogUtil.Log("lenita","cropUri = "+cropUri.toString());
        if (cropUri == null) {
//            Toast.makeText(context, "裁剪失败，无法上传", Toast.LENGTH_SHORT).show();
            Toast.Short(context,"裁剪失败，无法上传");
            return;
        }
        String lRealFilePath = getRealFilePath(context, cropUri);  //将URL转成真实的路径
        File lFile = new File(lRealFilePath);
        Map<String, RequestBody> lBodyMap = new HashMap<>();
        RequestBody lUserId = RequestBody.create(MediaType.parse("text/plain"), lUserBean.getUserId());
        lBodyMap.put("userId", lUserId);
        RequestBody lPicture = RequestBody.create(MediaType.parse("multipart/form-data"), lFile);
        lBodyMap.put("image\"; filename=\"" + lFile.getName() + "", lPicture);

        getView().showDialog("正在上传...", false);
        mCompositeSubscription.add(UpdateDataManager.uploadAvtor(lBodyMap)
                .subscribe(userBean -> {
                    if(isViewAttached()){
                        getView().userPhotoUpdate(userBean);
                        getView().hideDialog();
                    }
                },throwable -> {
                    if(isViewAttached()){
                        getView().onErrorHandle(throwable);
//                            getView().userPhotoUpdateFailed();
                        getView().hideDialog();
                    }
                }));

    }

    private UserBean mUserBean;

    public void userInfoUpdate(final String userId) {
        if (!isViewAttached())
            return;
        getView().showLoadingDialog();
        mCompositeSubscription.add(UpdateDataManager.getUserInfoUpdate(userId).subscribe(new Action1<UserBean>() {
            @Override
            public void call(UserBean userBean) {
                if (userBean == null) {
                    return;
                }
                try {
                    mUserBean = AppConfiguration.getInstance().getUserBean();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                boolean IsTeacherName = mUserBean.isTeacherName();
                if (String.valueOf(userBean.isTeacherName()) != null) {
                    IsTeacherName = userBean.isTeacherName();
                }
                String UserName = userBean.getUserName();
                String EchoName = userBean.getEchoName();
                int Sex = userBean.getSex();
                String Photo = userBean.getPhoto();
                String Phone = userBean.getPhone();
                String WorkNo = userBean.getWorkNo();
                //set拿到的数据
                mUserBean.setTeacherName(IsTeacherName);
                mUserBean.setUserName(UserName);
                mUserBean.setEchoName(EchoName);
                mUserBean.setSex(Sex);
                mUserBean.setPhoto(Photo);
                mUserBean.setPhone(Phone);
                mUserBean.setWorkNo(WorkNo);
                AppConfiguration.getInstance().setUserLoginBean(mUserBean).saveAppConfiguration();
                if(isViewAttached()){
                    getView().hideDialog();
                    getView().getUserInfoReturn(true);
                }
            }
        }, new Action1<Throwable>() {
            @Override
            public void call(Throwable throwable) {
                if(isViewAttached()){
                    getView().hideDialog();
                    if (throwable instanceof CustomException) {   //抛出自定义的异常
                        CustomException lE = (CustomException) throwable;
                        if (lE.getCode() == CustomException.NETWORK_UNAVAILABLE) {
                            getView().showError(throwable.getMessage());
                        }else {
                            getView().onErrorHandle(throwable);  //抛出默认异常
                        }
                    }else {
                        getView().getUserInfoReturn(false);     //别的未定义的异常
                    }
                }
            }
        }));

    }


    public void checkUpdate(final Context context) {

        if (!isViewAttached())
            return;
        getView().showLoadingDialog();
//        HttpMethod.getInstance().setmRetrofit(null);
        mCompositeSubscription.add(UpdateDataManager.checkUpdate()
                .subscribe(new Subscriber<UpdateBean>() {
                    @Override
                    public void onCompleted() {
                        getView().hideDialog();
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (getView() != null) {
                            getView().onErrorHandle(e);
                            getView().hideDialog();
                        }
                    }

                    @Override
                    public void onNext(UpdateBean updateBean) {
                        UpdataInfo mUpdataInfo = new UpdataInfo();
                        mUpdataInfo.setUrl(updateBean.getFilePath());
                        mUpdataInfo.setServerVersion(updateBean.getVersionCode());
                        try {
                            mUpdataInfo.setUpdateLevel(updateBean.getUpdateLevel());
                            PackageInfo pinfo = context.getPackageManager().getPackageInfo(context.getPackageName()
                                    , PackageManager.GET_CONFIGURATIONS);
                            String versionCode = String.valueOf(pinfo.versionCode);
                            mUpdataInfo.setCurrentVersion(versionCode);

                            if (Integer.parseInt(mUpdataInfo.getServerVersion()) > Integer.parseInt(versionCode)) {
                                mUpdataInfo.setDescription(updateBean.getUpdateMessage());
                                mUpdataInfo.setVersionName(updateBean.getVersionName());
                                DownloadDialog.getInstance(context, mUpdataInfo).isUpdate();
                            } else {
//                                无版本更新
                                Toast.Short(context, "暂无版本更新");
                            }

                        } catch (PackageManager.NameNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                }));
    }

    /**
     * 根据Uri得到真实路径
     *
     * @param context 上下文
     * @param uri     绝对路径
     * @return 真实路径
     */
    public String getRealFilePath(final Context context, final Uri uri) {
        if (null == uri) return null;
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

    public Uri cropUri;  //需要裁剪的Uri
//    private Context mContext;

    /**
     * 裁剪头像的绝对路径
     */
    public Uri getUploadTempFile(Context context, Uri uri) {
        String storageState = Environment.getExternalStorageState();
        if (storageState.equals(Environment.MEDIA_MOUNTED)) {
            File savedir = new File(StringUtils.IMAGE_SAVE_PATH);
            if (!savedir.exists()) {
                savedir.mkdirs();
            }
        } else {
            if(storageState.equals(Environment.MEDIA_REMOVED)){
                //先判断有没有内存卡路径，没有的话存在别的地方
                if(savePath.equals(StringUtils.IMAGE_SAVE_PATH)){
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
            }else {
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

    String savePath = StringUtils.IMAGE_SAVE_PATH;  //默认是这个，如果是另一种情况可能不存在这个路径
//    String savePath = AppConfiguration.DEFAULT_SAVE_IMAGE_PATH;  //默认是这个，如果是另一种情况可能不存在这个路径
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
            if(storageState.equals(Environment.MEDIA_REMOVED)) {
                //TODO remove的话先去获取能存储的路径
                String path = getExternalPath();
                if(path.equals("null")){
                    Toast.Long(mContext, R.string.text_save_photo_failed);
                    return null;
                }
                savePath = path+ "/ZSSX1_picture";
                File savedir = new File(savePath);
                if (!savedir.exists()) {
                    savedir.mkdir();
                    LogUtil.Log("lenita", "savedir.mkdir() = " + savedir.exists());
                }
            }else {
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
    private static String getExternalPath(){
        File file2 = new File("/etc/vold.fstab");
        ArrayList<String> strings = new ArrayList<>();
        InputStream instream = null;
        try {
            instream = new FileInputStream(file2);
            if (instream != null)
            {
                InputStreamReader inputReader = new InputStreamReader(instream);
                BufferedReader buffReader = new BufferedReader(inputReader);
                String line;
                //分行读取
                while (( line = buffReader.readLine()) != null) {
                    LogUtil.Log("test11","line = "+line);
                    strings.add(line);
                }
                instream.close();

            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        }

        String path = "null";  //默认没有路径
        if(strings.size() > 0){
            ArrayList<String> out = new ArrayList<>();
            for (int i = 0; i < strings.size(); i++) {
                String f = strings.get(i);
                String f1 = "/storage/sdcard0";
                String f2 = "/storage/emmc";
                if(f.contains("dev_mount")) {
                    if (f.contains("emmc")) {
                        if (new File(f2).exists()) {
                            out.add(f2);
                        }
                    }else if(f.contains("sdcard0")){
                        if (new File(f1).exists()) {
                            out.add(f1);
                        }
                    }
                }
            }
            if (out.size() > 0){
                LogUtil.Log("test11","size = "+out.size());
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
     *
     * @param uri         绝对路径
     * @param requestCode 请求码
     * @param context     上下文
     */
    private Intent mIntent;

    public void doCropImage(Uri uri, Context context, int requestCode) {
        try {
            mIntent = new Intent("com.android.camera.action.CROP");
//            mIntent.setDataAndType(uri, "image/*");
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                String url = ImageUtils.getPath(context, uri);
                mIntent.setDataAndType(Uri.fromFile(new File(url)), "image/*");
            } else {
                mIntent.setDataAndType(uri, "image/*");
            }

            cropUri = null;
            Uri uri1 = getUploadTempFile(context, uri);
            LogUtil.Log("lenita","uri1 e = "+uri1.toString());
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
            LogUtil.Log("lenita","Exception e = "+e.toString());
            Toast.Short(context, "裁剪失败，无法上传");
        }
    }
}
