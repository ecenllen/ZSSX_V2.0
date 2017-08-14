package com.gta.zssx.fun.coursedaily.registercourse.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gta.zssx.R;
import com.gta.zssx.fun.coursedaily.registercourse.model.bean.SearchClassBean;

import java.util.List;

/**
 * [Description]
 * <p/>
 * [How to use]
 * <p/>
 * [Tips]
 *
 * @author Created by Zhimin.Huang on 2016/6/17.
 * @since 1.0.0
 */
public class ClassSearchAdapter extends RecyclerView.Adapter {

    private String mSearchString;
    private Context mContext;

    private Listener mListener;
    private List<SearchClassBean> mSearchClassBeans;

    public ClassSearchAdapter(Context context, Listener listener, List<SearchClassBean> searchClassBeans, String searchString) {
        mContext = context;
        mListener = listener;
        mSearchClassBeans = searchClassBeans;
        mSearchString = searchString;
    }

    public List<SearchClassBean> getSearchClassBeans() {
        return mSearchClassBeans;
    }

    public void removeData() {
        mSearchClassBeans.clear();
        notifyItemRangeRemoved(0, getItemCount());
    }

    public interface Listener {
        void itemClick(SearchClassBean searchClassBean);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return SearchHolder.newHolder(parent, mContext);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        SearchHolder lHolder = (SearchHolder) holder;
        final SearchClassBean lSearchClassBean = mSearchClassBeans.get(position);
        String lClassName = lSearchClassBean.getClassName();
        String lSource = "<font color=\'#00ad64\'>" + mSearchString + "</font>";
        String[] lSplit = lClassName.split(mSearchString);
        int lIndexOf = lClassName.indexOf(mSearchString);

        String lS = "";
        /*if (lIndexOf == 0) {
            if (lSplit.length == 0) {
                lS = lSource;
            } else {
                lS = lSource + "<font color=\'#a6a6a6\'>" + lSplit[1] + "</font>";
            }
        } else if (lSplit.length == 1) {
            lS = "<font color=\'#a6a6a6\'>" + lSplit[0] + "</font>" + lSource;
        } else {
            for (int i = 0; i < lClassName.length(); i++) {
                char lC = lClassName.charAt(i);
                if (i == lIndexOf) {
                    lS = lS + lSource + "<font color=\'#a6a6a6\'>" + lSplit[lSplit.length - 1] + "</font>";
                    break;
                }
                lS = lS + "<font color=\'#a6a6a6\'>" + lC + "</font>";
            }
        }*/
        lHolder.classNameTv.setText(getSpannableString(lClassName));
//        Html.fromHtml("<font color=\'#a6a6a6\'>" + str1 + "</font>" + "<font color=\'#8d7af2\'>" + lName + "</font>" + "<font color=\'#323232\'>" + lContent + "</font>");
        lHolder.classNameTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.itemClick(lSearchClassBean);
                }
            }
        });
    }

    /**
     * 关键字标亮方法
     * @param string 要标亮的字符串
     * @return 标亮后的字符串
     */
    public SpannableStringBuilder getSpannableString(String string){
        String allLowerCaseStr = exChange(string);  //结果转成全小写
        String mSearchStr = exChange(this.mSearchString);  //搜索关键字转为全小写
        //不包含关键字的时候
        if(!allLowerCaseStr.contains(mSearchStr)){
            SpannableStringBuilder styleAll = new SpannableStringBuilder(string);
            styleAll.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.word_color_666666)),0,string.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
            return styleAll;
        }
        //关键字和结果一样的时候
        if(allLowerCaseStr.equals(mSearchStr)){
            SpannableStringBuilder styleAll = new SpannableStringBuilder(string);
            styleAll.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.main_color)),0,string.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
            return styleAll;
        }
        //含有关键字且不等于关键字的时候
        int keyWordStart = allLowerCaseStr.indexOf(mSearchStr);
        int keyWordEnd= keyWordStart + mSearchStr.length();
        if(keyWordStart == 0 && keyWordEnd != string.length()){
            //关键字在开头
            int normalWordStart;
            normalWordStart = keyWordEnd;
            int normalWordEnd = string.length();
            SpannableStringBuilder styleAll = new SpannableStringBuilder(string.substring(keyWordStart,keyWordEnd));
            styleAll.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.main_color)),keyWordStart,keyWordEnd, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
//            style.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.word_color_666666)),normalWordStart,normalWordEnd, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
            String subString = string.substring(normalWordStart,normalWordEnd);  //关键字后面剩余的字符串长度
            if(subString.length() >= mSearchStr.length()){
                //当关键字之后的字符长度大于关键字，再次循环,把余下的字符串再判断，然后拼接过来
                styleAll.append(getSpannableString(subString));
            }else {
                SpannableStringBuilder sub = new SpannableStringBuilder(subString);
                //否则直接赋予颜色值
                sub.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.word_color_666666)),0,sub.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
                styleAll.append(sub);
            }
            return styleAll;
        }else if(keyWordEnd == string.length() && keyWordStart != 0){
            SpannableStringBuilder styleAll = new SpannableStringBuilder(string);   //在结尾的可以直接使用整个字符串
            //关键字在结尾
            int normalWordStart = 0;
            int normalWordEnd;
            normalWordEnd = keyWordStart;
            styleAll.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.word_color_666666)),normalWordStart,normalWordEnd, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
            styleAll.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.main_color)),keyWordStart,keyWordEnd, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
            return styleAll;
        }else {
            SpannableStringBuilder styleAll = new SpannableStringBuilder(string.substring(0,keyWordEnd));
            styleAll.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.main_color)),keyWordStart,keyWordEnd, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
            //关键字在中间
            int normalWordStart1 = 0;
            int normalWordEnd1;
            int normalWordStart2;
            normalWordEnd1 = keyWordStart;
            normalWordStart2 = keyWordEnd;
            int normalWordEnd2 = string.length();
            styleAll.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.word_color_666666)),normalWordStart1,normalWordEnd1, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
            styleAll.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.main_color)),keyWordStart,keyWordEnd, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
            String subString = string.substring(normalWordStart2,normalWordEnd2);  //关键字后面剩余的字符串长度
            if(subString.length() >= mSearchStr.length()){
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

    //大写转小写
    public static String exChange(String str){
        StringBuffer sb = new StringBuffer();
        if(str!=null){
            for(int i=0;i<str.length();i++){
                char c = str.charAt(i);
                if(Character.isUpperCase(c)){
                    sb.append(Character.toLowerCase(c));
                }else{
                    sb.append(c);
                }
            }
        }
        return sb.toString();
    }


    @Override
    public int getItemCount() {
        return mSearchClassBeans.size();
    }

    private static class SearchHolder extends RecyclerView.ViewHolder {
        TextView classNameTv;

        public SearchHolder(Context context, View itemView) {
            super(itemView);
            classNameTv = (TextView) itemView.findViewById(R.id.class_name_tv);
        }

        private static SearchHolder newHolder(ViewGroup parent, Context context) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_class_search, parent, false);
            return new SearchHolder(context, view);
        }
    }
}
