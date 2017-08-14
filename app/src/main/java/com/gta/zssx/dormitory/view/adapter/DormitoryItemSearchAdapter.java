package com.gta.zssx.dormitory.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gta.zssx.R;
import com.gta.zssx.dormitory.model.bean.ItemLevelBean;
import com.gta.zssx.dormitory.model.bean.RxItemSelect;
import com.gta.zssx.pub.util.RxBus;

import java.util.ArrayList;
import java.util.List;

/**
 * [Description]
 * 指标项搜索适配器
 * [How to use]
 *
 * [Tips]
 *
 * Created by lan.zheng on 2017/7/25 11:27.
 */


public class DormitoryItemSearchAdapter extends RecyclerView.Adapter{
    private Context mContext;
    private List<ItemLevelBean> mItemLevelBeanList;
    private String mKeyWord;
    private int mSelectItemId;

    public DormitoryItemSearchAdapter(Context context,List<ItemLevelBean> itemLevelBeanList){
        mContext = context;
        this.mItemLevelBeanList = itemLevelBeanList;
    }

    public void setKeyWord(String keyWord){
        mKeyWord = keyWord;
    }

    public void setmSelectItemId(int mSelectItemId) {
        this.mSelectItemId = mSelectItemId;
    }

    public void setDataAndRefresh(List<ItemLevelBean> itemLevelBeanList){
        mItemLevelBeanList = itemLevelBeanList;
        notifyDataSetChanged();
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View mItemView = LayoutInflater.from(mContext).inflate(R.layout.list_item_dormitory_item_search_item, parent, false);
        return new DisplayHolder(mItemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        DisplayHolder displayHolder = (DisplayHolder) holder;
        ItemLevelBean itemLevelBean = mItemLevelBeanList.get(position);
        String itemName = itemLevelBean.getItemName();
        if(TextUtils.isEmpty(mKeyWord)){
            displayHolder.nameTv.setText(itemName);
        }else {
            displayHolder.nameTv.setText(getSpannableString(itemName));
        }
        int itemId = itemLevelBean.getItemId();
        if(mSelectItemId != -1 && mSelectItemId == itemId){
            displayHolder.ivSelectedIcon.setVisibility(View.VISIBLE);
        }else {
            displayHolder.ivSelectedIcon.setVisibility(View.INVISIBLE);
        }

    }

    @Override
    public int getItemCount() {
        return mItemLevelBeanList == null ? 0 : mItemLevelBeanList.size();
    }

    private class DisplayHolder extends RecyclerView.ViewHolder {
        TextView nameTv;
        ImageView ivSelectedIcon;
        RelativeLayout itemLayout;

        public DisplayHolder(View itemView) {
            super(itemView);
            nameTv = (TextView)itemView.findViewById(R.id.tv_dormitory_item);
            itemLayout = (RelativeLayout)itemView.findViewById(R.id.layout_item);
            ivSelectedIcon = (ImageView) itemView.findViewById(R.id.iv_selected_icon);
            itemLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getLayoutPosition();
                    ItemLevelBean itemLevelBean = mItemLevelBeanList.get(pos);
                    /**
                     * 回调
                     * rx_dormitory_item_select
                     */
                    RxItemSelect rxItemSelect = new RxItemSelect();
                    rxItemSelect.setItemLevelBean(itemLevelBean);
                    RxBus.getDefault().post(rxItemSelect);
                }
            });
        }
    }

    //课堂日志，关键字标亮，适用于巡课
    public SpannableStringBuilder getSpannableString(String str){
        //不包含关键字的时候
        if(!str.contains(mKeyWord)){
            SpannableStringBuilder styleAll = new SpannableStringBuilder(str);
            styleAll.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.word_color_666666)),0,str.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
            return styleAll;
        }
        //关键字和结果一样的时候
        if(str.equals(mKeyWord)){
            SpannableStringBuilder styleAll = new SpannableStringBuilder(str);
            styleAll.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.main_color)),0,str.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
            return styleAll;
        }
        //含有关键字且不等于关键字的时候
        int keyWordStart = str.indexOf(mKeyWord);
        int keyWordEnd= keyWordStart + mKeyWord.length();
        if(keyWordStart == 0 && keyWordEnd != str.length()){
            //关键字在开头
            int normalWordStart = keyWordEnd;
            int normalWordEnd = str.length();
            SpannableStringBuilder styleAll = new SpannableStringBuilder(str.substring(keyWordStart,keyWordEnd));
            styleAll.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.main_color)),keyWordStart,keyWordEnd, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
//            style.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.word_color_666666)),normalWordStart,normalWordEnd, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
            String subString = str.substring(normalWordStart,normalWordEnd);  //关键字后面剩余的字符串长度
            if(subString.length() >= mKeyWord.length()){
                //当关键字之后的字符长度大于关键字，再次循环,把余下的字符串再判断，然后拼接过来
                styleAll.append(getSpannableString(subString));
            }else {
                SpannableStringBuilder sub = new SpannableStringBuilder(subString);
                //否则直接赋予颜色值
                sub.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.word_color_666666)),0,sub.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
                styleAll.append(sub);
            }
            return styleAll;
        }else if(keyWordEnd == str.length() && keyWordStart != 0){
            SpannableStringBuilder styleAll = new SpannableStringBuilder(str);   //在结尾的可以直接使用整个字符串
            //关键字在结尾
            int normalWordStart = 0;
            int normalWordEnd = keyWordStart;
            styleAll.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.word_color_666666)),normalWordStart,normalWordEnd, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
            styleAll.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.main_color)),keyWordStart,keyWordEnd, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
            return styleAll;
        }else {
            SpannableStringBuilder styleAll = new SpannableStringBuilder(str.substring(0,keyWordEnd));
            styleAll.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.main_color)),keyWordStart,keyWordEnd, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
            //关键字在中间
            int normalWordStart1 = 0;
            int normalWordEnd1 = keyWordStart;
            int normalWordStart2 = keyWordEnd;
            int normalWordEnd2 = str.length();
            styleAll.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.word_color_666666)),normalWordStart1,normalWordEnd1, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
            styleAll.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.main_color)),keyWordStart,keyWordEnd, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
            String subString = str.substring(normalWordStart2,normalWordEnd2);  //关键字后面剩余的字符串长度
            if(subString.length() >= mKeyWord.length()){
                //当关键字之后的字符长度大于关键字，再次循环,把余下的字符串再判断，然后拼接过来
                styleAll.append(getSpannableString(subString));
            }else {
                SpannableStringBuilder sub = new SpannableStringBuilder(subString);
                //否则直接赋予颜色值
                sub.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.word_color_666666)),0,sub.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
                styleAll.append(sub);
            }
            return styleAll;
        }
    }
}
