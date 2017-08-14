package com.gta.zssx.pub.manager;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.gta.utils.helper.Helper_Drawable;
import com.gta.zssx.R;


/**
 * [Description]
 * <p>
 * [How to use]
 * <p>
 * [Tips]
 *
 * @author Created by zhimin.Huang on 2016/6/7.
 * @since 1.0.0
 */
public class ToolBarManager implements Parcelable {

    private TextView mLeftButton;
    private TextView mScheduleDate;
    private TextView mScheduleTitle;
    private LinearLayout mScheduleLayout;
    private Toolbar mToolbar;
    private Context mContext;
    private boolean mToolbarShow;
    private static final long HEADER_HIDE_ANIM_DURATION = 1000;
    private TextView mSave;
    private TextView mRightButton;
    private ImageView mLeftImage, ivTime, ivAddClass, ivSearch;
    private RadioButton mRightImage;



    protected ToolBarManager (Parcel in) {

    }

    public static final Creator<ToolBarManager> CREATOR = new Creator<ToolBarManager> () {
        @Override
        public ToolBarManager createFromParcel (Parcel in) {
            return new ToolBarManager (in);
        }

        @Override
        public ToolBarManager[] newArray (int size) {
            return new ToolBarManager[size];
        }
    };

    @Override
    public int describeContents () {
        return 0;
    }

    @Override
    public void writeToParcel (Parcel dest, int flags) {
    }


    //标题
    private TextView mTitleTv;
    /**
     * 二级标题
     */
    private TextView mSubTitleTv;
    /**
     * 小标题，在课堂教学反馈中用到
     */
    private TextView mSmallTitleTv;

    public ToolBarManager (Toolbar toolbar, Context context) {
        mToolbar = toolbar;
        mContext = context;
        mTitleTv = (TextView) mToolbar.findViewById (R.id.title);
        mSubTitleTv = (TextView) mToolbar.findViewById (R.id.sub_title);
        mSmallTitleTv = (TextView) mToolbar.findViewById (R.id.title_small);
        mScheduleTitle = (TextView) mToolbar.findViewById (R.id.schedule_title);
        mScheduleDate = (TextView) mToolbar.findViewById (R.id.date_tv);
        mSave = (TextView) mToolbar.findViewById (R.id.save);
        ivTime = (ImageView) mToolbar.findViewById (R.id.iv_ic_time);
        ivAddClass = (ImageView) mToolbar.findViewById (R.id.iv_ic_add);
        ivSearch = (ImageView) mToolbar.findViewById (R.id.iv_search);
        mRightButton = (TextView) mToolbar.findViewById (R.id.right_button);
        mLeftButton = (TextView) mToolbar.findViewById (R.id.left_text_tv);
        mLeftImage = (ImageView) mToolbar.findViewById (R.id.left_image);
        mScheduleLayout = (LinearLayout) mToolbar.findViewById (R.id.schedule_layout);
        mRightImage = (RadioButton) mToolbar.findViewById (R.id.right_image);


    }

    public ToolBarManager destroy () {
        mToolbar = null;
        mContext = null;
        return this;
    }

    public ToolBarManager init () {
        //设置toolbar
        mToolbar.setTitleTextColor (mContext.getResources ().getColor (android.R.color.white));
        renew ();
        return this;
    }

    public ToolBarManager renew () {

        mToolbar.setNavigationIcon (R.drawable.ic_left_arrow_white);

        if (mSave != null) {

            showSave (false);
        }

        //防止fragment被销毁后，右边按钮被隐藏
        //        showRightButton(false);
        if (mLeftImage != null) {

            showLeftImage (false);
        }
        if (ivAddClass != null) {

            showRightIcAddClass (false);
        }
        if (ivTime != null) {

            showRightIcTime (false);
        }
        if (ivSearch != null) {

            showIvSearch (false);
        }
        if (mRightImage != null) {

            showRightImage (false);
        }

        if (mScheduleLayout != null) {
            showScheduleLayout (false);
        }

        if (mLeftButton != null) {
            showLeftButton (false);
        }

        if (mSubTitleTv != null)
            showSubTitle (false);

        return this;
    }

