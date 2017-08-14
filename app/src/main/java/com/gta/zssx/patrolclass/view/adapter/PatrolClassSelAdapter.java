package com.gta.zssx.patrolclass.view.adapter;

import android.content.Context;
import android.graphics.Point;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gta.zssx.R;
import com.gta.zssx.patrolclass.model.entity.PatrolClassSelEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liang.lu1 on 2016/7/13.
 */
public class PatrolClassSelAdapter extends RecyclerView.Adapter {

    private Context mContext;

    private List<PatrolClassSelEntity> mPatrolClassSelModels;
    /**
     * 扣分标题集合：教师分， 学生分， 卫生分。。。。。
     */
    private List<String> scoreTitle;
    private int Ttype;

    public PatrolClassSelAdapter (Context context, int type) {
        this.Ttype = type;
        this.mContext = context;

    }

    public void setmPatrolClassSelModels (List<PatrolClassSelEntity> mPatrolClassSelModels) {
        this.mPatrolClassSelModels = mPatrolClassSelModels;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {
        View view = LayoutInflater.from (parent.getContext ()).inflate (R.layout.item_patrol_class_sel, parent, false);
        PatrolClassSelHolder pHolder = new PatrolClassSelHolder (view);
        return pHolder;
    }

    @Override
    public void onBindViewHolder (RecyclerView.ViewHolder holder, final int position) {
        PatrolClassSelEntity model = mPatrolClassSelModels.get (position);
        String className = model.getClassName ();
        String depeName = model.getDeptName ();
        int status = model.getStatus ();
        String section = model.getSection ();
        String teacherName = model.getTeacher ();
        final PatrolClassSelHolder selHolder = (PatrolClassSelHolder) holder;
        selHolder.className.setText (className);
        selHolder.section.setText (section);
        selHolder.teacherName.setText (teacherName);
        if (Ttype == 1) {
            selHolder.titleType.setText ("状态：");
            switch (status) {
                case 0:
                    selHolder.status.setText ("未送审");
                    break;
                case 1:
                    selHolder.status.setText ("待审核");
                    break;
                case 2:
                    selHolder.status.setText ("未通过");
                    break;
                case 3:
                    selHolder.status.setText ("已通过");
                    break;
            }
        } else {
            selHolder.titleType.setText ("专业部：");
            selHolder.status.setText (depeName);
        }

        selHolder.createTitleScore (model);

    }

    @Override
    public int getItemCount () {
        if (mPatrolClassSelModels == null) {
            return 0;
        }
        return mPatrolClassSelModels.size ();
    }

    public class PatrolClassSelHolder extends RecyclerView.ViewHolder {

        /**
         * 班级名称
         */
        private TextView className;
        /**
         * 上课节次
         */
        private TextView section;
        /**
         * 上课教师
         */
        private TextView teacherName;
        /**
         * 状态
         */
        private TextView status, titleType;

        private GridLayout mGridLayout;
        private RelativeLayout relativeLayout;

        public PatrolClassSelHolder (View itemView) {
            super (itemView);

            teacherName = (TextView) itemView.findViewById (R.id.tv_teacher_name);
            className = (TextView) itemView.findViewById (R.id.tv_class_name3);
            section = (TextView) itemView.findViewById (R.id.tv_class_num2);
            status = (TextView) itemView.findViewById (R.id.tv_department);
            titleType = (TextView) itemView.findViewById (R.id.tv_class_departmen);
            mGridLayout = (GridLayout) itemView.findViewById (R.id.rl_patrol_sel);
            relativeLayout = (RelativeLayout) itemView.findViewById (R.id.ll_date);
        }

        private void createTitleScore (PatrolClassSelEntity entity) {

            mGridLayout.removeAllViews ();

            scoreTitle = new ArrayList<> ();
            for (int i = 0; i < entity.getGetScoreList ().size (); i++) {
                scoreTitle.add (entity.getGetScoreList ().get (i).getName ());
            }

            WindowManager windowManager = (WindowManager) mContext.getSystemService (Context.WINDOW_SERVICE);

            Point point = new Point ();
            windowManager.getDefaultDisplay ().getSize (point);
            int size = scoreTitle.size ();
            for (int i = 0; i < size; i++) {

                View view = LayoutInflater.from (mContext).inflate (R.layout.item_patrol_class, null);
                TextView tvTitleScore = (TextView) view.findViewById (R.id.tv_teacher_score);
                TextView tvScore = (TextView) view.findViewById (R.id.tv_teacher_score_num);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams (ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                if (size  == 1) {
                    //item指标的宽度占满一行
                    params.width = point.x - 44;
                    tvTitleScore.setMaxEms (10);
                } else if (size == 2) {
                    //item指标的宽度是总宽度1/2
                    params.width = (point.x - 44) / 2;
                    tvTitleScore.setMaxEms (6);
                } else {
                    params.width = (point.x - 44) / 3;
                    tvTitleScore.setMaxEms (4);
                }
                view.setLayoutParams (params);

                mGridLayout.addView (view);

                tvTitleScore.setText (scoreTitle.get (i));
                tvScore.setText ("：" + entity.getGetScoreList ().get (i).getScore ());
            }
        }

    }

}
