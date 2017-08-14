package com.gta.zssx.fun.personalcenter.view.page;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.gta.utils.helper.GlideUtils;
import com.gta.utils.helper.Helper_cache;
import com.gta.utils.resource.Toast;
import com.gta.utils.view.CircleImageView;
import com.gta.utils.widget.image_viewpager.BottomSelectorPopDialog;
import com.gta.zssx.AppConfiguration;
import com.gta.zssx.R;
import com.gta.zssx.fun.personalcenter.model.bean.UserBean;
import com.gta.zssx.fun.personalcenter.presenter.UpdateDataPresenter;
import com.gta.zssx.fun.personalcenter.view.UpdateDataView;
import com.gta.zssx.pub.base.BaseActivity;
import com.gta.zssx.pub.manager.ToolBarManager;
import com.gta.zssx.pub.util.LogUtil;
import com.gta.zssx.pub.util.LoginFailUtil;
import com.tbruyelle.rxpermissions.RxPermissions;


/**
 * Created by lan.zheng on 2016/6/18.
 * 此处使用personal
 */
public class PersonCenterActivity extends BaseActivity<UpdateDataView, UpdateDataPresenter> implements UpdateDataView, View.OnClickListener {

    private RelativeLayout mUpdateLayout;
    private TextView mCacheTv;
    private TextView mVersionNameTv;
    private TextView mUserNameTextView;
    private TextView mUserRealNameTextView;
    private TextView mUserSexTextView;
    private TextView mUserWorkNoTextView;
    private TextView mUserPhoneNumberTextView;
    private TextView mUserLogoutTextView;
    private UserBean mUserBean;
    public Toolbar mToolbar;
    public ToolBarManager mToolBarManager;
    public BottomSelectorPopDialog mBottomSelectorPopDialog;
    public Uri mCameraTempFileUri;
    public AlertDialog mLogoutDialog;
    public AlertDialog.Builder mBuilder;
    //性别信息
    private final static int SEX_MAN = 1;
    private final static int SEX_WOMEN = 2;
    private final static int SEX_DONT_KNOW = 3;
    private final static int SEX_SECRET = 4;
    public CircleImageView mCircleImageView;