    public ToolBarManager showSubTitle (boolean b) {
        if (mSubTitleTv != null)
            mSubTitleTv.setVisibility (b ? View.VISIBLE : View.GONE);
        return this;
    }

    public ToolBarManager setSubTitleTv (int resId) {
        setSubTitleTv (mContext.getString (resId));
        return this;
    }

    public ToolBarManager setSubTitleTv (String subTitle) {
        if (mSubTitleTv != null && !TextUtils.isEmpty (subTitle)) {
            mSubTitleTv.setText (subTitle);
            showSubTitle (true);
        }
        return this;
    }

    public ToolBarManager setTitleClickListener(View.OnClickListener listener) {
        if(mTitleTv != null)
            mTitleTv.setOnClickListener(listener);
        return this;
    }

    public ToolBarManager showScheduleLayout (boolean isShow) {
        mScheduleLayout.setVisibility (isShow ? View.VISIBLE : View.GONE);
        return this;
    }


    public Toolbar getToolbar () {
        return mToolbar;
    }

    public ToolBarManager setTitle (int titleResId) {
        return setTitle (mContext.getString (titleResId));
    }


    public ToolBarManager setTitle (@Nullable String title) {
        if (title == null) {
            mTitleTv.setVisibility (View.GONE);
        } else {
            mTitleTv.setVisibility (View.VISIBLE);
            mTitleTv.setText (title);
        }
        return this;
    }

    public ToolBarManager setScheduleTitle (String title) {
        if (title == null) {
            mScheduleTitle.setVisibility (View.GONE);
        } else {
            mScheduleTitle.setVisibility (View.VISIBLE);
            mScheduleTitle.setText (title);
        }
        return this;
    }

    public ToolBarManager setScheduleDate (String date) {
        Drawable lDrawable = Helper_Drawable.tintDrawable (ContextCompat.getDrawable (mContext, R.drawable.group_down), ColorStateList.valueOf (Color.WHITE));
        lDrawable.setBounds (0, 0, lDrawable.getMinimumWidth (), lDrawable.getMinimumHeight ());
        mScheduleDate.setCompoundDrawables (null, null, lDrawable, null);
        if (date == null) {
            mScheduleDate.setVisibility (View.GONE);
        } else {
            mScheduleDate.setVisibility (View.VISIBLE);
            mScheduleDate.setText (date);
        }
        return this;
    }

    public ToolBarManager clickSchedule (View.OnClickListener clickListener) {
        mScheduleDate.setOnClickListener (clickListener);
        return this;
    }

    public TextView getRightButton () {
        return mRightButton;
    }

    public ImageView getLeftImage () {
        return mLeftImage;
    }

    public ToolBarManager showTitle (boolean isShow) {
        mTitleTv.setVisibility (isShow ? View.VISIBLE : View.GONE);
        return this;
    }

    public ToolBarManager showSave (boolean isShow) {
        mSave.setVisibility (isShow ? View.VISIBLE : View.GONE);
        return this;
    }

    public ToolBarManager showRightButton (boolean isShow) {
        mRightButton.setVisibility (isShow ? View.VISIBLE : View.GONE);
        return this;
    }

    public ToolBarManager showLeftButton (boolean isShow) {
        mLeftButton.setVisibility (isShow ? View.VISIBLE : View.GONE);
        return this;
    }

    public ToolBarManager showLeftImage (boolean isShow) {
        mLeftImage.setVisibility (isShow ? View.VISIBLE : View.GONE);
        return this;
    }

    public ToolBarManager showRightImage (boolean isShow) {
        mRightImage.setVisibility (isShow ? View.VISIBLE : View.GONE);
        return this;
    }

    public ToolBarManager setLeftImage (int imgResId) {
        mLeftImage.setImageResource (imgResId);
        return this;
    }

    public ToolBarManager setRightImage (int imgResId) {
        //        mRightImage.setImageResource(imgResId);
        Drawable lDrawable = ContextCompat.getDrawable (mContext, imgResId);
        lDrawable.setBounds (0, 0, lDrawable.getMinimumWidth (), lDrawable.getMinimumHeight ());
        mRightImage.setCompoundDrawables (null, null, lDrawable, null);

        return this;
    }

