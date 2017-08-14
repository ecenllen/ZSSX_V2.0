package com.gta.zssx.patrolclass.view.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gta.zssx.R;
import com.gta.zssx.patrolclass.model.entity.PatrolRegisterBeanNew;

import java.util.List;

/**
 * Created by liang.lu1 on 2016/7/14.
 */
public class PatrolRegisterPointsAdapter extends RecyclerView.Adapter {
    private List<PatrolRegisterBeanNew.TeacherListBean.DockScoreBean.OptionsBean> models;

    public PatrolRegisterPointsAdapter (List<PatrolRegisterBeanNew.TeacherListBean.DockScoreBean.OptionsBean> bean) {
        this.models = bean;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {
        View view = LayoutInflater.from (parent.getContext ()).inflate (R.layout.item_see_points, parent, false);
        SeePointsHolder holder = new SeePointsHolder (view);
        return holder;
    }

    @Override
    public void onBindViewHolder (RecyclerView.ViewHolder holder, int position) {
        SeePointsHolder holder1 = (SeePointsHolder) holder;
        PatrolRegisterBeanNew.TeacherListBean.DockScoreBean.OptionsBean bean = models.get (position);
        String content = bean.getTitle ();
        String points = bean.getGetScore ();
        if (position == models.size () - 1) {
            holder1.tvContent.setText (content);
            holder1.tvPoints.setText ("-" + points);
            holder1.tv_line.setVisibility (View.GONE);
        } else {
            holder1.tvContent.setText (content);
            holder1.tvPoints.setText ("-" + points);
            holder1.tv_line.setVisibility (View.VISIBLE);
        }


    }

    public class SeePointsHolder extends RecyclerView.ViewHolder {
        private TextView tvContent;
        private View tv_line;
        private TextView tvPoints;

        public SeePointsHolder (View itemView) {
            super (itemView);
            tvContent = (TextView) itemView.findViewById (R.id.tv_see_points);
            tvPoints = (TextView) itemView.findViewById (R.id.tv_see_score);
            tv_line = itemView.findViewById (R.id.view_line);
        }
    }

    @Override
    public int getItemCount () {
        return models.size ();
    }
}
