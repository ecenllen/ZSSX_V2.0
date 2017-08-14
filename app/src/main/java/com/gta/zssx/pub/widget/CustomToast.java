package com.gta.zssx.pub.widget;

import android.content.Context;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gta.zssx.R;


/**
 * Created by weiye.chen on 2017/7/7.
 * 有图片的弹框
 */
public class CustomToast {

    public static void ToastWithImageShort(Context context, int  imageRes, CharSequence text){
        makeToast(context, imageRes, text, Toast.LENGTH_SHORT);
    }

    public static void ToastWithImageLong(Context context,  int  imageRes, CharSequence text){
        //创建一个Toast提示消息
        makeToast(context, imageRes, text, Toast.LENGTH_LONG);
    }

    private static void makeToast(Context context, int imageRes, CharSequence text, int duration) {
        Toast toast = Toast.makeText(context,null, duration);
        //设置Toast提示消息在屏幕上的位置
        toast.setGravity(Gravity.FILL, 0, 0);
        View view = LinearLayout.inflate(context, R.layout.layout_dormitory_submit_check_view, null);
        view.setOnTouchListener((v, event) -> true); // 拦截点击事件
        ImageView imageView = (ImageView) view.findViewById(R.id.iv_custom);
        TextView textView = (TextView)view.findViewById(R.id.tv_custom);
        if(imageRes != 0)
            imageView.setImageDrawable(context.getResources().getDrawable(imageRes));
        textView.setText(text);
        //创建一个LineLayout容器
        LinearLayout toastView = (LinearLayout) toast.getView();
        LinearLayout.LayoutParams vlp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        vlp.setMargins(0,0,0,0);
        view.setLayoutParams(vlp);
        toastView.setBackgroundColor(context.getResources().getColor(R.color.transparent_slight));
        toastView.addView(view);
        toast.show();
    }
}
