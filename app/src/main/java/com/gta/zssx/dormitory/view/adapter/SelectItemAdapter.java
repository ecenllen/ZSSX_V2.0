package com.gta.zssx.dormitory.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.gta.zssx.R;
import com.gta.zssx.dormitory.model.bean.ItemInfoBean;

import java.util.ArrayList;
import java.util.List;

/**
 * [Description]
 * 新增录入 - 指标项选择
 * [How to use]
 *
 * [Tips]
 *
 * Created by lan.zheng on 2017/7/25 11:32.
 */


public class SelectItemAdapter extends BaseExpandableListAdapter {
    private List<ItemInfoBean> entities = new ArrayList<>();
    private Context context;
    private int selectedItemId;

    public SelectItemAdapter(Context context) {
        this.context = context;
    }


    public void setEntities(List<ItemInfoBean> entities,int selectedItemId) {
        this.entities.clear();
        this.entities.addAll(entities);
        this.selectedItemId = selectedItemId;
        notifyDataSetChanged();
    }

    @Override
    public int getGroupCount() {
        return entities == null ? 0 : entities.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return entities.get(groupPosition).getLevelList() == null ? 0 : entities.get(groupPosition).getLevelList() .size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return entities.get(groupPosition).getName();
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return entities.get(groupPosition).getLevelList() .get(childPosition).getItemName();
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

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupViewHolder groupViewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_choose_teacher_expandable_head, parent, false);
            groupViewHolder = new GroupViewHolder();
            groupViewHolder.mGroupName = (TextView) convertView.findViewById(R.id.tv_group);
            groupViewHolder.mGroupIcon = (ImageView) convertView.findViewById(R.id.iv_group);
            convertView.setTag(groupViewHolder);
        } else {
            groupViewHolder = (GroupViewHolder) convertView.getTag();
        }
        groupViewHolder.mGroupName.setText(entities.get(groupPosition).getName());
        groupViewHolder.mGroupIcon.setImageResource(R.drawable.department);
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildViewHolder childViewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item_dormitory_item_expandable_child, parent, false);
            childViewHolder = new ChildViewHolder();
            childViewHolder.mChildName = (TextView) convertView.findViewById(R.id.tv_child);
            childViewHolder.mChildIcon = (ImageView) convertView.findViewById(R.id.iv_child);
            childViewHolder.mTeacherCode = (TextView) convertView.findViewById(R.id.tv_teacher_code);
            childViewHolder.mSelectIcon = (ImageView)convertView.findViewById(R.id.iv_selected_icon);
            convertView.setTag(childViewHolder);
        } else {
            childViewHolder = (ChildViewHolder) convertView.getTag();
        }
        childViewHolder.mChildName.setText(entities.get(groupPosition).getLevelList().get(childPosition).getItemName());
        int[] postions = {groupPosition, childPosition};
        if (entities.get(groupPosition).getLevelList().get(childPosition).getType() == 0) {
            int itemId = entities.get(groupPosition).getLevelList().get(childPosition).getItemId();
//            childViewHolder.mTeacherCode.setVisibility(View.VISIBLE);
//            childViewHolder.mTeacherCode.setText(entities.get(groupPosition).getLevelList().get(childPosition).getItemId());
            childViewHolder.mChildIcon.setImageResource(R.drawable.ic_item_project2);
            if(selectedItemId != -1 && selectedItemId == itemId){
                childViewHolder.mSelectIcon.setVisibility(View.VISIBLE);
            }else {
                childViewHolder.mSelectIcon.setVisibility(View.INVISIBLE);
            }
        } else {
            childViewHolder.mChildIcon.setImageResource(R.drawable.department_1);
        }
        return convertView;
    }



    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    private class GroupViewHolder {
        ImageView mGroupIcon;
        TextView mGroupName;
    }

    private class ChildViewHolder {
        ImageView mChildIcon;
        TextView mChildName;
        TextView mTeacherCode;
        ImageView mSelectIcon;
    }


}
