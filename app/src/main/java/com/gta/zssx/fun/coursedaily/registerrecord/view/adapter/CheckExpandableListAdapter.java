package com.gta.zssx.fun.coursedaily.registerrecord.view.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gta.zssx.R;
import com.gta.zssx.pub.util.LogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lan.zheng on 2016/6/24.
 */
public class CheckExpandableListAdapter extends BaseExpandableListAdapter {
//    private onCheckListener mListener;
    public int[] group_checked = new int[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
//    public static int[] group_checked = new int[] { 0, 0, 0, 0};
//    private String[] group_title_array = new String[] { "迟到人数", "请假人数","旷课人数","公假人数" };
    private String[] group_title_array2 = new String[] { "迟到人数", "旷课人数","请假人数","公假人数" };
    private List<String> groupArray;  //title
    private List<String> groupCount;  //是否有箭头的标志
    private List<List<String>> childArray;  //child
    Context mContext;
    // 一级标签上的状态图片数据源
   /* int[] group_state_array = new int[] { R.drawable.group_down,
            R.drawable.group_up };*/
    int[] group_state_array = new int[] { R.drawable.group_up1,
            R.drawable.group_down1 };

    /**
     * 上下箭头变换
     * @param groupPosition
     */
    public void updateChecked(int groupPosition) {
        group_checked[groupPosition] = group_checked[groupPosition]+1;
    }


    public CheckExpandableListAdapter(Context context,String[] late,String[] leave,String[] truant,String[] holiday){
        mContext = context;
//        mListener = listener;
        groupCount = new ArrayList<>();
        groupCount.add(""+late.length);
        groupCount.add(""+truant.length);
        groupCount.add(""+leave.length);
        groupCount.add(""+holiday.length);

        groupArray = new ArrayList<>();
        for(int index=0;index<group_title_array2.length;index++){
            groupArray.add(group_title_array2[index]);
        }

        childArray = new ArrayList<>();
        List<String> childItem1 =new ArrayList<>();
        List<String> childItem2 =new ArrayList<>();
        List<String> childItem3 =new ArrayList<>();
        List<String> childItem4 =new ArrayList<>();
        for(int i=0;i<late.length;i++){
            childItem1.add(late[i]);}
        for(int i=0;i<leave.length;i++){
            childItem2.add(leave[i]);}
        for(int i=0;i<truant.length;i++){
            childItem3.add(truant[i]);}
        for(int i=0;i<holiday.length;i++){
            childItem4.add(holiday[i]);}
        childArray.add(childItem1);
        childArray.add(childItem3);   //旷课，先显示
        childArray.add(childItem2);   //请假，后显示
        childArray.add(childItem4);
        LogUtil.Log("lenita","childArray.size = "+childArray.size()+" "+groupArray.size());
    }

    @Override
    public int getGroupCount() {
        return groupArray.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return childArray.get(groupPosition).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return getGroup(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return childArray.get(groupPosition).get(childPosition);
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
     * @param groupPosition
     * @param isExpanded
     * @param convertView
     * @param parent
     * @return
     */
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        // 为视图对象指定布局
        convertView = LinearLayout.inflate(mContext,
                R.layout.list_item_expand_group, null);

        RelativeLayout myLayout = (RelativeLayout) convertView
                .findViewById(R.id.group_layout);
        /*myLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.itemClick(group_checked);
            }
        });*/

        /**
         * 声明视图上要显示的控件
         */
        // 新建一个TextView对象，用来显示一级标签上的标题信息
        TextView group_title = (TextView) convertView
                .findViewById(R.id.group_title);
        // 新建一个TextView对象，用来显示一级标签上的大体描述的信息
        ImageView group_state = (ImageView) convertView
                .findViewById(R.id.group_state);
        TextView group_count = (TextView)convertView.findViewById(R.id.group_num) ;
        /**
         * 设置相应控件的内容
         */
        // 设置标题上的文本信息
        group_title.setText(groupArray.get(groupPosition));

        // 设置整体描述上的文本信息
        if (group_checked[groupPosition] % 2 == 1) {
            // 设置默认的图片是选中状态
            group_state.setBackgroundResource(group_state_array[1]);
            myLayout.setBackgroundResource(R.drawable.text_item_expandable_list_bg);
        } else {
            for (int test : group_checked) {
                if (test == 0 || test % 2 == 0) {

                    // 设置默认的图片是未选中状态
                    group_state.setBackgroundResource(group_state_array[0]);
                    myLayout.setBackgroundResource(R.drawable.text_item_top_bg);
                }
            }
        }

        //是否显示箭头和改变count
        if(groupCount.get(groupPosition).equals("0")){
            group_state.setVisibility(View.INVISIBLE);
            myLayout.setBackgroundResource(R.drawable.text_item_expandable_list_bg);
        }else {
            group_count.setText(groupCount.get(groupPosition));
        }

        // 返回一个布局对象
        return convertView;
    }

    /**
     * 对一级标签下的二级标签进行设置
     */
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        // 为视图对象指定布局
        convertView =  LinearLayout.inflate(mContext, R.layout.list_item_expand_group_child, null);
        /**
         * 声明视图上要显示的控件
         */
        // 新建一个TextView对象，用来显示具体内容
        TextView child_text = (TextView) convertView
                .findViewById(R.id.child_text);
        /**
         * 设置相应控件的内容
         */
        // 设置要显示的文本信息
        child_text.setText(childArray.get(groupPosition).get(childPosition));
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public interface Listener {
        void itemClick(int[] group_checked);
    }
}
