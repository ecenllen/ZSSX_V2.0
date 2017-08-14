package com.gta.zssx.dormitory.view.adapter;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gta.zssx.R;
import com.gta.zssx.dormitory.model.bean.DetailItemBean;
import com.gta.zssx.dormitory.model.bean.EnterNameMultipleItemBean;
import com.gta.zssx.pub.common.Constant;
import com.gta.zssx.pub.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * [Description]
 * 录入姓名多项 - 宿舍维度
 * [How to use]
 *
 * [Tips]
 *
 * Created by lan.zheng on 2017/7/25 11:29.
 */


public class EnterNameMultipleDormitoryDimensionAdapter extends BaseExpandableListAdapter {
    //    private onCheckListener mListener;
//    public int[] group_checked ;
    private List<EnterNameMultipleItemBean> mDormitoryTitleStudentBeanList;  //即groupArray
    private List<List<DetailItemBean>> childArrayList;  //即childArray
    private List<String> groupArray;  //title
//    private List<String> groupCount;  //是否有箭头的标志
    private List<List<String>> childArray;  //child
    private Context mContext;
    // 一级标签上的状态图片数据源
   /* int[] group_state_array = new int[] { R.drawable.group_down,
            R.drawable.group_up };*/
    int[] group_state_array = new int[] { R.drawable.group_down1,
            R.drawable.group_up1 };
    private Listener mlistener;
    private TextView scoreTextView;
    private TextView minusTextView;
    private List<Set<Integer>> mCheckListList;  //用于标识当前是否有选项勾选了
    private boolean mIsCanEdit;
    private int mAdditionOrSubtraction;

    /**
     * 用于计算总分和更新到相应的宿舍
     * @return 新的改变后的bean
     */
    private List<EnterNameMultipleItemBean> getAllNewDormitoryTitleStudentBeanListData(){
        return mDormitoryTitleStudentBeanList;
    }

    /**
     * 上下箭头变换
     * @param groupPosition 父节点位置
     */
    public void updateChecked(int groupPosition) {
        mDormitoryTitleStudentBeanList.get(groupPosition).setGroupChecked(!mDormitoryTitleStudentBeanList.get(groupPosition).isGroupChecked());
//        notifyDataSetChanged();
//        group_checked[groupPosition] = group_checked[groupPosition]+1;
    }

    public void setmDormitoryTitleStudentBeanList(List<EnterNameMultipleItemBean> list, List<Set<Integer>> mCheckListList) {
        this.mDormitoryTitleStudentBeanList = list;
        initData(list, mCheckListList);
        notifyDataSetChanged();
    }

    /**
     * 宿舍维度
     * @param context context
     * @param dormitoryTitleStudentBeanList dormitoryTitleStudentBeanList
     * @param mCheckListList mCheckListList
     * @param listener listener
     */
    public EnterNameMultipleDormitoryDimensionAdapter(Context context,List<EnterNameMultipleItemBean> dormitoryTitleStudentBeanList, List<Set<Integer>> mCheckListList,boolean isCanEdit,int additionOrSubtraction,Listener listener){
        mContext = context;
        this.mlistener = listener;
        mIsCanEdit = isCanEdit;
        mAdditionOrSubtraction = additionOrSubtraction;
        mDormitoryTitleStudentBeanList = dormitoryTitleStudentBeanList;
        initData(dormitoryTitleStudentBeanList, mCheckListList);
    }

    private void initData(List<EnterNameMultipleItemBean> dormitoryTitleStudentBeanList, List<Set<Integer>> mCheckListList) {
        this.mCheckListList = mCheckListList;
//        groupCount = new ArrayList<>();  //TODO 都必须有箭头，所以都给大于0的值

        //拿到子列表
        childArrayList = new ArrayList<>();   //TODO 即childArray
        for(int i = 0;i< dormitoryTitleStudentBeanList.size();i++){
            List<DetailItemBean> childItem = dormitoryTitleStudentBeanList.get(i).getIndexItemBeanList();
            childArrayList.add(childItem);
//            if(dormitoryTitleStudentBeanList.get(i).getStudentId() == 0){
//                groupCount.add("0"); //表示该床位没有人，不显示箭头
//            }else {
//                groupCount.add("1");  //同时给标识是否显示具体项
//            }

        }
    }

