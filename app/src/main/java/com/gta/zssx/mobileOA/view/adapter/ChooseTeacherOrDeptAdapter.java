package com.gta.zssx.mobileOA.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.gta.zssx.R;
import com.gta.zssx.patrolclass.model.entity.ChooseTeacherEntity;

import java.util.List;

/**
 * 选择教师或部门
 */
public class ChooseTeacherOrDeptAdapter extends BaseExpandableListAdapter {
    private List<ChooseTeacherEntity> entities;
    private Context context;
    private int type;
    public static final int TYPE_TEACHER_SIGNAL =1;
    public static final int TYPE_TEACHER_MULTI =2;
    public static final int TYPE_DEPT_SIGNAL =3;
    public static final int TYPE_DEPT_MULTI =4;

    public ChooseTeacherOrDeptAdapter(Context context, int type) {
        this.context = context;
        this.type = type;
    }


    public void setEntities(List<ChooseTeacherEntity> entities) {
        this.entities = entities;
    }

    @Override
    public int getGroupCount() {
        return entities.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return entities.get(groupPosition).getDeptList().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return entities.get(groupPosition).getName();
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return entities.get(groupPosition).getDeptList().get(childPosition).getDeptName();
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_oa_choose_group, parent, false);
            groupViewHolder = new GroupViewHolder();
            groupViewHolder.mGroupName = (TextView) convertView.findViewById(R.id.tv_group);
            groupViewHolder.mGroupIcon = (ImageView) convertView.findViewById(R.id.iv_group);
            groupViewHolder.mChoose = (CheckBox)convertView.findViewById(R.id.cb_choose);
            convertView.setTag(groupViewHolder);
        } else {
            groupViewHolder = (GroupViewHolder) convertView.getTag();
        }
        if(type == TYPE_DEPT_MULTI ){
            groupViewHolder.mChoose.setVisibility(View.VISIBLE);
            groupViewHolder.mChoose.setFocusable(false);
            groupViewHolder.mChoose.setClickable(true);
        }else{
            groupViewHolder.mChoose.setVisibility(View.GONE);
        }
        groupViewHolder.mGroupName.setText(entities.get(groupPosition).getName());
        groupViewHolder.mGroupIcon.setImageResource(R.drawable.department);
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildViewHolder childViewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_oa_choose_child, parent, false);
            childViewHolder = new ChildViewHolder();
            childViewHolder.mChildName = (TextView) convertView.findViewById(R.id.tv_child);
            childViewHolder.mChildIcon = (ImageView) convertView.findViewById(R.id.iv_child);
            childViewHolder.mTeacherCode = (TextView) convertView.findViewById(R.id.tv_teacher_code);
            childViewHolder.mChoose = (CheckBox)convertView.findViewById(R.id.cb_choose);
            convertView.setTag(childViewHolder);
        } else {
            childViewHolder = (ChildViewHolder) convertView.getTag();
        }
        childViewHolder.mChildName.setText(entities.get(groupPosition).getDeptList().get(childPosition).getDeptName());
        int[] postions = {groupPosition, childPosition};
        if (entities.get(groupPosition).getDeptList().get(childPosition).getType() == 0) {
            childViewHolder.mTeacherCode.setVisibility(View.VISIBLE);
            childViewHolder.mTeacherCode.setText(entities.get(groupPosition).getDeptList().get(childPosition).getTeacherCode());
            childViewHolder.mChildIcon.setImageResource(R.drawable.search_user);
        } else {
            childViewHolder.mTeacherCode.setVisibility(View.GONE);
            childViewHolder.mChildIcon.setImageResource(R.drawable.department_1);
        }
        if(type == TYPE_TEACHER_MULTI || type == TYPE_DEPT_MULTI ){
            childViewHolder.mChoose.setVisibility(View.VISIBLE);
            childViewHolder.mChoose.setFocusable(false);
            childViewHolder.mChoose.setClickable(true);
        }else{
            childViewHolder.mChoose.setVisibility(View.GONE);
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
        CheckBox mChoose;
    }

    private class ChildViewHolder {
        ImageView mChildIcon;
        TextView mChildName;
        TextView mTeacherCode;
        CheckBox mChoose;
    }


}
