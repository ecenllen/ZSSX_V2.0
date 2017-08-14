package com.gta.zssx.patrolclass.view.adapter;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.gta.zssx.R;
import com.gta.zssx.patrolclass.model.entity.PatrolRegisterBeanNew;
import com.gta.zssx.patrolclass.view.page.DockScoreNewActivity;

import java.util.List;

/**
 * Created by liang.lu on 2017/3/23.
 */
public class AutoPatrolPointsDetailsFragmentAdapter extends
        BaseMultiItemQuickAdapter<PatrolRegisterBeanNew.TeacherListBean.DockScoreBean.OptionsBean, BaseViewHolder> {
    /**
     * status = 200,表示数据源是扣分详情页面点了保存后，传过来的，默认是 0
     */
    private int status;
    /*区分是否非自动计算*/
    private String autoScore;
    private List<PatrolRegisterBeanNew.TeacherListBean.DockScoreBean.OptionsBean> data;

    public AutoPatrolPointsDetailsFragmentAdapter (
            List<PatrolRegisterBeanNew.TeacherListBean.DockScoreBean.OptionsBean> data, int status,
            String autoScore) {
        super (data);
        this.data = data;
        this.status = status;
        this.autoScore = autoScore;
        addItemType (1, R.layout.adapter_patrol_point_title_item);
        addItemType (0, R.layout.adapter_patrol_point_options_item);
    }

    @Override
    protected void convert (BaseViewHolder baseViewHolder,
                            PatrolRegisterBeanNew.TeacherListBean.DockScoreBean.OptionsBean optionsBean) {
        switch (baseViewHolder.getItemViewType ()) {
            case 0:
                if (status == DockScoreNewActivity.STATUS) {
                    //如果是经过编辑保存过来的数据源的话要用optionBean.getInputScore(),这个分数
                    String score = "";
                    if (optionsBean.getInputScore () == null) {
                        score = optionsBean.getGetScore ().equals ("") ? optionsBean.getScore ()
                                : optionsBean.getGetScore ();
                    } else {
                        score = optionsBean.getInputScore ();
                    }
                    baseViewHolder.setText (R.id.tv_patrol_adapter_title, optionsBean.getTitle ())
                            .setText (R.id.tv_patrol_adapter_score, "-" + score);
                } else {
                    baseViewHolder.setText (R.id.tv_patrol_adapter_title, optionsBean.getTitle ())
                            .setText (R.id.tv_patrol_adapter_score, "-" + optionsBean.getGetScore ());
                }
                //item 下面的分割线，当该项的下一项是扣分标题的时候，也就是说是title_item时要变成铺满的分割线
                if (baseViewHolder.getLayoutPosition () != data.size () - 1
                        && data.get (baseViewHolder.getLayoutPosition () + 1).isIsTitle ()) {
                    baseViewHolder.setVisible (R.id.view_line_two, true);
                    baseViewHolder.setVisible (R.id.view_line, false);
                } else {
                    baseViewHolder.setVisible (R.id.view_line_two, false);
                    baseViewHolder.setVisible (R.id.view_line, true);
                }
                break;
            case 1:
                if (autoScore.equals (DockScoreNewActivity.UN_AUTO_SCORE)) {//非自动计算的情况
                    baseViewHolder.setText (R.id.tv_patrol_adapter_title, optionsBean.getTitle ())
                            .setText (R.id.tv_patrol_adapter_score, optionsBean.getInputScore ())
                            .setTextColor (R.id.tv_patrol_adapter_score, !optionsBean.getInputScore ()
                                    .equals (optionsBean.getAllScore ()) ? mContext.getResources ()
                                    .getColor (R.color.red) : mContext.getResources ()
                                    .getColor (R.color.textColor_def));
                } else {
                    baseViewHolder.setText (R.id.tv_patrol_adapter_title, optionsBean.getTitle ())
                            .setText (R.id.tv_patrol_adapter_score, optionsBean.getGetScore ())
                            .setTextColor (R.id.tv_patrol_adapter_score, !optionsBean.getGetScore ()
                                    .equals (optionsBean.getAllScore ()) ? mContext.getResources ()
                                    .getColor (R.color.red) : mContext.getResources ()
                                    .getColor (R.color.textColor_def));
                }

                break;
        }
    }
}
