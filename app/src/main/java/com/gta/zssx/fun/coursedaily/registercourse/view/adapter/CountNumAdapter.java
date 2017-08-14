package com.gta.zssx.fun.coursedaily.registercourse.view.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.gta.zssx.R;
import com.gta.zssx.fun.coursedaily.registercourse.model.bean.DetailItemShowBean;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by lan.zheng on 2017/3/7.
 * 显示教师或课程的总数
 */
public class CountNumAdapter extends BaseAdapter {
    private Set<Object> mCountSet = new HashSet<>();
    private Context mContext;
    private Listener mListener;

    public final static int COUNT_SHOW_TEACHER = 0;
    public final static int COUNT_SHOW_COUNSE = 1;
    private int mWhichCount = 0;

    public CountNumAdapter(Context context, Set<Object> set, int whichCountShow,Listener listener){
        mContext = context;
        mCountSet = set;
        mListener = listener;
        mWhichCount = whichCountShow;
    }

    @Override
    public int getCount() {
        return mCountSet.size();
    }

    @Override
    public Object getItem(int i) {
        return getObject(i);
    }

    private Object getObject(int position){
        Iterator<Object> iterator = mCountSet.iterator();
        int sum = 0;
        Object object;
        while (iterator.hasNext()){
            object = iterator.next();
            if(sum == position){
                return object;
            }
            sum++;
        }
        return null;
    }


    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder lViewHolder;  //一开始为null
        if(view  == null){
            view = View.inflate(mContext, R.layout.list_item_count_content,null);
            lViewHolder = new ViewHolder(view);
//            lViewHolder.itemTextView = (TextView) view.findViewById(R.id.tv_count_content);
//            lViewHolder.deleteImageView = (ImageView)view.findViewById(R.id.iv_count_delete);
//            lViewHolder.showImageView = (ImageView)view.findViewById(R.id.iv_count_show);
            view.setTag(lViewHolder);
        }else {
            lViewHolder = (ViewHolder) view.getTag();
        }
        Object object = getObject(i);
        String showString = "-";
        //文字内容设置
        if(mWhichCount == COUNT_SHOW_TEACHER){
            DetailItemShowBean.TeacherInfoBean teacherInfoBean = (DetailItemShowBean.TeacherInfoBean)object;
            assert teacherInfoBean != null;
            showString = teacherInfoBean.getTeacherName();
            lViewHolder.showImageView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_item_teacher));
        }else if(mWhichCount == COUNT_SHOW_COUNSE){
            DetailItemShowBean.CourseInfoBean courseInfoBean = (DetailItemShowBean.CourseInfoBean)object;
            assert courseInfoBean != null;
            showString = courseInfoBean.getCourseName();
            lViewHolder.showImageView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_item_course));
        }
        lViewHolder.itemTextView.setText(showString);
        lViewHolder.deleteImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCountSet.remove(object);      //删除该object
                notifyDataSetChanged();        //通知界面改变
//                ToastUtils.showShortToast(mContext,"deleteImageView.setOnClickListener click = "+mCountSet.size());
                mListener.onDeleteItemListener(object);  //回调通知计数改变
            }
        });
        return view;
    }

    private class ViewHolder {
        TextView itemTextView;
        ImageView deleteImageView;
        ImageView showImageView;

        public ViewHolder(View itemView){
            itemTextView = (TextView) itemView.findViewById(R.id.tv_count_content);
            deleteImageView = (ImageView)itemView.findViewById(R.id.iv_count_delete);
            showImageView = (ImageView)itemView.findViewById(R.id.iv_count_show);
        }

    }


    public interface Listener{
        void onDeleteItemListener(Object object);
    }


}