    public static void start(Context context) {
        final Intent lIntent = new Intent(context, PersonCenterActivity.class);
        if (!(context instanceof Activity)) {
            lIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(lIntent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_center);
        initView();
        initData();
    }


    private void initView() {
        //初始化标题栏
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolBarManager = new ToolBarManager(mToolbar, this);
        mToolBarManager.init();
        mToolbar.setNavigationOnClickListener(v -> onBackPressed());
        mToolBarManager.showBack(true)
                .setTitle(getResources().getString(R.string.text_personal_center))
                .showRightButton(false)
                .showLeftImage(false);


        mCircleImageView = (CircleImageView) findViewById(R.id.user_image);
        RelativeLayout lRelativeLayout = (RelativeLayout) findViewById(R.id.image_head_fl);
        mUpdateLayout = (RelativeLayout) findViewById(R.id.update_layout);
        mUserNameTextView = (TextView) findViewById(R.id.tv_user_name);
        mVersionNameTv = (TextView) findViewById(R.id.version_name_tv);
        mUserRealNameTextView = (TextView) findViewById(R.id.tv_user_real_name);
        mUserSexTextView = (TextView) findViewById(R.id.tv_user_sex);
        mUserWorkNoTextView = (TextView) findViewById(R.id.tv_user_work_number);
        mUserPhoneNumberTextView = (TextView) findViewById(R.id.tv_user_phone_number);
        mCacheTv = (TextView) findViewById(R.id.cache_tv);
        mUserLogoutTextView = (TextView) findViewById(R.id.tv_user_logout);

        //监听
        mUserLogoutTextView.setOnClickListener(this);       //退出登录监听
        mCircleImageView.setOnClickListener(this);
        lRelativeLayout.setOnClickListener(this);
        mCacheTv.setOnClickListener(this);
        mUpdateLayout.setOnClickListener(this);

    }

    private void initData() {
        try {
            mUserBean = AppConfiguration.getInstance().getUserBean();
            showUserInfo();  //显示缓存的个人信息
            presenter.userInfoUpdate(mUserBean.getUserId());  //去服务器看是否有更新
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            mCacheTv.setText(Helper_cache.getTotalCacheSize(this));
//            ListView listView=new ListView(mActivity);
//            listView.addHeaderView(mCacheTv);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @NonNull
    @Override
    public UpdateDataPresenter createPresenter() {
        return new UpdateDataPresenter();
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.user_image:
                ImageGalleryActivity.show(this, mUserBean.getPhoto());
                break;
            case R.id.image_head_fl:

                //android M runtime permission with Rxjava
                RxPermissions.getInstance(this).request(Manifest.permission.CAMERA,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .subscribe(aBoolean -> {
                            if (aBoolean) {
                                showTakePictureDialog();
                            } else {
                                Toast.Short(PersonCenterActivity.this, R.string.text_please_setting_authority);
                            }
                        }, throwable -> {

                        });
                break;
            case R.id.tv_user_logout: // 退出
                showLogoutDialog();
                break;
            case R.id.cache_tv:
                showClearCacheDialog();
                break;
            case R.id.update_layout:
                RxPermissions.getInstance(this).request(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .subscribe(aBoolean -> {
                            if (aBoolean) {
                                presenter.checkUpdate(this);
                            } else {
                                Toast.Short(PersonCenterActivity.this, R.string.text_please_setting_authority);
                            }
                        }, throwable -> {

                        });
                break;
            default:
                break;
        }
    }

    private void showTakePictureDialog() {
        if (mBottomSelectorPopDialog == null) {
            CharSequence[] actionTexts = new CharSequence[]{"从相机中选择", "拍照"};
            mBottomSelectorPopDialog = new BottomSelectorPopDialog(this, actionTexts, this.getResources().getColor(R.color.main_color));
            mBottomSelectorPopDialog.setListener(new BottomSelectorPopDialog.Listener() {
                @Override
                public void onBtnItemClick(int position) {
                    switch (position) {
                        case 0:
                            //从手机选择
                            startSelectPhotos();
                            break;
                        case 1:
                            //拍照
                            startActionCamera();
                            break;
                        default:
                            break;
                    }
                }

                @Override
                public void onCancelBtnClick() {
                    mBottomSelectorPopDialog.dismiss();
                }
            });
        }
        mBottomSelectorPopDialog.show();
    }

    private void showClearCacheDialog() {
        AlertDialog.Builder lBuilder = new AlertDialog.Builder(this);
        lBuilder.setMessage("确定清除缓存？")
                .setPositiveButton(getString(R.string.text_sure), (dialog, which) -> {
                    try {
                        Helper_cache.clearAllCache(PersonCenterActivity.this);
                    } catch (Exception e) {
                    }
                    Toast.Short(PersonCenterActivity.this, getString(R.string.already_clean));
                    mCacheTv.setText("0.0M");
                })
                .setNegativeButton(getString(R.string.text_cancel), null);
        lBuilder.show();
    }

    private void showLogoutDialog() {
        if (mBuilder == null) {
            mBuilder = new AlertDialog.Builder(this);
            mBuilder.setMessage("确定退出登录？").
                    setNegativeButton(getString(R.string.text_cancel), null)
                    .setPositiveButton(getString(R.string.text_sure), (dialog, which) -> LoginFailUtil.logout(this));
        }
        mBuilder.show();

    }

//    /**
//     * 注销退出
//     */
//    private void logout() {
//        ZSSXApplication.instance.clearTodayTempSche();  //清除日程提醒
//        //这一句防止removeAllCookie在低版本手机导致退出登陆不了
//        CookieSyncManager.createInstance(mActivity);
//        CookieManager manager = CookieManager.getInstance();
//        manager.removeAllCookie();
//        AppConfiguration.getInstance().finishAllActivity();
//        AppConfiguration.getInstance().setUserLoginBean(null).setFirstIn(null).saveAppConfiguration();
//        LoginActivity.start(PersonCenterActivity.this);
//        ZSSXJpushManager.unRegisterJpushManager(false);
//        ZSSXApplication.instance.userBean = null;
//    }

    /**
     * 照片 请求码
     */
    public final static int CAMERA = 100;
    public final static int PICTURE = 101;
    public final static int UPLOADPICTURE = 102;

    /**
     * 从手机中选择
     */
    private void startSelectPhotos() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//		打开相册（方式二）
//      Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//		intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "选择图片"), PICTURE);
    }

    /**
     * 拍照
     */
    private void startActionCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        if (mCameraTempFileUri == null) {
        mCameraTempFileUri = presenter.getCameraTempFile(this);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mCameraTempFileUri);
        startActivityForResult(intent, CAMERA);
    }

    public Uri imgUri = null;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case CAMERA:
                imgUri = mCameraTempFileUri;
                presenter.doCropImage(imgUri, this, UPLOADPICTURE);
                break;
            case PICTURE:
                imgUri = data.getData();
                presenter.doCropImage(imgUri, this, UPLOADPICTURE);
                break;
            case UPLOADPICTURE:
                presenter.userPhotoUpdate(this);  //上传，不需要传Uri,在presenter已经得到
                break;
            default:
                break;
        }
    }

