package com.gta.zssx.mobileOA.view.adapter.ui;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.gta.zssx.R;
import com.gta.zssx.mobileOA.view.adapter.ListPopupWindowAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lan.zheng on 2016/11/1.
 */
public class titlePopupWindow extends PopupWindow {
    private ListPopupWindowAdapter mListPopupWindowAdapter;
    private Listener mListener;

    public titlePopupWindow(final Context context, String[] strings, String selectName,Listener listener) {
        super(context);
        List<String> list = new ArrayList<>();
        for(int i = 0 ;i<strings.length;i++){
            list.add(strings[i]);
        }
        mListener = listener;
        //拿到基本的item高度
        /*DisplayMetrics metric = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(metric);
        float density = metric.density;  // 屏幕密度（0.75 / 1.0 / 1.5）
        int densityDpi = metric.densityDpi;  // 屏幕密度DPI（120 / 160 / 240）
        float titleItemHeight = density*50;  //52dp
        float paddingWight = density*12;
        //拿到屏幕宽度
        WindowManager wm = context.getWindowManager();
        int width = wm.getDefaultDisplay().getWidth();
        //拿到要显示的高度
        int height;
        if(list.size() > 5){
            height = (int)titleItemHeight * 5;
        }else {
            height = (int)titleItemHeight * list.size();
        }*/

        this.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        this.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        View contentView = LayoutInflater.from(context).inflate(R.layout.list_item_title_item, null);
        this.setContentView(contentView);
        ListView listView = (ListView) contentView.findViewById(R.id.lv_title_item);
//        LinearLayout.LayoutParams lp;
//        lp= (LinearLayout.LayoutParams) listView.getLayoutParams();
//        lp.height = height;
//        lp.width = width/2-(int)paddingWight;  //两边有点点空隙
        //设置整个ListView宽度
//        listView.setLayoutParams(lp);

        if(mListPopupWindowAdapter != null ){
            mListPopupWindowAdapter = null;
        }
        mListPopupWindowAdapter = new ListPopupWindowAdapter(context,list,selectName);
        listView.setAdapter(mListPopupWindowAdapter);

        this.setFocusable(true);
        this.setAnimationStyle(R.style.drop_down_list_style);
        ColorDrawable dw = new ColorDrawable(context.getResources().getColor(R.color.transparent));
        this.setBackgroundDrawable(dw);
//        this.showAsDropDown(view);

        //点击Item
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mListener.onItemClickListener(position);
                dismiss();
            }
        });
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
        void onItemClickListener(int position);
    }

}
