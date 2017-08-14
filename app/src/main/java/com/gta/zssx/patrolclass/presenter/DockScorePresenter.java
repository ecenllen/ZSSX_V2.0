package com.gta.zssx.patrolclass.presenter;

import com.gta.utils.mvp.BasePresenter;
import com.gta.zssx.patrolclass.model.entity.DockScoreEntity;
import com.gta.zssx.patrolclass.model.entity.SubmitRegisterPatrol;
import com.gta.zssx.patrolclass.view.DockScoreView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * [Description]
 * <p>
 * [How to use]
 * <p>
 * [Tips]
 *
 * @author Created by Zhimin.Huang on 2016/9/1.
 * @since 1.0.0
 */
public class DockScorePresenter extends BasePresenter<DockScoreView> {

    private int j;
    private int allScore;

    public List<Map<String, String>> getResultMap () {
        return resultMap;
    }

    private List<Map<String, String>> resultMap;

    public List<SubmitRegisterPatrol.JsonBean.GetScoreListBean> getGetScoreListBeens () {
        return getScoreListBeens;
    }

    private List<SubmitRegisterPatrol.JsonBean.GetScoreListBean> getScoreListBeens;

    public boolean calculateScore (DockScoreEntity dockScoreEntity1, List<DockScoreEntity.DockScoreBean.OptionsBean> optionsBeenList) {

        j = 0;
        allScore = 0;
        resultMap = new ArrayList<> ();
        getScoreListBeens = new ArrayList<> ();

        for (int i = 0; i < optionsBeenList.size (); i++) {
            DockScoreEntity.DockScoreBean.OptionsBean bean = optionsBeenList.get (i);
            if (bean.isTitle ()) {
                SubmitRegisterPatrol.JsonBean.GetScoreListBean getScoreListBean = new SubmitRegisterPatrol.JsonBean.GetScoreListBean ();
                getScoreListBean.setName (bean.getTitle ());
                getScoreListBean.setResult (new ArrayList<> ());
                getScoreListBean.setId (dockScoreEntity1.getDockScore ().get (j).getId ());
                Map<String, String> maps = new HashMap<> ();
                maps.put ("title", bean.getTitle ());
                if (resultMap.size () == 0) {
                } else {
                    if (allScore > Integer.parseInt (dockScoreEntity1.getDockScore ().get (j - 1).getAllScore ())) {
                        return false;
                    }
                    putResultValue (dockScoreEntity1);

                }
                resultMap.add (maps);
                getScoreListBeens.add (getScoreListBean);
                j++;
            } else {
                if (bean.isCheck ()) {
                    SubmitRegisterPatrol.JsonBean.GetScoreListBean.ResultBean resultBean = new SubmitRegisterPatrol.JsonBean.GetScoreListBean.ResultBean ();
                    resultBean.setId (bean.getItemId ());
                    if (bean.getChangeScoer () == null) {
                        resultBean.setScore (Integer.parseInt (bean.getGetScore ()));
                    } else if (bean.getChangeScoer ().length () == 0) {
                        resultBean.setScore (Integer.parseInt (bean.getScore ()));
                    } else {
                        resultBean.setScore (Integer.parseInt (bean.getChangeScoer ()));
                    }

                    if (bean.getChangeScoer () != null && bean.getChangeScoer ().equals ("")) {
                        return false;
                    }
                    String score = "";
                    if (bean.getChangeScoer () == null) {
                        score = bean.getGetScore ();
                    } else if (bean.getChangeScoer ().length () == 0) {
                        score = bean.getScore ();
                    } else {
                        score = bean.getChangeScoer ();
                    }

                    Integer getScore = Integer.valueOf (score);
                    allScore += getScore;

                    SubmitRegisterPatrol.JsonBean.GetScoreListBean getScoreListBean =
                            getScoreListBeens.get (getScoreListBeens.size () - 1);
                    getScoreListBean.getResult ().add (resultBean);

                }
            }
        }
        if (allScore > Integer.parseInt (dockScoreEntity1.getDockScore ().get (j - 1).getAllScore ())) {
            return false;
        }
        putResultValue (dockScoreEntity1);
        return true;
    }

    /**
     * 填充map值
     */
    private void putResultValue (DockScoreEntity dockScoreEntity1) {
        Map<String, String> stringMap = resultMap.get (resultMap.size () - 1);
        String lScore = Integer.parseInt (dockScoreEntity1.getDockScore ().get (j - 1).getAllScore ()) - allScore + "";
        stringMap.put ("score", lScore);
        SubmitRegisterPatrol.JsonBean.GetScoreListBean getScoreListBean =
                getScoreListBeens.get (getScoreListBeens.size () - 1);
        getScoreListBean.setGetScore (Integer.parseInt (lScore));
        allScore = 0;
    }
}
