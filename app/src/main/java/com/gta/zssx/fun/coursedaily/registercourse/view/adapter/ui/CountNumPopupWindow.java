package com.gta.zssx.fun.coursedaily.registercourse.view.adapter.ui;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.gta.zssx.R;
import com.gta.zssx.fun.coursedaily.registercourse.view.adapter.CountNumAdapter;

import java.util.HashSet;
import java.util.Set;

import rx.Observable;

/**
 * Created by lan.zheng on 2017/3/7.
 */
public class CountNumPopupWindow extends PopupWindow {
    private Listener mListener;
    private CountNumAdapter mCountNumAdapter;
    private Set<Object> mObjectSet;
    private int mWitchShow;
    private Context mContext;
    private int mItemNum;
    private int mMaxNumShowItem;

    public CountNumPopupWindow(final Context context,String title,Set<Object> objects,int witchShow,int maxNumShowItem,Listener listener) {
        super(context);
        mListener = listener;
        mObjectSet = objects;
        mWitchShow = witchShow;
        mContext = context;
        mItemNum = objects.size();
        mMaxNumShowItem = maxNumShowItem;
        //拿到基本的item高度
        DisplayMetrics metric = new DisplayMetrics();
        ((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(metric);
        float density = metric.density;  // 屏幕密度（0.75 / 1.0 / 1.5）
        float titleItemHeight = density*42;  //42dp
        this.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        this.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        View contentView = LayoutInflater.from(context).inflate(R.layout.list_item_count_num_item, null);
        this.setContentView(contentView);
        ListView listView = (ListView) contentView.findViewById(R.id.lv_count_num_item);
        //拿到要显示的高度，如果超过10个条目的，弹框不允许再增大
        if(mItemNum > mMaxNumShowItem){
           int height = (int)titleItemHeight * mMaxNumShowItem;
            //设置高度
            LinearLayout.LayoutParams lp;
            lp= (LinearLayout.LayoutParams) listView.getLayoutParams();
            lp.height = height;
            listView.setLayoutParams(lp);
        }
        mCountNumAdapter = new CountNumAdapter(mContext,mObjectSet,mWitchShow, new CountNumAdapter.Listener() {
            @Override
            public void onDeleteItemListener(Object object) {
                //回调删除监听
                if(mListener != null)
                    mListener.onItemDeleteListener(object);
                //更新ListView高度
                mItemNum--;
                int height = (int)titleItemHeight * mItemNum;
                if(mItemNum > mMaxNumShowItem){
                    height = (int)titleItemHeight * mMaxNumShowItem;
                }
                //设置高度
                LinearLayout.LayoutParams lp;
                lp= (LinearLayout.LayoutParams) listView.getLayoutParams();
                lp.height = height;
                listView.setLayoutParams(lp);
            }
        });
        listView.setAdapter(mCountNumAdapter);

        //删除全部
        ImageView imageView = (ImageView)contentView.findViewById(R.id.iv_delete_all_item);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mListener != null)
                    mListener.onDeleteAllClickListener();
            }
        });
        TextView textView = (TextView)contentView.findViewById(R.id.tv_title_show);
        textView.setText(title);
        this.setFocusable(true);
//        this.setAnimationStyle(R.style.drop_down_list_style);  //设置弹出动画
        ColorDrawable dw = new ColorDrawable(context.getResources().getColor(R.color.half_transparent));
        this.setBackgroundDrawable(dw);


        //点击返回键
        listView.setOnKeyListener(new View.OnKeyListener(){
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // TODO Auto-generated method stub
                dismiss();
                return true;
            }
        });

        //消失监听
        this.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                if(mListener != null)
                    mListener.onPopupWindowDismissListener();
            }
        });

        //点击外围
        this.getContentView().setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                setFocusable(false);
                dismiss();
                return true;
            }
        });
    }


    public interface Listener{
        void onPopupWindowDismissListener();
        void onDeleteAllClickListener();
        void onItemDeleteListener(Object object);
    }

}
