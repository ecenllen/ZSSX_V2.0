package com.gta.zssx.fun.personalcenter.deprecated;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.FutureTarget;
import com.bumptech.glide.request.target.Target;
import com.gta.utils.resource.Toast;
import com.gta.zssx.AppConfiguration;
import com.gta.zssx.R;
import com.gta.zssx.fun.personalcenter.view.page.ImagePreviewView;
import com.gta.zssx.fun.personalcenter.view.page.PreviewerViewPager;
import com.tbruyelle.rxpermissions.RxPermissions;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;


/**
 * [Description]
 * <p>
 * [How to use]
 * <p>
 * [Tips]
 *
 * @author Created by Zhimin.Huang on 2017/3/24.
 * @since 1.0.0
 */
@Deprecated
public class ImageGalleryActivityV2 extends AppCompatActivity implements ViewPager.OnPageChangeListener, View.OnClickListener {


    public static final String KEY_IMAGE = "images";
    public static final String KEY_POSITION = "position";
    public static final String KEY_NEED_SAVE = "save";
    PreviewerViewPager mImagePager;
    TextView mIndexText;
    private String[] mImageSources;
    private int mCurPosition;
    private boolean mNeedSaveLocal;
    public ImageView mSave;

    public static void show(Context context, String images) {
        show(context, images, true);
    }

    public static void show(Context context, String images, boolean needSaveLocal) {
        if (images == null){
            images = "";
            needSaveLocal = false;
        }
        show(context, new String[]{images}, 0, needSaveLocal);
    }


    public static void show(Context context, String[] images, int position) {
        show(context, images, position, true);
    }

    public static void show(Context context, String[] images, int position, boolean needSaveLocal) {
        Intent intent = new Intent(context, ImageGalleryActivityV2.class);
        intent.putExtra(KEY_IMAGE, images);
        intent.putExtra(KEY_POSITION, position);
        intent.putExtra(KEY_NEED_SAVE, needSaveLocal);
        context.startActivity(intent);
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_gallery);
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        initView();
    }

    private void initView() {

        mImageSources = getIntent().getStringArrayExtra(KEY_IMAGE);
        mCurPosition = getIntent().getIntExtra(KEY_POSITION, 0);
        mNeedSaveLocal = getIntent().getBooleanExtra(KEY_NEED_SAVE, true);

        mImagePager = (PreviewerViewPager) findViewById(R.id.vp_image);
        mIndexText = (TextView) findViewById(R.id.tv_index);

        mSave = (ImageView) findViewById(R.id.iv_save);
        mSave.setVisibility(mNeedSaveLocal?View.VISIBLE:View.GONE);
        assert mSave != null;
        mSave.setOnClickListener(this);
        mImagePager.addOnPageChangeListener(this);
        mImagePager.setAdapter(new ImageGalleryActivityV2.ViewPagerAdapter(getSupportFragmentManager(), Arrays.asList(mImageSources)));
        mImagePager.setCurrentItem(mCurPosition);

        int len = mImageSources.length;
        if (mCurPosition < 0 || mCurPosition >= len)
            mCurPosition = 0;

        // If only one, we not need the text to show
        if (len == 1)
            mIndexText.setVisibility(View.GONE);


    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        mCurPosition = position;
        mIndexText.setText(String.format("%s/%s", (position + 1), mImageSources.length));

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_save:
                RxPermissions.getInstance(this).request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .subscribe(new Subscriber<Boolean>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onNext(Boolean aBoolean) {
                                if (aBoolean) {
                                    saveToFile();
                                } else {
                                    Toast.Short(ImageGalleryActivityV2.this, "请授予保存图片权限");
                                }
                            }
                        });
                break;
            default:
        }
    }

    private class ViewPagerAdapter extends FragmentPagerAdapter {

        private List<String> mDatas;

        public ViewPagerAdapter(FragmentManager fm, List<String> urls) {
            super(fm);
            mDatas = urls;
        }

        @Override
        public Fragment getItem(int position) {
            String lUrl = mDatas.get(position);
            return ImageGalleryFragmentV2.newInstance(lUrl);
        }

        @Override
        public int getCount() {
            return mDatas.size();
        }
    }

    private class ViewPagerAdapterV2 extends PagerAdapter implements ImagePreviewView.OnReachBorderListener {

        private View.OnClickListener mFinishClickListener;

        @Override
        public int getCount() {
            return mImageSources.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==object;
        }

        @Override
        public void onReachBorder(boolean isReached) {

        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            return super.instantiateItem(container, position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            super.destroyItem(container, position, object);
        }
    }

    /**
     * 拷贝文件
     * 如果目标文件不存在将会自动创建
     *
     * @param srcFile  原文件
     * @param saveFile 目标文件
     * @return 是否拷贝成功
     */
    public static boolean copyFile(final File srcFile, final File saveFile) {
        File parentFile = saveFile.getParentFile();
        if (!parentFile.exists()) {
            if (!parentFile.mkdirs())
                return false;
        }

        BufferedInputStream inputStream = null;
        BufferedOutputStream outputStream = null;
        try {
            inputStream = new BufferedInputStream(new FileInputStream(srcFile));
            outputStream = new BufferedOutputStream(new FileOutputStream(saveFile));
            byte[] buffer = new byte[1024 * 4];
            int len;
            while ((len = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, len);
            }
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    /**
     * 获取图片的真实后缀
     *
     * @param filePath 图片存储地址
     * @return 图片类型后缀
     */
    public static String getExtension(String filePath) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);
        String mimeType = options.outMimeType;
        return mimeType.substring(mimeType.lastIndexOf("/") + 1);
    }

    private void callSaveStatus(final boolean success, final File savePath) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (success) {
                    // notify
                    Uri uri = Uri.fromFile(savePath);
                    sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
                    android.widget.Toast.makeText(ImageGalleryActivityV2.this, "保存成功", android.widget.Toast.LENGTH_SHORT).show();
                } else {
                    android.widget.Toast.makeText(ImageGalleryActivityV2.this, "保存失败", android.widget.Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void saveToFile() {
        String path = mImageSources[mCurPosition];
        FutureTarget<File> lTarget = Glide.with(this)
                .load(path)
                .downloadOnly(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL);
        Observable.just(lTarget)
                .observeOn(Schedulers.io())
                .flatMap(new Func1<FutureTarget<File>, Observable<Boolean>>() {
                    @Override
                    public Observable<Boolean> call(FutureTarget<File> fileFutureTarget) {
                        boolean isSuccess = false;
                        try {
                            File sourceFile = fileFutureTarget.get();
                            String lExtension = getExtension(sourceFile.getAbsolutePath());
                            String extDir = AppConfiguration.DEFAULT_SAVE_IMAGE_PATH;
                            File extDirFile = new File(extDir);
                            if (!extDirFile.exists()) {
                                if (!extDirFile.mkdirs()) {
                                    // If mk dir error
                                    callSaveStatus(false, null);
                                    Observable.error(new Throwable());
                                }
                            }
                            final File saveFile = new File(extDirFile, String.format("IMG_%s.%s", System.currentTimeMillis(), lExtension));
                            isSuccess = copyFile(sourceFile, saveFile);
                            callSaveStatus(isSuccess, saveFile);
                        } catch (Exception e) {
                            e.printStackTrace();
                            callSaveStatus(false, null);
                        }
                        return Observable.just(isSuccess);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Boolean aBoolean) {

                    }
                });
    }

}