    @Override
    public void checkVersionUpdate(String versionCode) {

    }

    @Override
    public void userPhotoUpdate(UserBean userBean) {
        Toast.Short(PersonCenterActivity.this, getString(R.string.text_update_photo_success));
        String lPhoto = userBean.getPhoto();
        mUserBean.setPhoto(lPhoto);
        GlideUtils.loadUserImage(mActivity, lPhoto, mCircleImageView);
        AppConfiguration.runOnThread(new Runnable() {
            @Override
            public void run() {
                AppConfiguration.getInstance().setUserLoginBean(mUserBean).saveAppConfiguration();
            }
        });
    }

    @Override
    public void userPhotoUpdateFailed() {
//        Toast.Short(PersonCenterActivity.this,getString(R.string.text_update_photo_failed));
    }

    @Override
    public void getUserInfoReturn(boolean isGetInfo) {
        if (!isGetInfo) {
            Toast.Long(PersonCenterActivity.this, getString(R.string.text_update_user_info_fail));
        } else {
            try {
                mUserBean = AppConfiguration.getInstance().getUserBean();
                showUserInfo();  //有更新的进行更新界面
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void showUserInfo() {
        LogUtil.Log("lenita", "showUserInfo()");
        String lPhoto = mUserBean.getPhoto();
        /**
         * 请求头像
         */
        GlideUtils.loadUserImage(mActivity, lPhoto, mCircleImageView);

        mUserNameTextView.setText(mUserBean.getUserName());
        mUserRealNameTextView.setText(mUserBean.getEchoName());
        String sex = "";
        LogUtil.Log("lenita", "sex = " + mUserBean.getSex());
        if (mUserBean.getSex() == SEX_MAN) {
            sex = "男";
        } else if (mUserBean.getSex() == SEX_WOMEN) {
            sex = "女";
        } else if (mUserBean.getSex() == SEX_DONT_KNOW) {
            sex = "未知";
        } else if (mUserBean.getSex() == SEX_SECRET) {
            sex = "保密";
        }
        mUserSexTextView.setText(sex);
        mUserWorkNoTextView.setText(mUserBean.getWorkNo());
        mUserPhoneNumberTextView.setText(mUserBean.getPhone());
        mVersionNameTv.setText(AppConfiguration.getInstance().getAppVersionName(this));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mToolBarManager != null) {
            mToolBarManager.destroy();
        }
    }
}
