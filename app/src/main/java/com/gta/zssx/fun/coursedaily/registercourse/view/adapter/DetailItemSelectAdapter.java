package com.gta.zssx.fun.coursedaily.registercourse.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gta.zssx.R;
import com.gta.zssx.fun.coursedaily.registercourse.model.bean.DetailItemShowBean;
import com.gta.zssx.fun.coursedaily.registercourse.model.bean.SectionBean;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lan.zheng on 2017/2/27.
 * 用于显示当前已选择的节次/课程/老师
 */
public class DetailItemSelectAdapter extends RecyclerView.Adapter{
//    private Set<Object> infoBeanSet = new HashSet<>();
    private List<Object> infoBeanSet = new ArrayList<>();
    private List<SectionBean> sectionBeanList = new ArrayList<>();
    private Context mContext;
    public Listener mListener;
    private int ITEM_VIEW_TYPE_FOOTER = 0;  //Footer标识
    private View footer;
    private boolean isModify;
    private static final int ITEM_VIEW_TYPE_ITEM = 0;  //普通item
    private int WITCH_BEAN = 0;  //默认
    public static final int SESSION_BEAN_FLAG = 0;
    public static final int COURSE_BEAN_FLAG = 1;
    public static final int TEACHER_BEAN_FLAG = 2;

    //设置footer
    public void setFooter(View footer,boolean isModify){
        this.footer = footer;
        this.isModify = isModify;
        TextView addTextView = (TextView) footer.findViewById(R.id.tv_add_item);
        ImageView addImageView = (ImageView)footer.findViewById(R.id.iv_add_item);
        /*if(isHighlight){  //高亮为绿色
            addTextView.setTextColor(mContext.getResources().getColor(R.color.main_color));
            addImageView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_add_hover));
        }else {  //不可进入选择为灰色
            addTextView.setTextColor(mContext.getResources().getColor(R.color.word_color_666666));
            addImageView.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_add));
        }*/
        if(WITCH_BEAN == SESSION_BEAN_FLAG){

            //修改节次
            if(isModify){
                addTextView.setText(mContext.getResources().getString(R.string.modify_section));
                addImageView.setVisibility(View.GONE);
            }else {
                addTextView.setText(mContext.getResources().getString(R.string.add_session));
                addImageView.setVisibility(View.VISIBLE);
            }
        }else if(WITCH_BEAN == COURSE_BEAN_FLAG){
            addTextView.setText(mContext.getResources().getString(R.string.add_course));
        }else if(WITCH_BEAN == TEACHER_BEAN_FLAG){
            addTextView.setText(mContext.getResources().getString(R.string.add_teacher));
        }
    }

    /*public DetailItemSelectAdapter(Context mContext, Set<Object> mInfoBean, int whichBean, Listener listener){
        this.mContext = mContext;
        mListener = listener;
        WITCH_BEAN = whichBean;
        infoBeanSet = mInfoBean;
        ITEM_VIEW_TYPE_FOOTER = infoBeanSet.size();  //footer最开始定为最后条目
    }*/

    /**
     * 教师和课程显示使用这个函数
     * @param mContext context
     * @param mInfoBean 信息bean
     * @param whichBean 显示那个
     * @param listener 监听
     */
    public DetailItemSelectAdapter(Context mContext, List<Object> mInfoBean, int whichBean, Listener listener){
        this.mContext = mContext;
        mListener = listener;
        WITCH_BEAN = whichBean;
        infoBeanSet = mInfoBean;
        ITEM_VIEW_TYPE_FOOTER = infoBeanSet.size();  //footer最开始定为最后条目
    }

    /**
     * 节次需要排序显示，因此使用这个构造函数
     * @param mContext context
     * @param sectionBeanList 节次bean
     * @param listener 监听
     */
    public DetailItemSelectAdapter(Context mContext, List<SectionBean> sectionBeanList,Listener listener){
        this.mContext = mContext;
        mListener = listener;
        WITCH_BEAN = SESSION_BEAN_FLAG;
        this.sectionBeanList = sectionBeanList;
        ITEM_VIEW_TYPE_FOOTER = sectionBeanList.size();  //footer最开始定为最后条目
    }

    public boolean isFooter(int position) {
        if(WITCH_BEAN == SESSION_BEAN_FLAG){
            return position == sectionBeanList.size(); //是否是footer
        }else {
            return position == infoBeanSet.size();  //是否是Footer
        }

    }

    @Override
    public int getItemViewType(int position) {
        return isFooter(position) ? ITEM_VIEW_TYPE_FOOTER : ITEM_VIEW_TYPE_ITEM;
    }

