package com.gta.zssx.patrolclass.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.gta.zssx.R;
import com.gta.zssx.patrolclass.model.entity.ClassChooseEntity;

import java.util.List;

/**
 * [Description] <p/>  添加按计划巡课班级adapter [How to use]
 * <p/>
 * [Tips] Created by tengfei.lv on 2016/7/18 13:28.
 */

public class AddClassChooseAdapter extends RecyclerView.Adapter {

    private List<ClassChooseEntity.ClassListBean> datas;
    private String classType;

    public AddClassChooseAdapter (String classType) {
        this.classType = classType;
    }

    private AddClassItemClicklistener itemListener;

    //随机巡课页面中，item点击事件监听器
    public void setItemListener (AddClassItemClicklistener itemListener) {
        this.itemListener = itemListener;
    }

    public void setDatas (List<ClassChooseEntity.ClassListBean> datas) {
        this.datas = datas;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {
        if (viewType == 0) {  // 系部
            return DeptViewHolder.create (parent, new AddClassItemClicklistener () {
                @Override
                public void onClick (int type, int position) {
                    if (classType.equals ("random")) {   //随机巡课，不处理该事件
                        return;
                    }
                    datas.get (position).setCheck (!datas.get (position).isCheck ());
                    int flag = position + 1;
                    for (int i = flag; i < datas.size (); i++) {
                        if (datas.get (i).getType () == 0) {   //如果到达下一个系部，就返回
                            notifyDataSetChanged ();
                            return;
                        }
                        datas.get (i).setCheck (datas.get (position).isCheck ());
                    }
                    //如果是最后一个系部，就需要更新一次
                    notifyDataSetChanged ();
                }
            });
        } else {
            return ClassViewHolder.create (parent, new AddClassItemClicklistener () {
                @Override
                public void onClick (int type, int position) {
                    if (classType.equals ("random")) {   //如果是随机巡课，直接让activity处理
                        itemListener.onClick (type, position);
                        return;
                    }
                    datas.get (position).setCheck (!datas.get (position).isCheck ());

                    int flag = position;
                    boolean isAllCheck = true;    //如果为true，就设置系部为选中
                    for (; flag >= 0 && datas.get (flag).getType () == 1; flag--) {
                        if (!datas.get (flag).isCheck ()) {    //如果有一个选项没有选择，就改为false
                            isAllCheck = false;
                            break;
                        }
                    }
                    if (isAllCheck) {    //如果position之前的都是选中状态，就判断posiiton之后的

                        flag = position + 1;
                        for (; flag < datas.size () && datas.get (flag).getType () == 1; flag++) {
                            if (!datas.get (flag).isCheck ()) {    //如果有一个选项没有选择，就改为false
                                isAllCheck = false;
                                break;
                            }
                        }
                    }
                    for (int i = position - 1; i >= 0; i--) {
                        if (datas.get (i).getType () == 0) {
                            datas.get (i).setCheck (isAllCheck);
                            notifyItemChanged (i);
                            break;
                        }
                    }
                }
            }, classType);
        }
    }

    @Override
    public void onBindViewHolder (RecyclerView.ViewHolder holder, int position) {
        ClassChooseEntity.ClassListBean bean = datas.get (position);

        if (holder instanceof DeptViewHolder) {
            ((DeptViewHolder) holder).deptNameTV.setText (bean.getDeptName ());
            ((DeptViewHolder) holder).checkBoxAll.setChecked (bean.isCheck ());
            if (classType.equals ("random")) {
                ((DeptViewHolder) holder).checkBoxAll.setVisibility (View.GONE);
            }
        } else if (holder instanceof ClassViewHolder) {
            ((ClassViewHolder) holder).classNameTv.setText (bean.getClassName ());
            ((ClassViewHolder) holder).checkBox.setChecked (bean.isCheck ());
            if (classType.equals ("random")) {
                ((ClassViewHolder) holder).checkBox.setVisibility (View.GONE);
                ((ClassViewHolder) holder).rightArrowIv.setVisibility (View.VISIBLE);
            } else {
                ((ClassViewHolder) holder).rightArrowIv.setVisibility (View.GONE);
            }
        }
    }

    @Override
    public int getItemCount () {
        return datas == null ? 0 : datas.size ();
    }

    @Override
    public int getItemViewType (int position) {
        return datas.get (position).getType ();
    }

    private static class ClassViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final CheckBox checkBox;
        private final TextView classNameTv;
        private AddClassItemClicklistener clicklistener;
        private final ImageView rightArrowIv;

        public ClassViewHolder (View itemView, AddClassItemClicklistener clicklistener) {
            super (itemView);
            this.clicklistener = clicklistener;
            checkBox = (CheckBox) itemView.findViewById (R.id.cb_add_class);
            classNameTv = (TextView) itemView.findViewById (R.id.tv_add_class);
            rightArrowIv = (ImageView) itemView.findViewById (R.id.iv_right_arrow_add_class);

            itemView.setOnClickListener (this);
        }

        private static ClassViewHolder create (ViewGroup parent, AddClassItemClicklistener clicklistener, String classType) {
            int id;
            if (classType.equals ("random")) {
                id = R.layout.adapter_item_add_class_chooes_class2;
            } else {
                id = R.layout.adapter_item_add_class_chooes_class;
            }
            View view = LayoutInflater.from (parent.getContext ()).inflate (id
                    , parent, false);
            return new ClassViewHolder (view, clicklistener);
        }

        @Override
        public void onClick (View v) {
            checkBox.setChecked (!checkBox.isChecked ());
            if (clicklistener != null) {
                clicklistener.onClick (1, getLayoutPosition ());
            }
        }
    }

    private static class DeptViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final CheckBox checkBoxAll;
        private final TextView deptNameTV;
        private AddClassItemClicklistener clicklistener;

        public DeptViewHolder (View itemView, AddClassItemClicklistener clicklistener) {
            super (itemView);

            this.clicklistener = clicklistener;
            checkBoxAll = (CheckBox) itemView.findViewById (R.id.checkbox_all);
            deptNameTV = (TextView) itemView.findViewById (R.id.section_header_tv);

            itemView.setOnClickListener (this);
        }

        private static DeptViewHolder create (ViewGroup parent, AddClassItemClicklistener clicklistener) {
            View view = LayoutInflater.from (parent.getContext ()).inflate (R.layout.
                    adapter_item_add_class_choose_dept, parent, false);
            return new DeptViewHolder (view, clicklistener);
        }

        @Override
        public void onClick (View v) {
            checkBoxAll.setChecked (!checkBoxAll.isChecked ());
            if (clicklistener != null) {
                clicklistener.onClick (0, getLayoutPosition ());
            }
        }
    }

    public interface AddClassItemClicklistener {
        void onClick (int type, int position);
    }
}
