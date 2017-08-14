package com.gta.zssx.fun.coursedaily.registercourse.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gta.zssx.R;
import com.gta.zssx.fun.coursedaily.registercourse.model.bean.DetailItemShowBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lan.zheng on 2017/3/13.
 *
 */
public class CourseSearchAdapter extends RecyclerView.Adapter{
    private List<DetailItemShowBean.CourseInfoBean> mCourseInfoBeenList = new ArrayList<>();
    private Listener mListener;
    private Context mContext;
    private String mSearchString;

    public CourseSearchAdapter(Context context, List<DetailItemShowBean.CourseInfoBean> courseInfoBeenList,String keyWord, Listener listener){
        mCourseInfoBeenList = courseInfoBeenList;
        mListener = listener;
        mContext = context;
        mSearchString = keyWord;
    }

    public void removeData () {
        notifyItemRangeRemoved (0, getItemCount());
        mCourseInfoBeenList.clear ();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return CourseHolder.newHolder(parent,mContext);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        CourseHolder courseHolder = (CourseHolder) holder;
        DetailItemShowBean.CourseInfoBean courseInfoBean = mCourseInfoBeenList.get(position);
        String courseName = courseInfoBean.getCourseName();
        courseHolder.mTextView.setText(getSpannableString(courseName));
        courseHolder.mTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.itemClickListener(courseInfoBean);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mCourseInfoBeenList.size();
    }

    private static class CourseHolder extends RecyclerView.ViewHolder {
        TextView mTextView;

        CourseHolder (View itemView) {
            super(itemView);
            mTextView = (TextView) itemView.findViewById(R.id.tv_search_course_content);
        }

        private static CourseHolder newHolder(ViewGroup parent, Context context) {
            View view = LayoutInflater.from(context).inflate(R.layout.list_item_search_course_content, parent, false);
            return new CourseHolder( view);
        }
    }

    public interface Listener{
        void itemClickListener(DetailItemShowBean.CourseInfoBean courseInfoBean);
    }

    /**
     * 关键字标亮方法
     * @param string 要标亮的字符串
     * @return 标亮后的字符串
     */
    private SpannableStringBuilder getSpannableString (String string){
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
    private static String exChange (String str){
        StringBuilder sb = new StringBuilder ();
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
}
