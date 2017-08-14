package com.gta.zssx.fun.personalcenter.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.view.ViewGroup.LayoutParams;
import com.gta.zssx.R;

/**
 * Created by lan.zheng on 2016/6/20.
 */
public class SelectPicPopupWindow extends PopupWindow{
    private TextView mChooseFromPhoneTextView;
    private TextView mTakePhotoTextView;
    private TextView mCancelTextView;
    private View mMenuView;

    public SelectPicPopupWindow(Activity context, View.OnClickListener itemsOnClick) {
        super(context);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.popup_window_change_photo, null);
        mChooseFromPhoneTextView = (TextView) mMenuView.findViewById(R.id.tv_modify);
        mTakePhotoTextView = (TextView) mMenuView.findViewById(R.id.tv_delete);
        mCancelTextView = (TextView)mMenuView.findViewById(R.id.tv_cancel_show) ;
        //从手机中选择
        mChooseFromPhoneTextView.setOnClickListener(itemsOnClick);
        //拍照
        mTakePhotoTextView.setOnClickListener(itemsOnClick);
        //点击取消
        mCancelTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                dismiss();
            }
        });
        //设置SelectPicPopupWindow的View
        this.setContentView(mMenuView);
        //设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(LayoutParams.MATCH_PARENT);
        //设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(LayoutParams.WRAP_CONTENT);
        //设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        //设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.AnimBottom);
        //实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        //设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
        //mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
        mMenuView.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {

                int height = mMenuView.findViewById(R.id.pop_layout).getTop();
                int y=(int) event.getY();
                if(event.getAction()==MotionEvent.ACTION_UP){
                    if(y<height){
                        dismiss();
                    }
                }
                return true;
            }
        });

    }


}