    public ToolBarManager setRightImage (Drawable drawable) {
        //        mRightImage.setImageDrawable(drawable);
        drawable.setBounds (0, 0, drawable.getMinimumWidth (), drawable.getMinimumHeight ());
        mRightImage.setCompoundDrawables (null, null, drawable, null);
        return this;
    }

    public ToolBarManager showRightIcTime (boolean isShow) {
        ivTime.setVisibility (isShow ? View.VISIBLE : View.GONE);
        return this;
    }

    public ToolBarManager showIvSearch (boolean isShow) {
        ivSearch.setVisibility (isShow ? View.VISIBLE : View.GONE);
        return this;
    }

    public ToolBarManager showRightIcAddClass (boolean isShow) {
        ivAddClass.setVisibility (isShow ? View.VISIBLE : View.GONE);
        return this;
    }

    public ToolBarManager clickLeftImage (View.OnClickListener clickListener) {
        mLeftImage.setOnClickListener (clickListener);
        return this;
    }

    public ToolBarManager clickIvSearch (View.OnClickListener clickListener) {
        ivSearch.setOnClickListener (clickListener);
        return this;
    }

    public ToolBarManager clickRightImage (View.OnClickListener clickListener) {
        mRightImage.setOnClickListener (clickListener);
        mRightImage.setChecked (false);
        return this;
    }

    public ToolBarManager setRightText (String rightText) {
        if (mRightButton != null) {
            if(!rightText.isEmpty() && rightText.length() >= 4) {
                mRightButton.setTextSize(14);
            }
            mRightButton.setText (rightText);
        }
        return this;
    }

    public ToolBarManager setLeftText (String rightText) {
        if (mLeftButton != null) {
            mLeftButton.setText (rightText);
        }
        return this;
    }

    public ToolBarManager setmSaveText (String mSaveText) {
        if (mSave != null) {
            mSave.setText (mSaveText);
        }
        return this;
    }

    public ToolBarManager setRightText (int stingResId) {
        return setRightText (mContext.getString (stingResId));
    }


    public ToolBarManager clickSave (View.OnClickListener clickListener) {
        mSave.setOnClickListener (clickListener);
        return this;
    }

    public ToolBarManager clickTime (View.OnClickListener listener) {
        ivTime.setOnClickListener (listener);
        return this;
    }

    public ToolBarManager clickRightButton (View.OnClickListener clickListener) {
        mRightButton.setOnClickListener (clickListener);
        return this;
    }

    public ToolBarManager clickLeftButton (View.OnClickListener clickListener) {
        mLeftButton.setOnClickListener (clickListener);
        return this;
    }

    public ToolBarManager setSamllTitle (boolean visible, String title) {
        if (visible) {
            mSmallTitleTv.setVisibility (View.VISIBLE);
            mSmallTitleTv.setText (title);
        } else {
            mSmallTitleTv.setVisibility (View.GONE);
        }
        return this;
    }

    public ToolBarManager showBack (boolean show) {
        if (show) {
            mToolbar.setNavigationIcon (R.drawable.ic_left_arrow_white);
        } else {
            mToolbar.setNavigationIcon (null);
        }
        return this;
    }

    /**
     * 显示或隐藏toolbar，带效果
     *
     * @param show
     */
    @TargetApi (Build.VERSION_CODES.HONEYCOMB_MR1)
    public void showToolbar (boolean show) {
        if (show == mToolbarShow || mToolbar == null)
            return;
        mToolbarShow = show;
        if (show) {
            mToolbar.animate ()
                    .translationY (0)
                    .alpha (1)
                    .setDuration (HEADER_HIDE_ANIM_DURATION)
                    .setInterpolator (new DecelerateInterpolator ());
        } else {
            mToolbar.animate ()
                    .translationY (-mToolbar.getBottom ())
                    .alpha (0)
                    .setDuration (HEADER_HIDE_ANIM_DURATION)
                    .setInterpolator (new DecelerateInterpolator ());
        }
    }

    /**
     * 设置toolbar透明度
     *
     * @param alpha
     */
    protected void setToolbarAlpha (float alpha) {
        //        mActionbarDrawable.setAlpha((int) (alpha * 255));
    }

}
