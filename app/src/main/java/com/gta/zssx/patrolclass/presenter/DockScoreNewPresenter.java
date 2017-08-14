package com.gta.zssx.patrolclass.presenter;

import com.gta.utils.mvp.BasePresenter;
import com.gta.zssx.patrolclass.model.entity.PatrolRegisterBeanNew;
import com.gta.zssx.patrolclass.view.DockScoreNewView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by liang.lu on 2017/3/9 17:01.
 */

public class DockScoreNewPresenter extends BasePresenter<DockScoreNewView> {
    private PatrolRegisterBeanNew.TeacherListBean teacherListBean = new PatrolRegisterBeanNew.TeacherListBean ();
    private List<PatrolRegisterBeanNew.TeacherListBean.DockScoreBean> dockScoreBeanList = new ArrayList<> ();
    private PatrolRegisterBeanNew.TeacherListBean.DockScoreBean dockScoreBean;
    private List<PatrolRegisterBeanNew.TeacherListBean.DockScoreBean.OptionsBean> optionsBeanList;
    private int count;

    /**
     * 重新生成一个 PatrolRegisterBeanNew.TeacherListBean teacherListBean 对象
     *
     * @param data
     * @param teacherId
     * @param teacherName
     * @param autoScore
     */
    public void resumeData (List<PatrolRegisterBeanNew.TeacherListBean.DockScoreBean.OptionsBean> data
            , String teacherId, String teacherName, String autoScore, Map<Integer, String> map) {
        count = 0;
        for (int i = 0; i < data.size (); i++) {
            if (data.get (i).isIsTitle ()) {
                if (i > 0) {
                    dockScoreBean.setOptions (optionsBeanList);
                    dockScoreBeanList.add (dockScoreBean);
                }
                dockScoreBean = new PatrolRegisterBeanNew.TeacherListBean.DockScoreBean ();
                optionsBeanList = new ArrayList<> ();
                dockScoreBean.setAllScore (data.get (i).getAllScore ());
                dockScoreBean.setUnAutoScore (map.get (count));
                dockScoreBean.setGetAllScore (data.get (i).getGetScore ());
                dockScoreBean.setId (data.get (i).getId ());
                dockScoreBean.setName (data.get (i).getTitle ());
                count++;
            } else {
                optionsBeanList.add (data.get (i));
                if (i == data.size () - 1) {
                    dockScoreBean.setOptions (optionsBeanList);
                    dockScoreBeanList.add (dockScoreBean);
                }
            }
        }
        teacherListBean.setDockScore (dockScoreBeanList);
        teacherListBean.setTeacherId (teacherId);
        teacherListBean.setTeacherName (teacherName);
        teacherListBean.setAutoScore (autoScore);
        teacherListBean.setOld (true);
    }

    /**
     * 自动计算得分时，判断是佛有选项为""
     *
     * @param data
     * @return 只有当所有勾选项非空串时（！""），返回true 否则返回 false
     */
    public boolean isAllValues (List<PatrolRegisterBeanNew.TeacherListBean.DockScoreBean.OptionsBean> data) {
        for (int i = 0; i < data.size (); i++) {
            if (data.get (i).isIsCheck () && data.get (i).getInputScore () != null && data.get (i).isInputNull ()) {
                return false;
            }
        }
        return true;
    }

    public PatrolRegisterBeanNew.TeacherListBean getTeacherListBean () {
        return teacherListBean;
    }
}
