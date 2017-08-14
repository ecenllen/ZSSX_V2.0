package com.gta.zssx.fun.coursedaily.registerrecord.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.gta.zssx.R;

/**
 * Created by lan.zheng on 2016/6/20.
 */
public class SelectOprationPopupWindow extends PopupWindow{
    private TextView mModifyTextView;
    private TextView mDeleteTextView;
    private TextView mCancelTextView;
    private View mMenuView;
    public SelectOprationPopupWindow(Activity context, boolean haveBeenApprove, View.OnClickListener itemsOnClick) {
        super(context);
//        super(context,R.style.transparentFrameWindowStyle);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.popup_window_change_photo, null);
        mModifyTextView = (TextView) mMenuView.findViewById(R.id.tv_modify);
        mDeleteTextView = (TextView) mMenuView.findViewById(R.id.tv_delete);
        mCancelTextView = (TextView)mMenuView.findViewById(R.id.tv_cancel_show) ;
        if(haveBeenApprove){
            mModifyTextView.setTextColor(context.getResources().getColor(R.color.word_color_999999));
            mDeleteTextView.setTextColor(context.getResources().getColor(R.color.word_color_999999));
        }else {
            mModifyTextView.setOnClickListener(itemsOnClick);
            mDeleteTextView.setOnClickListener(itemsOnClick);
        }
        //点击取消
        mCancelTextView.setOnClickListener(itemsOnClick);
        //设置SelectPicPopupWindow的View
        this.setContentView(mMenuView);
        //设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        //设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setAnimationStyle(R.style.AnimBottom);
//        设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        //实例化一个ColorDrawable颜色为半透明
//        ColorDrawable dw = new ColorDrawable(0xb0000000);
        //实例化一个ColorDrawable颜色为全透明！！！为了和window底色融合，设置#00ffffff
//         ColorDrawable dw = new ColorDrawable(context.getResources().getColor(R.color.transparent));
        ColorDrawable dw = new ColorDrawable(context.getResources().getColor(R.color.popup_window_color));
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
