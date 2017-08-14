package com.gta.zssx.patrolclass.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gta.zssx.R;
import com.gta.zssx.patrolclass.model.entity.DockScoreEntity;

import java.util.List;

/**
 * [Description]
 * <p>
 * [How to use]
 * <p>
 * [Tips] Created by tengfei.lv on 2016/7/25 16:56.
 */

public class ChooseSectionsAdapter extends RecyclerView.Adapter<ChooseSectionsAdapter.SectionViewHolder> {

    private List<DockScoreEntity.SectionsListBean> beanList;

    private Listener listener;

    public void setBeanList (List<DockScoreEntity.SectionsListBean> beanList) {
        this.beanList = beanList;
    }

    public void setListener (Listener listener) {
        this.listener = listener;
    }

    @Override
    public SectionViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {
        return SectionViewHolder.create (parent, new OnSectionItemListener () {
            @Override
            public void onItemClick (boolean isCheck, int position) {
                for (int i = 0; i < beanList.size (); i++) {
                    if (beanList.get (i).getType () == 0) {
                        beanList.get (i).setCheck (false);
                    }
                }
                if (beanList.get (position).getType () == 0) {
                    beanList.get (position).setCheck (isCheck);
                }
                notifyDataSetChanged ();
                listener.setEntities (beanList);
            }
        }, beanList);
    }

    @Override
    public void onBindViewHolder (SectionViewHolder holder, int position) {
        DockScoreEntity.SectionsListBean bean = beanList.get (position);
        holder.nameTv.setText (bean.getSectionName ());
        holder.mCheckBox.setChecked (bean.isCheck ());
        if (position == 0 || (position - 1 >= 0 && beanList.get (position - 1).getType () != bean.getType ())) {
            holder.headerLayout.setVisibility (View.VISIBLE);
            if (bean.getType () == 0) {
                holder.headerTv.setText ("未登记");
                holder.mCheckBox.setVisibility (View.VISIBLE);
            } else {
                holder.headerTv.setText ("已登记");
                holder.mCheckBox.setVisibility (View.INVISIBLE);
            }
        } else {
            holder.headerLayout.setVisibility (View.GONE);
            if (bean.getType () == 0) {
                holder.mCheckBox.setVisibility (View.VISIBLE);
            } else {
                holder.mCheckBox.setVisibility (View.INVISIBLE);
            }
        }
    }

    @Override
    public int getItemCount () {
        return beanList == null ? 0 : beanList.size ();
    }

    public static class SectionViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView headerTv;
        TextView nameTv;
        CheckBox mCheckBox;
        RelativeLayout headerLayout;
        private final RelativeLayout sectionLayout;
        OnSectionItemListener listener;
        private List<DockScoreEntity.SectionsListBean> beanList;

        public SectionViewHolder (View itemView, OnSectionItemListener listener, List<DockScoreEntity.SectionsListBean> beanList) {
            super (itemView);
            this.listener = listener;
            this.beanList = beanList;
            headerTv = (TextView) itemView.findViewById (R.id.section_choose_header_tv);
            nameTv = (TextView) itemView.findViewById (R.id.section_name_tv);
            mCheckBox = (CheckBox) itemView.findViewById (R.id.section_choose_checkbox);
            headerLayout = (RelativeLayout) itemView.findViewById (R.id.section_header_layout);

            sectionLayout = (RelativeLayout) itemView.findViewById (R.id.rl_section);

            sectionLayout.setOnClickListener (this);
        }

        public static SectionViewHolder create (ViewGroup parent, OnSectionItemListener listener, List<DockScoreEntity.SectionsListBean> beanList) {
            View view = LayoutInflater.from (parent.getContext ()).inflate (R.layout.
                    adapter_item_choose_section, parent, false);
            return new SectionViewHolder (view, listener, beanList);
        }

        @Override
        public void onClick (View v) {
            if (beanList.get (getLayoutPosition ()).getType () == 0) {
                mCheckBox.setChecked (!mCheckBox.isChecked ());
                if (listener != null) {
                    listener.onItemClick (mCheckBox.isChecked (), getLayoutPosition ());
                }
            }
        }
    }

    interface OnSectionItemListener {
        void onItemClick (boolean isCheck, int position);
    }

    public interface Listener {
        void setEntities (List<DockScoreEntity.SectionsListBean> sectionsListBeen);
    }
}
