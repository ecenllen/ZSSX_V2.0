package com.gta.zssx.patrolclass.view.adapter;

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
import com.gta.zssx.fun.coursedaily.registercourse.model.bean.DetailItemShowBean;
import com.gta.zssx.fun.coursedaily.registercourse.view.adapter.CourseSearchAdapter;
import com.gta.zssx.patrolclass.model.entity.ChooseTeacherSearchEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liang.lu on 2016/8/17 15:44.
 */
public class ChooseTeacherSearchAdapter extends RecyclerView.Adapter {
    private boolean mFromRegisterPage = false;  //用于点击条目时区别监听
    private Listener mListener;
    private Context mContext;
    private List<ChooseTeacherSearchEntity> entities;
    private ClickTeacherItemListener listener;
    private String mSearchString;

    public void setmSearchString (String mSearchString) {
        this.mSearchString = mSearchString;
    }

    public void setEntities (List<ChooseTeacherSearchEntity> entities) {
        this.entities = entities;
    }

    public ChooseTeacherSearchAdapter (Context context, ClickTeacherItemListener listener) {
        this.mContext = context;
        this.listener = listener;
    }

    //用于课堂日志登记搜索教师
    public ChooseTeacherSearchAdapter(Context context, List<ChooseTeacherSearchEntity> entities,String keyWord,Listener listener){
        mFromRegisterPage = true;
        this.entities = entities;
        mSearchString = keyWord;
        mContext = context;
        mListener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {
        View view = LayoutInflater.from (mContext).inflate (R.layout.item_choose_teacher_search, parent, false);
        ChooseTeacherSearchHolder holder = new ChooseTeacherSearchHolder (view);
        return holder;
    }

    @Override
    public void onBindViewHolder (RecyclerView.ViewHolder holder, int position) {
        ChooseTeacherSearchHolder chooseTeacherSearchHolder = (ChooseTeacherSearchHolder) holder;
        ChooseTeacherSearchEntity entity = entities.get (position);
        String mTeacherName = entity.getName ();
        String mTeacherCode = entity.getTeacherCode ();
        //赋值，关键字标亮
        chooseTeacherSearchHolder.mTeacherName.setText(getSpannableString(mTeacherName));
        chooseTeacherSearchHolder.mTeacherCode.setText(getSpannableString(mTeacherCode));
            //巡课
            /*String mTeacherColor = SetKeyWordColor (mTeacherName);
            String mCodeColor = SetKeyWordColor (mTeacherCode);
            if (mTeacherName.contains (mSearchString) && mTeacherCode.contains (mSearchString)) {
                chooseTeacherSearchHolder.mTeacherName.setText (Html.fromHtml (mTeacherColor));
                chooseTeacherSearchHolder.mTeacherCode.setText (Html.fromHtml (mCodeColor));
            } else if (mTeacherCode.contains (mSearchString)) {
                chooseTeacherSearchHolder.mTeacherName.setText (mTeacherName);
                chooseTeacherSearchHolder.mTeacherCode.setText (Html.fromHtml (mCodeColor));
            } else if (mTeacherName.contains (mSearchString)) {
                chooseTeacherSearchHolder.mTeacherName.setText (Html.fromHtml (mTeacherColor));
                chooseTeacherSearchHolder.mTeacherCode.setText (mTeacherCode);
            } else {
                chooseTeacherSearchHolder.mTeacherName.setText (mTeacherName);
                chooseTeacherSearchHolder.mTeacherCode.setText (mTeacherCode);
            }*/

    }

    @Override
    public int getItemCount () {
        return entities.size ();
    }

    private class ChooseTeacherSearchHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mTeacherName;
        private TextView mTeacherCode;

        public ChooseTeacherSearchHolder (View itemView) {
            super (itemView);
            mTeacherCode = (TextView) itemView.findViewById (R.id.tv_teacher_code);
            mTeacherName = (TextView) itemView.findViewById (R.id.tv_child);
            itemView.setOnClickListener (this);
        }

        @Override
        public void onClick (View v) {
            if(mFromRegisterPage){
                DetailItemShowBean.TeacherInfoBean teacherInfoBean = new DetailItemShowBean.TeacherInfoBean();
                int position = getLayoutPosition();
                ChooseTeacherSearchEntity entity = entities.get (position);
                teacherInfoBean.setTeacherName(entity.getName());
                teacherInfoBean.setTeacherId(entity.getTeacherId());
                teacherInfoBean.setTeacherNo(entity.getTeacherCode());
                mListener.itemClickListener(teacherInfoBean);
            }else {
                listener.clickTeacherChooseItem (getLayoutPosition ());
            }

        }
    }

    public void removeData () {
        notifyItemRangeRemoved (0, getItemCount ());
        entities.clear ();
    }

    public interface ClickTeacherItemListener {
        void clickTeacherChooseItem (int position);
    }

    public interface Listener {
        void itemClickListener(DetailItemShowBean.TeacherInfoBean teacherInfoBean);
    }

    //算法有问题，废弃
    private String SetKeyWordColor (String s) {
        String lSource = "<font color=\'#00ad64\'>" + mSearchString + "</font>";
        String[] lSplit = s.split (mSearchString);
        int lIndexOf = s.indexOf (mSearchString);

        String lS = "";
        if (lIndexOf == 0) {
            if (lSplit.length == 0) {
                lS = lSource;
            } else {
                lS = lSource + "<font color=\'#a6a6a6\'>" + lSplit[1] + "</font>";
            }
        } else if (lSplit.length == 1) {
            lS = "<font color=\'#a6a6a6\'>" + lSplit[0] + "</font>" + lSource;
        } else {
            for (int i = 0; i < s.length (); i++) {
                char lC = s.charAt (i);
                if (i == lIndexOf) {
                    lS = lS + lSource + "<font color=\'#a6a6a6\'>" + lSplit[lSplit.length - 1] + "</font>";
                    break;
                }
                lS = lS + "<font color=\'#a6a6a6\'>" + lC + "</font>";
            }
        }
        return lS;
    }

    //课堂日志，关键字标亮，适用于巡课
    public SpannableStringBuilder getSpannableString(String str){
        //不包含关键字的时候
        if(!str.contains(mSearchString)){
            SpannableStringBuilder styleAll = new SpannableStringBuilder(str);
            styleAll.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.word_color_666666)),0,str.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
            return styleAll;
        }
        //关键字和结果一样的时候
        if(str.equals(mSearchString)){
            SpannableStringBuilder styleAll = new SpannableStringBuilder(str);
            styleAll.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.main_color)),0,str.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
            return styleAll;
        }
        //含有关键字且不等于关键字的时候
        int keyWordStart = str.indexOf(mSearchString);
        int keyWordEnd= keyWordStart + mSearchString.length();
        if(keyWordStart == 0 && keyWordEnd != str.length()){
            //关键字在开头
            int normalWordStart = keyWordEnd;
            int normalWordEnd = str.length();
            SpannableStringBuilder styleAll = new SpannableStringBuilder(str.substring(keyWordStart,keyWordEnd));
            styleAll.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.main_color)),keyWordStart,keyWordEnd, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
//            style.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.word_color_666666)),normalWordStart,normalWordEnd, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
            String subString = str.substring(normalWordStart,normalWordEnd);  //关键字后面剩余的字符串长度
            if(subString.length() >= mSearchString.length()){
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
            if(subString.length() >= mSearchString.length()){
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