    @Override
    public int getGroupCount() {
//        return groupArray.size();
        return mDormitoryTitleStudentBeanList == null ? 0 : mDormitoryTitleStudentBeanList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;   //TODO 都只有一个child Item
    }

    @Override
    public Object getGroup(int groupPosition) {
        return getGroup(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return childArrayList.get(groupPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    /**
     * 一级标签设置
     * @param groupPosition groupPosition
     * @param isExpanded  isExpanded
     * @param convertView convertView
     * @param parent parent
     * @return View
     */
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupHolder groupHolder = null;
        if(convertView == null){
            // 为视图对象指定布局
            convertView = LinearLayout.inflate(mContext, R.layout.list_item_expand_group_dormitory_dimension, null);
            groupHolder = new GroupHolder();
            groupHolder.myLayout = (RelativeLayout) convertView.findViewById(R.id.group_layout);
            groupHolder.group_title = (TextView) convertView.findViewById(R.id.tv_group_student_bed);
            groupHolder.student_name = (TextView)convertView.findViewById(R.id.tv_group_student_name);
            groupHolder.group_score_name = (TextView)convertView.findViewById(R.id.tv_group_num_score_name);
            groupHolder.group_state = (ImageView) convertView.findViewById(R.id.group_state);
            convertView.setTag(groupHolder);
        }else {
            groupHolder = (GroupHolder) convertView.getTag();
        }

        /*
         * 声明视图上要显示的控件
         */
        // 新建一个TextView对象，用来显示一级标签上的标题信息
//        TextView group_title = (TextView) convertView.findViewById(R.id.tv_group_student_bed);
        // 新建一个TextView对象，用来显示一级标签
//        TextView student_name = (TextView)convertView.findViewById(R.id.tv_group_student_name) ;
//        ImageView group_state = (ImageView) convertView.findViewById(R.id.group_state);
        /*
             * 分数View要传递，所以声明公用的
             */
        scoreTextView = (TextView)convertView.findViewById(R.id.tv_group_num_score) ;
        minusTextView = (TextView)convertView.findViewById(R.id.tv_minus_sign_dormitory) ;
        /*
         * 设置相应控件的内容
         */
        // 设置标题上的文本信息
        EnterNameMultipleItemBean itemBean = mDormitoryTitleStudentBeanList.get(groupPosition);
        String studentName = itemBean.getStudentName();
        if(!TextUtils.isEmpty(itemBean.getClassName())){
            studentName += "("+itemBean.getClassName()+")";
        }
        groupHolder.group_title.setText(itemBean.getBedName());
        groupHolder.student_name.setText(studentName);

//        scoreTextView.setTextColor(mContext.getResources().getColor(R.color.main_color));
//        minusTextView.setTextColor(mContext.getResources().getColor(R.color.main_color));
        scoreTextView.setText(StringUtils.formatScore(itemBean.getStudentScore()));

        //设置是否可点击,可点击表示，父类控件的点击事件交给该Layout，父控件将不监听点击，此时不设置点击效果，学生为0的时候就不会可点击
        if(itemBean.getStudentId() == 0){   // 只有宿舍维度才存在空床位，班级维度不存在
            groupHolder.myLayout.setClickable(true);
            groupHolder.group_state.setVisibility(View.INVISIBLE);
        }else {
            groupHolder.myLayout.setClickable(false);
            groupHolder.group_state.setVisibility(View.VISIBLE);
        }

        // 设置是否显示-号
        if(mAdditionOrSubtraction == Constant.Subtraction){
            if(!TextUtils.isEmpty(itemBean.getStudentScore()) && Float.valueOf(itemBean.getStudentScore()) > 0) {
                minusTextView.setVisibility(View.VISIBLE);
            }else {
                minusTextView.setVisibility(View.GONE);
            }
        }else {
            minusTextView.setVisibility(View.GONE);
        }

        if(itemBean.isGroupChecked()) {
            // 设置默认的图片是选中状态
            groupHolder.group_state.setBackgroundResource(group_state_array[1]);
        } else {
            groupHolder.group_state.setBackgroundResource(group_state_array[0]);
        }

        // 返回一个布局对象
        return convertView;
    }

    /**
     * 对一级标签下的二级标签进行设置
     */
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildHolder childHolder = null;
        if(convertView == null){
            // 为视图对象指定布局
            convertView =  LinearLayout.inflate(mContext, R.layout.list_item_expand_group_child_dormitory_or_class_dimension, null);
            childHolder = new ChildHolder();
            childHolder.childRecyclerView = (RecyclerView) convertView.findViewById(R.id.rv_student_check_item);
            childHolder.childRecyclerView.setLayoutManager(new GridLayoutManager(mContext,2));
            convertView.setTag(childHolder);
        }else {
            childHolder = (ChildHolder) convertView.getTag();
        }

        float score = 0;
        if(!TextUtils.isEmpty(mDormitoryTitleStudentBeanList.get(groupPosition).getStudentScore())){
            score = Float.parseFloat(mDormitoryTitleStudentBeanList.get(groupPosition).getStudentScore());
        }
        /**
         * 声明视图上要显示的控件
         */
        if(mDormitoryTitleStudentBeanList.get(groupPosition).getStudentId() == 0){
            childHolder.childRecyclerView.setVisibility(View.GONE);
        }else {
            Set<Integer> checkList = mCheckListList.get(groupPosition);  //拿到当前勾选的选项
            EnterNameMultipleStudentDetailItemAdapter enterNameMultipleStudentDetailItemAdapter = new EnterNameMultipleStudentDetailItemAdapter(mContext,childArrayList.get(groupPosition),checkList,
                    groupPosition,score,mIsCanEdit,mAdditionOrSubtraction,listenerDetailItem);
            enterNameMultipleStudentDetailItemAdapter.setScoreTextView(scoreTextView);
            enterNameMultipleStudentDetailItemAdapter.setMinusTextView(minusTextView);
            childHolder.childRecyclerView.setAdapter(enterNameMultipleStudentDetailItemAdapter);
            childHolder.childRecyclerView.setVisibility(View.VISIBLE);
        }
        return convertView;
    }

    EnterNameMultipleStudentDetailItemAdapter.Listener listenerDetailItem = new EnterNameMultipleStudentDetailItemAdapter.Listener() {

        @Override
        public void addClickListener(int groupPosition, int childPosition, float newScore, Set<Integer> checkList, boolean isCheck) {
            //TODO 要把变了的分数记录下来，和是否点击过的状态改过来
            String scoreString = "";
            //大于等于0的才显示值，否则为""
            if(newScore >= 0){
                scoreString = String.valueOf(newScore);
            }
            mDormitoryTitleStudentBeanList.get(groupPosition).setStudentScore(scoreString);
            mDormitoryTitleStudentBeanList.get(groupPosition).getIndexItemBeanList().get(childPosition).setDetailItemCheck(isCheck);
            //TODO 记录状态值
            mCheckListList.get(groupPosition).clear();
            mCheckListList.get(groupPosition).addAll(checkList);
            childArrayList.get(groupPosition).get(childPosition).setDetailItemCheck(isCheck);
            //TODO 不能传负数回去
            if(newScore < 0 ){
                newScore = 0;
            }
            mlistener.sendNewInfoStudentBack(getAllNewDormitoryTitleStudentBeanListData(),groupPosition,newScore,checkList);
        }
    };

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }


    public interface Listener{
        /**
         * 回调所有更新的数据
         * @param enterNameMultipleItemBeanList 总表
         * @param groupPosition 改动的条目位置
         * @param checkList 针对一个条目勾选状态返回
         */
        void sendNewInfoStudentBack(List<EnterNameMultipleItemBean> enterNameMultipleItemBeanList, int groupPosition, float itemScore, Set<Integer> checkList);
    }

    private static class GroupHolder{
        RelativeLayout myLayout;
        TextView group_title;
        TextView student_name;
//        TextView group_minus_sign; //-
        TextView group_score_name; //分
        ImageView group_state;
    }

    private static class ChildHolder{
        RecyclerView childRecyclerView;
    }
}