    @Override
    public DisplayHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_VIEW_TYPE_FOOTER) {
            return new DisplayHolder(footer,true);  //传入footer
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_item_section_show, parent, false);
        return new DisplayHolder(view,false);
//        return DisplayHolder.newHolder(parent, context);  //传入条目
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        DisplayHolder lHolder = (DisplayHolder) holder;
        if(!isFooter(position)){ //不是footer
           //这里为footer时不做处理
           /* lHolder.addTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mListener != null){
                        mListener.addClickListener(WITCH_BEAN);
                    }
                }
            });
            return;*/
            Object object;
            if(WITCH_BEAN == SESSION_BEAN_FLAG){
                object =  sectionBeanList.get(position);  //节次
            }else {
//                object =  getObject(position);  //教师或课程
                object = infoBeanSet.get(position);
            }
            String name = "";
            if(WITCH_BEAN == SESSION_BEAN_FLAG){
                SectionBean sectionInfoBean = (SectionBean) object;
                name = sectionInfoBean.getLesson();
                //TODO 这里特殊，节次的选择当是修改时不可以删除
                if(isModify){
                    lHolder.deleteTv.setVisibility(View.GONE);
                }else {
                    lHolder.deleteTv.setVisibility(View.VISIBLE);
                }
            }else if (WITCH_BEAN == COURSE_BEAN_FLAG){
                DetailItemShowBean.CourseInfoBean courseInfoBean = (DetailItemShowBean.CourseInfoBean) object;
                name = courseInfoBean.getCourseName();
            }else if(WITCH_BEAN == TEACHER_BEAN_FLAG){
                DetailItemShowBean.TeacherInfoBean teacherInfoBean = (DetailItemShowBean.TeacherInfoBean)object;
                name = teacherInfoBean.getTeacherName();
            }
            //设置显示名称
            lHolder.classItemTv.setText(name);

            if(WITCH_BEAN == SESSION_BEAN_FLAG){
                if( sectionBeanList.get(position).getHaveBeenSignFlag()){ //TODO 如果是节次的话，要判断文字颜色,当已经被登记要显示红色
                    lHolder.classItemTv.setTextColor(mContext.getResources().getColor(R.color.delete_red));
                }else {
                    lHolder.classItemTv.setTextColor(mContext.getResources().getColor(R.color.word_color_666666));
                }
            }
        }

    }

   /* private Object getObject(int position){
        Iterator<Object> iterator = infoBeanSet.iterator();
        int sum = 0;
        Object object = null;
        while (iterator.hasNext()){
            object = iterator.next();
            if(sum == position){
                return object;
            }
            sum++;
        }
        return null;
    }*/

    @Override
    public int getItemCount() {
        if(WITCH_BEAN == SESSION_BEAN_FLAG){
            return sectionBeanList.size()+1;//有footer所以要+1
        }else {
            return infoBeanSet.size()+1;  //有footer所以要+1
        }
    }

    private class DisplayHolder extends RecyclerView.ViewHolder{

        ImageView deleteTv;
        TextView classItemTv;
        TextView addTv;

        public DisplayHolder(View itemView,boolean isFooter) {
            super(itemView);
            if(isFooter){
                addTv = (TextView) itemView.findViewById(R.id.tv_add_item);
                addTv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(mListener != null){
                            mListener.addClickListener(WITCH_BEAN);
                        }
                    }
                });
            }else {
                deleteTv = (ImageView) itemView.findViewById(R.id.iv_delete);
                classItemTv = (TextView) itemView.findViewById(R.id.tv_class_section);
                deleteTv.setOnClickListener(clickListener);
            }
        }

        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Object object;
                int position = getLayoutPosition();
                if(WITCH_BEAN == SESSION_BEAN_FLAG){
                    object =  sectionBeanList.get(position);  //节次
                }else {
//                    object =  getObject(position);  //教师或课程
                    object = infoBeanSet.get(position);
                }
                final Object finalObject;
                finalObject = object;
                if(WITCH_BEAN == SESSION_BEAN_FLAG){
                    sectionBeanList.remove(finalObject);  //节次
                }else {
                    infoBeanSet.remove(finalObject);  //教师或课程
                }
                notifyDataSetChanged();
                //TODO 回调通知Activity节次有改变，记得更新节次List
                if(mListener != null){
                    mListener.deleteClickListener(WITCH_BEAN, finalObject);
                }

            }
        };

/*      //生成Holder
        private static DisplayHolder newHolder(ViewGroup parent, Context context) {
            View view = LayoutInflater.from(context).inflate(R.layout.list_item_section_show, parent, false);
            return new DisplayHolder(view);
        }*/
    }

    public interface Listener{
        void addClickListener(int whichAdd);
        void deleteClickListener(int whichDelete,Object object);
    }

}
